/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import dao.ManagerDatabaseUser;
import entity.DatabaseUser;
import java.util.ArrayList;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

/**
 *
 * @author boulhol
 */
public class ComboBoxUserModel extends AbstractListModel<String> implements ComboBoxModel<String>
{
    private String m_selected;
    private ArrayList<DatabaseUser> m_list;
    
    public ComboBoxUserModel()
    {
	m_list = ManagerDatabaseUser.getDatabaseUsers();
    }
    
    @Override
    public int getSize()
    {
	return m_list.size();
    }

    @Override
    public String getElementAt(int index)
    {
	return m_list.get(index).getUser();
    }

    @Override
    public void setSelectedItem(Object anItem)
    {
	m_selected = (String)anItem;
    }

    @Override
    public Object getSelectedItem()
    {
	return m_selected;
    }
    
    public void setUser(DatabaseUser user)
    {
	int index = getDatabaseUserIndex(user.getUser());
	if (index == -1)
	{
	    m_list.add(user);
	    fireIntervalAdded(this, getSize(), getSize());
	}
	else
	{
	    m_list.set(index, user);
	    fireContentsChanged(this, index, index);
	}
    }
    
    /**
     * reourne le role de l'utilisateur donné si celui-ci appartient à la liste des utilistaeurs du modèle
     * @param user l'utilisateur dont on veut récupérer le rôle
     * @return le role de l'utilistateur, ou {@code null} s'il n'est pas présent dans la liste d'utilisateurs
     */
    public String getRole(String user)
    {
	int index = getDatabaseUserIndex(user);
	return index > -1 ? m_list.get(index).getRole() : null;
    }
    
    /**
     * retourne le role de l'utilisateur selectionné si celui-ci est présent dans la liste des utilisateurs du modèle
     * @return le role de l'utilisateur ou {@code null} s'il n'est pas dans la liste d'utilisateurs
     */
    public String getSelectedRole()
    {
	return getRole(m_selected);
    }
    
    public void removeUser(String user)
    {
	int index = getDatabaseUserIndex(user);
	if (index > -1 )
	{
	    m_list.remove(index);
	    fireIntervalRemoved(this, index, index);
	}
    }
    
    /**
     * retourne l'index de l'utilisateur donnné dans la liste de DatabaseUser
     * @param user le nom d'utilisateur dont retrouver l'index
     * @return l'index du DatabaseUser, ou {@code -1} si non présent
     */
    private int getDatabaseUserIndex(String user)
    {
	int index = 0;
	while (index < m_list.size() && !m_list.get(index).getUser().equals(user))
	   //arret i fin de liste ou si user trouvé
	{
	    index ++;
	}
	if (index >= m_list.size())
	{
	    index = -1;
	}
	return index;
    }
}
