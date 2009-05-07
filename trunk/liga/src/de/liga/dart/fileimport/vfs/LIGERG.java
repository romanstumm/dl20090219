package de.liga.dart.fileimport.vfs;

import java.sql.Date;

/**
 * Description:   <br/>
 * User: roman
 * Date: 25.04.2009, 16:08:30
 */
public class LIGERG {
    Date SAI_NR; // D
    long LIG_NR; // N,3
    int PAR_RUNDE; // N,2
    int PAR_SPIEL; // N,2 - was ist das? TODO RSt - Punkte, Spiel, Satz, Anzahl? (<56?)
    int ERG_HPUNKT; // N,4  Heimspieler Punkte
    int ERG_HSPIEL; // N,4  Heimspieler Spiel
    int ERG_HSATZ; // N,4   Heimspieler Satz
    int ERG_HANZAH; // N,2  Heimspieler Anzahl, immer "4"
    int ERG_GPUNKT; // N,4  Gast Punkte
    int ERG_GSPIEL; // N,4  Gast Spiel
    int ERG_GSATZ; // N,4  Gast Satz
    int ERG_GANZAH; // N,2 Gast Anzahl, immer "4"
}
/*
LITTEA
------
SAI_NR
LIG_NR
TEA_NR

LITSAD
------
SAI_NR  --> LITTEA.SAI_NR
LIG_NR  --> LITTEA.LIG_NR
TEA_NR  --> LITTEA.TEA_NR
SAI_POSNR 1-8

LITPAR
------
SAI_NR     -->LIGERG.SAI_NR, LITSAD.SAI_NR
LIG_NR     -->LITERG.LIG_NR, LITSAD.LIG_NR
PAR_RUNDE  -->LITERG.PAR_RUNDE
PAR_SPIEL  -->LITERG.PAR_SPIEL
PAR_HEIM --> LITSAD.SAI_POSNR
PAR_GAST --> LITSAD.SAI_POSNR 
PAR_SPIEL

LITERG
------
SAI_NR
LIG_NR
PAR_RUNDE
PAR_SPIEL
*/