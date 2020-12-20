package paquete.SegundaParte;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class ModeloComunicacion {
    List<String> simbolos;
    List<Double> probabilidades;
    private Emisor emisor;
    private Receptor receptor;
    private Codificador_Compresor cod_com;
    private Canal canal;
    private Ruido ruido;

    public ModeloComunicacion() {
        this.ruido=new Ruido();
        this.cod_com=new Codificador_Compresor();
        this.receptor=new Receptor(cod_com);
        this.canal=new Canal(receptor,ruido);
        this.emisor=new Emisor(cod_com,canal);
    }

    public void iniciarSimulacionEnvioMensaje(String nombreArchivo,String tipoCodificacion){
        this.emisor.prepararMensaje(nombreArchivo);
        this.cod_com.prepararCodificador(this.emisor.getMensaje(),tipoCodificacion);
        this.emisor.enviarMensaje();
        this.writeFile(editarNombreArchivoCodificacion(nombreArchivo,tipoCodificacion),this.canal.getMensaje());
        this.writeFile(editarNombreArchivoResultado(nombreArchivo,tipoCodificacion),this.receptor.getMensaje());
        this.writeFile(editarNombreArchivoTabla(nombreArchivo,tipoCodificacion),armarStringCodificacion(this.cod_com.getVectorAuxiliar()));
        String mensaje="El rendimiento es: "+this.cod_com.calcularRendimiento()+"\n";
        mensaje+="La redundancia es: "+this.cod_com.calcularRedundancia()+"\n";
        mensaje+="La tasa de compresion es: "+this.cod_com.calcularTasaCompresion()+"\n";
        this.writeFile(editarNombreArchivoParametros(nombreArchivo,tipoCodificacion),mensaje);
    }

    public void iniciarCalculosCanal(String nombreArchivo){
        this.canal.readFile(nombreArchivo);
        this.canal.realizarCalculos();
        String escribir=this.canal.informacionCanal();
        this.writeFile(editarNombreArchivoCanal(nombreArchivo),escribir);
    }
    private void writeFile(String nombreArchivo,String mensaje){
        FileWriter fw=null;
        PrintWriter pw=null;
        try {
            fw = new FileWriter(".\\"+nombreArchivo);
            pw = new PrintWriter(fw);
            pw.print(mensaje);
            if (fw!=null){
                fw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String armarStringCodificacion(List<NodoAux> list){
        String msj="";
        Comparator c=Collections.reverseOrder();
        Collections.sort(list,c);
        boolean preparado=false;
        int i=0;
        NodoAux aux;
        int aux_1,aux_2;
        while(preparado==false){
            preparado=true;
            i=0;
            while(i<list.size()-1){
                aux_1=(int) list.get(i).getSimbolo().charAt(0);
                aux_2=(int) list.get(i+1).getSimbolo().charAt(0);
                if(aux_1 > aux_2){
                    preparado=false;
                    aux=list.get(i+1);
                    list.set(i+1,list.get(i));
                    list.set(i,aux);
                }
                i+=1;
            }
        }
        Iterator<NodoAux> it=list.iterator();
        NodoAux naux;
        while(it.hasNext()){
            naux=it.next();
            if(naux.getSimbolo().equals("\n")){
                msj+="Simbolo: enter"+"\tCodificacion: "+naux.getCodificacion()+"\tProbabilidad: "+String.valueOf(naux.getProbabilidad())+"\n";
            } else{
                msj+="Simbolo: "+naux.getSimbolo()+"\tCodificacion: "+naux.getCodificacion()+"\tProbabilidad: "+String.valueOf(naux.getProbabilidad())+"\n";
            }
        }
        return msj;
    }

    private String editarNombreArchivoCodificacion(String mensaje,String tipo){
        return mensaje.replace(".txt","-codificacion-"+tipo+".txt");
    }
    private String editarNombreArchivoResultado(String mensaje,String tipo){
        return mensaje.replace(".txt","-decodificacion-"+tipo+".txt");
    }
    private String editarNombreArchivoParametros(String mensaje,String tipo){
        return mensaje.replace(".txt","-parametros-"+tipo+".txt");
    }
    private String editarNombreArchivoTabla(String mensaje,String tipo){
        return mensaje.replace(".txt","-Tabla-"+tipo+".txt");
    }
    private String editarNombreArchivoCanal(String nombre){
        return nombre.replace(".txt","-resultados-"+".txt");
    }
}
