/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Ensemble d'outils pour manipuler la base de données
 *
 * @author Thierry
 */
public final class DatabaseTools
{

    public static final String[] ROLES =
    {
	"db_owner", "ResponsableApplication", "ResponsableAtelier", "ResponsablePresse", "Controleur", "Magasinier", "ResponsableQualite"
    };

    /**
     * détermine si la chaine donnée représente un nom de role de l'application.
     * @param role la chaine à tester
     * @return {@code true} si la chaine représente un role de l'application.
     */
    public static boolean isRole(String role)
    {
	boolean valide = role != null;
	if (valide)
	{
	    valide = false;
	    int i = 0;
	    while (i < ROLES.length && ! valide)
	    {
		valide = ROLES[i].equals(role);
	    }
	}
	return valide;
    }

    /**
     * constructeur privé pour empêcher l'instanciation de cette classe
     */
    private DatabaseTools()
    {

    }

    /**
     * version de la base sur laquelle cette application travaille
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * requete pour récupérer le numéro de version de la base de données
     */
    private static final String SELECT_VERSION_QUERY = "SELECT numVersion FROM InfoBase";

    /**
     * retourne la version de la base de données actuellement paramétrée
     *
     * @return le numéro de version de la base, ou -1 en cas d'impossibilité de se connecter
     */
    private static int getDatabaseVersion()
    {
	int version = -1;
	Connection conn = SQLConnection.getConnection();
	if (conn != null)
	{
	    try
	    {
		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery(SELECT_VERSION_QUERY);
		if (rs.next())
		{
		    version = rs.getInt(1);
		}
		rs.close();
		s.close();
	    } catch (SQLException ex)
	    {
		Logger.getLogger(DatabaseTools.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}
	return version;
    }
    
    
    private static final String SELECT_NOMBRE_PIECES_PAR_CAISSE = "SELECT nombreDePiecesParCaisse FROM InfoBase WHERE id = 1";
    /**
     * retourne le nombde de pièces par caisse
     * @return le nombde de pièces par caisse
     */
    public static int getNombrePiecesParCaisse()
    {
	int nombre = -1;
	Connection conn = SQLConnection.getConnection();
	if (conn != null)
	{
	    try
	    {
		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery(SELECT_NOMBRE_PIECES_PAR_CAISSE);
		if (rs.next())
		{
		    nombre = rs.getInt(1);
		}
	    } catch (SQLException ex)
	    {
		Logger.getLogger(DatabaseTools.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}
	return nombre;
    }

    /**
     * vérifie que la base de donnée dont la connexion actuelle fait l'objet est valide
     *
     * @return {@code true} si la base de donnée est valide.
     */
    public static boolean checkDatabase()
    {
	return getDatabaseVersion() == DATABASE_VERSION;
    }

    /**
     * requete pour appeler la procédure qui donne le role de l'utilisateur
     */
    public static final String CALL_RETOURNER_ROLE = "{call sp_retournerRole()}";

    /**
     * retourne le role d'utilisateur associé à la connexion actuelle
     *
     * @return le nmo du rôle de l'utilisateur connecté
     */
    public static String getRole()
    {
	String role = null;
	Connection conn = SQLConnection.getConnection();
	if (conn != null)
	{
	    try
	    {
		CallableStatement cs = conn.prepareCall(CALL_RETOURNER_ROLE);
		ResultSet rs = cs.executeQuery();
		if (rs.next())
		{
		    role = rs.getString(1);
		}
		rs.close();
		cs.close();
	    } catch (SQLException ex)
	    {
		Logger.getLogger(DatabaseTools.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}
	return role;
    }

    /**
     * requete qui vérifie l'existence d'une base de données
     */
    private static final String SELECT_DATABASE_NAME = "SELECT name FROM sys.databases WHERE name = ?";

    /**
     * détermine si la base de données dont le nom est donné en paramètre existe sur la connexion actuelle
     *
     * @param conn la connexion où vérifier l'existence de la base de données
     * @param name le nom de la base de données dont vérifier l'existence
     * @return {@code true} si la base de données existe pour la connexion donnée
     */
    public static boolean databaseExists(Connection conn, String name)
    {
	boolean exists = false;
	if (conn != null)
	{
	    try
	    {
		PreparedStatement ps = conn.prepareStatement(SELECT_DATABASE_NAME);
		ps.setString(1, name);
		ResultSet rs = ps.executeQuery();
		exists = rs.next();
		rs.close();
		ps.close();
	    } catch (SQLException ex)
	    {
		Logger.getLogger(DatabaseTools.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}
	return exists;
    }

    /**
     * retourne un chaine de caractères représentant un lot d'instructions SQL séparées par le mot clé 'GO',extraite depuis la
     * source BufferedReader donnée
     *
     * @param br la source d'où extraire le lot de requetes.
     * @return le lot de requetes, ou null si une erreur survient ou si la source est vide.
     */
    private static String extractBatch(BufferedReader br)
    {
	StringBuffer batch = new StringBuffer();
	boolean over;
	String line;
	do
	{
	    line = null;
	    try
	    {
		line = br.readLine();
	    } catch (IOException ex)
	    {
	    }
	    over = line == null;
	    if (!over)
	    {
		line = line.trim();
		if (line.length() > 0)
		{
		    over = line.equalsIgnoreCase("GO");
		    if (!over)
		    {
			batch.append(line);
			batch.append('\n');
		    }
		}
	    }
	} while (!over);
	return batch.length() > 0 ? batch.toString() : null;
    }

    /**
     * execute l'ensemble des instructions sql contenues dans le fichier de script donné. Ces instructions ne doivent pas
     * retourner des résultats.
     *
     * @param conn la connexion sur laquelle exécuter le script
     * @param filename le nom du fichier
     * @return {@code true} si l'exécution du script s'est déroulée avec succès
     */
    private static boolean runScript(Connection conn, String filename)
    {
	boolean success;
	try
	{
	    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
	    String batch = extractBatch(br);
	    boolean oldAutoCommit = conn.getAutoCommit();
	    conn.setAutoCommit(false);
	    Statement s = conn.createStatement();
	    while (batch != null)
	    {
		try
		{
		    s.executeUpdate(batch);
		} catch (SQLException e)
		{
		    System.out.println(e.getMessage());
		    System.out.println("SQL : " + batch);
		}
		batch = extractBatch(br);
	    }
	    conn.commit();
	    s.close();
	    conn.setAutoCommit(oldAutoCommit);
	    success = true;
	} catch (FileNotFoundException ex)
	{
	    Logger.getLogger(DatabaseTools.class.getName()).log(Level.SEVERE, null, ex);
	    success = false;
	} catch (SQLException ex)
	{
	    Logger.getLogger(DatabaseTools.class.getName()).log(Level.SEVERE, null, ex);
	    success = false;
	    try
	    {
		conn.rollback();
	    } catch (SQLException ex1)
	    {
		Logger.getLogger(DatabaseTools.class.getName()).log(Level.SEVERE, null, ex1);
	    }
	}
	return success;
    }

    /**
     * installe la base de données sur le serveur indiqué
     *
     * @param url url du serveur sur lequel installer la base de données
     * @param name le nom de la base de données à installer
     * @param user le nom d'utilisateur qui se connectera à la base de données et deviendra le propriétaire de la base de données
     * @param password le mot de passe de l'utilisateur
     * @return {@code true} si la base de données a correctement installée
     */
    public static boolean installDatabase(String url, String name, String user, String password)
    {
	boolean success;
	try
	{
	    Connection conn = SQLConnection.getMasterConnection(url, user, password);
	    Statement s = conn.createStatement();
	    name = name.split(";")[0];
	    s.executeUpdate("CREATE DATABASE " + name);
	    s.executeUpdate("USE " + name);
	    success = runScript(conn, "data/Create Database.sql");
	    if (!success)
	    {
		s.executeQuery("USE master");
		s.executeQuery("DROP DATABASE " + name);
	    }
	    conn.close();
	} catch (SQLException ex)
	{
	    Logger.getLogger(DatabaseTools.class.getName()).log(Level.SEVERE, null, ex);
	    success = false;
	}
	return success;
    }
    
    
}
