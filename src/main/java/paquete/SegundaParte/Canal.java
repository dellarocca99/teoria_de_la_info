package paquete.SegundaParte;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class Canal {
    private Receptor receptor;
    private Ruido ruido;

    private String mensaje;

    private List<Double> probEntrada;
    private List<Double> probSalida;
    double [][] matriz_b_a;
    double [][] matriz_a_b;

    double entropia_A;
    double entropia_B;
    double H_A_B;
    double H_B_A;
    double I_A_B;
    double I_B_A;
    double entropiaAfin_AB;


    //CONSTRUCTOR
    public Canal(Receptor receptor, Ruido ruido) {
        this.receptor = receptor;
        this.ruido=ruido;
    }
    //GETTER Y SETTER
    public Receptor getReceptor() {
        return receptor;
    }
    public void setReceptor(Receptor receptor) {
        this.receptor = receptor;
    }
    public Ruido getRuido() {
        return ruido;
    }
    public void setRuido(Ruido ruido) {
        this.ruido = ruido;
    }
    public String getMensaje() {
        return mensaje;
    }
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public void moverMensaje(String mensaje){
        this.mensaje=mensaje;
        this.receptor.recibirMensaje(mensaje);
    }

    public void readFile(String nombreArchivo){
        File f=null;
        FileReader fr=null;
        BufferedReader br=null;
        try {
            f=new File(nombreArchivo);
            fr=new FileReader(f);
            br=new BufferedReader(fr);
            String linea;
            linea=br.readLine();
            int aux=Integer.valueOf(linea);
            this.probEntrada=new LinkedList<Double>();
            for(int i=0;i<aux;i++){
                linea=br.readLine();
                this.probEntrada.add(Double.valueOf(linea));
            }
            linea=br.readLine();
            String[] aux1=linea.split("x");
            int filas=Integer.valueOf(aux1[0]);
            int columnas=Integer.valueOf(aux1[1]);
            this.matriz_b_a =new double[filas][columnas];
            int i=0;
            while((linea=br.readLine())!=null && i<filas){
                String[] aux2=linea.split(" ");
                for(int j=0;j<columnas;j++){
                    this.matriz_b_a[i][j]=Double.valueOf(aux2[j]);
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

    public void realizarCalculos(){
        this.calcularProbabilidadesSalida();
        this.calcular_entropia_a();
        this.calcular_entropia_b();
        this.calcularMatrizA_B();
        this.calcularEntropiaAfin();
        this.calcularH_A_B();
        this.calcularH_B_A();
        this.calcularI_A_B();
        this.calcularI_B_A();
    }

    public String informacionCanal(){
        String aux=
                "H(A) = "+ entropia_A +" ---> Entropia entrada"+"\n"+
                "H(B) = "+ entropia_B +" ---> Entropia salida"+"\n"+
                "H(A,B) = "+entropiaAfin_AB+" ---> Entropia afin"+"\n"+
                "H(A/B) = "+H_A_B+" ---> Ruido/Equivocacion"+"\n"+
                "H(B/A) = "+H_B_A+" ---> Perdida"+"\n"+
                "I(A,B) = "+I_A_B+" ---> Informacion mutua"+"\n"+
                "I(B,A) = "+I_B_A+" ---> Informacion mutua"+"\n"+
                "Propiedades informacion mutua:\n"+
                "1) I(A,B) >= 0 ---> "+I_A_B+" >= 0"+"\n"+
                "2) I(A,B) = I(B,A) ---> I(A,B) = "+I_A_B+" I(B,A) = "+I_B_A+" ---> "+I_A_B+" = "+I_B_A+"\n"+
                "3) H(A,B) = H(A) + H(B) - I(A,B) ---> H(A,B) = "+entropiaAfin_AB+" H(A) + H(B) - I(A,B) = "+
                entropia_A+" + "+entropia_B+" - "+I_A_B+" = "+String.valueOf(entropia_A+entropia_B-I_A_B)+
                " ---> "+entropiaAfin_AB+" = "+String.valueOf(entropia_A+entropia_B-I_A_B);
        return aux;
    }

    private double calcular_entropia(List<Double> lista){
        double suma=0;
        for(int j=0;j<lista.size();j++){
            suma+=lista.get(j)*(-Math.log(lista.get(j))/Math.log(2));
        }
        return suma;
    }
    private void calcular_entropia_a(){
        this.entropia_A=calcular_entropia(this.probEntrada);
    }
    private void calcular_entropia_b(){
        this.entropia_B=calcular_entropia(this.probSalida);
    }
    private void calcularProbabilidadesSalida(){
        List<Double> aux=new LinkedList<Double>();
        double suma;
        for(int j=0;j<this.matriz_b_a[0].length;j++){
            suma=0;
            for(int i=0;i<this.matriz_b_a.length;i++){
                suma+=this.probEntrada.get(i)*this.matriz_b_a[i][j];
            }
            aux.add(suma);
        }
        this.probSalida=aux;
    }
    private void calcularMatrizA_B(){
        double [][]aux=new double[this.probEntrada.size()][this.probSalida.size()];
        for(int i=0;i<aux.length;i++){
            for(int j=0;j<aux[0].length;j++){
                aux[i][j]=(this.matriz_b_a[i][j]*this.probEntrada.get(i))/this.probSalida.get(j);
            }
        }
        this.matriz_a_b=aux;
    }
    private void calcularH_A_B(){
        double suma=0;
        for(int i=0;i<this.matriz_a_b.length;i++){
            for(int j=0;j<this.matriz_a_b[0].length;j++){
                if(this.matriz_a_b[i][j]!=0) {
                    suma += (this.matriz_a_b[i][j] * this.probSalida.get(j)) * (-Math.log(this.matriz_a_b[i][j]) / Math.log(2));
                }
            }
        }
        this.H_A_B=suma;
    }
    private void calcularH_B_A(){
        double suma=0;
        for(int i=0;i<this.matriz_b_a.length;i++){
            for(int j=0;j<this.matriz_b_a[0].length;j++){
                if(this.matriz_b_a[i][j]!=0) {
                    suma += (this.matriz_a_b[i][j] * this.probSalida.get(j)) * (-Math.log(this.matriz_b_a[i][j]) / Math.log(2));
                }
            }
        }
        this.H_B_A=suma;
    }
    private void calcularI_A_B(){
        this.I_A_B=this.entropia_A-this.H_A_B;
        //double suma=0;
        //for(int i=0;i<this.matriz_a_b.length;i++){
        //    for(int j=0;j<this.matriz_a_b[0].length;j++){
        //        suma+=(this.matriz_a_b[i][j]*this.probSalida.get(j))*(Math.log(this.matriz_a_b[i][j]*this.probSalida.get(j)/(this.probSalida.get(j)*this.probEntrada.get(i)))/Math.log(2));
        //    }
        //}
        //this.I_A_B=suma;
    }
    private void calcularI_B_A(){
        this.I_B_A=this.entropia_B-this.H_B_A;
        //double suma=0;
        //for(int i=0;i<this.matriz_a_b.length;i++){
        //    for(int j=0;j<this.matriz_a_b[0].length;j++){
        //        suma+=(this.matriz_a_b[i][j]*this.probSalida.get(j))*(Math.log(this.matriz_a_b[i][j]*this.probSalida.get(j)/(this.probSalida.get(j)*this.probEntrada.get(i)))/Math.log(2));
        //    }
        //}
        //this.I_A_B=suma;
    }
    private void calcularEntropiaAfin(){
        double suma=0;
        for(int i=0;i<this.matriz_a_b.length;i++){
            for(int j=0;j<this.matriz_a_b[0].length;j++){
                if(this.matriz_a_b[i][j]!=0) {
                    suma += (this.matriz_a_b[i][j] * this.probSalida.get(j)) * (-Math.log(this.matriz_a_b[i][j]*this.probSalida.get(j)) / Math.log(2));
                }
            }
        }
        this.entropiaAfin_AB=suma;
    }
}
