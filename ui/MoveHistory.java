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

public class MoveHistory extends JPanel {
	JTextArea text;
	
	public MoveHistory() {
		this.setLayout(new BorderLayout());
		this.add(new JButton("history"));
		
		JLabel label = new JLabel("История ходов:");
		
		Font font = new Font("Century Gothic", Font.BOLD, 20);
		label.setOpaque(true);
        label.setForeground(new Color(92, 71, 13));
        label.setBackground(new Color(245, 229, 186));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setFont(font);
        
		this.add(label, BorderLayout.PAGE_START);
		this.text = new JTextArea();
		text.setEditable(false);
        text.setLineWrap(true);
        text.setFont(new Font("Century Gothic", Font.PLAIN, 15));
        
        JScrollPane scroll = new JScrollPane(text);
        scroll.setPreferredSize(new Dimension(200, 250));
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        this.setBackground(new Color(245, 229, 186));
        add(scroll, BorderLayout.CENTER);
	}
	
	public void deleteMove() {
		String[] moves = text.getText().split(" ");
		StringBuffer buf = new StringBuffer();
		for(int i = 0; i < moves.length - 1; i++) {
			buf.append(moves[i]);
			buf.append(' ');
		}
		text.setText(buf.toString());
	}
	
	public void writeMove(String path) {
		String[] movePath = path.split(":");
		StringBuffer finalMove = new StringBuffer();
		for(int index = 0; index < movePath.length; index++) {
			finalMove.append((char)('a' + (movePath[index].charAt(0) - '0')));
			finalMove.append((char)(movePath[index].charAt(1) + 1));
			finalMove.append(':');
		}
		finalMove.deleteCharAt(finalMove.length() - 1);
		text.append(finalMove.toString());
		text.append(" ");
	}	
}
