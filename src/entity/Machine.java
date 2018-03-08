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
public class Machine {
    private int m_IdPresse ;            // numéro de presse de la machine
    private String m_LibellePresse ;    // intitulé de la presse
    private boolean m_EnService ;       // bouléen qui vaut vrai si la machine est en service
    private boolean m_Supprimable ;       // indique si la machine est supprimable ou non
    
    /**
     * Constructeur à utiliser quand on connait déjà l'id de la machine (lors du chargement de la machine dans la base de donnée)
     * @param idPresse
     * @param libellePresse
     * @param enService 
     * @param supprimable 
     */
    public Machine(int idPresse, String libellePresse, boolean enService, boolean supprimable)
    {
        this(libellePresse, enService) ;
        setIdPresse(idPresse);
        setSupprimable(supprimable);
    }
    
    /**
     * Constructeur à utiliser quand on ne connait pas l'id de la machine (lors de la création d'un machine à intégrer plus tard dans la base de donnée)
     * @param libellePresse
     * @param enService 
     */
    public Machine(String libellePresse, boolean enService)
    {
        setLibellePresse(libellePresse);
        setEnService(enService);
        setSupprimable(true);       // Par défaut supprimable vaut vrai car quand on vient de créer une machine la base de données ne l'a pas intégré et donc aucun lot n'est passé dessus
    }

    public int getIdPresse() {
        return m_IdPresse;
    }

    public void setIdPresse(int idPresse) {
        this.m_IdPresse = idPresse;
    }

    public String getLibellePresse() {
        return m_LibellePresse;
    }

    public void setLibellePresse(String libellePresse) {
        this.m_LibellePresse = libellePresse;
    }

    public boolean isEnService() {
        return m_EnService;
    }

    public void setEnService(boolean enService) {
        this.m_EnService = enService;
    }

    public boolean isSupprimable() {
        return m_Supprimable;
    }

    public void setSupprimable(boolean supprimable) {
        this.m_Supprimable = supprimable;
    }
    
     public void recopieMachine(Machine aCopier)
    {
        if (aCopier != null)
        {
            setIdPresse(aCopier.getIdPresse());
            setLibellePresse(aCopier.getLibellePresse());
            setEnService(aCopier.isEnService());
            setSupprimable(aCopier.isSupprimable());
        }
    }
    
    @Override
    public String toString() {
        return "Numéro presse : " + this.getIdPresse() + ", libellé presse : " + this.getLibellePresse() + ", en service : " + this.isEnService() + ", supprimable : " + this.isSupprimable() ;
    }
    
}
