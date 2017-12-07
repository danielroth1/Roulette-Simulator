package main;

import java.util.LinkedList;
import java.util.List;

public class Game {
	
	public final static int MAX_COUNT_GAMES_REACHED = 0;
	
	public final static int NO_MORE_CREDITS = 1;
	
	
	private int startCredit;
	
	private int credit;
	
	private int numberOfGames;
	
	private double winningProbability = (double)18/(double)37;
	
	private GameStructure lastGame;
	
	private List<GameStructure> stats = new LinkedList<GameStructure>();
	
	private boolean captureStats = true;
	
	private int gamesEndReason = -1;
	
	public Game(int credit) {
		super();
		this.credit = credit;
		this.startCredit = credit;
	}


	public boolean playGame(int stake){
		int creditsBefore = credit;
		boolean won;
		double n = Math.random();
		if (n <= winningProbability){
			//game is won
			won = true;
			credit += stake;
		}
		else{
			//game is lost
			credit -= stake;
			won = false;
		}
		lastGame = new GameStructure(numberOfGames, credit, creditsBefore, won, stake);
		if (captureStats)
			stats.add(lastGame);
		numberOfGames++;
		return won;
	}
	
	@Override
	public String toString(){
		String s = "";
		
		for (GameStructure g : stats){
			s = s.concat(g.toString().concat("\n"));
		}
		return s;
	}

	
	
	

	public int getGamesEndReason() {
		return gamesEndReason;
	}


	public void setGamesEndReason(int gamesEndReason) {
		this.gamesEndReason = gamesEndReason;
	}


	public boolean isCaptureStats() {
		return captureStats;
	}


	public void setCaptureStats(boolean captureStats) {
		this.captureStats = captureStats;
	}


	public int getStartCredit() {
		return startCredit;
	}


	public int getCredit() {
		return credit;
	}


	public int getNumberOfGames() {
		return numberOfGames;
	}


	public double getWinningProbability() {
		return winningProbability;
	}


	public List<GameStructure> getStats() {
		return stats;
	}


	public void setWinningProbability(double winningProbability) {
		this.winningProbability = winningProbability;
	}


	public GameStructure getLastGame() {
		return lastGame;
	}
	
	
	

}
