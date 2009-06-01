package de.liga.dart.fileimport.vfs;

import java.sql.Date;

/**
 * Description: saison tabelle <br/>
 * User: roman
 * Date: 24.02.2009, 19:48:52
 */
class LITSAI {
    Date SAI_NR; //,D Saison datum 01/2009 = 1.1.2009, 02/2009 = 1.2.2009, 01/2010 = 1.1.2010 usw.
    int LIG_NR; //,N,3,0   foreign key to LITLIG
    String SAI_EINZEL; // ,C,10
    String SAI_STATUS; // ,C,1    
}
