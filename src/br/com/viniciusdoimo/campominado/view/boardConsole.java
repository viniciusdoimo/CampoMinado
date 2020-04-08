package br.com.viniciusdoimo.campominado.view;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

import br.com.viniciusdoimo.campominado.exception.ExitException;
import br.com.viniciusdoimo.campominado.exception.ExplosionException;
import br.com.viniciusdoimo.campominado.model.Board;

public class boardConsole {
	
	private Board board;
	private Scanner input = new Scanner(System.in);
	
	public boardConsole(Board board) {
		this.board = board;
		
		executeGame();
	}

	private void executeGame() {
		try {
			boolean exit = Boolean.FALSE;

			while (!exit) {
				gameCycle();
				System.out.println("another match? (S/n) ");
				String reply = input.nextLine();
				
				if ("n".equalsIgnoreCase(reply)) {
					exit = Boolean.TRUE;
				}else {
					board.restar();
				}
			}
		} catch (Exception e) {
			System.out.println("Bye bye!!!");
		}finally {
			input.close();
		}
	}

	private void gameCycle() {
		try {
			
			while(!board.goalAchieved()){
				System.out.println(board);
				
				String typed = captureTypedValue("type it (x, y): ");
				
				Iterator<Integer> xy = Arrays.stream(typed.split(","))
				.map(e -> Integer.parseInt(e.trim())).iterator();
				
				typed = captureTypedValue("1 - open or 2 - Mark off: ");
				System.out.println();
				
				if("1".equals(typed)) {
					board.open(xy.next(), xy.next());
				}else if("2".equals(typed)){
					board.mark(xy.next(), xy.next());
				}
			}
			
			System.out.println("You win!!!");
		} catch (ExplosionException e) {
			System.out.println(board);
			System.out.println("you lose!");
		}
	}
	
	private String captureTypedValue(String text) {
		System.out.print(text);
		String typed = input.nextLine();
		
		if ("exit".equalsIgnoreCase(typed)) {
			throw new ExitException();
		}
		return typed;
	}
}
