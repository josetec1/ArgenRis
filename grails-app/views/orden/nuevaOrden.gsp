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
<strong> Ingrese la informacion para la nueva orden de estudio</strong>
<br/>

<g:if test="${faltanParametros}">
    <strong> completa los campos </strong>
</g:if>

<body>










<g:form action="crear">
    Numero Paciente: <g:select noSelection="['': 'Seleccionar Paciente']" optionKey="id" optionValue="nombre" name="pacienteId" from= "${pacientes}" />
    <br/><br/>

    Prioridad: <input type="text" name="prioridad" />
    <br/><br/>
    ProcedimientoID: <g:select noSelection="['': 'Seleccionar Procedimiento']" optionKey="id" optionValue="id" name="procedimientoId" from= "${procedimientos}" />
    <br/><br/>
    Nota: <input type="text" name="nota" />
    <br/><br/>
    Fecha de creacion: <input type="text" name="fecha" />
    <br/><br/>
    <button type="submit">Crear</button>
</g:form>
</body>

<g:link action="index">Volver al Inicio</g:link>


</body>
</html>