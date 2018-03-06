/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import dao.ManagerMachine;
import entity.Machine;
import java.util.ArrayList;
import util.SQLConnection;

/**
 *
 * @author denis
 */
public class TestManagerMachine {
    public static void main(String[] args)
    {
        SQLConnection.setup("jdbc:sqlserver://serveur-sql2017", "Pitson", "denis", "denis");
        ArrayList<Machine> liste = ManagerMachine.getMachineList() ;
         System.out.println("Liste des machines :");
        for (Machine m : liste)
        {
            System.out.println(m);
        }
        System.out.println(); 
         
        System.out.println("Resultat de la recherche de la machine n°6 :");
        Machine recherche = ManagerMachine.getMachineFromId(6);
        System.out.println(recherche);
        System.out.println();
        
        System.out.println("Création d'un nouveau modèle appelé test :");
        Machine nouvelle = new Machine("test", true) ;
        ManagerMachine.creerMachine(nouvelle);
        nouvelle = ManagerMachine.getMachineFromId(nouvelle.getIdPresse());
        System.out.println(nouvelle);
        System.out.println();
        
        System.out.println("Renommage de la machine test en test2 :");
        ManagerMachine.renommerMachine(nouvelle, "test2");
        nouvelle = ManagerMachine.getMachineFromId(nouvelle.getIdPresse());
        System.out.println(nouvelle);
        System.out.println();
        
        System.out.println("Mise à jour de test 2 pour la passer en hors service :");
        ManagerMachine.updateEnServiceMachine(nouvelle, false);
        nouvelle = ManagerMachine.getMachineFromId(nouvelle.getIdPresse());
        System.out.println(nouvelle);
        ManagerMachine.supprimerMachine(nouvelle);
        System.out.println();
        
        System.out.println("Machines restantes après suppression du modèle test2 :");
        liste = ManagerMachine.getMachineList() ;
        for (Machine m : liste)
        {
            System.out.println(m);
        }
        
    }
}
