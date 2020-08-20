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
<strong> Ingrese la informacion para la cita</strong>
<br/>

<g:if test="${faltanParametros}">
    <strong> completa los campos </strong>
</g:if>

<body>










<g:form action="crearCita">
    Numero Oden: <g:select noSelection="['': 'Seleccionar Orden']" optionKey="id" optionValue="notaAdicional" name="ordenId" from= "${ordenes}" />
    <br/><br/>

    SalaDeExamen: <g:select noSelection="['': 'Seleccionar Sala']" optionKey="id" optionValue="id" name="salaId" from= "${salas}" />
    <br/><br/>
    Fecha de Cita: <input type="text" name="fecha" />
    <br/><br/>
    <button type="submit">CrearCita</button>
</g:form>
</body>

<g:link action="index">Volver al Inicio</g:link>


</body>
</html>