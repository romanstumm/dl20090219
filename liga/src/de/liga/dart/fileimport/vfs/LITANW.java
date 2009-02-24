package de.liga.dart.fileimport.vfs;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * Description: Anwender und aktuelle Saison.
 * Tabelle hat nur einen Datensatz. einen 2. anzulegen verbietet die Altanwendung<br/>
 * User: roman
 * Date: 24.02.2009, 19:53:13
 */
class LITANW {
    int ANW_NR; // ,N,3,0
    String ANW_NAME; //  ,C,35
    String ANW_LIZENZ; // ,C,35
    String ANW_STRASS; // ,C,35
    String ANW_PLZ; // ,C,7
    String ANW_ORT; // ,C,35
    String ANW_TEL; // ,C,20
    String ANW_FAX; // ,C,20
    Date ANW_SAISON; // ,D   aktuelle Saison
    int ANW_VERSIO; // ,N,2,0
    String ANW_STUFE; // ,C,1
    BigDecimal ANW_KENNZ; // ,N,11,3
    
}
