import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Stack;

public class SeamCarver {

	private Picture picture;
	private double[][] energyPicture;
	private int[] minPathVertical;
	private int[] minPathHorizontal;
	
   // create a seam carver object based on the given picture
   public SeamCarver(Picture picture) {
	   if (picture == null) throw new IllegalArgumentException();
	   this.picture = createCopy(picture);
	   this.energyPicture = calculateEnergy(picture);
	   this.minPathVertical = this.calculateMinVerticalSeam();
	   this.minPathHorizontal = this.calculateMinHorizontalSeam();
   }

   // current picture
   public Picture picture() {
	   return createCopy(this.picture);
   }

   // width of current picture
   public int width() {
	   return this.picture.width();
   }

   // height of current picture
   public int height() {
	   return this.picture.height();
   }

   // energy of pixel at column x and row y
   public double energy(int x, int y) {
	   if (x < 0 || x >= this.picture.width() || y < 0 || y >= this.picture.height()) throw new IllegalArgumentException();
	   return this.energyPicture[x][y];
   }

   // sequence of indices for horizontal seam
   public int[] findHorizontalSeam() {
	   return this.minPathHorizontal;
   }

   // sequence of indices for vertical seam
   public int[] findVerticalSeam() {
	   return this.minPathVertical;
   }

   // remove horizontal seam from current picture
   public void removeHorizontalSeam(int[] seam) {
	   if (seam == null || seam.length != this.picture.width() || this.picture.height() <= 1) throw new IllegalArgumentException();
	   for (int i = 0; i < seam.length; i++) {
		   if (seam[i] < 0 || seam[i] >= this.picture.height() || (i > 0 && Math.abs(seam[i] - seam[i - 1]) > 1)) throw new IllegalArgumentException();
	   }
	   
	   Picture newPicture = new Picture(this.picture.width(), this.picture.height() - 1);
	   for (int i = 0; i < this.picture.width(); i++) {
		   for (int j = 0; j < this.picture.height(); j++) {
			   if (j < seam[i]) {
				   newPicture.set(i, j, picture.get(i, j));
			   } else if (j > seam[i]) {
				   newPicture.set(i, j - 1, picture.get(i, j));
			   }
		   }
	   }
	   
	   this.picture = newPicture;
	   this.energyPicture = calculateEnergy(newPicture);
	   this.minPathVertical = this.calculateMinVerticalSeam();
	   this.minPathHorizontal = this.calculateMinHorizontalSeam();
   }

   // remove vertical seam from current picture
   public void removeVerticalSeam(int[] seam) {
	   if (seam == null || seam.length != this.picture.height() || this.picture.width() <= 1) throw new IllegalArgumentException();
	   for (int i = 0; i < seam.length; i++) {
		   if (seam[i] < 0 || seam[i] >= this.picture.width() || (i > 0 && Math.abs(seam[i] - seam[i - 1]) > 1)) throw new IllegalArgumentException();
	   }
	   
	   Picture newPicture = new Picture(this.picture.width() - 1, this.picture.height());
	   for (int j = 0; j < this.picture.height(); j++) {
		   for (int i = 0; i < this.picture.width(); i++) {
			   if (i < seam[j]) {
				   newPicture.set(i, j, picture.get(i, j));
			   } else if (i > seam[j]) {
				   newPicture.set(i - 1, j, picture.get(i, j));
			   }
		   }
	   }
	   
	   this.picture = newPicture;
	   this.energyPicture = calculateEnergy(newPicture);
	   this.minPathVertical = this.calculateMinVerticalSeam();
	   this.minPathHorizontal = this.calculateMinHorizontalSeam();
   }

   private int getIndex(int col, int row) {
	   return col + row * this.picture.width();
   }
   
