package ru.yojo.codegen.util;

/**
 * Utility class for console logging, including ANSI-colored banners and delimiters.
 * Used during code generation to provide visual feedback and structure in the console output.
 *
 * @author Vladimir Morozkin (TG @vmorozkin)
 */
@SuppressWarnings("all")
public class LogUtils {

    /**
     * ANSI escape code for green text.
     */
    public static final String ANSI_GREEN = "\u001B[32m";

    /**
     * ANSI escape code for purple text.
     */
    public static final String ANSI_PURPLE = "\u001B[35m";

    /**
     * ANSI escape code to reset text formatting.
     */
    public static final String ANSI_RESET = "\u001B[0m";

    /**
     * ANSI escape code for cyan text.
     */
    public static final String ANSI_CYAN = "\u001B[36m";

    /**
     * ANSI escape code for red text.
     */
    public static final String ANSI_RED = "\033[0;31m";

    /**
     * Standard delimiter line for section separation in logs.
     */
    public static final String LOG_DELIMETER = "*********************************************************************";

    /**
     * Final success message with green highlighting and reset.
     */
    public static final String LOG_FINISH = ANSI_GREEN + "************************GENERATE IS FINISHED!**************************" + ANSI_RESET;

    /**
     * Prints the Yojo generator logo and header to the console using ANSI colors.
     * Includes a stylized ASCII-art banner.
     */
    public static void printLogo() {
        System.out.println(ANSI_GREEN + "*********************YAML TO POJO GENERATOR**************************");
        System.out.println(ANSI_GREEN + LOG_DELIMETER);
        System.out.println(ANSI_GREEN + LOG_DELIMETER);
        System.out.println(ANSI_GREEN + "**   ********   ***            **        **********            ******");
        System.out.println(ANSI_GREEN + "***   *****   ****   ********   ***   ************   ********   *****");
        System.out.println(ANSI_GREEN + "****   ***   *****   ********   ***   ************   ********   *****");
        System.out.println(ANSI_GREEN + "*****   *   ******   ********   ***   ************   ********   *****");
        System.out.println(ANSI_GREEN + "*******   ********   ********   ***   ************   ********   *****");
        System.out.println(ANSI_GREEN + "*******   ********   ********   ***   ******   ***   ********   *****");
        System.out.println(ANSI_GREEN + "*******   ********   ********   ****   ****   ****   ********   *****");
        System.out.println(ANSI_GREEN + "*******   *********            *******      *******            ******");
        System.out.println(ANSI_GREEN + LOG_DELIMETER);
        System.out.println(ANSI_GREEN + LOG_DELIMETER);
        System.out.println(ANSI_GREEN + LOG_DELIMETER + ANSI_RESET);
    }
}