package org.en.tealEye.guiMain.TaskbarPanel;

import org.en.tealEye.guiExt.ExtPanel.ExtendedJPanelImpl;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * Description: <br/>
 * User: Stephan
 * Date: 06.11.2007
 * Time: 20:13:55
 */
public abstract class TaskbarImpl extends ExtendedJPanelImpl implements Taskbar {

    private Map<String, Font> fontMap;
    private Map<String, ImageIcon> iconMap;
    private String title;
    private String taskbarTask;
    private String taskbarMessage;
    private boolean taskbarProgressEnabled;
    private final int[] taskbarProgressValues = new int[2];

    public TaskbarImpl() {
        super();
    }

    public void setTaskbarTask(String task) {
        this.taskbarTask = task;
    }

    public void setTaskbarMessage(String message) {
        this.taskbarMessage = message;
    }

    public void setTaskbarProgressBarEnabled(boolean enabled) {
        this.taskbarProgressEnabled = enabled;
    }

    public void setTaskbarProgressBarProgress(int oldValue, int newValue) {
        this.taskbarProgressValues[0] = oldValue;
        this.taskbarProgressValues[1] = newValue;
    }

    public String getTaskbarTask() {
        return this.taskbarTask;
    }

    public String getTaskbarMessage() {
        return this.taskbarMessage;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean getTaskbarProgressBarEnabled() {
        return this.taskbarProgressEnabled;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int[] getTaskbarProgressBarProgress() {
        return this.taskbarProgressValues;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setTitle(String string) {
        this.title = string; //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getTitle() {
        return this.title;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setIconMap(Map<String, ImageIcon> map) {
        this.iconMap = map;
    }

    public Map<String, ImageIcon> getIconMap() {
        return iconMap;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setFontMap(Map<String, Font> map) {
        this.fontMap = map;//To change body of implemented methods use File | Settings | File Templates.
    }

    public Map<String, Font> getFontMap() {
        return this.fontMap;  //To change body of implemented methods use File | Settings | File Templates.
    }

}
