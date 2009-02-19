package org.en.tealEye.guiPanels.applicationLogicPanels;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import org.en.tealEye.guiMain.util.GLRotatingCubeFrame;
import org.en.tealEye.guiMain.util.Mp3Player;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Description: <br/>
 * User: Stephan
 * Date: 28.03.2008
 * Time: 06:01:20
 */
public class AboutFrame extends JComponent implements WindowListener {
    private static final Log log = LogFactory.getLog(AboutFrame.class);

    private SwingWorker player;
    private Player mplayer;

    public AboutFrame(){
     initComponents();
    }

    public void initComponents(){
        new GLRotatingCubeFrame(this);
        initSoundPlayer();
     }

    public void initSoundPlayer(){
        player = new Mp3Player(this);
        player.execute();
    }


    public void windowOpened(WindowEvent e) {

    }

    public void windowClosing(WindowEvent e) {
       mplayer.close();
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
        // do nothing
    }

    public void windowActivated(WindowEvent e) {

    }

    public void windowDeactivated(WindowEvent e) {
        // do nothing
    }

    public void setPlayer(Player player) {
        mplayer = player;
        try {
            mplayer.play();
        } catch (JavaLayerException e) {
            log.error(e.getMessage(), e);
        }
    }
}
