/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import javax.swing.AbstractListModel;

/**
 *
 * @author brun
 */
public class ModelListeCategorie extends AbstractListModel<String>
{
    
    protected ArrayList<String> liste = new ArrayList<>(); // Liste des catégories 
    
    public ModelListeCategorie()
    {
        liste.add("Petit");
        liste.add("Moyen");
        liste.add("Grand");
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
