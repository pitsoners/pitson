/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;

/**
 * Classe de connexion à la base de donnée
 *
 * @author boulhol
 */
public final class SQLConnection
{
    /**
     * Constructeur privé pour empêcher l'instanciation de la classe
     */
    private SQLConnection()
    {
    }

    /**
     * contient les informations courantes de connexion
     */
    private static final SQLServerDataSource DATA_SOURCE = new SQLServerDataSource();

    /**
     * instance unique de la connexion
     */
    private static Connection s_connection;
   

    /**
     * Modifie les paramètres de la connexion (et ferme la connexion en cours si elle est ouverte)
     *
     * @param url URL du serveur de la base de donnée
     * @param db_name nom de la base de donnée
     * @param user login de l'utilisateur de base de donnée
     * @param password mot de passe de l'utilisateur
     */
    public static void setup(String url, String db_name, String user, String password)
    {
	try
	{
	    if (s_connection != null && !s_connection.isClosed())
	    {
		s_connection.close();
	    }
	}
	catch (SQLException e)
	{
	    
	}
	
	DATA_SOURCE.setURL(url);
	DATA_SOURCE.setDatabaseName(db_name);
	DATA_SOURCE.setUser(user);
	DATA_SOURCE.setPassword(password);
    }
    
    /**
     * retourne le nom de l'utilisateur actuellement connecté
     * @return le nom de l'utilisateur actuellement connecté, ou {@code null} si la connexion est invalide
     */
    public static String getUser()
    {
	return getConnection() == null ? null : DATA_SOURCE.getUser();
    }
    
    /**
     * retourne la base de données actuellement paramétrée, si la connexion est valide
     * @return la base de données connectée, ou {@code null} si la connexion est invalide
     */
    public static Database getDatabase()
    {
	return getConnection() == null ? null : new Database(DATA_SOURCE.getURL(), DATA_SOURCE.getDatabaseName());
    }

    /**
     * retourne l'instance de la connexion à la base de donnée avec les paramètres courants.
     * @return l'instance de la connexion, ou {@code null} si une erreur de connexion est survenue.
     */
    public static Connection getConnection()
    {
	try
	{
	    if (s_connection == null || s_connection.isClosed())
	    {
		synchronized (SQLConnection.class)
		{
		    if (s_connection == null || s_connection.isClosed())
		    {
			createConnection();
		    }
		}
	    }
	} catch (SQLException e)
	{
	    Logger.getLogger(SQLConnection.class.getName()).log(Level.SEVERE, null, e);
	    s_connection = null;
	}
	return s_connection;
    }
    
    /**
     * crée l'instance de connection avec les paramètres de connexion actuelle.
     * @throws SQLServerException  si une erreur de connexion survient.
     */
    private static void createConnection()
    {
	try
	{
	    s_connection = DATA_SOURCE.getConnection();
	}
	catch (SQLServerException e)
	{
	    s_connection = null;
	}
    }
    
    /**
     * retourne une connection à la base de données 'master' sur le serveur indiqué. Cette connection n'est pas une instance unique et doit être fermée lorsqu'elle n'est plus utilisée.
     * @param url l'url du serveur de base de données
     * @param user le nom d'utilisateur pour la connexion
     * @param password le mot de passede l'utilisateur
     * @return la connexion à la base de données, ou {@code null} si elle a échoué
     */
    public static Connection getMasterConnection(String url, String user, String password)
    {
	SQLServerDataSource dataSource = new SQLServerDataSource();
	dataSource.setURL(url);
	dataSource.setUser(user);
	dataSource.setDatabaseName("master");
	dataSource.setPassword(password);
	Connection conn = null;
	try
	{
	    conn = dataSource.getConnection();
	} catch (SQLServerException ex)
	{
	    Logger.getLogger(SQLConnection.class.getName()).log(Level.SEVERE, null, ex);
	}
	return conn;
    }
}
