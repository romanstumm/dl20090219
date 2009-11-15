package org.en.tealEye.framework;

import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.util.HashMap;
import java.util.Map;

public abstract class ComponentRegistry {

    protected abstract void setup();

    protected final Map<String, ModelFactory<ComboBoxModel>> comboModels = new HashMap();
    protected final Map<String, ModelFactory<ListModel>> listModels = new HashMap();
    protected final Map<String, ModelFactory<TableModel>> tableModels = new HashMap();

    public ComboBoxModel setComboBoxModel(JComboBox comboBox, JPanel panel) {
        if (StringUtils.isNotEmpty(comboBox.getName())) {
            ModelFactory<ComboBoxModel> modelFactory =
                  comboModels.get(comboBox.getName());
            if (modelFactory != null) {
                int idx = comboBox.getSelectedIndex();
                comboBox.setModel(modelFactory.create(panel));
                if (idx >= 0 && idx < comboBox.getModel().getSize()) {
                    comboBox.setSelectedIndex(idx);
                }
            }
        }
        return comboBox.getModel();
    }

    public ListModel setListModel(JList jList, JPanel panel) {
        if (StringUtils.isNotEmpty(jList.getName())) {
            ModelFactory<ListModel> modelFactory = listModels.get(jList.getName());
            if (modelFactory != null) {
                int[] index = jList.getSelectedIndices();
                jList.setModel(modelFactory.create(panel));
                jList.setSelectedIndices(index);
            }
        }
        return jList.getModel();
    }


    public TableModel setTableModel(JTable jTable, JPanel panel) {
        if (StringUtils.isNotEmpty(jTable.getName())) {
            ModelFactory<TableModel> modelFactory = tableModels.get(jTable.getName());
            if (modelFactory != null) {
                int[] index = jTable.getSelectedRows();
                TableModel model = modelFactory.create(panel);
                jTable.setModel(model);
                for (int i : index) {
                    jTable.getSelectionModel().addSelectionInterval(i, i);
                }
            }
        }
        return jTable.getModel();
    }


}
