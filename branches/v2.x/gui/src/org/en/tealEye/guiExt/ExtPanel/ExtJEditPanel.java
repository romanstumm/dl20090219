package org.en.tealEye.guiExt.ExtPanel;

import org.en.tealEye.framework.PanelMapper;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Description: <br/>
 * User: Stephan
 * Date: 13.11.2007
 * Time: 22:54:47
 */
public abstract class ExtJEditPanel extends ExtendedJPanelImpl {
    private Object modelEntity;

    public abstract Class getModelClass();

    public void newModelEntity() {
        try {
            modelEntity = getModelClass().newInstance();
        } catch (Exception e) {
            log.error("cannot create new model", e);
        }
    }

    public Object getModelEntity() {
        return modelEntity;
    }

    public void setModelEntity(Object modelEntity) {
        this.modelEntity = modelEntity;
    }

    public final void updatePanel(Object obj) {
        setModelEntity(obj);
        if (getModelEntity() == null) {
            newModelEntity();
        }
        doUpdatePanel();
        PanelMapper.getInstance().setPanelValues(getModelEntity(), this);
    }

    public final void updatePanel() {
        if (getModelEntity() == null) {
            newModelEntity();
        }
        super.updatePanel();
        PanelMapper.getInstance().setPanelValues(getModelEntity(), this);
    }

    public Map<String, Component> getPanelTextFields() {
        Component[] components = this.findComponents();
        Map<String, Component> tfields = new HashMap<String, Component>();
        for (Component comp : components) {
            tfields.put(comp.getName(), comp);
        }
        return tfields;
    }
}
