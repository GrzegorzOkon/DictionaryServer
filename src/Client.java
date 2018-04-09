import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Client extends Thread {
    
    // referencja na obiekt umo¿liwiaj¹cy po³¹czenie z serwerem
    private final DictionaryServiceServer serwerUs³ugiS³ownikowej;
    
    // strumienie s³u¿¹ce do odbierania oraz wysy³ania danych
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    
    public Client(final Socket gniazdo, DictionaryServiceServer serwerUs³ugiS³ownikowej) {
    	
        this.serwerUs³ugiS³ownikowej = serwerUs³ugiS³ownikowej;
        
        try {
        	
            oos = new ObjectOutputStream(gniazdo.getOutputStream());
            ois = new ObjectInputStream(gniazdo.getInputStream());
        } catch (IOException ex) {
        	
            System.err.println("Nie uda³o siê pobraæ strumieni od klienta");
        }
    }  

    private String odbierzWiadomoœæ() throws IOException, ClassNotFoundException {
    	
        String wiadomoœæOdebrana = (String) ois.readObject();
        
        return wiadomoœæOdebrana.trim();
    }

    private void wyœlijWiadomoœæ(String wiadomoœæZwrotna) throws IOException {
    	
        oos.writeObject(wiadomoœæZwrotna);
        oos.flush();
    }
    
    /*private void przetwórzWiadomoœæ(String wiadomoœæ) throws SQLException {          
        widok.dodajKomunikat("Odebrano: " + wiadomoœæ);
        // sprawdzamy jakie ¿¹danie wys³a³ klient
        // pobieraj¹c tylko ³añcuch znaków do znaku "<"
        String ¿¹danie = wiadomoœæ.substring(0, wiadomoœæ.indexOf("<"));
        // wyci¹gamy informacje z ³añcucha znaków
        String informacje = wiadomoœæ.substring(wiadomoœæ.indexOf("<")+1);
        
        switch (¿¹danie) {
            case "Zaloguj":
                {
                    // wydzielamy dane
                    String[] dane = informacje.split("/");
                    String login = dane[0];
                    String has³o = dane[1];
                    U¿ytkownik u¿ytkownik = Baza.pobierzInstancjê().zaloguj(login, has³o);
                    try {
                        if (u¿ytkownik == null) {
                            wyœlijWiadomoœæ("Nieudana próba logowania");                           
                        } else {
                            wyœlijWiadomoœæ("Dane do konta zosta³y pomyœlnie zweryfikowane");
                            oos.writeObject(u¿ytkownik); // wysy³amy obiekt u¿ytkownika przez strumieñ
                            oos.flush();
                        }
                    } catch (IOException ex) {
                        widok.dodajKomunikat("Nieudana próba wys³ania komunikatu do klienta");
                    }
                }
                break;
            case "Wyloguj":
                {
                    po³¹czenie.wyloguj(this);                    
                }
                break;
            case "Przegl¹daj ofertê":
                {
                    ArrayList<Samochód> oferta = Baza.pobierzInstancjê().pobierzOfertê();
                    try {
                        if (oferta == null) {
                            widok.dodajKomunikat("Nie uda³o siê pobraæ oferty");
                        } else {
                            wyœlijWiadomoœæ("Uda³o siê pobraæ ofertê");
                            oos.writeObject(oferta); // wysy³amy obiekt u¿ytkownika przez strumieñ
                            oos.flush();    
                        }
                    }catch (IOException ex) {
                        widok.dodajKomunikat("Nieudana próba wys³ania komunikatu do klienta");
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
                    int idRezerwacji = Baza.pobierzInstancjê().dodajRezerwacjê(new Rezerwacja(new U¿ytkownik(login, null, null, null, null), new Samochód(marka, model, kolor, paliwo, rok), dataOdbioru, dataZwrotu));
                    try {
                        if (idRezerwacji == 0) {
                            wyœlijWiadomoœæ("Nieudana próba rezerwacji");                           
                        } else {
                            wyœlijWiadomoœæ("Zarezerwowano samochód");
                        }
                    } catch (IOException ex) {
                        widok.dodajKomunikat("Nieudana próba wys³ania komunikatu do klienta");
                    }
                } 
                break;
            case "Przegl¹daj rezerwacje":
                {
                    // wydzielamy dane
                    String[] dane = informacje.split("/");
                    String login = dane[0];
                    ArrayList<Rezerwacja> rezerwacje = Baza.pobierzInstancjê().pobierzRezerwacje(login);
                    try {
                        if (rezerwacje == null) {
                            widok.dodajKomunikat("Nie uda³o siê pobraæ rezerwacji");
                        } else {
                            wyœlijWiadomoœæ("Uda³o siê pobraæ rezerwacje");
                            oos.writeObject(rezerwacje); // wysy³amy obiekt u¿ytkownika przez strumieñ
                            oos.flush();    
                        }
                    }catch (IOException ex) {
                        widok.dodajKomunikat("Nieudana próba wys³ania komunikatu do klienta");
                    }   
                    
                }
                break;
            case "Przegl¹daj wszystkie rezerwacje":
                {
                    ArrayList<Rezerwacja> rezerwacje = Baza.pobierzInstancjê().pobierzRezerwacje();
                    try {
                        if (rezerwacje == null) {
                            widok.dodajKomunikat("Nie uda³o siê pobraæ rezerwacji");
                        } else {
                            wyœlijWiadomoœæ("Uda³o siê pobraæ rezerwacje");
                            oos.writeObject(rezerwacje); // wysy³amy obiekt u¿ytkownika przez strumieñ
                            oos.flush();    
                        }
                    }catch (IOException ex) {
                        widok.dodajKomunikat("Nieudana próba wys³ania komunikatu do klienta");
                    }   
                    
                }
                break;                               
            case "Anuluj rezerwacjê":
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
                    Baza.pobierzInstancjê().anulujRezerwacjê(login, marka, model ,kolor);                
                }
                break;                                
        }
    }    */
    
    @Override
    public void run() {

    	try {
            	
            System.out.println("Klient: oczekujê na po³¹czenie...");
            String wiadomoœæ = odbierzWiadomoœæ();
            System.out.println("Klient: odebralem wiadomoœæ: " + wiadomoœæ);
            
            //przetwórzWiadomoœæ(wiadomoœæ);
                
            wyœlijWiadomoœæ("Wiadomoœæ zwrotna.");
                
            serwerUs³ugiS³ownikowej.usuñKlienta(this);
                
        } catch (IOException | ClassNotFoundException ex) {
            	
            ex.printStackTrace();
        }  
        
        interrupt();
    }    
}
