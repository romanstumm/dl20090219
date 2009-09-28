package org.en.tealEye.printing.controller.annotationClasses;

/**
 * Created by IntelliJ IDEA.
 * User: Stephan Pudras
 * Date: 28.09.2009
 * Time: 12:40:03
 * To change this template use File | Settings | File Templates.
 */
import java.lang.annotation.*;
import static java.lang.annotation.ElementType.*;
@Target( { TYPE, METHOD, CONSTRUCTOR, PACKAGE } )
@Retention( RetentionPolicy.RUNTIME )
public @interface CustomContainerListener {
    String listenerType();
    String listenerSection();
    boolean customClass();
    Class customListenerClass();
}
