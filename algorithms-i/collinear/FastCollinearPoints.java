import java.util.Arrays;

public class FastCollinearPoints {
	
   private LineSegment[] segments;
   private int numSegments;
   
   public FastCollinearPoints(Point[] points) {
	   if (points == null) throw new IllegalArgumentException();
	   for (int i = 0; i < points.length; i++) {
		   if (points[i] == null) throw new IllegalArgumentException();
		   for (int j = i + 1; j < points.length; j++) {
			   if (points[j] == null || points[i].compareTo(points[j]) == 0) throw new IllegalArgumentException();
		   }
	   }
	   
	   this.segments = new LineSegment[points.length];
	   Arrays.sort(points);
	   for (int i = 0; i < points.length; i++) {
		   Point[] candidates = new Point[points.length - i - 1];
		   for (int j = i + 1, k = 0; j < points.length; j++, k++) {
			   candidates[k] = points[j];
		   }
		   
		   Arrays.sort(candidates, points[i].slopeOrder());
		   double[] slopes = new double[candidates.length];
		   for (int j = 0; j < candidates.length; j++) {
			   slopes[j] = points[i].slopeTo(candidates[j]);
		   }
		   
		   int count = 1;
		   for (int j = 1; j < slopes.length; j++) {
			   if (slopes[j] == slopes[j - 1]) {
				   ++count;
			   } else {
				   if (count >= 3) this.addLineSegment(points[i], candidates[j - 1]);
				   count = 1;
			   }
		   }
		   
		   if (count >= 3) this.addLineSegment(points[i], candidates[candidates.length - 1]);
	   }
   }
   
   private void addLineSegment(Point p, Point s) {
	   LineSegment segment = new LineSegment(p, s);
	   this.segments[this.numSegments++] = segment;
   }
   
   public int numberOfSegments() {
	   return this.numSegments;
   }
   
   public LineSegment[] segments() {
	   LineSegment[] segments = new LineSegment[this.numSegments];
	   for (var i = 0; i < this.numSegments; i++) segments[i] = this.segments[i];
	   return segments;
   }
}