package Tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import logic.Board;
import logic.Board.GameResult;
import MachineIntellegence.MachineIntellegence;

class MachineIntellegenceTest {
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
	void testMove() {
		possibleMoves = playingBoard.getPossibleMoves();
		while(!possibleMoves.isEmpty()) {
			try { possibleMoves = playingBoard.move(MachineIntellegence.move(playingBoard)); }
			catch(Exception e){}
		}
		assertFalse(playingBoard.isWin().equals(GameResult.Continue));
	}
}
