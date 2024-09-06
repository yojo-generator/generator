package ru.yojo.codegen.util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RefParser {
    public static List<String> getParsedReference(String refString) {
        // Regular expression to split the reference into file path and object path
        Pattern pattern = Pattern.compile("(.+?)(#.+)");
        Matcher matcher = pattern.matcher(refString);

        if (matcher.find()) {
            // Get the file path and remove './' at the beginning if it exists
            // Remove './' from the start of the path
            String filePath = matcher.group(1).replaceFirst("^\\.", "");
            String objectPath = matcher.group(2); // Get the object path

            // Print the results
            System.out.println("Путь к файлу: " + filePath);
            System.out.println("Путь к объекту: " + objectPath);
            return List.of(filePath, objectPath);
        } else {
            throw new RuntimeException("Не удалось распарсить строку.");
        }
    }
}
