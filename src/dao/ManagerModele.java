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
import java.util.ArrayList;
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
    public static boolean creerModele(Modele mo)
    {
        boolean ok = false ;
        try
        {
            Connection co = SQLConnection.getConnection() ;
            CallableStatement cs = co.prepareCall("{? = call sp_creerModele (?,?,?)}") ;
            cs.registerOutParameter(1, java.sql.Types.TINYINT);
            cs.registerOutParameter(4, java.sql.Types.VARCHAR);
            cs.setString(2, mo.getIdModele());
            cs.setDouble(3, mo.getDiametre());
            cs.executeUpdate();
            if (cs.getByte(1) != 0)
            {
                System.out.println("Erreur lors de la création du modèle : " + cs.getByte(1) + "\nMessage d'erreur : " + cs.getString(4));
            }
            else
            {
                ok = true ;
            }
        }
        catch (Exception e)
        {
            System.out.println(e) ;
        }
        return ok ;
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
            PreparedStatement pst = co.prepareStatement("SELECT * FROM MODELE WHERE idModele = '" + idModele + "'") ;
            boolean test = pst.execute();
            if (test)
            {
                ResultSet rs = pst.getResultSet() ;
                while (rs.next())
                {
                    retour = new Modele(rs.getString(1), rs.getDouble(2), rs.getBoolean(3)) ;
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
            PreparedStatement pst = co.prepareStatement("SELECT * FROM MODELE ORDER BY idModele") ;
            boolean test = pst.execute();
            if (test)
            {
                ResultSet rs = pst.getResultSet() ;
                while (rs.next())
                {
                    liste.add(new Modele(rs.getString(1), rs.getDouble(2), rs.getBoolean(3))) ;
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
    public static boolean renommerModele(Modele aRenommer, String newId)
    {
        boolean ok = false ;
        try
        {
            Connection co = SQLConnection.getConnection() ;
            CallableStatement cs = co.prepareCall("{? = call sp_renommerModele (?,?,?)}") ;
            cs.registerOutParameter(1, java.sql.Types.TINYINT);
            cs.registerOutParameter(4, java.sql.Types.VARCHAR);
            cs.setString(2, aRenommer.getIdModele());
            cs.setString(3, newId);
            cs.executeUpdate();
            if (cs.getByte(1) != 0)
            {
                System.out.println("Erreur lors de du renommage : " + cs.getByte(1) + "\nMessage d'erreur : " + cs.getString(4));
            }
            else
            {
                ok = true ;
                aRenommer.setIdModele(newId);
            }
        }
        catch (Exception e)
        {
            System.out.println(e) ;
        }
        return ok ;
    }
    
    /**
     * Méthode permettant de changer le statut d'obsolescence d'un modèle
     * @param mo est le modèle dont on veut changer le statut
     * @param obsolete est le nouveau statut à affecter au modèle
     * @return 
     */
    public static boolean updateObsoleteModele(Modele mo, boolean obsolete)
    {
        boolean ok = false ;
        try
        {
            Connection co = SQLConnection.getConnection() ;
            CallableStatement cs = co.prepareCall("{? = call sp_changerStatutModele (?,?,?)}") ;
            cs.registerOutParameter(1, java.sql.Types.TINYINT);
            cs.registerOutParameter(4, java.sql.Types.VARCHAR);
            cs.setString(2, mo.getIdModele());
            cs.setInt(3, (mo.isObsolete() ? 1 : 0));
            cs.executeUpdate();
            if (cs.getByte(1) != 0)
            {
                System.out.println("Erreur lors du renommage : " + cs.getByte(1) + "\nMessage d'erreur : " + cs.getString(4));
            }
            else
            {
                ok = true ;
                mo.setObsolete(obsolete);
            }
        }
        catch (Exception e)
        {
            System.out.println(e) ;
        }
        return ok ;
    }
    
    /**
     * Méthode permettant de supprimer un modèle
     * @param aSupprimer est le modèle à supprimer
     * @return 
     */
    public static boolean supprimerModele(Modele aSupprimer)
    {
        boolean ok = false ;
        try
        {
            Connection co = SQLConnection.getConnection() ;
            CallableStatement cs = co.prepareCall("{? = call sp_supprimerModele (?,?)}") ;
            cs.registerOutParameter(1, java.sql.Types.TINYINT);
            cs.registerOutParameter(3, java.sql.Types.VARCHAR);
            cs.setString(2, aSupprimer.getIdModele());
            cs.executeUpdate();
            if (cs.getByte(1) != 0)
            {
                System.out.println("Erreur lors de la suppression : " + cs.getByte(1) + "\nMessage d'erreur : " + cs.getString(4));
            }
            else
            {
                ok = true ;
            }
        }
        catch (Exception e)
        {
            System.out.println(e) ;
        }
        return ok ;
    }
    
}
