import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Client extends Thread {
    
    // referencja na obiekt umo�liwiaj�cy po��czenie z serwerem
    private final DictionaryServiceServer serwerUs�ugiS�ownikowej;
    
    // strumienie s�u��ce do odbierania oraz wysy�ania danych
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    
    public Client(final Socket gniazdo, DictionaryServiceServer serwerUs�ugiS�ownikowej) {
    	
        this.serwerUs�ugiS�ownikowej = serwerUs�ugiS�ownikowej;
        
        try {
        	
            oos = new ObjectOutputStream(gniazdo.getOutputStream());
            ois = new ObjectInputStream(gniazdo.getInputStream());
        } catch (IOException ex) {
        	
            System.err.println("Nie uda�o si� pobra� strumieni od klienta");
        }
    }  

    private String odbierzWiadomo��() throws IOException, ClassNotFoundException {
    	
        String wiadomo��Odebrana = (String) ois.readObject();
        
        return wiadomo��Odebrana.trim();
    }

    private void wy�lijWiadomo��(String wiadomo��Zwrotna) throws IOException {
    	
        oos.writeObject(wiadomo��Zwrotna);
        oos.flush();
    }
    
    /*private void przetw�rzWiadomo��(String wiadomo��) throws SQLException {          
        widok.dodajKomunikat("Odebrano: " + wiadomo��);
        // sprawdzamy jakie ��danie wys�a� klient
        // pobieraj�c tylko �a�cuch znak�w do znaku "<"
        String ��danie = wiadomo��.substring(0, wiadomo��.indexOf("<"));
        // wyci�gamy informacje z �a�cucha znak�w
        String informacje = wiadomo��.substring(wiadomo��.indexOf("<")+1);
        
        switch (��danie) {
            case "Zaloguj":
                {
                    // wydzielamy dane
                    String[] dane = informacje.split("/");
                    String login = dane[0];
                    String has�o = dane[1];
                    U�ytkownik u�ytkownik = Baza.pobierzInstancj�().zaloguj(login, has�o);
                    try {
                        if (u�ytkownik == null) {
                            wy�lijWiadomo��("Nieudana pr�ba logowania");                           
                        } else {
                            wy�lijWiadomo��("Dane do konta zosta�y pomy�lnie zweryfikowane");
                            oos.writeObject(u�ytkownik); // wysy�amy obiekt u�ytkownika przez strumie�
                            oos.flush();
                        }
                    } catch (IOException ex) {
                        widok.dodajKomunikat("Nieudana pr�ba wys�ania komunikatu do klienta");
                    }
                }
                break;
            case "Wyloguj":
                {
                    po��czenie.wyloguj(this);                    
                }
                break;
            case "Przegl�daj ofert�":
                {
                    ArrayList<Samoch�d> oferta = Baza.pobierzInstancj�().pobierzOfert�();
                    try {
                        if (oferta == null) {
                            widok.dodajKomunikat("Nie uda�o si� pobra� oferty");
                        } else {
                            wy�lijWiadomo��("Uda�o si� pobra� ofert�");
                            oos.writeObject(oferta); // wysy�amy obiekt u�ytkownika przez strumie�
                            oos.flush();    
                        }
                    }catch (IOException ex) {
                        widok.dodajKomunikat("Nieudana pr�ba wys�ania komunikatu do klienta");
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
                    int idRezerwacji = Baza.pobierzInstancj�().dodajRezerwacj�(new Rezerwacja(new U�ytkownik(login, null, null, null, null), new Samoch�d(marka, model, kolor, paliwo, rok), dataOdbioru, dataZwrotu));
                    try {
                        if (idRezerwacji == 0) {
                            wy�lijWiadomo��("Nieudana pr�ba rezerwacji");                           
                        } else {
                            wy�lijWiadomo��("Zarezerwowano samoch�d");
                        }
                    } catch (IOException ex) {
                        widok.dodajKomunikat("Nieudana pr�ba wys�ania komunikatu do klienta");
                    }
                } 
                break;
            case "Przegl�daj rezerwacje":
                {
                    // wydzielamy dane
                    String[] dane = informacje.split("/");
                    String login = dane[0];
                    ArrayList<Rezerwacja> rezerwacje = Baza.pobierzInstancj�().pobierzRezerwacje(login);
                    try {
                        if (rezerwacje == null) {
                            widok.dodajKomunikat("Nie uda�o si� pobra� rezerwacji");
                        } else {
                            wy�lijWiadomo��("Uda�o si� pobra� rezerwacje");
                            oos.writeObject(rezerwacje); // wysy�amy obiekt u�ytkownika przez strumie�
                            oos.flush();    
                        }
                    }catch (IOException ex) {
                        widok.dodajKomunikat("Nieudana pr�ba wys�ania komunikatu do klienta");
                    }   
                    
                }
                break;
            case "Przegl�daj wszystkie rezerwacje":
                {
                    ArrayList<Rezerwacja> rezerwacje = Baza.pobierzInstancj�().pobierzRezerwacje();
                    try {
                        if (rezerwacje == null) {
                            widok.dodajKomunikat("Nie uda�o si� pobra� rezerwacji");
                        } else {
                            wy�lijWiadomo��("Uda�o si� pobra� rezerwacje");
                            oos.writeObject(rezerwacje); // wysy�amy obiekt u�ytkownika przez strumie�
                            oos.flush();    
                        }
                    }catch (IOException ex) {
                        widok.dodajKomunikat("Nieudana pr�ba wys�ania komunikatu do klienta");
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
    
    @Override
    public void run() {

    	try {
            	
            System.out.println("Klient: oczekuj� na po��czenie...");
            String wiadomo�� = odbierzWiadomo��();
            System.out.println("Klient: odebralem wiadomo��: " + wiadomo��);
            
            //przetw�rzWiadomo��(wiadomo��);
                
            wy�lijWiadomo��("Wiadomo�� zwrotna.");
                
            serwerUs�ugiS�ownikowej.usu�Klienta(this);
                
        } catch (IOException | ClassNotFoundException ex) {
            	
            ex.printStackTrace();
        }  
        
        interrupt();
    }    
}
