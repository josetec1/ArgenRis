<%--
  Created by IntelliJ IDEA.
  User: jose
  Date: 09/08/2020
  Time: 10:02 p. m.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>muestra las ordenes</title>
</head>

<body>

<g:link action="index">Volver al Inicio</g:link>

<g:if test="${!ordenes}">
    <strong> no hay ordenes</strong>
</g:if>




<!-- muestro los  -->
<p>muentro las ordenes</p>



<table style="border: 1px solid black;">

    <thead>
    <tr>
        <th>Id</th>
        <th>NotaAdicional</th>

    </tr>
    </thead>


    <tbody>
    <g:each in="${ordenes}" var="orden">
        <tr>
            <th>${orden.id}</th>
            <th>${orden.nota}</th>

        </tr>
    </g:each>
    </tbody>

</table>



<!-- todo reemplazar esto con mapping -->
<g:link action="nuevaOrden">Crear una nueva orden</g:link>






</body>
</html>