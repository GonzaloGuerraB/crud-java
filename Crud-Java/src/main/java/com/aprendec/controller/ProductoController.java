package com.aprendec.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aprendec.dao.ProductoDAO;
import com.aprendec.model.Producto;

/**
 * Servlet implementation class ProductoController
 * 
 * <p>
 * Esta clase controla las peticiones relacionadas con la gestión de productos. 
 * Proporciona métodos para crear, listar, editar y eliminar productos en el sistema.
 * </p>
 */
@WebServlet(description = "administra peticiones para la tabla productos", urlPatterns = { "/productos" })
public class ProductoController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor por defecto.
	 */
	public ProductoController() {
		super();
	}

	/**
	 * Método que maneja las solicitudes HTTP GET.
	 *
	 * @param request  objeto HttpServletRequest que contiene la solicitud del cliente
	 * @param response objeto HttpServletResponse que contiene la respuesta del servidor
	 * @throws ServletException si ocurre un error durante la gestión de la solicitud
	 * @throws IOException      si ocurre un error al enviar la respuesta
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String opcion = request.getParameter("opcion");

		if (opcion.equals("crear")) {
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/crear.jsp");
			requestDispatcher.forward(request, response);

		} else if (opcion.equals("listar")) {

			ProductoDAO productoDAO = new ProductoDAO();
			List<Producto> lista = new ArrayList<>();

			try {
				lista = productoDAO.obtenerProductos();
				request.setAttribute("lista", lista);
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/listar.jsp");
				requestDispatcher.forward(request, response);

			} catch (SQLException e) {
				e.printStackTrace();
				request.setAttribute("mensaje", "Error al obtener productos");
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/crear.jsp");
				requestDispatcher.forward(request, response);
			}

		} else if (opcion.equals("meditar")) {

			int id = Integer.parseInt(request.getParameter("id"));
			ProductoDAO productoDAO = new ProductoDAO();
			Producto p = new Producto();

			try {
				p = productoDAO.obtenerProducto(id);
				request.setAttribute("producto", p);
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/editar.jsp");
				requestDispatcher.forward(request, response);

			} catch (SQLException e) {
				e.printStackTrace();
				request.setAttribute("mensaje", "Error al obtener el producto");
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/editar.jsp");
				requestDispatcher.forward(request, response);
			}

		} else if (opcion.equals("eliminar")) {

			ProductoDAO productoDAO = new ProductoDAO();
			int id = Integer.parseInt(request.getParameter("id"));
			try {
				productoDAO.eliminar(id); // Eliminar el producto de la base de datos

				// Actualizar la lista de productos después de la eliminación
				List<Producto> listaActualizada = productoDAO.obtenerProductos();
				request.setAttribute("lista", listaActualizada);

				// Agregar mensaje de éxito
				request.setAttribute("mensaje", "Producto eliminado correctamente.");

				// Redirigir nuevamente a la página listar.jsp con la lista actualizada
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/listar.jsp");
				requestDispatcher.forward(request, response);
			} catch (SQLException e) {
				e.printStackTrace();
				request.setAttribute("mensaje", "Error al eliminar el producto");
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/listar.jsp");
				requestDispatcher.forward(request, response);
			}
		}
	}

	/**
	 * Método que maneja las solicitudes HTTP POST.
	 *
	 * @param request  objeto HttpServletRequest que contiene la solicitud del cliente
	 * @param response objeto HttpServletResponse que contiene la respuesta del servidor
	 * @throws ServletException si ocurre un error durante la gestión de la solicitud
	 * @throws IOException      si ocurre un error al enviar la respuesta
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String opcion = request.getParameter("opcion");
		String sourcePage = request.getParameter("sourcePage"); // Página de origen
		Timestamp fechaActual = new Timestamp(System.currentTimeMillis());

		if (opcion.equals("guardar")) {

			ProductoDAO productoDAO = new ProductoDAO();
			Producto producto = new Producto();
			String nombre = request.getParameter("nombre");
			String cantidadStr = request.getParameter("cantidad");
			String precioStr = request.getParameter("precio");

			// Validación de campos no nulos, vacíos y formato numérico
			String mensajeError = null;
			double cantidad = 0;
			double precio = 0;

			try {
				// Validación de nombre no nulo o vacío
				if (nombre == null || nombre.trim().isEmpty()) {
					mensajeError = "El nombre no puede estar vacío";
				} 
				// Validación de cantidad no nula, vacía y numérica
				else if (cantidadStr == null || cantidadStr.trim().isEmpty()) {
					mensajeError = "La cantidad no puede estar vacía";
				} 
				else {
					try {
						cantidad = Double.parseDouble(cantidadStr);
					} catch (NumberFormatException e) {
						mensajeError = "La cantidad debe ser un número válido";
					}
				}
				// Validación de precio no nulo, vacío y numérico
				if (mensajeError == null) { // Solo validamos el precio si no hay error previo
					if (precioStr == null || precioStr.trim().isEmpty()) {
						mensajeError = "El precio no puede estar vacío";
					} else {
						try {
							precio = Double.parseDouble(precioStr);
						} catch (NumberFormatException e) {
							mensajeError = "El precio debe ser un número válido";
						}
					}
				}

				// Si hubo errores, se envía el mensaje a la vista
				if (mensajeError != null) {
					request.setAttribute("mensaje", mensajeError);
				} else {
					// Seteo de valores si las validaciones pasaron
					producto.setNombre(nombre);
					producto.setCantidad(cantidad);
					producto.setPrecio(precio);
					producto.setFechaCrear(new java.sql.Timestamp(System.currentTimeMillis())); // Usar la fecha actual

					// Intentar guardar el producto
					try {
						if (productoDAO.guardar(producto)) {
							request.setAttribute("mensaje", "Registro guardado satisfactoriamente");
						} else {
							request.setAttribute("mensaje", "Error: el producto ya existe");
						}
					} catch (SQLException e) {
						e.printStackTrace();
						request.setAttribute("mensaje", "Error al acceder a la base de datos");
					}
				}

				// Obtener la lista actualizada de productos para mostrarla en la vista
				try {
					List<Producto> listaActualizada = productoDAO.obtenerProductos();
					request.setAttribute("lista", listaActualizada);
				} catch (SQLException e) {
					e.printStackTrace();
					request.setAttribute("mensaje", "Error al obtener la lista de productos");
				}

			} catch (Exception e) {
				e.printStackTrace();
				request.setAttribute("mensaje", "Error en la validación de datos");
				
				// Cargar la lista de productos incluso si hay un error
				try {
					List<Producto> listaActualizada = productoDAO.obtenerProductos();
					request.setAttribute("lista", listaActualizada);
				} catch (SQLException e1) {
					e1.printStackTrace();
					request.setAttribute("mensaje", "Error al obtener la lista de productos");
				}
			}

			// Redirigir a la página de origen (sourcePage)
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/" + sourcePage);
			requestDispatcher.forward(request, response);
		} else if (opcion.equals("editar")) {

			// Lógica para editar producto
			Producto producto = new Producto();
			ProductoDAO productoDAO = new ProductoDAO();

			producto.setId(Integer.parseInt(request.getParameter("id")));
			producto.setNombre(request.getParameter("nombre"));
			producto.setCantidad(Double.parseDouble(request.getParameter("cantidad")));
			producto.setPrecio(Double.parseDouble(request.getParameter("precio")));
			producto.setFechaActualizar(new java.sql.Timestamp(fechaActual.getTime()));

			try {
				if (productoDAO.editar(producto)) {
					request.setAttribute("mensaje", "Registro guardado satisfactoriamente");
				} else {
					request.setAttribute("mensaje", "Error: el producto ya existe");
				}
				// Obtener la lista actualizada de productos para mostrarla en la vista
				try {
					List<Producto> listaActualizada = productoDAO.obtenerProductos();
					request.setAttribute("lista", listaActualizada);
				} catch (SQLException e) {
					e.printStackTrace();
					request.setAttribute("mensaje", "Error al obtener la lista de productos");
				}
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/listar.jsp");
				requestDispatcher.forward(request, response);
			} catch (SQLException e) {
				e.printStackTrace();
				request.setAttribute("mensaje", "Error al acceder a la base de datos");
			}
		}
		// doGet(request, response);
	}

}
