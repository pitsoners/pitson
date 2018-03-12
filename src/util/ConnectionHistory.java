/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * classe qui stocke un historique de connexion. utilise le pattern singleton
 *
 * @author boulhol
 */
public class ConnectionHistory implements Serializable
{

    /**
     * l'instance unique de l'historique
     */
    private static ConnectionHistory s_instance;

    /**
     * ensemble des écouteurs qui sont notifiés lorsqu'une modification de
     * l'historique survient
     */
    private static HashSet<ChangeListener> s_listeners = new HashSet<>();

    /**
     * chemin d'accès au fichier de l'historique
     */
    private static final String HISTORY_FILENAME = "data/connectionHistory";

    /**
     * données de l'historique qui associe à une base de donnée un ensemble
     * d'utilisateurs qui s'y sont connectés.
     */
    private final HashMap<Database, Set<String>> m_history;

    /**
     * dernier utilisateur à s'être connecté
     */
    private String m_lastUser;
    /**
     * dernière base de données à laquelle un utilisateur s'est connecté
     */
    private Database m_lastDatabase;

    /**
     * constructeur par défaut. initialise une historique vide, un utilisateur
     * vide et une basse de donnée de nom et d'url vides.
     */
    private ConnectionHistory()
    {
	m_history = new HashMap<>();
	m_lastUser = "";
	m_lastDatabase = new Database("", "");
    }
    
    /**
     * retourne l'ensemble des utilisateurs qui se sont connectés à une base de données donnée
     * @param database la base dont obtenir l'ensemble des utilisateurs qui s'y sont connectés
     * @return l'ensemble des utilisateurs qui se sont connectés, ou null si aucun utilisateur ne s'est connecté à cette base
     */
    public Set<String> getUsers(Database database)
    {
	return m_history.get(database);
    }

    /**
     * ajoute une entrée à l'historique
     * @param database la base de données à laquelle s'est connecté l'utilisateur
     * @param user l'utilisateur qui s'est connecté à la base de données
     */
    public void add(Database database, String user)
    {
	Set<String> users = getUsers(database);
	if (users == null)
	{
	    users = new HashSet<>();
	    m_history.put(database, users);
	}
	if (users.add(user))
	{
	    fireConnectionHistoryChange();
	}
	m_lastDatabase = database;
	m_lastUser = user;
    }

    /**
     * retourne l'ensemble des bases de données référencées dans l'historique
     * @return l'ensemble des bases de données référencées dans l'historique
     */
    public Set<Database> getDatabases()
    {
	return m_history.keySet();
    }

    /**
     * retourne l'instance unique de l'historique de connexion
     * @return l'instance unique de l'historique de connexion
     */
    public static ConnectionHistory getInstance()
    {
	if (s_instance == null)
	{
	    synchronized (ConnectionHistory.class)
	    {
		if (s_instance == null)
		{
		    if (!loadHistory())
		    {
			s_instance = new ConnectionHistory();
		    }
		}
	    }
	}
	return s_instance;
    }

    /**
     * tente de charger l'historique depuis le fichier local
     * @return {@code true} en cas de réussite
     */
    private static boolean loadHistory()
    {
	boolean success;
	try
	{
	    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(HISTORY_FILENAME));
	    s_instance = (ConnectionHistory) ois.readObject();
	    ois.close();
	    success = true;
	} catch (IOException | ClassNotFoundException ex)
	{
	    Logger.getLogger(ConnectionHistory.class.getName()).log(Level.SEVERE, null, ex);
	    success = false;
	}
	return success;
    }

    /**
     * ajoute un écouteur qui sera notifié lorsqu'un changement sur l'historique survient
     * @param l l'écouteur à ajouter
     */
    public static void addChangeListener(ChangeListener l)
    {
	if (l != null)
	{
	    s_listeners.add(l);
	}
    }

    /**
     * retire un écouteur de modification d'historique
     * @param l l'écouteur à retirer
     */
    public static void removeChangeListener(ChangeListener l)
    {
	if (l != null)
	{
	    s_listeners.remove(l);
	}
    }

    /**
     * notifie les écouteurs d'historique qu'une modification est survenue
     */
    private static void fireConnectionHistoryChange()
    {
	ChangeEvent e = new ChangeEvent(s_instance);
	s_listeners.forEach(l -> l.stateChanged(e));
    }

    /**
     * sauvegarde dans le fichier l'historique.
     */
    public void save()
    {
	try
	{
	    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(HISTORY_FILENAME));
	    oos.writeObject(this);
	    oos.close();
	} catch (IOException ex)
	{
	    Logger.getLogger(ConnectionHistory.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    /**
     * retourne le nombre de bases de données référencées dans l'historique
     * @return 
     */
    public int getDatabaseCount()
    {
	return m_history.size();
    }

    /**
     * retourne la dernière base de données à laquelle un utilisateur s'est connecté
     * @return la dernière base de données à laquelle un utilisateur s'est connecté
     */
    public Database getLastDatabase()
    {
	return m_lastDatabase;
    }

    /**
     * retourne le dernier utilisateur qui s'est connecté
     * @return le dernier utilisateur qui s'est connecté
     */
    public String getLastUser()
    {
	return m_lastUser;
    }
}
