package com.aprendec.conexion;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

/**
 * La clase Conexion proporciona métodos para gestionar la conexión a una base de datos MySQL
 * utilizando un DataSource de Apache Tomcat.
 */
public class Conexion {
    private static BasicDataSource dataSource = null;

    /**
     * Obtiene el DataSource para establecer conexiones con la base de datos.
     * 
     * @return DataSource que permite obtener conexiones a la base de datos.
     */
    @SuppressWarnings("deprecation")
    private static DataSource getDataSource() {
        if (dataSource == null) {
            dataSource = new BasicDataSource();
            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            dataSource.setUsername("root");
            dataSource.setPassword("123456");
            dataSource.setUrl("jdbc:mysql://localhost:3306/crud?useTimezone=true&serverTimezone=UTC");
            dataSource.setInitialSize(20);
            dataSource.setMaxIdle(15);
            dataSource.setMaxTotal(20);
            dataSource.setMaxWaitMillis(5000);
        }
        return dataSource;
    }

    /**
     * Establece una conexión a la base de datos utilizando el DataSource configurado.
     * 
     * @return Connection a la base de datos.
     * @throws SQLException si ocurre un error al intentar establecer la conexión.
     */
    public static Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }
}

