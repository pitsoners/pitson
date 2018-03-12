/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.DatabaseUser;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.ReturnDataBase;
import util.SQLConnection;

/**
 *
 * @author boulhol
 */
public final class ManagerDatabaseUser
{

    private ManagerDatabaseUser()
    {

    }

    private static final String SELECT_DATABASE_USERS = "SELECT * FROM utilisateursRoles";

    public static final ArrayList<DatabaseUser> getDatabaseUsers()
    {
	ArrayList<DatabaseUser> list = null;
	Connection conn = SQLConnection.getConnection();
	if (conn != null)
	{
	    try
	    {
		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery(SELECT_DATABASE_USERS);
		list = new ArrayList<>();
		while (rs.next())
		{
		    list.add(new DatabaseUser(rs.getString(1), rs.getString(2)));
		}
	    } catch (SQLException ex)
	    {
		Logger.getLogger(ManagerDatabaseUser.class.getName()).log(Level.SEVERE, null, ex);
		list = null;
	    }
	}
	return list;
    }

    
    private static final String CALL_ASSIGNER_ROLE = "{ ? = call sp_assignerEmploye (?, ?, ?) }";
    
    /**
     * Met à jout l'affactation de l'utilisateur spécifié. Il est impossible de
     * modifier le role du propriétaire de la base de donnée, ou de le
     * supprimer. Il est également impossible d'affecter un nouveau propriétaire
     * de base de donénes.
     *
     * @param dbu l'utilisateur de la base et son rôle a affecter. si le rôle
     * est {@code null}, alors l'utilisateur sera supprimé
     * @return un objet {@link util.ReturnDataBase} indiquant le déroulement de la procédure.
     */
    public static final ReturnDataBase updateDatabaseUser(DatabaseUser dbu)
    {
	return updateDatabaseUser(dbu.getUser(), dbu.getRole());
    }
    
    /**
     * Met à jout l'affactation de l'utilisateur spécifié. Il est impossible de
     * modifier le role du propriétaire de la base de donnée, ou de le
     * supprimer. Il est également impossible d'affecter un nouveau propriétaire
     * de base de donénes.
     *
     * @param user l'utilisateur de la base
     * @param role le rôle a affecter. si le rôle est {@code null}, alors l'utilisateur sera supprimé
     * @return un objet {@link util.ReturnDataBase} indiquant le déroulement de la procédure.
     */
    public static final ReturnDataBase updateDatabaseUser(String user, String role)
    {
	ReturnDataBase ret;
	Connection conn = SQLConnection.getConnection();
	if (conn != null)
	{
	    try
	    {
		CallableStatement cs = conn.prepareCall(CALL_ASSIGNER_ROLE);
		cs.registerOutParameter(1, Types.INTEGER);
		cs.registerOutParameter(4, Types.VARCHAR);
		cs.setString(2, user);
		cs.setString(3, role);
		cs.executeUpdate();
		ret = new ReturnDataBase(cs.getInt(1), cs.getString(4));
	    } catch (SQLException ex)
	    {
		ret = null;
		Logger.getLogger(ManagerDatabaseUser.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}
	else
	{
	    ret = null;
	}
	return ret;
    }

    /**
     * supprime l'utilisateur de la base de données. Cet utilisateur ne peut être le propriétaire de la base.
     * <br>Formellement, cette méthode appelle:
     * <p><code>updateDatabaseUser(user, null)</code>
     * @param user le nom de l'utilisateur à supprimer
     * @return un objet {@link util.ReturnDataBase} indiquant le déroulement de la procédure
     */
    public static final ReturnDataBase deleteDatabaseUser(String user)
    {
	return updateDatabaseUser(user, null);
    }
    
    /**
     * supprime l'utilisateur de la base de données. Cet utilisateur ne peut être le propriétaire de la base.
     * <br>Formellement, cette méthode appelle:
     * <p><code>updateDatabaseUser(dbu.getUser(), null)</code>
     * @param dbu l'utilisateur à supprimer
     * @return un objet {@link util.ReturnDataBase} indiquant le déroulement de la procédure
     */
    public static final ReturnDataBase deleteDatabaseUser(DatabaseUser dbu)
    {
	return updateDatabaseUser(dbu.getUser(), null);
    }
}
