/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import dao.ManagerModele;
import entity.Modele;
import java.util.ArrayList;
import util.SQLConnection;

/**
 *
 * @author denis
 */
public class testManagerModele {
    public static void main(String[] args)
    {
        SQLConnection.setup("jdbc:sqlserver://serveur-sql2017", "Pitson", "denis", "denis");
        ArrayList<Modele> liste = ManagerModele.getModeleList() ;
         System.out.println("Liste des modèles :");
        for (Modele m : liste)
        {
            System.out.println(m);
        }
        System.out.println(); 
         
        System.out.println("Resultat de la recherche d'un modèle appelé V8 :");
        Modele recherche = ManagerModele.getModeleFromId("V8");
        System.out.println(recherche);
        System.out.println();
        
        System.out.println("Création d'un nouveau modèle appelé test :");
        Modele nouveau = new Modele("test", 6.56, false) ;
        ManagerModele.creerModele(nouveau);
        nouveau = ManagerModele.getModeleFromId(nouveau.getIdModele());
        System.out.println(nouveau);
        System.out.println();
        
        System.out.println("Renommage du modèle test en test2 :");
        ManagerModele.renommerModele(nouveau, "test2");
        nouveau = ManagerModele.getModeleFromId(nouveau.getIdModele());
        System.out.println(nouveau);
        System.out.println();
        
        System.out.println("Mise à jour de test 2 pour le passer en obsolète :");
        ManagerModele.updateObsoleteModele(nouveau, true);
        nouveau = ManagerModele.getModeleFromId(nouveau.getIdModele());
        System.out.println(nouveau);
        ManagerModele.supprimerModele(nouveau);
        System.out.println();
        
        System.out.println("Modèles restants après suppression du modèle test2 :");
        liste = ManagerModele.getModeleList() ;
        for (Modele m : liste)
        {
            System.out.println(m);
        }
        
    }
}
