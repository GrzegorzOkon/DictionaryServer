# DictionaryServer

Klienci i serwery (z użyciem gniazd)


Napisz aplikację "słownikową" według opisanego niżej schematu.

* Utwórz serwer reprezentujący serwer usługi słownikowej.

* Utwórz serwery reprezentujące pojedyncze słowniki, odpowiadające różnym językom. Każdy z nich przechowuje (jako mapę) dane do słownika w postaci par słów; na przykład dla słownika francuskiego pies -> chien, kot -> chat, itd. (np plik w formie .txt)

Klient wysyła do głównego serwera zapytanie w postaci polskiego słowa i kodu języka, na jaki chce to słowo przetłumaczyć, oraz numer portu na którym oczekuje na odpowiedź. Klient otwiera też port na jaki ma nadejść odpowiedź, a po jej nadejściu zamyka go. Aplikacja powinna umożliwić jednocześną obsługę wielu klientów.

Serwer przekazuje szukane słowo serwerowi odpowiadającemu danemu językowi wraz z adresem klienta i numerem portu, na jakim oczekuje on na odpowiedź. Serwer językowy nawiązuje połączenie z klientem i przesyła mu odpowiedź, po czym zamyka to połączenie.

Zakładamy, że wszystkie maszyny są dla siebie wzajemnie widoczne.

Stwórz proste GUI (separujące od logiki przetwarzania danych) dla klienta.

Dodawanie do aplikacji obsługi nowego języka  (ze strony serwerów) ma być bardzo łatwe. Np dodanie nowego słownika do projektu i jego implementacja za pomocą metody.

Aplikacja powinna być odporna na różne sytuacje awaryjne.
