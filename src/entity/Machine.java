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
    
    /**
     * Constructeur à utiliser quand on connait déjà l'id de la machine (lors du chargement de la machine dans la base de donnée)
     * @param idPresse
     * @param libellePresse
     * @param enService 
     */
    public Machine(int idPresse, String libellePresse, boolean enService)
    {
        this(libellePresse, enService) ;
        setIdPresse(idPresse);
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
    
    @Override
    public String toString() {
        return "Numéro presse : " + this.getIdPresse() + ", libellé presse : " + this.getLibellePresse() + ", en service : " + this.isEnService() ;
    }
    
    
}
