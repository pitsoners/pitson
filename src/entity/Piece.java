/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

/**
 *
 * @author preda
 */
public class Piece
{
    private int idLot;              // id du lot dont fait partie la pièce
    private double ht;              // la mesure de la partie haut/transversale de la pièce
    private double hl;              // la mesure de la partie haut/largeur de la pièce
    private double bt;              // la mesure de la partie bas/transversale de la pièce
    private double bl;              // la mesure de la partie bas/largeur de la pièce
    private boolean defautVisuel;   // boolean indique si la pièce a un defaut visuel
    private String commentaire;     // indique les datails du defaut

    public Piece()
    {
        
    }

    /**
     * Constructeur à utiliser quand la pièce n'a aucun defaut visuel 
     * @param idLot
     * @param ht
     * @param hl
     * @param bt
     * @param bl
     * @param defautVisuel 
     */
    public Piece(int idLot, double ht, double hl, double bt, double bl, boolean defautVisuel)
    {
        this.idLot = idLot;
        this.ht = ht;
        this.hl = hl;
        this.bt = bt;
        this.bl = bl;
        this.defautVisuel = defautVisuel;
    }

    /**
     * Constructeur à utiliser quand la pièce a un defaut visuel
     * @param idLot
     * @param ht
     * @param hl
     * @param bt
     * @param bl
     * @param defautVisuel
     * @param commentaire 
     */
    public Piece(int idLot, double ht, double hl, double bt, double bl, boolean defautVisuel, String commentaire)
    {
        this.idLot = idLot;
        this.ht = ht;
        this.hl = hl;
        this.bt = bt;
        this.bl = bl;
        this.defautVisuel = defautVisuel;
        this.commentaire = commentaire;
    }
    
    
    
    public int getIdLot()
    {
        return idLot;
    }

    public void setIdLot(int idLot)
    {
        this.idLot = idLot;
    }

    public double getHt()
    {
        return ht;
    }

    public void setHt(double ht)
    {
        this.ht = ht;
    }

    public double getHl()
    {
        return hl;
    }

    public void setHl(double hl)
    {
        this.hl = hl;
    }

    public double getBt()
    {
        return bt;
    }

    public void setBt(double bt)
    {
        this.bt = bt;
    }

    public double getBl()
    {
        return bl;
    }

    public void setBl(double bl)
    {
        this.bl = bl;
    }

    public boolean isDefautVisuel()
    {
        return defautVisuel;
    }

    public void setDefautVisuel(boolean defautVisuel)
    {
        this.defautVisuel = defautVisuel;
    }
    
    public String getCommentaire()
    {
        return commentaire;
    }

    public void setCommentaire(String commentaire)
    {
        this.commentaire = commentaire;
    }
 
}
