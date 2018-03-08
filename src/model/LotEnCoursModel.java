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
public class LotEnCoursModel extends AbstractListModel<Lot>
{

    private ArrayList<Lot> listLot;
    
    public LotEnCoursModel()
    {
        listLot = ManagerLot.getLotEnCoursList();
    }
    
       public void addLot(Lot l)
    {
        if(l != null)
        {
            listLot.add(l);
            fireIntervalAdded(listLot, listLot.size() - 1, listLot.size() - 1);
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
