package Tests;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;
import logic.Board;
import logic.Board.GameResult;

class BoardPossibleMovesTest {
	
	final String fileDirectory = System.getProperty("user.dir") + "/src/Tests/";
	
	Board playingBoard;
	ArrayList<String> possibleMoves;
	
	@BeforeEach                                         
    void setUp(){
		try {
			playingBoard = new Board(fileDirectory + "PlayingPosition.txt");
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
    }
	
	@Test
	void testPossibleMove() {
		possibleMoves = playingBoard.getPossibleMoves();
		while(!possibleMoves.isEmpty()) {
			try { possibleMoves = playingBoard.move(possibleMoves.get((int)(Math.random() * (possibleMoves.size() - 1)))); }
			catch(Exception e){}
		}
		assertFalse(playingBoard.isWin().equals(GameResult.Continue));
	}

}
