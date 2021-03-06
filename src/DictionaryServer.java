import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Klasa serwera obs逝guj鉍ego konkretny j瞛yk wybrany podczas inicjalizacji obieketu.
 * 
 */
public class DictionaryServer extends Thread {
    
    private ServerSocket gniazdoSerwera;   //gniazdo na kt鏎ym dzia豉 serwer
    private final ArrayList<DictionaryServerClient> pod章czeniKlienci = new ArrayList<>();   //pod豉czenie klienci do tego serwera
    private HashMap<String, String> s這wnik;   //przechowuje s這wa polskie i odpowiedniki w innym j瞛yku
    
    public DictionaryServer(HashMap<String, String> s這wnik) throws IOException {
    	
        gniazdoSerwera = new ServerSocket(0);   //tworzy serwer na wolnym porcie wybranym przez system
        this.s這wnik = s這wnik;
    }
    
    /**
     * Metoda zwraca numr portu dla klasy klienta obs逝guj鉍ego po豉czenia do serwera s這wnikowego.
     * 
     */
    public int pobierzNumerPortu() {
    	
    	return gniazdoSerwera.getLocalPort();
    }
    
    /**
     * Wybiera ze s這wnika szukany wyraz.
     * 
     * @param s這woT逝mczone polskie s這wo
     * 
     * @return t逝maczenie lub null je�li nie wyst瘼uje
     */
    public String pobierzObcoj瞛yczneS這wo(String s這woT逝mczone) {
    	
    	return s這wnik.get(s這woT逝mczone);
    }
    
    /**
     * Usuwa klienta z listy monitoruj鉍ej prac� serwera.
     * 
     * @param klient po章czony klient obsluguj鉍y klienta serwera us逝gi s這wnikowej
     */
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
            	
            }
        }
    }
}
