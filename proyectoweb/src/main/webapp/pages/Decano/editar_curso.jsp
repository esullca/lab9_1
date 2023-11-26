<%@ page import="com.example.proyectoweb.model.beans.Curso" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<jsp:useBean id="curso" type="com.example.proyectoweb.model.beans.Curso" scope="request"/>
<!DOCTYPE html>
<html>
<head>
    <title>Editar curso</title>
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
            <h1 class='mb-3'>Editar Curso</h1>
            <hr>
            <form method="POST" action="CursosServlet">
                <input type="hidden" name="curso_id" value="<%=curso.getIdCurso()%>"/>
                <div class="mb-3">
                    <label class="form-label" for="nombre">Nombre</label>
                    <input type="text" class="form-control form-control-sm" id="nombre" name="nombre"
                           value="<%= curso.getNombre() == null ? "" : curso.getNombre()%>">
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
