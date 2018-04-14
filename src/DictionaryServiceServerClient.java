import java.io.*;
import java.net.Socket;

/**
 * Klasa do obs³ugi po³¹czeñ przychodz¹cych do serwera. Ka¿dy klient obs³uguje pojedyncze po³¹czenie.
 * 
 */
public class DictionaryServiceServerClient extends Thread {
	// referencja na obiekt umo¿liwiaj¹cy po³¹czenie z serwerem
    private final DictionaryServiceServer serwerUs³ugiS³ownikowej;
    // strumienie s³u¿¹ce do odbierania oraz wysy³ania danych
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    
    private int portKienta;
    
    public DictionaryServiceServerClient(final Socket gniazdo, DictionaryServiceServer serwerUs³ugiS³ownikowej) {
    	
        this.serwerUs³ugiS³ownikowej = serwerUs³ugiS³ownikowej;
        
        try {
        	
            ois = new ObjectInputStream(gniazdo.getInputStream());
        } catch (IOException ex) {
        	
            System.err.println("Nie uda³o siê pobraæ strumieni od klienta");
        }
    }  

    /**
     * Odbiera wiadomoœæ przes³an¹ od klienta z ¿¹daniem t³umaczenia
     * 
     * @throw IOException przy b³êdzie odebrania wiadomoœci
     * 
     * @throw ClassNotFoundException przy b³êdzie rzutowania odebranej wiadomoœci do tekstu
     */
    private String odbierzWiadomoœæ() throws IOException, ClassNotFoundException {
    	
        String wiadomoœæOdebrana = (String) ois.readObject();
        
        return wiadomoœæOdebrana.trim();
    }
    
    /**
     * Przetwarza odebran¹ wiadomoœæ.
     * 
     * @param wiadomoœæ treœæ w formie np. "en,pies,5555"
     * 
     * @return przet³umaczone s³owo 
     */
    private String przetwórzWiadomoœæ(String wiadomoœæ) {          

        String przetworzonaWiadomoœæ = null;

        String ¿¹danie = wiadomoœæ.substring(0, wiadomoœæ.indexOf(","));	// sprawdzamy jakie ¿¹danie wys³a³ klient, np "en"
        String informacje = wiadomoœæ.substring(wiadomoœæ.indexOf(",") + 1, wiadomoœæ.lastIndexOf(","));   //np. "pies"
        portKienta = Integer.valueOf(wiadomoœæ.substring(wiadomoœæ.lastIndexOf(",") + 1));   //np. 50000
        
        try {
        	
        	oos = new ObjectOutputStream(new Socket("localhost", portKienta).getOutputStream());   //tworzy strumieñ do wys³ania zwrotnie przet³umaczonego s³owa
        } catch (Exception ex) {
        	
        	ex.printStackTrace();
        }
        
        Integer port = serwerUs³ugiS³ownikowej.pobierzPortSerweraS³ownikowego(¿¹danie);   //pobiera numer portu potrzebnego serwera jêzyka
        
        if (port != null) {

        	try {

        		Socket gniazdo = new Socket("localhost", port);
        		ObjectOutputStream oos = new ObjectOutputStream(gniazdo.getOutputStream());
        		ObjectInputStream ois = new ObjectInputStream(gniazdo.getInputStream());

        		System.out.println("Klient serwera us³ugi: pod³¹czy³em siê do serwera: '" + ¿¹danie + "' na porcie: " + port);
        		
                oos.writeObject(informacje);
                oos.flush();
                
                przetworzonaWiadomoœæ = (String) ois.readObject();
            } catch (IOException ex) {
            	
            	System.out.println("Klient serwera us³ugi: nie udalo pod³¹czyæ siê do serwera: '" + ¿¹danie + "' na porcie: " + port);
                ex.printStackTrace();
            } catch (ClassNotFoundException e) {

            	System.out.println("Klient serwera us³ugi: nie uda³o siê odebraæ przetworzonej wiadomoœci.");
				e.printStackTrace();
			}
        } else {
        	
        	przetworzonaWiadomoœæ = "Nie istnieje serwer dla danego kodu jêzyka: '" + ¿¹danie + "'";
        }
        
        System.out.println("Klient serwera us³ugi: przetworzy³em wiadomoœæ: " + wiadomoœæ);
        
        return przetworzonaWiadomoœæ;
    }    
    
    /**
     * Wysy³a wiadomoœæ do klienta, który przys³a³ ¿¹danie t³umaczenia
     * 
     * @param wiadomoœæZwrotna treœæ w formie np. "cat"
     * 
     * @throw IOException przy nieudanej próbie wys³ania
     */
    private void wyœlijWiadomoœæ(String wiadomoœæZwrotna) throws IOException {
    	
        oos.writeObject(wiadomoœæZwrotna);
        oos.flush();
    }
    
    @Override
    public void run() {

    	try {
            	
            System.out.println("Klient serwera us³ugi: oczekujê na po³¹czenie...");
            
            String wiadomoœæ = odbierzWiadomoœæ();
            System.out.println("Klient serwera us³ugi: odebralem wiadomoœæ: " + wiadomoœæ);
                
            wyœlijWiadomoœæ(przetwórzWiadomoœæ(wiadomoœæ));
                
            serwerUs³ugiS³ownikowej.usuñKlienta(this);
        } catch (IOException | ClassNotFoundException ex) {
            	
            ex.printStackTrace();
        }  
        
        interrupt();
    }    
}