   private int[] calculateMinHorizontalSeam() {
	   Graph graph = new Graph(this.picture.width() * this.picture.height());
	   for (int j = 0; j < this.picture.height(); j++) {
		   for (int i = 0; i < this.picture.width() - 1; i++) {
			   graph.addEdge(new DirectedEdge(getIndex(i, j), getIndex(i + 1, j), this.energyPicture[i + 1][j]));
			   if (j > 0) graph.addEdge(new DirectedEdge(getIndex(i, j), getIndex(i + 1, j - 1), this.energyPicture[i + 1][j - 1]));
			   if (j < this.picture.height() - 1) graph.addEdge(new DirectedEdge(getIndex(i, j), getIndex(i + 1, j + 1), this.energyPicture[i + 1][j + 1]));
		   }
	   }

	   double minDist = Double.POSITIVE_INFINITY;
	   Iterable<Integer> path = null;
	   List<Integer> sources = new ArrayList<>();
	   for (int i = 0; i < this.picture.height(); i++) {
		   sources.add(getIndex(0, i));
	   }
	   
	   Dijkstra dijkstra = new Dijkstra(graph, sources);
	   for (int i = 0; i < this.picture.height(); i++) {
		   double dist = dijkstra.distTo(getIndex(this.picture.width() - 1, i));
		   if (dist < minDist) {
			   minDist = dist;
			   path = dijkstra.pathTo(getIndex(this.picture.width() - 1, i));
		   }
	   }
	   
	   int[] fixedPath = new int[this.picture.width()];
	   int count = 0;
	   for (int index : path) {
		   fixedPath[count++] = index / this.picture.width();
	   }
	   
	   return fixedPath;
   }
   
   private int[] calculateMinVerticalSeam() {
	   Graph graph = new Graph(this.picture.width() * this.picture.height());
	   for (int i = 0; i < this.picture.width(); i++) {
		   for (int j = 0; j < this.picture.height() - 1; j++) {
			   graph.addEdge(new DirectedEdge(getIndex(i, j), getIndex(i, j + 1), this.energyPicture[i][j + 1]));
			   if (i > 0) graph.addEdge(new DirectedEdge(getIndex(i, j), getIndex(i - 1, j + 1), this.energyPicture[i - 1][j + 1]));
			   if (i < this.picture.width() - 1) graph.addEdge(new DirectedEdge(getIndex(i, j), getIndex(i + 1, j + 1), this.energyPicture[i + 1][j + 1]));
		   }
	   }
	   
	   double minDist = Double.POSITIVE_INFINITY;
	   Iterable<Integer> path = null;
	   List<Integer> sources = new ArrayList<>();
	   for (int i = 0; i < this.picture.width(); i++) {
		   sources.add(getIndex(i, 0));
	   }
	   
	   Dijkstra dijkstra = new Dijkstra(graph, sources);
	   for (int i = 0; i < this.picture.width(); i++) {
		   double dist = dijkstra.distTo(getIndex(i, this.picture.height() - 1));
		   if (dist < minDist) {
			   minDist = dist;
			   path = dijkstra.pathTo(getIndex(i, this.picture.height() - 1));
		   }
	   }
	   
	   int[] fixedPath = new int[this.picture.height()];
	   int count = 0;
	   for (int index : path) {
		   fixedPath[count++] = index % this.picture.width();
	   }
	   
	   return fixedPath;
   }
   
   //  unit testing (optional)
   public static void main(String[] args) {
	   Picture picture = new Picture("C:\\Users\\abner\\OneDrive\\Documents\\Java Projects\\UnionFind\\src\\HJoceanSmall.png");
	   SeamCarver carver = new SeamCarver(picture);
	   for (int i = 0; i < 50; i++) {
		   carver.removeVerticalSeam(carver.findVerticalSeam());
		   System.out.println(carver.picture().width() + " " + carver.picture().height());
	   }
	   
	   carver.picture().save("C:\\Users\\abner\\OneDrive\\Documents\\Java Projects\\UnionFind\\src\\output.png");
   }

