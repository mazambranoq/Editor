package model;

import java.awt.Graphics2D;
import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;
import javax.swing.undo.UndoableEditSupport;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import util.DrawingListener;
import util.DrawingListener.DrawingEvent;
import view.BoundBox;
import view.ControlPoint;

public class Drawing {
	
	class AddMemento extends AbstractUndoableEdit {

		public AddMemento( IFigure f)
		{
			this.f = f;	
		}
		
		public void undo() throws CannotUndoException
		{
			elements.remove( f );
		}
		
		public void redo() throws CannotRedoException
		{
			elements.add( f );
		}

		public boolean canUndo()
		{
			return true;
		}

		public boolean canRedo()
		{
			return true;
		}
		
		private IFigure f;
		private static final long serialVersionUID = 1L;
		
	};
	
	class RemoveMemento extends AbstractUndoableEdit {
	
		public RemoveMemento( IFigure f, int index  ){
			this.f = f;	
			this.i = index;
		}
		
		public void undo() throws CannotUndoException
		{
			elements.add( i , f );
		}
		
		public void redo() throws CannotRedoException
		{
			elements.remove( f );
		}

		public boolean canUndo()
		{
			return true;
		}

		public boolean canRedo()
		{
			return true;
		}
		
		private IFigure f;
		private int i;
		private static final long serialVersionUID = 1L;
		
	};
	
	public Drawing() {
		
		m_undoManager = new UndoManager();
		m_undoSupport = new UndoableEditSupport();
		m_undoSupport.addUndoableEditListener( new UndoAdapter() );
		elements = new LinkedList<IFigure>();
		name = "";
		version = VERSION;
		
		listeners = new LinkedList<DrawingListener>();
	}

	public void addListener( DrawingListener dl ) {
	
		assert dl != null;
		
		listeners.add( dl );
	}

	public void remListener( DrawingListener dl ) {
	
		assert dl != null;
		
		listeners.remove( dl );
	}
	
	private void notifyListeners( final DrawingEvent de ) {
	
		// iterator
		for ( DrawingListener dl : listeners ) {
	
			dl.processDrawingEvent( de );
		}
	}
	
	public void add( IFigure figure ) {
		
		assert( figure != null );
		
		UndoableEdit edit = new AddMemento( figure );
		
		setModified( elements.add( figure ) );
		assert ( edit != null);
		
		m_undoSupport.postEdit( edit );
		
	}

	public void remove( IFigure figure ) {
		
		assert( figure != null );
		
		UndoableEdit edit = new RemoveMemento ( figure, elements.indexOf( figure ) );
		
		setModified( elements.remove( figure ) );
		assert ( edit != null);
		
		m_undoSupport.postEdit( edit );
		
	}
	
	public void paint(Graphics2D g) {
	
		// iterator
		for ( IFigure f : elements ) {
			
			f.paint( g );
		}
	} 
	
	public void select( final Point pt ) {
		
		// explicit iterator
		Iterator<IFigure> it = elements.descendingIterator();
		while ( it.hasNext() ) {
			 
			IFigure f = it.next();
			
			if ( f.contains( pt ) ) {
				
				f.setSelected( true );
				break;
			}
		}
	}
	
	// PRE-CONDITION: bb normalized
	public void select( final BoundBox bb ) {
		
		assert bb != null;
		assert bb.isNormalized();
		
		for ( IFigure f : elements ) {
		
			if ( f.contained( bb ) ) {
				
				f.setSelected( true );
			}
		}
	}
	
	public void deselectAll() {

		for ( IFigure f : elements ) {
			
			f.setSelected( false );
		}
	}

	public boolean isModified() {

		return modified;
	}

	protected void setModified( boolean b ) {

		modified = b;
		
		notifyListeners( 
				modified ? DrawingEvent.MODIFIED : DrawingEvent.SAVED );
	}
	
	public String getName() {
		
		return name;
	}

	public void setName(String name) {
		
		this.name = name;
	}

	public void clear() {

		elements.clear();
		setName( "" );
		setModified( false );
	}

	public void save(ObjectOutputStream oos) {

		try {
			oos.writeInt( version );
			oos.writeObject( elements );
			oos.writeObject( name );
			
			setModified( false );
			
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void load(ObjectInputStream ois) {
		
		try {
			version = ois.readInt();
			
			if ( version >= 1 ) {
			
				elements = (LinkedList<IFigure>)ois.readObject();
				name = (String)ois.readObject();
			}
			else if ( version >= 2 ) {
				
				// ...
			}
			
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public int selected() {

		int n = 0;
		
		for ( IFigure f : elements ) {
			
			if ( f.isSelected() ) {
				
				n++;
			}
		}
		
		return n;
	}
	
	public void delete() {
	
		if ( selected() > 1 ) {
			
			for ( IFigure f : elements ) {
				
				if ( f.isSelected() ) {
					System.out.println("jajajaja");
					elements.remove( f );
					
				}
			}
		}
	}
	
	public void undo() {
		
		m_undoManager.undo();
		
	}
	
	public void redo() {
		
		m_undoManager.redo();
		
	}
	
	public void group() {

		if ( selected() > 1 ) {
			
			LinkedList<IFigure> children = new LinkedList<IFigure>();
			LinkedList<IFigure> newlist = new LinkedList<IFigure>();
			
			for ( IFigure f : elements ) {
				
				if ( f.isSelected() ) {
					
					children.add( f );
				}
				else {
					
					newlist.add( f );
				}
			}
			
			elements = newlist;

			add( new Group( children ) );
		}
	}

	public ControlPoint controlPointAt( final Point pt ) {

		assert pt != null;
		
		ControlPoint cp = null;
		BoundBox bbox = new BoundBox( pt.x - CP_AREA, pt.y - CP_AREA,  2 * CP_AREA, 2 * CP_AREA );
		
		for ( IFigure f : elements ) {
			
			if ( f.isSelected() ) {
				
				cp = f.ctrlPointInBoundBox( bbox );
				if ( cp != null ) {
					
					break;
				}
			}
		}

		return cp;
	}

	public IFigure selectedFigureAt( final Point pt ) {

		IFigure r = null;
		
		for ( IFigure f : elements ) {
			
			if ( f.isSelected() && f.contains( pt ) ) {
				
				r = f;
				break;
			}
		}
		
		return r;
	}
	
	private class UndoAdapter implements UndoableEditListener
	{
		public void undoableEditHappened( UndoableEditEvent evt )
		{
			UndoableEdit edit = evt.getEdit();
			m_undoManager.addEdit( edit );
			
		}
	}

	private LinkedList<IFigure> elements;
	private List<DrawingListener> listeners;
	UndoManager m_undoManager; // history list
	UndoableEditSupport m_undoSupport; // event support
	
	private boolean modified;
	private String name;
	private int version;
	
	public static final int VERSION = 1;
	public static final int CP_AREA = 5;

}
