package co.edu.udea.compumovil.gr08_20171.lab4;

/**
 * Created by nilto on 14/03/2017.
 */

public class Events {

    private String id;
    private String nombre;
    private String fecha;
    private String información;
    private String organizador;
    private String pais;
    private String departamento;
    private String ciudad;
    private String lugar;
    private String puntuacion;
    private String foto;

    public Events(String ciudad, String departamento, String direccion, String fecha, String foto, String id, String información, String nombre, String pais, String puntuacion, String tipo) {
        this.ciudad = ciudad;
        this.departamento = departamento;
        this.lugar = direccion;
        this.fecha = fecha;
        this.foto = foto;
        this.id = id;
        this.información = información;
        this.nombre = nombre;
        this.pais = pais;
        this.puntuacion = puntuacion;
        this.organizador = tipo;
    }

    public Events() {
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInformación() {
        return información;
    }

    public void setInformación(String información) {
        this.información = información;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(String puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getOrganizador() {
        return organizador;
    }

    public void setOrganizador(String organizador) {
        this.organizador = organizador;
    }


}
