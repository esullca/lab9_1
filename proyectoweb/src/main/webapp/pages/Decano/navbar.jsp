<%@ page import="com.example.proyectoweb.model.beans.Curso" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<% String currentPage = request.getParameter("currentPage"); %>
<jsp:useBean id="usuarioLogueado" scope="session" type="com.example.proyectoweb.model.beans.Usuario" class="com.example.proyectoweb.model.beans.Usuario" />


<nav class="navbar navbar-expand-md navbar-light bg-light">
    <div class="container-fluid">
        <a class="navbar-brand" href="<%=request.getContextPath()%>">Administracion del Decano</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent"
                aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse justify-content-end" id="navbarSupportedContent">
            <ul class="navbar-nav">
                <li class="nav-item">
                    <a class="nav-link <%=currentPage.equals("cur") ? "active" : ""%>"
                       href="<%=request.getContextPath()%>/CursosServlet">
                        Cursos
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link <%=currentPage.equals("doc") ? "active" : ""%>"
                       href="<%=request.getContextPath()%>/DocentesServlet">
                        Docentes
                    </a>
                </li>
                <% if(usuarioLogueado.getIdUsuario() != 0){ %>
                <li class="nav-item">
                    <a class="nav-link" style="text-decoration: underline;color: #0d6efd;" href="<%=request.getContextPath()%>/login?a=lo">(Cerrar sesión)</a>
                </li>
                <% } %>
            </ul>
        </div>
    </div>
</nav>
</html>
