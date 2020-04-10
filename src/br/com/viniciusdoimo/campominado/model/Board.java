package br.com.viniciusdoimo.campominado.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Board implements FieldObserver{
	private final int line;
	private final int column;
	private final int bomb;
	
	private final List<Field> field = new ArrayList<>();
	private final List<Consumer<EventResult>> observers = 
			new ArrayList<>();
	
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

	public int getColumn() {
		return column;
	}

	public int getBomb() {
		return bomb;
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
		} catch (Exception e) {
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
	
	public void registerObservers(Consumer<EventResult> observer) {
		observers.add(observer);
	}
	
	public void notifyObservers(boolean result) {
		observers.stream()
		.forEach(o -> o.accept(new EventResult(result)));
	}
	
	public boolean goalAchieved() {
		return field.stream().allMatch(f -> f.goalAchieved());
	}
	
	public void restart() {
		field.stream().forEach(f -> f.restart());
		randomlyDistributeBombs();
	}
	
	public void forEach(Consumer<Field> function) {
		field.forEach(function);
		
	}
	
	private void showBombs() {
		field.stream()
		.filter(f -> f.isUndermined())
		.filter(f -> !f.isMarked())
		.forEach(f -> f.setOpen(Boolean.TRUE));
	}
	
	private void generateField() {
		for(int l = 0; l < line; l++) {
			for(int c = 0; c < column; c++) {
				Field field = new Field(l, c);
				field.registerObservers(this);
				this.field.add(field);
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
	
	@Override
	public void occurredEvent(Field field, EventField event) {
		if(event == EventField.EXPLODE) {
			showBombs();
			notifyObservers(Boolean.FALSE);
		}else if(goalAchieved()) {
			notifyObservers(Boolean.TRUE);
		}
	}
}
