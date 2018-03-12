/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import entity.Lot;
import javax.swing.ComboBoxModel;

/**
 *
 * @author brun
 */
public class ModelComboAnnulerLot extends LotEnAttenteModel implements ComboBoxModel<Lot>
{
    private Lot lot; // Lot sur lequel on travaille
    
    @Override
    public void setSelectedItem(Object anItem)
    {
        lot = (Lot) anItem;
    }

    @Override
    public Object getSelectedItem()
    {
        return lot;
    }
    
}
