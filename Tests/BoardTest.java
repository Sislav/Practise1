package Tests;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.*;

import logic.Board;
import logic.CheckerColor;
import logic.Board.GameResult;

class BoardTest {
	
	Board startBoard;
	Board blackWinBoard;
	Board whiteWinBoard;
	Board classicBoard;
	
	private static final int BOARD_NUMBER = 4;
	
	final CheckerColor white = CheckerColor.White;
	final CheckerColor black = CheckerColor.Black;
	final String fileDirectory = System.getProperty("user.dir") + "/src/Tests/";
	
	@BeforeEach                                         
    void setUp(){
		
		try {
			startBoard = new Board(fileDirectory + "StartPosition.txt");
			
			blackWinBoard = new Board(fileDirectory + "PositionBlackWin.txt");
			
			whiteWinBoard = new Board(fileDirectory + "PositionWhiteWin.txt");
			
			classicBoard = new Board(fileDirectory + "PlayingPosition.txt");
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
    }
	
	@Test
	void testGetTotalWeight() {
		int depth = 1;
		
		int expResultStart = 0;
		int expResultWhiteWin = 3;
		int expResultBlackWin = Integer.MIN_VALUE - depth;
		int expResultClassic = 0;
		
		assertEquals(expResultStart, startBoard.getTotalWeight(white, depth));
		assertEquals(expResultWhiteWin, whiteWinBoard.getTotalWeight(white, depth));
		assertEquals(expResultBlackWin, blackWinBoard.getTotalWeight(white, depth));
		assertEquals(expResultClassic, classicBoard.getTotalWeight(white, depth));
		
	}

	@Test
	void testGetChildren() {
		startBoard.getPossibleMoves();
		assertNotNull("" , startBoard.getChildren());
		
		whiteWinBoard.getPossibleMoves();
		assertNotNull("" , whiteWinBoard.getChildren());
		
		blackWinBoard.getPossibleMoves();
		assertNull("" , blackWinBoard.getChildren());
		
		classicBoard.getPossibleMoves();
		assertNotNull("" , classicBoard.getChildren());
		
	}

	@Test
	void testIsWin() {
		assertTrue(startBoard.isWin().equals(GameResult.Continue));
		assertTrue(whiteWinBoard.isWin().equals(GameResult.WhiteWin));
		assertTrue(blackWinBoard.isWin().equals(GameResult.BlackWin));
		assertTrue(classicBoard.isWin().equals(GameResult.Continue));
	}
	
	@Test
	void testDeleteLastMove() {
		String movePath = classicBoard.getPossibleMoves().get(0);
		Board tmp = new Board(classicBoard);
		
		try {
			classicBoard.move(movePath);
			classicBoard.deleteLastMove();
		}
		catch(Exception e) {System.out.println(e.getMessage());}
		
		assertTrue(tmp.equals(classicBoard));
	}
	
	@Test
	void testMoveWithoutCheck() {
		String correctMove = classicBoard.getPossibleMoves().get(0);
		String uncorrectMove = new String("00:00");
		
		try {
			assertFalse(classicBoard.moveWithoutCheck(correctMove).isEmpty());
			assertTrue(classicBoard.moveWithoutCheck(uncorrectMove).isEmpty());
		}
		catch(Exception e) {}
	}

	@Test
	void testMove() {
		String correctMove = classicBoard.getPossibleMoves().get(0);
		String uncorrectMove = new String("00:00");
		
		try {
			assertFalse(classicBoard.move(correctMove).isEmpty());
			assertTrue(classicBoard.move(uncorrectMove).isEmpty());
		}
		catch(Exception e) {}
	}

	@Test
	void testCheckCorrectCoordinate() {
		int horizontal, vertical;
		
		horizontal = 2;
		vertical = 2;
		assertTrue(Board.checkCorrectCoordinate(horizontal, vertical));
		
		horizontal = 1;
		vertical = 2;
		assertFalse(Board.checkCorrectCoordinate(horizontal, vertical));
		
		horizontal = -1;
		vertical = 0;
		assertFalse(Board.checkCorrectCoordinate(horizontal, vertical));
		
		horizontal = 9;
		vertical = 9;
		assertFalse(Board.checkCorrectCoordinate(horizontal, vertical));
	}
	
	@Test
	void testEquals() {
		assertTrue(startBoard.equals(startBoard));
		assertFalse(startBoard.equals(whiteWinBoard));
	}
}
