package main;

public class GameStructure {
	
	public int number;
	
	public int creditsAfter;
	
	public int creditsBefore;
	
	public boolean won;
	
	public int stake;
	
	public GameStructure() {
		
	}

	public GameStructure(int number, int creditsAfter, int creditsBefore,
			boolean won, int stake) {
		super();
		this.number = number;
		this.creditsAfter = creditsAfter;
		this.creditsBefore = creditsBefore;
		this.won = won;
		this.stake = stake;
	}
	
	

	@Override
	public String toString() {
		return "GameStructure [number=" + number + ", creditsAfter="
				+ creditsAfter + ", won="
				+ won + ", stake=" + stake + "]";
	}

	public int getNumber() {
		return number;
	}

	public int getCreditsAfter() {
		return creditsAfter;
	}

	public int getCreditsBefore() {
		return creditsBefore;
	}

	public boolean isWon() {
		return won;
	}

	public int getStake() {
		return stake;
	}
	
	

}
