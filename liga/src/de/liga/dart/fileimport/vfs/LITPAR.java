package de.liga.dart.fileimport.vfs;

import java.sql.Date;

/**
 * Description:   <br/>
 * User: roman
 * Date: 25.04.2009, 16:02:36
 */
class LITPAR {
    Date SAI_NR; //,D   TODO RSt - ??
    long LIG_NR; //N,3,0
    int PAR_RUNDE; // N,2,0   Runde Nr
    int PAR_SPIEL; // N,2,0   Spiel Nr "1-56"
    String PAR_CODE; // Hin- oder Rückspiel, C,1 Spiel Nr 1-28= "V", Spiel Nr 29-56 = "R"
    /* V =
        1->2
        6->4
        7->3
        8->5
        3->1
        5->6
        4->7
        2->8
        2->3
        1->4
        7->5
        8->6
        4->2
        5->1
        6->7
        3->8
        3->4
        2->5
        1->6
        8->7
        5->3
        6->2
        7->1
        4->8
        4->5
        3->6
        2->7
        1->8
     R = wie V, bloß umgekehrt, d.h 2->1, 6->4, ...
     */
    int PAR_HEIM; // N,2,0 Heim-Position 1-8
    int PAR_GAST; // N,2,0 Gast-Position 1-8
    Date PAR_DATUM; //D
    long PAR_UHRZEI; // N(5) 72000 (20:00) 68400 (:60:60 = 19:00)
    String PAR_STATUS; // C, 1  "A", "M" ?

}