   private static Picture createCopy(Picture picture) {
	   Picture another = new Picture(picture.width(), picture.height());
	   for (int i = 0; i < picture.width(); i++) {
		   for (int j = 0; j < picture.height(); j++) {
			   another.setRGB(i, j, picture.getRGB(i, j));
		   }
	   }
	   
	   return another;
   }
   
   private static double[][] calculateEnergy(Picture input) {
	   double[][] energyPicture = new double[input.width()][input.height()];
	   for (int i = 0; i < input.width(); i++) {
		   for (int j = 0; j < input.height(); j++) {
			   if (i == 0 || j == 0 || i == input.width() - 1 || j == input.height() - 1) {
				   energyPicture[i][j] = 1000;
			   } else {
				   double diffX = calculateDiff(input, i - 1, j, i + 1, j);
				   double diffY = calculateDiff(input, i, j - 1, i, j + 1);
				   energyPicture[i][j] = Math.sqrt(diffX + diffY);
			   }
		   }
	   }
	   
	   return energyPicture;
   }
   
   private static double calculateDiff(Picture input, int i1, int j1, int i2, int j2) {
	   return Math.pow(input.get(i2, j2).getRed() - input.get(i1, j1).getRed(), 2)
			   + Math.pow(input.get(i2, j2).getGreen() - input.get(i1, j1).getGreen(), 2)
			   + Math.pow(input.get(i2, j2).getBlue() - input.get(i1, j1).getBlue(), 2);
   }
}

class Graph {
	
	private List<DirectedEdge>[] edges;
	
	public Graph(int V) {
		this.edges = new List[V];
		for (int i = 0; i < V; i++) {
			this.edges[i] = new ArrayList<>();
		}
	}
	
	public void addEdge(DirectedEdge e) {
		this.edges[e.from()].add(e);
	}
	
	public List<DirectedEdge> adj(int v) {
		return this.edges[v];
	}
	
	public int V() {
		return this.edges.length;
	}
}

class MinPQNode implements Comparable<MinPQNode> {
	
	public int vertex;
	public double weight;
	
	public MinPQNode(int vertex, double weight) {
		this.vertex = vertex;
		this.weight = weight;
	}

	@Override
	public int compareTo(MinPQNode o) {
		if (this.weight < o.weight) return -1;
		else if (this.weight > o.weight) return 1;
		return 0;
	}
}

class Dijkstra {
	
	private double[] dists;
	private int[] reachableFrom;
	private MinPQ<MinPQNode> pq;
	
	public Dijkstra(Graph g, List<Integer> sources) {
		this.dists = new double[g.V()];
		this.reachableFrom = new int[g.V()];
		for (int i = 0; i < g.V(); i++) {
			this.reachableFrom[i] = -1;
			this.dists[i] = Double.POSITIVE_INFINITY;
		}

		this.pq = new MinPQ<MinPQNode>(g.V());
		for (int source : sources) {
			this.dists[source] = 0;
			this.pq.insert(new MinPQNode(source, 0));
		}
		
		boolean[] visited = new boolean[g.V()];
		int countVisited = 0;
		while (!this.pq.isEmpty() && countVisited < g.V()) {
			int v = this.pq.delMin().vertex;
			if (visited[v]) continue;
			visited[v] = true;
			++countVisited;
			for (DirectedEdge e : g.adj(v)) {
				if (!visited[e.to()]) {
					relax(e);
				}
			}
		}
	}
	
	public double distTo(int v) {
		return this.dists[v];
	}
	
	public Iterable<Integer> pathTo(int v) {
		Stack<Integer> path = new Stack<>();
		while (v != -1) {
			path.push(v);
			v = this.reachableFrom[v];
		}
		
		return path;
	}
	
	private void relax(DirectedEdge e) {
		if (this.dists[e.to()] > this.dists[e.from()] + e.weight()) {
			this.dists[e.to()] = this.dists[e.from()] + e.weight();
			this.reachableFrom[e.to()] = e.from();
			this.pq.insert(new MinPQNode(e.to(), this.dists[e.to()]));
		}
	}
}