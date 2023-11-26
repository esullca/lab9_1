package com.example.proyectoweb.model.daos;

import com.example.proyectoweb.model.beans.Usuario;

import java.sql.*;
import java.util.ArrayList;

public class DecanoDao extends DaoBase {
    public int obtenerFacu(int decanoId) {

        int idFacu = -1;

        String sql = "SELECT * FROM facultad_has_decano WHERE iddecano = ?";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, decanoId);

            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {
                    idFacu=rs.getInt("idFacultad");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return idFacu;
    }
}
