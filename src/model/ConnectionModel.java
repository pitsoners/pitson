package model;

import java.util.HashSet;
import java.util.Set;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import util.ConnectionHistory;
import util.Database;

/**
 * classe qui contient les modèles de données pour les combobox de selectiond d'utilisateur et de base de donnée à l'écran de
 * connexion. Les informations sont obtenues à partir de l'historique de connexion. ({@link util.ConnectionHistory})
 *
 * @author Thierry
 */
public class ConnectionModel
{
    /**
     * modèle de données pour le comboBox base de données
     */
    private final ComboBoxDatabaseModel m_databaseModel;
    /**
     * modèle de données pour le comboBox login
     */
    private final ComboBoxUserModel m_userModel;

    /**
     * constructeur par défaut. initialise les deux modèles de données
     */
    public ConnectionModel()
    {
	m_databaseModel = new ComboBoxDatabaseModel();
	m_userModel = new ComboBoxUserModel();
    }

    /**
     * retourne le modèle de données du combobox de selection de base de données
     * @return le modèle de données du combobox de selection de base de données
     */
    public ComboBoxDatabaseModel getDatabaseModel()
    {
	return m_databaseModel;
    }

    /**
     * retourne le modèle de données du comboBox de selection d'utilisateur
     * @return le modèle de données du comboBox de selection d'utilisateur
     */
    public ComboBoxUserModel getUserModel()
    {
	return m_userModel;
    }

    /**
     * Classe du modèle de données pour le combobox de selection de base de données
     */
    public class ComboBoxDatabaseModel implements ComboBoxModel<Database>
    {
	/**
	 * base de donnée actuellement selectionnée
	 */
	private Database m_selectedDatabase;
	/**
	 * ensemble d'écouteurs abonnés en cas de changement dans le modèle
	 */
	private final HashSet<ListDataListener> m_listeners;

	/**
	 * constructeur qui initialise l'ensemble des écouteurs et la base de données selectionnée par défaut
	 */
	private ComboBoxDatabaseModel()
	{
	    m_listeners = new HashSet<>();
	    m_selectedDatabase = new Database("", "");
	    setSelectedItem(ConnectionHistory.getInstance().getLastDatabase());
	}

	@Override
	public void setSelectedItem(Object anItem)
	{
	    if (anItem instanceof Database)
	    {
		int oldSize = getUserModel().getSize();
		Database db = (Database)anItem;
		m_selectedDatabase.setName(db.getName());
		m_selectedDatabase.setUrl(db.getUrl());
		getUserModel().fireUserListChange(oldSize);
	    }
	}

	@Override
	public Object getSelectedItem()
	{
	    return m_selectedDatabase;
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
	    if (l != null)
	    {
		m_listeners.add(l);
	    }
	}

	@Override
	public void removeListDataListener(ListDataListener l)
	{
	    if (l != null)
	    {
		m_listeners.remove(l);
	    }
	}
    }

    /**
     * classe du modèle de données pour le comboBox de selection d'utilisateur
     */
    public class ComboBoxUserModel implements ComboBoxModel<String>
    {

	/**
	 * nom d'utilisateur actuellement selectionné
	 */
	private String m_selectedUser;
	/**
	 * ensemble d'écouteurs en cas de changement dans le modèle
	 */
	private HashSet<ListDataListener> m_listeners;

	/**
	 * constructeur qui initialise l'ensemble des écouteurs et l'utilisateur selectionné avec une valeur par défaut
	 */
	private ComboBoxUserModel()
	{
	    m_listeners = new HashSet<>();
	    m_selectedUser = ConnectionHistory.getInstance().getLastUser();
	}

	@Override
	public void setSelectedItem(Object anItem)
	{
	    if (anItem instanceof String)
	    {
		m_selectedUser = (String) anItem;
	    }
	}

	@Override
	public Object getSelectedItem()
	{
	    return m_selectedUser;
	}

	@Override
	public int getSize()
	{
	    int size;
	    Set<String> users = ConnectionHistory.getInstance().getUsers((Database) getDatabaseModel().getSelectedItem());
	    if (users != null)
	    {
		size = users.size();
	    } else
	    {
		size = 0;
	    }
	    return size;
	}

	@Override
	public String getElementAt(int index)
	{
	    String user;
	    Set<String> users = ConnectionHistory.getInstance().getUsers((Database) getDatabaseModel().getSelectedItem());
	    if (users != null)
	    {
		user = (String) users.toArray()[index];
	    } else
	    {
		user = null;
	    }
	    return user;
	}

	@Override
	public void addListDataListener(ListDataListener l)
	{
	    if (l != null)
	    {
		m_listeners.add(l);
	    }
	}

	@Override
	public void removeListDataListener(ListDataListener l)
	{
	    if (l != null)
	    {
		m_listeners.remove(l);
	    }
	}

	/**
	 * notifie les écouteurs que la liste des utilisateurs a été modifiée
	 * @param oldSize l'ancienne taille de la liste
	 */
	private void fireUserListChange(int oldSize)
	{
	    int newSize = getSize();
	    if (oldSize < newSize)
	    {
		ListDataEvent e = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, oldSize, newSize - 1);
		m_listeners.forEach(l -> l.intervalAdded(e));

	    } else if (oldSize > newSize)
	    {
		ListDataEvent e = new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, oldSize - 1, newSize);
		m_listeners.forEach(l -> l.intervalRemoved(e));
	    }
	    int min = Math.min(oldSize, newSize);
	    ListDataEvent e = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, min, min);
	    m_listeners.forEach(l -> l.contentsChanged(e));
	}

    }
}
