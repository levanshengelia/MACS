import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/*
 * File: NameSurferDataBase.java
 * -----------------------------
 * This class keeps track of the complete database of names.
 * The constructor reads in the database from a file, and
 * the only public method makes it possible to look up a
 * name and get back the corresponding NameSurferEntry.
 * Names are matched independent of case, so that "Eric"
 * and "ERIC" are the same names.
 */

public class NameSurferDataBase implements NameSurferConstants {

	private Map<String, NameSurferEntry> dataBase;

	/* Constructor: NameSurferDataBase(filename) */
	/**
	 * Creates a new NameSurferDataBase and initializes it using the data in the
	 * specified file. The constructor throws an error exception if the requested
	 * file does not exist or if an error occurs as the file is being read.
	 */
	public NameSurferDataBase(String filename) {
		dataBase = new HashMap<>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			while (true) {
				String line = br.readLine();
				if (line == null) {
					break;
				}
				String name = line.substring(0, line.indexOf(' '));
				dataBase.put(name, new NameSurferEntry(line));
			}
			br.close();
		} catch (Exception ex) {
			System.out.println(ex);
			System.out.println("es programa ar mushaobs mgoni brakia...");
		}
	}

	/* Method: findEntry(name) */
	/**
	 * Returns the NameSurferEntry associated with this name, if one exists. If the
	 * name does not appear in the database, this method returns null.
	 */
	public NameSurferEntry findEntry(String name) {
		if (name.equals("")) {
			return null;
		}
		String validName = "";
		validName += Character.toUpperCase(name.charAt(0));
		validName += name.substring(1).toLowerCase();
		if (dataBase.containsKey(validName)) {
			return dataBase.get(validName);
		}
		return null;
	}
}
