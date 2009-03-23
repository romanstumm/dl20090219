package org.en.tealEye.controller.gui;

import org.en.tealEye.guiMain.Hypervisor;
import org.en.tealEye.guiMain.MainAppFrame;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: stephan
 * Date: 19.03.2009
 * Time: 03:02:31
 * To change this template use File | Settings | File Templates.
 */
public class MainController implements KeyListener {

    private final Hypervisor h;

    public MainController(Hypervisor h){
        this.h = h;      
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if(code == KeyEvent.VK_F1){
         h.showPanel("CreateGroup");
        }else if(code == KeyEvent.VK_F2){
         h.showPanel("CreateTeam");
        }else if(code == KeyEvent.VK_F3){
         h.showPanel("CreateLocation");
        }else if(code == KeyEvent.VK_F4){
         h.showPanel("CreateVendor");
        }else if(code == KeyEvent.VK_F5){
         h.showPanel("ShowGroups");
        }else if(code == KeyEvent.VK_F6){
         h.showPanel("ShowTeams");
        }else if(code == KeyEvent.VK_F7){
         h.showPanel("ShowLocations");
        }else if(code == KeyEvent.VK_F8){
         h.showPanel("ShowVendors");
        }else{
            
        }
    }
}
