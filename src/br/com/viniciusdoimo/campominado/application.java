package br.com.viniciusdoimo.campominado;

import br.com.viniciusdoimo.campominado.model.Board;
import br.com.viniciusdoimo.campominado.view.boardConsole;

public class application {
	public static void main(String[] args) {
		
		Board board = new Board(6, 6, 3);
		new boardConsole(board);
	}
}