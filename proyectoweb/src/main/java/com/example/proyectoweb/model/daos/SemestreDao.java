package com.example.proyectoweb.model.daos;

import com.example.proyectoweb.model.beans.Evaluacion;
import com.example.proyectoweb.model.beans.Semestre;

import java.sql.*;
import java.util.ArrayList;

public class SemestreDao extends DaoBase {
    public ArrayList<Semestre> listaSemestres() {
        ArrayList<Semestre> listaSemestres = new ArrayList<>();

        try (Connection conn = this.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM semestre ");) {

            while (rs.next()) {
                Semestre semestre = new Semestre();
                semestre.setIdSemestre(rs.getInt(1));
                semestre.setNombre(rs.getString(2));

                listaSemestres.add(semestre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaSemestres;
    }

    public int semestreHabilitado() {

        String sql = "SELECT idsemestre\n" +
                "FROM semestre\n" +
                "WHERE habilitado = 1;";

        int idCur=0;

        try(Connection connection = getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql)){


            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    idCur = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return idCur;
    }
}
