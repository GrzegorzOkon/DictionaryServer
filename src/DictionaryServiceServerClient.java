import java.io.*;
import java.net.Socket;

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

    private String odbierzWiadomo��() throws IOException, ClassNotFoundException {
    	
        String wiadomo��Odebrana = (String) ois.readObject();
        
        return wiadomo��Odebrana.trim();
    }
    
    private String przetw�rzWiadomo��(String wiadomo��) {          

        String przetworzonaWiadomo�� = null;

        String ��danie = wiadomo��.substring(0, wiadomo��.indexOf(","));	// sprawdzamy jakie ��danie wys�a� klient
        String informacje = wiadomo��.substring(wiadomo��.indexOf(",") + 1, wiadomo��.lastIndexOf(","));
        portKienta = Integer.valueOf(wiadomo��.substring(wiadomo��.lastIndexOf(",") + 1));
        
        try {
        	
        	oos = new ObjectOutputStream(new Socket("localhost", portKienta).getOutputStream());
        } catch (Exception ex) {
        	
        	ex.printStackTrace();
        }
        
        Integer port = serwerUs�ugiS�ownikowej.pobierzPortSerweraS�ownikowego(��danie);
        
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
