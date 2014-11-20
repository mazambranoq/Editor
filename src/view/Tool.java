package view;

import java.awt.Point;
import java.awt.event.MouseEvent;

public abstract class Tool {

	public abstract void mouseDragged( final MouseEvent me );
	
	public void mousePressed(MouseEvent me) {
		
		ptPressed = me.getPoint();
	}

	public void mouseReleased(MouseEvent me) {
		
		ptReleased = me.getPoint();
	}
	
	protected Point ptPressed;
	protected Point ptReleased;
}
