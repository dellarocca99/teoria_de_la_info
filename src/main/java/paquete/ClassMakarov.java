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
        
       
        for(int t=0; t<N; t++) {
    		double[][]mataux= new double[this.matTransitiva[0].length][this.matTransitiva[0].length];
    		for(int i=0; i<this.matTransitiva[0].length; i++) {
    			for(int j=0; j<this.matTransitiva[0].length; j++) {
    				mataux[i][j]= matTransitiva[i][0]*mat[0][j];
    				for(int cont=1; cont<cantestados; cont++) {
    					mataux[i][j]= mataux[i][j] + matTransitiva[i][cont]*matTransitiva[cont][j]; 
    				}
    			}
    		}
    		for(int i=0; i<this.matTransitiva[0].length; i++) {
    			for(int j=0; j<this.matTransitiva[0].length; j++) {
    				matTransitiva[i][j]= mataux[i][j];
    			}
    		}
    	}

        int i=0,j=0;
        double aux1,aux2;
        boolean follow=true;
        while(i<this.matTransitiva[0].length && follow){
            while(j<this.matTransitiva.length-1 && follow){
                aux1=matTransitiva[i][j];
                aux2=matTransitiva[i][j+1];
                if(Math.abs(aux2-aux1)>exactitud){
                    follow=false;
                }
                j++;
            }
            i++;
        }
        if(follow){
            for(int i=0;i<this.matTransitiva[0].length;i++){
                vecEstacionario.add(matTransitiva[i][1]);
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

}
