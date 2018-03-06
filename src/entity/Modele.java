/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

/**
 *
 * @author denis
 */
public class Modele {
    private String m_IdModele ;     // nom du modèle
    private double m_Diametre ;     // diamètre nominal du modèle
    private boolean m_Obsolete ;    // bouléen qui vaut vrai si le modèle est obsolète
    private boolean m_Supprimable ;   // indique si le modèle est supprimable ou non

    public Modele(String idModele, double diametre, boolean obsolete)
    {
        setIdModele(idModele);
        setDiametre(diametre);
        setObsolete(obsolete);
    }
    
    public String getIdModele() {
        return m_IdModele;
    }

    public void setIdModele(String idModele) {
        this.m_IdModele = idModele;
    }

    public double getDiametre() {
        return m_Diametre;
    }

    public void setDiametre(double diametre) {
        this.m_Diametre = diametre;
    }

    public boolean isObsolete() {
        return m_Obsolete;
    }

    public void setObsolete(boolean obsolete) {
        this.m_Obsolete = obsolete;
    }

    public boolean isSupprimable() {
        return m_Supprimable;
    }

    public void setSupprimable(boolean supprimable) {
        this.m_Supprimable = supprimable;
    }
    
    

    @Override
    public String toString() {
        return "Nom : " + this.getIdModele() + ", diamètre : " + this.getDiametre() + ", obsolete : " + this.isObsolete() + ", supprimable : " + this.isSupprimable() ;
    }
    
    
    
}
