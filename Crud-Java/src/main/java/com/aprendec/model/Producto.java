package com.aprendec.model;

import java.sql.Timestamp;

/**
 * Esta clase representa un producto en el sistema.
 * Contiene información relevante como el nombre, cantidad, precio y fechas de creación y actualización.
 */
public class Producto {

    private int id; // Identificador único del producto
    private String nombre; // Nombre del producto
    private double cantidad; // Cantidad disponible del producto
    private double precio; // Precio del producto
    private Timestamp fechaCrear; // Fecha de creación del producto
    private Timestamp fechaActualizar; // Fecha de última actualización del producto

    /**
     * Constructor que inicializa un objeto Producto con los valores dados.
     *
     * @param id             Identificador único del producto
     * @param nombre         Nombre del producto
     * @param cantidad       Cantidad disponible del producto
     * @param precio         Precio del producto
     * @param fechaCrear     Fecha de creación del producto
     * @param fechaActualizar Fecha de última actualización del producto
     */
    public Producto(int id, String nombre, double cantidad, double precio, Timestamp fechaCrear, Timestamp fechaActualizar) {
        super();
        this.id = id;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
        this.fechaCrear = fechaCrear;
        this.fechaActualizar = fechaActualizar;
    }

    /**
     * Constructor por defecto.
     */
    public Producto() {
        // TODO Auto-generated constructor stub
    }

    /**
     * Obtiene el identificador del producto.
     *
     * @return El identificador del producto.
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el identificador del producto.
     *
     * @param id El identificador a establecer.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del producto.
     *
     * @return El nombre del producto.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del producto.
     *
     * @param nombre El nombre a establecer.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la cantidad disponible del producto.
     *
     * @return La cantidad del producto.
     */
    public double getCantidad() {
        return cantidad;
    }

    /**
     * Establece la cantidad del producto.
     *
     * @param cantidad La cantidad a establecer.
     */
    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Obtiene el precio del producto.
     *
     * @return El precio del producto.
     */
    public double getPrecio() {
        return precio;
    }

    /**
     * Establece el precio del producto.
     *
     * @param precio El precio a establecer.
     */
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    /**
     * Obtiene la fecha de creación del producto.
     *
     * @return La fecha de creación.
     */
    public Timestamp getFechaCrear() {
        return fechaCrear;
    }

    /**
     * Establece la fecha de creación del producto.
     *
     * @param fechaCrear La fecha de creación a establecer.
     */
    public void setFechaCrear(Timestamp fechaCrear) {
        this.fechaCrear = fechaCrear;
    }

    /**
     * Obtiene la fecha de última actualización del producto.
     *
     * @return La fecha de última actualización.
     */
    public Timestamp getFechaActualizar() {
        return fechaActualizar;
    }

    /**
     * Establece la fecha de última actualización del producto.
     *
     * @param fechaActualizar La fecha de actualización a establecer.
     */
    public void setFechaActualizar(Timestamp fechaActualizar) {
        this.fechaActualizar = fechaActualizar;
    }

    /**
     * Devuelve una representación en cadena del producto.
     *
     * @return Una cadena que representa el producto.
     */
    @Override
    public String toString() {
        return "Producto [id=" + id + ", nombre=" + nombre + ", cantidad=" + cantidad + ", precio=" + precio
                + ", fechaCrear=" + fechaCrear + ", fechaActualizar=" + fechaActualizar + "]";
    }
}
