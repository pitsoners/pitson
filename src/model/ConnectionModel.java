/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.HashSet;
import java.util.Set;
import javax.swing.ComboBoxModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import util.ConnectionHistory;
import util.Database;

/**
 * classe qui contient les modèles de données pour les combobox de selection
 * d'utilisateur et de base de donnée à l'écran de connexion. Les informations
 * sont obtenues à partir de l'historique de connexion.
 * ({@link util.ConnectionHistory})
 *
 * @author Thierry
 */
public class ConnectionModel
{
    /**
     * l'utilisateur actuellement selectionné
     */
    private String m_selectedUser;
    /**
     * la base de données actuellement selectionnée
     */
    private Database m_selectedDatabase;

    /**
     * ensemble d'écouteurs auxquels notifier une modification de la liste des utilisateurs
     */
    private final HashSet<ListDataListener> m_userListeners;
    /**
     * ensembel d'écouteurs auxquels notifier une modification de la liste des bases de données
     */
    private final HashSet<ListDataListener> m_databaseListeners;

    /**
     * le modele de données pour le combobox d'utilisateurs
     */
    private ComboBoxUserModel m_userModel;
    /**
     * le modèle de données pour le combobox des bases de données
     */
    private ComboBoxDatabaseModel m_databaseModel;

    /**
     * constructeur par défaut. initialise les deux modèles de données
     */
    public ConnectionModel()
    {
	m_userListeners = new HashSet<>();
	m_databaseListeners = new HashSet<>();
	m_userModel = new ComboBoxUserModel();
	m_databaseModel = new ComboBoxDatabaseModel();
	m_selectedDatabase = new Database("", "");
	setSelectedDatabase(ConnectionHistory.getInstance().getLastDatabase());
	setSelectedUser(ConnectionHistory.getInstance().getLastUser());
	new ConnectionModelHistoryListener();
    }

    /**
     * retourne le tableau de bases de données du modèle, récupéré a partir de l'historique de connexion
     * @return le tableau de bases de données
     */
    public Database[] getDatabases()
    {
	return (Database[]) ConnectionHistory.getInstance().getDatabases().toArray();
    }

    /**
     * retourne le tableau d'utilisateurs qui se sont connectés a la base de données actuellement selectionnée, récupéré à partir de l'historique de connexion
     * @return le tableau d'utilisateurs récemment connectés à la base de données actuellement sélectionnée
     */
    public String[] getUsers()
    {
	Set<String> users = ConnectionHistory.getInstance().getUsers(m_selectedDatabase);
	return users == null ? new String[0] : users.toArray(new String[0]);
    }

    /**
     * retourne le nombre de bases de données dans le modèle (obtenu depuis l'historique de connexion)
     * @return le nombre de bases de données
     */
    public int getDatabaseCount()
    {
	return ConnectionHistory.getInstance().getDatabases().size();
    }

    /**
     * retourne le nombre d'utilisateurs récemment connectés à la base de données actuellement selectionnée(obtenu à partir de l'historique de connexion)
     * @return le nombre d'utilisateurs récemment connectés à la base de données actuellement selectionnée
     */
    public int getUserCount()
    {
	Set<String> users = ConnectionHistory.getInstance().getUsers(m_selectedDatabase);
	return users == null ? 0 : users.size();
    }

    /**
     * retourne l'utilisateur actuellement selectionné
     * @return l'utilisateur actuellement selectionné
     */
    public String getSelectedUser()
    {
	return m_selectedUser;
    }

    /**
     * retourne la base de données actuellement selectionnée
     * @return la base de données actuellement selectionnée
     */
    public Database getSelectedDatabase()
    {
	return m_selectedDatabase;
    }

    /**
     * modifie l'utilisateur actuellement selectionné
     * @param user le nouvel utilisateur
     */
    private void setSelectedUser(String user)
    {
	m_selectedUser = user;
    }

    /**
     * modifie la base de données actuellement selectionnée
     * @param db la nouvelle base de données
     */
    private void setSelectedDatabase(Database db)
    {
	boolean change = m_selectedDatabase == null && db != null || !m_selectedDatabase.equals(db);
	if (change)
	{
	    if (db != null)
	    {
		m_selectedDatabase.setName(db.getName());
		m_selectedDatabase.setUrl(db.getUrl());
	    } else
	    {
		m_selectedDatabase.setName("");
		m_selectedDatabase.setUrl("");
	    }
	    fireUserListChange();
	}
    }

    /**
     * ajoute un écouteur qui sera notifié lorsqu'une modification sur la liste des utilisateurs intervient
     * @param l l'écouteur à ajouter
     */
    private void addUserListDataListener(ListDataListener l)
    {
	if (l != null)
	{
	    m_userListeners.add(l);
	}
    }

