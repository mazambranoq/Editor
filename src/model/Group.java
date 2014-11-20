package model;

import java.awt.Graphics2D;
import java.util.LinkedList;

// Composite
public class Group extends Figure {

	private static final long serialVersionUID = 1L;

	public Group( final LinkedList<IFigure> children ) {
		
		super( children.get( 0 ).getBoundBox().normalized() );
		
		this.children = children;
		
		for ( IFigure f : children ) {
			
			f.setSelected( false );
			bbox = bbox.union( f.getBoundBox().normalized() );
		}

		setSelected( true );
	}

	@Override
	protected boolean needsNormalize() {
		
		return false;
	}

	@Override
	protected void doPaint( final Graphics2D g ) {

		for ( IFigure f : children ) {
			
			f.paint( g );
		}
	}

	@Override
	public int numChildren() {
		
		return children.size();
	}

	private LinkedList<IFigure> children;
}
