package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.Serializable;

public class ControlPoint implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static enum Cardinal {
		
		N, NE, E, SE, S, SW, W, NW
	}
	
	public ControlPoint( final Cardinal cd ) {
		
		super();

		this.cd = cd;
		this.pos = new Point();
	}

	// PRE-CONDITION: bb normalized
	public Point getPosition( final BoundBox bb ) {

		assert bb != null;
		assert bb.isNormalized();
	
		switch ( cd ) {
		
			case N:
				pos.x = bb.x + bb.width / 2;
				pos.y = bb.y;
				break;
				
			case NE:
				pos.x = bb.x + bb.width;
				pos.y = bb.y;
				break;
				
			case E:
				pos.x = bb.x + bb.width;
				pos.y = bb.y + bb.height / 2;
				break;
				
			case SE:
				pos.x = bb.x + bb.width;
				pos.y = bb.y + bb.height;
				break;
				
			case S:
				pos.x = bb.x + bb.width / 2;
				pos.y = bb.y + bb.height;
				break;
				
			case SW:
				pos.x = bb.x;
				pos.y = bb.y + bb.height;
				break;
				
			case W:
				pos.x = bb.x;
				pos.y = bb.y + bb.height / 2;
				break;
				
			case NW:
				pos.x = bb.x;
				pos.y = bb.y;
				break;
		}
		
		return pos;
	}
	
	public void paint( final Graphics2D g, final BoundBox bbox ) {

		assert g != null;
		assert bbox != null;
		
		Point pt = getPosition( bbox );
		
		g.setColor( COLOR );
		g.fillRect( pt.x - HSIZE, pt.y - HSIZE, 2 * HSIZE, 2 * HSIZE );
	}

	public Cursor getCursor() {
		
		Cursor cursor = null;
		
		switch ( cd ) {
		
			case N:
				cursor = Cursor.getPredefinedCursor( Cursor.N_RESIZE_CURSOR );
				break;
				
			case NE:
				cursor = Cursor.getPredefinedCursor( Cursor.NE_RESIZE_CURSOR );
				break;
				
			case E:
				cursor = Cursor.getPredefinedCursor( Cursor.E_RESIZE_CURSOR );
				break;
				
			case SE:
				cursor = Cursor.getPredefinedCursor( Cursor.SE_RESIZE_CURSOR );
				break;
				
			case S:
				cursor = Cursor.getPredefinedCursor( Cursor.S_RESIZE_CURSOR );
				break;
				
			case SW:
				cursor = Cursor.getPredefinedCursor( Cursor.SW_RESIZE_CURSOR );
				break;
				
			case W:
				cursor = Cursor.getPredefinedCursor( Cursor.W_RESIZE_CURSOR );
				break;
				
			case NW:
				cursor = Cursor.getPredefinedCursor( Cursor.NW_RESIZE_CURSOR );
				break;
		}
		
		return cursor;
	}
	
	private Cardinal cd;
	private Point pos;
	
	public static final Color COLOR = Color.LIGHT_GRAY;
	public static final int HSIZE = 3;
}
