<%@ page import="com.example.proyectoweb.model.beans.Usuario" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<jsp:useBean id="listaDocentes" type="java.util.ArrayList<com.example.proyectoweb.model.beans.Usuario>" scope="request"/>
<!DOCTYPE html>
<html>
<head>
    <title>Nuevo empleado</title>
    <jsp:include page="../Includes/headCss.jsp"></jsp:include>
</head>
<body>
<div class='container'>
    <jsp:include page="./navbar.jsp">
        <jsp:param name="currentPage" value="cur"/>
    </jsp:include>
    <div class="row mb-4">
        <div class="col"></div>
        <div class="col-md-6">
            <h1 class='mb-3'>Nuevo Curso</h1>
            <hr>
            <form method="POST" action="CursosServlet">
                <div class="mb-3">
                    <label class="form-label" for="codigo">Codigo</label>
                    <input type="text" class="form-control form-control-sm" id="codigo" name="codigo">
                </div>
                <div class="mb-3">
                    <label class="form-label" for="nombre">Nombre</label>
                    <input type="text" class="form-control form-control-sm" id="nombre" name="nombre">
                </div>
                <div class="mb-3">
                    <label class="form-label" for="docente_id">Docente</label>
                    <select name="docente_id" id="docente_id" class="form-select">

                        <% for (Usuario usuario : listaDocentes) {%>
                        <option value="<%=usuario.getIdUsuario()%>"><%=usuario.getNombre()%>
                        </option>
                        <% }%>
                    </select>
                </div>
                <a href="<%= request.getContextPath()%>/CursosServlet" class="btn btn-danger">Cancelar</a>
                <input type="submit" value="Guardar" class="btn btn-primary"/>
            </form>
        </div>
        <div class="col"></div>
    </div>
</div>
<jsp:include page="../Includes/footer.jsp"/>
</body>
</html>
