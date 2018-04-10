import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class DictionaryServer extends Thread {
    
    private ServerSocket gniazdoSerwera;  
    private final ArrayList<DictionaryServiceServerClient> pod��czeniKlienci = new ArrayList<>();	
    private HashMap<String, String> s�ownik;
    
    public DictionaryServer(HashMap<String, String> s�ownik) throws IOException {
    	
        gniazdoSerwera = new ServerSocket(0); 
        this.s�ownik = s�ownik;
    }
    
    /*public void usu�Klienta(Client klient) {
    	
    	pod��czeniKlienci.remove(klient);
    	
    	System.out.println("Serwer: usun��em klienta...");
    	System.out.println("Serwer: liczba po��czonych klient�w: " + pod��czeniKlienci.size());
    }*/
    
    public int pobierzNumerPortu() {
    	
    	return gniazdoSerwera.getLocalPort();
    }
    
    @Override
    public void run() {
        while (true) {
        	
            try {
            	
            	System.out.println("Serwer s�ownikowy: oczekuj� na po��czenie...");
            	
                Socket gniazdo = gniazdoSerwera.accept();	// wywo�ujemy metod� blokuj�c� oczekuj�ca na po��czenie ze strony klienta
                System.out.println("Serwer s�ownikowy: odebra�em po��czenie");
          
                //DictionaryServiceServerClient klient = new DictionaryServiceServerClient(gniazdo, this);
                //System.out.println("Serwer us�ug: utworzy�em klienta");
                //pod��czeniKlienci.add(klient);
                //klient.start();
                //System.out.println("Serwer us�ug: liczba po��czonych klient�w: " + pod��czeniKlienci.size());
            } catch (IOException ex) {
            	
                //widok.dodajKomunikat("Nieudana pr�ba nawi�zania po��czenia z klientem");
            }
        }
    }
}
