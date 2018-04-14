import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * G³ówna klasa uruchamiaj¹ca program oraz tworz¹ca serwer oczekuj¹cy na po³¹czenia.
 * 
 */
public class DictionaryServiceServer extends Thread {
    
    private ServerSocket gniazdoSerwera;  
    private final int PORT = 1300;
    private final ArrayList<DictionaryServiceServerClient> pod³¹czeniKlienci = new ArrayList<>();	//przechowuje obiekty obs³ugu¹ce po³¹czonych klientów
    private final HashMap<String, Integer> pod³¹czoneSerweryS³ownikowe = new HashMap<>();	//przechowuje porty na których dzia³aj¹ serwery s³ownikowe w formie np. "en, 4000"
        
    /**
     * Konstruktor tworz¹cy serwer na porcie 1300
     * 
     * @throws IOException jeœli wyst¹pi¹ b³êdy I/O podczas otwierania gniazda
     */
    public DictionaryServiceServer() throws IOException {
    	
        gniazdoSerwera = new ServerSocket(PORT);       
    }
    
    public static void main (String[] args) {
    	
        Socket socket;
        ObjectInputStream ois;
        ObjectOutputStream oos;
        
    	try {
    		
    		DictionaryServiceServer serwerUs³ugiS³ownikowej = new DictionaryServiceServer();
    		serwerUs³ugiS³ownikowej.start();	//uruchamia w¹tek serwera g³ównego
    		
    		serwerUs³ugiS³ownikowej.dodajSerwer("de", new HashMap<String, String>() {{put("pies","Hund"); put("kot","Katze");}});
    		serwerUs³ugiS³ownikowej.dodajSerwer("en", new HashMap<String, String>() {{put("pies","dog"); put("kot","cat");}});
    		serwerUs³ugiS³ownikowej.dodajSerwer("fr", new HashMap<String, String>() {{put("pies","chien"); put("kot","chat");}});
    	} catch (IOException ex) {
    		
    		ex.printStackTrace();
    	} catch (Exception ex) {
    		
    		ex.printStackTrace();
    	}
    }
    
    /**
     * Dodaje i uruchamia serwer do obs³ugi konkretnego jêzyka.
     * 
     * @param kod oznacza kod jêzyka
     * @param s³ownik wprowadza listê s³ów z t³umaczeniami
     */
    private void dodajSerwer(String kod, HashMap<String, String> s³ownik) {

    	try {
    		
    		DictionaryServer serwerS³ownikowy = new DictionaryServer(s³ownik);
    		System.out.println("Serwer us³ug: utworzy³em serwer s³ownika: " + "'" + kod + "'" + " na porcie: " + serwerS³ownikowy.pobierzNumerPortu());
    		pod³¹czoneSerweryS³ownikowe.put(kod, serwerS³ownikowy.pobierzNumerPortu());   //dodaje serwer do mapy monitoruj¹cej uruchomione serwery
    		serwerS³ownikowy.start();
    		System.out.println("Serwer us³ug: liczba utworzonych serwerów s³ownikowych: " + pod³¹czoneSerweryS³ownikowe.size());
    	} catch (IOException ex) {
    		
    		ex.printStackTrace();
    	}
    }
    
    /**
     * Pobiera numer portu uruchomionego serwera w celu pod³¹czenia na podanym porcie.
     * 
     * @param kod oznacza kod jêzyka
     * 
     * @return port wybranego serwera s³ownikowego
     */
    public Integer pobierzPortSerweraS³ownikowego(String kod) {
    	
    	return pod³¹czoneSerweryS³ownikowe.get(kod);
    }
    
    public void usuñKlienta(DictionaryServiceServerClient klient) {
    	
    	pod³¹czeniKlienci.remove(klient);
    	
    	System.out.println("Serwer us³ug: usun¹³em klienta");
    	System.out.println("Serwer us³ug: liczba po³¹czonych klientów: " + pod³¹czeniKlienci.size());
    }
    
    @Override
    public void run() {
        while (true) {
        	
            try {
            	
                System.out.println("Serwer us³ug: oczekujê na po³¹czenie...");
                Socket gniazdo = gniazdoSerwera.accept();   // wywo³ujemy metodê blokuj¹c¹ oczekuj¹ca na po³¹czenie ze strony klienta
                System.out.println("Serwer us³ug: odebra³em po³¹czenie");
                DictionaryServiceServerClient klient = new DictionaryServiceServerClient(gniazdo, this);
                System.out.println("Serwer us³ug: utworzy³em klienta");
                pod³¹czeniKlienci.add(klient);
                klient.start();
                System.out.println("Serwer us³ug: liczba po³¹czonych klientów: " + pod³¹czeniKlienci.size());
            } catch (IOException ex) {
            	
            }
        }
    }    
    
}
