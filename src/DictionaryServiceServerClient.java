import java.io.*;
import java.net.Socket;

public class DictionaryServiceServerClient extends Thread {
    
    // referencja na obiekt umożliwiający połączenie z serwerem
    private final DictionaryServiceServer serwerUsługiSłownikowej;
    
    // strumienie służące do odbierania oraz wysyłania danych
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    
    public DictionaryServiceServerClient(final Socket gniazdo, DictionaryServiceServer serwerUsługiSłownikowej) {
    	
        this.serwerUsługiSłownikowej = serwerUsługiSłownikowej;
        
        try {
        	
            oos = new ObjectOutputStream(gniazdo.getOutputStream());
            ois = new ObjectInputStream(gniazdo.getInputStream());
        } catch (IOException ex) {
        	
            System.err.println("Nie udało się pobrać strumieni od klienta");
        }
    }  

    private String odbierzWiadomość() throws IOException, ClassNotFoundException {
    	
        String wiadomośćOdebrana = (String) ois.readObject();
        
        return wiadomośćOdebrana.trim();
    }
    
    private String przetwórzWiadomość(String wiadomość) {          

        String przetworzonaWiadomość = null;

        String żądanie = wiadomość.substring(0, wiadomość.indexOf(","));	// sprawdzamy jakie żądanie wysłał klient
        String informacje = wiadomość.substring(wiadomość.indexOf(",") + 1);
        
        Integer port = serwerUsługiSłownikowej.pobierzPortSerweraSłownikowego(żądanie);
        
        if (port != null) {

        	try {

        		Socket gniazdo = new Socket("localhost", port);
        		ObjectOutputStream oos = new ObjectOutputStream(gniazdo.getOutputStream());
        		ObjectInputStream ois = new ObjectInputStream(gniazdo.getInputStream());

        		System.out.println("Klient serwera usługi: podłączyłem się do serwera: '" + żądanie + "' na porcie: " + port);
        		
                oos.writeObject(informacje);
                oos.flush();
                
                przetworzonaWiadomość = (String) ois.readObject();
            } catch (IOException ex) {
            	
            	System.out.println("Klient serwera usługi: nie udalo podłączyć się do serwera: '" + żądanie + "' na porcie: " + port);
                ex.printStackTrace();
            } catch (ClassNotFoundException e) {

            	System.out.println("Klient serwera usługi: nie udało się odebrać przetworzonej wiadomości.");
				e.printStackTrace();
			}
        } else {
        	
        	przetworzonaWiadomość = "Nie istnieje serwer dla danego kodu języka: '" + żądanie + "'";
        }
        
        System.out.println("Klient serwera usługi: przetworzyłem wiadomość: " + wiadomość);
        
        return przetworzonaWiadomość;
    }    
    
    private void wyślijWiadomość(String wiadomośćZwrotna) throws IOException {
    	
        oos.writeObject(wiadomośćZwrotna);
        oos.flush();
    }
    
    @Override
    public void run() {

    	try {
            	
            System.out.println("Klient serwera usługi: oczekuję na połączenie...");
            
            String wiadomość = odbierzWiadomość();
            System.out.println("Klient serwera usługi: odebralem wiadomość: " + wiadomość);
                
            wyślijWiadomość(przetwórzWiadomość(wiadomość));
                
            serwerUsługiSłownikowej.usuńKlienta(this);
        } catch (IOException | ClassNotFoundException ex) {
            	
            ex.printStackTrace();
        }  
        
        interrupt();
    }    
}
