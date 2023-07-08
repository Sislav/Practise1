package ui;

import logic.*;
import io.*;
import ui.*;
import logic.CheckerColor;
import logic.CheckerType;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.border.Border;
import java.util.*;

public class OptionalPanel extends JPanel{
	public OptionalPanel(JFrame frame, BoardView board){
		
		this.setLayout(new FlowLayout());
		JButton prevMove = new JButton("Прошлый ход");
		JButton mainMenu = new JButton("Главное меню");
		
		prevMove.setOpaque(true);
		prevMove.setBorderPainted(false);
		prevMove.setBackground(new Color(201, 174, 101));
		
		mainMenu.setOpaque(true);
		mainMenu.setBorderPainted(false);
		mainMenu.setBackground(new Color(201, 174, 101));
		
		ActionListener actionMainMenu = new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	frame.dispose();
		    }
		};
		
		ActionListener actionPrevMove = new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	try {
		    		board.deleteLastMove();
		    		if(board.thisGameWithComputer) {
		    			board.deleteLastMove();
		    		}
		    	}
		    	catch(IOException ex) {
		    		System.out.print(ex.getMessage());
		    	}
		    }
		};
		
		mainMenu.addActionListener(actionMainMenu);
		prevMove.addActionListener(actionPrevMove);
		
		this.add(prevMove);
		this.add(mainMenu);
		this.setBackground(new Color(245, 229, 186));
	}
}
