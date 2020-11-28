package paquete.SegundaParte;

import java.io.*;

public class Emisor {
    private Codificador_Compresor cod_com;
    private String mensaje;
    private Canal canal;
    //CONSTRUCTORES
    public Emisor(Codificador_Compresor cod_com,Canal canal) {
        this.cod_com = cod_com;
        this.canal=canal;
        this.mensaje="";
    }
    //GETTER Y SETTER
    public Codificador_Compresor getCod_com() {
        return cod_com;
    }
    public void setCod_com(Codificador_Compresor cod_com) {
        this.cod_com = cod_com;
    }
    public String getMensaje() {
        return mensaje;
    }
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
    public void prepararMensaje(String nombreArchivo){
        this.mensaje="";
        this.readFile(nombreArchivo);
    }
    private void readFile(String nombreArchivo){
        File f=null;
        FileReader fr=null;
        BufferedReader br=null;
        try {
            f=new File(nombreArchivo);
            fr=new FileReader(f);
            br=new BufferedReader(fr);
            String linea;
            while((linea=br.readLine())!=null){
                this.mensaje+=linea;
            }
            if(fr!=null){
                fr.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void enviarMensaje(){
        this.canal.moverMensaje(this.cod_com.codificarMensaje(mensaje));
    }
}
