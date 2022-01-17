import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BaseballElimination {
	
	private Map<String, Integer> teams;
	private int[] wins;
	private int[] losses;
	private int[] remaining;
	private int[][] games;
	private String[] names;
	private boolean[] eliminated;
	private List<String>[] certificates;
	
	public BaseballElimination(String filename) {
		In in = new In(filename);
		int numTeams = in.readInt();
		this.teams = new HashMap<>();
		this.wins = new int[numTeams];
		this.losses = new int[numTeams];
		this.remaining = new int[numTeams];
		this.games = new int[numTeams][numTeams];
		this.names = new String[numTeams];
		this.eliminated = new boolean[numTeams];
		this.certificates = new List[numTeams];
		for (int i = 0; i < numTeams; i++) {
			this.names[i] = in.readString();
			this.teams.put(this.names[i], i);
			this.wins[i] = in.readInt();
			this.losses[i] = in.readInt();
			this.remaining[i] = in.readInt();
			for (int j = 0; j < numTeams; j++) {
				this.games[i][j] = in.readInt();
			}
		}
		
		for (int i = 0; i < numTeams; i++) {
			this.certificates[i] = new ArrayList<>();
			this.eliminated[i] = this.isTeamEliminated(i, this.certificates[i]);
		}
	}
	
	public int numberOfTeams() {
		return this.teams.size();
	}
	
	public Iterable<String> teams() {
		return this.teams.keySet();
	}
	
	public int wins(String team) {
		if (!this.teams.containsKey(team)) throw new IllegalArgumentException();
		return this.wins[this.teams.get(team)];
	}
	
	public int losses(String team) {
		if (!this.teams.containsKey(team)) throw new IllegalArgumentException();
		return this.losses[this.teams.get(team)];
	}
	
	public int remaining(String team) {
		if (!this.teams.containsKey(team)) throw new IllegalArgumentException();
		return this.remaining[this.teams.get(team)];
	}
	
	public int against(String team1, String team2) {
		if (!this.teams.containsKey(team1) || !this.teams.containsKey(team2)) throw new IllegalArgumentException();
		return this.games[this.teams.get(team1)][this.teams.get(team2)];
	}
	
	public boolean isEliminated(String team) {
		if (!this.teams.containsKey(team)) throw new IllegalArgumentException();
		return this.eliminated[this.teams.get(team)];
	}
	
	public Iterable<String> certificateOfElimination(String team) {
		if (!this.teams.containsKey(team)) throw new IllegalArgumentException();
		return this.certificates[this.teams.get(team)];
	}
	
	private FlowNetwork createNetwork(int team, int[] teamVertices) {
		List<Game> games = this.gamesWithoutTeam(team);
		for (int i = 0, count = 0; i < this.teams.size(); i++) {
			if (i == team) continue;
			teamVertices[i] = 1 + games.size() + count++;
		}
		
		int numVertices = games.size() + this.teams.size() - 1 + 2;
		FlowNetwork network = new FlowNetwork(numVertices);
		int sIndex = 0;
		int tIndex = numVertices - 1;
		for (int i = 0; i < games.size(); i++) {
			Game game = games.get(i);
			int gameIndex = 1 + i;
			network.addEdge(new FlowEdge(sIndex, gameIndex, game.games));
			network.addEdge(new FlowEdge(gameIndex, teamVertices[game.i], Double.POSITIVE_INFINITY));
			network.addEdge(new FlowEdge(gameIndex, teamVertices[game.j], Double.POSITIVE_INFINITY));
		}
		
		for (int i = 0; i < this.teams.size(); i++) {
			if (i == team) continue;
			network.addEdge(new FlowEdge(teamVertices[i], tIndex, Math.max(0, this.wins[team] + this.remaining[team] - this.wins[i])));
		}
		
		return network;
	}
	
	private List<Game> gamesWithoutTeam(int team) {
		List<Game> newGames = new ArrayList<>();
		for (int i = 0; i < this.teams.size(); i++) {
			for (int j = i + 1; j < this.teams.size(); j++) {
				if (i == team || j == team) continue;
				newGames.add(new Game(i, j, this.games[i][j]));
			}
		}
		
		return newGames;
	}
	
	private int numGamesLeft(int team) {
		List<Game> games = this.gamesWithoutTeam(team);
		int count = 0;
		for (Game game : games) {
			count += game.games;
		}
		
		return count;
	}
	
	private boolean isTeamEliminated(int team, List<String> certificateElimination) {
		boolean isEliminated = false;
		for (int i = 0; i < this.teams.size(); i++) {
			if (this.wins[team] + this.remaining[team] < this.wins[i]) {
				certificateElimination.add(this.names[i]);
				isEliminated = true;
			}
		}
		
		if (isEliminated) return true;
		
		int[] teamVertices = new int[this.teams.size()];
		FlowNetwork network = this.createNetwork(team, teamVertices);
		FordFulkerson ff = new FordFulkerson(network, 0, network.V() - 1);
		if (this.numGamesLeft(team) > ff.value()) {
			for (int i = 0; i < this.teams.size(); i++) {
				if (i == team) continue;
				if (ff.inCut(teamVertices[i])) {
					certificateElimination.add(this.names[i]);
				}
			}
			
			return true;
		}
		
		return false;
	}
	
	public static void main(String[] args) {
		BaseballElimination division = new BaseballElimination("C:\\Users\\abner\\OneDrive\\Documents\\Java Projects\\UnionFind\\src\\teams5.txt");
	    for (String team : division.teams()) {
	        if (division.isEliminated(team)) {
	            StdOut.print(team + " is eliminated by the subset R = { ");
	            for (String t : division.certificateOfElimination(team)) {
	                StdOut.print(t + " ");
	            }
	            StdOut.println("}");
	        }
	        else {
	            StdOut.println(team + " is not eliminated");
	        }
	    }
	}
}

class Game {
	
	public int games;
	public int i;
	public int j;
	
	public Game(int i, int j, int games) {
		this.i = i;
		this.j = j;
		this.games = games;
	}
}
