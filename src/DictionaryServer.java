import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class DictionaryServer extends Thread {
    
    private ServerSocket gniazdoSerwera;  
    private final ArrayList<DictionaryServiceServerClient> pod章czeniKlienci = new ArrayList<>();	
    private HashMap<String, String> s這wnik;
    
    public DictionaryServer(HashMap<String, String> s這wnik) throws IOException {
    	
        gniazdoSerwera = new ServerSocket(0); 
        this.s這wnik = s這wnik;
    }
    
    /*public void usu鄺lienta(Client klient) {
    	
    	pod章czeniKlienci.remove(klient);
    	
    	System.out.println("Serwer: usun像em klienta...");
    	System.out.println("Serwer: liczba po章czonych klient闚: " + pod章czeniKlienci.size());
    }*/
    
    public int pobierzNumerPortu() {
    	
    	return gniazdoSerwera.getLocalPort();
    }
    
    @Override
    public void run() {
        while (true) {
        	
            try {
            	
            	System.out.println("Serwer s這wnikowy: oczekuj� na po章czenie...");
            	
                Socket gniazdo = gniazdoSerwera.accept();	// wywo逝jemy metod� blokuj鉍� oczekuj鉍a na po章czenie ze strony klienta
                System.out.println("Serwer s這wnikowy: odebra貫m po章czenie");
          
                //DictionaryServiceServerClient klient = new DictionaryServiceServerClient(gniazdo, this);
                //System.out.println("Serwer us逝g: utworzy貫m klienta");
                //pod章czeniKlienci.add(klient);
                //klient.start();
                //System.out.println("Serwer us逝g: liczba po章czonych klient闚: " + pod章czeniKlienci.size());
            } catch (IOException ex) {
            	
                //widok.dodajKomunikat("Nieudana pr鏏a nawi頊ania po章czenia z klientem");
            }
        }
    }
}
