import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;

public class DictionaryServiceServer extends Thread {
    
    private ServerSocket gniazdoSerwera;  
    private final int PORT = 1300;
    private final ArrayList<DictionaryServiceServerClient> podłączeniKlienci = new ArrayList<>();	
    private final HashMap<String, Integer> podłączoneSerwerySłownikowe = new HashMap<>();
        
    /**
     * Konstruktor tworzący serwer na porcie 1300
     * 
     * @throws IOException jeśli wystąpią błędy I/O podczas otwierania gniazda
     */
    public DictionaryServiceServer() throws IOException {
    	
        gniazdoSerwera = new ServerSocket(PORT);       
    }
    
    public static void main (String[] args) {
    	
        Socket socket;
        ObjectInputStream ois;
        ObjectOutputStream oos;
        
    	try {
    		
    		DictionaryServiceServer serwerUsługiSłownikowej = new DictionaryServiceServer();
    		serwerUsługiSłownikowej.start();
    		
    		serwerUsługiSłownikowej.dodajSerwer("de", new HashMap<String, String>() {{put("pies","Hund"); put("kot","Katze");}});
    		serwerUsługiSłownikowej.dodajSerwer("en", new HashMap<String, String>() {{put("pies","dog"); put("kot","cat");}});
    		serwerUsługiSłownikowej.dodajSerwer("fr", new HashMap<String, String>() {{put("pies","chien"); put("kot","chat");}});
    	} catch (IOException ex) {
    		
    		ex.printStackTrace();
    	} catch (Exception ex) {
    		
    		ex.printStackTrace();
    	}
    }
    
    private void dodajSerwer(String kod, HashMap<String, String> słownik) {

    	try {
    		
    		DictionaryServer serwerSłownikowy = new DictionaryServer(słownik);
    		System.out.println("Serwer usług: utworzyłem serwer słownika: " + "'" + kod + "'" + " na porcie: " + serwerSłownikowy.pobierzNumerPortu());
    		podłączoneSerwerySłownikowe.put(kod, serwerSłownikowy.pobierzNumerPortu());
    		serwerSłownikowy.start();
    		System.out.println("Serwer usług: liczba utworzonych serwerów słownikowych: " + podłączoneSerwerySłownikowe.size());
    	} catch (IOException ex) {
    		
    		ex.printStackTrace();
    	}
    }
    
    public Integer pobierzPortSerweraSłownikowego(String kod) {
    	
    	return podłączoneSerwerySłownikowe.get(kod);
    }
    
    public void usuńKlienta(DictionaryServiceServerClient klient) {
    	
    	podłączeniKlienci.remove(klient);
    	
    	System.out.println("Serwer usług: usunąłem klienta");
    	System.out.println("Serwer usług: liczba połączonych klientów: " + podłączeniKlienci.size());
    }
    
    @Override
    public void run() {
        while (true) {
        	
            try {
            	
                System.out.println("Serwer usług: oczekuję na połączenie...");
                Socket gniazdo = gniazdoSerwera.accept();   // wywołujemy metodę blokującą oczekująca na połączenie ze strony klienta
                System.out.println("Serwer usług: odebrałem połączenie");
                DictionaryServiceServerClient klient = new DictionaryServiceServerClient(gniazdo, this);
                System.out.println("Serwer usług: utworzyłem klienta");
                podłączeniKlienci.add(klient);
                klient.start();
                System.out.println("Serwer usług: liczba połączonych klientów: " + podłączeniKlienci.size());
            } catch (IOException ex) {
            	
            }
        }
    }    
    
}
