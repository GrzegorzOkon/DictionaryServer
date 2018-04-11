import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class DictionaryServerClient extends Thread {
	
	//referencja na obiekt umo¿liwiaj¹cy po³¹czenie z serwerem
	private final DictionaryServer serwerS³ownikowy;
	    
	// strumienie s³u¿¹ce do odbierania oraz wysy³ania danych
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	    
	public DictionaryServerClient(final Socket gniazdo, DictionaryServer serwerS³ownikowy) {
	    	
		this.serwerS³ownikowy = serwerS³ownikowy;
	        
	    try {
	        	
	    	ois = new ObjectInputStream(gniazdo.getInputStream());
	    	oos = new ObjectOutputStream(gniazdo.getOutputStream());     
	    } catch (IOException ex) {
	        	
	    	System.err.println("Nie uda³o siê pobraæ strumieni od klienta");
	    }
	} 

	private String odbierzWiadomoœæ() throws IOException, ClassNotFoundException {
	    	
		String wiadomoœæOdebrana = (String) ois.readObject();
	        
	    return wiadomoœæOdebrana.trim();
	}
	    
    private String przetwórzWiadomoœæ(String wiadomoœæ) {          

        String przetworzonaWiadomoœæ = serwerS³ownikowy.pobierzObcojêzyczneS³owo(wiadomoœæ);
        
        if (przetworzonaWiadomoœæ == null) {
        	
        	przetworzonaWiadomoœæ = "Nie istnieje t³umaczenie dla danego s³owa: " + wiadomoœæ;
    	}    
        
        System.out.println("Klient serwera s³ownikowego: przetworzy³em wiadomoœæ: " + wiadomoœæ);
        
        return przetworzonaWiadomoœæ;
    } 
	    
    private void wyœlijWiadomoœæ(String wiadomoœæZwrotna) throws IOException {
    	
        oos.writeObject(wiadomoœæZwrotna);
        oos.flush();
    }
    
	//@Override
	public void run() {

		try {
	            	
			System.out.println("Klient serwera s³ownikowego: oczekujê na po³¹czenie...");
	        String wiadomoœæ = odbierzWiadomoœæ();
	        System.out.println("Klient serwera s³ownikowego: odebra³em wiadomoœæ: " + wiadomoœæ);
	                
	        wyœlijWiadomoœæ(przetwórzWiadomoœæ(wiadomoœæ));
	                
	        serwerS³ownikowy.usuñKlienta(this);
	                
	    } catch (IOException | ClassNotFoundException ex) {
	            	
	        ex.printStackTrace();
	    }  
	        
	    interrupt();
	}
}
