/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Piece;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.ReturnDataBase;
import util.SQLConnection;

/**
 *
 * @author preda
 */
public class ManagerPiece
{
     public static ReturnDataBase saisirPiece(Piece piece)
    {
        ReturnDataBase retour = null;
        Connection connection;
        try
        {
            connection = SQLConnection.getConnection();
            CallableStatement cs = connection.prepareCall("{? = call sp_saisirPiece (?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setInt(2, piece.getIdLot());
            cs.setDouble(3, piece.getHt());
            cs.setDouble(4, piece.getHl());
            cs.setDouble(5, piece.getBt());
            cs.setDouble(6, piece.getBl());
            cs.setBoolean(7, piece.isDefautVisuel());
            cs.setString(8, piece.getCommentaire());
            cs.registerOutParameter(9, Types.INTEGER);
            cs.registerOutParameter(10, Types.VARCHAR);
            cs.execute();
            int code = cs.getInt(1);
            String message = cs.getString(10);

            retour = new ReturnDataBase(code, message);
        }
        catch (SQLException ex)
        {
            Logger.getLogger(ManagerPiece.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retour;
    }
}
