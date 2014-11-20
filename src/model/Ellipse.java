package model;

import java.awt.Graphics2D;

import view.BoundBox;

public class Ellipse extends GeometricFigure implements IFigure {

	private static final long serialVersionUID = 1L;

	public Ellipse( final BoundBox bbox ) {
		
		super(bbox);
	}

	@Override
	protected void doPaint( final Graphics2D g ) {

		BoundBox bbox = getBoundBox();
		g.drawOval( bbox.x, bbox.y, bbox.width, bbox.height );
	}
}
