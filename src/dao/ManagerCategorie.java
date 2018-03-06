/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Categorie;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.ReturnDataBase;
import util.SQLConnection;

/**
 *
 * @author brun
 */
public class ManagerCategorie
{
    private static Object util;

    /**
     * Cette méthode permet de récupérer une catégorie
     *
     * @param cat la catégorie que l'on veut récupérer
     * @return retourne la catégorie que l'on veut récupérer
     */
    public static Categorie getCategorie(String cat)
    {
        List<Categorie> liste = ManagerCategorie.getCategories();
        Categorie result = null;
        for (Categorie c : liste)
        {
            if (c.getNomCategorie().equals(cat))
            {
                result = c;
                break;
            }
        }
        return result;
    }

    /**
     * Cette méthode permet de récupérer les catégories et leurs max/min
     *
     * @return une arraylist des catégories
     */
    public static ArrayList<Categorie> getCategories()
    {
        ArrayList<Categorie> listeCategorie = new ArrayList<>(); // ArrayList dans laquelle on va ranger nos catégories
        Connection connection;
        try
        {
            connection = SQLConnection.getConnection();
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM Categorie");
            while (rs.next())
            {
                String nomCategorie = rs.getString(1); // Récupération du nom de la catégorie
                if (!rs.getString(1).equals("Rebut"))
                {
                    double minCategorie = rs.getDouble(2); // Récupération du min de la catégorie 
                    double maxCategorie = rs.getDouble(3); // Récupération du max de la catégorie
                    listeCategorie.add(new Categorie(nomCategorie, minCategorie, maxCategorie));
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(ManagerCategorie.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listeCategorie;
    }

    /**
     * Cette méthode permet d'initialiser les catégories
     *
     * @param catPetit est la catégorie petit
     * @param catMoyen est la catégorie moyen
     * @param catGrand est la catégorie grand
     * @return retourne vrai si les catégories on bien été initialisées
     */
    public static ReturnDataBase initialiseCategorie(Categorie catPetit, Categorie catMoyen, Categorie catGrand)
    {
        ReturnDataBase retour = null;
        Connection connection; // Connection sur laquelle on travaille
        try
        {
            connection = SQLConnection.getConnection();
            CallableStatement cs = connection.prepareCall("{? = call sp_modifierBornesCategories (?, ?, ?, ?, ?, ?, ?)}");
            cs.registerOutParameter(1, Types.TINYINT);
            cs.setDouble(2, catPetit.getMinCategorie());
            cs.setDouble(3, catPetit.getMaxCategorie());
            cs.setDouble(4, catMoyen.getMinCategorie());
            cs.setDouble(5, catMoyen.getMaxCategorie());
            cs.setDouble(6, catGrand.getMinCategorie());
            cs.setDouble(7, catGrand.getMaxCategorie());
            cs.registerOutParameter(8, Types.VARCHAR);

            cs.executeUpdate();

            int code = cs.getInt(1);
            String message = cs.getString(8);
            
            retour = new ReturnDataBase(code, message);
        }
        catch (Exception ex)
        {
            Logger.getLogger(ManagerCategorie.class.getName()).log(Level.SEVERE, null, ex);

        }
        return retour;
    }
    
}