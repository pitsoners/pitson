/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thierry
 */
public final class DatabaseTools
{
    private DatabaseTools()
    {
	
    }
    
    private static final int DATABASE_VERSION = 1;
    
    
    private static final String SELECT_VERSION_QUERY = "SELECT numVersion FROM InfoBase";
    
    /**
     * retourne la version de la base de données actuellement paramétrée
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
    
    /**
     * vérifie que la base de donnée dont la connexion actuelle fait l'objet est valide
     * @return {@code true} si la base de donnée est valide.
     */
    public static boolean checkDatabase()
    {
	return getDatabaseVersion() == DATABASE_VERSION;
    }
    
    public static final String CALL_RETOURNER_ROLE = "{call dbo.sp_retournerRole()}";
    
    /**
     * retourne le role d'utilisateur associé à la connexion actuelle
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
}
