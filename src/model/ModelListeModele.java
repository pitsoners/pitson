/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import dao.ManagerModele;
import java.util.ArrayList;
import javax.swing.AbstractListModel;

/**
 *
 * @author brun
 */
public class ModelListeModele extends AbstractListModel<String>
{

    protected ArrayList<String> liste;

    public ModelListeModele()
    {
        liste = ManagerModele.getListeIdModele();
    }

    @Override
    public int getSize()
    {
        return liste.size();
    }

    @Override
    public String getElementAt(int index)
    {
        return liste.get(index);
    }

}
