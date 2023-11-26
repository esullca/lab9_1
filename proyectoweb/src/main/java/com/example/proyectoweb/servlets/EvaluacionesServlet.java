package com.example.proyectoweb.servlets;

import com.example.proyectoweb.model.beans.Evaluacion;
import com.example.proyectoweb.model.beans.Usuario;
import com.example.proyectoweb.model.daos.EvaluacionDao;
import com.example.proyectoweb.model.daos.SemestreDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "EvaluacionesServlet", value = "/EvaluacionesServlet")
public class EvaluacionesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");
        HttpSession httpSession = request.getSession();
        Usuario docente = (Usuario) httpSession.getAttribute("usuarioLogueado");

        RequestDispatcher view;
        EvaluacionDao evaluacionDao = new EvaluacionDao();

        SemestreDao semestreDao = new SemestreDao();

        switch (action) {
            case "lista":
                request.setAttribute("listaEvaluaciones", evaluacionDao.listarEvaluaciones());
                request.setAttribute("listaSemestres", semestreDao.listaSemestres());
                view = request.getRequestDispatcher("pages/Docente/lista_evaluaciones.jsp");
                view.forward(request, response);
                break;
            case "agregar":
                view = request.getRequestDispatcher("/pages/Docente/crear_evaluaciones.jsp");
                view.forward(request, response);
                break;
            case "editar":


                if(docente != null && docente.getIdUsuario() > 0) {
                    if (request.getParameter("id") != null) {
                        String evaluacionIdString = request.getParameter("id");
                        int evaluacionID = 0;
                        try {
                            evaluacionID = Integer.parseInt(evaluacionIdString);
                        } catch (NumberFormatException ex) {
                            response.sendRedirect("EvaluacionesServlet");
                        }

                        Evaluacion eva = evaluacionDao.obtenerEvaluacion(evaluacionID);

                        if (eva != null) {
                            request.setAttribute("evaluacion", eva);
                            view = request.getRequestDispatcher("/pages/Docente/editar_evaluaciones.jsp");
                            view.forward(request, response);
                        } else {
                            response.sendRedirect("EvaluacionesServlet");
                        }

                    } else {
                        response.sendRedirect("EvaluacionesServlet");
                    }
                } else {
                    response.sendRedirect("EvaluacionesServlet");
                }
                break;
            case "borrar":
                if (request.getParameter("id") != null) {
                    String employeeIdString = request.getParameter("id");
                    int evaluacionId = 0;
                    try {
                        evaluacionId = Integer.parseInt(employeeIdString);
                    } catch (NumberFormatException ex) {
                        response.sendRedirect("EvaluacionesServlet?err=Error al borrar la evaluacion");
                    }

                    Evaluacion doc = evaluacionDao.obtenerEvaluacion(evaluacionId);

                    if (doc != null) {
                        try {
                            evaluacionDao.borrarEvaluacion(evaluacionId);
                            response.sendRedirect("EvaluacionesServlet?msg=Evaluacion borrada exitosamente");
                        } catch (SQLException e) {
                            response.sendRedirect("EvaluacionesServlet?err=Error al borrar la evaluacion");
                        }
                    }
                } else {
                    response.sendRedirect("EvaluacionesServlet?err=Error al borrar la evaluacion");
                }
                break;
            default:
                response.sendRedirect("EvaluacionesServlet");
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "guardar" : request.getParameter("action");
        EvaluacionDao evaluacionDao = new EvaluacionDao();
        int idcurso=0;
        switch (action) {
            case "guardar":
                Evaluacion e = new Evaluacion();
                e.setNombreEstudiante(request.getParameter("nombre"));
                e.setCodigoEstudiante(request.getParameter("codigo"));
                e.setCorreoEstudiante(request.getParameter("correo"));
                e.setNota(Integer.parseInt(request.getParameter("nota")));
                HttpSession httpSession = request.getSession();
                Usuario docente = (Usuario) httpSession.getAttribute("usuarioLogueado");


                idcurso = evaluacionDao.obtenerIdCurso(docente.getIdUsuario());
                e.setIdCurso(idcurso);
                if (request.getParameter("evaluacion_id") == null) {
                    try {
                        evaluacionDao.guardarEvaluacion(e);
                        response.sendRedirect("EvaluacionesServlet?msg=Docente creado exitosamente");
                    } catch (SQLException ex) {
                        response.sendRedirect("EvaluacionesServlet?err=Error al crear Docente");
                    }
                } else {
                    e.setIdEvaluacion(Integer.parseInt(request.getParameter("evaluacion_id")));
                    try {
                        evaluacionDao.actualizarEvaluacion(e);
                        response.sendRedirect("EvaluacionesServlet?msg=Docente actualizado exitosamente");
                    } catch (SQLException ex) {
                        response.sendRedirect("EvaluacionesServlet?err=Error al actualizar Docente");
                    }
                }
                break;
            case "buscar":
                String textoBuscar = request.getParameter("semestre");
                if (textoBuscar == null) {
                    response.sendRedirect("EmployeeServlet");
                } else {
                    SemestreDao semestreDao1=new SemestreDao();
                    request.setAttribute("listaSemestres", semestreDao1.listaSemestres());
                    request.setAttribute("listaEvaluaciones", evaluacionDao.buscarEvaluacionPorSemestre(Integer.parseInt(textoBuscar)));
                    RequestDispatcher view = request.getRequestDispatcher("/pages/Docente/lista_evaluaciones.jsp");
                    view.forward(request, response);
                }
                break;
        }

    }
}
