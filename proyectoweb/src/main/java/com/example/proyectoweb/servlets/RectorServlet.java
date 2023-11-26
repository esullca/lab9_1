package com.example.proyectoweb.servlets;

import com.example.proyectoweb.model.beans.*;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
//no se habilito ni rector ni administrador
//se puso los decanos en la base de datos directmanete
//se tiene decano con ususariopassword decano decano
//se tiene docente con ususario y password profesor profesor
@MultipartConfig
@WebServlet(name = "rector", value = "/rector")
public class RectorServlet extends HttpServlet {



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


    }
}

