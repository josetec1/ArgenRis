<%--
  Created by IntelliJ IDEA.
  User: jose
  Date: 09/08/2020
  Time: 10:02 p. m.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>muestra las citas</title>
</head>

<body>

<g:link action="index">Volver al Inicio</g:link>

<g:if test="${!citas}">
    <strong> no hay citas</strong>
</g:if>




<!-- muestro los  -->
<p>muentro las citas</p>



<table style="border: 1px solid black;">

    <thead>
    <tr>
        <th>Id</th>
        <th>fecha</th>

    </tr>
    </thead>


    <tbody>
    <g:each in="${citas}" var="cita">
        <tr>
            <th>${cita.id}</th>
            <th>${cita.fecha}</th>

        </tr>
    </g:each>
    </tbody>

</table>



<!-- todo reemplazar esto con mapping -->







</body>
</html>