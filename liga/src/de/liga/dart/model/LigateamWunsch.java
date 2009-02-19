package de.liga.dart.model;

import java.io.Serializable;

/**
 * Description:   <br/>
 * User: roman
 * Date: 07.02.2008, 23:16:10
 */
public class LigateamWunsch implements Serializable, LigaPersistence
{
    private long wunschId;
    private Ligateam team1;
    private Ligateam team2;
    private int wunschArt;

    public long getWunschId() {
        return wunschId;
    }

    public void setWunschId(long wunschId) {
        this.wunschId = wunschId;
    }

    public Ligateam getTeam1() {
        return team1;
    }

    public void setTeam1(Ligateam team1) {
        this.team1 = team1;
    }

    public Ligateam getTeam2() {
        return team2;
    }

    public void setTeam2(Ligateam team2) {
        this.team2 = team2;
    }

    /**
     * Werte und deren Bedeutung, siehe WunschArt.
     * Hier könnte man auch eine Enum verwenden...
     * @return
     */
    public int getWunschArt() {
        return wunschArt;
    }

    public void setWunschArt(int wunschArt) {
        this.wunschArt = wunschArt;
    }

    public long getKeyValue() {
        return getWunschId();
    }

    public Class getModelClass() {
        return LigateamWunsch.class;
    }

    public String toInfoString() {
        return getClass().getSimpleName();
    }

}
