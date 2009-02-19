package de.liga.dart.gruppen;

import java.io.Serializable;

/**
 * Description: <br/>
 * User: roman.stumm <br/>
 * Date: 03.03.2008 <br/>
 * Time: 18:53:47 <br/>
 * Copyright: Agimatec GmbH
 */
public class TeamStatusInfo implements Serializable {
    private final TeamStatus teamStatus;
    private final String info;


    public TeamStatusInfo(TeamStatus teamStatus, String info) {
        this.teamStatus = teamStatus;
        this.info = (info == null ? "" : info);
    }

    public String getInfo() {
        return info;
    }

    public TeamStatus getTeamStatus() {
        return teamStatus;
    }

    public String toString() {
        if (info.length() > 0) {
            return teamStatus.getToolTip() + info;
        } else {
            return teamStatus.getToolTip();
        }
    }
}
