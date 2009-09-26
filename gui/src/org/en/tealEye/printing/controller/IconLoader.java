package org.en.tealEye.printing.controller;

import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Stephan Pudras
 * Date: 24.09.2009
 * Time: 14:53:35
 * To change this template use File | Settings | File Templates.
 */
public abstract class IconLoader {

    public IconLoader(String resource){

    }

    public static ImageIcon getIcon(String resource){
        return new ImageIcon(ClassLoader.getSystemClassLoader().getClass().getResource("/org/en/tealEye/resources/"+resource));
    }
}
