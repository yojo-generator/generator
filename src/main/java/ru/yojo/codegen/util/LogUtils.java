package ru.yojo.codegen.util;

public class LogUtils {
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_RED = "\033[0;31m";

    public static final String LOG_DELIMETER = "*********************************************************************";
    public static final String LOG_FINISH = ANSI_GREEN + "************************GENERATE IS FINISHED!**************************" + ANSI_RESET;

    public static void printLogo() {
        System.out.println(ANSI_GREEN + "*********************YAML TO POJO GENERATOR**************************");
        System.out.println(LOG_DELIMETER);
        System.out.println(LOG_DELIMETER);
        System.out.println("**   ********   ***            **        **********            ******");
        System.out.println("***   *****   ****   ********   ***   ************   ********   *****");
        System.out.println("****   ***   *****   ********   ***   ************   ********   *****");
        System.out.println("*****   *   ******   ********   ***   ************   ********   *****");
        System.out.println("*******   ********   ********   ***   ************   ********   *****");
        System.out.println("*******   ********   ********   ***   ******   ***   ********   *****");
        System.out.println("*******   ********   ********   ****   ****   ****   ********   *****");
        System.out.println("*******   *********            *******      *******            ******");
        System.out.println(LOG_DELIMETER);
        System.out.println(LOG_DELIMETER);
        System.out.println(LOG_DELIMETER + ANSI_RESET);
    }

}
