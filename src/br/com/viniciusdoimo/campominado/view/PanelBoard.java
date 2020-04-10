package br.com.viniciusdoimo.campominado.view;

import java.awt.GridLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import br.com.viniciusdoimo.campominado.model.Board;

@SuppressWarnings("serial")
public class PanelBoard extends JPanel{

	public PanelBoard(Board board) {
		
		setLayout(new GridLayout(
				board.getLine(), board.getColumn()));
		
		board.forEach(f -> add(new Button(f)));
		board.registerObservers(e -> {
			
			SwingUtilities.invokeLater(() -> {
				if(e.isWin()) {
					JOptionPane.showMessageDialog(this, "You Win!");
				}else {
					JOptionPane.showMessageDialog(this, "You Lose!");
				}
				board.restart();
			});
		});
	}
}