package dao;

import entity.Lot;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import util.SQLConnection;

/**
 *
 * @author preda
 */
public class ManagerLot
{
    /**
     * Méthode permettant le lancement d'un lot
     * @param lot
     * @return 
     */
    public static boolean lancerLot(Lot lot)
    {
        boolean ok = false;
        try
        {
            Connection co = SQLConnection.getConnection();
            CallableStatement cs = co.prepareCall("{? = call sp_lancerLot(?,?,?)}");
            cs.registerOutParameter(1, java.sql.Types.INTEGER);
            cs.registerOutParameter(4, java.sql.Types.VARCHAR);
            cs.setString(2, lot.getIdModele());
            cs.setInt(3, lot.getNbrPieceDemande());
            cs.executeUpdate();
            if(cs.getByte(1) != 0)
            {
                System.out.println("Erreur lors du lancement du lot :" + cs.getByte(1) + "\nMessage d'erreur : " + cs.getString(4));
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
                    liste.add(new Lot(rs.getInt(1), rs.getDate(2), rs.getString(3), rs.getDate(4), rs.getInt(5)));
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
}
