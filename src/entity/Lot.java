/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.sql.Date;

/**
 *
 * @author preda
 */
public class Lot
{
    private int idLot;              // le numéro du lot
    private String idModele;        // le id du modèle
    private String etatProduction;  // l'état de la production
    private String etatControle;    // l'état du controle
    private int nbrPieceDemande;    // la quantité des pieces demandées
    private int idPresse;           // le numéro de la presse
    private Date dateDemande;       // date de la demande du lot
    private Date dateProduction;    // date du début de la production du lot
    private int piecesControlees;   // nombre de pièces controlées
    
    public Lot(int idLot, Date dateDemande, String idModele, int nbPieces)
    {
        this(idModele, "Attente", "Attente", nbPieces);
        this.dateDemande = dateDemande;
        this.idLot = idLot;
    }
    
    public Lot(int idLot, Date dateDemande, String idModele, Date dateProduction, int nbPieces)
    {
        this(idLot, dateDemande, idModele, nbPieces);
        this.dateProduction = dateProduction;
    }
    
    /**
     * Constructeur à utiliser quand on crée un lot dans l'application en attendant de le mettre dans la base de données
     * @param idModele
     * @param etatProduction
     * @param etatControle
     * @param nbrPieceDemande 
     */
    public Lot(String idModele, String etatProduction, String etatControle, int nbrPieceDemande)
    {
        this.idModele = idModele;
        this.etatProduction = etatProduction;
        this.etatControle = etatControle;
        this.nbrPieceDemande = nbrPieceDemande;
    }
    
    /**
     * Constructeur à utiliser quand on crée un lot en attente à partir de la base de données
     * @param idLot
     * @param idModele
     * @param etatProduction
     * @param etatControle
     * @param nbrPieceDemande 
     */
    public Lot(int idLot, String idModele, String etatProduction, String etatControle, int nbrPieceDemande)
    {
        this(idModele, etatProduction, etatControle, nbrPieceDemande);
        this.idLot = idLot;
    }
    
    public Lot()
    {
        
    }

    public int getIdLot()
    {
        return idLot;
    }

    public String getIdModele()
    {
        return idModele;
    }

    public String getEtatProduction()
    {
        return etatProduction;
    }

    public String getEtatControle()
    {
        return etatControle;
    }

    public int getNbrPieceDemande()
    {
        return nbrPieceDemande;
    }

    public int getIdPresse()
    {
        return idPresse;
    }

    public void setIdLot(int idLot)
    {
        this.idLot = idLot;
    }

    public void setIdModele(String idModele)
    {
        this.idModele = idModele;
    }

    public void setEtatProduction(String etatProduction)
    {
        this.etatProduction = etatProduction;
    }

    public void setEtatControle(String etatControle)
    {
        this.etatControle = etatControle;
    }

    public void setNbrPieceDemande(int nbrPieceDemande)
    {
        this.nbrPieceDemande = nbrPieceDemande;
    }

    public void setIdPresse(int idPresse)
    {
        this.idPresse = idPresse;
    }

    public Date getDateDemande()
    {
        return dateDemande;
    }

    public void setDateDemande(Date dateDemande)
    {
        this.dateDemande = dateDemande;
    }

    public Date getDateProduction()
    {
        return dateProduction;
    }

    public void setDateProduction(Date dateProduction)
    {
        this.dateProduction = dateProduction;
    }

    public int getPiecesControlees()
    {
        return piecesControlees;
    }

    public void setPiecesControlees(int piecesControlees)
    {
        this.piecesControlees = piecesControlees;
    }
    
    
    @Override
    public String toString() 
    {
        return "Numéro lot : " + this.getIdLot() + ", ID modèle : " + this.getIdModele()+ 
                ", Etat de production : " + this.getEtatProduction() + ", Etat de controle : " + this.getEtatControle()+
                ", Numéro pieces demandées : " + this.getNbrPieceDemande() +
                ", ID presse : " + this.getIdPresse();
    }

}
