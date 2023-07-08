package logic;

import io.*;
import ui.*;
import java.util.*;
import java.io.*;
import java.lang.Math;


/*Главный класс который отвечает за работу с доской.
 * Инициализация доски, ходы и прочее происходит здесь
 * Полезный метод для отладки = toString*/
public class Board {

	public enum GameResult{
		BlackWin,
		WhiteWin,
		Draw,
		Continue;
	}
	
	public enum Move{
		SimpleMove, //простой ход
		EatMove; //ход с поеданием вражеской шашки
	}
	
	private static final int BOARD_SIZE = 8;
	private static final int EOF = -1; 
	private static final int NOEOF = 0;
	private static final int NULL = 0;
	private static final int ONE_CHECKER_SIZE = 2;
	private static final CheckerColor DEFAULT_FIRST_MOVE_COLOR = CheckerColor.White;
	
	private Cell board[][];
	
	private FileMaster fMaster;
	private BoardPossibleMoves possibleMovesClass = new BoardPossibleMoves();
	private CheckerColor nextMoveColor = CheckerColor.NoColor;
	private ArrayList<String> possibleMoves = null;
	
	private String player1 = "Player1";
	private String player2 = "Player2";
	
	private int childrenCounter = 0; //для искусственного интеллекта
	
	private ArrayList<String> kingPaths = new ArrayList<String>(); //для отката ходов назад
	
