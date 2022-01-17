import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
   
   private WordNet wordnet;
   
   public Outcast(WordNet wordnet) {
	   this.wordnet = wordnet;
   }
   
   public String outcast(String[] nouns) {
	   int maxDistance = 0;
	   int outcast = 0;
	   for (int i = 0; i < nouns.length; i++) {
		   int distance = 0;
		   for (int j = 0; j < nouns.length; j++) {
			   distance += this.wordnet.distance(nouns[i], nouns[j]);
		   }
		   
		   if (distance > maxDistance) {
			   maxDistance = distance;
			   outcast = i;
		   }
	   }
	   
	   return nouns[outcast];
   }
   
   public static void main(String[] args) {
	    WordNet wordnet = new WordNet(args[0], args[1]);
	    Outcast outcast = new Outcast(wordnet);
	    for (int t = 2; t < args.length; t++) {
	        In in = new In(args[t]);
	        String[] nouns = in.readAllStrings();
	        StdOut.println(args[t] + ": " + outcast.outcast(nouns));
	    }
   }
}