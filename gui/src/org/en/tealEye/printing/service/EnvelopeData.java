package org.en.tealEye.printing.service;

import de.liga.dart.common.service.ServiceFactory;
import de.liga.dart.gruppen.service.GruppenService;
import de.liga.dart.model.Spielort;
import de.liga.dart.model.Ligagruppe;
import de.liga.dart.model.Ligateam;
import de.liga.dart.spielort.service.SpielortService;
import de.liga.util.StringUtils;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Vector;
import java.util.Set;

import org.en.tealEye.framework.BeanTableModel;

/**
 * Created by IntelliJ IDEA.
 * User: Stephan Pudras
 * Date: 17.09.2009
 * Time: 17:38:34
 * To change this template use File | Settings | File Templates.
 */
public class EnvelopeData {

    private Object parentObject;
    private Vector<Vector<String[]>> labelStrings = new Vector<Vector<String[]>>();

    public EnvelopeData(Object parentObject){
        this.parentObject = parentObject;
        aquireData();
    }

    private void aquireData(){
        ServiceFactory.runAsTransaction(new Runnable() {
            public void run() {
        JTable table = (JTable)parentObject;
        int[] rowSelectionCount = table.getSelectedRows();
        if(rowSelectionCount.length<1){
            rowSelectionCount = new int[table.getRowCount()];
            for(int i = 0; i<table.getRowCount();i++){
                rowSelectionCount[i] = i;
            }
        }
        GruppenService gs = ServiceFactory.get(GruppenService.class);
        Spielort spielort = null;
        String[] rowString;


        if(table.getName().equals("Table_Ligagruppe")){
            for(int rowIndex:rowSelectionCount){
                Object obj = ((BeanTableModel) table.getModel()).getObject(rowIndex);
                Ligagruppe gruppe = ((Ligagruppe) obj);
                ArrayList<Long> ortIds = new ArrayList<Long>();
                ArrayList<Long> ortIdx = new ArrayList<Long>();
                Vector<String[]> groupVec = new Vector<String[]>();
            for(int teamIndex = 1; teamIndex <= 8; teamIndex++){

                    Ligateam lt;
                    try {
                        lt = gruppe.findSpiel(gs.findSpieleInGruppe(gruppe), teamIndex).getLigateam();

                    } catch (NullPointerException e) {
                        lt = null;
                    }
                    if (lt != null) {
                        spielort = ServiceFactory.get(SpielortService.class)
                                .findSpielortById(lt.getSpielort().getSpielortId());
                    }
                    if(!(lt==null)){
                        if(!ortIds.contains(spielort.getSpielortId())){
                            rowString = new String[5];
                            String lines[] = StringUtils.wordWrap(spielort.getSpielortName(), 25);
                            rowString[0] = lines[0];
                            if(lines.length > 1) {
                                rowString[1] = lines[1];
                            } else {
                                rowString[1] = "";
                            }
                            rowString[2] = spielort.getStrasse();

                            rowString[3] = spielort.getPlzUndOrt();
                            rowString[4] = "";
                            groupVec.addElement(rowString);

                            ortIds.add(spielort.getSpielortId());
                            ortIdx.add(spielort.getSpielortId());
                            System.out.println(ortIds.size());
                            System.out.println(rowString[0]);
                        }else{
                            //Set<Ligateam> teams = spielort.getLigateamsInGruppe(gruppe);
                            int idx = ortIdx.lastIndexOf(spielort.getSpielortId());

                            String[] entries = groupVec.get(idx);
                            ortIds.add(spielort.getSpielortId());
                            int tmpAnzahl = 0;
                            for(Long anzahl : ortIds){
                                if(anzahl == spielort.getSpielortId())
                                    tmpAnzahl++;
                            }
                            entries[4] = String.valueOf(tmpAnzahl);

                        }
                    }
                }
                    labelStrings.add(groupVec);
            }
        }
    }
        });
    }                                                                              


}
