package com.example.proyectoweb.model.daos;

import com.example.proyectoweb.model.beans.Curso;
import com.example.proyectoweb.model.beans.Evaluacion;
import com.example.proyectoweb.model.beans.Usuario;

import java.sql.*;
import java.util.ArrayList;

public class EvaluacionDao extends DaoBase{
    public ArrayList<Evaluacion> listarEvaluaciones() {
        ArrayList<Evaluacion> listaEvaluaciones = new ArrayList<>();

        String sql = "SELECT * FROM evaluaciones e \n";
        try (Connection conn = this.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Evaluacion evaluacion = new Evaluacion();
                fetchEmployeeData(evaluacion, rs);

                listaEvaluaciones.add(evaluacion);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return listaEvaluaciones;
    }
    private void fetchEmployeeData(Evaluacion evaluacion, ResultSet rs) throws SQLException {
        evaluacion.setIdEvaluacion(rs.getInt(1));
        evaluacion.setNombreEstudiante(rs.getString(2));
        evaluacion.setCodigoEstudiante(rs.getString(3));
        evaluacion.setCorreoEstudiante(rs.getString(4));
        evaluacion.setNota(rs.getInt(5));




    }

    public Evaluacion obtenerEvaluacion(int evaluacionId) {

        Evaluacion evaluacion = null;

        String sql = "SELECT * FROM evaluaciones e \n"
                + "WHERE e.idevaluaciones = ?";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, evaluacionId);

            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {
                    evaluacion = new Evaluacion();
                    evaluacion.setIdEvaluacion(rs.getInt(1));
                    evaluacion.setNombreEstudiante(rs.getString(2));
                    evaluacion.setCodigoEstudiante(rs.getString(3));
                    evaluacion.setCorreoEstudiante(rs.getString(4));
                    evaluacion.setNota(rs.getInt(5));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return evaluacion;
    }

    public void guardarEvaluacion(Evaluacion evaluacion) throws SQLException {

        String sql = "INSERT INTO evaluaciones (idevaluaciones, nombre_estudiantes, codigo_estudiantes, correo_estudiantes, nota, idcurso, idsemestre, fecha_registro, fecha_edicion) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ? )";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {
            pstmt.setInt(1, obtenerMaximoIdEvaluacion()+1);
            pstmt.setString(2, evaluacion.getNombreEstudiante());
            pstmt.setString(3, evaluacion.getCodigoEstudiante());
            pstmt.setString(4, evaluacion.getCorreoEstudiante());
            pstmt.setInt(5, evaluacion.getNota());
            pstmt.setInt(6, evaluacion.getIdCurso());
            SemestreDao semestreDao=new SemestreDao();
            pstmt.setInt(7, semestreDao.semestreHabilitado());
            pstmt.setTimestamp(8, new java.sql.Timestamp(System.currentTimeMillis()));
            pstmt.setTimestamp(9, new java.sql.Timestamp(System.currentTimeMillis()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Esto te dará más información sobre el error.
            throw new RuntimeException("Error al guardar evaluación", e);
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private int obtenerMaximoIdEvaluacion() {
        int maxId = 0;

        try (Connection connection = this.getConnection()) {
            String sql = "SELECT MAX(idevaluaciones) FROM evaluaciones";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        maxId = resultSet.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Manejar la excepción según tu lógica de la aplicación
        }

        return maxId;
    }
    public void actualizarEvaluacion(Evaluacion evaluacion) throws SQLException {

        String sql = "UPDATE evaluaciones SET nombre_estudiantes = ?, codigo_estudiantes = ?, correo_estudiantes = ?, nota = ?,fecha_edicion=? "
                +" WHERE idevaluaciones = ?";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {
            pstmt.setString(1, evaluacion.getNombreEstudiante());
            pstmt.setString(2, evaluacion.getCodigoEstudiante());
            pstmt.setString(3, evaluacion.getCorreoEstudiante());
            pstmt.setInt(4, evaluacion.getNota());
            pstmt.setTimestamp(5, new java.sql.Timestamp(System.currentTimeMillis()));
            pstmt.setInt(6, evaluacion.getIdEvaluacion());
            pstmt.executeUpdate();
        }
    }

    public void borrarEvaluacion(int evaluacionId) throws SQLException {
        String sql = "DELETE FROM employees WHERE employee_id = ?";
        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {
            pstmt.setInt(1, evaluacionId);
            pstmt.executeUpdate();
        }
    }

    public ArrayList<Evaluacion> buscarEvaluacionPorSemestre(int name){

        ArrayList<Evaluacion> listaEvaluaciones = new ArrayList<>();

        String sql = "SELECT * FROM evaluaciones e \n"
                + "WHERE e.idsemestre = ? ";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, name);


            try (ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    Evaluacion evaluacion = new Evaluacion();
                    fetchEmployeeData(evaluacion, rs);

                    listaEvaluaciones.add(evaluacion);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return listaEvaluaciones;
    }

    public int obtenerIdCurso(int idDocente) {

        String sql = "SELECT MAX(idcurso) AS ultimo_idcurso\n" +
                "FROM curso_has_docente\n" +
                "WHERE iddocente = ?;";

        int idCur=0;

        try(Connection connection = getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql)){

            pstmt.setInt(1,idDocente);


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




    public boolean validarUsuarioPassword(String username, String password){

        String sql = "SELECT * FROM employees_credentials where email = ? and password = ?";

        boolean exito = false;

        try(Connection connection = getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql)){

            pstmt.setString(1,username);
            pstmt.setString(2,password);

            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    exito = true;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return exito;
    }

    public boolean validarUsuarioPasswordHashed(String username, String password){

        String sql = "SELECT * FROM employees_credentials where email = ? and password_hashed = sha2(?,256)";

        boolean exito = false;

        try(Connection connection = getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql)){

            pstmt.setString(1,username);
            pstmt.setString(2,password);

            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    exito = true;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return exito;
    }

    public Evaluacion obtenerEvaluacion(String email) {

        Evaluacion evaluacion = null;

        String sql = "SELECT * FROM employees e \n"
                + "left join jobs j ON (j.job_id = e.job_id) \n"
                + "left join departments d ON (d.department_id = e.department_id)\n"
                + "left  join employees m ON (e.manager_id = m.employee_id)\n"
                + "WHERE e.email = ?";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {
                    evaluacion = new Evaluacion();
                  // fetchEmployeeData(evaluacion, rs);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return evaluacion;
    }



}
