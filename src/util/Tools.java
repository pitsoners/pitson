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
}
