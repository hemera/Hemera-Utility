package hemera.utility;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;

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
	 * Delete the target. If the target is a directory,
	 * all the children directories and files are also
	 * deleted.
	 * @param target The <code>String</code> target.
	 * @return <code>true</code> if target is deleted.
	 * <code>false</code> otherwise.
	 */
	public boolean delete(final String target) {
		final File file = new File(target);
		if (file.isDirectory()) {
			final String[] children = file.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = this.delete(new File(target, children[i]).getAbsolutePath());
				if (!success) return false;
			}
		}
		return file.delete();
	}
	
	/**
	 * Read the contents of the file as a single string
	 * value.
	 * @param file The <code>File</code> to read.
	 * @return The <code>String</code> of the file
	 * contents.
	 * @throws IOException If any file processing failed.
	 */
	public String readAsString(final File file) throws IOException {
		BufferedReader input = null;
		final StringBuilder builder = new StringBuilder();
		try {
			input = new BufferedReader(new FileReader(file));
			final char[] buffer = new char[1024];
			while (true) {
				final int count = input.read(buffer);
				if (count <= 0) break;
				else {
					final String str = String.valueOf(buffer, 0, count);
					builder.append(str);
				}
			}
		} finally {
			if (input != null) input.close();
		}
		return builder.toString();
	}
	
	/**
	 * Write all the entries of the Jar file to the
	 * specified directory.
	 * @param jarFile The <code>File</code> to retrieve
	 * entries from.
	 * @param path The <code>String</code> directory
	 * to write the entries to.
	 * @throws IOException If any file processing failed.
	 */
	public void writeAll(final File jarFile, final String path) throws IOException {
		final JarFile jar = new JarFile(jarFile);
		JarInputStream input = null;
		try {
			input = new JarInputStream(new FileInputStream(jarFile));
			ZipEntry entry = input.getNextEntry();
			while (entry != null) {
				FileUtils.instance.writeToFile(jar, entry.getName(), path);
				entry = input.getNextEntry();
			}
		} finally {
			if (input != null) input.close();
		}
	}

	/**
	 * Write the given content as a file at the given
	 * target location. If the given target is an
	 * existing file, the contents of that file will
	 * be over-written.
	 * @param content The <code>String</code> content.
	 * @param target The <code>String</code> target
	 * file to write to.
	 * @return The <code>File</code> written to.
	 * @throws IOException If any file processing failed.
	 */
	public File writeAsString(final String content, final String target) throws IOException {
		// Delete existing.
		final File file = new File(target);
		file.delete();
		file.createNewFile();
		// Write contents.
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(content);
		} finally {
			if (writer != null) writer.close();
		}
		return file;
	}

	/**
	 * Write the Jar entry with specified name from
	 * the given Jar file to the target location.
	 * <p>
	 * This method creates the necessary directories
	 * for the target file.
	 * @param jar The <code>JarFile</code> to retrieve
	 * the entry from.
	 * @param entryName The <code>String</code> name of
	 * the entry, which is also the name of the output
	 * file.
	 * @param path The <code>String</code> directory
	 * to write the file to.
	 * @return The created <code>File</code>. If the
	 * operation failed, <code>null</code>.
	 * @throws IOException If any file processing failed.
	 */
	public File writeToFile(final JarFile jar, final String entryName, final String path) throws IOException {
		final String filePath = path.endsWith(File.separator) ? path : path + File.separator; 
		// Create necessary directories.
		final File dir = new File(filePath);
		dir.mkdirs();
		// Retrieve the entry.
		final ZipEntry entry = jar.getEntry(entryName);
		// Delete and create target file.
		final int index = entryName.lastIndexOf(File.separator)+1;
		final String targetName = entryName.substring(index);
		final String targetPath = filePath + targetName;
		final File target = new File(targetPath);
		target.delete();
		target.createNewFile();
		// Write to file.
		FileOutputStream output = null;
		InputStream input = null;
		try {
			output = new FileOutputStream(target);
			input = jar.getInputStream(entry);
			final byte[] buffer = new byte[1024];
			while (true) {
				final int count = input.read(buffer);
				if (count <= 0) break;
				else output.write(buffer, 0, count);
			}
		} finally {
			if (output != null) output.close();
			if (input != null) input.close();
		}
		return target;
	}

	/**
	 * Create a new Jar file using the given files and
	 * save the Jar file at the target location.
	 * <p>
	 * This method includes an empty manifest file in
	 * the final Jar file.
	 * @param files The <code>List</code> of all the
	 * <code>File</code> to be jarred.
	 * @param target The <code>String</code> path to
	 * store the new Jar file.
	 * @return The new Jar <code>File</code>.
	 * @throws IOException If any file processing failed.
	 */
	public File jarFiles(final List<File> files, final String target) throws IOException {
		return this.jarFiles(files, target, new Manifest());
	}

	/**
	 * Create a new Jar file using the given files and
	 * save the Jar file at the target location.
	 * @param files The <code>List</code> of all the
	 * <code>File</code> to be jarred.
	 * @param target The <code>String</code> path to
	 * store the new Jar file.
	 * @param manifest The <code>Manifest</code> to be
	 * included in the Jar file.
	 * @return The new Jar <code>File</code>.
	 * @throws IOException If any file processing failed.
	 */
	public File jarFiles(final List<File> files, final String target, final Manifest manifest) throws IOException {
		// Delete and create new file.
		final File jarfile = new File(target);
		jarfile.delete();
		jarfile.createNewFile();
		// Create a new output stream for the Jar file.
		JarOutputStream output = null;
		try {
			output = new JarOutputStream(new FileOutputStream(jarfile), manifest);
			// Add all files as Jar entries.
			final int size = files.size();
			for (int i = 0; i < size; i++) {
				final File file = files.get(i);
				this.writeJarEntry(file, output);
			}
		} finally {
			if (output != null) output.close();
		}
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
	 * Retrieve the current executing Jar file path
	 * on the local file system.
	 * @return The current Jar <code>File</code>.
	 */
	public File getCurrentJarFile() {
		// Retrieve the resource of this class.
		final String classResource = this.getClass().getName().replace(".", File.separator) + ".class";
		final URL classUrl = this.getClass().getClassLoader().getResource(classResource);
		final String classUrlPath = classUrl.getFile();
		// Only use the actual path portion excluding any descriptor.
		final int start = classUrlPath.indexOf(File.separator);
		final int index = classUrlPath.lastIndexOf(File.separator)+1;
		final String path = classUrlPath.substring(start, index);
		// Find the Jar file portion.
		final int jarIndex = path.indexOf("!");
		final String jarPath = path.substring(0, jarIndex);
		return new File(jarPath);
	}

	/**
	 * Retrieve all files from the specified directory
	 * including all sub-directories.
	 * @param rootDir The <code>String</code> directory
	 * to search from.
	 * @return The <code>List</code> of all the
	 * <code>File</code>. <code>null</code> if there
	 * are none.
	 */
	public List<File> getFiles(final String rootDir) {
		return this.getFiles(rootDir, null);
	}

	/**
	 * Retrieve the files from the specified directory
	 * including all sub-directories that have the file
	 * extension matching the given one.
	 * @param rootDir The <code>String</code> directory
	 * to search from.
	 * @param extension The <code>String</code> file
	 * extension to search for. <code>null</code> if
	 * all file types should be retrieved.
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
				if (extension == null) files.add(file);
				else if (file.getAbsolutePath().endsWith(extension)) files.add(file);
			}
		}
		return files;
	}
}
