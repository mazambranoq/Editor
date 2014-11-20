package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;

public class BoundBox extends Rectangle {

	private static final long serialVersionUID = 1L;

	// default constructor 
	public BoundBox() {
		
		super( 0, 0, 0, 0 );
	}

	// explicit constructor 
	public BoundBox( int x, int y, int width, int height ) {
		
		super( x, y, width, height );
		this.normalize();
	}

	// copy constructor 
	public BoundBox( final BoundBox bb ) {
		
		super( bb.x, bb.y, bb.width, bb.height );
	}

	public BoundBox( final Point pt1, final Point pt2 ) {
		
		super( pt1.x, pt1.y, pt2.x - pt1.x, pt2.y - pt1.y );
	}

	public BoundBox normalize() {
		
		if ( width < 0 ) {
			
			width *= -1;
			x -= width;
		}

		if ( height < 0 ) {
			
			height *= -1;
			y -= height;
		}
		
		return this;
	}

	public void paint( final Graphics2D g ) {

		// save stroke
		Stroke stroke = g.getStroke();
		
		g.setStroke( DASHED );
		g.setColor( COLOR );
		g.drawRect( x, y, width, height );

		// recover stroke
		g.setStroke( stroke );
	}

	public boolean isNormalized() {
	
		return (width >= 0 && height >= 0);
	}
	
	public BoundBox normalized() {
		
		return (isNormalized() ? this : new BoundBox( this ).normalize());
	}
	
	public BoundBox union( final BoundBox bbox ) {
		
		Rectangle r = super.union( bbox );
		
		return new BoundBox( r.x, r.y, r.width, r.height );
	}

	public static final Color COLOR = ControlPoint.COLOR;
	public static final BasicStroke DASHED = new BasicStroke(
		1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{ 2.0f, 2.0f }, 0.0f );
}
