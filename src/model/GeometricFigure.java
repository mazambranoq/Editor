package model;

import view.BoundBox;

public abstract class GeometricFigure extends Figure {

	private static final long serialVersionUID = 1L;

	public GeometricFigure(BoundBox bbox) {
		super(bbox);

		setLineWidth( 1 );
	}
	
	public int getLineWidth() {
		return lineWidth;
	}

	public void setLineWidth(int lineWidth) {
		this.lineWidth = lineWidth;
	}

	private int lineWidth;
}
