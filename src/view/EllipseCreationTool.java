package view;

import model.Ellipse;
import model.IFigure;

public class EllipseCreationTool extends CreationTool {

	@Override
	protected IFigure createFigure() {
	
		return new Ellipse( 
			new BoundBox( ptPressed, ptReleased ) );
	}
}
