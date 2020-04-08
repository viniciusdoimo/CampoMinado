package br.com.viniciusdoimo.campominado.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.viniciusdoimo.campominado.exception.ExplosionException;
import br.com.viniciusdoimo.campominado.model.Field;

class FieldTest {
	private Field field;

	@BeforeEach
	void startFiled() {
		field = new Field(3, 3);
	}

	@Test
	void testNeighborWest() {
		Field Neighbor = new Field(3, 2);
		boolean result = field.addNeighbor(Neighbor);
		assertTrue(result);
	}

	@Test
	void testNeighborEast() {
		Field Neighbor = new Field(3, 4);
		boolean result = field.addNeighbor(Neighbor);
		assertTrue(result);
	}

	@Test
	void testNeighborNorth() {
		Field Neighbor = new Field(2, 3);
		boolean result = field.addNeighbor(Neighbor);
		assertTrue(result);
	}

	@Test
	void testNeighborSouth() {
		Field Neighbor = new Field(4, 3);
		boolean result = field.addNeighbor(Neighbor);
		assertTrue(result);
	}

	@Test
	void testNeighborNortheast() {
		Field Neighbor = new Field(2, 4);
		boolean result = field.addNeighbor(Neighbor);
		assertTrue(result);
	}

	@Test
	void testNeighborSoutheast() {
		Field Neighbor = new Field(4, 4);
		boolean result = field.addNeighbor(Neighbor);
		assertTrue(result);
	}

	@Test
	void testNeighborSouthwest() {
		Field Neighbor = new Field(4, 2);
		boolean result = field.addNeighbor(Neighbor);
		assertTrue(result);
	}

	@Test
	void testNeighborNorthwest() {
		Field Neighbor = new Field(2, 2);
		boolean result = field.addNeighbor(Neighbor);
		assertTrue(result);
	}

	@Test
	void testFakeNeighbor() {
		Field Neighbor = new Field(1, 1);
		boolean result = field.addNeighbor(Neighbor);
		assertFalse(result);
	}

	@Test
	void testCheckedDefaultValue() {
		assertFalse(field.isMarked());
	}

	@Test
	void testChangeChecked() {
		field.changeChecked();
		assertTrue(field.isMarked());
	}

	@Test
	void testChangeTwoChecked() {
		field.changeChecked();
		field.changeChecked();
		assertFalse(field.isMarked());
	}

	@Test
	void testOpenNotMinedUnmarked() {
		assertTrue(field.openField());
	}

	@Test
	void testOpenNotMinedmarked() {
		field.changeChecked();
		assertFalse(field.openField());
	}

	@Test
	void testOpenMinedmarked() {
		field.changeChecked();
		field.putBomb();
		assertFalse(field.openField());
	}

	@Test
	void testOpenMinedUnmarked() {
		field.putBomb();

		assertThrows(ExplosionException.class, () -> {
			assertFalse(field.openField());
		});
	}

	@Test
	void testOpenWithNeighbors() {
		Field neighbor = new Field(2, 2);
		Field neighborOfNeighbor = new Field(1, 1);

		neighbor.addNeighbor(neighborOfNeighbor);
		field.addNeighbor(neighbor);

		field.openField();

		assertTrue(neighbor.isOpen() && neighborOfNeighbor.isOpen());
	}

	@Test
	void testOpenWithNeighborMined() {
		Field neighbor = new Field(1, 1);
		Field neighborMined = new Field(1, 2);
		neighborMined.putBomb();

		Field neighborOfNeighbor = new Field(2, 2);
		neighborOfNeighbor.addNeighbor(neighbor);
		neighborOfNeighbor.addNeighbor(neighborMined);
		field.addNeighbor(neighborOfNeighbor);

		field.openField();

		assertTrue(neighborOfNeighbor.isOpen() && !neighbor.isOpen());
	}
}
