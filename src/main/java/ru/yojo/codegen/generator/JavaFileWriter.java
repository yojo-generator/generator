package ru.yojo.codegen.generator;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import ru.yojo.codegen.util.Logger;

/**
 * Isolates file system operations for writing generated Java source code.
 * <p>
 * Handles:
 * <ul>
 *   <li>Creating target directories</li>
 *   <li>Writing files with correct encoding (UTF-8)</li>
 *   <li>Optional dry-run mode (return generated code without writing)</li>
 * </ul>
 *
 * <p>This makes it easier to test code generation without the file system.
 *
 * @author Vladimir Morozkin (TG @vmorozkin)
 */
public class JavaFileWriter {

    private static final Logger LOG = new Logger(JavaFileWriter.class);
    private final boolean dryRun;

    /**
     * Creates a new JavaFileWriter.
     *
     * @param dryRun if {@code true}, files won't be written (useful for testing)
     */
    public JavaFileWriter(boolean dryRun) {
        this.dryRun = dryRun;
    }

    /**
     * Creates a new JavaFileWriter with dry-run disabled.
     */
    public JavaFileWriter() {
        this(false);
    }

    /**
     * Validates that a file name does not contain path traversal sequences.
     *
     * @param fileName the file name to validate (without extension)
     * @throws IllegalArgumentException if the file name contains path traversal
     */
    static void validateFileName(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("File name must not be null or empty");
        }
        if (fileName.contains("..")) {
            throw new IllegalArgumentException(
                    "File name must not contain path traversal sequences: " + fileName);
        }
        if (fileName.contains("/") || fileName.contains("\\")) {
            throw new IllegalArgumentException(
                    "File name must not contain path separators: " + fileName);
        }
        // Reject names that would resolve to only a separator or dot
        Path resolved = Path.of(fileName);
        if (resolved.getNameCount() == 0) {
            throw new IllegalArgumentException(
                    "File name does not resolve to a valid name: " + fileName);
        }
    }

    /**
     * Writes Java source code to a file with path traversal protection.
     *
     * @param dirPath  target directory path
     * @param fileName file name (without extension)
     * @param content  Java source code
     * @throws RuntimeException if writing fails
     */
    public void writeFile(String dirPath, String fileName, String content) {
        if (dryRun) {
            LOG.info("DRY-RUN: Would write " + fileName + ".java → " + dirPath);
            return;
        }
        writeFile(Path.of(dirPath), fileName, content);
    }

    /**
     * Writes Java source code to a file using Path API with path traversal protection.
     *
     * @param targetDir target directory
     * @param fileName  file name (without extension)
     * @param content   Java source code
     * @throws RuntimeException if writing fails
     */
    public void writeFile(Path targetDir, String fileName, String content) {
        if (dryRun) {
            LOG.info("DRY-RUN: Would write " + fileName + ".java → " + targetDir);
            return;
        }

        validateFileName(fileName);

        Path absoluteDir = targetDir.toAbsolutePath().normalize();
        Path targetFile = absoluteDir.resolve(fileName + ".java").normalize();

        // Path traversal prevention: ensure the resolved file is still within the intended directory
        if (!targetFile.startsWith(absoluteDir)) {
            throw new SecurityException(
                    "Path traversal detected: " + fileName + " escapes directory " + targetDir);
        }

        try {
            Files.createDirectories(absoluteDir);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to create directory: " + absoluteDir, ex);
        }

        try (OutputStreamWriter writer = new OutputStreamWriter(
                Files.newOutputStream(targetFile), StandardCharsets.UTF_8)) {
            writer.write(content);
            writer.flush();
            LOG.info(" Written: " + fileName + ".java → " + absoluteDir);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to write: " + targetFile, ex);
        }
    }
}
