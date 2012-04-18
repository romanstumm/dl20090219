package de.liga.dart.fileimport.vfs.rangliste;

import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import junit.framework.TestCase;

import java.io.File;

/**
 * Description:   <br/>
 * User: roman
 * Date: 06.05.2009, 16:55:59
 */
public class GroovyScriptTest extends TestCase {

    public GroovyScriptTest(String string) {
        super(string);
    }

    public void testExecGroovyScript() throws Exception {
        String groovyDir =
                new File(getClass().getClassLoader().getResource("log4j.xml").
                        getPath()).getParent();
        GroovyScriptEngine scriptEngine =
                new GroovyScriptEngine(groovyDir);
        scriptEngine.run("Rangliste.groovy", new Binding());
    }
}
