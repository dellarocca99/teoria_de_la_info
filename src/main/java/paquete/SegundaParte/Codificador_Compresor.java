package paquete.SegundaParte;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Codificador_Compresor {
    private List<NodoAux> vectorAuxiliar;
    private String tipoCodificacion;
    private String mensajeSinCodificar;
    private String mensajeCodificado;
    //Constructor
    public Codificador_Compresor() {
        this.vectorAuxiliar=new ArrayList<NodoAux>();
        this.tipoCodificacion="";
    }
    //GETTER Y SETTER
    public List<NodoAux> getVectorAuxiliar() {
        return vectorAuxiliar;
    }
    public void setVectorAuxiliar(List<NodoAux> vectorAuxiliar) {
        this.vectorAuxiliar = vectorAuxiliar;
    }
    public String getTipoCodificacion() {
        return tipoCodificacion;
    }
    public void setTipoCodificacion(String tipoCodificacion) {
        this.tipoCodificacion = tipoCodificacion;
    }
    //PREPARAR CODIFICADOR
    public void prepararCodificador(String mensaje,String tipoCodificacion){
        List<Character> simbolos=new ArrayList<Character>();
        List<Integer> apariciones=new ArrayList<Integer>();
        int totalApariciones=mensaje.length();
        this.vectorAuxiliar=new ArrayList<NodoAux>();
        this.tipoCodificacion=tipoCodificacion;
        for(int i=0;i<mensaje.length();i++){
            Character aux=mensaje.charAt(i);
            if(!simbolos.contains(aux)){
                simbolos.add(aux);
                apariciones.add(1);
            } else {
                apariciones.set(simbolos.indexOf(aux),apariciones.get(simbolos.indexOf(aux))+1);
            }
        }
        Iterator<Character> itSimbolos=simbolos.listIterator();
        Iterator<Integer> itApariciones=apariciones.listIterator();
        while(itSimbolos.hasNext() && itApariciones.hasNext()){
            vectorAuxiliar.add(new NodoAux(String.valueOf(itSimbolos.next()),"",Double.valueOf(itApariciones.next())/totalApariciones));
        }
        this.generarCodificacion();
    }
    public void generarCodificacion(){
        if(this.tipoCodificacion.equals("Huffman")){
            this.generarCodificacionHuffman();
        } else {
            if(this.tipoCodificacion.equals("Shannon")){
                this.generarCodificacionShannon();
            } else {
                if(this.tipoCodificacion.equals("RLC")){
                    this.generarCodificacionRLC();
                }
            }
        }
    }
    //CODIFICACION HUFFMAN
    private void huffmanArmadoPila(){
        Collections.sort(this.vectorAuxiliar);
        while(this.vectorAuxiliar.size()!=1){
            this.vectorAuxiliar.get(1).pushNodoAux(this.vectorAuxiliar.remove(0));
            Collections.sort(this.vectorAuxiliar);
        }
    }
    private void huffmanDesarmadoPila(int tamanoInicial){
        int i;
        while(this.vectorAuxiliar.size()!=tamanoInicial){
            i=0;
            while(i<this.vectorAuxiliar.size()){
                NodoAux aux=this.vectorAuxiliar.get(i).popNodoAux();
                if(aux!=null) {
                    this.vectorAuxiliar.add(aux);
                    this.vectorAuxiliar.get(i).armarCodificacion("0");
                    aux.armarCodificacion("1");
                    Collections.sort(this.vectorAuxiliar);
                    i=this.vectorAuxiliar.size();
                }
                i++;
            }
        }
    }
    private void generarCodificacionHuffman(){
        int tamanoInicial=this.vectorAuxiliar.size();
        this.huffmanArmadoPila();
        this.huffmanDesarmadoPila(tamanoInicial);
    }
    //CODIFICACION SHANON
    private void separarEnDos(List<NodoAux> lista){
        List<NodoAux> lista1=new ArrayList<NodoAux>();
        List<NodoAux> lista2=new ArrayList<NodoAux>();
        double minimo=1.0,acumulador1,acumulador2;
        int indiceMinimo=-1;
        int i=0;
        if(lista.size()!=1){
            Collections.sort(lista);
            while(i<lista.size()){
                acumulador1=0;
                acumulador2=0;
                for(int j=0;j<i;j++){
                    acumulador1+=lista.get(j).getProbabilidad();
                }
                for(int j=i;j<lista.size();j++){
                    acumulador2+=lista.get(j).getProbabilidad();
                }
                if(Math.abs(acumulador1-acumulador2)<minimo){
                    minimo=Math.abs(acumulador1-acumulador2);
                    indiceMinimo=i;
                }
                i++;
            }
            for(int j=0;j<indiceMinimo;j++){
                lista.get(j).armarCodificacion("0");
                lista1.add(lista.get(j));
            }
            for(int j=indiceMinimo;j<lista.size();j++){
                lista.get(j).armarCodificacion("1");
                lista2.add(lista.get(j));
            }
            separarEnDos(lista1);
            separarEnDos(lista2);
        }
    }
    private void generarCodificacionShannon(){
        this.separarEnDos(this.vectorAuxiliar);
    }
    //CODIFICACION RLC
    private void generarCodificacionRLC(){
        Collections.sort(this.vectorAuxiliar);
        for(int i=0;i<this.vectorAuxiliar.size();i++){
            for(int j=0;j<i;j++){
                this.vectorAuxiliar.get(i).armarCodificacion("0");
            }
        }
    }
    //CODIFICAR MENSAJE
    public String codificarMensaje(String mensaje){
        String mensajeCodificado="";
        if(this.tipoCodificacion.equals("Huffman") || this.tipoCodificacion.equals("Shannon")){
            mensajeCodificado=this.codificarMensajeHuffmanShanon(mensaje);
        } else {
            if(this.tipoCodificacion.equals("RLC")){
                mensajeCodificado=this.codificarMensajeRLC(mensaje);
            }
        }
        this.mensajeSinCodificar=mensaje;
        this.mensajeCodificado=mensajeCodificado;
        return mensajeCodificado;
    }
    private String codificarMensajeRLC(String mensaje){
        String mensajeCodificado = "";
        int i = 0;
        while (i < mensaje.length()) {
            boolean iguales = true;
            int cont = 1;
            while (iguales) {
                if (i+1<mensaje.length() && mensaje.charAt(i) == mensaje.charAt(i + 1)) {
                    cont += 1;
                    i += 1;
                } else {
                    iguales = false;
                }
            }
            Iterator<NodoAux> itAux = vectorAuxiliar.iterator();
            boolean encontrado = false;
            while (itAux.hasNext() && !encontrado) {
                NodoAux actual = itAux.next();
                if (actual.getSimbolo().equals(String.valueOf(mensaje.charAt(i)))) {
                    mensajeCodificado += actual.getCodificacion();
                    encontrado = true;
                }
            }
            for(int j=0;j<cont;j++){
                mensajeCodificado+="1";
            }
            i++;
        }
        return mensajeCodificado;
    }
    private String codificarMensajeHuffmanShanon(String mensaje){
        String mensajeCodificado="";
        for(int i=0;i<mensaje.length();i++){
            Character aux=mensaje.charAt(i);
            Iterator<NodoAux> itAux=vectorAuxiliar.iterator();
            boolean encontrado=false;
            while(itAux.hasNext() && !encontrado){
                NodoAux actual= itAux.next();
                if(actual.getSimbolo().equals(String.valueOf(aux))){
                    mensajeCodificado+=actual.getCodificacion();
                    encontrado=true;
                }
            }
        }
        return mensajeCodificado;
    }
    //DECODIFICAR MENSAJE
    public String decodificarMensaje(String mensaje) {
        String mensajeDecodificado="";
        if(this.tipoCodificacion.equals("Huffman") || this.tipoCodificacion.equals("Shannon")){
            mensajeDecodificado=this.decodificarMensajeHuffmanShanon(mensaje);
        } else {
            if(this.tipoCodificacion.equals("RLC")){
                mensajeDecodificado=this.decodificarMensajeRLC(mensaje);
            }
        }
        return mensajeDecodificado;
    }
    private String decodificarMensajeHuffmanShanon(String mensaje){
        String mensajeDecodificado="";
        String letraActual="";
        NodoAux nodoLetraActual=null;
        int i=0;
        while(i<mensaje.length()){
            letraActual+=mensaje.charAt(i);
            Iterator<NodoAux> it=vectorAuxiliar.iterator();
            boolean encontrado=false;
            while(it.hasNext() && !encontrado){
                nodoLetraActual=it.next();
                if(nodoLetraActual.getCodificacion().equals(letraActual)){
                    encontrado=true;
                }
            }
            if(encontrado){
                mensajeDecodificado+=nodoLetraActual.getSimbolo();
                letraActual="";
            }
            i++;
        }
        return mensajeDecodificado;
    }
    private String decodificarMensajeRLC(String mensaje){
        int i=0;
        String letraActual;
        String mensajeDecodificado="";
        while(i<mensaje.length()){
            int cont=0;
            letraActual="";
            while(mensaje.charAt(i)=='0'){
                letraActual+=mensaje.charAt(i);
                i++;
            }
            while(i<mensaje.length() && mensaje.charAt(i)=='1'){
                cont+=1;
                i++;
            }
            Iterator<NodoAux> it=vectorAuxiliar.iterator();
            boolean encontrado=false;
            while(it.hasNext() && !encontrado){
                NodoAux actual=it.next();
                if(actual.getCodificacion().equals(letraActual)){
                    encontrado=true;
                    for(int j=0;j<cont;j++){
                        mensajeDecodificado+=actual.getSimbolo();
                    }
                }
            }
        }
        return mensajeDecodificado;
    }

    //CALCULOS
    public List<Double> calcularCantidadInformacion(){
        List<Double> cantInfo=new ArrayList<Double>();
        for(int i=0;i<vectorAuxiliar.size();i++){
            cantInfo.add(-Math.log(vectorAuxiliar.get(i).getProbabilidad()) / Math.log(2));
        }
        return cantInfo;
    }
    private Double calcularEntropia(){
        double aux=0;
        List<Double> cantInfo=calcularCantidadInformacion();
        for(int i=0;i<vectorAuxiliar.size();i++){
            aux+=vectorAuxiliar.get(i).getProbabilidad()*cantInfo.get(i);
        }
        return aux;
    }
    private Double calcularLongitudMedia(){
        double aux=0;
        for(int i=0;i<vectorAuxiliar.size();i++){
            aux+=vectorAuxiliar.get(i).getProbabilidad()*vectorAuxiliar.get(i).getCodificacion().length();
        }
        return aux;
    }
    private boolean verificarCompacto(){
        List<Integer> longitud_minima=new ArrayList<Integer>();;
        for(int i=0;i<vectorAuxiliar.size();i++){
            longitud_minima.add((int) Math.ceil(-Math.log(vectorAuxiliar.get(i).getProbabilidad())/Math.log(2)));
        }
        int i=0;
        boolean esComp=true;
        while(i<longitud_minima.size() && esComp){
            if(vectorAuxiliar.get(i).getCodificacion().length()>longitud_minima.get(i)){
                esComp=false;
            }
            i++;
        }
        return esComp;
    }
    private double calcularKraft(){
        double aux=0;
        for(int i=0;i<vectorAuxiliar.size();i++){
            aux+=Math.pow(2,-vectorAuxiliar.get(i).getCodificacion().length());
        }
        return aux;
    }

    public double calcularRendimiento(){
        double entropia=this.calcularEntropia();
        double longitudMedia=this.calcularLongitudMedia();
        return entropia/longitudMedia;
    }
    public double calcularRedundancia(){
        return 1-this.calcularRendimiento();
    }
    public double calcularTasaCompresion(){
        return 1.0*this.mensajeSinCodificar.length()*8/this.mensajeCodificado.length();
    }
}
