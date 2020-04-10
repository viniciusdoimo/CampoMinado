package br.com.viniciusdoimo.campominado.model;

import java.util.ArrayList;
import java.util.List;

public class Field {
	private final int line;
	private final int column;

	private boolean open = false;
	private boolean undermined = false;
	private boolean marked = false;

	private List<Field> neighbors = new ArrayList<>();
	private List<FieldObserver> observers = new ArrayList<>();
		
	public Field(int line, int column) {
		this.line = line;
		this.column = column;
	}
	
	public int getLine() {
		return line;
	}
	
	public int getColumn() {
		return column;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
		
		if(open) {
			notifyobservers(EventField.OPEN);
		}
	}

	public boolean isUndermined() {
		return undermined;
	}

	public void setUndermined(boolean undermined) {
		this.undermined = undermined;
	}

	public boolean isMarked() {
		return marked;
	}

	public void setMarked(boolean marked) {
		this.marked = marked;
	}

	public List<Field> getNeighbors() {
		return neighbors;
	}

	public void setNeighbors(List<Field> neighbors) {
		this.neighbors = neighbors;
	}
	
	public void registerObservers(FieldObserver observer) {
		observers.add(observer);
	}
	
	private void notifyobservers(EventField event) {
		observers.stream()
		.forEach(o -> o.occurredEvent(this, event));
	}

	public boolean addNeighbor(Field neighbor) {
		boolean diagonal = isDiagonal(this.line, this.column, neighbor);
		int deltaGeneral = getDeltaGeneral(this.line, this.column, neighbor);

		if (deltaGeneral == 1 && !diagonal) {
			getNeighbors().add(neighbor);
			return Boolean.TRUE;
		} else if (deltaGeneral == 2 && diagonal) {
			getNeighbors().add(neighbor);
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	public void changeChecked() {
		if (!open) {
			marked = !marked;
			
			if(marked) {
				notifyobservers(EventField.MARK);
			}else {
				notifyobservers(EventField.MARK_OFF);
			}
		}
	}

	public boolean putBomb() {
		if (!undermined) {
			undermined = Boolean.TRUE;
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	public int bombsInNeighborhood() {
		return (int) neighbors.stream().filter(n -> n.undermined).count();
	}

	public boolean neighborhoodSecurity() {
		return neighbors.stream().noneMatch(neighbor -> neighbor.undermined);
	}
	
	public boolean openField() {
		if (!open && !marked) {
			if (undermined) {
				notifyobservers(EventField.EXPLODE);
				return Boolean.TRUE;
			}
			
			setOpen(Boolean.TRUE);
			notifyobservers(EventField.OPEN);
			

			if (neighborhoodSecurity()) {
				neighbors.forEach(neighbor -> neighbor.openField());
			}
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	boolean goalAchieved() {
		boolean fildUnveiled = !undermined && open;
		boolean fieldProtected = undermined && marked;

		return fildUnveiled || fieldProtected;
	}

	void restart() {
		open = Boolean.FALSE;
		undermined = Boolean.FALSE;
		marked = Boolean.FALSE;
		
		notifyobservers(EventField.RESTART);
	}

	private boolean isDiagonal(int line, int column, Field neighbor) {
		return differentLine(line, neighbor.getLine()) && differentColumn(column, neighbor.getColumn());
	}

	private boolean differentLine(int line, int neighborLine) {
		return line != neighborLine;
	}

	private boolean differentColumn(int column, int neighborclumn) {
		return column != neighborclumn;
	}

	private int getDeltaGeneral(int line, int column, Field neighbor) {
		int deltaLine = Math.abs(line - neighbor.getLine());
		int deltaColumn = Math.abs(column - neighbor.getColumn());
		return deltaLine + deltaColumn;
	}
}

	