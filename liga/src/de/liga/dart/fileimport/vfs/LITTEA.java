package de.liga.dart.fileimport.vfs;

import java.sql.Date;

/**
 * Description:   Team<br/>
 * User: roman
 * Date: 17.05.2008, 15:57:33
 */
class LITTEA {
    Date SAI_NR; 
    int LIG_NR;   // Ligaklasse
    int TEA_NR;   // TEA_NR,N,5,0 PK
    String TEA_NAME;
    int LOK_NR;   // Spielort.externe ID
    //String TEA_KAPIT;
    String TEA_SPIELT; // Freitag ...
    int TEA_UHRZEI; // N(5) 72000 (20:00) 68400 (:60:60 = 19:00)
    String TEA_STATUS; // D = Spielfrei
}
