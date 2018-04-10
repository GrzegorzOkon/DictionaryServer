import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class DictionaryServerClient {
	
	// referencja na obiekt umo磧iwiaj鉍y po章czenie z serwerem
	//private final DictionaryServer serwerS這wnikowy;
	    
	    // strumienie s逝蕨ce do odbierania oraz wysy豉nia danych
	    private ObjectInputStream ois;
	    private ObjectOutputStream oos;
	    
	    /*public DictionaryServerClient(final Socket gniazdo, DictionaryServer serwerS這wnikowy) {
	    	
	    	this.serwerS這wnikowy = serwerS這wnikowy;
	        
	        try {
	        	
	            oos = new ObjectOutputStream(gniazdo.getOutputStream());
	            ois = new ObjectInputStream(gniazdo.getInputStream());
	        } catch (IOException ex) {
	        	
	            System.err.println("Nie uda這 si� pobra� strumieni od klienta");
	        }
	    }  */

	    /*private String odbierzWiadomo��() throws IOException, ClassNotFoundException {
	    	
	        String wiadomo�潗debrana = (String) ois.readObject();
	        
	        return wiadomo�潗debrana.trim();
	    }*/

	    /*private void wy�lijWiadomo��(String wiadomo�澋wrotna) throws IOException {
	    	
	        oos.writeObject(wiadomo�澋wrotna);
	        oos.flush();
	    }*/
	    
	    /*private void przetw鏎zWiadomo��(String wiadomo��) throws SQLException {          
	        widok.dodajKomunikat("Odebrano: " + wiadomo��);
	        // sprawdzamy jakie 蕨danie wys豉� klient
	        // pobieraj鉍 tylko 豉鎍uch znak闚 do znaku "<"
	        String 蕨danie = wiadomo��.substring(0, wiadomo��.indexOf("<"));
	        // wyci鉚amy informacje z 豉鎍ucha znak闚
	        String informacje = wiadomo��.substring(wiadomo��.indexOf("<")+1);
	        
	        switch (蕨danie) {
	            case "Zaloguj":
	                {
	                    // wydzielamy dane
	                    String[] dane = informacje.split("/");
	                    String login = dane[0];
	                    String has這 = dane[1];
	                    U篡tkownik u篡tkownik = Baza.pobierzInstancj�().zaloguj(login, has這);
	                    try {
	                        if (u篡tkownik == null) {
	                            wy�lijWiadomo��("Nieudana pr鏏a logowania");                           
	                        } else {
	                            wy�lijWiadomo��("Dane do konta zosta造 pomy�lnie zweryfikowane");
	                            oos.writeObject(u篡tkownik); // wysy豉my obiekt u篡tkownika przez strumie�
	                            oos.flush();
	                        }
	                    } catch (IOException ex) {
	                        widok.dodajKomunikat("Nieudana pr鏏a wys豉nia komunikatu do klienta");
	                    }
	                }
	                break;
	            case "Wyloguj":
	                {
	                    po章czenie.wyloguj(this);                    
	                }
	                break;
	            case "Przegl鉅aj ofert�":
	                {
	                    ArrayList<Samoch鏚> oferta = Baza.pobierzInstancj�().pobierzOfert�();
	                    try {
	                        if (oferta == null) {
	                            widok.dodajKomunikat("Nie uda這 si� pobra� oferty");
	                        } else {
	                            wy�lijWiadomo��("Uda這 si� pobra� ofert�");
	                            oos.writeObject(oferta); // wysy豉my obiekt u篡tkownika przez strumie�
	                            oos.flush();    
	                        }
	                    }catch (IOException ex) {
	                        widok.dodajKomunikat("Nieudana pr鏏a wys豉nia komunikatu do klienta");
	                    }                  
	                }
	                break;
	            case "Rezerwuj":
	                {
	                    // wydzielamy dane
	                    String[] dane = informacje.split("/");
	                    String login = dane[0];
	                    String marka = dane[1];
	                    String model = dane[2];
	                    String kolor = dane[3]; 
	                    String paliwo = dane[4];
	                    int rok = Integer.valueOf(dane[5]);     
	                    LocalDate dataOdbioru = LocalDate.parse(dane[6]);
	                    LocalDate dataZwrotu = LocalDate.parse(dane[7]);
	                    int idRezerwacji = Baza.pobierzInstancj�().dodajRezerwacj�(new Rezerwacja(new U篡tkownik(login, null, null, null, null), new Samoch鏚(marka, model, kolor, paliwo, rok), dataOdbioru, dataZwrotu));
	                    try {
	                        if (idRezerwacji == 0) {
	                            wy�lijWiadomo��("Nieudana pr鏏a rezerwacji");                           
	                        } else {
	                            wy�lijWiadomo��("Zarezerwowano samoch鏚");
	                        }
	                    } catch (IOException ex) {
	                        widok.dodajKomunikat("Nieudana pr鏏a wys豉nia komunikatu do klienta");
	                    }
	                } 
	                break;
	            case "Przegl鉅aj rezerwacje":
	                {
	                    // wydzielamy dane
	                    String[] dane = informacje.split("/");
	                    String login = dane[0];
	                    ArrayList<Rezerwacja> rezerwacje = Baza.pobierzInstancj�().pobierzRezerwacje(login);
	                    try {
	                        if (rezerwacje == null) {
	                            widok.dodajKomunikat("Nie uda這 si� pobra� rezerwacji");
	                        } else {
	                            wy�lijWiadomo��("Uda這 si� pobra� rezerwacje");
	                            oos.writeObject(rezerwacje); // wysy豉my obiekt u篡tkownika przez strumie�
	                            oos.flush();    
	                        }
	                    }catch (IOException ex) {
	                        widok.dodajKomunikat("Nieudana pr鏏a wys豉nia komunikatu do klienta");
	                    }   
	                    
	                }
	                break;
	            case "Przegl鉅aj wszystkie rezerwacje":
	                {
	                    ArrayList<Rezerwacja> rezerwacje = Baza.pobierzInstancj�().pobierzRezerwacje();
	                    try {
	                        if (rezerwacje == null) {
	                            widok.dodajKomunikat("Nie uda這 si� pobra� rezerwacji");
	                        } else {
	                            wy�lijWiadomo��("Uda這 si� pobra� rezerwacje");
	                            oos.writeObject(rezerwacje); // wysy豉my obiekt u篡tkownika przez strumie�
	                            oos.flush();    
	                        }
	                    }catch (IOException ex) {
	                        widok.dodajKomunikat("Nieudana pr鏏a wys豉nia komunikatu do klienta");
	                    }   
	                    
	                }
	                break;                               
	            case "Anuluj rezerwacj�":
	                {
	                    // wydzielamy dane
	                    String[] dane = informacje.split("/");
	                    String login = dane[0];
	                    String marka = dane[1];
	                    String model = dane[2];
	                    String kolor = dane[3]; 
	                    String paliwo = dane[4];
	                    int rok = Integer.valueOf(dane[5]);     
	                    LocalDate dataOdbioru = LocalDate.parse(dane[6]);
	                    LocalDate dataZwrotu = LocalDate.parse(dane[7]);
	                    Baza.pobierzInstancj�().anulujRezerwacj�(login, marka, model ,kolor);                
	                }
	                break;                                
	        }
	    }    */
	    
	    //@Override
	    /*public void run() {

	    	try {
	            	
	            System.out.println("Klient: oczekuj� na po章czenie...");
	            String wiadomo�� = odbierzWiadomo��();
	            System.out.println("Klient: odebralem wiadomo��: " + wiadomo��);
	            
	            //przetw鏎zWiadomo��(wiadomo��);
	                
	            wy�lijWiadomo��("Wiadomo�� zwrotna.");
	                
	            serwerUs逝giS這wnikowej.usu鄺lienta(this);
	                
	        } catch (IOException | ClassNotFoundException ex) {
	            	
	            ex.printStackTrace();
	        }  
	        
	        interrupt();
	    }    */

}
