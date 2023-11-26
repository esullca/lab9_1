package com.example.proyectoweb.model.daos;

import com.example.proyectoweb.model.beans.Curso;
import com.example.proyectoweb.model.beans.Usuario;

import java.sql.*;
import java.util.ArrayList;

public class CursoDao extends DaoBase {
    public ArrayList<Curso> listarCursos() {
        ArrayList<Curso> listaCursos = new ArrayList<>();

        String sql = "SELECT * FROM cursos  \n"
                + "left join jobs j on (j.job_id = e.job_id) \n"
                + "left join departments d on (d.department_id = e.department_id)\n"
                + "left  join employees m on (e.manager_id = m.employee_id)";
        try (Connection conn = this.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Curso curso = new Curso();


                listaCursos.add(curso);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return listaCursos;
    }

    public Curso obtenerCurso(int cursoId) {

        Curso curso = null;

        String sql = "SELECT * FROM curso c \n"
                + "WHERE c.idcurso = ?";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, cursoId);

            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {
                    curso = new Curso();
                    curso.setIdCurso(cursoId);
                    curso.setNombre(rs.getString(3));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return curso;
    }

    public void guardarCurso(Curso curso) throws SQLException {

        String sql = "INSERT INTO curso (idcurso,codigo, nombre, idfacultad, fecha_registro, fecha_edicion) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {
            pstmt.setInt(1, obtenerMaximoIdCurso()+1);
            pstmt.setString(2, curso.getCodigo());
            pstmt.setString(3, curso.getNombre());
            pstmt.setInt(4, curso.getIdFacultad());
            pstmt.setTimestamp(5, new java.sql.Timestamp(System.currentTimeMillis()));
            pstmt.setTimestamp(6, new java.sql.Timestamp(System.currentTimeMillis()));

            pstmt.executeUpdate();
        }
        enlaceCursoDocente(obtenerMaximoIdCurso(),curso.getUsuario().getIdUsuario());
    }
    private int obtenerMaximoIdCurso() {
        int maxId = 0;

        try (Connection connection = this.getConnection()) {
            String sql = "SELECT MAX(idcurso) FROM curso";

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
    public void enlaceCursoDocente(int cursoId,int docenteId) throws SQLException {

        String sql = "INSERT INTO curso_has_docente (idcurso,iddocente) "
                + "VALUES (?,?)";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {
            pstmt.setInt(1, cursoId);
            pstmt.setInt(2, docenteId);
            pstmt.executeUpdate();

        }
    }

    public void actualizarCurso(Curso curso) throws SQLException {

        String sql = "UPDATE curso SET  nombre = ?,"
                + "fecha_edicion = ? WHERE idcurso=?";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setString(1, curso.getNombre());

            pstmt.setTimestamp(2, new java.sql.Timestamp(System.currentTimeMillis()));

            pstmt.setInt(3, curso.getIdCurso());
            pstmt.executeUpdate();
        }
    }

    public void borrarCurso(int cursoId) throws SQLException {
        String sql = "DELETE FROM employees WHERE employee_id = ?";
        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {
            pstmt.setInt(1, cursoId);
            pstmt.executeUpdate();
        }
    }

    public ArrayList<Curso> buscarCursosPorFacultad(int name){

        ArrayList<Curso> listaCursos = new ArrayList<>();

        String sql = "SELECT\n" +
                "    c.idcurso,\n" +
                "    c.codigo,\n" +
                "    c.nombre AS nombre_curso,\n" +
                "    f.idfacultad,\n" +
                "    f.nombre AS nombre_facultad,\n" +
                "    cd.iddocente,\n" +
                "    d.nombre AS nombre_docente\n" +
                "FROM\n" +
                "    lab_9.curso c\n" +
                "JOIN\n" +
                "    lab_9.facultad f ON c.idfacultad = f.idfacultad\n" +
                "JOIN\n" +
                "    lab_9.curso_has_docente cd ON c.idcurso = cd.idcurso\n" +
                "JOIN\n" +
                "    lab_9.usuario d ON cd.iddocente = d.idusuario\n" +
                "WHERE\n" +
                "    f.idfacultad = ?;;";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, name);


            try (ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    Curso curso = new Curso();
                    fetchEmployeeData(curso, rs);

                    listaCursos.add(curso);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return listaCursos;
    }
    private void fetchEmployeeData(Curso curso, ResultSet rs) throws SQLException {
        curso.setIdCurso(rs.getInt(1));
        curso.setCodigo(rs.getString(2));
        curso.setNombre(rs.getString(3));
        curso.setIdFacultad(rs.getInt(4));
        Usuario docente=new Usuario();
        curso.setUsuario(docente);
        curso.getUsuario().setIdUsuario(rs.getInt(6));
        curso.getUsuario().setNombre(rs.getString(7));



    }










    public Curso obtenerCurso(String email) {

        Curso curso = null;

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
                    curso = new Curso();
                    curso.setIdCurso(rs.getInt(1));
                    curso.setCodigo(rs.getString(2));
                    curso.setNombre(rs.getString(3));


                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return curso;
    }


}
