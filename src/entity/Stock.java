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
public class Stock
{

    private String modele; // Id du model 
    private int quantitePetit; // Quantité en stock du modèle en catégorie Petit
    private int quantiteMoyen; // Quantité en stock du modèle en catégorie Moyen
    private int quantiteGrand; // Quantité en stock du modèle en catégorie Grand
    private int seuilPetit; // Seuil minimum de caisse pour le modèle en catégorie Petit
    private int seuilMoyen; // Seuil minimum de caisse pour le modèle en catégorie Moyen
    private int seuilGrand; // Seuil minimum de caisse pour le modèle en catégorie Grand

    /**
     * Constructeur de l'entitée Stock
     *
     * @param modele est le model dans le stock
     * @param quantitePetit est la quantité en stock du modèle en catégorie
     * Petit
     * @param quantiteMoyen est la quantité en stock du modèle en catégorie
     * Moyen
     * @param quantiteGrand est la quantité en stock du modèle en catégorie
     * Grand
     * @param seuilPetit est le seuil minimum de caisse pour le modèle en
     * catégorie Petit
     * @param seuilMoyen est le seuil minimum de caisse pour le modèle en
     * catégorie Moyen
     * @param seuilGrand est le seuil minimum de caisse pour le modèle en
     * catégorie Grand
     */
    public Stock(String modele, int quantitePetit, int quantiteMoyen, int quantiteGrand, int seuilPetit, int seuilMoyen, int seuilGrand)
    {
        setModele(modele);
        setQuantitePetit(quantitePetit);
        setQuantiteMoyen(quantiteMoyen);
        setQuantiteGrand(quantiteGrand);
        setSeuilPetit(seuilPetit);
        setSeuilMoyen(seuilMoyen);
        setSeuilGrand(seuilGrand);
    }

    /**
     * Constructeur de l'entité Stock
     *
     * @param modele est le model dans le stock
     */
    public Stock(String modele)
    {
        setModele(modele);
    }

    /**
     * Cette méthode permet de récupérer le model du stock
     *
     * @return retourne le modele dans ce stock
     */
    public String getModele()
    {
        return modele;
    }

    /**
     * Cette méthode permet de mettre à jour le modele
     *
     * @param model est le modele que l'on veut mettre à jour
     */
    private void setModele(String model)
    {
        this.modele = model;
    }

    /**
     * Cette méthode permet de récupérer la quantité de caisses en stock de la
     * catégorie Petit
     *
     * @return retourne la quantité de caisses en stock pour la catégorie Petit
     */
    public int getQuantitePetit()
    {
        return quantitePetit;
    }

    /**
     * Cette méthode permet de mettre à jour la quantité de caisses en stock de
     * la catégorie Petit
     *
     * @param quantitePetit est la quantité que l'on met à jour
     */
    public void setQuantitePetit(int quantitePetit)
    {
        this.quantitePetit = quantitePetit;
    }

    /**
     * Cette méthode permet de récupérer la quantité de caisses en stock de la
     * catégorie Moyen
     *
     * @return retourne la quantité de caisses en stock pour la catégorie Moyen
     */
    public int getQuantiteMoyen()
    {
        return quantiteMoyen;
    }

    /**
     * Cette méthode permet de mettre à jour la quantité de caisses en stock de
     * la catégorie Moyen
     *
     * @param quantiteMoyen est la quantité que l'on met à jour
     */
    public void setQuantiteMoyen(int quantiteMoyen)
    {
        this.quantiteMoyen = quantiteMoyen;
    }

    /**
     * Cette méthode permet de récupérer la quantité de caisses en stock de la
     * catégorie Grand
     *
     * @return retourne la quantité de caisses en stock pour la catégorie Grand
     */
    public int getQuantiteGrand()
    {
        return quantiteGrand;
    }

    /**
     * Cette méthode permet de mettre à jour la quantité de caisses en stock de
     * la catégorie Grand
     *
     * @param quantiteGrand est la quantité que l'on met à jour
     */
    public void setQuantiteGrand(int quantiteGrand)
    {
        this.quantiteGrand = quantiteGrand;
    }

    /**
     * Cette méthode permet de récupérer le seuil minimum de caisses en stock
     * pour la catégorie Petit
     *
     * @return retourne le seuil minimum de caisse en stock
     */
    public int getSeuilPetit()
    {
        return seuilPetit;
    }

    /**
     * Cette méthode permet de mettre à jour le seuil minimum de caisses en
     * stock pour la catégorie Petit
     *
     * @param seuilPetit est le seuil que l'on met à jour
     */
    public void setSeuilPetit(int seuilPetit)
    {
        this.seuilPetit = seuilPetit;
    }

    /**
     * Cette méthode permet de récupérer le seuil minimum de caisses en stock
     * pour la catégorie Moyen
     *
     * @return retourne le seuil minimum de caisse en stock
     */
    public int getSeuilMoyen()
    {
        return seuilMoyen;
    }

    /**
     * Cette méthode permet de mettre à jour le seuil minimum de caisses en
     * stock pour la catégorie Moyen
     *
     * @param seuilMoyen est le seuil que l'on met à jour
     */
    public void setSeuilMoyen(int seuilMoyen)
    {
        this.seuilMoyen = seuilMoyen;
    }

    /**
     * Cette méthode permet de récupérer le seuil minimum de caisses en stock
     * pour la catégorie Grand
     *
     * @return retourne le seuil minimum de caisse en stock
     */
    public int getSeuilGrand()
    {
        return seuilGrand;
    }

    /**
     * Cette méthode permet de mettre à jour le seuil minimum de caisses en
     * stock pour la catégorie Grand
     *
     * @param seuilGrand est le seuil que l'on met à jour
     */
    public void setSeuilGrand(int seuilGrand)
    {
        this.seuilGrand = seuilGrand;
    }

    /**
     * Cette méthode permet de set la quantité en stock et le seuil de stock par
     * catégorie
     *
     * @param categorie est la catégorie sur laquelle on travail
     * @param quantite est la quantité en stock
     * @param seuil est le seuil minimum en stock
     */
    public void setSeuilEtQuantite(String categorie, int quantite, int seuil)
    {
        if (categorie.equals("Petit"))
        {
            setQuantitePetit(quantite);
            setSeuilPetit(seuil);
        }
        else if (categorie.equals("Moyen"))
        {
            setQuantiteMoyen(quantite);
            setSeuilMoyen(seuil);
        }
        else if (categorie.equals("Grand"))
        {
            setQuantiteGrand(quantite);
            setSeuilGrand(seuil);
        }

    }

    /**
     * Cette méthode permet de récupérer la totalité des caisses en stock pour
     * un modele
     *
     * @return retourne la quantité de caisse en stock du modele
     */
    public int getTotalQuantite()
    {
        return getQuantitePetit() + getQuantiteMoyen() + getQuantiteGrand();
    }
}
