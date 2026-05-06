# System Zarządzania Biblioteką oparty o mikroserwisy

Krótki opis projektu i instrukcja uruchamiania.

## O projekcie

System składa się z dwóch niezależnych mikroserwisów zbudowanych zgodnie z zasadami **Architektury Heksagonalnej** (Porty i Adaptery) oraz **Domain-Driven Design (DDD)**:

*   **UserService (port 8081):** Odpowiada za zarządzanie tożsamością i kontami użytkowników (czytelnicy, bibliotekarze, administratorzy). Korzysta z bazy danych `user_db`.
*   **LibraryService (port 8080):** Odpowiada za domenę wypożyczalni, zarządzanie zasobami (książkami) i obsługę procesu wypożyczeń. Korzysta z bazy danych `library_db`.

Każdy z serwisów to wielomodułowy projekt Maven, działający niezależnie i posiadający własną bazę danych (MongoDB).

---

## Jak uruchomić system?

Możesz uruchomić projekt na dwa sposoby, w zależności od tego, czy chcesz testować gotowy system, czy pisać kod.

### Pełne środowisko w Dockerze
Uruchamia bazę danych oraz obie aplikacje w izolowanych kontenerach.

1. Otwórz terminal w głównym katalogu projektu (tam, gdzie znajduje się `docker-compose.yml`).
2. Zbuduj i uruchom środowisko poleceniem:
   ```bash
   docker-compose up --build
   ```
3. Aplikacje będą dostępne pod adresami `http://localhost:8080` (Library) oraz `http://localhost:8081` (User).
4. Aby zatrzymać i wyczyścić środowisko, użyj skrótu `Ctrl+C`, a następnie wpisz:
   ```bash
   docker-compose down
   ```