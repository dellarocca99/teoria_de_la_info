package paquete;

import java.util.Scanner;

public class Prueba {

    private static final double[] vectorprob = new double[15];
    private static final double[] vectorprobacum = new double[15];
    private static final Scanner lector = new Scanner(System.in);
    private static int cantsimb = 0;
    private static int cantsimbolosgenerar = 0;

    public static void main(String[] args) {
        System.out.println("ingrese cantidad de simbolos de la fuente");
        cantsimb = lector.nextInt();
        System.out.println("ingrese las probabilidades de cada uno de los simbolos");
        for(int i=0; i<cantsimb;i++){
            vectorprob[i] = lector.nextDouble();
            if (i == 0){
                vectorprobacum[i]=vectorprob[i];
            }
            else{
                vectorprobacum[i]=vectorprobacum[i-1]+vectorprob[i];
            }
        }
        double cantinfo,acum = 0;
        for(int i=0; i<cantsimb; i++){
            cantinfo=-Math.log10(vectorprob[i])/Math.log10(2);
            System.out.println("Para el simbolo S"+(i+1)+" la cantidad de informacion es "+ cantinfo);
            acum += cantinfo*vectorprob[i];
        }
        System.out.println("La entropia de la fuente de memoria nula ingresada es: " + acum);
        System.out.println("ingrese cantidad de simbolos a simular");
        cantsimbolosgenerar = lector.nextInt();
        for (int i=0;i<cantsimbolosgenerar;i++){
            double rnd = Math.random();
            int j= 0;
            while ((rnd >= vectorprobacum[j])){
                j++;
            }
            System.out.print("S"+(j+1)+" ");
        }
    }
}
