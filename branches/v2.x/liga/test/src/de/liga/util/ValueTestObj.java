package de.liga.util;

import java.util.List;


/**
 * <b>Description:</b>   <br>
 * <b>Copyright:</b>     Copyright (c) 2005<br>
 * <b>Company:</b>       daGama Business Travel GmbH<br>
 * <b>Creation Date:</b> 22.03.2005
 *
 * @author Roman Stumm
 * @version $Id: ValueTestObj.java 185 2005-10-06 08:07:57Z kguenster $
 */
public class ValueTestObj<T> {
    private int attr;
    private int BSP;

    public List<TestObject> getTestObjectsList() {
        return null; // used for getPropertyType() getBeanType() test only
    }


    public TestObject[] getTestObjectsArray() {
        return null;  // used for getPropertyType() getBeanType() test only
    }

    public ValueTestObj<List<String>[]>[] getReflectorTestObj2()
    {
        return null;  // used for getPropertyType() getBeanType() test only
    }

    public ValueTestObj<T> getReflectorTestObj()
    {
        return this;
    }

    public boolean isAttr() {
        return attr == 1;
    }

    public void setAttr(boolean v) {
        attr = v ? 1 : 0;
    }

    public int getAttr() {
        return attr;
    }

    public void setAttr(int aAttr) {
        attr = aAttr;
    }

    public boolean isBSP() {
        return BSP == 1;
    }

    public void setBSP(boolean v) {
        BSP = v ? 1 : 0;
    }

    public int getBSP() {
        return BSP;
    }

    public void setBSP(int aBSP) {
        BSP = aBSP;
    }
}