	public Board() throws IOException{
		try {
			nextMoveColor = DEFAULT_FIRST_MOVE_COLOR;
			startNewGame();
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	public Board(String filename) throws IOException{
		try {
			nextMoveColor = DEFAULT_FIRST_MOVE_COLOR;
			startNewGame(filename);
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public Board(CheckerColor color, String player1, String player2) throws IOException{
		this.player1 = player1;
		this.player2 = player2;
		nextMoveColor = color;
		startNewGame();
	}
	
	public Board(Board basic) {
		board = new Cell[BOARD_SIZE][BOARD_SIZE];
		for(int i = 0; i < BOARD_SIZE; i++) {
			for(int j = 0; j < BOARD_SIZE; j++) {
				board[i][j] = new Cell(basic.board[i][j]);
			}
		}
		
		nextMoveColor = basic.nextMoveColor;
		possibleMoves = new ArrayList<String>();
		for(int i = 0; i < basic.possibleMoves.size(); i++) {
			possibleMoves.add(new String(basic.possibleMoves.get(i)));
		}
	}
	
	public ArrayList<String> initializePossibleMoves() {
		possibleMoves = possibleMovesClass.possibleMove(nextMoveColor, board);
		return possibleMoves;
	}
	
	public Cell[][] getBoard(){
		if(board == null) return null;
		
		return board;
	}
	
	/*для ИИ.
	 * Высчитывает текущую стоимость доски*/
	public int getTotalWeight(CheckerColor frendsColor, int depth) {
		String current = this.toString();
		int whitePawnCount = current.length() - current.replace("w", "").length();
		int whiteKingCount = current.length() - current.replace("W", "").length();
		
		if((whitePawnCount + whiteKingCount) == 0 && frendsColor == CheckerColor.Black) {
			return (int)(Integer.MAX_VALUE - depth);
		}
		else if((whitePawnCount + whiteKingCount) == 0 && frendsColor == CheckerColor.White) {
			return (int)(Integer.MIN_VALUE - depth);
		}
		
		int blackPawnCount = current.length() - current.replace("b", "").length();
		int blackKingCount = current.length() - current.replace("B", "").length();
		
		if((blackPawnCount + blackKingCount) == 0 && frendsColor == CheckerColor.Black) {
			return (int)(Integer.MIN_VALUE - depth);
		}
		else if((blackPawnCount + blackKingCount) == 0 && frendsColor == CheckerColor.Black) {
			return (int)(Integer.MAX_VALUE - depth);
		}
		
		int result = whitePawnCount - blackPawnCount + 3 * (whiteKingCount - blackKingCount);
		if(frendsColor == CheckerColor.White) return result;
		else return (-1)*result; 
	}
	
	/*для ИИ. Возвращает возможную позицию из текущей позиции*/
	public Board getChildren(){
		try {
			if(childrenCounter < possibleMoves.size()) {
				Board nextBoard = new Board(this);
				nextBoard.moveWithoutCheck(nextBoard.possibleMoves.get(childrenCounter));
				childrenCounter++;
				return nextBoard;
			}
			return null;	
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	public String getCurrentPath() {
		if(childrenCounter - 1 < possibleMoves.size() && childrenCounter - 1 >= 0) {
			return possibleMoves.get(childrenCounter - 1);
		}
		return null;
	}
	
	public ArrayList<String> getPossibleMoves(){
		if(possibleMoves == null) {
			possibleMoves = possibleMovesClass.possibleMove(nextMoveColor, board);
		}
		return possibleMoves;
	}
	
	public CheckerColor getNextMoveColor() {
		return nextMoveColor;
	}
	
	private void clearBoard() {
		
		for(int horizontal = 0; horizontal < BOARD_SIZE; horizontal++) {
			for(int vertical = 0; vertical < BOARD_SIZE; vertical++) {
				if((horizontal % 2) == (vertical % 2)) {
					board[horizontal][vertical].setColor(CheckerColor.NoColor);
					board[horizontal][vertical].setType(CheckerType.NoChecker);
				}
			}
		}
	}
	
	private void startNewGame() throws IOException{
		fMaster = new FileMaster(player1, player2);
		board = fMaster.loadBoardFromTextFile("logic/BasicCheckersPosition.txt");
	}
	
	private void startNewGame(String filename) throws IOException{
		fMaster = new FileMaster(player1, player2);
		board = fMaster.loadBoardFromTextFile(filename);
	}
	
	public GameResult isWin() {
		try {
			if(possibleMovesClass.possibleMove(CheckerColor.White, board).isEmpty() 
					&& possibleMovesClass.possibleMove(CheckerColor.Black, board).isEmpty()) {
				return GameResult.Draw;
			}
			else if(possibleMovesClass.possibleMove(CheckerColor.White, board).isEmpty()) {
				return GameResult.BlackWin;
			}
			else if(possibleMovesClass.possibleMove(CheckerColor.Black, board).isEmpty()) {
				return GameResult.WhiteWin;
			}
			else {
				return GameResult.Continue;
			}
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	private boolean isKingPath(String path) {
		for(String str : kingPaths) {
			if(path.contains(str)) return true;
		}
		return false;
	}
	
	public GameResult endGame() throws IOException {
		fMaster.endGame(isWin());
		return isWin();
	}
	
	/*метод делающий ход без проверки на корректность*/
	public ArrayList<String> moveWithoutCheck(String movePath) throws IOException {
		possibleMoves.clear();
		
		String[] checker = movePath.split(":");
		StringBuffer prevChecker = new StringBuffer();
		StringBuffer deleteCheckers = new StringBuffer();
		
		for(String currentChecker : checker) {
			if(prevChecker.length() == NULL) { 
				prevChecker = new StringBuffer(currentChecker); 
				prevChecker.append(':');
				continue; 
			}
			
			if(deleteCheckers.length() != 0) deleteCheckers.append(',');				
			deleteCheckers.append(makeMove(new StringBuffer(prevChecker).append(currentChecker)));
			
			prevChecker = new StringBuffer(currentChecker);
			prevChecker.append(':');
		}
		nextMoveColor = Cell.enemyColor(nextMoveColor);
		possibleMoves = possibleMovesClass.possibleMove(nextMoveColor, board);
		
		return possibleMoves;
	}
	/*метод делающий ход с проверкой на корректность и записью в файл*/
	public ArrayList<String> move(String movePath) throws IOException{
		possibleMoves.clear();
		
		String[] checker = movePath.split(":");
		StringBuffer prevChecker = new StringBuffer();
		StringBuffer deleteCheckers = new StringBuffer();
		
		for(String currentChecker : checker) {
			if(prevChecker.length() == NULL) { 
				prevChecker = new StringBuffer(currentChecker); 
				prevChecker.append(':');
				continue; 
			}
			
			if(deleteCheckers.length() != 0) deleteCheckers.append(',');
			
			boolean flag = isCorrectMove(new StringBuffer(prevChecker).append(currentChecker));
			
			if(flag) 
				deleteCheckers.append(makeMove(new StringBuffer(prevChecker).append(currentChecker)));
			else return new ArrayList<String>();
			//return new ArrayList<String>();
			prevChecker = new StringBuffer(currentChecker);
			prevChecker.append(':');
			
		
		}
		if(this.fMaster != null) fMaster.writeMove(movePath, deleteCheckers.toString());
		nextMoveColor = Cell.enemyColor(nextMoveColor);
		possibleMoves = possibleMovesClass.possibleMove(nextMoveColor, board);
		
		return possibleMoves;
	}
	
	/*Удаляет последний ход*/
	public ArrayList<String> deleteLastMove() throws IOException{
		String move = fMaster.deleteLastMove();
		
		if(move == null) {
			return null;
		}
		
		String[] twoParts = move.split("\\(");
		String[] movePath = twoParts[0].split(":");
		String deletePath = twoParts[1];
		
		int i = 0;
		int startVertical = (int)(movePath[i].charAt(0) - '0');
		int startHorizontal = (int)(movePath[i].charAt(1) - '0');
		i = movePath.length - 1;
		int finishVertical = (int)(movePath[i].charAt(0) - '0');
		int finishHorizontal = (int)(movePath[i].charAt(1) - '0');
		
		board[startHorizontal][startVertical].setColor(board[finishHorizontal][finishVertical].getColor());
		board[startHorizontal][startVertical].setType(board[finishHorizontal][finishVertical].getType());
		
		if(board[startHorizontal][startVertical].getType() == CheckerType.King && isKingPath(move)) {
			board[startHorizontal][startVertical].setType(CheckerType.Pawn);
		}
		
		board[finishHorizontal][finishVertical].setColor(CheckerColor.NoColor);
		board[finishHorizontal][finishVertical].setType(CheckerType.NoChecker);
		
		
		if(deletePath.length() > 2) {
			for(int index = 0; index < deletePath.length() - 1; index++) {
				char symbol = deletePath.charAt(index++);
				if(symbol >= 'a' && symbol <= 'h') {
					startVertical = (int)(symbol - 'a');
					startHorizontal = (int)(deletePath.charAt(index++) - '1');
					board[startHorizontal][startVertical].setColor(nextMoveColor);
					board[startHorizontal][startVertical].setType(CheckerType.Pawn);
				}
				else if(symbol >= 'A' && symbol <= 'H') {
					startVertical = (int)(symbol - 'A');
					startHorizontal = (int)(deletePath.charAt(index++) - '0');
					board[startHorizontal][startVertical].setColor(nextMoveColor);
					board[startHorizontal][startVertical].setType(CheckerType.King);
				}
			}
		}
		
		nextMoveColor = Cell.enemyColor(nextMoveColor);
		possibleMoves = possibleMovesClass.possibleMove(nextMoveColor, board);
		return possibleMoves;
	}
	
	private boolean isCorrectMove(StringBuffer path) {
		Cell startPosition;
		Cell finishPosition;

		int index = 0;
		int endIndex = path.length();
		
		int vertical = (int)(path.charAt(index++) - '0');
		int horizontal = (int)(path.charAt(index++) - '0');
		
		index++;//пропуск символа ':'
		//Проверка координат начала хода на выход за пределы доски и на цвет клетки
		if(!Board.checkCorrectCoordinate(horizontal, vertical)) {
			return false;
		}
		
		startPosition = board[horizontal][vertical];
		
		//Проверка на наличие ходящей шашки
		if(startPosition.getColor() == CheckerColor.NoColor 
				|| startPosition.getType() == CheckerType.NoChecker) {
			return false;
		}
		
		vertical = (int)(path.charAt(index++) - '0');
		horizontal = (int)(path.charAt(index++) - '0');
		
		//Проверка координат конца хода на выход за пределы доски и на цвет клетки
		if(!Board.checkCorrectCoordinate(horizontal, vertical)) {
			return false;
		}
			
		finishPosition = board[horizontal][vertical];
			
		//Проверка на пустоту конечной клетки
		if(finishPosition.getColor() != CheckerColor.NoColor && 
				finishPosition.getType() != CheckerType.NoChecker) {
			return false;
		}
		
		int verticalDifference = finishPosition.getVertical() - startPosition.getVertical();
		int horizontalDifference = finishPosition.getHorizontal() - startPosition.getHorizontal();
		
		//Расположены ли начальная и конечные клетки на одной диагонали?
		if(Math.abs(verticalDifference) != Math.abs(horizontalDifference)) {
			return false;
		}
		
		//Eсли я хочу ходить пешкой
		if(startPosition.getType() == CheckerType.Pawn) {
			
			//если пешка идёт на соседнюю клетку, т.е. просто ходит
			if(Math.abs(verticalDifference) == 1) {
				
				//Проверка правильности направления пешки
				if(horizontalDifference < 0 && startPosition.getColor() == CheckerColor.White
						|| horizontalDifference > 0 && startPosition.getColor() == CheckerColor.Black) {
					return false;
				}
				return true;
			}
			
			//если пешка сьедает вражескую фигуру
			else if(Math.abs(verticalDifference) == 2){
				
				int middleHorizontal = startPosition.getHorizontal() + 
						horizontalDifference / Math.abs(horizontalDifference);
				int middleVertical = startPosition.getVertical() + 
						verticalDifference / Math.abs(verticalDifference);
				
				//Проверка различности цветов сьеденной пешки и сьедающей пешки
				if((startPosition.getColor() == CheckerColor.White && board[middleHorizontal][middleVertical].getColor() == CheckerColor.Black)
						|| (startPosition.getColor() == CheckerColor.Black && board[middleHorizontal][middleVertical].getColor() == CheckerColor.White)){
					return true;
				}
				return false;
			}
			
			return false;
		}
		
		//Если ходят дамкой	
		else if(startPosition.getType() == CheckerType.King) {
			
			int diagonalHorizontal = startPosition.getHorizontal() + horizontalDifference / Math.abs(horizontalDifference);
			int diagonalVertical = startPosition.getVertical() + verticalDifference / Math.abs(verticalDifference);
			
			int checkersCount = 0; //счётчик вподряд идущих шашек
			
			while(diagonalHorizontal != finishPosition.getHorizontal() 
					&& diagonalVertical != finishPosition.getVertical()) {
				
				//если на пути дамки встретилась союзная фигура
				if(startPosition.getColor() == board[diagonalHorizontal][diagonalVertical].getColor()) {
					return false;
				}
				
				//проверка на чередующиеся фигруры
				if(board[diagonalHorizontal][diagonalVertical].getType() != CheckerType.NoChecker) {
					checkersCount++;
					if(checkersCount > 1) {
						return false;
					}
				}
				else {
					checkersCount = 0;
				}
				
				diagonalHorizontal += horizontalDifference / Math.abs(horizontalDifference);
				diagonalVertical += verticalDifference / Math.abs(verticalDifference);
				
			}
			return true;
		}
		return false;
	}
	
	/*Функция которая передвигает шашки на доске*/
	private String makeMove(StringBuffer path) {

		Cell startPosition;
		Cell finishPosition;
		
		StringBuffer deleteCheckers = new StringBuffer();
		StringBuffer delete = new StringBuffer();
		
		int index = 0;
		int endIndex = path.length();
		
		int vertical = (int)(path.charAt(index++) - '0');
		int horizontal = (int)(path.charAt(index++) - '0');
		
		index++;
		
		startPosition = board[horizontal][vertical];
		
		vertical = (int)(path.charAt(index++) - '0');
		horizontal = (int)(path.charAt(index++) - '0');
		
		finishPosition = board[horizontal][vertical];
		
		int verticalDifference = finishPosition.getVertical() - startPosition.getVertical();
		int horizontalDifference = finishPosition.getHorizontal() - startPosition.getHorizontal();
		
		int currentHorizontal = startPosition.getHorizontal() + horizontalDifference / Math.abs(horizontalDifference);
		int currentVertical = startPosition.getVertical() + verticalDifference / Math.abs(verticalDifference);
			
		while(currentHorizontal != finishPosition.getHorizontal() 
				&& currentVertical != finishPosition.getVertical()) {
				
			if(board[currentHorizontal][currentVertical].getType() != CheckerType.NoChecker) {
				if(board[currentHorizontal][currentVertical].getType() == CheckerType.King) {
					delete.append((char)('A' + currentVertical));	
				}
				if(board[currentHorizontal][currentVertical].getType() == CheckerType.Pawn) {
					delete.append((char)('a' + currentVertical));
				}
				delete.append((char)('1' + currentHorizontal));
				
				deleteCheckers.append(currentVertical);
				deleteCheckers.append(currentHorizontal);
				deleteCheckers.append(',');
			}
					
			currentHorizontal += horizontalDifference / Math.abs(horizontalDifference);
			currentVertical += verticalDifference / Math.abs(verticalDifference);		
		}
			
		if(finishPosition.getHorizontal() == BOARD_SIZE - 1 && startPosition.getColor() == CheckerColor.White
				|| finishPosition.getHorizontal() == NULL && startPosition.getColor() == CheckerColor.Black) {
			finishPosition.setType(CheckerType.King);
			kingPaths.add(path.toString());
		}
		else finishPosition.setType(startPosition.getType());
		
		finishPosition.setColor(startPosition.getColor());
		startPosition.setType(CheckerType.NoChecker);
		startPosition.setColor(CheckerColor.NoColor);
		
		String[] currentDeleteCheckers = deleteCheckers.toString().split(",");
		
		for(String currentChecker: currentDeleteCheckers) {
			if(currentChecker.length() == NULL) continue;
			int currentIndex = 0;
			
			int deleteVertical = currentChecker.charAt(currentIndex++) - '0';
			int deleteHorizontal = currentChecker.charAt(currentIndex++) - '0';
			
			board[deleteHorizontal][deleteVertical].setType(CheckerType.NoChecker);
			board[deleteHorizontal][deleteVertical].setColor(CheckerColor.NoColor);
		}
		return delete.toString();
	}
	
	private CheckerColor identifyColor(StringBuffer path) {
		int index = path.length() - ONE_CHECKER_SIZE;
		int vertical = (int)(path.charAt(index++) - '0');
		int horizontal = (int)(path.charAt(index) - '0');
		
		return board[horizontal][vertical].getColor();
	}
	
	static public boolean checkCorrectCoordinate(int horizontal, int vertical) {
		final int BOARD_SIZE = 8;
		return (horizontal >= 0 && horizontal < BOARD_SIZE 
				&& vertical >= 0 && vertical < BOARD_SIZE
				&& horizontal % 2 == vertical % 2);
	}
	
	public boolean equals(Object o) {
		if(o instanceof Board) {
			Board second = (Board)o;
			for(int i = 0; i < BOARD_SIZE; i++) {
				for(int j = 0; j < BOARD_SIZE; j++) {
					if(!board[i][j].equals(second.board[i][j])) {
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		for(int i = 0; i < BOARD_SIZE; i++) {
			for(int j = 0; j < BOARD_SIZE; j++) {
				buf.append(board[i][j].toString());
			}
			buf.append('\n');
		}
		return buf.toString();
	}
	
}
