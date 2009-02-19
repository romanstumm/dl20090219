package de.liga.util;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.lang.reflect.Type;
import java.util.*;

/**
 * Class declaration
 * <p/>
 * <p/>
 * $Author: stumm $
 */
public class ValueTest extends TestCase {
    public ValueTest(String s) {
        super(s);
    }

    public static Test suite() {
        return new TestSuite(ValueTest.class);
    }

    public void testSet() {
        TestObject obj = new TestObject();
        Value.getDefault().setPath(obj, "Next.Next.Next.Name1", "myName1", true);
        assertEquals("myName1",
                Value.getDefault().getPath(obj, "Next.Next.Next.Name1", ""));
        assertEquals(true, Value.getDefault().setPath(obj, "Next.Next.Next.Name1",
                "myName1", false));
        assertEquals(true, Value.getDefault().setPath(obj, "Next.Next.Next.Name1",
                "myName2", false));
    }

    public void testSetPrimitiveValue() {
        TestObject obj = new TestObject();
        obj.setNumber(1);
        Value.getDefault().setPath(obj, "Number", null, true);
        assertEquals("Primitive default Value not set", 0, obj.getNumber());

        obj.setBoolProperty(true);
        Value.getDefault().setPath(obj, "BoolProperty", null, true);
        assertEquals("Primitive boolean default Value not set", false,
                obj.isBoolProperty());

        obj.setChar('F');
        Value.getDefault().setPath(obj, "Char", null, true);
        assertEquals("Primitive boolean default Value not set", 0, obj.getChar());
    }

    public void testGetGenericType() {
        Type type = Value.getGenericType(ValueTestObj.class, "testObjectsList");
        assertEquals("java.util.List<de.liga.util.TestObject>", type.toString());
        assertEquals(TestObject[].class, Value.getGenericType(ValueTestObj.class, "testObjectsArray"));
    }

    public void testGetPropertyType() {
        assertEquals(List.class, Value.getPropertyType(ValueTestObj.class, "testObjectsList"));
        assertEquals(TestObject[].class, Value.getPropertyType(ValueTestObj.class, "testObjectsArray"));
    }

    public void testBeanType() {
        assertEquals(TestObject.class, Value.getBeanType(ValueTestObj.class, "testObjectsList"));
        assertEquals(TestObject.class, Value.getBeanType(ValueTestObj.class, "testObjectsArray"));
        assertEquals(ValueTestObj.class, Value.getBeanType(ValueTestObj.class, "ReflectorTestObj"));
        assertEquals(ValueTestObj.class, Value.getBeanType(ValueTestObj.class, "ReflectorTestObj2"));
    }

    // set attributes of list/array-elements
    public void testSetWithListAccess1() {
        TestObject obj = new TestObject();
        obj.list = new ArrayList();
        obj.list.add(obj);
        obj.list.add(obj);
        assertTrue(Value.getDefault().setPath(obj, "List.#0.List.#1.Name1",
                "testname", true));
        assertEquals("testname", obj.getName1());
    }

    // set list/array-elements itself
    public void testSetWithListAccess2() {
        TestObject obj = new TestObject();
        obj.list = new ArrayList();
        obj.list.add(obj);
        obj.list.add(obj);
        TestObject obj2 = new TestObject();
        assertTrue(Value.getDefault().setPath(obj, "List.#0", obj2, true));
        assertTrue(obj2 == obj.getList().get(0));
    }

    public void testToMany() {
        TestObject fr = new TestObject();
        for (int i = 0; i < 3; i++) {
            TestObject frs = new TestObject();
            frs.setName1("CODE" + i);
            fr.getManyTestObjects().add(frs);
        }

        // access each with Value.get(...)
        for (int i = 0; i < 3; i++) {
            String each = (String) Value.getDefault()
                    .eval(fr, "ManyTestObjects.#" + i + ".Name1");
            assertEquals("CODE" + i, each);
        }
    }

