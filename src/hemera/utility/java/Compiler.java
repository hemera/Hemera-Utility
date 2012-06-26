package hemera.utility.java;

import hemera.utility.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

/**
 * <code>Compiler</code> defines the implementation
 * that provides the functionality to compile a set
 * of Java files in a directory with a specified
 * library class path into a single Jar file.
 *
 * @author Yi Wang (Neakor)
 * @version 1.0.0
 */
public class Compiler {

	/**
	 * Compile the source files in the specified source
	 * directory including all of its sub-directories
	 * with all the Jar files in the library directory
	 * including all of its sub-directories as compiling
	 * class-path into given build directory.
	 * @param srcDir The <code>String</code> root source
	 * files directory.
	 * @param libDir The <code>String</code> root library
	 * Jar files directory.
	 * @param buildDir The <code>String</code> directory
	 * to put all compiled class files.
	 * @return The <code>List</code> of all the compiled
	 * class files.
	 * @throws Exception If any compilation error ocurred.
	 */
	public List<File> compile(final String srcDir, final String libDir, final String buildDir) throws Exception {
		// Retrieve all Java files.
		final List<File> sourcefiles = FileUtils.instance.getFiles(srcDir, ".java");
		if (sourcefiles == null || sourcefiles.isEmpty()) return null;
		final int size = sourcefiles.size();
		final File[] sourcearray = new File[size];
		for (int i = 0; i < size; i++) sourcearray[i] = sourcefiles.get(i);
		// Create the compiler.
		final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		final DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
		final StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
		final Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjects(sourcearray);
		// Create temporary build directory.
		FileUtils.instance.delete(buildDir);
		final File buildDirFile = new File(buildDir);
		buildDirFile.mkdirs();
		// Create task with compiler options.
		final List<String> options = this.getCompileOptions(libDir, buildDir);
		final CompilationTask task = compiler.getTask(null, fileManager, diagnostics, options, null, compilationUnits);
		final boolean succeeded = task.call();
		fileManager.close();
		if (!succeeded) {
			final StringBuilder builder = new StringBuilder();
			builder.append("Compiling source at directory: ");
			builder.append(srcDir).append(" failed.\n");
			final List<Diagnostic<? extends JavaFileObject>> errors = diagnostics.getDiagnostics();
			for (final Diagnostic<? extends JavaFileObject> error : errors) {
				builder.append(error.toString()).append("\n");
			}
			throw new RuntimeException(builder.toString());
		}
		// Retrieve all the compiled class files from build directory.
		return FileUtils.instance.getFiles(buildDir, ".class");
	}
	
	/**
	 * Create the compile class-path and destination
	 * directory options.
	 * @param libDir The <code>String</code> library
	 * path to search for all class-path Jar files.
	 * @param buildDir The <code>String</code> build
	 * destination directory.
	 * @return The <code>List</code> of compile option
	 * <code>String</code> values.
	 */
	private List<String> getCompileOptions(final String libDir, final String buildDir) {
		final List<String> options = new ArrayList<String>();
		// Search for all Jar files within the library directory.
		final List<File> libFiles = FileUtils.instance.getFiles(libDir, ".jar");
		// Append all Jar files as class-path option.
		options.add("-classpath");
		final StringBuilder builder = new StringBuilder();
		final int size = libFiles.size();
		final int last = size - 1;
		for (int i = 0; i < size; i++) {
			final File libFile = libFiles.get(i);
			builder.append(libFile.getAbsolutePath());
			if (i != last) builder.append(":");
		}
		options.add(builder.toString());
		// Destination option.
		options.add("-d");
		options.add(buildDir);
		return options;
	}
}
