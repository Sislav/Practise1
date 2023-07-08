package MachineIntellegence;

import logic.Cell;
import logic.CheckerColor;

import logic.*;

import java.util.*;
import java.io.*;

public class MachineIntellegence {
	private static CheckerColor frendsColor;
	private static CheckerColor enemyColor;
	private static final int DEPTH = 8;
	
	public static String move(Board board){
		frendsColor = board.getNextMoveColor();
		enemyColor = Cell.enemyColor(frendsColor);
		
	    String bestMove = null;
	    int bestScore = Integer.MIN_VALUE;
	    Board gameTreeRoot = new Board(board);	    
	    for(Board child = gameTreeRoot.getChildren(); child != null; child = gameTreeRoot.getChildren()) {
	    	int alpha = miniMax(child, DEPTH - 1, bestScore, Integer.MAX_VALUE);
	        if (alpha >= bestScore || bestMove == null) {
	            bestMove = gameTreeRoot.getCurrentPath();
	            bestScore = alpha;
	        }
	    }
	    return bestMove;
	}

	private static int miniMax(Board currentNode, int depth, int alpha, int beta) {
		Board tmp = new Board(currentNode);
	    if (depth <= 0 || tmp.isWin() != Board.GameResult.Continue) {
	        return currentNode.getTotalWeight(frendsColor, depth);
	    }
	    if (currentNode.getNextMoveColor() == frendsColor) {
	        int currentAlpha = Integer.MIN_VALUE;
	        for(Board child = currentNode.getChildren(); child != null; child = currentNode.getChildren()) {
	        	currentAlpha = Math.max(currentAlpha, miniMax(child, depth - 1, alpha, beta));
	            alpha = Math.max(alpha, currentAlpha);
	            if (alpha >= beta || currentAlpha == Integer.MIN_VALUE) {
	                return alpha;
	            }	
		    }
	       
	        return currentAlpha;
	    }
	    int currentBeta = Integer.MAX_VALUE;
	    
	    for(Board child = currentNode.getChildren(); child != null; child = currentNode.getChildren()) {
	    	currentBeta = Math.min(currentBeta, miniMax(child, depth - 1, alpha, beta));
	        beta = Math.min(beta, currentBeta);
	        if (beta <= alpha || currentBeta == Integer.MAX_VALUE) {
	            return beta;
	        }
	    }
	    return currentBeta;
	}
}
