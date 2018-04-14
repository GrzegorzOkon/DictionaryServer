import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * G��wna klasa uruchamiaj�ca program oraz tworz�ca serwer oczekuj�cy na po��czenia.
 * 
 */
public class DictionaryServiceServer extends Thread {
    
    private ServerSocket gniazdoSerwera;  
    private final int PORT = 1300;
    private final ArrayList<DictionaryServiceServerClient> pod��czeniKlienci = new ArrayList<>();	//przechowuje obiekty obs�ugu�ce po��czonych klient�w
    private final HashMap<String, Integer> pod��czoneSerweryS�ownikowe = new HashMap<>();	//przechowuje porty na kt�rych dzia�aj� serwery s�ownikowe w formie np. "en, 4000"
        
    /**
     * Konstruktor tworz�cy serwer na porcie 1300
     * 
     * @throws IOException je�li wyst�pi� b��dy I/O podczas otwierania gniazda
     */
    public DictionaryServiceServer() throws IOException {
    	
        gniazdoSerwera = new ServerSocket(PORT);       
    }
    
    public static void main (String[] args) {
    	
        Socket socket;
        ObjectInputStream ois;
        ObjectOutputStream oos;
        
    	try {
    		
    		DictionaryServiceServer serwerUs�ugiS�ownikowej = new DictionaryServiceServer();
    		serwerUs�ugiS�ownikowej.start();	//uruchamia w�tek serwera g��wnego
    		
    		serwerUs�ugiS�ownikowej.dodajSerwer("de", new HashMap<String, String>() {{put("pies","Hund"); put("kot","Katze");}});
    		serwerUs�ugiS�ownikowej.dodajSerwer("en", new HashMap<String, String>() {{put("pies","dog"); put("kot","cat");}});
    		serwerUs�ugiS�ownikowej.dodajSerwer("fr", new HashMap<String, String>() {{put("pies","chien"); put("kot","chat");}});
    	} catch (IOException ex) {
    		
    		ex.printStackTrace();
    	} catch (Exception ex) {
    		
    		ex.printStackTrace();
    	}
    }
    
    /**
     * Dodaje i uruchamia serwer do obs�ugi konkretnego j�zyka.
     * 
     * @param kod oznacza kod j�zyka
     * @param s�ownik wprowadza list� s��w z t�umaczeniami
     */
    private void dodajSerwer(String kod, HashMap<String, String> s�ownik) {

    	try {
    		
    		DictionaryServer serwerS�ownikowy = new DictionaryServer(s�ownik);
    		System.out.println("Serwer us�ug: utworzy�em serwer s�ownika: " + "'" + kod + "'" + " na porcie: " + serwerS�ownikowy.pobierzNumerPortu());
    		pod��czoneSerweryS�ownikowe.put(kod, serwerS�ownikowy.pobierzNumerPortu());   //dodaje serwer do mapy monitoruj�cej uruchomione serwery
    		serwerS�ownikowy.start();
    		System.out.println("Serwer us�ug: liczba utworzonych serwer�w s�ownikowych: " + pod��czoneSerweryS�ownikowe.size());
    	} catch (IOException ex) {
    		
    		ex.printStackTrace();
    	}
    }
    
    /**
     * Pobiera numer portu uruchomionego serwera w celu pod��czenia na podanym porcie.
     * 
     * @param kod oznacza kod j�zyka
     * 
     * @return port wybranego serwera s�ownikowego
     */
    public Integer pobierzPortSerweraS�ownikowego(String kod) {
    	
    	return pod��czoneSerweryS�ownikowe.get(kod);
    }
    
    public void usu�Klienta(DictionaryServiceServerClient klient) {
    	
    	pod��czeniKlienci.remove(klient);
    	
    	System.out.println("Serwer us�ug: usun��em klienta");
    	System.out.println("Serwer us�ug: liczba po��czonych klient�w: " + pod��czeniKlienci.size());
    }
    
    @Override
    public void run() {
        while (true) {
        	
            try {
            	
                System.out.println("Serwer us�ug: oczekuj� na po��czenie...");
                Socket gniazdo = gniazdoSerwera.accept();   // wywo�ujemy metod� blokuj�c� oczekuj�ca na po��czenie ze strony klienta
                System.out.println("Serwer us�ug: odebra�em po��czenie");
                DictionaryServiceServerClient klient = new DictionaryServiceServerClient(gniazdo, this);
                System.out.println("Serwer us�ug: utworzy�em klienta");
                pod��czeniKlienci.add(klient);
                klient.start();
                System.out.println("Serwer us�ug: liczba po��czonych klient�w: " + pod��czeniKlienci.size());
            } catch (IOException ex) {
            	
            }
        }
    }    
    
}
