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
    private final ArrayList<Client> pod³¹czeniKlienci = new ArrayList<>();	
    private final HashMap<String, Integer> pod³¹czoneSerweryS³ownikowe = new HashMap<>();
        
    public DictionaryServiceServer() throws IOException {
    	
        gniazdoSerwera = new ServerSocket(PORT);       
    }
    
    public static void main (String[] args) {
    	
        Socket socket;
        ObjectInputStream ois;
        ObjectOutputStream oos;
        
    	try {
    		
    		DictionaryServiceServer serwerUs³ugiS³ownikowej = new DictionaryServiceServer();
    		serwerUs³ugiS³ownikowej.start();
    		
    		serwerUs³ugiS³ownikowej.dodajSerwer("en", new HashMap<String, String>() {{put("pies","dog"); put("kot","cat");}});
    		
    		socket = new Socket("localhost", 1300);
    		oos = new ObjectOutputStream(socket.getOutputStream());
    		ois = new ObjectInputStream(socket.getInputStream());
            oos.writeObject("Wiadomoœæ");
            oos.flush();
            
            String wiadomoœæ = (String) ois.readObject();
            System.out.println("Main klient: odebra³em wiadomoœæ: " + wiadomoœæ);
    	} catch (IOException | ClassNotFoundException ex) {
    		
    		ex.printStackTrace();
    	}
    }
    
    private void dodajSerwer(String kod, HashMap<String, String> s³ownik) {

    	try {
    		
    		DictionaryServer serwerS³ownikowy = new DictionaryServer(s³ownik);
    		System.out.println("Serwer us³ug: utworzy³em serwer s³ownika: " + "'" + kod + "'" + " na porcie: " + serwerS³ownikowy.pobierzNumerPortu());
    		pod³¹czoneSerweryS³ownikowe.put(kod, serwerS³ownikowy.pobierzNumerPortu());
    		serwerS³ownikowy.start();
    		System.out.println("Serwer us³ug: liczba utworzonych serwerów s³ownikowych: " + pod³¹czoneSerweryS³ownikowe.size());
    	} catch (IOException ex) {
    		
    		ex.printStackTrace();
    	}
    }
    
    public void usuñKlienta(Client klient) {
    	
    	pod³¹czeniKlienci.remove(klient);
    	
    	System.out.println("Serwer us³ug: usun¹³em klienta");
    	System.out.println("Serwer us³ug: liczba po³¹czonych klientów: " + pod³¹czeniKlienci.size());
    }
    
    @Override
    public void run() {
        while (true) {
        	
            try {
            	
                System.out.println("Serwer us³ug: oczekujê na po³¹czenie...");
                Socket gniazdo = gniazdoSerwera.accept();	// wywo³ujemy metodê blokuj¹c¹ oczekuj¹ca na po³¹czenie ze strony klienta
                System.out.println("Serwer us³ug: odebra³em po³¹czenie");
                Client klient = new Client(gniazdo, this);
                System.out.println("Serwer us³ug: utworzy³em klienta");
                pod³¹czeniKlienci.add(klient);
                klient.start();
                System.out.println("Serwer us³ug: liczba po³¹czonych klientów: " + pod³¹czeniKlienci.size());
            } catch (IOException ex) {
            	
                //widok.dodajKomunikat("Nieudana próba nawi¹zania po³¹czenia z klientem");
            }
        }
    }    
    
}
