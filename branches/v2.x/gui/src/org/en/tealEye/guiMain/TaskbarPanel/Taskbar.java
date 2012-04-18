package org.en.tealEye.guiMain.TaskbarPanel;

import javax.swing.*;

/**
 * Description: <br/>
 * User: Stephan
 * Date: 06.11.2007
 * Time: 20:08:54
 */
public interface Taskbar {

    void setTaskbarTask(String task);

    void setTaskbarMessage(String message);

    void setTaskbarProgressBarEnabled(boolean enabled);

    void setTaskbarProgressBarProgress(int oldValue, int newValue);

    String getTaskbarTask();

    String getTaskbarMessage();

    boolean getTaskbarProgressBarEnabled();

    int[] getTaskbarProgressBarProgress();

    JProgressBar getProgBar();

}
