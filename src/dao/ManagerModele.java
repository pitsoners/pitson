/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;


import entity.Modele;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.ReturnDataBase;
import util.SQLConnection;

/**
 *
 * @author denis
 */
public class ManagerModele {
    
    /**
     * Méthode permettant d'insérer un modèle dans la base de données
     * @param mo
     * @return 
     */
    public static ReturnDataBase creerModele(Modele mo)
    {
        ReturnDataBase rdb = null ;
        try
        {
            Connection co = SQLConnection.getConnection() ;
            CallableStatement cs = co.prepareCall("{? = call sp_creerModele (?,?,?)}") ;
            cs.registerOutParameter(1, java.sql.Types.TINYINT);
            cs.registerOutParameter(4, java.sql.Types.VARCHAR);
            cs.setString(2, mo.getIdModele());
            cs.setDouble(3, mo.getDiametre());
            cs.executeUpdate();
            rdb = new ReturnDataBase(cs.getByte(1), cs.getString(4));
            if (rdb.getCode()!= 0)
            {
                System.out.println(rdb);
            }
        }
        catch (Exception e)
        {
            System.out.println(e) ;
        }
        return rdb ;
    }
    
    /**
     * Méthode permettant de récupérer un modèle dans la base de données à partir d'un id passé en paramètre
     * @param idModele
     * @return 
     */
    public static Modele getModeleFromId(String idModele)
    {
        Modele retour = null ;
        try
        {            
            Connection co = SQLConnection.getConnection() ;
            PreparedStatement pst = co.prepareStatement("SELECT *, (SELECT COUNT(idLot) FROM Lot l WHERE l.idModele = Modele.idModele), (SELECT SUM(qtStock) FROM Stock s WHERE s.idModele = Modele.idModele) FROM Modele WHERE idModele = '" + idModele + "'") ;
            boolean test = pst.execute();
            if (test)
            {
                ResultSet rs = pst.getResultSet() ;
                while (rs.next())
                {
                    retour = new Modele(rs.getString(1), rs.getDouble(2), rs.getBoolean(3), (rs.getInt(4) == 0 && rs.getInt(5) == 0)) ;
                }
            }
            else
            {
                System.out.println("Aucun modèle correspondant à cet id.") ;
            }
        }
        catch (Exception e)
        {
            System.out.println(e) ;
        }
        return retour ;
    }
    
    /**
     * Méthode permettant de récupérer la liste de tous les modèles existant dans la base de données
     * @return 
     */
    public static ArrayList<Modele> getModeleList()
    {
        ArrayList<Modele> liste = new ArrayList() ;
        try
        {            
            Connection co = SQLConnection.getConnection() ;
            PreparedStatement pst = co.prepareStatement("SELECT *, (SELECT COUNT(idLot) FROM Lot l WHERE l.idModele = Modele.idModele), (SELECT SUM(qtStock) FROM Stock s WHERE s.idModele = Modele.idModele) FROM Modele ORDER BY idModele") ;
            boolean test = pst.execute();
            if (test)
            {
                ResultSet rs = pst.getResultSet() ;
                while (rs.next())
                {
                    liste.add(new Modele(rs.getString(1), rs.getDouble(2), rs.getBoolean(3), (rs.getInt(4) == 0 && rs.getInt(5) == 0))) ;
                }
            }
            else
            {
                System.out.println("Aucun modèle correspondant à cet id.") ;
            }
        }
        catch (Exception e)
        {
            System.out.println(e) ;
        }
        return liste ;
    }
    
    /**
     * Méthode permettant de renommer un modèle dans la base de données
     * @param aRenommer est le modèle à renommer
     * @param newId est le nouveau nom à affecter au modèle
     * @return 
     */
    public static ReturnDataBase renommerModele(Modele aRenommer, String newId)
    {
        ReturnDataBase rdb = null ;
        try
        {
            Connection co = SQLConnection.getConnection() ;
            CallableStatement cs = co.prepareCall("{? = call sp_renommerModele (?,?,?)}") ;
            cs.registerOutParameter(1, java.sql.Types.TINYINT);
            cs.registerOutParameter(4, java.sql.Types.VARCHAR);
            cs.setString(2, aRenommer.getIdModele());
            cs.setString(3, newId);
            cs.executeUpdate();
            rdb = new ReturnDataBase(cs.getByte(1), cs.getString(4));
            if (rdb.getCode()!= 0)
            {
                System.out.println(rdb);
            }
            else
            {
                aRenommer.setIdModele(newId);
            }
        }
        catch (Exception e)
        {
            System.out.println(e) ;
        }
        return rdb ;
    }
    
    /**
     * Méthode permettant de changer le statut d'obsolescence d'un modèle
     * @param mo est le modèle dont on veut changer le statut
     * @param obsolete est le nouveau statut à affecter au modèle
     * @return 
     */
    public static ReturnDataBase updateObsoleteModele(Modele mo, boolean obsolete)
    {
        ReturnDataBase rdb = null ;
        try
        {
            Connection co = SQLConnection.getConnection() ;
            CallableStatement cs = co.prepareCall("{? = call sp_changerStatutModele (?,?,?)}") ;
            cs.registerOutParameter(1, java.sql.Types.TINYINT);
            cs.registerOutParameter(4, java.sql.Types.VARCHAR);
            cs.setString(2, mo.getIdModele());
            cs.setInt(3, (obsolete ? 1 : 0));
            cs.executeUpdate();
            rdb = new ReturnDataBase(cs.getByte(1), cs.getString(4));
            if (rdb.getCode()!= 0)
            {
                System.out.println(rdb);
            }
            else
            {
                mo.setObsolete(obsolete);
            }
        }
        catch (Exception e)
        {
            System.out.println(e) ;
        }
        return rdb ;
    }
    
    /**
     * Méthode permettant de supprimer un modèle
     * @param aSupprimer est le modèle à supprimer
     * @return 
     */
    public static ReturnDataBase supprimerModele(Modele aSupprimer)
    {
        ReturnDataBase rdb = null ;
        try
        {
            Connection co = SQLConnection.getConnection() ;
            CallableStatement cs = co.prepareCall("{? = call sp_supprimerModele (?,?)}") ;
            cs.registerOutParameter(1, java.sql.Types.TINYINT);
            cs.registerOutParameter(3, java.sql.Types.VARCHAR);
            cs.setString(2, aSupprimer.getIdModele());
            cs.executeUpdate();
            rdb = new ReturnDataBase(cs.getByte(1), cs.getString(3));
            if (rdb.getCode()!= 0)
            {
                System.out.println(rdb);
            }
        }
        catch (Exception e)
        {
            System.out.println(e) ;
        }
        return rdb ;
    }
    
        /**
     * Cette méthode permet de récupérer l'id de tous les modèles qui ne sont
     * pas obsolète ou qui sont obsolètes mais ont toujours des caisses en stock
     *
     * @return retourne une liste de tous les modèles qui ne sont pas obsolètes
     */
    public static ArrayList<String> getListeIdModele()
    {
        ArrayList<String> listeIdModele = new ArrayList<>();
        Connection connection;
        try
        {
            connection = SQLConnection.getConnection();
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("SELECT Modele.idModele\n"
                    + "FROM Modele\n"
                    + "Join Stock ON Modele.idModele = Stock.idModele\n"
                    + "WHERE Modele.obsolete = 0\n"
                    + "OR (Modele.obsolete = 1 \n"
                    + "AND Stock.qtStock > 0)\n"
                    + "GROUP BY Modele.idModele");
            while (rs.next())
            {
                listeIdModele.add(rs.getString(1));
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(ManagerStock.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listeIdModele;
    }
}
