/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package render;

import entity.Lot;
import java.awt.Component;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author preda
 */
public class RendererListLotAttente implements ListCellRenderer<Lot>
{

    @Override
    public Component getListCellRendererComponent(JList<? extends Lot> list, Lot lot, int index, boolean isSelected, boolean cellHasFocus)
    {
        JLabel lab = new JLabel("Lot n° "+lot.getIdLot()+" - ["+lot.getIdModele()+"] - "+lot.getNbrPieceDemande() + " pièces");
        lab.setOpaque(true);
        if (isSelected)
        {
            lab.setBackground(list.getSelectionBackground());
            lab.setForeground(list.getSelectionForeground());
        }
        else
        {
            lab.setBackground(list.getBackground());
            lab.setForeground(list.getForeground());
        }
        if (cellHasFocus)
        {
            lab.setBorder(new javax.swing.border.LineBorder(Color.yellow));
        }
        
        return lab;
    }
}
