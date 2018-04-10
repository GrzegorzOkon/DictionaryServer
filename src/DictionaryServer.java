import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class DictionaryServer extends Thread {
    
    private ServerSocket gniazdoSerwera;  
    private final ArrayList<DictionaryServiceServerClient> pod³¹czeniKlienci = new ArrayList<>();	
    private HashMap<String, String> s³ownik;
    
    public DictionaryServer(HashMap<String, String> s³ownik) throws IOException {
    	
        gniazdoSerwera = new ServerSocket(0); 
        this.s³ownik = s³ownik;
    }
    
    /*public void usuñKlienta(Client klient) {
    	
    	pod³¹czeniKlienci.remove(klient);
    	
    	System.out.println("Serwer: usun¹³em klienta...");
    	System.out.println("Serwer: liczba po³¹czonych klientów: " + pod³¹czeniKlienci.size());
    }*/
    
    public int pobierzNumerPortu() {
    	
    	return gniazdoSerwera.getLocalPort();
    }
    
    @Override
    public void run() {
        while (true) {
        	
            try {
            	
            	System.out.println("Serwer s³ownikowy: oczekujê na po³¹czenie...");
            	
                Socket gniazdo = gniazdoSerwera.accept();	// wywo³ujemy metodê blokuj¹c¹ oczekuj¹ca na po³¹czenie ze strony klienta
                System.out.println("Serwer s³ownikowy: odebra³em po³¹czenie");
          
                //DictionaryServiceServerClient klient = new DictionaryServiceServerClient(gniazdo, this);
                //System.out.println("Serwer us³ug: utworzy³em klienta");
                //pod³¹czeniKlienci.add(klient);
                //klient.start();
                //System.out.println("Serwer us³ug: liczba po³¹czonych klientów: " + pod³¹czeniKlienci.size());
            } catch (IOException ex) {
            	
                //widok.dodajKomunikat("Nieudana próba nawi¹zania po³¹czenia z klientem");
            }
        }
    }
}
