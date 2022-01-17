import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class PointSET {

	private TreeSet<Point2D> points;
	
	public PointSET() {
		this.points = new TreeSet<>();
	}

	public boolean isEmpty() {
		return this.points.isEmpty();
	}

	public int size() {
		return this.points.size();
	}

	public void insert(Point2D p) {
		if (p == null) throw new IllegalArgumentException();
		this.points.add(p);
	}

	public boolean contains(Point2D p) {
		if (p == null) throw new IllegalArgumentException();
		return this.points.contains(p);
	}

	public void draw() {
		for (Point2D point : this.points) {
			point.draw();
		}
	}

	public Iterable<Point2D> range(RectHV rect) {
		if (rect == null) throw new IllegalArgumentException();
		List<Point2D> pointsWithinRect = new ArrayList<>();
		for (Point2D point : this.points) {
			if (rect.contains(point)) {
				pointsWithinRect.add(point);
			}
		}
		
		return pointsWithinRect;
	}

	public Point2D nearest(Point2D p) {
		if (p == null) throw new IllegalArgumentException();
		double nearestDist = Double.MAX_VALUE;
		Point2D nearestPoint = null;
		for (Point2D point : this.points) {
			double dist = point.distanceTo(p);
			if (dist < nearestDist) {
				nearestDist = dist;
				nearestPoint = point;
			}
		}
		
		return nearestPoint;
	}

	public static void main(String[] args) {

	}
}