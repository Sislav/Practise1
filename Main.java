import logic.*;
import io.*;
import ui.*;
import logic.CheckerColor;
import logic.CheckerType;
public class Main {
	
	/*
	 * Класс который создает главное окно приложения. Главный класс
	*/
	public static void main(String[] argv) {
		try {
			MainWindow window = new MainWindow();
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
