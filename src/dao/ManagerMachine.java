/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Machine;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import util.ReturnDataBase;
import util.SQLConnection;

/**
 *
 * @author preda
 */
public class ManagerMachine
{
    /**
     * méthode permettant d'inserer une machine dans la base de données
     * @param ma
     * @return 
     */
    public static ReturnDataBase creerMachine(Machine ma)
    {
        ReturnDataBase rdb = null ;
        try
        {
            Connection co = SQLConnection.getConnection();
            CallableStatement cs = co.prepareCall("{? = call sp_creerMachine(?,?,?)}");
            cs.registerOutParameter(1, java.sql.Types.INTEGER);
            cs.registerOutParameter(3, java.sql.Types.INTEGER);
            cs.registerOutParameter(4, java.sql.Types.VARCHAR);
            cs.setString(2, ma.getLibellePresse());
            cs.executeUpdate();
            rdb = new ReturnDataBase(cs.getByte(1), cs.getString(4));
            if(rdb.getCode() != 0)
            {
                System.out.println(rdb);
            }
            else
            {
                ma.setIdPresse(cs.getInt(3));
            }
        }
        catch(Exception e)
            {
                System.out.println(e);
            }
        return rdb;
        
    }

    /**
     * Méthode permettant de récupérer un machine dans la base de données à partir d'un id passé en paramètre
     * @param idMachine
     * @return 
     */
    public static Machine getMachineFromId(int idMachine)
    {
        Machine retour = null;
        try
        {
            Connection co = SQLConnection.getConnection();
            PreparedStatement pst = co.prepareStatement("SELECT *, (SELECT COUNT(idLot) FROM Lot l WHERE l.idPresse = Machine.idPresse) FROM Machine WHERE idPresse = " + idMachine);
            boolean test = pst.execute();
            if(test)
            {
                ResultSet rs = pst.getResultSet();
                while(rs.next())
                {
                    retour = new Machine(rs.getInt(1), rs.getString(2), rs.getBoolean(3), (rs.getInt(4)==0));
                }
            }
            else
            {
                System.out.println("Aucune machine correspondante à cet id.");
            }
        }
        catch(Exception e)
        {
              System.out.println(e);  
        }
        return retour;
        
    }
    
    /**
     * Méthode permettant de récupérer la liste de tous les machines existantes dans la base de données
     * @return 
     */
    public static ArrayList<Machine> getMachineList()
    {
        ArrayList<Machine> liste = new ArrayList();
        try
        {
            Connection co = SQLConnection.getConnection();
            PreparedStatement pst = co.prepareStatement("SELECT *, (SELECT COUNT(idLot) FROM Lot l WHERE l.idPresse = Machine.idPresse) FROM Machine ORDER BY idPresse");
            boolean test = pst.execute();
            if(test)
            {
                ResultSet rs = pst.getResultSet();
                while(rs.next())
                {
                    liste.add(new Machine(rs.getInt(1), rs.getString(2), rs.getBoolean(3), (rs.getInt(4)==0)));
                }
            }
            else
            {
                System.out.println("Aucune machine correspondante à cet id.");
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
   
        return liste;
    }

    /**
     * Méthode permettant de renommer une machine dans la base de données
     * @param aRenommer
     * @param newLibelle
     * @return 
     */
    public static ReturnDataBase renommerMachine(Machine aRenommer, String newLibelle)
    {
        ReturnDataBase rdb = null ;
        try
        {
            Connection co = SQLConnection.getConnection();
            CallableStatement cs = co.prepareCall("{? = call sp_renommerMachine(?,?,?)}");
            cs.registerOutParameter(1, java.sql.Types.INTEGER);
            cs.registerOutParameter(4, java.sql.Types.VARCHAR);
            cs.setInt(2, aRenommer.getIdPresse());
            cs.setString(3, newLibelle);
            cs.executeUpdate();
            rdb = new ReturnDataBase(cs.getByte(1), cs.getString(4));
            if(rdb.getCode() != 0)
            {
                System.out.println(rdb);
            }
            else
            {
                aRenommer.setLibellePresse(newLibelle);
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        
        return rdb;
    }

    /**
     * Méthode permettant de changer le statut d'activité d'une machine
     * @param ma
     * @param enService
     * @return 
     */
    public static ReturnDataBase updateEnServiceMachine(Machine ma, boolean enService)
    {
        ReturnDataBase rdb = null ;
        boolean ok = false;
        try
        {
            Connection co = SQLConnection.getConnection();
            CallableStatement cs = co.prepareCall("{? = call sp_changerStatutMachine(?,?,?)}");
            cs.registerOutParameter(1, java.sql.Types.TINYINT);
            cs.registerOutParameter(4, java.sql.Types.VARCHAR);
            cs.setInt(2, ma.getIdPresse());
            cs.setBoolean(3, enService);
            cs.executeUpdate();
            rdb = new ReturnDataBase(cs.getByte(1), cs.getString(4));
            if(rdb.getCode() != 0)
            {
                System.out.println(rdb);
            }
            else
            {
                ma.setEnService(enService);
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        
        return rdb;
    }
    
    public static ReturnDataBase supprimerMachine(Machine aSupprimer)
    {
        ReturnDataBase rdb = null ;
        try
        {
            Connection co = SQLConnection.getConnection();
            CallableStatement cs = co.prepareCall("{? = call sp_supprimerMachine(?,?)}");
            cs.registerOutParameter(1, java.sql.Types.INTEGER);
            cs.registerOutParameter(3, java.sql.Types.VARCHAR);
            cs.setInt(2, aSupprimer.getIdPresse());
            cs.executeUpdate();
            rdb = new ReturnDataBase(cs.getByte(1), cs.getString(3));
            if(rdb.getCode() != 0)
            {
                System.out.println(rdb);
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        
        return rdb;       
    }
}
