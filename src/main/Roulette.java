package main;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Roulette {

	private JFrame frame = new JFrame("Roulett Simulator");
	private JPanel loadPanel = new JPanel();
	private JTextArea loadLabel = new JTextArea("Ready.");
	private int loadingPrecentage = 0;
	
	public String playGameUntil(Game g, int minStack, int maxGames, boolean onlyGameEndReason, final boolean updateLoadingBar){
		double time = System.currentTimeMillis();
		String s = "";
		String gameEndReason = "";
		int stake = minStack;
		int loadingPrecentageBefore = 0;
		for (int i = 0; i < maxGames; i++){
			stake = Math.min(stake, g.getCredit());
			if (g.playGame(stake)){
				//won
				stake = minStack;
			}
			else{
				//lost
				stake *= 2;
			}
			if (g.getCredit()<= 0){
				//no more credits
				gameEndReason = "no more credits";
				g.setGamesEndReason(Game.NO_MORE_CREDITS);
				break;
			}
			if (i == maxGames-1){
				g.setGamesEndReason(Game.MAX_COUNT_GAMES_REACHED);
				gameEndReason = "maximum count games reached";
			}
			
			if (updateLoadingBar){
				loadingPrecentage = (int)(((double)(i+1)/maxGames)*100);
				if (loadingPrecentage != loadingPrecentageBefore){
					loadingPrecentageBefore = loadingPrecentage;
					SwingUtilities.invokeLater(new Runnable() {
						
						@Override
						public void run() {
							loadLabel.setText("");
							loadLabel.insert("Loading..." + loadingPrecentage + "%", 0);
							
						}
					});
				}
			}
			
		}
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				if (updateLoadingBar)
					loadLabel.setText("Ready.");
				
			}
		});
		
		if (onlyGameEndReason){
			return gameEndReason;
		}
		System.out.println("ready");
		s = s.concat(g.toString());
		s = s.concat("\ngame ended cause of: " + gameEndReason);
		s = s.concat("\ncalculationTime = " + (System.currentTimeMillis()-time)/1000 + " s");
		System.out.println(s);
		return s;
	}
	
	public String playGames(int numberOfGames, int minStack, int maxGames, int credits, boolean extensiveStats){
		double time = System.currentTimeMillis();
		
		String leftAlignFormatDouble = "| %-52s | %-16f |%n";
		String leftAlignFormatInteger = "| %-52s | %-16d |%n";
		
		String s = "";
		int gameEndsNoMoreCredits = 0;
		int gameEndsMaxGameCountReached = 0;
		int moneyWon = 0;
		int moneyWonAfterSuccessfullGames = 0;
		int gamesUntilFinish = 0;
		int gamesUntilFinishNoMoreCredits = 0;
		
		int loadingPrecentageBefore = 0;
		
		for (int i = 0; i < numberOfGames; i++){
			Game g = new Game(credits);
			String gameEndReason = playGameUntil(g, minStack, maxGames, true, false);
			
			g.setCaptureStats(false);
			//stats
			moneyWon += (g.getLastGame().creditsAfter-credits);
			if (g.getGamesEndReason() == Game.NO_MORE_CREDITS){
				gameEndsNoMoreCredits++;
				gamesUntilFinishNoMoreCredits+=g.getNumberOfGames();
			}
			else{
				gameEndsMaxGameCountReached++;
				moneyWonAfterSuccessfullGames += g.getLastGame().creditsAfter-credits;
			}
			gamesUntilFinish += g.getNumberOfGames();
			
			
			if (extensiveStats)
				s = s + g.getLastGame().toString() + ", game end reason: " + gameEndReason + "\n";
			
			loadingPrecentage = (int)(((double)(i+1)/numberOfGames)*100);
			if (loadingPrecentage != loadingPrecentageBefore){
				loadingPrecentageBefore = loadingPrecentage;
				SwingUtilities.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						loadLabel.setText("");
						loadLabel.insert("Loading..." + loadingPrecentage + "%", 0);
						
					}
				});
			}
			
		}
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				loadLabel.setText("Ready.");
				
			}
		});
		
		double moneyWonAverage = (double)moneyWon/numberOfGames;
		double moneyWonAfterSuccessfullGamesAverage = (double)moneyWonAfterSuccessfullGames/numberOfGames;
		double gamesUntilFinishAverage = (double)gamesUntilFinish/numberOfGames;
		double gamesUntilFinishOfAllUnfinishedGames = (double)gamesUntilFinishNoMoreCredits/gameEndsNoMoreCredits;
		double winningProbabilityInPercentage = ((double)gameEndsMaxGameCountReached/numberOfGames)*100;
		/*
		System.out.format("+------------------------------------------------------+------------------+%n");
		System.out.format(leftAlignFormatInteger, "number of games played", numberOfGames);
		System.out.format(leftAlignFormatInteger, "maximum games played", maxGames);
		System.out.format(leftAlignFormatInteger, "starting credits", credits);
		System.out.format("+------------------------------------------------------+------------------+%n");
		System.out.format(leftAlignFormatDouble, "money won average", moneyWonAverage);
		System.out.format(leftAlignFormatDouble, "money won average(after " + gameEndsMaxGameCountReached + " successfull games)", moneyWonAfterSuccessfullGamesAverage);
		System.out.format(leftAlignFormatInteger, "games(no more credits)", gameEndsNoMoreCredits);
		System.out.format(leftAlignFormatInteger, "games(max games count reached)", gameEndsMaxGameCountReached);
		System.out.format(leftAlignFormatDouble, "winning probability(%)", winningProbabilityInPercentage);
		System.out.format(leftAlignFormatDouble, "games until finish(average)", gamesUntilFinishAverage);
		System.out.format(leftAlignFormatDouble, "games until finish of all unfinished games(average)", gamesUntilFinishOfAllUnfinishedGames);
		System.out.format("+------------------------------------------------------+------------------+%n");
		*/
		s += s.format("+------------------------------------------------------+------------------+%n");
		s += s.format(leftAlignFormatInteger, "number of games played", numberOfGames);
		s += s.format(leftAlignFormatInteger, "maximum games played", maxGames);
		s += s.format(leftAlignFormatInteger, "starting credits", credits);
		s += s.format("+------------------------------------------------------+------------------+%n");
		s += s.format(leftAlignFormatDouble, "money won average", moneyWonAverage);
		s += s.format(leftAlignFormatDouble, "money won average(after " + gameEndsMaxGameCountReached + " successfull games)", moneyWonAfterSuccessfullGamesAverage);
		s += s.format(leftAlignFormatInteger, "games(no more credits)", gameEndsNoMoreCredits);
		s += s.format(leftAlignFormatInteger, "games(max games count reached)", gameEndsMaxGameCountReached);
		s += s.format(leftAlignFormatDouble, "winning probability(%)", winningProbabilityInPercentage);
		s += s.format(leftAlignFormatDouble, "games until finish(average)", gamesUntilFinishAverage);
		s += s.format(leftAlignFormatDouble, "games until finish of all unfinished games(average)", gamesUntilFinishOfAllUnfinishedGames);
		s += s.format("+------------------------------------------------------+------------------+%n");
		s += s.format("calculation time = %f s", (System.currentTimeMillis()-time)/1000);
