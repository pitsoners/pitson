/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author boulhol
 */
public class Database implements Serializable
{
    
    private String m_serverUrl;
    private String m_databaseName;

    public Database(String url, String name)
    {
	if (!setUrl(url))
	    throw new IllegalArgumentException("L'url ne peut etre null");
	if (!setName(name))
	    throw new IllegalArgumentException("le nom de la base de donnée ne peut être null");
    }

    public String getUrl()
    {
	return m_serverUrl;
    }

    public String getName()
    {
	return m_databaseName;
    }
    
    public boolean setName(String newDatabaseName)
    {
	boolean valide = newDatabaseName != null;
	if (valide)
	{
	    m_databaseName = newDatabaseName;
	}
	return valide;
    }
    
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
    
    
}
