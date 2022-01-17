import java.util.Arrays;

public class BruteCollinearPoints {
	
   private LineSegment[] segments;
   private int numSegments;
   
   public BruteCollinearPoints(Point[] points) {
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
		   for (int j = i + 1; j < points.length; j++) {
			   for (int m = j + 1; m < points.length; m++) {
				   for (int n = m + 1; n < points.length; n++) {
					   Point p = points[i];
					   Point q = points[j];
					   Point r = points[m];
					   Point s = points[n];
					   double slopeq = p.slopeTo(q);
					   double sloper = p.slopeTo(r);
					   double slopes = p.slopeTo(s);
					   if (slopeq == sloper && slopeq == slopes) {
						   this.segments[this.numSegments++] = new LineSegment(p, s);
					   }
				   }
			   }
		   }
	   }
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