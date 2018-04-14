import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class DictionaryServerClient extends Thread {
	
	//referencja na obiekt umo�liwiaj�cy po��czenie z serwerem
	private final DictionaryServer serwerS�ownikowy;
	    
	// strumienie s�u��ce do odbierania oraz wysy�ania danych
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	    
	public DictionaryServerClient(final Socket gniazdo, DictionaryServer serwerS�ownikowy) {
	    	
		this.serwerS�ownikowy = serwerS�ownikowy;
	        
	    try {
	        	
	    	ois = new ObjectInputStream(gniazdo.getInputStream());
	    	oos = new ObjectOutputStream(gniazdo.getOutputStream());     
	    } catch (IOException ex) {
	        	
	    	System.err.println("Nie uda�o si� pobra� strumieni od klienta");
	    }
	} 

    /**
     * Odbiera wiadomo�� od klienta serwera us�ugi s�ownikowej.
     * 
     * @throws IOException przy b�edzie odbioru poprzez strumie�
     * @throws ClassNotFoundException przy b�edzie pr�by rzutowania do tekstu
     * 
     * @return tre�� wiadomo�ci, np. "kot"
     */
	private String odbierzWiadomo��() throws IOException, ClassNotFoundException {
	    	
		String wiadomo��Odebrana = (String) ois.readObject();
	        
	    return wiadomo��Odebrana.trim();
	}
	    
    /**
     * Przetwarza wiadomo�� 
     * 
     * @param wiadomo�� tre�� wiadomo�ci np. "kot"
     * 
     * @return zwraca odpowiedz, np. "cat" lub null gdy brak
     */
    private String przetw�rzWiadomo��(String wiadomo��) {          

        String przetworzonaWiadomo�� = serwerS�ownikowy.pobierzObcoj�zyczneS�owo(wiadomo��);
        
        if (przetworzonaWiadomo�� == null) {
        	
        	przetworzonaWiadomo�� = "Nie istnieje t�umaczenie dla danego s�owa: " + wiadomo��;
    	}    
        
        System.out.println("Klient serwera s�ownikowego: przetworzy�em wiadomo��: " + wiadomo��);
        
        return przetworzonaWiadomo��;
    } 
	    
    /**
     * Wysy�a wiadomo�� po przetworzeniu
     * 
     * @param wiadomo��Zwrotna np. "kot" lub null
     * 
     * @throws IOException przy nieudanej pr�bie
     */
    private void wy�lijWiadomo��(String wiadomo��Zwrotna) throws IOException {
    	
        oos.writeObject(wiadomo��Zwrotna);
        oos.flush();
    }
    
	//@Override
	public void run() {

		try {
	            	
			System.out.println("Klient serwera s�ownikowego: oczekuj� na po��czenie...");
	        String wiadomo�� = odbierzWiadomo��();
	        System.out.println("Klient serwera s�ownikowego: odebra�em wiadomo��: " + wiadomo��);
	                
	        wy�lijWiadomo��(przetw�rzWiadomo��(wiadomo��));
	                
	        serwerS�ownikowy.usu�Klienta(this);
	                
	    } catch (IOException | ClassNotFoundException ex) {
	            	
	        ex.printStackTrace();
	    }  
	        
	    interrupt();
	}
}
