import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {

	private KdTreeNode root;
	private int size;
	
	public KdTree() {
	}

	public boolean isEmpty() {
		return this.root == null;
	}

	public int size() {
		return this.size;
	}

	public void insert(Point2D p) {
		if (p == null) throw new IllegalArgumentException();
		if (this.root == null) {
			this.root = new KdTreeNode(p, true);
			++this.size;
		} else {
			if (this.root.insert(p)) ++this.size;
		}
	}

	public boolean contains(Point2D p) {
		if (p == null) throw new IllegalArgumentException();
		if (this.isEmpty()) return false;
		return this.root.contains(p);
	}

	public void draw() {
		if (this.root != null) {
			this.root.draw();
		}
	}

	public Iterable<Point2D> range(RectHV rect) {
		if (rect == null) throw new IllegalArgumentException();
		if (this.root == null) return new ArrayList<Point2D>();
		return this.root.rangeSearch(rect);
	}

	public Point2D nearest(Point2D p) {
		if (p == null) throw new IllegalArgumentException();
		if (this.root == null) return null;
		return this.root.nearestNeighbor(p);
	}

	public static void main(String[] args) {
		KdTree tree = new KdTree();
		tree.insert(new Point2D(0.7, 0.2));
		tree.insert(new Point2D(0.5, 0.4));
		tree.insert(new Point2D(0.2, 0.3));
		tree.insert(new Point2D(0.4, 0.7));
		tree.insert(new Point2D(0.9, 0.6));
		System.out.println(tree.contains(new Point2D(0.2, 0.3)));
	}
}

class KdTreeNode {
	
	private Point2D point;
	private boolean isXAxis;
	private KdTreeNode leftChild;
	private KdTreeNode rightChild;
	
	public KdTreeNode(Point2D point, boolean isXAxis) {
		this.point = point;
		this.isXAxis = isXAxis;
	}
	
	public boolean insert(Point2D point) {
		if (this.point.equals(point)) return false;
		boolean isLeft = this.isToTheLeft(point);
		if (isLeft) {
			if (this.leftChild == null) {
				this.leftChild = new KdTreeNode(point, !this.isXAxis);
				return true;
			} else {
				return this.leftChild.insert(point);
			}
		} else {
			if (this.rightChild == null) {
				this.rightChild = new KdTreeNode(point, !this.isXAxis);
				return true;
			} else {
				return this.rightChild.insert(point);
			}
		}
	}
	
	public boolean contains(Point2D point) {
		if (this.point.equals(point)) return true;
		boolean isLeft = this.isToTheLeft(point);
		if (isLeft && this.leftChild != null) return this.leftChild.contains(point);
		else if (!isLeft && this.rightChild != null) return this.rightChild.contains(point);
		return false;
	}
	
	public void draw() {
		this.draw(new RectHV(0, 0, 1, 1));
	}
	
	public Point2D nearestNeighbor(Point2D point) {
		return this.nearestNeighbor(point, Double.MAX_VALUE, null, new RectHV(0, 0, 1, 1)).second;
	}
	
	public List<Point2D> rangeSearch(RectHV rangeRect) {
		List<Point2D> intersections = new ArrayList<>();
		this.rangeSearch(rangeRect, new RectHV(0, 0, 1, 1), intersections);
		return intersections;
	}
	
	private void draw(RectHV currentRect) {
		Pair<RectHV, RectHV> boundaries = this.getBoundaryRects(currentRect, this.point.x(), this.point.y());
		if (this.leftChild != null) this.leftChild.draw(boundaries.first);
		if (this.rightChild != null) this.rightChild.draw(boundaries.second);
		if (this.isXAxis) {
			StdDraw.setPenColor(StdDraw.RED);
		} else {
			StdDraw.setPenColor(StdDraw.BLUE);
		}
		boundaries.first.draw();
		boundaries.second.draw();
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.setPenRadius(StdDraw.getPenRadius() * 10);
		this.point.draw();
		StdDraw.setPenRadius(StdDraw.getPenRadius() / 10);
	}
	
	private boolean isToTheLeft(Point2D point) {
		return this.isXAxis ? point.x() < this.point.x() : point.y() < this.point.y();
	}
	
	private void rangeSearch(RectHV rangeRect, RectHV currentRect, List<Point2D> intersections) {
		Pair<RectHV, RectHV> boundaries = this.getBoundaryRects(currentRect, this.point.x(), this.point.y());
		RectHV leftRect = boundaries.first;
		RectHV rightRect = boundaries.second;
		
		if (rangeRect.contains(this.point)) {
			intersections.add(this.point);
		}
		
		if (rangeRect.intersects(leftRect) && this.leftChild != null) {
			this.leftChild.rangeSearch(rangeRect, leftRect, intersections);
		}
		
		if (rangeRect.intersects(rightRect) && this.rightChild != null) {
			this.rightChild.rangeSearch(rangeRect, rightRect, intersections);
		}
	}
	
	private Pair<Double, Point2D> nearestNeighbor(Point2D point, double closestDist, Point2D closestPoint, RectHV currentRect) {
		Pair<RectHV, RectHV> boundaries = this.getBoundaryRects(currentRect, this.point.x(), this.point.y());
		RectHV leftRect = boundaries.first;
		RectHV rightRect = boundaries.second;
		
		if (this.point.distanceTo(point) < closestDist) {
			closestDist = this.point.distanceTo(point);
			closestPoint = this.point;
		}
		
		double distLeftRect = leftRect.distanceTo(point);
		double distRightRect = rightRect.distanceTo(point);
		if (distLeftRect < distRightRect) {
			if (distLeftRect < closestDist && this.leftChild != null) {
				Pair<Double, Point2D> result = this.leftChild.nearestNeighbor(point, closestDist, closestPoint, leftRect);
				closestDist = result.first;
				closestPoint = result.second;
			}
			
			if (distRightRect < closestDist && this.rightChild != null) {
				Pair<Double, Point2D> result = this.rightChild.nearestNeighbor(point, closestDist, closestPoint, rightRect);
				closestDist = result.first;
				closestPoint = result.second;
			}
		} else {
			if (distRightRect < closestDist && this.rightChild != null) {
				Pair<Double, Point2D> result = this.rightChild.nearestNeighbor(point, closestDist, closestPoint, rightRect);
				closestDist = result.first;
				closestPoint = result.second;
			}
			
			if (distLeftRect < closestDist && this.leftChild != null) {
				Pair<Double, Point2D> result = this.leftChild.nearestNeighbor(point, closestDist, closestPoint, leftRect);
				closestDist = result.first;
				closestPoint = result.second;
			}
		}
		
		
		return new Pair<Double, Point2D>(closestDist, closestPoint);
	}
	
	private Pair<RectHV, RectHV> getBoundaryRects(RectHV currentRect, double currentX, double currentY) {
		RectHV leftRect, rightRect;
		if (this.isXAxis) {
			leftRect = new RectHV(currentRect.xmin(), currentRect.ymin(), currentX, currentRect.ymax());
			rightRect = new RectHV(currentX, currentRect.ymin(), currentRect.xmax(), currentRect.ymax());
		} else {
			leftRect = new RectHV(currentRect.xmin(), currentRect.ymin(), currentRect.xmax(), currentY);
			rightRect = new RectHV(currentRect.xmin(), currentY, currentRect.xmax(), currentRect.ymax());
		}
		
		return new Pair<RectHV, RectHV>(leftRect, rightRect);
	}
}

class Pair<K, V> {
	public K first;
	public V second;
	public Pair(K first, V second) {
		this.first = first;
		this.second = second;
	}
}