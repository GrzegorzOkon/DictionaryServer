import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DictionaryServiceServer extends Thread {
    
    private ServerSocket gniazdoSerwera;  
    private final int PORT = 1300;
    private final ArrayList<DictionaryServiceServerClient> pod��czeniKlienci = new ArrayList<>();	
    private final HashMap<String, Integer> pod��czoneSerweryS�ownikowe = new HashMap<>();
        
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
    		serwerUs�ugiS�ownikowej.start();
    		
    		serwerUs�ugiS�ownikowej.dodajSerwer("en", new HashMap<String, String>() {{put("pies","dog"); put("kot","cat");}});
    		serwerUs�ugiS�ownikowej.dodajSerwer("de", new HashMap<String, String>() {{put("pies","Hund"); put("kot","Katze");}});
    		
    		socket = new Socket("localhost", 1300);
    		oos = new ObjectOutputStream(socket.getOutputStream());
    		ois = new ObjectInputStream(socket.getInputStream());
            oos.writeObject("en,kot");
            oos.flush();
            
            String wiadomo�� = (String) ois.readObject();
            System.out.println("Main klient: odebra�em wiadomo��: " + wiadomo��);
    	} catch (IOException | ClassNotFoundException ex) {
    		
    		ex.printStackTrace();
    	} catch (Exception ex) {
    		
    		ex.printStackTrace();
    	}
    }
    
    private void dodajSerwer(String kod, HashMap<String, String> s�ownik) {

    	try {
    		
    		DictionaryServer serwerS�ownikowy = new DictionaryServer(s�ownik);
    		System.out.println("Serwer us�ug: utworzy�em serwer s�ownika: " + "'" + kod + "'" + " na porcie: " + serwerS�ownikowy.pobierzNumerPortu());
    		pod��czoneSerweryS�ownikowe.put(kod, serwerS�ownikowy.pobierzNumerPortu());
    		serwerS�ownikowy.start();
    		System.out.println("Serwer us�ug: liczba utworzonych serwer�w s�ownikowych: " + pod��czoneSerweryS�ownikowe.size());
    	} catch (IOException ex) {
    		
    		ex.printStackTrace();
    	}
    }
    
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
                Socket gniazdo = gniazdoSerwera.accept();	// wywo�ujemy metod� blokuj�c� oczekuj�ca na po��czenie ze strony klienta
                System.out.println("Serwer us�ug: odebra�em po��czenie");
                DictionaryServiceServerClient klient = new DictionaryServiceServerClient(gniazdo, this);
                System.out.println("Serwer us�ug: utworzy�em klienta");
                pod��czeniKlienci.add(klient);
                klient.start();
                System.out.println("Serwer us�ug: liczba po��czonych klient�w: " + pod��czeniKlienci.size());
            } catch (IOException ex) {
            	
                //widok.dodajKomunikat("Nieudana pr�ba nawi�zania po��czenia z klientem");
            }
        }
    }    
    
}
