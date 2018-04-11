import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class DictionaryServer extends Thread {
    
    private ServerSocket gniazdoSerwera;  
    private final ArrayList<DictionaryServerClient> pod³¹czeniKlienci = new ArrayList<>();	
    private HashMap<String, String> s³ownik;
    
    public DictionaryServer(HashMap<String, String> s³ownik) throws IOException {
    	
        gniazdoSerwera = new ServerSocket(0); 
        this.s³ownik = s³ownik;
    }
    
    public int pobierzNumerPortu() {
    	
    	return gniazdoSerwera.getLocalPort();
    }
    
    public String pobierzObcojêzyczneS³owo(String s³owoT³umczone) {
    	
    	return s³ownik.get(s³owoT³umczone);
    }
    
    public void usuñKlienta(DictionaryServerClient klient) {
	
    	pod³¹czeniKlienci.remove(klient);
	
    	System.out.println("Serwer s³ownikowy: usun¹³em klienta...");
    	System.out.println("Serwer s³ownikowy: liczba po³¹czonych klientów: " + pod³¹czeniKlienci.size());
    }
    
    @Override
    public void run() {
        while (true) {
        	
            try {
            	
            	System.out.println("Serwer s³ownikowy: oczekujê na po³¹czenie...");
            	
                Socket gniazdo = gniazdoSerwera.accept();	// wywo³ujemy metodê blokuj¹c¹ oczekuj¹c¹ na po³¹czenie ze strony klienta
                System.out.println("Serwer s³ownikowy: odebra³em po³¹czenie");
          
                DictionaryServerClient klient = new DictionaryServerClient(gniazdo, this);
                System.out.println("Serwer s³ownikowy: utworzy³em klienta");
                pod³¹czeniKlienci.add(klient);
                klient.start();
                System.out.println("Serwer s³ownikowy: liczba po³¹czonych klientów: " + pod³¹czeniKlienci.size());
            } catch (IOException ex) {
            	
                //widok.dodajKomunikat("Nieudana próba nawi¹zania po³¹czenia z klientem");
            }
        }
    }
}
