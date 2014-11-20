package model;

import java.awt.Graphics2D;
import java.awt.Point;
import java.io.Serializable;

import view.BoundBox;
import view.ControlPoint;

public interface IFigure extends Serializable {

	void paint( final Graphics2D g );

	BoundBox getBoundBox();
	
	boolean contains( final Point pt );
	boolean contained( final BoundBox bb );

	void setSelected( boolean b );
	boolean isSelected();

	ControlPoint ctrlPointInBoundBox( final BoundBox bbox );
}
