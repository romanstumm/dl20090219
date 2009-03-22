package de.liga.dart.gruppen;

import java.io.Serializable;

/**
 * Description: <br/>
 * User: roman.stumm <br/>
 * Date: 03.03.2008 <br/>
 * Time: 18:53:47 <br/>
 * Copyright: Agimatec GmbH
 */
public class PositionStatusInfo implements Serializable {
    private final PositionStatus status;
    private final String info;


    public PositionStatusInfo(PositionStatus positionStatus, String info) {
        this.status = positionStatus;
        this.info = (info == null ? "" : info);
    }

    public String getInfo() {
        return info;
    }

    public PositionStatus getStatus() {
        return status;
    }

    public String toString() {
        if (info.length() > 0) {
            return status.getToolTip() + info;
        } else {
            return status.getToolTip();
        }
    }
}
