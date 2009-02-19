package org.en.tealEye.framework;

import org.apache.commons.lang.StringUtils;
import org.en.tealEye.guiExt.ExtPanel.ExtendedJPanelImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Description:   <br/>
 * User: roman
 * Date: 23.11.2007, 22:44:38
 */
public class SwingUtils {
    public static void acceptDigitsOnly(final JTextField field) {
        field.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
                // do nothing
            }

            public void keyPressed(KeyEvent e) {
                // do nothing
            }

            public void keyReleased(KeyEvent e) {
                String text = field.getText();
                if (text != null && text.length() > 0 &&
                        !StringUtils.isNumeric(text)) {
                    field
                            .setText(text.replace("" + e.getKeyChar(), ""));
                }
            }
        });
    }

    public static boolean createYesNoDialog(Component parent, String msg, String title) {
        int result = JOptionPane.showConfirmDialog(parent, msg,
                title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return result == JOptionPane.YES_OPTION;
    }

    public static void createOkDialog(Component parent, String msg, String title) {
        JOptionPane.showConfirmDialog(parent, msg,
                title, JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
    }

    public static String showInputDialog(String msg, String initialValue) {
        return JOptionPane.showInputDialog(msg, initialValue);
    }

    public static ExtendedJPanelImpl getExtendedJPanel(JComponent jComponent) {
        Container panel = jComponent.getParent();
        while (panel != null) {
            if (panel instanceof ExtendedJPanelImpl) return (ExtendedJPanelImpl) panel;
            panel = panel.getParent();
        }
        return null;
    }
}
