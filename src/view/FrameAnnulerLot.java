/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import dao.ManagerLot;
import entity.Lot;

/**
 *
 * @author brun
 */
public class FrameAnnulerLot extends javax.swing.JFrame
{

    /**
     * Creates new form frameAnnulerLot
     */
    public FrameAnnulerLot()
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

        labelAnnulerLot = new javax.swing.JLabel();
        comboboxLotEnAttente = new javax.swing.JComboBox<Lot>();
        buttonAnnulerLot = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        labelAnnulerLot.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelAnnulerLot.setText("Annuler un Lot");

        comboboxLotEnAttente.setModel(new model.ModelComboAnnulerLot());

        buttonAnnulerLot.setText("Annuler Lot");
        buttonAnnulerLot.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                buttonAnnulerLotActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelAnnulerLot, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(comboboxLotEnAttente, javax.swing.GroupLayout.PREFERRED_SIZE, 532, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 74, Short.MAX_VALUE)
                .addComponent(buttonAnnulerLot)
                .addGap(40, 40, 40))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(labelAnnulerLot)
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboboxLotEnAttente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonAnnulerLot))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonAnnulerLotActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_buttonAnnulerLotActionPerformed
    {//GEN-HEADEREND:event_buttonAnnulerLotActionPerformed
        if (evt.getSource() == buttonAnnulerLot)
        {
            Lot l = (Lot)comboboxLotEnAttente.getSelectedItem();
            if (l != null)
            {
                ManagerLot.annulerLot(l);
            }
        }
    }//GEN-LAST:event_buttonAnnulerLotActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonAnnulerLot;
    private javax.swing.JComboBox<Lot> comboboxLotEnAttente;
    private javax.swing.JLabel labelAnnulerLot;
    // End of variables declaration//GEN-END:variables
}
