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
public class ModelComboModele extends ModelListeModele implements ComboBoxModel<String>
{

    private String modele; // Modele sur lequel on travail

    @Override
    public void setSelectedItem(Object anItem)
    {
        modele = (String) anItem;
    }

    @Override
    public Object getSelectedItem()
    {
        return modele;
    }

}
