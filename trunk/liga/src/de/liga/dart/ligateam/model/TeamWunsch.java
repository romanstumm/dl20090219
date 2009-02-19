package de.liga.dart.ligateam.model;

import de.liga.dart.model.Ligateam;
import de.liga.dart.model.LigateamWunsch;

/**
 * Description:   <br/>
 * User: roman
 * Date: 09.02.2008, 12:06:53
 */
public class TeamWunsch {
    private Ligateam otherTeam;
    private int wunschArt = WunschArt.WHITELIST_MUST;

    public TeamWunsch(Ligateam thisTeam, LigateamWunsch wunsch) {
        if (wunsch.getTeam1().getLigateamId() == thisTeam.getLigateamId()) {
            setOtherTeam(wunsch.getTeam2());
        } else {
            setOtherTeam(wunsch.getTeam1());
        }
        setWunschArt(wunsch.getWunschArt());
    }

    public TeamWunsch(Ligateam otherTeam) {
        this.otherTeam = otherTeam;
    }

    public Ligateam getOtherTeam() {
        return otherTeam;
    }

    public void setOtherTeam(Ligateam otherTeam) {
        this.otherTeam = otherTeam;
    }

    public int getWunschArt() {
        return wunschArt;
    }

    public void setWunschArt(int wunschArt) {
        this.wunschArt = wunschArt;
    }

    public String toString() {
        return getWunschArtName() + "mit " + otherTeam.toString();
    }

    public String getWunschArtName() {
        return name(wunschArt);
    }

    public static int valueOf(String teamWunschName)
    {
         if (teamWunschName.equals("MÖCHTE IMMER ")) {
             return WunschArt.WHITELIST_SHALL;
         } else if (teamWunschName.equals("DARF NIE ")) {
             return WunschArt.BLACKLIST_MUST;
         } else if (teamWunschName.equals("MÖCHTE NIE ")) {
             return WunschArt.BLACKLIST_SHALL;
         } else {
             return WunschArt.WHITELIST_MUST;
         }
    }
           
    public static String name(int wunschArt) {
        switch (wunschArt) {

            case WunschArt.WHITELIST_SHALL:
                return "MÖCHTE IMMER ";
            case WunschArt.BLACKLIST_MUST:
                return "DARF NIE ";
            case WunschArt.BLACKLIST_SHALL:
                return "MÖCHTE NIE ";
            case WunschArt.WHITELIST_MUST:
            default:
                return "MUSS IMMER ";
        }
    }
}
