/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author brun
 */
public class ReturnDataBase
{

    private int code; // Code  retourné par la base de données
    private String message; // Message retourné par la base de données

    /**
     * Constructeur de ReturnDataBase
     *
     * @param code est le code retourné par la base de données
     * @param message est le message retourné par la base de données
     */
    public ReturnDataBase(int code, String message)
    {
        setCode(code);
        setMessage(message);
    }

    /**
     * Cette méthode permet de récupérer le code de retourné par la base de
     * données
     *
     * @return retourne le code retourné par la base de données
     */
    public int getCode()
    {
        return code;
    }

    /**
     * Cette méthode permet de mettre à jour le code de retour de la base de
     * données
     *
     * @param code est le code de retour à mettre à jour
     */
    public void setCode(int code)
    {
        this.code = code;
    }

    /**
     * Cette méthode permet de récupérer le message retourné par la base de
     * données
     *
     * @return retourne le message
     */
    public String getMessage()
    {
        return message;
    }

    /**
     * Cette méthode permet de mettre à jour le message retourné par la base de
     * données
     *
     * @param message est le message à mettre à jour
     */
    public void setMessage(String message)
    {
        this.message = message;
    }
    
    @Override
    public String toString() {
        return "Code retour : " + code + ", message : " + message;
    }

}
