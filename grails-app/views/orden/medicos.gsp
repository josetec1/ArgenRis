<%--
  Created by IntelliJ IDEA.
  User: jose
  Date: 09/08/2020
  Time: 10:02 p. m.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>muestra los medicos</title>
</head>

<body>


<g:if test="${!medicos}">
    <strong> no hay medicos</strong>
</g:if>




<!-- muestro los  -->
<p>muentro los medicos</p>
<table style="border: 1px solid black;">

    <thead>
    <tr>
        <th>Id</th>
        <th>Nombre</th>

    </tr>
    </thead>


    <tbody>
    <g:each in="${medicos}" var="medico">
        <tr>
            <th>${medico.id}</th>
            <th>${medico.nombre}</th>
        </tr>
    </g:each>
    </tbody>

</table>



<!-- todo reemplazar esto con mapping -->
<g:link action="nuevaOrden">Crear una nueva orden</g:link>
<g:link action="index">Volver al Inicio</g:link>





</body>
</html>