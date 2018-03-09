/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Stock;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.SQLConnection;

/**
 *
 * @author brun
 */
public class ManagerStock
{

    /**
     * Cette méthode permet de récupérer tout les stocks
     *
     * @return retourne une arraylist de Stock
     */
    public static ArrayList<Stock> getStocks()
    {
        ArrayList<Stock> listeStock = new ArrayList<>();
        Connection connection;
        try
        {
            connection = SQLConnection.getConnection();
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM Stock ORDER BY IdModele");
            Stock st;
            if (rs.next())
            {
                st = new Stock(rs.getString(1));
                st.setSeuilEtQuantite(rs.getString(2), rs.getInt(3), rs.getInt(4));
                while (rs.next())
                {
                    String modele = rs.getString(1);
                    if (!st.getModele().equals(modele))
                    {
                        listeStock.add(st);
                        st = new Stock(modele);
                    }
                    st.setSeuilEtQuantite(rs.getString(2), rs.getInt(3), rs.getInt(4));
                }
                listeStock.add(st);
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(ManagerStock.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listeStock;
    }

    /**
     * Cette méthode permet de récupérer le nom des colonnes de la table Stock
     *
     * @return retourne les noms de colonne de la table Stock
     */
    public static ArrayList<String> getTitreStock()
    {
        ArrayList<String> listeTitre = new ArrayList<>();
        Connection connection;
        try
        {
            connection = SQLConnection.getConnection();
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM Stock");
            ResultSetMetaData rsmd = rs.getMetaData();
            int i;
            for (i = 1; i <= rsmd.getColumnCount(); i++)
            {
                listeTitre.add(rsmd.getColumnName(i));
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(ManagerStock.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listeTitre;
    }
    
        /**
     * Cette méthode permet d'ajouter des caisses en stock pour un modele et une
     * catégorie donnée
     *
     * @param model est le model des pieces dans les caisses
     * @param categorie est la catégorie des pieces dans les caisses
     * @param quantite est la quantité de caisses
     * @return
     */
    public static ReturnDataBase addStock(String model, String categorie, String quantite)
    {
        //Exception s = new Exception();
        //s.printStackTrace();

        ReturnDataBase retour = null;
        int q = 0;
        Connection connection;
        try
        {
            connection = SQLConnection.getConnection();

            if (Tools.estInt(quantite))
            {
                q = Integer.parseInt(quantite);
            }

            CallableStatement cs = connection.prepareCall("{? = call sp_entreeStock (?, ?, ?, ?)}");
            cs.registerOutParameter(1, Types.TINYINT);
            cs.setString(2, model);
            cs.setString(3, categorie);
            cs.setInt(4, q);
            cs.registerOutParameter(5, Types.VARCHAR);

            cs.executeUpdate();

            int code = cs.getInt(1);
            String message = cs.getString(5);

            retour = new ReturnDataBase(code, message);
        }
        catch (SQLException ex)
        {
            Logger.getLogger(ManagerStock.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retour;
    }

    public static ReturnDataBase deStock(String model, String categorie, String quantite)
    {
        ReturnDataBase retour = null;
        int q = 0;
        Connection connection;
        try
        {
            connection = SQLConnection.getConnection();

            if (Tools.estInt(quantite))
            {
                q = Integer.parseInt(quantite);
            }

            CallableStatement cs = connection.prepareCall("{? = call sp_sortieStock (?, ?, ?, ?)}");
            cs.registerOutParameter(1, Types.TINYINT);
            cs.setString(2, model);
            cs.setString(3, categorie);
            cs.setInt(4, q);
            cs.registerOutParameter(5, Types.VARCHAR);

            cs.executeUpdate();

            int code = cs.getInt(1);
            String message = cs.getString(5);

            retour = new ReturnDataBase(code, message);
        }
        catch (SQLException ex)
        {
            Logger.getLogger(ManagerStock.class.getName()).log(Level.SEVERE, null, ex);
        }

        return retour;
    }
    
        /**
     * Cette méthode permet de récupérer la quantité de caisse en stock pour un
     * modele donné
     *
     * @param id est l'id du modele dont on veut le stock
     * @return retourne la quantité de caisses en stock
     */
    public static int getQuantiteEnStock(String id)
    {
        int total = 0;
        Connection connection;
        try
        {
            connection = SQLConnection.getConnection();
            CallableStatement cs = connection.prepareCall("SELECT qtStock FROM Stock WHERE idModele = ?");
            cs.setString(1, id);
            if (cs.execute())
            {
                ResultSet rs = cs.executeQuery();
                while (rs.next())
                {
                    total = total + rs.getInt(1);
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(ManagerStock.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total;
    }
}
