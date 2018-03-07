/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import dao.ManagerStock;
import entity.Stock;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author brun
 */
public class ModelTableStock extends AbstractTableModel
{

    ArrayList<Stock> listeStock = ManagerStock.getStocks();
    ArrayList<String> nomColonne = ManagerStock.getTitreStock();

    public ModelTableStock()
    {
        super();
        nomColonne.add("Total de Caisse");
    }

    @Override
    public int getRowCount()
    {
        return listeStock.size();
    }

    @Override
    public int getColumnCount()
    {
        return nomColonne.size();
    }

    @Override
    public String getColumnName(int column)
    {
        String nomColumn = null;

        if (column == 0)
        {
            nomColumn = "Modèle";
        }
        else if (column == 1)
        {
            nomColumn = "Petit";
        }
        else if (column == 2)
        {
            nomColumn = "Moyen";
        }
        else if (column == 3)
        {
            nomColumn = "Grand";
        }
        else if (column == 4)
        {
            nomColumn = "Quantité Totale";
        }

        return nomColumn;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        String retour = ""; // Contenu de la case que l'on retourne
        Stock stock = listeStock.get(rowIndex); // Stock sur lequel on travail

        switch (columnIndex)
        {
            case 0:
                retour = stock.getModele();
                break;
            case 1:
                retour = "" + stock.getQuantitePetit();
                break;
            case 2:
                retour = "" + stock.getQuantiteMoyen();
                break;
            case 3:
                retour = "" + stock.getQuantiteGrand();
            case 4:
                retour = "" + stock.getTotalQuantite();
        }
        return retour;
    }
}
