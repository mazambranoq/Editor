package util;

public interface DrawingListener {

	public static enum DrawingEvent {
	
		MODIFIED, SAVED, SELECTED, // ...
	}
	
	// push observer model
	void processDrawingEvent( final DrawingEvent de );
}
