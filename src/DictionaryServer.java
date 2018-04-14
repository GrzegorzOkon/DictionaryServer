import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Klasa serwera obs³uguj¹cego konkretny jêzyk wybrany podczas inicjalizacji obieketu.
 * 
 */
public class DictionaryServer extends Thread {
    
    private ServerSocket gniazdoSerwera;   //gniazdo na którym dzia³a serwer
    private final ArrayList<DictionaryServerClient> pod³¹czeniKlienci = new ArrayList<>();   //pod³aczenie klienci do tego serwera
    private HashMap<String, String> s³ownik;   //przechowuje s³owa polskie i odpowiedniki w innym jêzyku
    
    public DictionaryServer(HashMap<String, String> s³ownik) throws IOException {
    	
        gniazdoSerwera = new ServerSocket(0);   //tworzy serwer na wolnym porcie wybranym przez system
        this.s³ownik = s³ownik;
    }
    
    /**
     * Metoda zwraca numr portu dla klasy klienta obs³uguj¹cego po³aczenia do serwera s³ownikowego.
     * 
     */
    public int pobierzNumerPortu() {
    	
    	return gniazdoSerwera.getLocalPort();
    }
    
    /**
     * Wybiera ze s³ownika szukany wyraz.
     * 
     * @param s³owoT³umczone polskie s³owo
     * 
     * @return t³umaczenie lub null jeœli nie wystêpuje
     */
    public String pobierzObcojêzyczneS³owo(String s³owoT³umczone) {
    	
    	return s³ownik.get(s³owoT³umczone);
    }
    
    /**
     * Usuwa klienta z listy monitoruj¹cej pracê serwera.
     * 
     * @param klient po³¹czony klient obsluguj¹cy klienta serwera us³ugi s³ownikowej
     */
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
            	
            }
        }
    }
}
