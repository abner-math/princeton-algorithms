import java.util.Arrays;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {

   private Digraph graph;
   
   // constructor takes a digraph (not necessarily a DAG)
   public SAP(Digraph G) {
	   if (G == null) throw new IllegalArgumentException();
	   this.graph = G;
   }

   // length of shortest ancestral path between v and w; -1 if no such path
   public int length(int v, int w) {
	   this.validateVertex(v);
	   this.validateVertex(w);
	   return this.length(Arrays.asList(v), Arrays.asList(w));
   }

   // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
   public int ancestor(int v, int w) {
	   this.validateVertex(v);
	   this.validateVertex(w);
	   return this.ancestor(Arrays.asList(v), Arrays.asList(w));
   }

   // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
   public int length(Iterable<Integer> v, Iterable<Integer> w) {
	   for (int vertex : v) this.validateVertex(vertex);
	   for (int vertex : w) this.validateVertex(vertex);
	   return this.findLeastCommonAncestor(v, w).u;
   }

   // a common ancestor that participates in shortest ancestral path; -1 if no such path
   public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
	   for (int vertex : v) this.validateVertex(vertex);
	   for (int vertex : w) this.validateVertex(vertex);
	   return this.findLeastCommonAncestor(v, w).t;
   }

   private Pair<Integer, Integer> findLeastCommonAncestor(Iterable<Integer> v, Iterable<Integer> w) {
	   boolean[] markedFromV = new boolean[this.graph.V()];
	   int[] lengthFromV = new int[this.graph.V()];
	   Queue<Integer> queueV = new Queue<>();
	   for (int vertex : v) {
		   markedFromV[vertex] = true;
		   lengthFromV[vertex] = 0;
		   queueV.enqueue(vertex);
	   }

	   boolean[] markedFromW = new boolean[this.graph.V()];
	   int[] lengthFromW = new int[this.graph.V()];
	   Queue<Integer> queueW = new Queue<>();
	   for (int vertex : w) {
		   markedFromW[vertex] = true;
		   lengthFromW[vertex] = 0;
		   queueW.enqueue(vertex);
	   }

	   int ancestor = -1;
	   int minLength = -1;
	   while (!queueV.isEmpty() || !queueW.isEmpty()) {
		   if (!queueV.isEmpty()) {
			   int top = queueV.dequeue();
			   int length = lengthFromV[top] + lengthFromW[top];
			   if (markedFromW[top] && (minLength == -1 || length < minLength)) {
				   ancestor = top;
				   minLength = length;
			   }
			   
			   if (minLength == -1 || lengthFromV[top] < minLength) {
				   for (int vertex : this.graph.adj(top)) {
					   if (!markedFromV[vertex]) {
						   markedFromV[vertex] = true;
						   lengthFromV[vertex] = lengthFromV[top] + 1;
						   queueV.enqueue(vertex);
					   }
				   }
			   }
		   }
		   
		   if (!queueW.isEmpty()) {
			   int top = queueW.dequeue();
			   int length = lengthFromV[top] + lengthFromW[top];
			   if (markedFromV[top] && (minLength == -1 || length < minLength)) {
				   ancestor = top;
				   minLength = length;
			   }
			   
			   if (minLength == -1 || lengthFromW[top] < minLength) {
				   for (int vertex : this.graph.adj(top)) {
					   if (!markedFromW[vertex]) {
						   markedFromW[vertex] = true;
						   lengthFromW[vertex] = lengthFromW[top] + 1;
						   queueW.enqueue(vertex);
					   }
				   }
			   }
		   }
	   }
	   
	   return new Pair<Integer, Integer>(ancestor, minLength);
   }
   
   private void validateVertex(int v) {
	   if (v < 0 || v >= this.graph.V()) throw new IllegalArgumentException();
   }
   
   // do unit testing of this class
   public static void main(String[] args) {
	   In in = new In(args[0]);
	    Digraph G = new Digraph(in);
	    SAP sap = new SAP(G);
	    while (!StdIn.isEmpty()) {
	        int v = StdIn.readInt();
	        int w = StdIn.readInt();
	        int length   = sap.length(v, w);
	        int ancestor = sap.ancestor(v, w);
	        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
	    }
   }
}

class Pair<T, U> {         
    public final T t;
    public final U u;

    public Pair(T t, U u) {         
        this.t= t;
        this.u= u;
     }
 }
