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

    /**
     * Constructeur de l'objet catégorie
     *
     * @param nomCategorie est le nom de la catégorie
     * @param minCategorie est le minimum de la catégorie
     * @param maxCategorie est le maximum de la catégorie
     */
    public Categorie(String nomCategorie, double minCategorie, double maxCategorie)
    {
        this.nomCategorie = nomCategorie;
        this.minCategorie = minCategorie;
        this.maxCategorie = maxCategorie;
    }

    /**
     * Constructeur de l'objet catégorie
     *
     * @param nomCategorie est le nom de la catégorie
     */
    public Categorie(String nomCategorie)
    {
        this.nomCategorie = nomCategorie;
    }

    /**
     * Cette méthode permet de récupérer le nom de la catégorie
     *
     * @return retourne le nom de la catégorie
     */
    public String getNomCategorie()
    {
        return nomCategorie;
    }

    /**
     * Cette méthode permet de mettre à jour ke nom de la catégorie
     *
     * @param nomCategorie est le nom que l'on veut donner à la catégorie
     */
    public void setNomCategorie(String nomCategorie)
    {
        this.nomCategorie = nomCategorie;
    }

    /**
     * Cette méthode permet de récupérer le minimum de la catégorie
     *
     * @return retourne le minimum de la catégorie
     */
    public double getMinCategorie()
    {
        return minCategorie;
    }

    /**
     * Cette méthode permet de mettre à jour le minimum de la catégorie
     *
     * @param minCategorie est le minimum que l'on veut donner à la catégorie
     */
    public void setMinCategorie(double minCategorie)
    {
        this.minCategorie = minCategorie;
    }

    /**
     * Cette méthode permet de récupérer le maximum de la catégorie
     *
     * @return retourne le maximum de la catégorie
     */
    public double getMaxCategorie()
    {
        return maxCategorie;
    }

    /**
     * cette méthode permet de mettre à jour le maximum de la catégorie
     *
     * @param maxCategorie est le maximim que l'on veut donner à la catégorie
     */
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
