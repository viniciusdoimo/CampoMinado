package br.com.viniciusdoimo.campominado.view;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

import br.com.viniciusdoimo.campominado.model.EventField;
import br.com.viniciusdoimo.campominado.model.Field;
import br.com.viniciusdoimo.campominado.model.FieldObserver;

@SuppressWarnings("serial")
public class Button extends JButton implements FieldObserver, MouseListener {

	private final Color BG_DEFAULT = new Color(184, 184, 184);
	private final Color BG_MARKET = new Color(8, 179, 247);
	private final Color BG_EXPLODE = new Color(189, 66, 68);
	private final Color TEXT_GREEN = new Color(0, 100, 0);

	private Field field;

	public Button(Field field) {
		this.field = field;
		setOpaque(Boolean.TRUE);
		setBorder(BorderFactory.createBevelBorder(0));

		addMouseListener(this);
		field.registerObservers(this);
	}

	@Override
	public void occurredEvent(Field field, EventField event) {
		switch (event) {
		case OPEN:
			styleOpen();
			break;
		case MARK:
			styleMarket();
			break;
		case EXPLODE:
			styleExplode();
			break;
		default:
			styleDefault();
		}
		
		SwingUtilities.invokeLater(() -> {
			repaint();
			validate();
		});
	}

	private void styleDefault() {
		setBackground(BG_DEFAULT);
		setBorder(BorderFactory.createBevelBorder(0));
		setText("");
	}

	private void styleExplode() {
		setBackground(BG_EXPLODE);
		setForeground(Color.WHITE);
		setText("x");
	}

	private void styleMarket() {
		setBackground(BG_MARKET);
		setForeground(Color.BLACK);
		setText("M");
	}

	private void styleOpen() {
		
		setBorder(BorderFactory.createLineBorder(Color.GRAY));
		
		if (field.isUndermined()) {
			setBackground(BG_EXPLODE);
			return;
		}
		
		setBackground(BG_DEFAULT);

		switch (field.bombsInNeighborhood()) {
		case 1:
			setForeground(TEXT_GREEN);
			break;
		case 2:
			setForeground(Color.BLUE);
			break;
		case 3:
			setForeground(Color.YELLOW);
			break;
		case 4:
		case 5:
		case 6:
			setForeground(Color.RED);
			break;
		default:
			setForeground(Color.PINK);
		}

		String value = !field.neighborhoodSecurity() ? field.bombsInNeighborhood() + "" : "";
		setText(value);
	}

	// Methods Interface Mouse
	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == 1) {
			field.openField();
		} else {
			field.changeChecked();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	}
}
