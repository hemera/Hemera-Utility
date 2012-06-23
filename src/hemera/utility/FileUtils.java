package hemera.utility;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <code>FileUtils</code> defines the singleton utility
 * that provides various file operation methods.
 *
 * @author Yi Wang (Neakor)
 * @version 1.0.0
 */
public enum FileUtils {
	/**
	 * The singleton instance.
	 */
	instance;

	/**
	 * Retrieve the files from the specified directory
	 * including all sub-directories that have the file
	 * extension matching the given one.
	 * @param rootDir The <code>String</code> directory
	 * to search from.
	 * @param extension The <code>String</code> file
	 * extension to search for.
	 * @return The <code>List</code> of all matching
	 * <code>File</code>. <code>null</code> if there
	 * are none.
	 */
	public List<File> getFiles(final String rootDir, final String extension) {
		// Check root directory is a directory.
		final File root = new File(rootDir);
		if (!root.isDirectory()) return null;
		// Recursively find all files.
		final List<File> files = new ArrayList<File>();
		final File[] list = root.listFiles();
		for (int i = 0; i < list.length; i++) {
			final File file = list[i];
			// Recursive.
			if (file.isDirectory()) {
				final List<File> results = this.getFiles(file.getAbsolutePath(), extension);
				files.addAll(results);
			} else {
				// Check extension.
				if (file.getAbsolutePath().endsWith(extension)) files.add(file);
			}
		}
		return files;
	}
}
