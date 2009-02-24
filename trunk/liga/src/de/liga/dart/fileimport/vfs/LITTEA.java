package de.liga.dart.fileimport.vfs;

import java.sql.Date;

/**
 * Description:   Team<br/>
 * User: roman
 * Date: 17.05.2008, 15:57:33
 */
class LITTEA {
    Date SAI_NR; 
    long LIG_NR;   // Ligaklasse
    long TEA_NR;   // PK
    String TEA_NAME;
    long LOK_NR;   // Spielort.externe ID
    //String TEA_KAPIT;
    String TEA_SPIELT; // Freitag ...
    long TEA_UHRZEI; // N(5) 72000 (20:00) 68400 (:60:60 = 19:00)
    String TEA_STATUS; // D = Spielfrei
}
