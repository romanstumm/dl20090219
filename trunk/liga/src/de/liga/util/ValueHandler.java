package de.liga.util;

/**
 * Description: Interface declaration - used for command tokens of the Value class<br/>
 * User: roman.stumm <br/>
 * Date: 23.05.2007 <br/>
 * Time: 16:26:21 <br/>
 * Copyright: Agimatec GmbH
 */
public interface ValueHandler {
      /**
   *
   * @param value - the attribute value to be formatted
   * @param params - null or a string with parameters (between brackets)
   * @return the formatted object (e.g. a new value)
   */
  Object getValue(Object value, String params);
}
