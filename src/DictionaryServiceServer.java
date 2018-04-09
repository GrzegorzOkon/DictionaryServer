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
    private final ArrayList<Client> podłączeniKlienci = new ArrayList<>();	
    private final HashMap<String, Integer> podłączoneSerwerySłownikowe = new HashMap<>();
        
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
    		
    		serwerUsługiSłownikowej.dodajSerwer("en", new HashMap<String, String>() {{put("pies","dog"); put("kot","cat");}});
    		
    		socket = new Socket("localhost", 1300);
    		oos = new ObjectOutputStream(socket.getOutputStream());
    		ois = new ObjectInputStream(socket.getInputStream());
            oos.writeObject("Wiadomość");
            oos.flush();
            
            String wiadomość = (String) ois.readObject();
            System.out.println("Main klient: odebrałem wiadomość: " + wiadomość);
    	} catch (IOException | ClassNotFoundException ex) {
    		
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
    
    public void usuńKlienta(Client klient) {
    	
    	podłączeniKlienci.remove(klient);
    	
    	System.out.println("Serwer usług: usunąłem klienta");
    	System.out.println("Serwer usług: liczba połączonych klientów: " + podłączeniKlienci.size());
    }
    
    @Override
    public void run() {
        while (true) {
        	
            try {
            	
                System.out.println("Serwer usług: oczekuję na połączenie...");
                Socket gniazdo = gniazdoSerwera.accept();	// wywołujemy metodę blokującą oczekująca na połączenie ze strony klienta
                System.out.println("Serwer usług: odebrałem połączenie");
                Client klient = new Client(gniazdo, this);
                System.out.println("Serwer usług: utworzyłem klienta");
                podłączeniKlienci.add(klient);
                klient.start();
                System.out.println("Serwer usług: liczba połączonych klientów: " + podłączeniKlienci.size());
            } catch (IOException ex) {
            	
                //widok.dodajKomunikat("Nieudana próba nawiązania połączenia z klientem");
            }
        }
    }    
    
}
