package com.example.ofertasuv;

public class Promociones_Restaurantes {
    public String promocion;
    public String nombre_restaurante;
    public String horario;
    public String dia;
    public String key;
    public String direccion;
    public String imagenNet;
    public String telefono;
    public int imagen;
    public double posX;
    public double posY;

    public Promociones_Restaurantes(String promocion, String nombre_restaurante, String horario, String dia, String key, String direccion, String telefono,int imagen) {
        this.promocion = promocion;
        this.nombre_restaurante = nombre_restaurante;
        this.horario = horario;
        this.dia = dia;
        this.key = key;
        this.direccion = direccion;
        this.telefono = telefono;
        this.imagen = imagen;
    }


    public Promociones_Restaurantes(){

    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    public String getPromocion() {
        return promocion;
    }

    public void setPromocion(String promocion) {
        this.promocion = promocion;
    }

    public String getNombre_restaurante() {
        return nombre_restaurante;
    }

    public void setNombre_restaurante(String nombre_restaurante) {
        this.nombre_restaurante = nombre_restaurante;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getImagenNet() {
        return imagenNet;
    }

    public void setImagenNet(String imagenNet) {
        this.imagenNet = imagenNet;
    }
}
