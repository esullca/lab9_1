package com.example.proyectoweb.model.daos;

import com.example.proyectoweb.model.SHA256;
import com.example.proyectoweb.model.beans.Curso;
import com.example.proyectoweb.model.SHA256;
import com.example.proyectoweb.model.beans.Evaluacion;
import com.example.proyectoweb.model.beans.Usuario;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;

public class DocentesDao extends DaoBase{
    public ArrayList<Usuario> listarDocentes() {
        ArrayList<Usuario> listaDocentes = new ArrayList<>();



        String sql = "SELECT * FROM usuario c \n"
                + "WHERE c.idRol = ? ";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, 4);


            try (ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    Usuario docentes = new Usuario();
                    fetchEmployeeData1(docentes,rs);

                    listaDocentes.add(docentes);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return listaDocentes;
    }
    private void fetchEmployeeData1(Usuario docentes, ResultSet rs) throws SQLException {
        docentes.setIdUsuario(rs.getInt(1));
        docentes.setNombre(rs.getString(2));
        docentes.setCorreo(rs.getString(3));
        docentes.setIdRol(rs.getInt(5));

    }

    public Usuario obtenerDocente(int docenteId) {

        Usuario docente = null;

        String sql = "SELECT * FROM usuario u \n"
                + "WHERE u.idusuario = ?";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, docenteId);

            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {
                    docente = new Usuario();
                    docente.setIdUsuario(docenteId);
                    docente.setNombre(rs.getString(2));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return docente;
    }


    public void guardarDocente(Usuario docente) throws SQLException {

        String sql = "INSERT INTO usuario (idusuario,nombre, correo, password, idrol,cantidad_ingresos, fecha_registro, fecha_edicion,password_hash) "
                + "VALUES (?,?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {
            pstmt.setInt(1, obtenerMaximoIdUsuario()+1);
            pstmt.setString(2, docente.getNombre());
            pstmt.setString(3, docente.getCorreo());
            pstmt.setString(4, docente.getPassword());
            pstmt.setInt(5, 4);
            pstmt.setInt(6, 0);
            pstmt.setTimestamp(7, new java.sql.Timestamp(System.currentTimeMillis()));
            pstmt.setTimestamp(8, new java.sql.Timestamp(System.currentTimeMillis()));
            pstmt.setString(9, SHA256.cipherPassword(docente.getPassword()));
            pstmt.executeUpdate();

        }
    }

    public void actualizarDocente(Usuario docente) throws SQLException {

        String sql = "UPDATE usuario SET nombre = ?,fecha_edicion=? WHERE idusuario = ?";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {
            pstmt.setString(1, docente.getNombre());
            pstmt.setTimestamp(2, new java.sql.Timestamp(System.currentTimeMillis()));
            pstmt.setInt(3, docente.getIdUsuario());
            pstmt.executeUpdate();
        }
    }

    public void borrarDocente(int docenteId) throws SQLException {
        String sql = "DELETE FROM employees WHERE employee_id = ?";
        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {
            pstmt.setInt(1, docenteId);
            pstmt.executeUpdate();
        }
    }


    private int obtenerMaximoIdUsuario() {
        int maxId = 0;

        try (Connection connection = this.getConnection()) {
            String sql = "SELECT MAX(idusuario) FROM usuario";

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
    public int ultimoCursoDocente() {

        Evaluacion evaluacion = null;
        int semId=0;
        String sql = "SELECT MAX(idcurso) AS ultimo_idcurso\n" +
                "FROM curso_has_docente\n" +
                "WHERE iddocente = 4;";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {


            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {
                    semId=rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return semId;
    }











    public boolean validarUsuarioPassword(String username, String password){

        String sql = "SELECT * FROM usuario where correo = ? and password = ?";

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

        String sql = "SELECT * FROM usuario where correo = ? and password_hash = sha2(?,256)";

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
    public Usuario obtenerUsuario(String email) {

        Usuario usuario = null;

        String sql = "SELECT * FROM usuario e \n"
                + "WHERE e.correo = ?";


        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            aumentarIngreso(email);
            pstmt.setString(1, email);


            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {
                    usuario = new Usuario();
                    fetchEmployeeData(usuario, rs);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return usuario;
    }
    private void fetchEmployeeData(Usuario usuario, ResultSet rs) throws SQLException {
        usuario.setIdUsuario(rs.getInt(1));
        usuario.setNombre(rs.getString(2));
        usuario.setCorreo(rs.getString(3));
        usuario.setIdRol(rs.getInt(5));



    }
    public void aumentarIngreso(String correo) throws SQLException {

        String selectQuery = "SELECT cantidad_ingresos FROM usuario WHERE correo = ?";
        try (Connection conn = this.getConnection();PreparedStatement selectStatement = conn.prepareStatement(selectQuery)) {
            selectStatement.setString(1, correo);

            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (resultSet.next()) {
                    int cantidadActual = resultSet.getInt("cantidad_ingresos");

                    // Paso 2: Actualizar la cantidad de ingresos sumando 1
                    String updateQuery = "UPDATE usuario SET cantidad_ingresos = ?,ultimo_ingreso=? WHERE correo = ?";
                    try (PreparedStatement updateStatement = conn.prepareStatement(updateQuery)) {
                        updateStatement.setInt(1, cantidadActual + 1);
                        updateStatement.setTimestamp(2, new java.sql.Timestamp(System.currentTimeMillis()));
                        updateStatement.setString(3, correo);

                        int filasActualizadas = updateStatement.executeUpdate();
                        if (filasActualizadas > 0) {
                            System.out.println("Cantidad de ingresos actualizada correctamente.");
                        } else {
                            System.out.println("No se pudo actualizar la cantidad de ingresos.");
                        }
                    }
                } else {
                    System.out.println("No se encontró un usuario con el correo especificado.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
