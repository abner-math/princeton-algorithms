import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Topological;

public class WordNet {

   private Digraph graph;
   private String[] synsets;
   private Map<String, Set<Integer>> synsetIndices;
   private Set<String> nouns;
   private SAP sap;
   
   // constructor takes the name of the two input files
   public WordNet(String synsets, String hypernyms) {
	   if (synsets == null || hypernyms == null) throw new IllegalArgumentException();
	   this.synsets = this.readSynsets(synsets);
	   this.graph = this.readHypernyms(hypernyms);
	   this.sap = new SAP(this.graph);
	   this.nouns = new HashSet<>();
	   this.synsetIndices = new HashMap<>();
	   for (int i = 0; i < this.synsets.length; i++) {
		   List<String> nouns = Arrays.asList(this.synsets[i].split(" "));
		   this.nouns.addAll(nouns);
		   for (String noun : nouns) {
			   if (this.synsetIndices.containsKey(noun)) {
				   this.synsetIndices.get(noun).add(i);
			   } else {
				   this.synsetIndices.put(noun, new HashSet<>(Arrays.asList(i)));
			   }
		   }
	   }
	   
	   Topological topo = new Topological(this.graph);
       if (!topo.hasOrder()) throw new IllegalArgumentException("is not a DAG");
   }

   // returns all WordNet nouns
   public Iterable<String> nouns() {
	   return this.nouns();
   }

   // is the word a WordNet noun?
   public boolean isNoun(String word) {
	   if (word == null) throw new IllegalArgumentException();
	   return this.nouns.contains(word);
   }

   // distance between nounA and nounB (defined below)
   public int distance(String nounA, String nounB) {
	   if (nounA == null || nounB == null || !this.isNoun(nounA) || !this.isNoun(nounB)) throw new IllegalArgumentException();
	   return this.sap.length(this.synsetIndices.get(nounA), this.synsetIndices.get(nounB));
   }

   // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
   // in a shortest ancestral path (defined below)
   public String sap(String nounA, String nounB) {
	   if (!this.isNoun(nounA) || !this.isNoun(nounB)) throw new IllegalArgumentException();
	   int ancestor = this.sap.ancestor(this.synsetIndices.get(nounA), this.synsetIndices.get(nounB));
	   return this.synsets[ancestor];
   }
   
   private String[] readSynsets(String synsets) {
	   String[] lines = (new In(synsets)).readAllLines();
	   String[] nouns = new String[lines.length];
	   for (int i = 0; i < lines.length; i++) {
		   String[] values = lines[i].split(",");
		   nouns[Integer.parseInt(values[0])] = values[1];
	   }
	   
	   return nouns;
   }
   
   private Digraph readHypernyms(String hypernyms) {
	   String[] lines = (new In(hypernyms)).readAllLines();
	   Digraph graph = new Digraph(lines.length);
	   for (int i = 0; i < lines.length; i++) {
		   String[] values = lines[i].split(",");
		   for (int j = 1; j < values.length; j++) {
			   graph.addEdge(Integer.parseInt(values[0]), Integer.parseInt(values[j]));
		   }
	   }
	   
	   return graph;
   }

   // do unit testing of this class
   public static void main(String[] args) {
	   
   }
}