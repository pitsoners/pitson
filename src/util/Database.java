/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.Serializable;
import java.util.Objects;

/**
 * reorésente une base de données par l'url de son serveur et son nom dans ce serveur
 * @author boulhol
 */
public class Database implements Serializable, Cloneable
{
    /**
     * l'url du serveur de la base de données
     */
    private String m_serverUrl;
    /**
     * le nom de la base de données
     */
    private String m_databaseName;

    /**
     * Constructeur par initialisation
     * @param url l'url du serveur de la base de données
     * @param name le nom de la base de données
     */
    public Database(String url, String name)
    {
	if (!setUrl(url))
	    throw new IllegalArgumentException("L'url ne peut etre null");
	if (!setName(name))
	    throw new IllegalArgumentException("le nom de la base de donnée ne peut être null");
    }

    /**
     * retourne l'url du serveur de la base de données
     * @return l'url du serveur de la base de données
     */
    public String getUrl()
    {
	return m_serverUrl;
    }

    /**
     * retourne le nom de la base de données
     * @return le nom de la base de données
     */
    public String getName()
    {
	return m_databaseName;
    }
    
    /**
     * modifie le nom de la base de données
     * @param newDatabaseName le nouveau nom de la base de données
     * @return {@code true} si le nom a affecter est valide et a bien été pris en compte
     */
    public boolean setName(String newDatabaseName)
    {
	boolean valide = newDatabaseName != null;
	if (valide)
	{
	    m_databaseName = newDatabaseName;
	}
	return valide;
    }
    /**
     * modifie l'url du serveur de la base de données
     * @param newUrl le nouvel url du serveur de la base de données
     * @return {@code true} si l'url est valide et a bien été affecté
     */
    public boolean setUrl(String newUrl)
    {
	boolean valide = newUrl != null;
	if (valide)
	{
	    m_serverUrl = newUrl;
	}
	return valide;
    }

    @Override
    public boolean equals(Object obj)
    {
	boolean egal = obj == this;
	if (!egal)
	{
	    if (obj != null && obj.getClass() == getClass() && obj.hashCode() == hashCode())
	    {
		Database other = (Database) obj;
		egal = getUrl().equals(other.getUrl()) && getName().equals(other.getName());
	    } else
	    {
		egal = false;
	    }
	}
	return egal;
    }

    @Override
    public int hashCode()
    {
	int hash = 7;
	hash = 13 * hash + Objects.hashCode(this.m_serverUrl);
	hash = 13 * hash + Objects.hashCode(this.m_databaseName);
	return hash;
    }

    @Override
    public String toString()
    {
	return String.format("%s sur %s", getName(), getUrl());
    }

    @Override
    public Object clone()
    {
	Object clone;
	try
	{
	    clone = super.clone();
	}
	catch (CloneNotSupportedException e)
	{
	    clone = new Database(getUrl(), getName());
	}
	return clone;
    }
    
    
}
