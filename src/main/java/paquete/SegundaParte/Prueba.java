package paquete.SegundaParte;

public class Prueba {

    public static void main(String[] args) {
        ModeloComunicacion mc=new ModeloComunicacion();
        primeraParte(mc);
        segundaParte(mc);
    }

    private static void primeraParte(ModeloComunicacion mc){
        mc.iniciarSimulacionEnvioMensaje(".\\mdp-español\\mdp-español.txt","Huffman");
        mc.iniciarSimulacionEnvioMensaje(".\\mdp-español\\mdp-español.txt","Shannon");
        mc.iniciarSimulacionEnvioMensaje(".\\mdp-español\\mdp-español.txt","RLC");
        mc.iniciarSimulacionEnvioMensaje(".\\mdp-danes\\mdp-danes.txt","Huffman");
        mc.iniciarSimulacionEnvioMensaje(".\\mdp-danes\\mdp-danes.txt","Shannon");
        mc.iniciarSimulacionEnvioMensaje(".\\mdp-danes\\mdp-danes.txt","RLC");
    }
    private static void segundaParte(ModeloComunicacion mc){
        mc.iniciarCalculosCanal(".\\Canales\\ejemplo.txt");
        mc.iniciarCalculosCanal(".\\Canales\\canal1.txt");
        mc.iniciarCalculosCanal(".\\Canales\\canal2.txt");
        mc.iniciarCalculosCanal(".\\Canales\\canal3.txt");
    }
}
