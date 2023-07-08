package ui;

import logic.*;
import io.*;
import ui.*;
import logic.CheckerColor;
import logic.CheckerType;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.Border;
import java.util.*;
import java.io.*;


public class MainWindow extends JFrame{
	private static final int MAIN_WIDTH = 300;
	private static final int MAIN_HEIGHT = 250;
	
	private static final int BUTTON_WIDTH = 50;
	private static final int BUTTON_HEIGHT = 50;
	
	private static final int DIALOG_WIDTH = 500;
	private static final int DIALOG_HEIGHT = 250;
	
	private static final String TITLE = "Шашки";
	
	private static final String PLAY_WITH_FRENDS = "Играть с другом";
	private static final String PLAY_WITH_COMPUTER = "Играть с компьютером";
	private static final String LOAD_GAME = "Загрузить прошлую игру";
	
	private static final String fontName = "Century Gothic";
	
	private static final String WHITE = "white";
	private static final String BLACK = "black";
	
	private static String player1;
	private static String player2;
	
	private CheckerColor firstMove = CheckerColor.NoColor;
	private CheckerColor firstPlayerColor = CheckerColor.NoColor;
	
	
	public MainWindow() throws IOException {
		super(TITLE);
		
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
		}
		catch(Exception exception){
			System.out.println(exception.getMessage());
		}
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(MAIN_WIDTH, MAIN_HEIGHT);
		this.setLocation(dim.width/2 - MAIN_WIDTH/2, dim.height/2 - MAIN_HEIGHT/2);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		addButtonsAndLetter();
		
		this.getContentPane().setBackground(new Color(245, 229, 186));
		
