package org.en.tealEye.guiMain.FloatPanel;

import org.en.tealEye.controller.gui.MenuController;
import org.en.tealEye.guiExt.TitleBarPanel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.tree.*;
import java.awt.*;
import java.util.Map;

/**
 * Description: <br/>
 * User: Stephan
 * Date: 03.11.2007
 * Time: 03:21:40
 */
public class FloatingTreeMenu extends TitleBarPanel {


    public FloatingTreeMenu(MenuController menuController, Map<String, ImageIcon> iconMap) {
        super();
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(200, 250));
        setMaximumSize(new Dimension(200, 250));
        setPreferredSize(new Dimension(200, 250));
        setTitle("Hauptmenü");
        setIconMap(iconMap);
        initComponents(menuController);
    }

    private void initComponents(MenuController menuController) {
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("Einträge", true);
        DefaultMutableTreeNode edit = new DefaultMutableTreeNode("Einträge Anlegen", true);
        DefaultMutableTreeNode view = new DefaultMutableTreeNode("Einträge Anzeigen", true);
        top.add(edit);
        top.add(view);

        JTree tree = new JTree(top);
        tree.setCellRenderer(new MyRenderer());
        tree.setRootVisible(true);
//        tree.getShowsRootHandles();
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.expandRow(0);

        MutableTreeNode createGroup = new DefaultMutableTreeNode("Gruppe anlegen", false);
        MutableTreeNode createTeam = new DefaultMutableTreeNode("Team anlegen", false);
        MutableTreeNode createLocation = new DefaultMutableTreeNode("Spielort anlegen", false);
        MutableTreeNode createVendor = new DefaultMutableTreeNode("Aufsteller anlegen", false);
        MutableTreeNode showGroups = new DefaultMutableTreeNode("Gruppen anzeigen", false);
        MutableTreeNode showTeams = new DefaultMutableTreeNode("Teams anzeigen", false);
        MutableTreeNode showLocations = new DefaultMutableTreeNode("Spielorte anzeigen", false);
        MutableTreeNode showProviders = new DefaultMutableTreeNode("Aufsteller anzeigen", false);
        edit.add(createGroup);
        edit.add(createTeam);
        edit.add(createLocation);
        edit.add(createVendor);
        view.add(showGroups);
        view.add(showTeams);
        view.add(showLocations);
        view.add(showProviders);
        tree.addMouseListener(menuController);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(21, 0, 0, 0);
        panel.add(new JScrollPane(tree), gbc);
        this.add(panel, BorderLayout.CENTER);
    }

    public Map<String, ImageIcon> getIcons() {
        return this.getIconMap();
    }


    private class MyRenderer extends DefaultTreeCellRenderer {

        public MyRenderer() {
        }

        public Component getTreeCellRendererComponent(
                JTree tree,
                Object value,
                boolean sel,
                boolean expanded,
                boolean leaf,
                int row,
                boolean hasFocus) {

            super.getTreeCellRendererComponent(
                    tree, value, sel,
                    expanded, leaf, row,
                    hasFocus);

            Border outsideBorder = BorderFactory.createEmptyBorder(5, 0, 5, 0);
            this.setBorder(outsideBorder);
            if (((DefaultMutableTreeNode) value).getUserObject().equals("Einträge")) {
                setPreferredSize(new Dimension(150, 22));
                setIcon(getIcons().get("network.png"));
            } else if (((DefaultMutableTreeNode) value).getUserObject().equals("Einträge Anlegen")) {
                setPreferredSize(new Dimension(150, 22));
                setIcon(getIcons().get("edit.png"));
            } else if (((DefaultMutableTreeNode) value).getUserObject().equals("Einträge Anzeigen")) {
                setPreferredSize(new Dimension(150, 22));
                setIcon(getIcons().get("viewmag.png"));
            } else if (((DefaultMutableTreeNode) value).getUserObject().equals("Gruppe anlegen")) {
                setPreferredSize(new Dimension(150, 22));
                setIcon(getIcons().get("groupevent.png"));
            } else if (((DefaultMutableTreeNode) value).getUserObject().equals("Team anlegen")) {
                setPreferredSize(new Dimension(150, 22));
                setIcon(getIcons().get("groupevent.png"));
            } else if (((DefaultMutableTreeNode) value).getUserObject().equals("Spielort anlegen")) {
                setPreferredSize(new Dimension(150, 22));
                setIcon(getIcons().get("kfm-home.png"));
            } else if (((DefaultMutableTreeNode) value).getUserObject().equals("Aufsteller anlegen")) {
                setSize(150, 22);
                setIcon(getIcons().get("joystick.png"));
            } else if (((DefaultMutableTreeNode) value).getUserObject().equals("Gruppen anzeigen")) {
                setPreferredSize(new Dimension(150, 22));
                setIcon(getIcons().get("contents.png"));
            } else if (((DefaultMutableTreeNode) value).getUserObject().equals("Teams anzeigen")) {
                setPreferredSize(new Dimension(150, 22));
                setIcon(getIcons().get("contents.png"));
            } else if (((DefaultMutableTreeNode) value).getUserObject().equals("Spielorte anzeigen")) {
                setPreferredSize(new Dimension(150, 22));
                setIcon(getIcons().get("contents.png"));
            } else if (((DefaultMutableTreeNode) value).getUserObject().equals("Aufsteller anzeigen")) {
                setPreferredSize(new Dimension(150, 22));
                setIcon(getIcons().get("contents.png"));
            }
            return this;
        }
    }
}
