package paquete.SegundaParte;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class Receptor {
    private Codificador_Compresor cod_com;
    private String mensaje;
    private Canal canal;
    //CONSTRUCTOR
    public Receptor(Codificador_Compresor cod_com) {
        this.cod_com = cod_com;
    }
    //GETTER Y SETTER
    public Codificador_Compresor getCod_com() {
        return cod_com;
    }
    public void setCod_com(Codificador_Compresor cod_com) {
        this.cod_com = cod_com;
    }
    public String getMensaje(){return this.mensaje; }
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    public Canal getCanal() {
        return canal;
    }
    public void setCanal(Canal canal) {
        this.canal = canal;
    }
    //OTROS
    public void recibirMensaje(String mensaje){
        this.mensaje=cod_com.decodificarMensaje(mensaje);
    }
}
