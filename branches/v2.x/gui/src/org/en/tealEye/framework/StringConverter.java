package org.en.tealEye.framework;

public interface StringConverter {
    String toString(Object value);

    Object fromString(String value);
}
