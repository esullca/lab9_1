
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<jsp:useBean id="docente" type="com.example.proyectoweb.model.beans.Usuario" scope="request"/>
<!DOCTYPE html>
<html>
<head>
    <title>Editar docente</title>
    <jsp:include page="../Includes/headCss.jsp"></jsp:include>
</head>
<body>
<div class='container'>
    <jsp:include page="./navbar.jsp">
        <jsp:param name="currentPage" value="doc"/>
    </jsp:include>
    <div class="row mb-4">
        <div class="col"></div>
        <div class="col-md-6">
            <h1 class='mb-3'>Editar Docente</h1>
            <hr>
            <form method="POST" action="DocentesServlet">
                <input type="hidden" name="docente_id" value="<%=docente.getIdUsuario()%>"/>
                <div class="mb-3">
                    <label class="form-label" for="nombre">Nombre</label>
                    <input type="text" class="form-control form-control-sm" id="nombre" name="nombre"
                           value="<%= docente.getNombre() == null ? "" : docente.getNombre()%>">
                </div>

                <a href="<%= request.getContextPath()%>/DocentesServlet" class="btn btn-danger">Cancelar</a>
                <input type="submit" value="Guardar" class="btn btn-primary"/>
            </form>
        </div>
        <div class="col"></div>
    </div>
</div>
<jsp:include page="../Includes/footer.jsp"/>
</body>
</html>
