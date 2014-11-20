package controller;

import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import model.Drawing;
import model.IFigure;
import util.DrawingListener;
import view.BoundBox;
import view.ControlPoint;
import view.MainFrame;

// App: Mediator & Singleton & Decorator
// Canvas: Controller & State
// Group: Composite
// Drawing: Iterator & Observer
// MainFrame: Observer
// Figure: Template Method
// CreationTool: Template Method
// MenuManager: Command
public final class App {

	private App() {
	
		drawing = new Drawing();
		mainFrame = new MainFrame( "Editor Gráfico v7E-11" );
	}
	
	private void run() {

		mainFrame.setBounds( 100, 100, 800, 600 );
		mainFrame.setExtendedState( Frame.MAXIMIZED_BOTH );
		mainFrame.init();
	}

	public synchronized static App getInstance() {
		
		if ( app == null ) {
			
			app = new App();
		}
		
		return app;
	}
	
	public void paint(Graphics2D g) {
	
		drawing.paint( g );
	} 
	
	public void repaint() {
		
		mainFrame.repaintCanvas();
	}
	
	public void add(IFigure f) {
	
		drawing.add(f);
		repaint();
	}
	
	public void select( final Point pt ) {
		
		drawing.select( pt );
		repaint();
	}
	
	public void select( final BoundBox bb ) {
		
		drawing.select( bb );
		repaint();
	}
	
	public void deselectAll() {
		
		drawing.deselectAll();  
		repaint();
	}

	public void newDrawing() {

		int r = saveDrawingIfModified();
		if ( r == JOptionPane.CANCEL_OPTION ) {
			
			// noop
		}
		else {
			
			drawing.clear();
			repaint();
		}
	}

	// decorator
	private void doLoadDrawing() {
	
		JFileChooser fc = new JFileChooser( System.getProperty( "user.home" ) );
		fc.setFileSelectionMode( JFileChooser.FILES_ONLY );
		fc.setFileFilter( new FileFilter() {
			
			@Override
			public String getDescription() {

				return "EG files";
			}
			
			@Override
			public boolean accept(File f) {
				
				return f.getName().endsWith( EG_SUFFIX );
			}
		} );
		fc.showOpenDialog( mainFrame );
		
		File f = fc.getSelectedFile();
		if ( f != null ) {
		
			try {
				
				ObjectInputStream ois = new ObjectInputStream(
											new FileInputStream( f ) );
				drawing.load( ois );
				ois.close();
				
				drawing.setName( f.getAbsolutePath() );
				repaint();
				
			} catch (Exception e) {
			
				// TODO tell the user
			}
		}
		else {
			
			// noop
		}
	}

	public void loadDrawing() {

		int r = saveDrawingIfModified();
		if ( r == JOptionPane.CANCEL_OPTION ) {
			
			// noop
		}
		else {
			
			doLoadDrawing();
		}			
	}
	
	// decorator
	public boolean saveDrawing() {
		
		boolean r = false;
		
		String name = drawing.getName(); 
		if ( name.isEmpty() ) {
			
			JFileChooser fc = new JFileChooser( System.getProperty( "user.home" ) );
			fc.showSaveDialog( mainFrame );
			
			File f = fc.getSelectedFile();
			if ( f != null ) {

				name = f.getAbsolutePath();
			}
		}
		
		if ( name.isEmpty() ) {
			
			// user cancelled
		}
		else {
			
			if ( name.endsWith( EG_SUFFIX ) ) {
				
				// OK
			}
			else {
			
				name += EG_SUFFIX;
			}
			
			try {
				
				File f = new File( name );
				// TODO if ( f.canWrite() )
				// TODO if ( f.exists() )
				ObjectOutputStream oos = new ObjectOutputStream(
											new FileOutputStream( f ) );
				drawing.save( oos );
				oos.close();
				
				drawing.setName( f.getAbsolutePath() );
				r = true;
				
			} catch (Exception e) {
			
				// TODO tell the user
			}
		}
		
		return r;
	}
	
	protected int saveDrawingIfModified() {
		
		int r = JOptionPane.YES_OPTION;
				
		if ( drawing.isModified() ) {

			r = JOptionPane.showConfirmDialog( 
					mainFrame, 
					"Doc has been modified, do you want to save it before continuing?",
					"Save Drawing",
					JOptionPane.YES_NO_CANCEL_OPTION, 
					JOptionPane.QUESTION_MESSAGE );
			
			if ( r == JOptionPane.YES_OPTION ) {
				
				if ( saveDrawing() ) {
					
					// OK
				}
				else {
					
					// noop (user cancelled)
				}
			}
			else {
				
				// noop
			}
		}
		else {
			
			// noop
		}
		
		return r;
	}
		
	public void quit() {

		int r = saveDrawingIfModified();
		if ( r == JOptionPane.CANCEL_OPTION ) {
			
			// noop
		}
		else {
			
			exit();
		}
	}

	private void exit() {
		
		// TODO save user options
		System.exit( 0 );
	}
	
	public void group() {

		drawing.group();
		repaint();
	}
	
	public void delete() {
		
		drawing.delete();
		repaint();
		
	}
	
	public void undo() {
		
		drawing.undo();
		repaint();
		
	}
	
	public void redo() {
		
		drawing.redo();
		repaint();
		
	}


	public void drawRubberBand( final BoundBox rb ) {

		mainFrame.drawRubberBand( rb );
	}

	public ControlPoint controlPointAt( final Point pt ) {

		return drawing.controlPointAt( pt );
	}

	public IFigure selectedFigureAt( final Point pt ) {

		return drawing.selectedFigureAt( pt );
	}

	public void addDrawingListener( final DrawingListener dl ) {

		drawing.addListener( dl );
	}

	public void remDrawingListener( final DrawingListener dl ) {

		drawing.remListener( dl );
	}
	
	public static void main(String[] args) throws Exception {
	
		App app = getInstance();
		app.run();
	}

	private static App app;
	private MainFrame mainFrame;
	private Drawing drawing;
	
	public static final String EG_SUFFIX = ".eg";


}
