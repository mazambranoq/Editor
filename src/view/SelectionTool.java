package view;

import java.awt.Point;
import java.awt.event.MouseEvent;

import controller.App;

public class SelectionTool extends Tool {

	@Override
	public void mousePressed( final MouseEvent me ) {
		
		super.mousePressed( me );
		
		// TODO check keyboard
		App.getInstance().deselectAll();

		rubberBand.x = me.getX();
		rubberBand.y = me.getY();
		rubberBand.setSize( 0, 0 );
	}
	
	@Override
	public void mouseDragged( final MouseEvent me ) {
		
		// delete previous bound box
		App.getInstance().drawRubberBand( rubberBand );
		
		rubberBand.setSize( me.getX() - rubberBand.x, me.getY() - rubberBand.y );
		
		// draw new one
		App.getInstance().drawRubberBand( rubberBand );
	}

	@Override
	public void mouseReleased( final MouseEvent me ) {
		
		super.mouseReleased( me );
		
		if ( ptPressed.equals( ptReleased ) ) {
			
			select( ptPressed );
		}
		else {
			
			select( new BoundBox( ptPressed, ptReleased ).normalized() );
		}
	}
	
	protected void select( final Point pt ) {
		
		App.getInstance().select( pt );
	}
	
	protected void select( final BoundBox bb ) {
		
		App.getInstance().select( bb );
	}

	private BoundBox rubberBand = new BoundBox();
}
