/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javax.swing.ComboBoxModel;

/**
 *
 * @author brun
 */
public class ModelComboCategorie extends ModelListeCategorie implements ComboBoxModel<String>
{

    private String categorie; // Categorie sur laquelle on travaille

    @Override
    public void setSelectedItem(Object anItem)
    {
        categorie = (String) anItem;
    }

    @Override
    public Object getSelectedItem()
    {
        return categorie;
    }

}
