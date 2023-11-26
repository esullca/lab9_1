<%@page import="java.util.ArrayList" %>
<%@ page import="com.example.proyectoweb.model.beans.Evaluacion" %>
<%@ page import="com.example.proyectoweb.model.beans.Semestre" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<jsp:useBean id="listaEvaluaciones" type="java.util.ArrayList<com.example.proyectoweb.model.beans.Evaluacion>" scope="request"/>
<jsp:useBean id="listaSemestres" type="java.util.ArrayList<com.example.proyectoweb.model.beans.Semestre>" scope="request"/>
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
        <jsp:param name="currentPage" value="eva"/>
    </jsp:include>
    <div class="row mb-5 mt-4">
        <div class="col-md-7">
            <h1>Lista de Evaluaciones</h1>
        </div>
        <div class="col-md-5 col-lg-4 ms-auto my-auto text-md-end">
            <a href="<%= request.getContextPath()%>/EvaluacionesServlet?action=agregar" class="btn btn-primary">Agregar
                Nueva Evaluacion</a>
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
    <form action="<%=request.getContextPath()%>/EvaluacionesServlet?action=buscar" method="post">
        <div class="mb-3">
            <label class="form-label" for="semestre">Semestre</label>
            <select name="semestre" id="semestre" class="form-select">
                <% for (Semestre semestre : listaSemestres) { %>
                <option value="<%= semestre.getIdSemestre() %>"><%= semestre.getNombre() %></option>
                <% } %>
            </select>
        </div>
        <input type="submit" value=filtrar>
    </form>
    <table class="table">
        <thead>
        <tr>
            <th>#</th>
            <th>Nombre</th>
            <th>Codigo</th>
            <th>Correo</th>
            <th>Nota</th>
            <% if(usuarioLogueado != null && usuarioLogueado.getIdUsuario() > 0) {%>
            <th></th>
            <th></th>
            <% } %>
        </tr>
        </thead>
        <tbody>
        <%
            int i = 1;
            for (Evaluacion e : listaEvaluaciones) {
        %>
        <tr>
            <td><%= i%>
            </td>
            <td><%= e.getNombreEstudiante() %>
            </td>
            <td><%= e.getCodigoEstudiante()%>
            </td>
            <td><%= e.getCorreoEstudiante()%>
            </td>
            <td><%= e.getNota()%>
            </td>
            <% if(usuarioLogueado != null && usuarioLogueado.getIdUsuario() > 0) {%>
            <td>
                <a href="<%=request.getContextPath()%>/EvaluacionesServlet?action=editar&id=<%= e.getIdEvaluacion()%>"
                   type="button" class="btn btn-primary">
                    <i class="bi bi-pencil-square"></i>
                </a>
            </td>
            <td>
                <a onclick="return confirm('¿Estas seguro de borrar?');"
                   href="<%=request.getContextPath()%>/EvaluacionesServlet?action=borrar&id=<%= e.getIdEvaluacion()%>"
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