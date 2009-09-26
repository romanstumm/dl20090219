package org.en.tealEye.printing.controller;


import javax.swing.*;
import javax.imageio.ImageIO;
import java.util.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: Stephan Pudras
 * Date: 13.09.2009
 * Time: 04:32:18
 * To change this template use File | Settings | File Templates.
 */
public class ExtResourceBundle {

    private ResourceBundle resourceBundle;


    public ExtResourceBundle(String resource){
        resourceBundle = ResourceBundle.getBundle(resource);
    }



    public Icon getIcon(String resource) {
        String path = resourceBundle.getString(resource);
        return new ImageIcon(getClass().getResource("/org/en/tealEye/resources/"+path));
    }


    public Color getColor(String resource) {
        String col = resourceBundle.getString(resource);
        String[] c = col.split(", ");
        ArrayList colo = new ArrayList();
        System.out.println(col);
        for(String s:c){
            if(!s.contains(",")){
                colo.add(s);
            }
        }
        return new Color(Integer.parseInt(colo.get(0).toString()), Integer.parseInt(colo.get(1).toString()), Integer.parseInt(colo.get(2).toString()));
    }

    public String getString(String s) {
        return resourceBundle.getString(s);
    }
}
