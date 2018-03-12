/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Lot;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.ReturnDataBase;
import util.SQLConnection;

/**
 *
 * @author preda
 */
public class ManagerLot
{
    /**
     * Cette méthode permet de lancer la production d'un lot en attente
     * @param lot
     * @return 
     */
    public static ReturnDataBase lancerLot(Lot lot)
    {
        ReturnDataBase retour = null;
        Connection connection;
        try
        {
            connection = SQLConnection.getConnection();
            CallableStatement cs = connection.prepareCall("{? = call sp_lancerLot (?, ?, ?,?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setString(2, lot.getIdModele());
            cs.setInt(3, lot.getNbrPieceDemande());
            cs.registerOutParameter(4, Types.INTEGER);
            cs.registerOutParameter(5, Types.VARCHAR);
            cs.execute();
            lot.setIdLot(cs.getInt(4));
            int code = cs.getInt(1);
            String message = cs.getString(5);

            retour = new ReturnDataBase(code, message);
        }
        catch (SQLException ex)
        {
            Logger.getLogger(ManagerLot.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retour;
    }
    
    /**
     * Méthode permettant le demarrage de la production d'un lot
     * @param lotEnProd
     * @return 
     */
    public static boolean demarrerProd(Lot lotEnProd)
    {
        boolean ok = false;
        try
        {
            Connection co = SQLConnection.getConnection();
            CallableStatement cs = co.prepareCall("{? = call sp_demarrerProd(?,?,?)}");
            cs.registerOutParameter(1, java.sql.Types.INTEGER);
            cs.registerOutParameter(4, java.sql.Types.VARCHAR);
            cs.setInt(2, lotEnProd.getIdLot());
            cs.setInt(3, lotEnProd.getIdPresse());
            cs.executeUpdate();
            if(cs.getByte(1) != 0)
            {
                System.out.println("Erreur lors du demarrage de la production :" + cs.getByte(1) + "\nMessage d'erreur : " + cs.getString(4));
            }
            else
            {
                ok = true;
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        
        return ok;
    }
    
    /**
     * Méthode permettant de terminer la production d'un lot
     * @param lotProdFini
     * @return 
     */
    public static boolean terminerProd(Lot lotProdFini)
    {
        boolean ok = false;
        try
        {
            Connection co = SQLConnection.getConnection();
            CallableStatement cs = co.prepareCall("{? = call sp_terminerProd(?,?)}");
            cs.registerOutParameter(1, java.sql.Types.INTEGER);
            cs.registerOutParameter(3, java.sql.Types.VARCHAR);
            cs.setInt(2, lotProdFini.getIdLot());
            cs.executeUpdate();
            if(cs.getByte(1) != 0)
            {
                System.out.println("Erreur, production pas terminé :"+ cs.getByte(1) + "\nMessage d'erreur : " + cs.getString(4));
            }
            else
            {
                ok = true;
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        
        return ok;
        
    }
    
    /**
     * Méthode permettant de récupérer la liste de tous les lots en attente de production existants dans la base de données
     * @return 
     */
    public static ArrayList<Lot> getLotEnAttenteList()
    {
        ArrayList<Lot> liste = new ArrayList();
        try
        {
            Connection co = SQLConnection.getConnection();
            PreparedStatement pst = co.prepareStatement("SELECT * FROM dbo.LotsAttenteProduction");
            boolean test = pst.execute();
            if(test)
            {
                ResultSet rs = pst.getResultSet();
                while(rs.next())
                {
                    liste.add(new Lot(rs.getInt(1), rs.getDate(2), rs.getString(3), rs.getInt(4)));
                }
            }
            else
            {
                System.out.println("Pas de lots en attente de production");
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        
        return liste;   
    }
    
    /**
     * Méthode permettant de récupérer la liste de tous les lots en attente de controle existants dans la base de données
     * @return 
     */
     public static ArrayList<Lot> getLotAttControlList()
    {
        ArrayList<Lot> liste = new ArrayList();
        try
        {
            Connection co = SQLConnection.getConnection();
            PreparedStatement pst = co.prepareStatement("SELECT * FROM dbo.LotsAControler");
            boolean test = pst.execute();
            if(test)
            {
                ResultSet rs = pst.getResultSet();
                while(rs.next())
                {
                   Lot l = new Lot();
                    l.setIdLot(rs.getInt(1));
                    l.setIdModele(rs.getString(2));
                    l.setEtatControle(rs.getString(3));
                    l.setNbrPieceDemande(rs.getInt(4));
                    l.setPiecesControlees(rs.getInt(5));
                    liste.add(l);
                }
            }
            else
            {
                System.out.println("pas de lot en attente de controle");
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        
        return liste;
    }
    
    /**
     * Méthode permettant de récupérer la liste de tous les lots en cours de production existants dans la base de données
     * @return 
     */
    public static ArrayList<Lot> getLotEnCoursList()
    {
        ArrayList<Lot> liste = new ArrayList();
        try
        {
            Connection co = SQLConnection.getConnection();
            PreparedStatement pst = co.prepareStatement("SELECT * FROM dbo.LotsEnCoursProduction");
            boolean test = pst.execute();
            if(test)
            {
                ResultSet rs = pst.getResultSet();
                while(rs.next())
                {
                    Lot l = new Lot();
                    l.setIdLot(rs.getInt(1));
                    l.setDateDemande(rs.getDate(2));
                    l.setIdModele(rs.getString(3));
                    l.setDateProduction(rs.getDate(4));
                    l.setIdPresse(rs.getInt(5));
                    liste.add(l);
                }
            }
            else
            {
               System.out.println("Pas de lots en cours de production"); 
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        
        return liste;  
    }
    
    public static boolean annulerLot(Lot annulerlot)
    {
        boolean ok = false;
        try
        {
            Connection co = SQLConnection.getConnection();
            CallableStatement cs = co.prepareCall("{? = call sp_annulerLot(?,?)}");
            cs.registerOutParameter(1, java.sql.Types.INTEGER);
            cs.registerOutParameter(3, java.sql.Types.VARCHAR);
            cs.setInt(2, annulerlot.getIdLot());
            cs.executeUpdate();
            if(cs.getByte(1) != 0)
            {
                System.out.println("Erreur d'annulation du lot : " + cs.getByte(1) + "\nMessage d'erreur : " + cs.getString(4));
            }
            else
            {
                ok = true;
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        
        return ok;
    }

        /**
     * Cette méthode permet de récupérer les information d'un lot à partir d'un
     * ID
     *
     * @param id est l'id du Lot dont on veut les informations
     * @return retourne le Lot de l'id donné
     */
    public static Lot getLotFromId(int id)
    {
        Lot lot = null;
        try
        {
            Connection connection = SQLConnection.getConnection();
            CallableStatement cs = connection.prepareCall("{SELECT * FROM Lot WHERE idLot = ?}");
            cs.setInt(1, id);

            lot.setIdLot(cs.getInt(1));
            lot.setDateDemande(cs.getDate(2));
            lot.setDateProduction(cs.getDate(3));
            lot.setEtatProduction(cs.getString(4));
            lot.setEtatControle(cs.getString(5));
            lot.setIdModele(cs.getString(6));
            lot.setIdPresse(cs.getInt(7));
            lot.setNbrPieceDemande(cs.getInt(24));
        }
        catch (SQLException ex)
        {
            Logger.getLogger(ManagerLot.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lot;
    }
    
     /**
     * Méthode permettant le demarrage du controle d'un lot
     * @param lotControle
     * @return 
     */
    public static boolean demarrerControleLot(Lot lotControle)
    {
        boolean ok = false;
        try
        {
            Connection co = SQLConnection.getConnection();
            CallableStatement cs = co.prepareCall("{? = call sp_demarrerControle(?,?)}");
            cs.registerOutParameter(1, java.sql.Types.INTEGER);
            cs.registerOutParameter(3, java.sql.Types.VARCHAR);
            cs.setInt(2, lotControle.getIdLot());
            cs.executeUpdate();
            if(cs.getByte(1) != 0)
            {
                System.out.println("Erreur lors du demarrage du controle :" + cs.getByte(1) + "\nMessage d'erreur : " + cs.getString(3));
            }
            else
            {
                lotControle.setEtatControle("EnCours");
                ok = true;
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        
        return ok;
        
    }
}
