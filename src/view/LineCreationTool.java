package view;

import model.IFigure;
import model.Line;

public class LineCreationTool extends CreationTool {

	@Override
	protected IFigure createFigure() {
	
		return new Line( 
			new BoundBox( ptPressed, ptReleased ) );
	}
}
