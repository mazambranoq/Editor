package view;

import java.awt.event.MouseEvent;

import model.IFigure;
import controller.App;

public abstract class CreationTool extends Tool {

	protected abstract IFigure createFigure();
	
	@Override
	public void mouseDragged( final MouseEvent me ) {

		// TODO show feedback
	}
	
	@Override // template method
	public final void mouseReleased(MouseEvent me) {
		
		super.mouseReleased(me);
		
		// 1. check pressed and released points
		if ( ptPressed.equals( ptReleased ) ) {
			
			// noop
		}
		else {
			
			// 2. create figure
			IFigure f = createFigure();
			if ( f != null ) {
				
				// 3. add figure to drawing
				App.getInstance().add( f );
			}
			else {
				
				throw new IllegalStateException( "CreationTool::createFigure() failed!" );
			}
		}
	}
}
