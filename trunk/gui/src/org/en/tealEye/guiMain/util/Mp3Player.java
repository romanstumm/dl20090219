package org.en.tealEye.guiMain.util;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import org.en.tealEye.guiMain.AppStartup;
import org.en.tealEye.guiPanels.applicationLogicPanels.AboutFrame;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Description: <br/>
 * User: Stephan
 * Date: 28.03.2008
 * Time: 06:06:28
 */
public class Mp3Player extends SwingWorker {
    private static final Log log = LogFactory.getLog(Mp3Player.class);
    private Player player;
    private final AboutFrame af;
    private InputStream is;

    public Mp3Player(AboutFrame af) {
        this.af = af;
    }

    protected Object doInBackground() throws Exception {

        File f = new File("media\\sounds\\" + AppStartup.getProperty("about.song.file"));
        try {
            is = new FileInputStream(f);
        } catch (FileNotFoundException e) {
            log.error(f.toString(), e);
        }
        try {
            player = new Player(is);
            af.setPlayer(player);
        } catch (JavaLayerException e) {
            log.error(f.toString(),  e);
        }
        return null;  // do nothing
    }

    protected void done() {

    }
}
