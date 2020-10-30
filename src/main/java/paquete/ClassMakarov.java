package paquete;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ClassMakarov {
    private String nombreArchivoEntrada;
    private String nombreArchivoSalida;
    private double matTransitiva[][];
    private List<Double> probabilidades;
    private List<Double> vecEstacionario;
    private double entropia;

    public ClassMakarov(String nombreArchivoEntrada, String nombreArchivoSalida) {
        this.nombreArchivoEntrada = nombreArchivoEntrada;
        this.nombreArchivoSalida = nombreArchivoSalida;
        this.probabilidades=new ArrayList<Double>();
        this.vecEstacionario=new ArrayList<Double>();
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
            String[] aux1=linea.split("x");
            int filas=Integer.valueOf(aux1[0]);
            int columnas=Integer.valueOf(aux1[1]);
            this.matTransitiva=new double[filas][columnas];
            int i=0;
            while((linea=br.readLine())!=null && i<filas){
                String[] aux2=linea.split(" ");
                for(int j=0;j<columnas;j++){
                    this.matTransitiva[i][j]=Double.valueOf(aux2[j]);
                }
                i++;
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
        this.calcularVecEstacionario(20,0.01);
        this.calcularEntropia();
    }
    private void writeFile(){
        FileWriter fw=null;
        PrintWriter pw=null;
        try {
            fw = new FileWriter(this.nombreArchivoSalida);
            pw = new PrintWriter(fw);
            pw.println("El vector estacionario es: ");
            for(int i=0;i<vecEstacionario.size();i++){
                pw.print(String.valueOf(vecEstacionario.get(i))+" ");
            }
            pw.print("\n");
            pw.print("La entropia es "+String.valueOf(this.entropia)+"\n");
            if (fw!=null){
                fw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void calcularVecEstacionario(int n,double exactitud){
        double matTemp1[][]=this.matTransitiva;
        double matTemp2[][];
        for(int i=0;i<n;i++){
            matTemp2=matTemp1;
            matTemp1=multiplicarMatrices(matTemp1,matTemp2);
        }

        int i=0,j=0;
        double aux1,aux2;
        boolean follow=true;
        while(i<this.matTransitiva[0].length && follow){
            while(j<this.matTransitiva.length-1 && follow){
                aux1=matTemp1[i][j];
                aux2=matTemp1[i][j+1];
                if(Math.abs(aux2-aux1)>exactitud){
                    follow=false;
                }
                j++;
            }
            i++;
        }
        if(follow){
            for(n=0;n<this.matTransitiva[0].length;n++){
                vecEstacionario.add(matTemp1[n][1]);
            }
        }
    }
    private double[][] calcularCantidadInformacion(){
        int filas=this.matTransitiva[0].length;
        int columnas=this.matTransitiva.length;
        double matCantidadInformacion[][]=new double[filas][columnas];
        for(int i=0;i<filas;i++){
            for(int j=0;j<columnas;j++){
                matCantidadInformacion[i][j]=-Math.log(this.matTransitiva[i][j])/Math.log(2);
            }
        }
        return matCantidadInformacion;
    }
    private double[][] calcularMatTranXCantInfo(){
        int filas=this.matTransitiva[0].length;
        int columnas=this.matTransitiva.length;
        double matTranXcantInfo[][]=new double[filas][columnas];
        double matCantInfo[][]=calcularCantidadInformacion();
        for(int i=0;i<filas;i++){
            for(int j=0;j<columnas;j++){
                matTranXcantInfo[i][j]=matTransitiva[i][j]*matCantInfo[i][j];
            }
        }
        return matTranXcantInfo;
    }
    private double[] calcularVecSuma(){
        int filas=this.matTransitiva[0].length;
        int columnas=this.matTransitiva.length;
        double vecSuma[]=new double[filas];
        double matAux[][]=calcularMatTranXCantInfo();
        for(int i=0;i<filas;i++){
            for(int j=0;j<columnas;j++){
                vecSuma[i]+=matAux[i][j];
            }
        }
        return vecSuma;
    }
    private void calcularEntropia(){
        int columnas=this.matTransitiva.length;
        double vecSuma[]=calcularVecSuma();
        double aux=0;
        for(int i=0;i<columnas;i++){
            aux+=this.vecEstacionario.get(i)*vecSuma[i];
        }
        this.entropia=aux;
    }
    private double[][] multiplicarMatrices(double[][] a, double[][] b) {
        double[][] c = new double[a.length][b[0].length];
        if (a[0].length == b.length) {
            for (int i = 0; i < a.length; i++) {
                for (int j = 0; j < b[0].length; j++) {
                    for (int k = 0; k < a[0].length; k++) {
                        c[i][j] += a[i][k] * b[k][j];
                    }
                }
            }
        }
        return c;
    }

}
