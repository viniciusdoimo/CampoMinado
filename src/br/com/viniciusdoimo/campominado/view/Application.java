package br.com.viniciusdoimo.campominado.view;

import javax.swing.JFrame;

import br.com.viniciusdoimo.campominado.model.Board;

@SuppressWarnings("serial")
public class Application extends JFrame {
	
	public Application() {
		Board board = new Board(16, 30, 5);
		add( new PanelBoard(board));
		
		setTitle("Campo Minado");
		setSize(690, 438);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(Boolean.TRUE);
	}
	
	public static void main(String[] args) {
		new Application();
	}
}
