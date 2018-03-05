package util;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.sql.Connection;
import java.sql.SQLException;

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
	    s_connection = null;
	}
	return s_connection;
    }
    
    /**
     * crée l'instance de connection avec les paramètres de connexion actuelle.
     * @throws SQLServerException  si une erreur de connexion survient.
     */
    private static void createConnection() throws SQLServerException
    {
	s_connection = DATA_SOURCE.getConnection();
    }
}
