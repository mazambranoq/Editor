package model;

import java.awt.Graphics2D;

import view.BoundBox;

public class Line extends GeometricFigure implements IFigure {

	private static final long serialVersionUID = 1L;

	public Line( final BoundBox bbox ) {
		
		super(bbox);
	}

	@Override
	protected boolean needsNormalize() {
		
		return false;
	}
		
	@Override
	protected void doPaint( final Graphics2D g ) {

		BoundBox bbox = getBoundBox();
		g.drawLine( bbox.x, bbox.y, bbox.x + bbox.width, bbox.y + bbox.height );
	}
}