    public void testGetAttributeReflective() {
        java.util.Date d = new java.util.Date();
        long t = d.getTime();
        assertEquals(t, ((Long) Value.getDefault().eval(d, "Time")).longValue());
        assertEquals(t, ((Long) Value.getDefault()
                .getAttributeReflective(d, "Time")).longValue());
        assertEquals(d, Value.getDefault().eval(d, ""));
    }

    public void testMapEntry() throws Exception {
        HashMap map = new HashMap();
        map.put("1", "eins");
        ArrayList list = new ArrayList(map.entrySet());
        assertEquals(1, list.size());
        assertTrue(list.get(0) instanceof Map.Entry);
        Map.Entry entry = (Map.Entry) list.get(0);

        assertNotNull("error invoking Object.getClass()",
                Value.getDefault().eval(entry, "Class"));

        assertEquals("error invoking HashMap.Entry.getKey()", "1",
                Value.getDefault().eval(entry, "Key"));
        assertEquals("error invoking HashMap.Entry.getValue()", "eins",
                Value.getDefault().eval(entry, "Value"));

    }

    public void testBooleanJavaBeanProperty() {
        TestObject obj = new TestObject();
        Value.getDefault().setPath(obj, "BoolProperty", Boolean.TRUE, true);
        assertEquals(Boolean.TRUE, Value.getDefault().eval(obj, "BoolProperty"));
        Value.getDefault().setPath(obj, "BoolProperty", Boolean.FALSE, true);
        assertEquals(Boolean.FALSE, Value.getDefault().eval(obj, "BoolProperty"));
    }

    public void testGet_emptyString() {
        TestObject obj = new TestObject();
        obj.setName1("");
        assertEquals("", Value.getDefault().getPath(obj, "Name1", ""));
        assertEquals("&nbsp;", Value.getDefault().getPath(obj, "Name1", "&nbsp;"));
    }

    public void testListAccess() {
        String[] array = new String[]{"0", "1", "2", "3"};
        List list = new ArrayList(Arrays.asList(array));
        assertEquals("3", Value.getDefault().eval(array, "#3"));
        assertEquals("2", Value.getDefault().eval(list, "#2"));
        assertEquals("3", Value.getDefault().eval(list, "#"));
        assertEquals("3", Value.getDefault().eval(array, "#"));
        list.add("val0");
        Value.getDefault().setPath(list, "#", "val", false);
        assertEquals("val", Value.getDefault().eval(list, "#"));
    }

    public void testMap() throws Exception {
        Map bean = new HashMap();
        bean.put("ControlField", "testValue");
        assertEquals("testValue", bean.get("ControlField"));
        assertEquals("testValue", Value.getDefault().eval(bean, "ControlField"));
        Value.getDefault().setPath(bean, "ControlField", "newValue", false);
        assertEquals("newValue", Value.getDefault().eval(bean, "ControlField"));
        Value.getDefault().setPath(bean, "ControlField", "newValue2", false);
        assertEquals("newValue2", Value.getDefault().eval(bean, "ControlField"));
    }

    public void testDubiousAttribute() {
        ValueTestObj obj = new ValueTestObj();
        obj.setAttr(false);
        obj.setBSP(false);

        assertEquals(Boolean.FALSE, Value.getDefault().eval(obj, "attr"));
        assertEquals(Boolean.FALSE, Value.getDefault().eval(obj, "BSP"));
        Value.getDefault().setAttribute(obj, "attr", Boolean.TRUE);
        Value.getDefault().setAttribute(obj, "BSP", Boolean.TRUE);
        assertEquals(Boolean.TRUE, Value.getDefault().eval(obj, "attr"));
        assertEquals(Boolean.TRUE, Value.getDefault().eval(obj, "BSP"));

        Value.getDefault().setAttribute(obj, "attr", 0);
        Value.getDefault().setAttribute(obj, "BSP", 0);
    }

}
