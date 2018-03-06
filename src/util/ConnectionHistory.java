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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author boulhol
 */
public class ConnectionHistory implements Serializable
{

    private static ConnectionHistory s_instance;

    private static final String HISTORY_FILENAME = "data/connectionHistory";

    private final HashMap<Database, Set<String>> m_history;

    private ConnectionHistory()
    {
	m_history = new HashMap<>();
    }

    public Set<String> getUsers(Database database)
    {
	return m_history.get(database);
    }

    public void add(Database database, String user)
    {
	Set<String> users = getUsers(database);
	if (users == null)
	{
	    users = new HashSet<>();
	    m_history.put(database, users);
	}
	users.add(user);
    }

    public Set<Database> getDatabases()
    {
	return m_history.keySet();
    }

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
			s_instance.add(new Database("jdbc:sqlserver://serveur-sql2017", "Pitson"), "boulhol");
		    }
		}
	    }
	}
	return s_instance;
    }

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
    
    public void save()     {
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

    public int getDatabaseCount()
    {
	return m_history.size();
    }

    public String getLastUser()
    {
	return null; // TODO!
    }

    public Database getLastDatabase()
    {
	return null;
    }
}
