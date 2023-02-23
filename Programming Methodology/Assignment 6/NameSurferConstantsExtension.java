/*
 * File: NameSurferConstants.java
 * ------------------------------
 * This file declares several constants that are shared by the
 * different modules in the NameSurfer application.  Any class
 * that implements this interface can use these constants.
 */

public interface NameSurferConstantsExtension {

/** The width of the application window */
	public static final int APPLICATION_WIDTH = 800;

/** The height of the application window */
	public static final int APPLICATION_HEIGHT = 600;

/** The name of the file containing the data */
	public static final String NAMES_DATA_FILE = "names-data.txt";

/** The first decade in the database */
	public static final int START_DECADE = 1900;

/** The number of decades */
	public static final int NDECADES = 11;

/** The maximum rank in the database */
	public static final int MAX_RANK = 1000;

	// extension constants
	public static final int OFFSET = 40;
	public static final int SCALE_NUM = 10;
	public static final double GROWTH_SPEED = 0.1;

}
