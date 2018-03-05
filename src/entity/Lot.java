package entity;

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
    
    /**
     * Constructeur à utiliser quand..........
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
     * Constructeur à utiliser quand..............
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
    
    /**
     * Constructeur à utiliser quand......
     * @param idPresse
     * @param idLot
     * @param idModele
     * @param etatProduction
     * @param etatControle
     * @param nbrPieceDemande 
     */
    public Lot(int idPresse, int idLot, String idModele, String etatProduction, String etatControle, int nbrPieceDemande)
    {
        this(idLot, idModele, etatProduction, etatControle, nbrPieceDemande);
        this.idPresse = idPresse;
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

}