		setVisible(true);
		
	}
	
	private void addButtonsAndLetter() throws IOException{
		
		JButton buttonPlayFrends = new JButton(PLAY_WITH_FRENDS);
		JButton buttonPlayComputer = new JButton(PLAY_WITH_COMPUTER);
		
		buttonPlayFrends.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonPlayComputer.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		ActionListener action = new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	try {
			        JButton btn = (JButton)e.getSource();
			        String name = btn.getActionCommand();
			        
			        if(name.equals(PLAY_WITH_FRENDS)) {
			        	getOptionMenu(PLAY_WITH_FRENDS);
			        	BoardWindow newGame = new BoardWindow(player1, player2, firstMove, firstPlayerColor);
			        }
			        else if(name.equals(PLAY_WITH_COMPUTER)) {
			        	
			        	getOptionMenu(PLAY_WITH_COMPUTER);
			        	BoardWindow newGame = new BoardWindow(player1, firstMove, firstPlayerColor);
			        }
			        else {
			        	loadGame();
			        }
		    	}
	        	catch(Exception exep) {
	        		System.out.println(exep.getMessage());
	        	}
		        
		    }
		};
		
		buttonPlayFrends.addActionListener(action);
		buttonPlayComputer.addActionListener(action);
		
		buttonPlayFrends.setBorderPainted(false);
		buttonPlayComputer.setBorderPainted(false);
		
		buttonPlayFrends.setOpaque(true);
		buttonPlayComputer.setOpaque(true);
		
		buttonPlayFrends.setBackground(new Color(201, 174, 101));
		buttonPlayComputer.setBackground(new Color(201, 174, 101));
		
		Container container = new Container();
		Dimension size = new Dimension(20, 20);
		
		Font font = new Font(fontName, Font.BOLD, 50);
        
        JLabel label = new JLabel("ШАШКИ");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setFont(font);
        label.setOpaque(true);
        label.setForeground(new Color(92, 71, 13));
        label.setBackground(new Color(245, 229, 186));
        
		container.add(label);
		container.add(Box.createRigidArea(new Dimension(5, 5)));
        container.add(buttonPlayFrends);
        container.add(Box.createRigidArea(size));
        container.add(buttonPlayComputer);
    
        container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
       
        getContentPane().add(container);        

        setVisible(true);    
	}
	
	public void getOptionMenu(String bottonName) {
		
		JDialog optionMenu = new JDialog(this,"Настойки", true);
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		optionMenu.setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
		optionMenu.setLocation(dim.width/2 - DIALOG_WIDTH/2, dim.height/2 - DIALOG_HEIGHT/2);
		optionMenu.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		JPanel container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
		JButton[] buttons = getPlayersName(container, bottonName);
		
		ButtonGroup group1 = getFirstMoveInformation(container);
		ButtonGroup group2 = getPlayerColorInformation(container);

		
		
		ActionListener action = new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	buttons[0].doClick();
		    	
		    	if(bottonName == PLAY_WITH_FRENDS) buttons[1].doClick();
		
		    	for (Enumeration<AbstractButton> group = group1.getElements(); group.hasMoreElements();) {
		         	AbstractButton b = group.nextElement();
		          	if(b.isSelected()) {
		          		if(b.getName().equals(WHITE)) firstMove = CheckerColor.White;
		          		else firstMove = CheckerColor.Black;
		          	}
		    	}
		    	for (Enumeration<AbstractButton> group = group2.getElements(); group.hasMoreElements();) {
		         	AbstractButton b = group.nextElement();
		          	if(b.isSelected()) {
		          		if(b.getName().equals(WHITE)) firstPlayerColor = CheckerColor.White;
		          		else firstPlayerColor = CheckerColor.Black;
		          	}
		    	}
		    	optionMenu.dispose();
		    }
		};
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		buttonPanel.setBackground(new Color(245, 229, 186));
		JButton	button = new JButton("Начать игру");
		button.addActionListener(action);
		button.setOpaque(true);
		button.setBorderPainted(false);
		button.setBackground(new Color(201, 174, 101));
		
		buttonPanel.add(button);
		container.setBackground(new Color(245, 229, 186));
		container.add(buttonPanel);
		
		
		//container.setBackground(new Color(245, 229, 186));
		optionMenu.setBackground(new Color(245, 229, 186));
		optionMenu.getContentPane().add(container);
		optionMenu.setVisible(true);
	}
	
	private ButtonGroup getPlayerColorInformation(Container container) {
		Font font = new Font(fontName, Font.PLAIN, 15);
		JPanel firstPlayerColor = new JPanel();
		firstPlayerColor.setLayout(new FlowLayout(FlowLayout.CENTER));
			
		JLabel firstPlayerLabel = new JLabel("Первый игрок играет: ");
	    firstPlayerLabel.setFont(font);
	    firstPlayerColor.add(firstPlayerLabel);
			
	    JRadioButton whiteColor = new JRadioButton("Белыми");
	    whiteColor.setName(WHITE);
        JRadioButton blackColor = new JRadioButton("Чёрными");
        blackColor.setName(BLACK);
        whiteColor.setSelected(true);
        
        ButtonGroup bottonGroupForPlayer = new ButtonGroup();
        bottonGroupForPlayer.add(whiteColor);
        bottonGroupForPlayer.add(blackColor);
	        
        firstPlayerColor.add(whiteColor);
        firstPlayerColor.add(blackColor);
        firstPlayerColor.setBackground(new Color(245, 229, 186));
        container.add(firstPlayerColor);
        return bottonGroupForPlayer;
        
	}
	
	
	private ButtonGroup getFirstMoveInformation(Container container) {
		
		Font font = new Font(fontName, Font.PLAIN, 15);
		
		JPanel firstMoveColor = new JPanel();
		firstMoveColor.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		JLabel firstMoveLabel = new JLabel("Первый ход: ");
        firstMoveLabel.setFont(font);
        firstMoveColor.add(firstMoveLabel);
        
        JRadioButton whiteFirstMove = new JRadioButton("Белые");
        whiteFirstMove.setName(WHITE);
        JRadioButton blackFirstMove = new JRadioButton("Чёрные");
        blackFirstMove.setName(BLACK);
        whiteFirstMove.setSelected(true);
        
        
        
        ButtonGroup bottonGroupForColor = new ButtonGroup();
        bottonGroupForColor.add(whiteFirstMove);
        bottonGroupForColor.add(blackFirstMove);
        
        firstMoveColor.add(whiteFirstMove);
        firstMoveColor.add(blackFirstMove);
        
        firstMoveColor.setBackground(new Color(245, 229, 186));
        container.add(firstMoveColor);
        return bottonGroupForColor;
        
	}
	
	private JButton[] getPlayersName(Container container, String version) {
		Font font = new Font(fontName, Font.PLAIN, 15);
		
		JButton[] result = new JButton[2];
		int index = 0;
		
		JPanel firstPlayerPanel = new JPanel();
		firstPlayerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		JLabel firstPlayerLabel = new JLabel("Имя игрока 1: ");
        firstPlayerLabel.setFont(font);
		firstPlayerPanel.add(firstPlayerLabel);
		
		JTextField firstPlayerText = new JTextField("Player 1", 20);
		firstPlayerPanel.add(firstPlayerText);
		
		JButton firstPlayerButton = new JButton("OK");
		ActionListener actionForFirstPlayerButton = new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        player1 = firstPlayerText.getText();
		    }
		};
		firstPlayerButton.addActionListener(actionForFirstPlayerButton);
		result[index++] = firstPlayerButton;
		firstPlayerPanel.setBackground(new Color(245, 229, 186));
		firstPlayerPanel.add(firstPlayerButton);
		
		container.add(firstPlayerPanel);
		
		if(version == PLAY_WITH_FRENDS) {
			
			JPanel secondPlayerPanel = new JPanel();
			secondPlayerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			
			JLabel secondPlayerLabel = new JLabel("Имя Игрока 2: ");
	        secondPlayerLabel.setFont(font);
			secondPlayerPanel.add(secondPlayerLabel);
			
			JTextField secondPlayerText = new JTextField("Player 2", 20);
			secondPlayerPanel.add(secondPlayerText);
			
			JButton secondPlayerButton = new JButton("OK");
			ActionListener actionForSecondPlayerButton = new ActionListener() {
			    public void actionPerformed(ActionEvent e) {
			        player2 = secondPlayerText.getText();
			    }
			};
			secondPlayerButton.addActionListener(actionForSecondPlayerButton);
			result[index++] = secondPlayerButton;
			secondPlayerPanel.add(secondPlayerButton);
			secondPlayerPanel.setBackground(new Color(245, 229, 186));
			
			container.add(secondPlayerPanel);	
		}
		return result;
	}
	
	
	
	public void loadGame() {
		System.out.println("load");
	}
	
}
