import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class DictionaryServer extends Thread {
    
    private ServerSocket gniazdoSerwera;  
    private final ArrayList<DictionaryServerClient> pod章czeniKlienci = new ArrayList<>();	
    private HashMap<String, String> s這wnik;
    
    public DictionaryServer(HashMap<String, String> s這wnik) throws IOException {
    	
        gniazdoSerwera = new ServerSocket(0); 
        this.s這wnik = s這wnik;
    }
    
    public int pobierzNumerPortu() {
    	
    	return gniazdoSerwera.getLocalPort();
    }
    
    public String pobierzObcoj瞛yczneS這wo(String s這woT逝mczone) {
    	
    	return s這wnik.get(s這woT逝mczone);
    }
    
    public void usu鄺lienta(DictionaryServerClient klient) {
	
    	pod章czeniKlienci.remove(klient);
	
    	System.out.println("Serwer s這wnikowy: usun像em klienta...");
    	System.out.println("Serwer s這wnikowy: liczba po章czonych klient闚: " + pod章czeniKlienci.size());
    }
    
    @Override
    public void run() {
        while (true) {
        	
            try {
            	
            	System.out.println("Serwer s這wnikowy: oczekuj� na po章czenie...");
            	
                Socket gniazdo = gniazdoSerwera.accept();	// wywo逝jemy metod� blokuj鉍� oczekuj鉍� na po章czenie ze strony klienta
                System.out.println("Serwer s這wnikowy: odebra貫m po章czenie");
          
                DictionaryServerClient klient = new DictionaryServerClient(gniazdo, this);
                System.out.println("Serwer s這wnikowy: utworzy貫m klienta");
                pod章czeniKlienci.add(klient);
                klient.start();
                System.out.println("Serwer s這wnikowy: liczba po章czonych klient闚: " + pod章czeniKlienci.size());
            } catch (IOException ex) {
            	
                //widok.dodajKomunikat("Nieudana pr鏏a nawi頊ania po章czenia z klientem");
            }
        }
    }
}
