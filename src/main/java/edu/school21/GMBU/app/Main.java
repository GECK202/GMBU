package edu.school21.GMBU.app;

import edu.school21.GMBU.config.SpringConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static byte toByte(int i) {
        return (byte)i;
    }

    public static void main(String[] args) {

        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                SpringConfig.class)) {
        }
    }
}

