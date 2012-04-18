package de.liga.util;

import java.util.List;

public class TestObject {
    public String name1;
    public int number;
    public char myChar;
    public TestObject next;
    public List list;
    public String alwaysSet = "String is set";

    public char getChar() {
        return myChar;
    }

    public void setChar(char aChar) {
        myChar = aChar;
    }

    public boolean isBoolProperty() {
        return myBoolProperty;
    }

    public void setBoolProperty(boolean aBoolProperty) {
        myBoolProperty = aBoolProperty;
    }

    private boolean myBoolProperty;


    private java.util.ArrayList myManyTestObjects = new java.util.ArrayList();

    public java.util.List getManyTestObjects() {
        return myManyTestObjects;
    }

    public TestObject(TestObject other) {
        next = other;
    }

    public TestObject() {
    }

    public TestObject(String n1, int num) {
        name1 = n1;
        number = num;
    }

    public String toString() {
        return getName1() + " >> " + getNumber();
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String value) {
        name1 = value;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int value) {
        number = value;
    }

    public TestObject getNext() {
        return next;
    }

    public List getList() {
        return list;
    }

    public void setNext(TestObject theNext) {
        next = theNext;
    }

    public String getAlwaysSet() {
        return alwaysSet;
    }

    public void setAlwaysSet(String aAlwaysSet) {
        alwaysSet = aAlwaysSet;
    }
}
                                