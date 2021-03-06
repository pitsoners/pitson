/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import dao.ManagerPiece;
import entity.Piece;
import javax.swing.JOptionPane;
import util.ReturnDataBase;
import util.Tools;

/**
 *
 * @author preda
 */
public class FrameControleurSaisiePiece extends javax.swing.JDialog
{

    
    private int idLotEnCours;

    /**
     * Creates new form ControleurSaisiePiece
     */
    public FrameControleurSaisiePiece(java.awt.Frame parent, boolean modal, int idLot)
    {
        super(parent, modal);
        initComponents();
        setIdLotEnCours(idLot);
    }

    private void setIdLotEnCours(int idLot)
    {
        idLotEnCours = idLot;
        labelLot.setText(String.valueOf(idLot));
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

        labelPiece = new javax.swing.JLabel();
        labelCotes = new javax.swing.JLabel();
        labelHl = new javax.swing.JLabel();
        labelHt = new javax.swing.JLabel();
        labelBl = new javax.swing.JLabel();
        labelBt = new javax.swing.JLabel();
        textHl = new javax.swing.JTextField();
        textHt = new javax.swing.JTextField();
        textBl = new javax.swing.JTextField();
        textBt = new javax.swing.JTextField();
        butValiderPiece = new javax.swing.JButton();
        butRetour = new javax.swing.JButton();
        labelLotEnCours = new javax.swing.JLabel();
        labelLot = new javax.swing.JLabel();
        checkBoxDefaut = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        textAreaCommentaire = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Controleur saisie piece");
        setResizable(false);

        labelPiece.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        labelPiece.setText("Piece");

        labelCotes.setText("Cotes :");

        labelHl.setText("HL :");

        labelHt.setText("HT :");

        labelBl.setText("BL :");

        labelBt.setText("BT :");

        butValiderPiece.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        butValiderPiece.setText("Valider Piece");
        butValiderPiece.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                butValiderPieceActionPerformed(evt);
            }
        });

        butRetour.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        butRetour.setText("Retour");
        butRetour.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                butRetourActionPerformed(evt);
            }
        });

        labelLotEnCours.setText("Lot en cours :");

        checkBoxDefaut.setText("Defaut visuel ?");

        textAreaCommentaire.setColumns(20);
        textAreaCommentaire.setRows(5);
        jScrollPane1.setViewportView(textAreaCommentaire);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(labelPiece, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labelCotes, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(84, 84, 84)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(labelHl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labelHt, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                            .addComponent(labelBl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labelBt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(butValiderPiece, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(butRetour, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(textHl)
                                    .addComponent(textHt)
                                    .addComponent(textBl)
                                    .addComponent(textBt, javax.swing.GroupLayout.DEFAULT_SIZE, 468, Short.MAX_VALUE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(labelLotEnCours, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(labelLot, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(checkBoxDefaut, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelPiece, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                    .addComponent(labelLot, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelLotEnCours, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelCotes, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelHl, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textHl, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(labelHt, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textHt, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(labelBl, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textBl, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(labelBt, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textBt, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(checkBoxDefaut)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(butRetour, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addComponent(butValiderPiece, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void butValiderPieceActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_butValiderPieceActionPerformed
    {//GEN-HEADEREND:event_butValiderPieceActionPerformed
        Piece piece;
        if (evt.getSource() == butValiderPiece && Tools.isTypeMesure(textHl.getText()) 
                && Tools.isTypeMesure(textHt.getText()) && Tools.isTypeMesure(textBl.getText()) && Tools.isTypeMesure(textBt.getText()))
        {
            if (!checkBoxDefaut.isSelected())
            {
                piece = new Piece(idLotEnCours, Double.parseDouble(textHt.getText()), Double.parseDouble(textHl.getText()), 
                        Double.parseDouble(textBt.getText()), Double.parseDouble(textBl.getText()), false);
            }
            else
            {
                piece = new Piece(idLotEnCours, Double.parseDouble(textHt.getText()), Double.parseDouble(textHl.getText()), 
                        Double.parseDouble(textBt.getText()), Double.parseDouble(textBl.getText()), true, textAreaCommentaire.getText());
            }
            ReturnDataBase retour = ManagerPiece.saisirPiece(piece);
            
            if(retour.getCode() == 0)
            {
                ((FrameControleurQualite)getParent()).refreshLots();
                JOptionPane.showMessageDialog(this, retour.getMessage());
            }
            else
            {
                JOptionPane.showMessageDialog(this, "Code = " + retour.getCode() + "Message = " + retour.getMessage());
            }
        }
        else
        {
            JOptionPane.showMessageDialog(this, "erreur de syntaxe dans les mesures");
        }
    }//GEN-LAST:event_butValiderPieceActionPerformed

    private void butRetourActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_butRetourActionPerformed
    {//GEN-HEADEREND:event_butRetourActionPerformed
       if(evt.getSource() == butRetour)
       {
           dispose();
       }
    }//GEN-LAST:event_butRetourActionPerformed

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
            java.util.logging.Logger.getLogger(FrameControleurSaisiePiece.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(FrameControleurSaisiePiece.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(FrameControleurSaisiePiece.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(FrameControleurSaisiePiece.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                FrameControleurSaisiePiece dialog = new FrameControleurSaisiePiece(new javax.swing.JFrame(), true, 0);
                dialog.addWindowListener(new java.awt.event.WindowAdapter()
                {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e)
                    {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton butRetour;
    private javax.swing.JButton butValiderPiece;
    private javax.swing.JCheckBox checkBoxDefaut;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelBl;
    private javax.swing.JLabel labelBt;
    private javax.swing.JLabel labelCotes;
    private javax.swing.JLabel labelHl;
    private javax.swing.JLabel labelHt;
    private javax.swing.JLabel labelLot;
    private javax.swing.JLabel labelLotEnCours;
    private javax.swing.JLabel labelPiece;
    private javax.swing.JTextArea textAreaCommentaire;
    private javax.swing.JTextField textBl;
    private javax.swing.JTextField textBt;
    private javax.swing.JTextField textHl;
    private javax.swing.JTextField textHt;
    // End of variables declaration//GEN-END:variables
}
