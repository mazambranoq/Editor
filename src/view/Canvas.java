package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import model.IFigure;
import controller.App;

// controller & state
public class Canvas extends JPanel {
	
	private static final long serialVersionUID = 1L;

	public Canvas() {
		
		super();
		
		tools = new Tool[ NUM_TOOLS ];
		tools[ SELECTION ] = new SelectionTool();
		tools[ LINE ] = new LineCreationTool();
// TODO		tools[ RECTANGLE ] = new RectangleCreationTool();
		tools[ ELLIPSE ] = new EllipseCreationTool();
// TODO		tools[ TEXT ] = new TextCreationTool();
		
		curTool = tools[ LINE ];
		
		// state & adapter patterns
		addMouseListener( new MouseAdapter() {
			
			@Override
			public void mousePressed( final MouseEvent me ) {
				
				curTool.mousePressed( me );
			}
			
			@Override
			public void mouseReleased( final MouseEvent me ) {
				
				curTool.mouseReleased( me );
			}
		} );
		
		// state & adapter patterns
		addMouseMotionListener( new MouseAdapter() {
			
			@Override
			public void mouseMoved( final MouseEvent me ) {
				
				setCursor( me.getPoint() );
			}
			
			@Override
			public void mouseDragged( final MouseEvent me ) {
				
				curTool.mouseDragged( me );
			}
		} );
	}

	protected void setCursor( final Point pt ) {
		
		ControlPoint cp = App.getInstance().controlPointAt( pt );
		if ( cp == null ) {
			
			IFigure f = App.getInstance().selectedFigureAt( pt );
			if ( f == null ) {
				
				setCursor( Cursor.getDefaultCursor() );
			}
			else {

				setCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) );
			}
		}
		else {
			
			setCursor( cp.getCursor() );
		}
	}
	
	public void selectTool( int t ) {

		if ( 0 <= t && t < NUM_TOOLS ) {
			
			curTool = tools[ t ];
		}
		else {
			
			throw new IllegalArgumentException( "tool="+ t );
		}
	}

	// PRE-CONDITION: bb normalized
	public void drawRubberBand( final BoundBox rb ) {

		assert( rb != null );
		BoundBox bbox = rb.normalized();
		
		Graphics2D g2d = (Graphics2D)getGraphics();
		
		g2d.setXORMode( getBackground() );
		g2d.setColor( Color.LIGHT_GRAY );
		g2d.setStroke( DASHED );
		g2d.drawRect( bbox.x, bbox.y, bbox.width, bbox.height );

		g2d.dispose();
	}

	@Override
	public void paint( final Graphics g ) {

		// paint background
		super.paint( g );
		
		// paint drawing
		App.getInstance().paint( (Graphics2D)g );
	}
	
	private Tool curTool;
	private Tool[] tools;

	public static final BasicStroke DASHED = new BasicStroke(
		1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{ 4.0f, 4.0f }, 0.0f );
	
	public static final int SELECTION = 0;
	public static final int LINE = 1;
	public static final int RECTANGLE = 2;
	public static final int ELLIPSE = 3;
	public static final int TEXT = 4;
	public static final int NUM_TOOLS = 5;
}
