# Testkonzept GamesLibrary

Stand: erste Fassung (Meilenstein 22.11.2026). Elemente 5 bis 7 werden bis zur Schlussabgabe ausgebaut.

## 1. Teststrategie

**Testobjekt:** GamesLibrary (Handout `bbw-md/GamesLibraryHandout`), Maven-Ordner `backend`
(Spring Boot, JPA, H2). Das Frontend (`frontend/`) hat keinen Maven-Wrapper und wird in der Pipeline
nicht gebaut; es wird später mit Cypress geprüft.

**Abzudeckende Funktionen:**

| Funktion | Klasse |
|---|---|
| Alle Spiele lesen, Spiel per ID lesen, Titel suchen | `GameService` |
| Spiel anlegen, ändern, löschen | `GameService` |
| Titel vereinheitlichen (trimmen, Leerzeichen, Grossbuchstabe, leer = Fehler) | `TitleNormalizer` |
| REST-Endpunkte `/api/games` | `GameController` (später) |

**Risiken:**

| # | Risiko | Eintritt | Schaden | Teststufe |
|---|---|---|---|---|
| R1 | Unbekannte ID wird nicht als Fehler gemeldet (Update/Delete/Get) | mittel | hoch | Unit (Mockito) |
| R2 | Titel werden uneinheitlich oder leer gespeichert | hoch | mittel | Unit (TDD) |
| R3 | Update überschreibt Felder nicht vollständig oder verliert die ID | mittel | hoch | Unit (Mockito, ArgumentCaptor) |
| R4 | Controller und Datenbank harmonieren nicht (Mapping, Validierung) | mittel | hoch | Integration (`@SpringBootTest`, H2), später |
| R5 | Fehler bleibt unbemerkt im Hauptzweig | hoch | hoch | Pipeline (jeder Push) |

**Teststufen:** Unit-Tests (JUnit 5, Mockito) für Service und Hilfsklassen; Integrationstests mit H2
(Meilenstein 29.11.); Schnittstellentests mit MockMvc und Postman (Meilenstein 06.12./13.12.).

## 2. Rollen und Verantwortlichkeiten

Folgt bis zur Schlussabgabe (Person A / Person B: Testfälle, Pipeline, Review, Bericht).

## 3. Infrastruktur

Folgt bis zur Schlussabgabe. Vorläufig: lokal IntelliJ IDEA, JDK 26, `./mvnw test`;
Pipeline auf GitHub Actions (`ubuntu-latest`, Temurin 26); Datenbank H2 im Speicher, keine
laufende Fremddienste nötig.

## 4. Entwicklungsvorgehen und Testobjekte

- **TDD:** Neues Verhalten entsteht in kurzen Zyklen. Pro Zyklus ein Commit `rot: …` (nur Test,
  Lauf rot) und ein Commit `grün: …` (Umsetzung, alle Tests grün); Aufräumen als `refactor: …`.
- **Branches und Pull Request:** Entwicklung auf `feature/…`, Merge nach `main` per Pull Request
  nach grünem Pipeline-Lauf.
- **Pipeline:** `.github/workflows/ci.yml` läuft bei jedem Push und Pull Request. Der Job `test`
  führt `./mvnw -B test` je Maven-Ordner aus (Matrix, `fail-fast: false`).
- **Deploy nur bei grünen Tests:** Der Job `deploy` hat `needs: test`. Ist ein Test rot, wird
  `deploy` übersprungen. Das Deployment ist vorerst ein Platzhalter (`echo`).
- **Testobjekte der ersten Fassung:** `TitleNormalizer` (neu, TDD) und `GameService` (Mockito).

## 5. Testfallerstellung

Folgt bis zur Schlussabgabe. Vorläufig angewendete Methoden: Äquivalenzklassen und Grenzwerte
(zum Beispiel Titel `null`, `""`, `" "`, mit Rand- und Doppel-Leerzeichen, klein geschrieben) sowie
positive und negative Fälle (bekannte und unbekannte ID).

## 6. Testfalldurchführung

Folgt bis zur Schlussabgabe. Vorläufig: Ein Test gilt als bestanden, wenn `./mvnw test` mit
Exit-Code 0 endet. Abnahmekriterium für einen Merge: alle Matrix-Jobs der Pipeline grün.

## 7. Fehlerbehandlung

Folgt bis zur Schlussabgabe (Issue-Vorlage mit Schritten, Erwartung, Ergebnis; Schweregrade
kritisch / hoch / mittel / tief).