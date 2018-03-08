/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package render;

import entity.Machine;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author preda
 */
public class RendererListMachineDispo implements ListCellRenderer<Machine>
{

    @Override
    public Component getListCellRendererComponent(JList<? extends Machine> list, Machine ma, int index, boolean isSelected, boolean cellHasFocus)
    {
        JLabel lab = new JLabel("n° "+ma.getIdPresse()+" - ["+ma.getLibellePresse()+"] - ");
        lab.setOpaque(true);
        if(isSelected)
        {
            lab.setBackground(list.getSelectionBackground());
            lab.setForeground(list.getSelectionForeground());
        }
        else
        {
            lab.setBackground(list.getBackground());
            lab.setForeground(list.getForeground());
        }
        return lab;
    }
    
}
