package edu.eci.dosw.DOSW_Library.core.util;

public class ValidationUtil {

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new RuntimeException(message);
        }
    }
}
