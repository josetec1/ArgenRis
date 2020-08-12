<%--
  Created by IntelliJ IDEA.
  User: jose
  Date: 09/08/2020
  Time: 05:37 p. m.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title></title>
</head>

<body>
<p>ingrese la informacion para la nueva orden de estudio</p>


<g:if test="${faltanParametros}">
    <strong> completa los campos </strong>
</g:if>

<body>










<g:form action="crear">
    Numero Paciente: <input type="text" name="pacienteId"/>
    <br/>
    Prioridad: <input type="text" name="prioridad" />
    <br/>
    ProcedimientoID: <input type="text" name="procedimientoId" />
    <br/>
    Nota: <input type="text" name="nota" />
    <br/>
    Fecha de creacion: <input type="text" name="fecha" />
    <br/>
    <button type="submit">Crear</button>
</g:form>
</body>



</body>
</html>