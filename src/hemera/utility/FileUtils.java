package hemera.utility;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

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
	 * Create a new Jar file using the given files and
	 * save the Jar file at the target location.
	 * @param files The <code>List</code> of all the
	 * <code>File</code> to be jarred.
	 * @param target The <code>String</code> path to
	 * store the new Jar file.
	 * @return The new Jar <code>File</code>.
	 * @throws IOException If any file processing failed.
	 */
	public File jarFiles(final List<File> files, final String target) throws IOException {
		if (!target.endsWith(".jar")) throw new IllegalArgumentException("Target must be a Jar file with extension .jar");
		// Delete and create new file.
		final File jarfile = new File(target);
		jarfile.delete();
		jarfile.createNewFile();
		// Create a new output stream for the Jar file.
		final JarOutputStream output = new JarOutputStream(new FileOutputStream(jarfile));
		// Add all files as Jar entries.
		final int size = files.size();
		for (int i = 0; i < size; i++) {
			final File file = files.get(i);
			this.writeJarEntry(file, output);
		}
		// Close output.
		output.close();
		return jarfile;
	}
	
	/**
	 * Write the given source file as a Jar entry into
	 * the given Jar output stream.
	 * @param source The source <code>File</code> to be
	 * written as a Jar entry into the given target.
	 * @param target The <code>JarOutputStream</code>
	 * to write to.
	 * @throws IOException If any file processing failed.
	 */
	private void writeJarEntry(final File source, final JarOutputStream target) throws IOException {
		// Source file must exist and cannot be a directory.
		if (source.isDirectory() || !source.exists()) {
			throw new IllegalArgumentException("Jar file entry does not exist or is a directory: " + source.getAbsolutePath());
		}
		// Create the Jar entry.
		final JarEntry entry = new JarEntry(source.getName());
		entry.setTime(source.lastModified());
		target.putNextEntry(entry);
		// Read from the source file and write into the target Jar stream.
		BufferedInputStream input = null;
		try {
			input = new BufferedInputStream(new FileInputStream(source));
			final byte[] buffer = new byte[1024];
			while (true) {
				final int count = input.read(buffer);
				if (count == -1) break;
				else target.write(buffer, 0, count);
			}
			target.closeEntry();
		} finally {
			if (input != null) input.close();
		}
	}

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
