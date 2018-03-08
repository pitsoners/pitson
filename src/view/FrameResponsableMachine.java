/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import dao.ManagerLot;
import dao.ManagerMachine;
import entity.Lot;
import entity.Machine;
import javax.swing.JOptionPane;
import model.LotEnAttenteModel;
import model.LotEnCoursModel;
import model.MachineDispoModel;
import util.SQLConnection;

/**
 *
 * @author preda
 */
public class FrameResponsableMachine extends javax.swing.JFrame
{

    /**
     * Creates new form ResponsableMachine
     */
    public FrameResponsableMachine()
    {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        paneLotMachine = new javax.swing.JTabbedPane();
        panelDemarrer = new javax.swing.JPanel();
        labelLotAttente = new javax.swing.JLabel();
        labelMachinesDispo = new javax.swing.JLabel();
        scrollPaneLotsAttente = new javax.swing.JScrollPane();
        listeLotAttente = new javax.swing.JList();
        ScrollPaneMachinesDispo = new javax.swing.JScrollPane();
        listeMachinesDispo = new javax.swing.JList();
        butGO = new javax.swing.JButton();
        panelTerminer = new javax.swing.JPanel();
        labelLotsEnCours = new javax.swing.JLabel();
        butEnd = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        listeLotsEnCours = new javax.swing.JList();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Responsable Machine");
        setName("responsableMachine"); // NOI18N

        labelLotAttente.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        labelLotAttente.setText("Lot en attente");

        labelMachinesDispo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        labelMachinesDispo.setText("Machines dispo");

        listeLotAttente.setModel(new model.LotEnAttenteModel());
        listeLotAttente.setCellRenderer(new render.RendererListLotAttente());
        scrollPaneLotsAttente.setViewportView(listeLotAttente);

        listeMachinesDispo.setModel(new model.MachineDispoModel());
        listeMachinesDispo.setCellRenderer(new render.RendererListMachineDispo());
        ScrollPaneMachinesDispo.setViewportView(listeMachinesDispo);

        butGO.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        butGO.setText("GO");
        butGO.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                butGOActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelDemarrerLayout = new javax.swing.GroupLayout(panelDemarrer);
        panelDemarrer.setLayout(panelDemarrerLayout);
        panelDemarrerLayout.setHorizontalGroup(
            panelDemarrerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDemarrerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelDemarrerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(labelLotAttente, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)
                    .addComponent(scrollPaneLotsAttente, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGroup(panelDemarrerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelDemarrerLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(labelMachinesDispo, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelDemarrerLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(ScrollPaneMachinesDispo, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addComponent(butGO, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelDemarrerLayout.setVerticalGroup(
            panelDemarrerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDemarrerLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(panelDemarrerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelLotAttente, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelMachinesDispo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelDemarrerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelDemarrerLayout.createSequentialGroup()
                        .addGroup(panelDemarrerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ScrollPaneMachinesDispo, javax.swing.GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE)
                            .addComponent(scrollPaneLotsAttente))
                        .addGap(16, 16, 16))
                    .addGroup(panelDemarrerLayout.createSequentialGroup()
                        .addComponent(butGO, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        paneLotMachine.addTab("Demarer", panelDemarrer);

        labelLotsEnCours.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        labelLotsEnCours.setText("Lots en cours");

        butEnd.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        butEnd.setText("END");
        butEnd.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                butEndActionPerformed(evt);
            }
        });

        listeLotsEnCours.setModel(new model.LotEnCoursModel());
        listeLotsEnCours.setCellRenderer(new render.RendererLotEnCours());
        jScrollPane4.setViewportView(listeLotsEnCours);

        javax.swing.GroupLayout panelTerminerLayout = new javax.swing.GroupLayout(panelTerminer);
        panelTerminer.setLayout(panelTerminerLayout);
        panelTerminerLayout.setHorizontalGroup(
            panelTerminerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTerminerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTerminerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelLotsEnCours, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 418, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addComponent(butEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelTerminerLayout.setVerticalGroup(
            panelTerminerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTerminerLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(labelLotsEnCours, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelTerminerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTerminerLayout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE)
                        .addGap(19, 19, 19))
                    .addGroup(panelTerminerLayout.createSequentialGroup()
                        .addComponent(butEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        paneLotMachine.addTab("Terminer", panelTerminer);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(paneLotMachine)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(paneLotMachine)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void butGOActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_butGOActionPerformed
    {//GEN-HEADEREND:event_butGOActionPerformed
        Lot l = (Lot)listeLotAttente.getSelectedValue();
        Machine m = (Machine)listeMachinesDispo.getSelectedValue();
        if (l != null && m != null)
        {
            l.setIdPresse(m.getIdPresse());
            if (ManagerLot.demarrerProd(l))
            {
                JOptionPane.showMessageDialog(this, "production démarée pour le lot " + l.getIdLot() + " sur la machine " + m.getLibellePresse());
                ((LotEnAttenteModel)listeLotAttente.getModel()).removeLot(l);
                ((MachineDispoModel)listeMachinesDispo.getModel()).removeMachine(m);
                ((LotEnCoursModel)listeLotsEnCours.getModel()).addLot(l);
            }
        }
    }//GEN-LAST:event_butGOActionPerformed

    private void butEndActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_butEndActionPerformed
    {//GEN-HEADEREND:event_butEndActionPerformed
        Lot l = (Lot)listeLotsEnCours.getSelectedValue();
        if(l != null)
        {
            ManagerLot.terminerProd(l);
            Machine m = ManagerMachine.getMachineFromId(l.getIdPresse());
            JOptionPane.showMessageDialog(this, "production terminé pour le lot " + l.getIdLot() + " sur la machine " + m.getLibellePresse());
            ((LotEnCoursModel)listeLotsEnCours.getModel()).removeLot(l);
            ((MachineDispoModel)listeMachinesDispo.getModel()).add(m);
        }
    }//GEN-LAST:event_butEndActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try
        {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            {
                if ("Nimbus".equals(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex)
        {
            java.util.logging.Logger.getLogger(FrameResponsableMachine.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(FrameResponsableMachine.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(FrameResponsableMachine.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(FrameResponsableMachine.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        SQLConnection.setup("jdbc:sqlserver://serveur-sql2017", "Pitson", "denis", "denis");
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new FrameResponsableMachine().setVisible(true);
            }
        });
    }
   
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane ScrollPaneMachinesDispo;
    private javax.swing.JButton butEnd;
    private javax.swing.JButton butGO;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel labelLotAttente;
    private javax.swing.JLabel labelLotsEnCours;
    private javax.swing.JLabel labelMachinesDispo;
    private javax.swing.JList listeLotAttente;
    private javax.swing.JList listeLotsEnCours;
    private javax.swing.JList listeMachinesDispo;
    private javax.swing.JTabbedPane paneLotMachine;
    private javax.swing.JPanel panelDemarrer;
    private javax.swing.JPanel panelTerminer;
    private javax.swing.JScrollPane scrollPaneLotsAttente;
    // End of variables declaration//GEN-END:variables
}
