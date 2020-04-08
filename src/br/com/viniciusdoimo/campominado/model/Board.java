package br.com.viniciusdoimo.campominado.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import br.com.viniciusdoimo.campominado.exception.ExplosionException;

public class Board {
	private int line;
	private int column;
	private int bomb;
	
	private final List<Field> field = new ArrayList<>();
	
	public Board(int line, int column, int bomb) {
		this.line = line;
		this.column = column;
		this.bomb = bomb;
		
		generateField();
		associateNeighbors();
		randomlyDistributeBombs();
	}
	
	public int getLine() {
		return line;
	}
	
	public void setLine(int line) {
		this.line = line;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public int getBomb() {
		return bomb;
	}

	public void setBomb(int bomb) {
		this.bomb = bomb;
	}

	public List<Field> getField() {
		return field;
	}
	
	public void open(int line,int column) {
		try {
			field.parallelStream()
			.filter(f -> f.getLine() == line && f.getColumn() == column)
			.findFirst()
			.ifPresent(f -> f.openField());
		} catch (ExplosionException e) {
			field.forEach(f -> f.setOpen(Boolean.TRUE));
			throw e;
		}
	}
	
	public void mark(int line,int column) {
		field.parallelStream()
			.filter(f -> f.getLine() == line && f.getColumn() == column)
			.findFirst()
			.ifPresent(f -> f.changeChecked());
	}
	
	private void generateField() {
		for(int l = 0; l < line; l++) {
			for(int c = 0; c < column; c++) {
				field.add(new Field(l,c));
			}
		}
	}
	
	private void associateNeighbors() {
		for(Field field: field) {
			for(Field neighbor: this.field) {
				field.addNeighbor(neighbor);
			}
		}
	}

	private void randomlyDistributeBombs() {
		long armedBomb = 0;
		Predicate<Field> undermined = f -> f.isUndermined();
		
		do {
			int random = (int) (Math.random() * field.size());
			field.get(random).putBomb();
			armedBomb = field.stream().filter(undermined).count();
		} while(armedBomb < bomb);
	}
	
	public boolean goalAchieved() {
		return field.stream().allMatch(f -> f.goalAchieved());
	}
	
	public void restar() {
		field.stream().forEach(f -> f.restart());
		randomlyDistributeBombs();
	}
	
	@Override
	public String toString() {
		StringBuilder stringBoard = new StringBuilder();
		int i = 0;
		
		stringBoard.append("  ");
		for (int f = 0; f < column; f++) {
			stringBoard.append(" ");
			stringBoard.append(f);
			stringBoard.append(" ");
		}
		stringBoard.append("\n");
		
		for (int l = 0; l < line; l++) {
			stringBoard.append(l);
			stringBoard.append(" ");
			for (int c = 0; c < column; c++) {
				stringBoard.append(" ");
				stringBoard.append(field.get(i));
				stringBoard.append(" ");
				i++;
			}
			stringBoard.append("\n");
		}
		return stringBoard.toString();
	}
}
