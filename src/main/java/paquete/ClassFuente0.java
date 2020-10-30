package paquete;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ClassFuente0 {
    private String nombreArchivoEntrada;
    private String nombreArchivoSalida;
    private List<String> sucesos;
    private List<Double> probabilidades;
    private List<Double> cantidadInformacion;
    private String secuencia;
    private double entropia;
    private int numeroIteraciones;

    public ClassFuente0(String nombreArchivoEntrada, String nombreArchivoSalida) {
        this.nombreArchivoEntrada = nombreArchivoEntrada;
        this.nombreArchivoSalida = nombreArchivoSalida;
        this.sucesos=new ArrayList<String>();
        this.probabilidades=new ArrayList<Double>();
        this.cantidadInformacion=new ArrayList<Double>();
        this.secuencia="";
        this.entropia=0;
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
            linea=br.readLine();
            this.numeroIteraciones=Integer.valueOf(linea);
            while((linea=br.readLine())!=null){
                String aux[]=linea.split("/");
                sucesos.add(aux[0]);
                probabilidades.add(Double.valueOf(aux[1]));
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
        this.generarSecuencia();
        this.calcularCantidadInformacion();
        this.calcularEntropia();
    }
    private void writeFile(){
        FileWriter fw=null;
        PrintWriter pw=null;
        try {
            fw = new FileWriter(this.nombreArchivoSalida);
            pw = new PrintWriter(fw);
            pw.println("La secuencia es: ");
            pw.println(this.secuencia);
            pw.println("La cantidad de informacion es: ");
            for(int i=0;i<this.probabilidades.size();i++){
                pw.println("Suceso: "+this.sucesos.get(i)+"-->Probabilidad: "+String.valueOf(this.probabilidades.get(i))
                        +"-->Cantidad informacion: "+String.valueOf(this.cantidadInformacion.get(i)));
            }
            pw.print("La entropia es "+String.valueOf(this.entropia)+"\n");
            if (fw!=null){
                fw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Double> generarProbAcumulada(){
        List<Double> probAcum=new ArrayList<Double>();
        probAcum.add(0.0);
        for(int i=1;i<this.probabilidades.size();i++){
            probAcum.add(probAcum.get(i-1)+this.probabilidades.get(i-1));
        }
        return probAcum;
    }
    private void generarSecuencia(){
        double num;
        int j;
        boolean encontrado;
        List<Double> probAcum=generarProbAcumulada();
        for(int i=0;i<this.numeroIteraciones;i++){
            num=Math.random();
            j=0;
            encontrado=false;
            while(j<probAcum.size()-1 && !encontrado){
                if(num<probAcum.get(j+1)){
                    encontrado=true;
                    this.secuencia+=sucesos.get(j)+" ";
                }
                j++;
            }
        }
    }
    private void calcularCantidadInformacion(){
        for(int i=0;i<this.probabilidades.size();i++){
            this.cantidadInformacion.add(-Math.log(this.probabilidades.get(i)) / Math.log(2));
        }
    }
    private void calcularEntropia() {
        double aux=0;
        for(int i=0;i<this.cantidadInformacion.size();i++){
            aux+=this.probabilidades.get(i)*this.cantidadInformacion.get(i);
        }
        this.entropia=aux;
    }
}
