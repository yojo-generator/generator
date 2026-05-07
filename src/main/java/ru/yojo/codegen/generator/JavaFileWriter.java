package ru.yojo.codegen.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

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
     * Writes Java source code to a file.
     *
     * @param dirPath  target directory path
     * @param fileName file name (without extension)
     * @param content  Java source code
     * @throws RuntimeException if writing fails
     */
    public void writeFile(String dirPath, String fileName, String content) {
        if (dryRun) {
            System.out.println("DRY-RUN: Would write " + fileName + ".java → " + dirPath);
            return;
        }

        File dir = new File(dirPath);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new RuntimeException("Failed to create directory: " + dirPath);
        }

        File file = new File(dir, fileName + ".java");
        try (OutputStreamWriter writer = new OutputStreamWriter(
                new FileOutputStream(file), StandardCharsets.UTF_8)) {
            writer.write(content);
            writer.flush();
            System.out.println(" Written: " + fileName + ".java → " + dirPath);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to write: " + file, ex);
        }
    }

    /**
     * Writes Java source code to a file using Path API.
     *
     * @param targetDir target directory
     * @param fileName  file name (without extension)
     * @param content   Java source code
     * @throws RuntimeException if writing fails
     */
    public void writeFile(Path targetDir, String fileName, String content) {
        writeFile(targetDir.toAbsolutePath().toString(), fileName, content);
    }
}
