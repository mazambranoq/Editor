package model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import view.BoundBox;
import view.ControlPoint;
import view.ControlPoint.Cardinal;

public abstract class Figure implements IFigure {

	private static final long serialVersionUID = 1L;

	protected abstract void doPaint( final Graphics2D g );
	
	protected Figure( final BoundBox bbox ) {

		super();
		this.bbox = bbox;
		
		if ( needsNormalize() ) {
			
			this.bbox.normalize();
		}

		buildControlPoints();
		
		color = Color.RED;
	}

	protected void buildControlPoints() {
		
		ctrlPoints = new ControlPoint[ NUM_CPOINTS ];
		Cardinal[] ca = Cardinal.values();
		
		for ( int i = 0; i < NUM_CPOINTS; i++ ) {
			
			ctrlPoints[ i ] = new ControlPoint( ca[ i ] );
		}
	}

	protected boolean needsNormalize() {
		
		return true;
	}

	public final void paint( final Graphics2D g ) {

		// 1. set color
		g.setColor( color );
		
		// 2. do paint
		doPaint( g );
		
		// 3. paint bbox
		if ( selected ) {
		
			paintBoundBox( g );
			paintControlPoints( g );
		}
	}

	private void paintBoundBox( final Graphics2D g ) {

		bbox.paint( g );
	}

	private void paintControlPoints( final Graphics2D g ) {

		for ( int i = 0; i < NUM_CPOINTS; i++ ) {

			ctrlPoints[ i ].paint( g, bbox.normalized() );
		}
	}

	public boolean isSelected() {
		
		return selected;
	}

	public void setSelected( boolean b ) {
	
		selected = b;	
	}

	public BoundBox getBoundBox() {
		
		return bbox;
	}

	public boolean contains( final Point pt ) {
		
		BoundBox bb = bbox.normalized();
		
		return bb.contains( pt );
	}
	
	// PRE-CONDITION: bb normalized
	public boolean contained( final BoundBox bb ) {
		
		assert bb != null;
		assert bb.isNormalized();
		
		return bb.contains( needsNormalize() ? bbox.normalized() : bbox );
	}

	// PRE-CONDITION: bb normalized
	public ControlPoint ctrlPointInBoundBox( final BoundBox bb ) {
		
		assert bb != null;
		assert bb.isNormalized();
	
		ControlPoint cp = null;
		
		for ( int i = 0; i < NUM_CPOINTS; i++ ) {

			Point pt = ctrlPoints[ i ].getPosition( bbox.normalized() );
			if ( bb.contains( pt ) ) {
				
				cp = ctrlPoints[ i ];
				break;
			}
		}
		
		return cp;
	}
	
	public int numChildren() {
		
		return 0;
	}
	
	private static final int NUM_CPOINTS = Cardinal.values().length;
	
	// TODO move to BoundBox class 
	protected ControlPoint[] ctrlPoints;
	
	protected boolean selected;
	protected BoundBox bbox;
	protected Color color;
}
