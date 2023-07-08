package Tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import logic.Board;

class FileMasterTest {
	final String fileDirectory = System.getProperty("user.dir") + "/src/Tests/";
	
	Board uncorrectBoard;
	Board correctBoard;
	
	@BeforeEach                                         
    void setUp(){
		try {
			uncorrectBoard = new Board(fileDirectory + "TestPosition.txt");
			correctBoard = new Board(fileDirectory + "StartPosition.txt");
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
    }

	@Test
	void testLoadBoardFromTextFile() {
		assertNotNull(correctBoard.getBoard());
		assertNull(uncorrectBoard.getBoard());
	}

}
