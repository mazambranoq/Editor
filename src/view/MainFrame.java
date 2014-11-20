package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import util.DrawingListener;
import controller.App;

// Observer
public class MainFrame extends JFrame
						implements DrawingListener {

	private static final long serialVersionUID = 1L;

	public MainFrame( String title ) throws HeadlessException {
		super( title );

		
		setDefaultCloseOperation( DO_NOTHING_ON_CLOSE );
		
		canvas = new Canvas();
		canvas.setBackground( Color.WHITE );
		add( canvas, BorderLayout.CENTER );
		
		// TODO move to MenuManager
		JMenu fileMenu = new JMenu( "File" );
		JMenuItem fileNew = new JMenuItem( "New" );
		fileNew.addActionListener( new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				App.getInstance().newDrawing();
			}
		});

		JMenuItem fileSave = new JMenuItem( "Save" );
		fileSave.addActionListener( new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				App.getInstance().saveDrawing();
			}
		});

		JMenuItem fileLoad = new JMenuItem( "Load" );
		fileLoad.addActionListener( new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				App.getInstance().loadDrawing();
			}
		});

		JMenuItem fileQuit = new JMenuItem( "Quit" );
		fileQuit.addActionListener( new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				App.getInstance().quit();
			}
		});
		
		fileMenu.add( fileNew );
		fileMenu.add( fileLoad );
		fileMenu.add( fileSave );
		fileMenu.addSeparator();
		fileMenu.add( fileQuit );

		JMenu editMenu = new JMenu( "Edit" );
		JMenuItem editGroup = new JMenuItem( "Group" );

		editGroup.addActionListener( new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				App.getInstance().group();
			}
		});
		
		JMenuItem delete = new JMenuItem( "Delete" );		
		delete.addActionListener( new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
			
				App.getInstance().delete();	
			}
		});

		
		JMenuItem undo = new JMenuItem( "Undo" );		
		undo.addActionListener( new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				App.getInstance().undo();
				
			}
		});
		
		JMenuItem redo = new JMenuItem( "Redo" );		
		redo.addActionListener( new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				App.getInstance().redo();
				
			}
		});
		
		
		editMenu.add( editGroup );
		editMenu.add( delete );
		editMenu.add( undo );
		editMenu.add( redo ); 
		
		
		

		JMenu toolMenu = new JMenu( "Tool" );
		JMenu helpMenu = new JMenu( "Help" );
		
		JMenuItem selTool = new JMenuItem( "Selection Tool" );
		JMenuItem linTool = new JMenuItem( "Line Tool" );
		JMenuItem ellTool = new JMenuItem( "Ellipse Tool" );

		selTool.addActionListener( new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				canvas.selectTool( Canvas.SELECTION );
			}
		});

		linTool.addActionListener( new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				canvas.selectTool( Canvas.LINE );
			}
		});

		ellTool.addActionListener( new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				canvas.selectTool( Canvas.ELLIPSE );
			}
		});
		
		toolMenu.add( linTool );
		toolMenu.add( ellTool );
		toolMenu.addSeparator();
		toolMenu.add( selTool );
		
		menuBar = new JMenuBar();
		menuBar.add( fileMenu );
		menuBar.add( editMenu );
		menuBar.add( toolMenu );
		menuBar.add( helpMenu );
		
		add( menuBar, BorderLayout.NORTH );
	}
	
	public void init() {
		
		App.getInstance().addDrawingListener( this );
		
		setVisible( true );
	}
	
	public void repaintCanvas() {
		
		canvas.repaint();
	}

	public void drawRubberBand( final BoundBox rb ) {

		canvas.drawRubberBand( rb );
	}

	public void processDrawingEvent( final DrawingEvent de ) {

		switch ( de ) {
		
			case MODIFIED:
				// TODO
				break;
				
			case SAVED:
				// TODO
				break;
				
			case SELECTED:
				// noop
				break;
				
			default: throw new UnsupportedOperationException( "MainFrame::processDrawingEvent(): " + 
						de );
		}
	}
	
	private Canvas canvas;
	private JMenuBar menuBar;
}
