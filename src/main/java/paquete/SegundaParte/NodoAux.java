package paquete.SegundaParte;

import java.util.Stack;

public class NodoAux implements Comparable<NodoAux>{
    private String simbolo;
    private String codificacion;
    private Double probabilidad;
    private Stack<NodoAux> pilaSiguiente;
    private NodoAux izq;
    private NodoAux der;
    //CONSTRUCTOR
    public NodoAux(String simbolo, String codificacion, Double probabilidad) {
        this.simbolo = simbolo;
        this.codificacion = codificacion;
        this.probabilidad = probabilidad;
        this.pilaSiguiente = new Stack<NodoAux>();
        this.izq=null;
        this.der=null;
    }

    public NodoAux(NodoAux izq, NodoAux der){
        this(izq.simbolo+" , "+der.simbolo,"",izq.probabilidad+der.probabilidad);
        this.izq=izq;
        this.der=der;
    }
    public NodoAux getIzq(){
        return this.izq;
    }
    public NodoAux getDer(){
        return this.der;
    }
    //GETTER Y SETTER
    public String getSimbolo() {
        return simbolo;
    }
    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }
    public String getCodificacion() {
        return codificacion;
    }
    public void setCodificacion(String codificacion) {
        this.codificacion = codificacion;
    }
    public Double getProbabilidad() {
        return probabilidad;
    }
    public void setProbabilidad(Double probabilidad) {
        this.probabilidad = probabilidad;
    }
    public Stack<NodoAux> getPilaSiguiente() {
        return pilaSiguiente;
    }
    public void setPilaSiguiente(Stack<NodoAux> pilaSiguiente) {
        this.pilaSiguiente = pilaSiguiente;
    }
    //OTROS
    public void pushNodoAux(NodoAux siguiente){
        pilaSiguiente.push(siguiente);

    }
    public NodoAux popNodoAux(){
        NodoAux aux;
        if(!this.pilaSiguiente.empty()){
            aux=pilaSiguiente.pop();
        }else{
            aux=null;
        }
        return aux;
    }

    public double probNodo(){
        double acum=this.probabilidad;
        //Stack<NodoAux> aux=new Stack<NodoAux>();
        //while(!this.pilaSiguiente.empty()){
        //    acum+=this.pilaSiguiente.peek().getProbabilidad();
        //    aux.push(pilaSiguiente.pop());
        //}
        //while(!aux.empty()){
        //    this.pilaSiguiente.push(aux.pop());
        //}
        return acum;
    }
    public void armarCodificacion(String bit){
        this.codificacion=this.codificacion+bit;
        Stack<NodoAux> aux=new Stack<NodoAux>();
        while(!this.pilaSiguiente.empty()){
            this.pilaSiguiente.peek().armarCodificacion(bit);
            aux.push(pilaSiguiente.pop());
        }
        while(!aux.empty()){
            this.pilaSiguiente.push(aux.pop());
        }
    }
    //SOBRE-ESCRITOS
    @Override
    public int compareTo(NodoAux o) {
        double aux1=this.probNodo();
        double aux2=o.probNodo();
        if(aux1>aux2){
            return 1;
        }else{
            if(aux2>aux1){
                return -1;
            } else {
                if(this.codificacion.length()<o.codificacion.length()){
                    return 1;
                }else{
                    if(this.codificacion.length()>o.codificacion.length()){
                        return -1;
                    }else{
                        if(this.izq!=null && this.der!=null && o.izq==null && o.der==null){
                            return 1;
                        }else{
                            if(this.izq==null && this.der==null && o.izq!=null && o.der!=null){
                                return -1;
                            }else{
                                if(this.simbolo.charAt(0)>o.simbolo.charAt(0)){
                                    return 1;
                                }else{
                                    if(this.simbolo.charAt(0)<o.simbolo.charAt(0)){
                                        return -1;
                                    }else{
                                        return 0;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
