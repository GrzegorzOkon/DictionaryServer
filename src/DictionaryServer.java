import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Klasa serwera obs�uguj�cego konkretny j�zyk wybrany podczas inicjalizacji obieketu.
 * 
 */
public class DictionaryServer extends Thread {
    
    private ServerSocket gniazdoSerwera;   //gniazdo na kt�rym dzia�a serwer
    private final ArrayList<DictionaryServerClient> pod��czeniKlienci = new ArrayList<>();   //pod�aczenie klienci do tego serwera
    private HashMap<String, String> s�ownik;   //przechowuje s�owa polskie i odpowiedniki w innym j�zyku
    
    public DictionaryServer(HashMap<String, String> s�ownik) throws IOException {
    	
        gniazdoSerwera = new ServerSocket(0);   //tworzy serwer na wolnym porcie wybranym przez system
        this.s�ownik = s�ownik;
    }
    
    /**
     * Metoda zwraca numr portu dla klasy klienta obs�uguj�cego po�aczenia do serwera s�ownikowego.
     * 
     */
    public int pobierzNumerPortu() {
    	
    	return gniazdoSerwera.getLocalPort();
    }
    
    /**
     * Wybiera ze s�ownika szukany wyraz.
     * 
     * @param s�owoT�umczone polskie s�owo
     * 
     * @return t�umaczenie lub null je�li nie wyst�puje
     */
    public String pobierzObcoj�zyczneS�owo(String s�owoT�umczone) {
    	
    	return s�ownik.get(s�owoT�umczone);
    }
    
    /**
     * Usuwa klienta z listy monitoruj�cej prac� serwera.
     * 
     * @param klient po��czony klient obsluguj�cy klienta serwera us�ugi s�ownikowej
     */
    public void usu�Klienta(DictionaryServerClient klient) {
	
    	pod��czeniKlienci.remove(klient);
	
    	System.out.println("Serwer s�ownikowy: usun��em klienta...");
    	System.out.println("Serwer s�ownikowy: liczba po��czonych klient�w: " + pod��czeniKlienci.size());
    }
    
    @Override
    public void run() {
        while (true) {
        	
            try {
            	
            	System.out.println("Serwer s�ownikowy: oczekuj� na po��czenie...");
            	
                Socket gniazdo = gniazdoSerwera.accept();	// wywo�ujemy metod� blokuj�c� oczekuj�c� na po��czenie ze strony klienta
                System.out.println("Serwer s�ownikowy: odebra�em po��czenie");
          
                DictionaryServerClient klient = new DictionaryServerClient(gniazdo, this);
                System.out.println("Serwer s�ownikowy: utworzy�em klienta");
                pod��czeniKlienci.add(klient);
                klient.start();
                System.out.println("Serwer s�ownikowy: liczba po��czonych klient�w: " + pod��czeniKlienci.size());
            } catch (IOException ex) {
            	
            }
        }
    }
}
