package com.aprendec.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.aprendec.conexion.Conexion;
import com.aprendec.model.Producto;

/**
 * La clase ProductoDAO se encarga de realizar las operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * sobre la entidad Producto en la base de datos.
 */
public class ProductoDAO {
    private Connection connection;
    private PreparedStatement statement;
    private boolean estadoOperacion;

    /**
     * Guarda un nuevo producto en la base de datos.
     * 
     * @param producto El objeto Producto a guardar.
     * @return true si el producto se guarda correctamente, false si ya existe.
     * @throws SQLException si ocurre un error al acceder a la base de datos.
     */
    public boolean guardar(Producto producto) throws SQLException {
        String sql = null;
        estadoOperacion = false;
        connection = obtenerConexion();

        try {
            connection.setAutoCommit(false);

            // Verificar si el producto ya existe basado en el nombre
            String sqlVerificar = "SELECT COUNT(*) FROM productos WHERE nombre = ?";
            PreparedStatement verificarStmt = connection.prepareStatement(sqlVerificar);
            verificarStmt.setString(1, producto.getNombre()); 
            ResultSet rs = verificarStmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);  // Obtenemos el número de coincidencias por nombre
            
            if (count > 0) {
                // Si el producto ya existe, devolvemos false o podemos realizar otra acción como una actualización
                System.out.println("El producto con nombre '" + producto.getNombre() + "' ya existe.");
                estadoOperacion = false;
            } else {
                // Si el producto no existe, procedemos a insertarlo
                sql = "INSERT INTO productos (nombre, cantidad, precio, fecha_crear, fecha_actualizar) VALUES(?,?,?,?,?)";
                statement = connection.prepareStatement(sql);

                statement.setString(1, producto.getNombre());
                statement.setDouble(2, producto.getCantidad());
                statement.setDouble(3, producto.getPrecio());
                statement.setTimestamp(4, producto.getFechaCrear());
                statement.setTimestamp(5, producto.getFechaActualizar());

                estadoOperacion = statement.executeUpdate() > 0;

                if (estadoOperacion) {
                    System.out.println("Producto insertado correctamente.");
                }
            }

            connection.commit();
            verificarStmt.close();
            if (statement != null) statement.close();
            connection.close();
        } catch (SQLException e) {
            connection.rollback();
            System.out.println(e.getMessage());
        }

        return estadoOperacion;
    }

    /**
     * Edita un producto existente en la base de datos.
     * 
     * @param producto El objeto Producto con la información actualizada.
     * @return true si el producto se edita correctamente, false si el nombre ya existe.
     * @throws SQLException si ocurre un error al acceder a la base de datos.
     */
    public boolean editar(Producto producto) throws SQLException {
        String sql = null;
        estadoOperacion = false;
        connection = obtenerConexion();
        
        try {
            connection.setAutoCommit(false);

            // Verificar si el nombre ya existe, pero excluyendo el producto actual (por id)
            String sqlCheckName = "SELECT COUNT(*) FROM productos WHERE nombre = ? AND id != ?";
            PreparedStatement checkStatement = connection.prepareStatement(sqlCheckName);
            checkStatement.setString(1, producto.getNombre());
            checkStatement.setInt(2, producto.getId());

            ResultSet resultSet = checkStatement.executeQuery();
            if (resultSet.next() && resultSet.getInt(1) > 0) {
                // Si hay resultados, significa que ya existe otro producto con el mismo nombre
                System.out.println("El nombre del producto ya existe.");
                connection.rollback(); // Deshacemos cualquier operación si ya existe el nombre
                return false;
            }

            // Si no existe conflicto de nombres, procedemos con la actualización
            sql = "UPDATE productos SET nombre=?, cantidad=?, precio=?, fecha_actualizar=? WHERE id=?";
            statement = connection.prepareStatement(sql);

            statement.setString(1, producto.getNombre());
            statement.setDouble(2, producto.getCantidad());
            statement.setDouble(3, producto.getPrecio());
            statement.setTimestamp(4, producto.getFechaActualizar());
            statement.setInt(5, producto.getId());

            estadoOperacion = statement.executeUpdate() > 0;

            connection.commit();
            statement.close();
            checkStatement.close();
            connection.close();

        } catch (SQLException e) {
            connection.rollback();
            e.printStackTrace();
        }

        return estadoOperacion;
    }

    /**
     * Elimina un producto de la base de datos.
     * 
     * @param idProducto El ID del producto a eliminar.
     * @return true si el producto se elimina correctamente, false en caso contrario.
     * @throws SQLException si ocurre un error al acceder a la base de datos.
     */
    public boolean eliminar(int idProducto) throws SQLException {
        String sql = null;
        estadoOperacion = false;
        connection = obtenerConexion();
        try {
            connection.setAutoCommit(false);
            sql = "DELETE FROM productos WHERE id=?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, idProducto);

            estadoOperacion = statement.executeUpdate() > 0;
            connection.commit();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            connection.rollback();
            e.printStackTrace();
        }

        return estadoOperacion;
    }

    /**
     * Obtiene una lista de todos los productos en la base de datos.
     * 
     * @return una lista de objetos Producto.
     * @throws SQLException si ocurre un error al acceder a la base de datos.
     */
    public List<Producto> obtenerProductos() throws SQLException {
        ResultSet resultSet = null;
        List<Producto> listaProductos = new ArrayList<>();

        String sql = null;
        estadoOperacion = false;
        connection = obtenerConexion();

        try {
            sql = "SELECT * FROM productos";
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Producto p = new Producto();
                p.setId(resultSet.getInt(1));
                p.setNombre(resultSet.getString(2));
                p.setCantidad(resultSet.getDouble(3));
                p.setPrecio(resultSet.getDouble(4));
                p.setFechaCrear(resultSet.getTimestamp(5));
                p.setFechaActualizar(resultSet.getTimestamp(6));
                listaProductos.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaProductos;
    }

    /**
     * Obtiene un producto específico de la base de datos por su ID.
     * 
     * @param idProducto El ID del producto a obtener.
     * @return un objeto Producto con la información del producto.
     * @throws SQLException si ocurre un error al acceder a la base de datos.
     */
    public Producto obtenerProducto(int idProducto) throws SQLException {
        ResultSet resultSet = null;
        Producto p = new Producto();

        String sql = null;
        estadoOperacion = false;
        connection = obtenerConexion();

        try {
            sql = "SELECT * FROM productos WHERE id =?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, idProducto);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                p.setId(resultSet.getInt(1));
                p.setNombre(resultSet.getString(2));
                p.setCantidad(resultSet.getDouble(3));
                p.setPrecio(resultSet.getDouble(4));
                p.setFechaCrear(resultSet.getTimestamp(5));
                p.setFechaActualizar(resultSet.getTimestamp(6));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return p;
    }

    /**
     * Obtiene una conexión a la base de datos desde el pool de conexiones.
     * 
     * @return un objeto Connection para interactuar con la base de datos.
     * @throws SQLException si ocurre un error al obtener la conexión.
     */
    private Connection obtenerConexion() throws SQLException {
        return Conexion.getConnection();
    }
}
