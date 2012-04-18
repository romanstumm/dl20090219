package de.liga.dart.database;

import java.net.URL;
import java.util.StringTokenizer;

/**
 * Description:   <br/>
 * User: roman
 * Date: 07.02.2008, 23:59:56
 */
final class DBVersion implements Comparable {
    private final String version;
    private URL url;

    public DBVersion(String version) {
        this.version = version;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DBVersion dbVersion = (DBVersion) o;
        return version.equals(dbVersion.version);
    }

    public String toString() {
        return version;
    }

    public String getVersion() {
        return version;
    }

    public int hashCode() {
        return version.hashCode();
    }

    public int compareTo(Object o) {
        DBVersion other = (DBVersion) o;
        StringTokenizer tokens = new StringTokenizer(version, ".");
        StringTokenizer otherTokens = new StringTokenizer(other.version, ".");
        while(tokens.hasMoreTokens() && otherTokens.hasMoreTokens()) {
            String next = tokens.nextToken();
            String onex = otherTokens.nextToken();
            if(!next.equals(onex)) {
                return Integer.parseInt(next) - Integer.parseInt(onex);
            }
        }
        if(tokens.hasMoreTokens()) {
            return 1;
        }
        if(otherTokens.hasMoreTokens()) {
            return -1;
        }
        return 0;  
    }

    public boolean isLowerThan(DBVersion other)
    {
        return this.compareTo(other) < 0;
    }

    public boolean isHigherThan(DBVersion other)
    {
        return this.compareTo(other) > 0;
    }
}
