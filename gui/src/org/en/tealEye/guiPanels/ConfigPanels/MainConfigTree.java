package org.en.tealEye.guiPanels.ConfigPanels;

import org.en.tealEye.guiExt.ExtPanel.ExtendedJPanelImpl;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Stephan
 * Date: 05.11.2007
 * Time: 18:55:13
 * To change this template use File | Settings | File Templates.
 */
public class MainConfigTree extends ExtendedJPanelImpl {

    public MainConfigTree(TreeSelectionListener tsl) {
        super();
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(200, 200));
        setMaximumSize(new Dimension(200, 200));
        setPreferredSize(new Dimension(200, 200));
        setTitle("Einstellungen");
        initTree(tsl);
    }

    private void initTree(TreeSelectionListener tsl) {
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("Einstellungen", true);
        JTree tree = new JTree(top);
        tree.setCellRenderer(new MyRenderer());
        tree.setRootVisible(true);
        tree.getShowsRootHandles();
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        DefaultMutableTreeNode configFonts = new DefaultMutableTreeNode("Schriftarten", false);
//        DefaultMutableTreeNode configDB = new DefaultMutableTreeNode("Datenbank", false);
        top.add(configFonts);
//        top.add(configDB);
        tree.expandRow(0);
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
        tree.addTreeSelectionListener(tsl);
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
            if (((DefaultMutableTreeNode) value).getUserObject().equals("Einstellungen")) {
                setPreferredSize(new Dimension(150, 22));
            } else if (((DefaultMutableTreeNode) value).getUserObject().equals("Schriftarten")) {
                setPreferredSize(new Dimension(150, 22));
            } else if (((DefaultMutableTreeNode) value).getUserObject().equals("Datenbank")) {
                setPreferredSize(new Dimension(150, 22));
            }
            return this;
        }
    }
}
