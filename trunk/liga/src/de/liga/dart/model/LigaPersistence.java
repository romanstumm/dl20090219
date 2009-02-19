package de.liga.dart.model;

/**
 * Description: <br/>
 * User: Stephan
 * Date: 18.11.2007
 * Time: 18:30:48
 */
public interface LigaPersistence {
    long getKeyValue();

    /**
     * anstatt this.getClass(), weil Hibernate auch
     * "unsichtbare" Unterklassen und Proxies erzeugt.
     * @return
     */
    Class getModelClass();

    /**
     * @return info as string about the type and instance
     */
    String toInfoString();
}
