import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class DictionaryServerClient extends Thread {
	
	//referencja na obiekt umo磧iwiaj鉍y po章czenie z serwerem
	private final DictionaryServer serwerS這wnikowy;
	    
	// strumienie s逝蕨ce do odbierania oraz wysy豉nia danych
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	    
	public DictionaryServerClient(final Socket gniazdo, DictionaryServer serwerS這wnikowy) {
	    	
		this.serwerS這wnikowy = serwerS這wnikowy;
	        
	    try {
	        	
	    	ois = new ObjectInputStream(gniazdo.getInputStream());
	    	oos = new ObjectOutputStream(gniazdo.getOutputStream());     
	    } catch (IOException ex) {
	        	
	    	System.err.println("Nie uda這 si� pobra� strumieni od klienta");
	    }
	} 

    /**
     * Odbiera wiadomo�� od klienta serwera us逝gi s這wnikowej.
     * 
     * @throws IOException przy b貫dzie odbioru poprzez strumie�
     * @throws ClassNotFoundException przy b貫dzie pr鏏y rzutowania do tekstu
     * 
     * @return tre�� wiadomo�ci, np. "kot"
     */
	private String odbierzWiadomo��() throws IOException, ClassNotFoundException {
	    	
		String wiadomo�潗debrana = (String) ois.readObject();
	        
	    return wiadomo�潗debrana.trim();
	}
	    
    /**
     * Przetwarza wiadomo�� 
     * 
     * @param wiadomo�� tre�� wiadomo�ci np. "kot"
     * 
     * @return zwraca odpowiedz, np. "cat" lub null gdy brak
     */
    private String przetw鏎zWiadomo��(String wiadomo��) {          

        String przetworzonaWiadomo�� = serwerS這wnikowy.pobierzObcoj瞛yczneS這wo(wiadomo��);
        
        if (przetworzonaWiadomo�� == null) {
        	
        	przetworzonaWiadomo�� = "Nie istnieje t逝maczenie dla danego s這wa: " + wiadomo��;
    	}    
        
        System.out.println("Klient serwera s這wnikowego: przetworzy貫m wiadomo��: " + wiadomo��);
        
        return przetworzonaWiadomo��;
    } 
	    
    /**
     * Wysy豉 wiadomo�� po przetworzeniu
     * 
     * @param wiadomo�澋wrotna np. "kot" lub null
     * 
     * @throws IOException przy nieudanej pr鏏ie
     */
    private void wy�lijWiadomo��(String wiadomo�澋wrotna) throws IOException {
    	
        oos.writeObject(wiadomo�澋wrotna);
        oos.flush();
    }
    
	//@Override
	public void run() {

		try {
	            	
			System.out.println("Klient serwera s這wnikowego: oczekuj� na po章czenie...");
	        String wiadomo�� = odbierzWiadomo��();
	        System.out.println("Klient serwera s這wnikowego: odebra貫m wiadomo��: " + wiadomo��);
	                
	        wy�lijWiadomo��(przetw鏎zWiadomo��(wiadomo��));
	                
	        serwerS這wnikowy.usu鄺lienta(this);
	                
	    } catch (IOException | ClassNotFoundException ex) {
	            	
	        ex.printStackTrace();
	    }  
	        
	    interrupt();
	}
}
