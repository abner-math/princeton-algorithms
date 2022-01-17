import java.util.HashSet;
import java.util.Set;

import edu.princeton.cs.algs4.Digraph;

public class DigraphDAGChecker {

	private Digraph graph;
	private boolean isRootedDAG;
	
	public DigraphDAGChecker(Digraph graph) {
		this.graph = graph;
		this.isRootedDAG = !this.hasCycles() && this.isRooted();
	}
	
	public boolean isRootedDAG() {
		return this.isRootedDAG;
	}
	
	private boolean hasCycle(int v, boolean[] visited, boolean[] marked) {
		if (visited[v]) return false;
		marked[v] = true;
		visited[v] = true;
		for (int w : this.graph.adj(v)) {
			if (marked[w]) {
				return true;
			} else if (hasCycle(w, visited, marked)) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean hasCycles() {
		boolean[] visited = new boolean[this.graph.V()];
		for (int i = 0; i < graph.V(); i++) {
			if (visited[i]) {
				continue;
			} else if (this.hasCycle(i, visited, new boolean[this.graph.V()])) {
				return true;
			}
		}
		
		return false;
	}
	
	private void findRoots(int v, boolean[] visited, Set<Integer> roots) {
		visited[v] = true;
		boolean hasEdges = false;
		for (int w : this.graph.adj(v)) {
			hasEdges = true;
			if (visited[w]) {
				this.findRoots(w, visited, roots);
			}
		}
		
		if (!hasEdges) roots.add(v);
	}
	
	private boolean isRooted() {
		Set<Integer> roots = new HashSet<>();
		boolean[] visited = new boolean[this.graph.V()];
		for (int i = 0; i < this.graph.V(); i++) {
			if (visited[i]) {
				continue;
			}
			
			this.findRoots(i, visited, roots);
			if (roots.size() > 1) {
				return false;
			}
		}
		
		return true;
	}
}
