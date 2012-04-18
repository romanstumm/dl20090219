package org.en.tealEye.printing.controller.annotationClasses;

/**
 * Created by IntelliJ IDEA.
 * User: Stephan Pudras
 * Date: 24.09.2009
 * Time: 07:34:15
 * To change this template use File | Settings | File Templates.
 */
import java.lang.annotation.*;
import static java.lang.annotation.ElementType.*;
@Target( { TYPE, METHOD, CONSTRUCTOR, PACKAGE } )
@Retention( RetentionPolicy.RUNTIME )
public @interface GlobalMethod {
}
