package com.example.proyectoweb.servlets;

import com.example.proyectoweb.model.beans.Usuario;
import com.example.proyectoweb.model.beans.Curso;
import com.example.proyectoweb.model.daos.CursoDao;
import com.example.proyectoweb.model.daos.DecanoDao;
import com.example.proyectoweb.model.daos.DocentesDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

@WebServlet(name = "CursosServlet", value = "/CursosServlet")
public class CursosServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");

        RequestDispatcher view;
        CursoDao cursoDao = new CursoDao();

        DocentesDao docentesDao = new DocentesDao();
        HttpSession httpSession = request.getSession();
        Usuario decano = (Usuario) httpSession.getAttribute("usuarioLogueado");
        DecanoDao decanodao=new DecanoDao();
        switch (action) {
            case "lista":
                int idfacu=decanodao.obtenerFacu(decano.getIdUsuario());
                request.setAttribute("listaCursos", cursoDao.buscarCursosPorFacultad(idfacu));
                view = request.getRequestDispatcher("pages/Decano/lista_cursos.jsp");
                view.forward(request, response);
                break;
            case "agregar":

                request.setAttribute("listaDocentes", docentesDao.listarDocentes());
                view = request.getRequestDispatcher("pages/Decano/crear_curso.jsp");
                view.forward(request, response);
                break;
            case "editar":


                if (decano != null && decano.getIdUsuario() > 0) {
                    if (request.getParameter("id") != null) {
                        String employeeIdString = request.getParameter("id");
                        int cursoId = 0;
                        try {
                            cursoId = Integer.parseInt(employeeIdString);
                        } catch (NumberFormatException ex) {
                            response.sendRedirect("CursosServlet");
                        }

                        Curso cur = cursoDao.obtenerCurso(cursoId);

                        if (cur != null) {
                            request.setAttribute("curso", cur);
                            view = request.getRequestDispatcher("pages/Decano/editar_curso.jsp");
                            view.forward(request, response);
                        } else {
                            response.sendRedirect("CursosServlet");
                        }

                    } else {
                        response.sendRedirect("CursosServlet");
                    }
                } else {
                    response.sendRedirect("CursosServlet");
                }
                break;
            case "borrar":
                if (request.getParameter("id") != null) {
                    String employeeIdString = request.getParameter("id");
                    int employeeId = 0;
                    try {
                        employeeId = Integer.parseInt(employeeIdString);
                    } catch (NumberFormatException ex) {
                        response.sendRedirect("CursosServlet?err=Error al borrar el curso");
                    }

                    Curso cur = cursoDao.obtenerCurso(employeeId);

                    if (cur != null) {
                        try {
                            cursoDao.borrarCurso(employeeId);
                            response.sendRedirect("CursosServlet?msg=Curso borrado exitosamente");
                        } catch (SQLException e) {
                            response.sendRedirect("CursosServlet?err=Error al borrar el curso");
                        }
                    }
                } else {
                    response.sendRedirect("CursosServlet?err=Error al borrar el curso");
                }
                break;
            default:
                response.sendRedirect("CursosServlet");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        CursoDao cursoDao = new CursoDao();

        Curso curso = new Curso();
        curso.setCodigo(request.getParameter("codigo"));
        curso.setNombre(request.getParameter("nombre"));

        HttpSession httpSession1 = request.getSession();
        Usuario decano = (Usuario) httpSession1.getAttribute("usuarioLogueado");
        DecanoDao decanoDao=new DecanoDao();
        DocentesDao docentedao=new DocentesDao();
        int idfacu=decanoDao.obtenerFacu(decano.getIdUsuario());
        curso.setIdFacultad(idfacu);




        if (request.getParameter("curso_id") == null) {
            try {
                String docenteId = request.getParameter("docente_id");
                int idDocente= Integer.parseInt(docenteId);
                Usuario docente =new Usuario();
                docente.setIdUsuario(idDocente);
                curso.setUsuario(docente);
                cursoDao.guardarCurso(curso);

                response.sendRedirect("CursosServlet?msg=Curso creado exitosamente");
            } catch (SQLException ex) {
                response.sendRedirect("CursosServlet?err=Error al crear curso");
            }
        } else {
            curso.setIdCurso(Integer.parseInt(request.getParameter("curso_id")));
            try {
                cursoDao.actualizarCurso(curso);
                response.sendRedirect("CursosServlet?msg=Curso actualizado exitosamente");
            } catch (SQLException ex) {
                response.sendRedirect("CursosServlet?err=Error al actualizar curso");
            }
        }



    }
}
