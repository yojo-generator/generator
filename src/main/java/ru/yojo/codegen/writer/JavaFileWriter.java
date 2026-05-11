package ru.yojo.codegen.writer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Encapsulates file system operations for writing generated Java source files.
 * <p>
 * Handles:
 * <ul>
 *   <li>Directory creation</li>
 *   <li>Writing files with UTF-8 encoding</li>
 *   <li>Dry-run mode for testing (returns content without writing)</li>
 * </ul>
 *
 * @author Vladimir Morozkin (TG @vmorozkin)
 */
public class JavaFileWriter {

    private final boolean dryRun;

    /**
     * Creates a file writer that actually writes to disk.
     */
    public JavaFileWriter() {
        this(false);
    }

    /**
     * Creates a file writer with optional dry-run mode.
     *
     * @param dryRun if {@code true}, write operations return the content without writing to disk
     */
    public JavaFileWriter(boolean dryRun) {
        this.dryRun = dryRun;
    }

    /**
     * Writes the given Java source content to a file in the specified directory.
     * <p>
     * The file name is derived from the class name (appended with ".java").
     * Directories are created if they don't exist.
     *
     * @param outputDirectory the base directory for output
     * @param packageName   the package name (used to create subdirectories)
     * @param className     the class name (becomes file name)
     * @param content       the Java source code to write
     * @return the path to the written file (or would-be path in dry-run mode)
     * @throws IOException if directory creation or file writing fails
     */
    public String writeJavaFile(String outputDirectory, String packageName,
                               String className, String content) throws IOException {
        String relativePath = packageName.replace(".", File.separator);
        String fullPath = outputDirectory + File.separator + relativePath;
        Path dirPath = Paths.get(fullPath);

        if (!dryRun) {
            Files.createDirectories(dirPath);
        }

        String fileName = className + ".java";
        Path filePath = dirPath.resolve(fileName);

        if (dryRun) {
            return content; // In dry-run, just return the content
        }

        try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
            fos.write(content.getBytes(StandardCharsets.UTF_8));
        }

        return filePath.toString();
    }

    /**
     * Creates directories for the given path.
     *
     * @param path the directory path to create
     * @throws IOException if creation fails
     */
    public void createDirectories(String path) throws IOException {
        if (!dryRun) {
            Files.createDirectories(Paths.get(path));
        }
    }

    /**
     * Checks if dry-run mode is enabled.
     *
     * @return {@code true} if in dry-run mode
     */
    public boolean isDryRun() {
        return dryRun;
    }
}
