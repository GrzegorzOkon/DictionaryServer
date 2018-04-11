import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class DictionaryServer extends Thread {
    
    private ServerSocket gniazdoSerwera;  
    private final ArrayList<DictionaryServerClient> pod��czeniKlienci = new ArrayList<>();	
    private HashMap<String, String> s�ownik;
    
    public DictionaryServer(HashMap<String, String> s�ownik) throws IOException {
    	
        gniazdoSerwera = new ServerSocket(0); 
        this.s�ownik = s�ownik;
    }
    
    public int pobierzNumerPortu() {
    	
    	return gniazdoSerwera.getLocalPort();
    }
    
    public String pobierzObcoj�zyczneS�owo(String s�owoT�umczone) {
    	
    	return s�ownik.get(s�owoT�umczone);
    }
    
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
            	
                //widok.dodajKomunikat("Nieudana pr�ba nawi�zania po��czenia z klientem");
            }
        }
    }
}