    /**
     * retire un écouteur de modification de la liste des utilisateurs
     * @param l l'écouteur à retirer
     */
    private void removeUserListDataListener(ListDataListener l)
    {
	if (l != null)
	{
	    m_userListeners.remove(l);
	}
    }

    /**
     * ajoute un écouteur qui sera notifié lorsqu'une modification sur la liste des bases de données intervient
     * @param l l'écouteur à ajouter
     */
    private void addDatabaseListDataListener(ListDataListener l)
    {
	if (l != null)
	{
	    m_databaseListeners.add(l);
	}
    }

    /**
     * retire un écouteur de  modifications de la liste des bases de données
     * @param l l'écouteur à retirer
     */
    private void removeDatabaseListDataListener(ListDataListener l)
    {
	if (l != null)
	{
	    m_databaseListeners.remove(l);
	}
    }

    /**
     * notifie les écouteurs abonnés qu'un changement a eu lieu sur la liste des utilisateurs
     */
    private void fireUserListChange()
    {
	int newSize = getUserCount();
	ListDataEvent e = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, newSize - 1);
	m_userListeners.forEach(l ->
	{
	    l.contentsChanged(e);
	});
    }
    /**
     * notifie les écouteurs abonnés qu'un changement a eu lieu sur la liste des bases de données
     */
    private void fireDatabaseListChange()
    {
	int newSize = getDatabaseCount();
	ListDataEvent e = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, newSize - 1);
	m_databaseListeners.forEach(l ->
	{
	    l.contentsChanged(e);
	});
    }

    /**
     * retourne le modèle de données pour le comnbobox d'utilisateurs
     * @return le modèle de données pour le comnbobox d'utilisateurs
     */
    public ComboBoxUserModel getUserModel()
    {
	return m_userModel;
    }

    /**
     * retourne le modèle de données pour le combobox de bases de données
     * @return le modèle de données pour le combobox de bases de données
     */
    public ComboBoxDatabaseModel getDatabaseModel()
    {
	return m_databaseModel;
    }

    /**
     * Classe du modèle de données pour le combobox de selection de base de
     * données
     */
    public class ComboBoxDatabaseModel implements ComboBoxModel<Database>
    {
	/**
	 * constructeur qui initialise l'ensemble des écouteurs et la base de
	 * données selectionnée par défaut
	 */
	private ComboBoxDatabaseModel()
	{
	}

	@Override
	public void setSelectedItem(Object anItem)
	{
	    if (anItem instanceof Database)
	    {
		setSelectedDatabase((Database) anItem);
	    }
	}

	@Override
	public Object getSelectedItem()
	{
	    return m_selectedDatabase.clone();
	}

	@Override
	public int getSize()
	{
	    return ConnectionHistory.getInstance().getDatabaseCount();
	}

	@Override
	public Database getElementAt(int index)
	{
	    return (Database) (ConnectionHistory.getInstance().getDatabases().toArray()[index]);
	}

	@Override
	public void addListDataListener(ListDataListener l)
	{
	    addDatabaseListDataListener(l);
	}

	@Override
	public void removeListDataListener(ListDataListener l)
	{
	    removeDatabaseListDataListener(l);
	}
    }

    /**
     * classe du modèle de données pour le comboBox de selection d'utilisateur
     */
    public class ComboBoxUserModel implements ComboBoxModel<String>
    {

	/**
	 * constructeur qui initialise l'ensemble des écouteurs et l'utilisateur
	 * selectionné avec une valeur par défaut
	 */
	private ComboBoxUserModel()
	{
	}

	@Override
	public void setSelectedItem(Object anItem)
	{
	    if (anItem instanceof String)
	    {
		setSelectedUser((String) anItem);
	    }
	}

	@Override
	public Object getSelectedItem()
	{
	    return getSelectedUser();
	}

	@Override
	public int getSize()
	{
	    return getUserCount();
	}

	@Override
	public String getElementAt(int index)
	{
	    String[] users = getUsers();

	    return users[index];
	}

	@Override
	public void addListDataListener(ListDataListener l)
	{
	    addUserListDataListener(l);
	}

	@Override
	public void removeListDataListener(ListDataListener l)
	{
	    removeUserListDataListener(l);
	}
    }

    /**
     * classe d'écouteur qui est notifiée qu'un changement sur l'historique de connexion a eu lieu
     */
    private class ConnectionModelHistoryListener implements ChangeListener
    {

	public ConnectionModelHistoryListener()
	{
	    ConnectionHistory.addChangeListener(this);
	}

	@Override
	public void stateChanged(ChangeEvent e)
	{
	    fireDatabaseListChange();
	    fireUserListChange();
	}

    }
}
