/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import dao.ManagerLot;
import entity.Lot;
import java.util.ArrayList;
import javax.swing.AbstractListModel;

/**
 *
 * @author preda
 */
public class LotEnAttenteModel extends AbstractListModel<Lot>
{

    private ArrayList<Lot> listLot;
    
    public LotEnAttenteModel()
    {
        listLot = ManagerLot.getLotEnAttenteList();
    }
    
    public void removeLot(Lot l) 
    {
        if(l != null)
        {
            int index = listLot.indexOf(l);
            listLot.remove(l);
            fireIntervalRemoved(listLot, index, index);
        }
    }
    
    @Override
    public int getSize()
    {
        return listLot.size();
    }

    @Override
    public Lot getElementAt(int index)
    {
        return listLot.get(index);
    }
    
}
