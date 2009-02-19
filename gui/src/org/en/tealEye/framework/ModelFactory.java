package org.en.tealEye.framework;

import javax.swing.*;

public abstract class ModelFactory<T> {

    public abstract T create(JPanel panel);

}
