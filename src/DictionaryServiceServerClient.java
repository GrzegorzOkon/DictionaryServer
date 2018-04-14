import java.io.*;
import java.net.Socket;

/**
 * Klasa do obs�ugi po��cze� przychodz�cych do serwera. Ka�dy klient obs�uguje pojedyncze po��czenie.
 * 
 */
public class DictionaryServiceServerClient extends Thread {
	// referencja na obiekt umo�liwiaj�cy po��czenie z serwerem
    private final DictionaryServiceServer serwerUs�ugiS�ownikowej;
    // strumienie s�u��ce do odbierania oraz wysy�ania danych
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    
    private int portKienta;
    
    public DictionaryServiceServerClient(final Socket gniazdo, DictionaryServiceServer serwerUs�ugiS�ownikowej) {
    	
        this.serwerUs�ugiS�ownikowej = serwerUs�ugiS�ownikowej;
        
        try {
        	
            ois = new ObjectInputStream(gniazdo.getInputStream());
        } catch (IOException ex) {
        	
            System.err.println("Nie uda�o si� pobra� strumieni od klienta");
        }
    }  

    /**
     * Odbiera wiadomo�� przes�an� od klienta z ��daniem t�umaczenia
     * 
     * @throw IOException przy b��dzie odebrania wiadomo�ci
     * 
     * @throw ClassNotFoundException przy b��dzie rzutowania odebranej wiadomo�ci do tekstu
     */
    private String odbierzWiadomo��() throws IOException, ClassNotFoundException {
    	
        String wiadomo��Odebrana = (String) ois.readObject();
        
        return wiadomo��Odebrana.trim();
    }
    
    /**
     * Przetwarza odebran� wiadomo��.
     * 
     * @param wiadomo�� tre�� w formie np. "en,pies,5555"
     * 
     * @return przet�umaczone s�owo 
     */
    private String przetw�rzWiadomo��(String wiadomo��) {          

        String przetworzonaWiadomo�� = null;

        String ��danie = wiadomo��.substring(0, wiadomo��.indexOf(","));	// sprawdzamy jakie ��danie wys�a� klient, np "en"
        String informacje = wiadomo��.substring(wiadomo��.indexOf(",") + 1, wiadomo��.lastIndexOf(","));   //np. "pies"
        portKienta = Integer.valueOf(wiadomo��.substring(wiadomo��.lastIndexOf(",") + 1));   //np. 50000
        
        try {
        	
        	oos = new ObjectOutputStream(new Socket("localhost", portKienta).getOutputStream());   //tworzy strumie� do wys�ania zwrotnie przet�umaczonego s�owa
        } catch (Exception ex) {
        	
        	ex.printStackTrace();
        }
        
        Integer port = serwerUs�ugiS�ownikowej.pobierzPortSerweraS�ownikowego(��danie);   //pobiera numer portu potrzebnego serwera j�zyka
        
        if (port != null) {

        	try {

        		Socket gniazdo = new Socket("localhost", port);
        		ObjectOutputStream oos = new ObjectOutputStream(gniazdo.getOutputStream());
        		ObjectInputStream ois = new ObjectInputStream(gniazdo.getInputStream());

        		System.out.println("Klient serwera us�ugi: pod��czy�em si� do serwera: '" + ��danie + "' na porcie: " + port);
        		
                oos.writeObject(informacje);
                oos.flush();
                
                przetworzonaWiadomo�� = (String) ois.readObject();
            } catch (IOException ex) {
            	
            	System.out.println("Klient serwera us�ugi: nie udalo pod��czy� si� do serwera: '" + ��danie + "' na porcie: " + port);
                ex.printStackTrace();
            } catch (ClassNotFoundException e) {

            	System.out.println("Klient serwera us�ugi: nie uda�o si� odebra� przetworzonej wiadomo�ci.");
				e.printStackTrace();
			}
        } else {
        	
        	przetworzonaWiadomo�� = "Nie istnieje serwer dla danego kodu j�zyka: '" + ��danie + "'";
        }
        
        System.out.println("Klient serwera us�ugi: przetworzy�em wiadomo��: " + wiadomo��);
        
        return przetworzonaWiadomo��;
    }    
    
    /**
     * Wysy�a wiadomo�� do klienta, kt�ry przys�a� ��danie t�umaczenia
     * 
     * @param wiadomo��Zwrotna tre�� w formie np. "cat"
     * 
     * @throw IOException przy nieudanej pr�bie wys�ania
     */
    private void wy�lijWiadomo��(String wiadomo��Zwrotna) throws IOException {
    	
        oos.writeObject(wiadomo��Zwrotna);
        oos.flush();
    }
    
    @Override
    public void run() {

    	try {
            	
            System.out.println("Klient serwera us�ugi: oczekuj� na po��czenie...");
            
            String wiadomo�� = odbierzWiadomo��();
            System.out.println("Klient serwera us�ugi: odebralem wiadomo��: " + wiadomo��);
                
            wy�lijWiadomo��(przetw�rzWiadomo��(wiadomo��));
                
            serwerUs�ugiS�ownikowej.usu�Klienta(this);
        } catch (IOException | ClassNotFoundException ex) {
            	
            ex.printStackTrace();
        }  
        
        interrupt();
    }    
}
