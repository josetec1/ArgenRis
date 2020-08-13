<%--
  Created by IntelliJ IDEA.
  User: jose
  Date: 09/08/2020
  Time: 03:56 p. m.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Manejador de Ordenes</title>
</head>

<body>



<p> listar las  opciones de esta pantalla </p>
<br/>
<g:link action="ordenes">ver ordenes</g:link>

<g:link action="medicos">ver medicos</g:link>




<!-- muestro los pacientes -->
<p>muentro los pacientes</p>
<table style="border: 1px solid black;">

    <thead>
    <tr>
        <th>Id</th>
        <th>Nombre</th>
        <th>apellido</th>
        <th>email</th>
    </tr>
    </thead>


    <tbody>
    <g:each in="${pacientes}" var="paciente">
        <tr>
            <th>${paciente.id}</th>
            <th>${paciente.nombre}</th>
            <th>${paciente.apellido}</th>
            <th>${paciente.email}</th>
        </tr>
    </g:each>
    </tbody>

</table>



<!-- todo reemplazar esto con mapping -->
<g:link action="nuevaOrden">Crear una nueva orden</g:link>
<br/><br/>
<a href="http://localhost:8015/h2-console/">H2 Consola</a>

</body>
</html>