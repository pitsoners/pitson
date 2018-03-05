/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

/**
 *
 * @author brun
 */
public class Categorie
{
    private String nomCategorie; // Nom de la catégorie (Petit/Moyen/Grand/Rebut)
    private double minCategorie; // Taille minimum de la catégorie
    private double maxCategorie; // Taille maximum de la catégorie

    public Categorie(String nomCategorie, double minCategorie, double maxCategorie)
    {
        this.nomCategorie = nomCategorie;
        this.minCategorie = minCategorie;
        this.maxCategorie = maxCategorie;
    }

    public Categorie(String nomCategorie)
    {
        this.nomCategorie = nomCategorie;
    }

    public String getNomCategorie()
    {
        return nomCategorie;
    }

    public void setNomCategorie(String nomCategorie)
    {
        this.nomCategorie = nomCategorie;
    }

    public double getMinCategorie()
    {
        return minCategorie;
    }

    public void setMinCategorie(double minCategorie)
    {
        this.minCategorie = minCategorie;
    }

    public double getMaxCategorie()
    {
        return maxCategorie;
    }

    public void setMaxCategorie(double maxCategorie)
    {
        this.maxCategorie = maxCategorie;
    }

    @Override
    public String toString()
    {
        return "Categorie : " + "\nNom de la Categorie =" + getNomCategorie() + "\nMinimum de la Categorie = " + getMinCategorie() + "\tMaximum de la Categorie = " + getMaxCategorie();
    }  
}
