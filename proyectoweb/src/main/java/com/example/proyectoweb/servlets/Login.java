package com.example.proyectoweb.servlets;

import com.example.proyectoweb.model.beans.Usuario;
import com.example.proyectoweb.model.daos.DocentesDao;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "login", value = "/login")
public class Login extends HttpServlet {

    DocentesDao userDao = new DocentesDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession httpSession = request.getSession();
        Usuario usuarioLogueado = (Usuario) httpSession.getAttribute("usuarioLogueado");

        if(usuarioLogueado != null && usuarioLogueado.getIdUsuario() > 0){

            if(request.getParameter("a") != null){//logout
                httpSession.invalidate();
            }
            response.sendRedirect(request.getContextPath()+"/index.jsp");
        }else{
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {



                String mailStr = request.getParameter("username");
                String passwdStr = request.getParameter("password");

                System.out.println("email: " + mailStr + " | passwd: " + passwdStr);


                if (userDao.validarUsuarioPasswordHashed(mailStr,passwdStr)){
                    System.out.println("usuario y password válidos");
                    Usuario user = userDao.obtenerUsuario(mailStr);

                    // Iniciar sesión exitosa, redireccionar al servlet de inicio.
                    HttpSession httpSession = request.getSession();
                    // Almacena la información del usuario en la sesión

                    httpSession.setAttribute("usuarioLogueado", user);



                    switch (user.getIdRol()){
                        case 4:
                            response.sendRedirect("EvaluacionesServlet");
                            break;

                        case 3:
                            response.sendRedirect("CursosServlet");
                            break;

                        case 2:
                            response.sendRedirect("rector");
                            break;
                        case 1:
                            response.sendRedirect("admin_gen");
                            break;

                    }

                } else {
                    System.out.println("Usuario o Contraseña Incorrectos");
                    request.setAttribute("err","Usuario o password incorrectos");
                    request.getRequestDispatcher("index.jsp").forward(request,response);
                }









    }
}

