/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import util.DatabaseTools;

/**
 * représente un utilisateur de l'application, identifié par son nom d'utilisateur auquel on assicie un role
 * @author boulhol
 */
public class DatabaseUser
{
    private String m_user;
    private String m_role;

    /**
     * constructeur par initialisation de toutes les propriétés
     * @param user le nom d'utilisateur
     * @param role le role de l'utilisateur
     */
    public DatabaseUser(String user, String role)
    {
	this.m_user = user;
	this.m_role = role;
    }
    
    /**
     * retourne le nom d'utilisateur
     * @return le nom d'utilisateur
     */
    public String getUser()
    {
	return m_user;
    }
    
    /**
     * modifie le nom d'utilisateur
     * @param user le nouveau nom d'utilistateur
     */
    public void setUser(String user)
    {
	
	m_user = user;
    }
    
    /**
     * retourne le role de cet utilisateur
     * @return le role de cet utilisateur
     */
    public String getRole()
    {
	return m_role;
    }
    
    /**
     * modifie le role de cet utilisateur
     * @param role le nouveau role
     */
    public boolean setRole(String role)
    {
	boolean valide = (role == null || DatabaseTools.isRole(role));
	if (valide)
	{
	    m_role = role;
	}
	return valide;
    }
    
    @Override
    public String toString()
    {
	return String.format("%s [%s]", getUser(), getRole());
    }
}
