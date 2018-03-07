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
}
