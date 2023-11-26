<%@page import="java.util.ArrayList" %>
<%@ page import="com.example.proyectoweb.model.beans.Usuario" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<jsp:useBean id="listaDocentes" type="java.util.ArrayList<com.example.proyectoweb.model.beans.Usuario>" scope="request"/>
<jsp:useBean id="usuarioLogueado" class="com.example.proyectoweb.model.beans.Usuario" type="com.example.proyectoweb.model.beans.Usuario" scope="session" />
<!DOCTYPE html>
<html>
<head>
    <title>Lista docentes</title>
    <jsp:include page="../Includes/headCss.jsp"></jsp:include>
</head>
<body>
<div class='container'>
    <jsp:include page="./navbar.jsp">
        <jsp:param name="currentPage" value="doc"/>
    </jsp:include>
    <div class="row mb-5 mt-4">
        <div class="col-md-7">
            <h1>Lista de Docentes</h1>
        </div>
        <div class="col-md-5 col-lg-4 ms-auto my-auto text-md-end">
            <a href="<%= request.getContextPath()%>/DocentesServlet?action=agregar" class="btn btn-primary">Agregar
                nuevo Docente</a>
        </div>
    </div>
    <% if (request.getParameter("msg") != null) {%>
    <div class="alert alert-success" role="alert"><%=request.getParameter("msg")%>
    </div>
    <% } %>
    <% if (request.getParameter("err") != null) {%>
    <div class="alert alert-danger" role="alert"><%=request.getParameter("err")%>
    </div>
    <% } %>

    <table class="table">
        <thead>
        <tr>
            <th>#</th>
            <th>Nombre</th>
            <th>Correo</th>
            <% if(usuarioLogueado != null && usuarioLogueado.getIdUsuario() > 0) {%>
            <th></th>
            <th></th>
            <% } %>
        </tr>
        </thead>
        <tbody>
        <%
            int i = 1;
            for (Usuario e : listaDocentes) {
        %>
        <tr>
            <td><%= i%>
            </td>
            <td><%= e.getNombre() %>
            </td>
            <td><%= e.getCorreo()%>
            </td>
            <% if(usuarioLogueado != null && usuarioLogueado.getIdUsuario() > 0) {%>
            <td>
                <a href="<%=request.getContextPath()%>/DocentesServlet?action=editar&id=<%= e.getIdUsuario()%>"
                   type="button" class="btn btn-primary">
                    <i class="bi bi-pencil-square"></i>
                </a>
            </td>
            <td>
                <a onclick="return confirm('¿Estas seguro de borrar?');"
                   href="<%=request.getContextPath()%>/DocentesServlet?action=borrar&id=<%= e.getIdUsuario()%>"
                   type="button" class="btn btn-danger">
                    <i class="bi bi-trash"></i>
                </a>
            </td>
            <% } %>
        </tr>
        <%
                i++;
            }
        %>
        </tbody>
    </table>
    <jsp:include page="../Includes/footer.jsp"/>
</div>
</body>
</html>