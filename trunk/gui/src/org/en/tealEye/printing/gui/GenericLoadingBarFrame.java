package org.en.tealEye.printing.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Stephan Pudras
 * Date: 03.09.2009
 * Time: 01:36:19
 * To change this template use File | Settings | File Templates.
 */
public class GenericLoadingBarFrame extends JFrame {

    private JProgressBar loadBar;
    private Point parentWindow;
    private JPanel jPanel1;

    public GenericLoadingBarFrame(Point parentWindow){
        this.parentWindow = parentWindow;
        initComponents();
    }

    private void initComponents(){
        jPanel1 = new javax.swing.JPanel();
        loadBar = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        setMinimumSize(new java.awt.Dimension(200, 100));
        setName("Form"); // NOI18N
        setResizable(false);
        setUndecorated(true);
        loadBar.setIndeterminate(true);
        jPanel1.setBackground(Color.darkGray); // NOI18N
        jPanel1.setBorder(BorderFactory.createLineBorder(Color.GREEN, 5));
        jPanel1.setName("jPanel1"); // NOI18N

        loadBar.setName("jProgressBar1"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(loadBar, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(loadBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
//        System.out.println(parentWindow.getLocation());
        this.setLocation((int)parentWindow.getX()+((1024-200)/2),(int)parentWindow.getY()+((768-100)/2));
        this.setVisible(true);
    }

    public void setProgress(int value, String loadText){
        loadBar.setValue(value);
        loadBar.setString(loadText);
    }

    public void removeFrame(){
        this.dispose();
    }
}
