package com.example.proyectoweb.servlets;

import com.example.proyectoweb.model.beans.Curso;
import com.example.proyectoweb.model.beans.Usuario;
import com.example.proyectoweb.model.daos.CursoDao;
import com.example.proyectoweb.model.daos.DocentesDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

@WebServlet(name = "DocentesServlet", value = "/DocentesServlet")
public class DocentesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");

        RequestDispatcher view;
        DocentesDao docentesDao = new DocentesDao();
        HttpSession httpSession = request.getSession();
        Usuario decano = (Usuario) httpSession.getAttribute("usuarioLogueado");

        switch (action) {
            case "lista":
                request.setAttribute("listaDocentes", docentesDao.listarDocentes());
                view = request.getRequestDispatcher("pages/Decano/lista_docentes.jsp");
                view.forward(request, response);
                break;
            case "agregar":


                view = request.getRequestDispatcher("/pages/Decano/crear_docentes.jsp");
                view.forward(request, response);
                break;
            case "editar":


                if(decano != null && decano.getIdUsuario() > 0) {
                    if (request.getParameter("id") != null) {
                        String employeeIdString = request.getParameter("id");
                        int docenteId = 0;
                        try {
                            docenteId = Integer.parseInt(employeeIdString);
                        } catch (NumberFormatException ex) {
                            response.sendRedirect("DocentesServlet");
                        }

                        Usuario doc = docentesDao.obtenerDocente(docenteId);

                        if (doc != null) {
                            request.setAttribute("docente", doc);
                            view = request.getRequestDispatcher("pages/Decano/editar_docentes.jsp");
                            view.forward(request, response);
                        } else {
                            response.sendRedirect("DocentesServlet");
                        }

                    } else {
                        response.sendRedirect("DocentesServlet");
                    }
                } else {
                    response.sendRedirect("DocentesServlet");
                }
                break;
            case "borrar":
                if (request.getParameter("id") != null) {
                    String employeeIdString = request.getParameter("id");
                    int employeeId = 0;
                    try {
                        employeeId = Integer.parseInt(employeeIdString);
                    } catch (NumberFormatException ex) {
                        response.sendRedirect("DocentesServlet?err=Error al borrar al docente");
                    }

                    Usuario doc = docentesDao.obtenerDocente(employeeId);

                    if (doc != null) {
                        try {
                            docentesDao.borrarDocente(employeeId);
                            response.sendRedirect("DocentesServlet?msg=Docente borrado exitosamente");
                        } catch (SQLException e) {
                            response.sendRedirect("DocentesServlet?err=Error al borrar al docente");
                        }
                    }
                } else {
                    response.sendRedirect("DocentesServlet?err=Error al borrar al docente");
                }
                break;
            default:
                response.sendRedirect("DocentesServlet");
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        DocentesDao docentesDao = new DocentesDao();
        Usuario u = new Usuario();
        u.setNombre(request.getParameter("nombre"));
        u.setCorreo(request.getParameter("correo"));
        u.setPassword(request.getParameter("contrasena"));



        if (request.getParameter("docente_id") == null) {
            try {
                docentesDao.guardarDocente(u);
                response.sendRedirect("DocentesServlet?msg=Docente creado exitosamente");
            } catch (SQLException ex) {
                response.sendRedirect("DocentesServlet?err=Error al crear Docente");
            }
        } else {
            u.setIdUsuario(Integer.parseInt(request.getParameter("docente_id")));
            try {
                docentesDao.actualizarDocente(u);
                response.sendRedirect("DocentesServlet?msg=Docente actualizado exitosamente");
            } catch (SQLException ex) {
                response.sendRedirect("DocentesServlet?err=Error al actualizar Docente");
            }
        }

    }
}
