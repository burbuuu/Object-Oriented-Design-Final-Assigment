package edu.uoc.uoctron;

/**
 * Special wrapper class that is created because JAR files do not allow us to use
 * a main method that is in a Class that extends from Application.<br/><br/>
 *
 * This class is used as entry point of a JAR file created by us.
 * Otherwise, it is not used.
 *
 * @author Carles Gallel Soler
 * @version 1.0
 */
public class Main {

    /**
     * Main that is invoked when we use JAR file.
     *
     * @param args It is not required.
     */
    public static void main(String[] args) {
        UOCtron.main(args);
    }

}