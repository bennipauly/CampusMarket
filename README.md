# CampusMarket

CampusMarket ist eine native Android-App, auf der Studierende gebrauchte Gegenstände (Bücher, Skripte, Taschenrechner, Technik, Möbel) anbieten und finden können.
Die App ist ein Prüfungsprojekt im Modul „Entwicklung mobiler Applikationen“ an der DHBW Mannheim.

Die App arbeitet **rein lokal**: Es gibt kein Backend, keinen Login und keine Netzwerkzugriffe. Alle Anzeigen liegen in einer Room-Datenbank auf dem Gerät.

**Gruppe:** eintragen

## Funktionen

- Anzeigenübersicht mit Suche (Titel, Beschreibung, Übergabeort)
- Detailansicht mit Foto, Preis, Kategorie, Zustand, Übergabeort und Status
- Anzeige erstellen mit Prüfung der Eingaben (leerer Titel, ungültiger Preis, fehlender Übergabeort)
- Anzeige löschen mit Bestätigungsdialog
- Produktfoto mit der Gerätekamera (mit Kamera-Berechtigung; ohne Berechtigung lässt sich die Anzeige trotzdem speichern)
- Foto im Vollbild: In der Detailansicht auf das Foto tippen
- Lokale Speicherung: Die Daten bleiben nach einem Neustart erhalten. Beim ersten Start werden sechs Beispielanzeigen angelegt.

## Voraussetzungen

| Was | Version |
|---|---|
| Android Studio | aktuelle Version mit Unterstützung für AGP 9.4 (getestet mit Build `AI-262.9437.185`) |
| Android SDK | Platform **API 37** (Android Studio bietet die Installation beim ersten Sync an) |
| Gerät oder Emulator | Android **8.0 (API 26)** oder neuer |
| Java | Kein eigenes JDK nötig, siehe unten |
| Internet | Nur beim **ersten** Build, damit Gradle, die Bibliotheken und das JDK geladen werden |

**Zum JDK:** Gradle baut mit **Java 25**. Das ist in `gradle/gradle-daemon-jvm.properties` festgelegt. Android Studio bringt Java 25 mit, und auf der Kommandozeile lädt Gradle es beim ersten Build selbst herunter. Zum Starten von `gradlew` reicht jedes installierte Java (getestet ab Java 8).

## Build und Start in Android Studio

1. Repository klonen:

   ```
   git clone https://github.com/bennipauly/CampusMarket.git
   ```

2. Android Studio öffnen → **File → Open** → den Ordner `CampusMarket` wählen → dem Projekt vertrauen („Trust Project“).
3. Warten, bis der **Gradle Sync** fertig ist. Android Studio legt dabei die Datei `local.properties` mit dem Pfad zum Android SDK an. Fehlt API 37, die angebotene Installation bestätigen.
4. Oben ein Gerät auswählen (Emulator aus dem **Device Manager** oder ein Handy mit USB-Debugging).
5. Auf **Run ▶** klicken. Die App startet mit der Anzeigenliste und sechs Beispielanzeigen.

## Build über die Kommandozeile

Die Kommandozeile braucht den Pfad zum Android SDK. Entweder vorher einmal in Android Studio öffnen (legt `local.properties` an) oder die Umgebungsvariable `ANDROID_HOME` auf den SDK-Ordner setzen (unter Windows meist `%LOCALAPPDATA%\Android\Sdk`).

Im Projektordner (Windows: `gradlew.bat` statt `./gradlew`):

```
./gradlew assembleDebug          # APK bauen: app/build/outputs/apk/debug/app-debug.apk
./gradlew installDebug           # APK auf ein verbundenes Gerät / einen Emulator installieren
./gradlew testDebugUnitTest      # JUnit-Tests ausführen
```

## Tests

Die Logik ohne Android-Abhängigkeit ist mit JUnit getestet (`app/src/test`):

| Testklasse | Prüft |
|---|---|
| `AdSearchTest` | Suche: leerer Suchtext, Groß-/Kleinschreibung, Treffer in Titel/Beschreibung/Ort, keine Treffer |
| `AdValidationTest` | Eingabeprüfung: Titel, Übergabeort, Preis (z. B. „12,50“ gültig; „-5“, „1e3“, „12,505“ ungültig) |
| `PriceFormatTest` | Preisanzeige im deutschen Format („12,50 €“) |

In Android Studio: Rechtsklick auf `app/src/test/java` → **Run 'Tests in …'**.

## Hinweise zum Emulator

- **Kamera:** Die Emulator-Kamera liefert ein Testbild. Zum Abbrechen ohne Foto die Zurück-Geste oder den ◁-Knopf in der Seitenleiste des Emulators benutzen.
- Stürzt der Emulator beim Öffnen der Kamera ab: **Device Manager → ⋮ → Edit → Advanced Settings → Back camera: Emulated**, danach **Cold Boot Now**.
- **Beispieldaten zurücksetzen:** Einstellungen → Apps → CampusMarket → Speicher → Speicher leeren.

## Aufbau des Codes

Schichten: **UI → ViewModel → Repository → Room (DAO)**. Jeder Screen besteht aus einer Route (holt den ViewModel), einem zustandslosen Screen-Composable und einem ViewModel mit einer `UiState`-Klasse.

```
app/src/main/java/de/dhbw/campusmarket/
├── CampusMarketApplication.kt   Datenbank und Repository (genau einmal), Beispieldaten
├── MainActivity.kt              setzt Theme und NavHost
├── data/                        Room: Entity Ad, AdDao, AppDatabase, AdRepository, Enums, Beispieldaten
├── logic/                       reine Kotlin-Funktionen: Suche, Eingabeprüfung (mit JUnit-Tests)
└── ui/
    ├── navigation/              typsichere Ziele und NavHost
    ├── adlist/                  Anzeigenliste mit Suche
    ├── addetail/                Detailansicht mit Löschen
    ├── adform/                  Formular zum Erstellen, Kamera
    ├── photoviewer/             Foto im Vollbild
    └── theme/                   Farben, Schrift, Theme
```

Die Regeln für Architektur und Code stehen in [CLAUDE.md](CLAUDE.md).

## KI-Nutzung

Bei der Entwicklung wurde KI (Claude Code) eingesetzt. Alle Prompts und Ergebnisse sind vollständig in [AI_USAGE.md](AI_USAGE.md) dokumentiert.

Die Gruppe verantwortet jede Zeile des Projekts.
