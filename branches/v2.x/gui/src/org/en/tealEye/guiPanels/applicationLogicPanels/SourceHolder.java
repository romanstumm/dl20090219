package org.en.tealEye.guiPanels.applicationLogicPanels;

import javax.swing.*;

/**
 * Description: <br/>
 * User: Stephan
 * Date: 20.03.2008
 * Time: 01:43:28
 */
public class SourceHolder {

    private JComponent source;

    public SourceHolder(){

    }

    public void setSource(JComponent c){
        this.source = c;
    }

    public JComponent getSource(){
        return source;        
    }
}
