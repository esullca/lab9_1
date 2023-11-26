
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<jsp:useBean id="evaluacion" type="com.example.proyectoweb.model.beans.Evaluacion" scope="request"/>
<!DOCTYPE html>
<html>
<head>
    <title>Nuevo empleado</title>
    <jsp:include page="../Includes/headCss.jsp"></jsp:include>
</head>
<body>
<div class='container'>
    <jsp:include page="./navbar.jsp">
        <jsp:param name="currentPage" value="eva"/>
    </jsp:include>
    <div class="row mb-4">
        <div class="col"></div>
        <div class="col-md-6">
            <h1 class='mb-3'>Actualizar evaluacion</h1>
            <hr>
            <form method="POST" action="EvaluacionesServlet">
                <input type="hidden" name="evaluacion_id" value="<%= evaluacion.getIdEvaluacion()%>"/>
                <div class="mb-3">
                    <label class="form-label" for="first_name">Nombre</label>
                    <input type="text" class="form-control form-control-sm" id="first_name" name="nombre">
                </div>
                <div class="mb-3">
                    <label class="form-label" for="codigo">Codigo</label>
                    <input type="text" class="form-control form-control-sm" id="codigo" name="codigo">
                </div>

                <div class="mb-3">
                    <label class="form-label" for="correo">Correo</label>
                    <input type="text" class="form-control form-control-sm" id="correo" name="correo">
                </div>
                <div class="mb-3">
                    <label class="form-label" for="nota">Nota</label>
                    <input type="text" class="form-control form-control-sm" id="nota" name="nota">
                </div>

                <a href="<%= request.getContextPath()%>/EvaluacionesServlet" class="btn btn-danger">Cancelar</a>
                <input type="submit" value="Guardar" class="btn btn-primary"/>
            </form>
        </div>
        <div class="col"></div>
    </div>
</div>
<jsp:include page="../Includes/footer.jsp"/>
</body>
</html>
