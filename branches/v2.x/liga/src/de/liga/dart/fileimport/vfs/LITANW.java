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
/**********
 * Sonstige Tabellen:
 * LITAUS: (leer)
 * SAI_NR,D	LIG_NR,N,3,0
 * PAR_RUNDE,N,2,0
 * PAR_SPIEL,N,2,0
 * AUS_SPIH1,C,9
 * AUS_SPIH2,C,9
 * AUS_SPIH3,C,9
 * AUS_SPIH4,C,9
 * AUS_SPIHE,C,9
 * AUS_SPIG1,C,9
 * AUS_SPIG2,C,9
 * AUS_SPIG3,C,9
 * AUS_SPIG4,C,9
 * AUS_SPIGE,C,9
 * AUS_ERSRUN,N,1,0
 * AUS_ERSSPI,N,1,0
 * COLN17,N,1,0
 * COLN18,N,1,0
 * AUS_ANTRIT,N,1,0
 * COLN20,N,1,0
 * AUS_RUN1G1,N,2,0
 * AUS_RUN1G2,N,2,0
 * AUS_RUN1G3,N,2,0
 * AUS_RUN1G4,N,2,0
 * AUS_RUN2G1,N,2,0
 * AUS_RUN2G2,N,2,0
 * AUS_RUN2G3,N,2,0
 * AUS_RUN2G4,N,2,0
 * AUS_RUN3G1,N,2,0
 * AUS_RUN3G2,N,2,0
 * AUS_RUN3G3,N,2,0
 * AUS_RUN3G4,N,2,0
 * AUS_RUN4G1,N,2,0
 * AUS_RUN4G2,N,2,0
 * AUS_RUN4G3,N,2,0
 * AUS_RUN4G4,N,2,0
 * AUS_RUN1H1,N,2,0
 * AUS_RUN1H2,N,2,0
 * AUS_RUN1H3,N,2,0
 * AUS_RUN1H4,N,2,0
 * AUS_RUN2H1,N,2,0
 * AUS_RUN2H2,N,2,0
 * AUS_RUN2H3,N,2,0
 * AUS_RUN2H4,N,2,0
 * AUS_RUN3H1,N,2,0
 * AUS_RUN3H2,N,2,0
 * AUS_RUN3H3,N,2,0
 * AUS_RUN3H4,N,2,0
 * AUS_RUN4H1,N,2,0
 * AUS_RUN4H2,N,2,0
 * AUS_RUN4H3,N,2,0
 * AUS_RUN4H4,N,2,0
 *
 * LITAUT: (leer)
 * LOK_NR,N,3,0	SPO_SPORTA,C,20	AUT_ANZAHL,N,3,0
 *
 * LITBEI: (leer)
 * SAI_NR,D	SPI_NR,C,9	BEI_ZAHL,C,1
 *
 * LITBEIER: (leer)
 * SAI_NR,D	SPI_NR,C,9	BEI_DATUM,D	BEI_UHRZEI,N,5,0
 *
 * LITCRE:
 * LOK_NR,N,3,0 PK?	
 * SAI_NR,D
 * CRE_SUM,N,5,0 immer "0"
 *
 * LITDOP: (1 Satz)
 * DOP_B,C,1 "n"
 * DOP_C,C,1 "n"
 *
 * LITDOPA: (1 Satz)
 * DOP_A,C,1 "n"
 *
 * LITDRUCK: Druck-Einstellungen (plan,rang,einzel,HF, 180er: report)
 * LITDRUCK2: Druck-Einstellungen (html, doc, ...)
 *
 * LITEIN: (leer)
 * SAI_NR,D	LIG_NR,N,3,0
 * PAR_RUNDE,N,2,0
 * PAR_SPIEL,N,2,0
 * TEA_NR,N,5,0	
 * SPI_NR,C,9
 * EIN_HSPIEL,N,4,0
 * EIN_HSATZ,N,4,0
 * EIN_GSPIEL,N,4,0
 * EIN_GSATZ,N,4,0
 *
 * LITFUS: (leer)
 * FUS_NR,N,3,0	FUS_TEXT1,C,78	FUS_TEXT2,C,78	FUS_TEXT3,C,78
 *
 * LITGLPAN:
 * LIG_STAERK,N,2,0 "1-5"
 * PLG_ANZTEA,N,5,0
 * PLG_POSTEA,N,5,0 "1-8"
 * PLG_GELD,N,14,2
 *
 * LITPAJ: (1 Satz)
 * PAJ_SAI_NR,D
 * PAJ_STAERK,N,2,0  "0"
 * PAJ_LIG_NR,N,3,0  "0"
 * PAJ_VON,D         "09.02.09"
 * PAJ_BIS,D         "07.06.09"
 * PAJ_CODE,C,1 "B"
 *
 * LITPAN:
 * PAN_SAI_NR,D
 * PAN_STAERK,N,2,0 "0"
 * PAN_LIG_NR,N,3,0 "0"
 * PAN_VON,D
 * PAN_BIS,D
 * PAN_ERSDAT,D
 * PAN_UHRZEI,N,5,0 meistens "0"
 *
 * LITPASS:
 * LOK_NR,N,3,0
 * PWD_PASSWO,C,20 "leer"
 *
 * LITPLZ: Postleitzahlen von Städten
 * PLZ_PLZ,C,7	PLZ_ORT,C,35
 *
 * LITUEBER:
 * LOK_NR,N,3,0
 * UEB_ON,N,1,0
 * UEB_TAG,C,10  "Montag"
 * UEB_ZEITVO,N,5,0
 * UEB_ZEITBI,N,5,0
 */
