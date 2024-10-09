<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="styles/styles.css">
<title>Listar Productos</title>
</head>
<body>
	<%
	String mensaje = (String) request.getAttribute("mensaje");
	if (mensaje != null) {
	%>
	<div class="error-message">
		<span class="text">¡ATENCION! </span> &nbsp;<%=mensaje%>
	</div>
	<%
	}
	%>
	<h1>Crear Producto</h1>
	<form action="productos" method="post">
		<input type="hidden" name="sourcePage" value="listar.jsp"> <input
			type="hidden" name="opcion" value="guardar">
		<table border="1">
			<tr>
				<td>Nombre:</td>
				<td><input type="text" name="nombre" size="50" required></td>
			</tr>
			<tr>
				<td>Cantidad:</td>
				<td><input type="number" name="cantidad" size="50" required></td>
			</tr>
			<tr>
				<td>Precio:</td>
				<td><input type="number" name="precio" size="50" required></td>
			</tr>
		</table>
		<input type="submit" value="Guardar">
	</form>
	<h1>Listar Productos</h1>
	<table border="1">
		<tr>
			<td>Nombre</td>
			<td>Cantidad</td>
			<td>Precio</td>
			<td>Fecha Creacion</td>
			<td>Fecha Actualizacion</td>
			<td>Eliminar</td>
			<td>Modificar</td>
		</tr>
		<c:forEach var="producto" items="${lista}">
			<tr>
				<td><c:out value="${ producto.nombre}"></c:out></td>
				<td><c:out value="${ producto.cantidad}"></c:out></td>
				<td><c:out value="${ producto.precio}"></c:out></td>
				<td><fmt:formatDate value="${producto.fechaCrear}"
						pattern="EEEE, dd MMMM yyyy HH:mm:ss" /></td>
				<td><fmt:formatDate value="${producto.fechaActualizar}"
						pattern="EEEE, dd MMMM yyyy HH:mm:ss" /></td>
				<td><a
					href="productos?opcion=eliminar&id=<c:out value="${ producto.id}"></c:out>">
						Eliminar </a></td>

				<td><a
					href="productos?opcion=meditar&id=<c:out value="${ producto.id}"></c:out>">
						Editar </a></td>
			</tr>
		</c:forEach>
	</table>
	<a href="index.jsp" class="btn-volver">Ir a la página principal</a>
</body>
</html>