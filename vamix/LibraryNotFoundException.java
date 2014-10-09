package vamix;

@SuppressWarnings("serial")
public class LibraryNotFoundException extends Exception {
	
	public LibraryNotFoundException() {}

    //Constructor that accepts a message
    public LibraryNotFoundException(String message)
    {
       super(message);
    }

}
