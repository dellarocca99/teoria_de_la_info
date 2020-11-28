package paquete.PrimeraParte;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ClassFuente0 {
    private String nombreArchivoEntrada;
    private String nombreArchivoSalida;
    private String nombreArchivoSecuencia;
    private List<Integer> cantAparicionesSecuencia;
    private List<String> sucesos;
    private List<Double> probabilidades;
    private List<Double> cantidadInformacion;
    private List<Double> probabilidadesSecuencia;
    private List<Double> cantidadInformacionSecuencia;
    private String secuencia;
    private double entropia;
    private double entropiaSecuencia;
    private int numeroIteraciones;

    public ClassFuente0(String nombreArchivoEntrada, String nombreArchivoSalida,String nombreArchivoSecuencia) {
        this.nombreArchivoEntrada = nombreArchivoEntrada;
        this.nombreArchivoSalida = nombreArchivoSalida;
        this.nombreArchivoSecuencia = nombreArchivoSecuencia;
        this.cantAparicionesSecuencia=new ArrayList<Integer>();
        this.sucesos=new ArrayList<String>();
        this.probabilidades=new ArrayList<Double>();
        this.probabilidadesSecuencia=new ArrayList<Double>();
        this.cantidadInformacion=new ArrayList<Double>();
        this.cantidadInformacionSecuencia=new ArrayList<Double>();
        this.secuencia="";
        this.entropia=0;
        this.entropiaSecuencia=0;
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
        FileWriter fwSec=null;
        PrintWriter pwSec=null;
        try {
            fw = new FileWriter(this.nombreArchivoSalida);
            pw = new PrintWriter(fw);
            fwSec = new FileWriter(this.nombreArchivoSecuencia);
            pwSec = new PrintWriter(fwSec);
            pwSec.println(this.secuencia);
            if (fwSec!=null){
                fwSec.close();
            }
            pw.println("Resultados Teoricos:");
            pw.println("La cantidad de informacion es: ");
            for(int i=0;i<this.probabilidades.size();i++){
                pw.println("Suceso: "+this.sucesos.get(i)+"-->Probabilidad: "+String.valueOf(this.probabilidades.get(i))
                        +"-->Cantidad informacion: "+String.valueOf(this.cantidadInformacion.get(i)));
            }
            pw.print("La entropia es "+String.valueOf(this.entropia)+"\n");

            pw.println("Resultados Practicos de la secuencia generada: \n");
            pw.println("La cantidad de informacion es: \n");
            for(int i=0;i<this.probabilidadesSecuencia.size();i++){
                pw.println("Suceso: "+this.sucesos.get(i)+"-->Apariciones: "+String.valueOf(this.cantAparicionesSecuencia.get(i))+"-->Probabilidad: "+String.valueOf(this.probabilidadesSecuencia.get(i))
                        +"-->Cantidad informacion: "+String.valueOf(this.cantidadInformacionSecuencia.get(i)));
            }
            pw.print("La entropia es "+String.valueOf(this.entropiaSecuencia)+"\n");

            if (fw!=null){
                fw.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Double> generarProbAcumulada(){
        List<Double> probAcum=new ArrayList<Double>();
        probAcum.add(this.probabilidades.get(0));
        for(int i=1;i<this.probabilidades.size();i++){
            probAcum.add(probAcum.get(i-1)+this.probabilidades.get(i));
        }
        return probAcum;
    }
    private void generarSecuencia(){
        double num;
        int j;
        boolean encontrado;
        List<Double> probAcum=generarProbAcumulada();
        for (int i=0;i<this.probabilidades.size();i++){
            this.cantAparicionesSecuencia.add(0);
        }
        for(int i=0;i<this.numeroIteraciones;i++){
            num=Math.random();
            j=0;
            encontrado=false;
            while(j<probAcum.size() && !encontrado){
                if(num<=probAcum.get(j)){
                    encontrado=true;
                    this.secuencia+=sucesos.get(j)+"\n";
                    this.cantAparicionesSecuencia.set(j,new Integer(this.cantAparicionesSecuencia.get(j)+1));
                }
                j++;
            }
        }
        for (int i=0;i<this.cantAparicionesSecuencia.size();i++){
            this.probabilidadesSecuencia.add(Double.valueOf(this.cantAparicionesSecuencia.get(i))/this.numeroIteraciones);
            if(this.probabilidadesSecuencia.get(i)==0){
                this.cantidadInformacionSecuencia.add(0.0);
            }else{
                this.cantidadInformacionSecuencia.add(-Math.log(this.probabilidadesSecuencia.get(i)) / Math.log(2));
            }
        }
        for(int i=0;i<this.cantidadInformacionSecuencia.size();i++){
            this.entropiaSecuencia+=this.probabilidadesSecuencia.get(i)*this.cantidadInformacionSecuencia.get(i);
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
