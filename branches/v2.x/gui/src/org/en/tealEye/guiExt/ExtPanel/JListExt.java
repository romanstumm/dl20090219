package org.en.tealEye.guiExt.ExtPanel;


import javax.swing.*;
import java.util.Vector;

/**
 * Description: <br/>
 * User: Stephan
 * Date: 20.11.2007
 * Time: 14:48:50
 */
public class JListExt extends JList {

    private final Vector<Object> v = new Vector<Object>();

    public void setObject(Object obj) {
        v.addElement(obj);
    }

    public Object getObject(int i) {
        return v.get(i);
    }

    public void deleteObject(int i) {
        v.remove(i);
    }

    public void deleteObject(Object obj) {
        v.remove(obj);
    }
}
