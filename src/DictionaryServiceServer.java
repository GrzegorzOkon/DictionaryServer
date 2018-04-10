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
    private final ArrayList<DictionaryServiceServerClient> pod章czeniKlienci = new ArrayList<>();	
    private final HashMap<String, Integer> pod章czoneSerweryS這wnikowe = new HashMap<>();
        
    public DictionaryServiceServer() throws IOException {
    	
        gniazdoSerwera = new ServerSocket(PORT);       
    }
    
    public static void main (String[] args) {
    	
        Socket socket;
        ObjectInputStream ois;
        ObjectOutputStream oos;
        
    	try {
    		
    		DictionaryServiceServer serwerUs逝giS這wnikowej = new DictionaryServiceServer();
    		serwerUs逝giS這wnikowej.start();
    		
    		serwerUs逝giS這wnikowej.dodajSerwer("en", new HashMap<String, String>() {{put("pies","dog"); put("kot","cat");}});
    		serwerUs逝giS這wnikowej.dodajSerwer("de", new HashMap<String, String>() {{put("pies","Hund"); put("kot","Katze");}});
    		
    		socket = new Socket("localhost", 1300);
    		oos = new ObjectOutputStream(socket.getOutputStream());
    		ois = new ObjectInputStream(socket.getInputStream());
            oos.writeObject("en,kot");
            oos.flush();
            
            String wiadomo�� = (String) ois.readObject();
            System.out.println("Main klient: odebra貫m wiadomo��: " + wiadomo��);
    	} catch (IOException | ClassNotFoundException ex) {
    		
    		ex.printStackTrace();
    	}
    }
    
    private void dodajSerwer(String kod, HashMap<String, String> s這wnik) {

    	try {
    		
    		DictionaryServer serwerS這wnikowy = new DictionaryServer(s這wnik);
    		System.out.println("Serwer us逝g: utworzy貫m serwer s這wnika: " + "'" + kod + "'" + " na porcie: " + serwerS這wnikowy.pobierzNumerPortu());
    		pod章czoneSerweryS這wnikowe.put(kod, serwerS這wnikowy.pobierzNumerPortu());
    		serwerS這wnikowy.start();
    		System.out.println("Serwer us逝g: liczba utworzonych serwer闚 s這wnikowych: " + pod章czoneSerweryS這wnikowe.size());
    	} catch (IOException ex) {
    		
    		ex.printStackTrace();
    	}
    }
    
    public Integer pobierzPortSerweraS這wnikowego(String kod) {
    	
    	return pod章czoneSerweryS這wnikowe.get(kod);
    }
    
    public void usu鄺lienta(DictionaryServiceServerClient klient) {
    	
    	pod章czeniKlienci.remove(klient);
    	
    	System.out.println("Serwer us逝g: usun像em klienta");
    	System.out.println("Serwer us逝g: liczba po章czonych klient闚: " + pod章czeniKlienci.size());
    }
    
    @Override
    public void run() {
        while (true) {
        	
            try {
            	
                System.out.println("Serwer us逝g: oczekuj� na po章czenie...");
                Socket gniazdo = gniazdoSerwera.accept();	// wywo逝jemy metod� blokuj鉍� oczekuj鉍a na po章czenie ze strony klienta
                System.out.println("Serwer us逝g: odebra貫m po章czenie");
                DictionaryServiceServerClient klient = new DictionaryServiceServerClient(gniazdo, this);
                System.out.println("Serwer us逝g: utworzy貫m klienta");
                pod章czeniKlienci.add(klient);
                klient.start();
                System.out.println("Serwer us逝g: liczba po章czonych klient闚: " + pod章czeniKlienci.size());
            } catch (IOException ex) {
            	
                //widok.dodajKomunikat("Nieudana pr鏏a nawi頊ania po章czenia z klientem");
            }
        }
    }    
    
}
