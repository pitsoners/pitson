/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import util.DatabaseTools;

/**
 *
 * @author Thierry
 */
public class ComboBoxRoleModel extends AbstractListModel<String> implements ComboBoxModel<String>
{
    String m_selectedRole;

    @Override
    public int getSize()
    {
	return DatabaseTools.ROLES.length - 1;
    }

    @Override
    public String getElementAt(int index)
    {
	return DatabaseTools.ROLES[index + 1];
    }

    @Override
    public void setSelectedItem(Object anItem)
    {
	m_selectedRole = (String)anItem;
    }

    @Override
    public Object getSelectedItem()
    {
	return m_selectedRole;
    }
    
}
