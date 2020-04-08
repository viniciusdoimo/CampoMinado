package br.com.viniciusdoimo.campominado.model;

import java.util.ArrayList;
import java.util.List;

import br.com.viniciusdoimo.campominado.exception.ExplosionException;

public class Field {
	private final int line;
	private final int column;

	private boolean open = false;
	private boolean undermined = false;
	private boolean marked = false;

	private List<Field> neighbors = new ArrayList<>();

	public Field(int line, int column) {
		this.line = line;
		this.column = column;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
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

	public int getLine() {
		return line;
	}

	public int getColumn() {
		return column;
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
		}
	}

	public boolean putBomb() {
		if (!undermined) {
			undermined = Boolean.TRUE;
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public boolean openField() {
		if (!open && !marked) {
			open = Boolean.TRUE;

			if (undermined) {
				throw new ExplosionException();
			}

			if (neighborhoodSecurity()) {
				neighbors.forEach(neighbor -> neighbor.openField());
			}
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	@Override
	public String toString() {
		if (marked) {
			return "x";
		} else if (open && undermined) {
			return "*";
		} else if (open && bombsInNeighborhood() > 0) {
			return Long.toString(bombsInNeighborhood());
		} else if (open) {
			return " ";
		}
		return "?";
	}

	boolean goalAchieved() {
		boolean fildUnveiled = !undermined && open;
		boolean fieldProtected = undermined && marked;

		return fildUnveiled || fieldProtected;
	}

	Long bombsInNeighborhood() {
		return neighbors.stream().filter(n -> n.undermined).count();
	}

	void restart() {
		open = Boolean.FALSE;
		undermined = Boolean.FALSE;
		marked = Boolean.FALSE;
	}

	private boolean neighborhoodSecurity() {
		return neighbors.stream().noneMatch(neighbor -> neighbor.undermined);
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