//		s = s + "money won average = " +  moneyWonAverage;
//		s = s + "games until finish = " + gamesUntilFinish;
//		s = s + "games until finish of all unfinished games = " + gamesUntilFinishOfAllUnfinishedGames;
		
		return s;
	}
	
	public Roulette() {
		
		final JTextArea textArea = new JTextArea();
		final JScrollPane scroll = new JScrollPane(textArea);
		
//		JTextArea textArea = new JTextArea(13, 77);
		textArea.setFont(new Font("Consolas", Font.PLAIN, 12));
		
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		contentPane.setPreferredSize(new Dimension(800, 600));
		JPanel topPanel = new JPanel();
		contentPane.add(topPanel);
		contentPane.add(scroll);
		
		contentPane.add(loadPanel);
		loadPanel.setLayout(new BoxLayout(loadPanel, BoxLayout.X_AXIS));
		loadPanel.setMinimumSize(new Dimension(0, 25));
		
		loadPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		loadPanel.add(loadLabel);
		loadPanel.add(Box.createHorizontalGlue());
		
		textArea.setMinimumSize(new Dimension(200, 10000));
		scroll.setPreferredSize(textArea.getMinimumSize());
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
		textArea.setEditable(false);
		
		Box box1 = new Box(BoxLayout.Y_AXIS);
		final JRadioButton singleGame = new JRadioButton("single game");
		JRadioButton multipleGames = new JRadioButton("multiple games");
		ButtonGroup bGroup = new ButtonGroup();
		bGroup.add(singleGame);
		bGroup.add(multipleGames);
		multipleGames.setSelected(true);
		box1.add(singleGame);
		box1.add(Box.createRigidArea(new Dimension(0, 5)));
		box1.add(multipleGames);
		
		Box box2 = new Box(BoxLayout.Y_AXIS);
		Box startBox = new Box(BoxLayout.X_AXIS);
		final JTextField startingCreditField = new JTextField("1000");
		JLabel startingLabel = new JLabel("starting credits: ");
		startBox.add(startingLabel);
		startBox.add(startingCreditField);
		Box minStackBox = new Box(BoxLayout.X_AXIS);
		JLabel minimumStackLabel = new JLabel("minimum stack: ");
		minStackBox.add(minimumStackLabel);
		startingLabel.setPreferredSize(minimumStackLabel.getPreferredSize());
		final JTextField minStackField = new JTextField("10");
		minStackBox.add(minStackField);
		box2.add(startBox);
		box2.add(Box.createRigidArea(new Dimension(0, 5)));
		box2.add(minStackBox);
		
		Box box3 = new Box(BoxLayout.Y_AXIS);
		Box maxGamesBox = new Box(BoxLayout.X_AXIS);
		JLabel maxGamesLabel = new JLabel("maximum #Games: ");
		maxGamesBox.add(maxGamesLabel);
		final JTextField maxGamesField = new JTextField("20");
		maxGamesBox.add(maxGamesField);
		Box numberTriesBox = new Box(BoxLayout.X_AXIS);
		JLabel triesLabel = new JLabel("#tries: ");
		numberTriesBox.add(triesLabel);
		triesLabel.setPreferredSize(maxGamesLabel.getPreferredSize());
		final JTextField numberTriesField = new JTextField("10000");
		numberTriesBox.add(numberTriesField);
		box3.add(maxGamesBox);
		box3.add(Box.createRigidArea(new Dimension(0, 5)));
		box3.add(numberTriesBox);
		
		Box box4 = new Box(BoxLayout.Y_AXIS);
		JButton simulateButton = new JButton("Simulate!");
		simulateButton.setToolTipText("start the simulation");
		JButton clearButton = new JButton("Clear");
		clearButton.setToolTipText("clears the text area");
		clearButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				textArea.setText("");
			}
		});
		simulateButton.setMaximumSize(new Dimension(500, 500));
		clearButton.setMaximumSize(new Dimension(500, 500));
		box4.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		simulateButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				final int startingCredits = Integer.valueOf(startingCreditField.getText());
				final int minStack = Integer.valueOf(minStackField.getText());
				final int maxGames = Integer.valueOf(maxGamesField.getText());
				
				boolean isSingleGame = singleGame.isSelected();
				
				if (isSingleGame){
					textArea.insert("\n\n_______________________________________________________________________________________________________________\n",0);
					Thread t = new Thread(){
						public void run(){
							textArea.insert(playGameUntil(new Game(startingCredits), minStack, maxGames, false, true), 0);
							
						}
					};
					t.start();
				}
				else{
					final int numberOfTries = Integer.valueOf(numberTriesField.getText());
					textArea.insert("\n\n_______________________________________________________________________________________________________________\n",0);
					Thread t = new Thread(){
						public void run(){
							textArea.insert(playGames(numberOfTries, minStack, maxGames, startingCredits, false), 0);
							
						}
					};
					t.start();
					
					
				}
				textArea.setCaretPosition(0);
				//scroll.getViewport().setViewPosition(new Point(0, 500));
			}
		});
		//box4.add(Box.createHorizontalGlue());
		box4.add(clearButton);
		box4.add(Box.createRigidArea(new Dimension(0, 2)));
		box4.add(simulateButton);
		
		
		topPanel.add(box1);
		topPanel.add(Box.createRigidArea(new Dimension(20, 0)));
		topPanel.add(box2);
		topPanel.add(Box.createRigidArea(new Dimension(20, 0)));
		topPanel.add(box3);
		topPanel.add(Box.createRigidArea(new Dimension(20, 0)));
		topPanel.add(box4);
		
		/*
		int startingCredits = Integer.valueOf(startingCreditField.getText());
		int minStack = Integer.valueOf(minStackField.getText());
		int maxGames = Integer.valueOf(maxGamesField.getText());
		int numberOfTries = Integer.valueOf(numberTriesField.getText());
		boolean isSingleGame = singleGame.isSelected();
		*/
		
		frame.setContentPane(contentPane);
//		frame.setPreferredSize(new Dimension(800, 600));
		frame.pack();
		//textArea.insert(playGameUntil(new Game(1000), 1000, 20, false), 0);
		//textArea.insert(playGames(100000, 100, 20, 1000, false), 0);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public static void main(String[] args){
		Roulette r = new Roulette();
		//r.playGames(100000, 100, 20, 1000, false);
		//System.out.println(r.playGameUntil(new Game(1000), 1000, 20, false));
	}

}
