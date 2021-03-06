import java.io.*;
import java.net.Socket;

/**
 * Klasa do obsługi połączeń przychodzących do serwera. Każdy klient obsługuje pojedyncze połączenie.
 * 
 */
public class DictionaryServiceServerClient extends Thread {
	// referencja na obiekt umożliwiający połączenie z serwerem
    private final DictionaryServiceServer serwerUsługiSłownikowej;
    // strumienie służące do odbierania oraz wysyłania danych
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    
    private int portKienta;
    
    public DictionaryServiceServerClient(final Socket gniazdo, DictionaryServiceServer serwerUsługiSłownikowej) {
    	
        this.serwerUsługiSłownikowej = serwerUsługiSłownikowej;
        
        try {
        	
            ois = new ObjectInputStream(gniazdo.getInputStream());
        } catch (IOException ex) {
        	
            System.err.println("Nie udało się pobrać strumieni od klienta");
        }
    }  

    /**
     * Odbiera wiadomość przesłaną od klienta z żądaniem tłumaczenia
     * 
     * @throw IOException przy błędzie odebrania wiadomości
     * 
     * @throw ClassNotFoundException przy błędzie rzutowania odebranej wiadomości do tekstu
     */
    private String odbierzWiadomość() throws IOException, ClassNotFoundException {
    	
        String wiadomośćOdebrana = (String) ois.readObject();
        
        return wiadomośćOdebrana.trim();
    }
    
    /**
     * Przetwarza odebraną wiadomość.
     * 
     * @param wiadomość treść w formie np. "en,pies,5555"
     * 
     * @return przetłumaczone słowo 
     */
    private String przetwórzWiadomość(String wiadomość) {          

        String przetworzonaWiadomość = null;

        String żądanie = wiadomość.substring(0, wiadomość.indexOf(","));	// sprawdzamy jakie żądanie wysłał klient, np "en"
        String informacje = wiadomość.substring(wiadomość.indexOf(",") + 1, wiadomość.lastIndexOf(","));   //np. "pies"
        portKienta = Integer.valueOf(wiadomość.substring(wiadomość.lastIndexOf(",") + 1));   //np. 50000
        
        try {
        	
        	oos = new ObjectOutputStream(new Socket("localhost", portKienta).getOutputStream());   //tworzy strumień do wysłania zwrotnie przetłumaczonego słowa
        } catch (Exception ex) {
        	
        	ex.printStackTrace();
        }
        
        Integer port = serwerUsługiSłownikowej.pobierzPortSerweraSłownikowego(żądanie);   //pobiera numer portu potrzebnego serwera języka
        
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
    
    /**
     * Wysyła wiadomość do klienta, który przysłał żądanie tłumaczenia
     * 
     * @param wiadomośćZwrotna treść w formie np. "cat"
     * 
     * @throw IOException przy nieudanej próbie wysłania
     */
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
