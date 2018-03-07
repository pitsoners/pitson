package util;

public class Tools
{
    /**
     * Cette outil permet de vérifier que la donnée est bien de type mesure 
     * @param s est la string que l'on veut vérifier
     * @return retourne vrai si la string est de type mesure
     */
    public static boolean isTypeMesure(String s)
    {
        boolean ok = false; // Booléen retourné par la fonction
        if ((s != null) && (s.matches("^(\\d{1,2})(,\\d{1,3})?$")))
        {
            ok = true; 
        }
        return ok;
    }
    
    /**
	 * Permet de vérifier si une chaine de caractères contient réellement des caractères
	 * Les espaces et les retours chariot ne sont pas pris en compte
	 * @param chaine est la chaine à vérifier
	 * @return
	 */
	public static boolean estNonVide(String chaine)
	{
		boolean ok = false ;	// booléen à renvoyer
		int i = 0 ;				// indice de parcours
		
		if (chaine != null && chaine != "")
		{
			while(i < chaine.length() && ok == false)
			{
				if (chaine.charAt(i) != ' ' && chaine.charAt(i) != '\n')
				{
					ok = true ;
				}
				i ++ ;
			}
		}
		return ok ;
	}
}
