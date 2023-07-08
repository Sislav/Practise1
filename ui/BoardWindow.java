package ui;

import logic.*;
import io.*;
import ui.*;
import logic.CheckerColor;
import logic.CheckerType;
import java.awt.*;
import javax.swing.*;
import java.io.*;

public class BoardWindow extends JFrame {

	private static final int WIDTH = 1000;
	private static final int HEIGHT = 700;
	
	private static final String TITLE = "Шашки";
	
	private static final String fontName = "Century Gothic";
	
	BoardView board;
	
	OptionalPanel panel;
	
	MoveHistory history;
	
	private String player1;
	private String player2;
	private CheckerColor firstMove;
	private CheckerColor firstPlayer;
	private boolean playingWithComputer = false;
	
	public BoardWindow(String player1, String player2, CheckerColor firstMove, CheckerColor firstPlayer) throws IOException{
		super(TITLE);
		this.setLayout(new BorderLayout());
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(WIDTH, HEIGHT);
		this.setLocation(dim.width/2 - WIDTH/2, dim.height/2 - HEIGHT/2);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.player1 = player1;
		this.player2 = player2;
		this.firstMove = firstMove;
		this.firstPlayer = firstPlayer;
		
		playingWithComputer = false;
		
		
		board = new BoardView(player1, player2, firstMove, firstPlayer, playingWithComputer);
		panel = new OptionalPanel(this, board);
		history = board.getMoveHistoty();
		
		
		
		JPanel ourPanel = new JPanel();
		ourPanel.setLayout(new BorderLayout());
		ourPanel.add(panel, BorderLayout.PAGE_END);
		ourPanel.add(history, BorderLayout.CENTER);
		
		this.add(board, BorderLayout.CENTER);
		this.add(ourPanel, BorderLayout.LINE_END);
		
		setVisible(true);
		
	}
	
	public BoardWindow(String player1, CheckerColor firstMove, CheckerColor firstPlayer) throws IOException{
		super(TITLE);
		this.setLayout(new BorderLayout());
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(WIDTH, HEIGHT);
		this.setLocation(dim.width/2 - WIDTH/2, dim.height/2 - HEIGHT/2);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.player1 = player1;
		this.player2 = "Computer";
		this.firstMove = firstMove;
		this.firstPlayer = firstPlayer;
		
		playingWithComputer = true;
		
		board = new BoardView(player1, player2, firstMove, firstPlayer, playingWithComputer);
		panel = new OptionalPanel(this, board);
		history = board.getMoveHistoty();
		
		
		
		JPanel ourPanel = new JPanel();
		ourPanel.setLayout(new BorderLayout());
		ourPanel.add(panel, BorderLayout.PAGE_END);
		ourPanel.add(history, BorderLayout.CENTER);
		
		this.add(board, BorderLayout.CENTER);
		this.add(ourPanel, BorderLayout.LINE_END);
		
		setVisible(true);
		
	}
}
