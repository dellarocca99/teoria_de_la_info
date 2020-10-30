package paquete;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ClassCodificacion{

    private String nombreArchivoEntrada;
    private String nombreArchivoSalida;
    private List<Double> probabilidades;
    private List<String> codificacion;
    private List<Double> cantidadInformacion;
    private double longitudMedia;
    private double entropia;
    private double resultadoKraft;
    private boolean esCompacto;

    public ClassCodificacion(String nombreArchivoEntrada, String nombreArchivoSalida) {
        this.nombreArchivoEntrada = nombreArchivoEntrada;
        this.nombreArchivoSalida = nombreArchivoSalida;
        this.probabilidades=new ArrayList<Double>();
        this.codificacion=new ArrayList<String>();;
        this.cantidadInformacion=new ArrayList<Double>();;
        this.longitudMedia=0;
        this.entropia=0;
        this.resultadoKraft=0;
        this.esCompacto=true;
    }

    public void inicio(){
        this.readFile();
        this.calculos();
        this.writeFile();
    }

    private void readFile(){
        File f=null;
        FileReader fr=null;
        BufferedReader br=null;
        try {
            f=new File(this.nombreArchivoEntrada);
            fr=new FileReader(f);
            br=new BufferedReader(fr);
            String linea;
            while((linea=br.readLine())!=null){
                probabilidades.add(Double.valueOf(linea));
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
    private void calculos(){
        this.armarCodigoBinario();
        this.calcularCantidadInformacion();
        this.calcularEntropia();
        this.calcularLongitudMedia();
        this.verificarCompacto();
        this.calcularKraft();
    }
    private void writeFile(){
        FileWriter fw=null;
        PrintWriter pw=null;
        try {
            fw = new FileWriter(this.nombreArchivoSalida);
            pw = new PrintWriter(fw);
            pw.println("La codificacion es: ");
            for(int i=0;i<codificacion.size();i++){
                pw.print(codificacion.get(i)+" ");
            }
            pw.print("\n");
            pw.print("La entropia es "+String.valueOf(this.entropia)+"\n");
            pw.print("La longitud media es "+String.valueOf(this.longitudMedia)+"\n");
            pw.print("El resultado de la ecuacion de kraft es "+String.valueOf(this.resultadoKraft)+"\n");
            if(this.esCompacto==true){
                pw.print("Es compacto");
            }else{
                pw.print("No es compacto");
            }
            if (fw!=null){
                fw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void armarCodigoBinario(){
        int cantidadBits= (int) Math.ceil(Math.log10(this.probabilidades.size())/Math.log10(2));
        String aux1;
        String aux2;
        for(int i=0;i<this.probabilidades.size();i++) {
            aux2 = Integer.toString(i, 2);
            aux1 = "";
            for (int j = 0; j < cantidadBits-aux2.length(); j++) {
                aux1 = aux1 + "0";
            }
            aux1=aux1+aux2;
            this.codificacion.add(aux1);
        }
    }
    private void calcularCantidadInformacion(){
        for(int i=0;i<this.probabilidades.size();i++){
            this.cantidadInformacion.add(-Math.log(this.probabilidades.get(i)) / Math.log(2));
        }
    }
    private void calcularEntropia(){
        double aux=0;
        for(int i=0;i<this.cantidadInformacion.size();i++){
            aux+=this.probabilidades.get(i)*this.cantidadInformacion.get(i);
        }
        this.entropia=aux;
    }
    private void calcularLongitudMedia(){
        double aux=0;
        for(int i=0;i<this.probabilidades.size();i++){
            aux+=this.probabilidades.get(i)*this.codificacion.get(i).length();
        }
        this.longitudMedia=aux;
    }
    private void verificarCompacto(){
        List<Integer> longitud_minima=new ArrayList<Integer>();;
        for(int i=0;i<this.probabilidades.size();i++){
            longitud_minima.add((int) Math.ceil(-Math.log(probabilidades.get(i))/Math.log(2)));
        }
        int i=0;
        boolean esComp=true;
        while(i<longitud_minima.size() && esComp){
            if(codificacion.get(i).length()>longitud_minima.get(i)){
                esComp=false;
            }
            i++;
        }
        this.esCompacto=esComp;
    }
    private void calcularKraft(){
        double aux=0;
        for(int i=0;i<this.codificacion.size();i++){
            aux+=Math.pow(2,-codificacion.get(i).length());
        }
        this.resultadoKraft=aux;
    }

}