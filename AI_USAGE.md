# KI-Nutzung im Projekt CampusMarket

Dieses Dokument legt die gesamte KI-Nutzung beider Gruppenmitglieder offen.
Alle Prompts stehen wortwörtlich, in Originalsprache und in der Reihenfolge, in der sie gesendet wurden.
Geheimnisse und personenbezogene Daten sind durch `[REDACTED]` ersetzt. Angehängte Dateien werden nur mit Namen genannt.

Hinweis zu den Links: Nach der Umbenennung des Pakets (Eintrag 8) zeigten Datei-Links in älteren Einträgen auf Ordner, die es nicht mehr gibt. In Eintrag 11 wurde deshalb nur das Linkziel (der Teil in runden Klammern) von `com/example/campusmarket` auf `de/dhbw/campusmarket` umgestellt. Der sichtbare Text aller Einträge ist unverändert.

Hinweis zum Format: Seit der Regeländerung in Eintrag 18 (24.11.2026) steht bei jedem Prompt nur noch, was die KI getan hat (ausgeführte Aktionen) und ihr Kurzfazit. Die ausführlichen Antworttexte der KI wurden in Eintrag 19 aus allen älteren Einträgen entfernt. Stattdessen steht unter „Commits“, in welchen Commit (Hash und Nachricht) das Ergebnis eingeflossen ist. Die Prompts selbst sind unverändert.

---

## 1. Setup

| Punkt                   | Angabe                                                                                                                                                                                                                                                                                                                |
|-------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| IDE und Umgebung        | Android Studio (Gradle 9.6.0, Android Gradle Plugin 9.4.1, Kotlin 2.2.10), Windows 11                                                                                                                                                                                                                                 |
| Assistenten und Modelle | Claude Code (Desktop-App, Code-Tab), Modell Claude Opus 5.5 (`claude-opus-5-5`)                                                                                                                                                                                                                                       |
| Tools und Erweiterungen | eintragen (z. B. weitere Plugins in Android Studio, falls genutzt)                                                                                                                                                                                                                                                    |
| Instruction-File        | [`CLAUDE.md`](CLAUDE.md) im Root des Repos                                                                                                                                                                                                                                                                            |
| Teamregeln              | Jeder Prompt wird sofort und wortwörtlich hier eingetragen. Die KI committet und pusht nie selbst; Commits machen wir, damit beide Mitglieder sinnvolle Commits haben. Jede Änderung wird von uns gelesen, verstanden und getestet, bevor sie committet wird. Jedes Mitglied muss jede Zeile ohne KI erklären können. |

---

## 2. Einträge

### Eintrag 1: Projekt-Setup, CLAUDE.md, AI_USAGE.md und Schrittplan

- **Datum:** 04.11.2026
- **Autor:** Benjamin & Semih
- **Tool:** Claude Code (Desktop-App), Claude Opus 5.5

#### Prompt 1

````text
```
Du arbeitest in unserem Android-Studio-Projekt „CampusMarket“ mit. Lies dir diese Nachricht vollständig durch, bevor du irgendetwas tust.

Kontext
Wir sind zwei Studierende (duales Studium Informatik, DHBW Mannheim) im Modul „Entwicklung mobiler Applikationen“. Wir bauen eine native Android-App als Prüfungsprojekt. Abgabe ist der 08.12.2026 über GitHub. Es zählt der letzte Commit vor der Deadline. Danach folgt eine Code Defence: Jeder von uns muss ohne KI und ohne Internet jede Zeile erklären können.
Deshalb gilt: Schreibe einfachen, gut lesbaren Code auf Studierenden-Niveau. Keine unnötigen Abstraktionen, keine ausgefallenen Konstrukte, keine Bibliotheken, die wir nicht brauchen. Erkläre nach jeder Änderung kurz auf Deutsch, was sie tut und warum.

Die App CampusMarket ist ein Marktplatz, auf dem Studierende gebrauchte Gegenstände (Bücher, Skripte, Taschenrechner, Technik, Möbel) anbieten und finden. Der Dozent hat das Thema genehmigt, mit dem Fokus, dass nur der Client, zunächst statisch ohne Server, erstellt werden soll, weil ein Backend den Rahmen sprengen würde. Es gibt also kein Netzwerk, keinen Login und kein Backend.

Angemeldeter Funktionsumfang:
- Anzeigenübersicht mit Suche, Filter und Sortierung
- Detailansicht mit Produktinfos, Bild, Zustand und Übergabeort
- Anzeigen erstellen, bearbeiten und löschen
- Lokale Speicherung der Angebote
- Favoritenfunktion
- Status verfügbar/verkauft
- Produktfotos mit der Gerätekamera

Mindestanforderungen
Ist eine davon nicht erfüllt, ist das Projekt nicht bestanden.
1. Die App baut und läuft aus einem sauberen Checkout, mit den Schritten im README.
2. Mindestens zwei Screens mit Navigation. Der State überlebt eine Bildschirmdrehung.
3. Echte Daten, hier aus einer lokalen Room-Datenbank.
4. Eine Gerätefunktion hinter einer Permission (Kamera). Die App funktioniert weiter, wenn die Permission verweigert wird.
5. Im Flugmodus: kein Absturz, kein eingefrorener Screen.

Was außerdem bewertet wird:
- Vollständige Features und keine Abstürze bei leerer Eingabe, verweigerter Permission oder Rotation.
- UI, State und Daten sind getrennt. Jeder State hat genau einen Besitzer.
- Jeder ladende Screen hat einen Loading-, Error- und Ready-Zustand.
- Die UX ist konsistent und folgt den Android-Konventionen.
- Kleine Einheiten, kein toter Code, Unit-Tests für die Logik.
- Sinnvolle Commits von beiden Mitgliedern. README mit dem Satz: „Die Gruppe verantwortet jede Zeile des Projekts.“

Technische Vorgaben
- Kotlin, Jetpack Compose, Material 3, minSdk 26.
- Architektur: UI → ViewModel → Repository → Room.
- Jeder Screen hat einen ViewModel mit einer UiState-data-class als StateFlow (MutableStateFlow + update).
- Nur der ViewModel ändert den State.
- Jeder Screen besteht aus einer Route (holt den ViewModel) und einem stateless Screen-Composable (nur Werte und Callbacks) mit Preview.
- Typsichere Navigation Compose mit @Serializable-Destinations. Übergeben wird nur die id. Nur der NavHost navigiert.
- Room mit KSP. Die Datenbank ist die Single Source of Truth, und das DAO liefert Flows. Datenbank und Repository werden einmal in einer eigenen Application-Klasse erzeugt. Beim ersten Start werden Beispieldaten eingefügt. (Kein Hilt und kein Koin) 
ViewModels werden über viewModelFactory { initializer { ... } } gebaut.
- Kamera:
- Erst RequestPermission(CAMERA) 

- bei Erfolg: ActivityResultContracts.TakePicture mit FileProvider.
- bei Ablehnung erscheint ein Hinweis, und Speichern ohne Foto bleibt möglich.
- Bilder werden mit Coil angezeigt.
- Such-, Filter- und Sortierlogik als reine Kotlin-Funktionen mit JUnit-Tests.

KI-Dokumentation 
Die Nutzung von KI ist erlaubt, muss aber vollständig offengelegt werden. Nicht offengelegte Nutzung gilt als Täuschungsversuch.

AI_USAGE.md liegt im Root des Repos, auf Markdown. Sie enthält ALLE Prompts beider Mitglieder an jedes Tool, in der Reihenfolge, in der sie gesendet wurden, in der Originalsprache. Nichts wird zusammengefasst, gekürzt, übersetzt oder geglättet. Das gilt auch für Rückfragen, Korrekturen, Verständnisfragen, verworfene Ergebnisse und Prompts für Texte wie README oder Logbuch. Geheimnisse und personenbezogene Daten werden durch [REDACTED] ersetzt. Angehängte Dateien werden nur mit Namen genannt.

Aufbau von AI_USAGE.md:
1. Setup: IDE und Umgebung, Assistenten und Modelle, Tools und Erweiterungen, Instruction-File, Teamregeln.
2. Einträge: Einer pro Aufgabe. Jeder Eintrag enthält:
   - Überschrift
   - Datum, Autor, Tool
   - alle Prompts wortwörtlich
   - Akzeptiert / Geändert / Abgelehnt, jeweils mit Begründung
   - Geprüft / Getestet
   - Commits
3. Reflexion: kommt am Ende, 1–2 Seiten. Lege dafür jetzt nur einen Platzhalter an.

Deine Regeln dafür:
- Trage jeden Prompt, den ich dir in dieser Sitzung schicke, sofort und wortwörtlich in AI_USAGE.md ein, bevor du die Aufgabe bearbeitest. Das beginnt mit DIESER Nachricht.
- Neue Aufgabe = neuer Eintrag. Rückfragen zur selben Aufgabe werden als Prompt 2, 3, … in denselben Eintrag geschrieben.
- Felder, die nur wir kennen (Autor, Akzeptiert/Geändert/Abgelehnt, Geprüft, Commits), lässt du als "eintragen" stehen, oder du schlägst Text vor, den wir bestätigen.
- Frag mich beim ersten Mal, wie die Autoren heißen.
- Committe nicht selbst. Wir committen, damit beide Mitglieder sinnvolle Commits haben. Schlage nach jedem Schritt eine Commit-Message vor.

Außerdem brauchen wir eine CLAUDE.md im Root mit konkreten, prüfbaren Architektur- und Code-Regeln. Sie enthält die Vorgaben oben, „Nur Room-Zugriffe im Repository/DAO“, „Jeder ladende Screen hat Loading/Error/Ready“, „Kein Code ohne kurze Erklärung“ und „Prompts immer wortwörtlich ins Logbuch“. Halte dich daran und aktualisiere die Datei, wenn sich Regeln ändern.

Vorgehen
1. Schau dir zuerst das bestehende Projekt an: Paketname, Theme-Name, Gradle-Dateien und Versionen. Lege dann CLAUDE.md und AI_USAGE.md an und trage diese Nachricht als Eintrag 1 ein.
2. Schlage mir danach einen Plan in kleinen Schritten vor, die jeweils für sich bauen und als eigener Commit taugen. Zuerst kommen die Mindestanforderungen (Room-Datenschicht, Liste mit Suche, Detail mit Löschen, Erstellen mit Kamera, Tests, README), danach die übrigen Features (Bearbeiten, Favoriten, Status, Filter, Sortierung, Galerie-Fallback). Warte auf mein OK, bevor du Code schreibst.
3. Nach jedem Schritt gibst du mir eine kurze Erklärung, Testanleitung (Rotation, Permission verweigert, Flugmodus, App-Neustart, ungültige Eingaben) und Commit-Vorschlag. Danach wartest du auf mich.
Führe niemals selbst Git-Commits oder Git-Pushes durch.
```
````

#### Ergebnis der KI zu Prompt 1

**Ausgeführte Aktionen:** Dateiliste und Gradle-Dateien gelesen (`settings.gradle.kts`, `build.gradle.kts`, `app/build.gradle.kts`, `gradle/libs.versions.toml`, `gradle.properties`, `gradle-wrapper.properties`), außerdem `MainActivity.kt`, `AndroidManifest.xml`, `themes.xml`, `strings.xml`, `Theme.kt`, `gradle-daemon-jvm.properties`, `rules.keep`. Danach `AI_USAGE.md` und `CLAUDE.md` neu angelegt. Kein App-Code geändert.

**Kurzfazit im Antwortfeld der App:** „CLAUDE.md und AI_USAGE.md liegen jetzt im Root, deine Nachricht steht wortwörtlich als Eintrag 1 drin. Am App-Code habe ich noch nichts geändert, und es ist nichts committet. Die Autorennamen fehlen noch.“ Frage: „Passt der Plan so, darf ich mit Schritt 1 anfangen?“

#### Prompt 2 (11.11.2026)

````text
Ändere deine Anleitung so, dass du dein komplettes Ergebnis in die AI-USAGE einfügst. Mache das rückwirkend auch für die erste Nachricht.
Danach fangen wir mit Schritt 1 an
````

#### Ergebnis der KI zu Prompt 2

Siehe Eintrag 2. Die Regeländerung aus Prompt 2 und Schritt 1 wurden dort gemeinsam bearbeitet.

#### Akzeptiert / Geändert / Abgelehnt

- **Akzeptiert:** CLAUDE.md und AI_USAGE.md übernommen, weil sie die Vorgaben aus der Aufgabenstellung abbilden.
- **Geändert:** nichts
- **Abgelehnt:** nichts

#### Geprüft / Getestet

Beide Dateien gelesen und mit der Aufgabenstellung abgeglichen. Kein Build nötig, da kein Code geändert wurde.

#### Commits

- `b645de5` CLAUDE.md und AI_USAGE.md anlegen

---

### Eintrag 2: Schritt 1 – Build-Setup (minSdk 26, Room + KSP, Navigation, Serialization, ViewModel-Compose, Coil)

- **Datum:** 11.11.2026
- **Autor:** Benjamin
- **Tool:** Claude Code (Desktop-App), Claude Opus 5.5

#### Prompt 1

Derselbe Prompt wie Eintrag 1, Prompt 2 (dort wortwörtlich). Der letzte Satz „Danach fangen wir mit Schritt 1 an“ hat diesen Eintrag ausgelöst.

#### Ergebnis der KI zu Prompt 1

**Ausgeführte Aktionen:**

1. `git status`, `git log` und Teile von `AI_USAGE.md` gelesen, `CLAUDE.md` nach der neuen Regel durchsucht. Ergebnis: Prompt 2, das Ergebnis zu Prompt 1 und die CLAUDE.md-Regel 2 waren schon aus einem früheren Lauf eingetragen; die Autoren hat das Team eingetragen.
2. `AI_USAGE.md` ab Zeile 100 gelesen.
3. `git diff` angesehen und den lokalen Gradle-Cache nach vorhandenen Bibliotheken durchsucht (Navigation 2.10.2 vorhanden).
4. Aktuelle Versionen von Maven Central / Google Maven abgefragt (`curl` auf `maven-metadata.xml`): Room 2.8.5, KSP 2.3.13, Coil 3.6.3, kotlinx-serialization-json 1.12.0, Navigation 2.10.2.
5. `gradle/libs.versions.toml`, `build.gradle.kts` und `app/build.gradle.kts` per Python-Skript geändert (Versionen, Bibliotheken, Plugins, minSdk 24 → 26).
6. `room-ktx` per `sed` wieder entfernt.
7. `gradlew.bat assembleDebug testDebugUnitTest` ausgeführt. Ergebnis: **BUILD FAILED**, „Class 'kotlin.Unit' was compiled with an incompatible version of Kotlin. The actual metadata version is 2.4.0, but the compiler version 2.2.0 can read versions up to 2.3.0.“
8. `gradlew.bat :app:dependencyInsight` ausgeführt. Ergebnis: `coil-compose-core-android:3.6.3` zieht `kotlin-stdlib:2.4.10` und Compose 1.12.
9. POM-Dateien von Coil 3.2.0 bis 3.5.0 abgefragt. Ergebnis: 3.3.0 → Kotlin 2.2.0, 3.4.0 → Kotlin 2.3.10, 3.5.0 → Kotlin 2.4.0.
10. Coil per `sed` auf 3.3.0 gesetzt, `gradlew.bat assembleDebug testDebugUnitTest` erneut ausgeführt. Ergebnis: **BUILD SUCCESSFUL**.
11. `CLAUDE.md` (Projektdaten: Versionsregel für Coil, keine überflüssigen Bibliotheken) und `AI_USAGE.md` (dieser Eintrag) geändert.

**Kurzfazit im Antwortfeld der App:** „Schritt 1 ist umgesetzt: minSdk 26 und die Bibliotheken für Room, Navigation und Coil sind drin, Debug-Build und Unit-Tests laufen durch. Coil steht auf 3.3.0, weil neuere Versionen Kotlin 2.4 brauchen. Es ist nichts committet.“ Frage: „Soll ich mit Schritt 2 (Room-Datenschicht) weitermachen?“

#### Akzeptiert / Geändert / Abgelehnt

- **Akzeptiert:** Ganze Antwort
- **Geändert:** Coil von 3.6.3 auf 3.3.0 gesenkt, weil 3.6.3 mit Kotlin 2.2 nicht baut
- **Abgelehnt:** nichts

#### Geprüft / Getestet

`gradlew assembleDebug testDebugUnitTest` erfolgreich; App startet auf Emulator API ≥ 26.

#### Commits

- `42f843c` Regel für vollständige KI-Ergebnisse im Logbuch
- `3b78ba6` minSdk 26 und Abhängigkeiten für Room, Navigation und Coil

---

### Eintrag 3: Schritt 2 – Room-Datenschicht (Entity, DAO, Datenbank, Repository, Application-Klasse, Beispieldaten)

- **Datum:** 17.11.2026
- **Autor:** Semih
- **Tool:** Claude Code (Desktop-App), Claude Opus 5.5

#### Prompt 1

````text
Ja, Schritt 2
````

#### Ergebnis der KI zu Prompt 1

**Ausgeführte Aktionen:**

1. `git status`, `git log`, die Überschriften von `AI_USAGE.md` und die Root-`.gitignore` gelesen. Ergebnis: Schritt 1 ist committet (`3b78ba6`), `app/.gitignore` geändert, `.idea/` weiter untracked.
2. `AI_USAGE.md`: Eintrag 3 mit dem Prompt angelegt.
3. Neu angelegt: `data/Category.kt`, `data/Condition.kt`, `data/Ad.kt`, `data/AdDao.kt`, `data/AppDatabase.kt`, `data/AdRepository.kt`, `data/SampleData.kt`, `CampusMarketApplication.kt` (alle unter `app/src/main/java/com/example/campusmarket/`).
4. Geändert: `app/src/main/AndroidManifest.xml` (`android:name=".CampusMarketApplication"`).
5. `gradlew.bat assembleDebug testDebugUnitTest` ausgeführt. Ergebnis: **BUILD SUCCESSFUL**. KSP hat `AdDao_Impl.kt` und `AppDatabase_Impl.kt` erzeugt.
6. `adb devices` ausgeführt. Ergebnis: kein Gerät/Emulator verbunden, daher kein Laufzeittest.
7. `AI_USAGE.md`: dieses Ergebnis eingetragen.

**Kurzfazit im Antwortfeld der App:** „Schritt 2 ist umgesetzt: Entity, DAO, Datenbank, Repository und CampusMarketApplication mit Beispieldaten sind angelegt, der Build läuft durch. Ob die Beispieldaten wirklich in der Datenbank landen, konnte ich nicht prüfen, weil kein Emulator lief. Es ist nichts committet.“ Frage: „Soll ich mit Schritt 3 (Navigation-Grundgerüst) weitermachen?“

#### Akzeptiert / Geändert / Abgelehnt

- **Akzeptiert:** Ganze Antwort
- **Geändert:** nichts
- **Abgelehnt:** nichts

#### Geprüft / Getestet

Build erfolgreich; im Database Inspector 6 Zeilen in `ads`, nach Neustart weiter 6.

#### Commits

- `1982a7c` Room-Datenschicht mit Repository und Beispieldaten.

---

### Eintrag 4: Schritt 3 – Navigation-Grundgerüst (Destinations, NavHost, Beispielcode entfernen)

- **Datum:** 18.11.2026
- **Autor:** Benjamin
- **Tool:** Claude Code (Desktop-App), Claude Opus 5.5

#### Prompt 1

````text
Schau dir nochmal das komplette Projekt an und mach dann weiter mit Schritt 3
````

#### Ergebnis der KI zu Prompt 1

**Ausgeführte Aktionen:**

1. `git ls-files`, `git log --stat -3` und `git status` ausgeführt. Ergebnis: Schritt 2 ist committet (`1982a7c`), nur `.idea/` ist untracked.
2. Gelesen: `AI_USAGE.md`, alle Gradle-Dateien (`app/build.gradle.kts`, `build.gradle.kts`, `gradle/libs.versions.toml`, `settings.gradle.kts`), beide `.gitignore`, `AndroidManifest.xml`, `CampusMarketApplication.kt`, `MainActivity.kt`, alle Dateien unter `data/`, `strings.xml`, `themes.xml`, `Theme.kt`, `ExampleUnitTest.kt`, `ExampleInstrumentedTest.kt`, `rules.keep`.
3. `git show --stat 82ad9da` und `ls .idea` ausgeführt. Ergebnis: Der Commit hat `.idea/` aus dem Repo entfernt, aber keine Ignore-Regel hinzugefügt, die den ganzen Ordner abdeckt.
4. `AI_USAGE.md`: Eintrag 4 mit dem Prompt angelegt.
5. Neu angelegt: `ui/navigation/Destinations.kt`, `ui/navigation/AppNavHost.kt`, `ui/adlist/AdListScreen.kt`, `ui/addetail/AdDetailScreen.kt` (unter `app/src/main/java/com/example/campusmarket/`) und `app/src/main/res/drawable/ic_arrow_back.xml`.
6. Geändert: `MainActivity.kt` (`Greeting` und `GreetingPreview` entfernt, ruft jetzt `AppNavHost()` auf), `res/values/strings.xml` (fünf neue Texte).
7. `gradlew.bat assembleDebug testDebugUnitTest` ausgeführt. Ergebnis: **BUILD FAILED**, „resource attr/colorControlNormal … not found“ in `ic_arrow_back.xml`.
8. Die Zeile `android:tint="?attr/colorControlNormal"` aus `ic_arrow_back.xml` entfernt und den Kommentar ergänzt.
9. `gradlew.bat assembleDebug testDebugUnitTest` erneut ausgeführt. Ergebnis: **BUILD SUCCESSFUL**, keine Kotlin-Warnungen.
10. `adb devices` ausgeführt. Ergebnis: kein Gerät/Emulator verbunden, daher kein Laufzeittest.
11. `CLAUDE.md` (Projektdaten: Regel für Icons als Vector-Drawable) und `AI_USAGE.md` (dieses Ergebnis) geändert.

**Kurzfazit im Antwortfeld der App:** „Schritt 3 ist umgesetzt: typsichere Navigation mit AdList und AdDetail(adId), NavHost in der MainActivity, der Beispielcode Greeting ist weg. Debug-Build und Unit-Tests laufen durch; auf einem Gerät konnte ich es nicht testen, weil kein Emulator lief. Es ist nichts committet.“ Frage: „Soll ich mit Schritt 4 (Liste mit ViewModel) weitermachen?“

#### Akzeptiert / Geändert / Abgelehnt

- **Akzeptiert:** Ganze Antwort
- **Geändert:** Keine Änderung
- **Abgelehnt:** Nichts

#### Geprüft / Getestet

App erfolgreich ohne Fehler gebaut

#### Commits

- `faa076a` Typsichere Navigation mit Liste und Detail als Platzhalter

---

### Eintrag 5: Schritt 4 – Anzeigenliste mit ViewModel und UiState (Loading/Error/Ready)

- **Datum:** 18.11.2026
- **Autor:** Benjamin
- **Tool:** Claude Code (Desktop-App), Claude Opus 5.5

#### Prompt 1

````text
Ja, Schritt 4
````

#### Ergebnis der KI zu Prompt 1

**Ausgeführte Aktionen:**

1. `git status`, `git log --oneline -3` und `git diff .gitignore` ausgeführt, außerdem Eintrag 4 und das Ende von `AI_USAGE.md` gelesen. Ergebnis: Schritt 3 ist committet (`faa076a`), die Felder in Eintrag 4 hat das Team ausgefüllt, und in der Root-`.gitignore` steht neu `.idea/` (noch nicht committet).
2. `AI_USAGE.md`: Eintrag 5 mit dem Prompt angelegt.
3. Neu angelegt: `ui/adlist/AdListUiState.kt`, `ui/adlist/AdListViewModel.kt`.
4. Geändert: `ui/adlist/AdListScreen.kt` (Platzhalter ersetzt durch `AdListRoute` und den stateless `AdListScreen` mit Loading/Error/Leer/Liste und zwei Previews), `ui/navigation/AppNavHost.kt` (ruft `AdListRoute` auf), `res/values/strings.xml` (Platzhalter-Texte raus, Texte für Fehler, „Erneut versuchen“, leere Liste und Preisformat rein).
5. Die private Composable `AdList` per `sed` in `AdListContent` umbenannt, damit sie nicht mit der Destination `AdList` verwechselt wird.
6. `gradlew.bat assembleDebug testDebugUnitTest` ausgeführt. Ergebnis: **BUILD SUCCESSFUL**, keine Warnungen.
7. `adb devices` ausgeführt. Ergebnis: `emulator-5554` verbunden.
8. APK mit `adb install -r` installiert, App gestartet, Screenshot gemacht. Ergebnis: 6 Anzeigen sichtbar, aber Preise als „15.00 €“, weil der Emulator auf Englisch steht.
9. Neu angelegt: `ui/PriceFormat.kt` (`formatPrice()` mit `Locale.GERMANY`) und `app/src/test/java/com/example/campusmarket/ui/PriceFormatTest.kt` (3 Tests). `AdListScreen.kt` nutzt jetzt `formatPrice()`, der String `price_format` und sein Kommentar sind per `sed` aus `strings.xml` entfernt.
10. `app/src/test/java/com/example/campusmarket/ExampleUnitTest.kt` gelöscht, weil es jetzt einen echten Unit-Test gibt.
11. `gradlew.bat assembleDebug testDebugUnitTest` ausgeführt. Ergebnis: **BUILD SUCCESSFUL**, `PriceFormatTest`: 3 Tests, 0 Fehler.
12. Auf dem Emulator: dritte Anzeige angetippt, auf Querformat gedreht, Zurück gedrückt, Screenshots gemacht. Ergebnis: „Anzeige Nr. 3“ bleibt nach der Drehung stehen, Zurück führt zur Liste, Preise als „15,00 €“. Kein Eintrag unter `AndroidRuntime:E`. Danach wieder Hochformat eingestellt (automatisches Drehen bleibt im Emulator aus).
13. Flugmodus eingeschaltet (`cmd connectivity airplane-mode enable`), App beendet und neu gestartet, Bildschirminhalt mit `uiautomator dump` gelesen. Ergebnis: Liste mit den Beispielanzeigen, kein Absturz. Flugmodus wieder ausgeschaltet. (Der erste Versuch scheiterte, weil Git Bash den Pfad `/sdcard/ui.xml` umgeschrieben hat; mit `MSYS_NO_PATHCONV=1` hat es geklappt.)
14. `AI_USAGE.md`: dieses Ergebnis eingetragen.

**Kurzfazit im Antwortfeld der App:** „Schritt 4 ist umgesetzt: Die Startseite zeigt jetzt die Anzeigen aus Room über AdListViewModel und AdListUiState, mit Loading, Fehler mit „Erneut versuchen“, leerer Liste und Liste. Build, 3 Unit-Tests und ein Test auf dem Emulator (Antippen, Drehung, Zurück, Flugmodus, Neustart) liefen ohne Fehler. Es ist nichts committet.“ Frage: „Soll ich mit Schritt 5 (Suche mit Tests) weitermachen?“

#### Akzeptiert / Geändert / Abgelehnt

- **Akzeptiert:** Ganze Antwort
- **Geändert:** Nichts geändert
- **Abgelehnt:** Nichts

#### Geprüft / Getestet

App gebaut und getestet, keine Fehler

#### Commits

- `b310001` .idea komplett ignorieren
- `ccd81aa` Anzeigenliste mit ViewModel, UiState und Preisformat

---

### Eintrag 6: Schritt 5 – Suche als reine Funktion mit JUnit-Tests und Suchfeld in der Liste

- **Datum:** 19.11.2026
- **Autor:** Semih
- **Tool:** Claude Code (Desktop-App), Claude Opus 5.5

#### Prompt 1

````text
Ja, Schritt 5
````

#### Ergebnis der KI zu Prompt 1

**Ausgeführte Aktionen:**

1. `git status`, `git log --oneline -4` und `git diff HEAD --stat` ausgeführt, `AdListScreen.kt` und das Ende von `AI_USAGE.md` gelesen. Ergebnis: Arbeitsverzeichnis sauber, Schritt 4 und die `.gitignore` sind committet (`b310001`, `ccd81aa`), die Felder in Eintrag 5 hat das Team ausgefüllt.
2. `AI_USAGE.md`: Eintrag 6 mit dem Prompt angelegt.
3. Neu angelegt: `logic/AdSearch.kt` (`searchAds()`), `app/src/test/java/com/example/campusmarket/logic/AdSearchTest.kt` (10 Tests), `res/drawable/ic_search.xml`, `res/drawable/ic_close.xml`.
4. Geändert: `ui/adlist/AdListUiState.kt` (`query` und berechnete Eigenschaft `visibleAds`), `ui/adlist/AdListViewModel.kt` (`onQueryChange()`), `res/values/strings.xml` (3 neue Texte), `ui/adlist/AdListScreen.kt` per Python-Skript (Suchfeld, `ReadyContent`, `MessageContent` statt `EmptyContent`, neuer Parameter `onQueryChange`, Preview „Keine Treffer“).
5. `gradlew.bat assembleDebug testDebugUnitTest` ausgeführt. Ergebnis: **BUILD SUCCESSFUL**, keine Warnungen. `AdSearchTest` 10/10, `PriceFormatTest` 3/3 bestanden.
6. Auf dem Emulator (`emulator-5554`) installiert und gestartet, Suchfeld per `uiautomator dump` gefunden, „RECHNER“ eingetippt. Ergebnis: Im Feld stand nur „RECH“, Treffer „Taschenrechner TI-30X“. Nach der Drehung kam die zweite Eingabe „Fahrrad“ nicht an.
7. Ursache gesucht: schnelles und langsames Tippen verglichen, Screenshot gemacht. Ergebnis: Die Stylus-Einführung von Gboard („Try out your stylus“) hatte sich über die App gelegt und die Eingaben abgefangen. Kein Fehler der App. Dialog mit „Cancel“ geschlossen.
8. Erneut getestet: „RECHNER“ kommt vollständig an → nur „Taschenrechner TI-30X“. Ins Querformat und zurück gedreht → Suchtext und Treffer bleiben. Kreuz angetippt → Feld leer, alle 6 Anzeigen. „Fahrrad“ → „Keine Treffer für „Fahrrad“.“. Drei Leerzeichen → alle 6 Anzeigen. Lupe auf der Tastatur angetippt → Tastatur geschlossen (`mInputShown=false`). `AndroidRuntime:E` leer, kein Absturz.
9. `AI_USAGE.md`: dieses Ergebnis eingetragen.

**Kurzfazit im Antwortfeld der App:** „Schritt 5 ist umgesetzt: searchAds() durchsucht Titel, Beschreibung und Übergabeort, 10 JUnit-Tests sind grün, und die Liste hat ein Suchfeld mit Kreuz und „Keine Treffer“-Hinweis. Auf dem Emulator liefen Suche, Drehung, Kreuz und Tastatur ohne Fehler. Es ist nichts committet.“ Frage: „Soll ich mit Schritt 6 (Detail mit Löschen) weitermachen?“

#### Akzeptiert / Geändert / Abgelehnt

- **Akzeptiert:** alles
- **Geändert:** nichts
- **Abgelehnt:** nichts

#### Geprüft / Getestet

App gebaut, Suchfilter getestet

#### Commits

- `95459bf` Suche in der Anzeigenliste mit JUnit-Tests

---

### Eintrag 7: Schritt 6 – Detailansicht mit Löschen, dazu Frage zum Paketnamen

- **Datum:** 19.11.2026
- **Autor:** Semih
- **Tool:** Claude Code (Desktop-App), Claude Opus 5.5

#### Prompt 1

````text
Ja, Schritt 6. Wir haben außerdem noch gesehen, dass unser Package com.example.campusmarket heißt. Kann man das einfach ändern?
````

#### Rückfrage der KI und Antwort (Prompt 2)

Die KI hat vor dem Start im Auswahldialog der App gefragt: „Schritt 5 (Suche) ist noch nicht committet. Schritt 6 ändert auch strings.xml, AI_USAGE.md und weitere Dateien, die Commits würden sich also vermischen. Wie wollt ihr vorgehen?“ Optionen: „Wir committen erst (Recommended)“ / „Trotzdem weitermachen“.

Antwort im Dialog (Freitext):

````text
Jetzt ist der commit durch
````

#### Ergebnis der KI zu Prompt 1 und 2

**Ausgeführte Aktionen:**

1. `git status`, `git log`, `git diff HEAD~1 --stat` und `git diff --ignore-cr-at-eol` ausgeführt, das Ende von `AI_USAGE.md` gelesen und alle Gradle-/XML-Dateien nach `campusmarket` durchsucht. Ergebnis: Schritt 5 war noch nicht committet. Die Änderung an `AdListScreen.kt` waren nur Zeilenenden (CRLF/LF), inhaltlich identisch. Der Paketname steht in `app/build.gradle.kts` (`namespace`, `applicationId`).
2. Rückfrage im Auswahldialog gestellt (siehe Prompt 2). Antwort: „Jetzt ist der commit durch“.
3. `git status` und `git log --oneline -2` ausgeführt. Ergebnis: Commit `95459bf` „Suche in der Anzeigenliste mit JUnit-Tests“, Arbeitsverzeichnis sauber.
4. `AI_USAGE.md`: Eintrag 7 mit beiden Prompts angelegt.
5. Neu angelegt: `ui/StateContent.kt` (`LoadingContent`, `ErrorContent`, `MessageContent`), `ui/Labels.kt` (deutsche Texte für `Category` und `Condition`), `ui/addetail/AdDetailUiState.kt`, `ui/addetail/AdDetailViewModel.kt`, `res/drawable/ic_delete.xml`.
6. Geändert: `ui/adlist/AdListScreen.kt` per Python-Skript (eigene Loading-/Error-/Message-Composables entfernt, nutzt jetzt die aus `StateContent.kt`), `ui/addetail/AdDetailScreen.kt` (Platzhalter ersetzt durch `AdDetailRoute`, stateless `AdDetailScreen`, Lösch-Dialog, zwei Previews), `res/values/strings.xml` per Python-Skript (`ad_detail_placeholder` entfernt, 23 Texte neu), `ui/navigation/AppNavHost.kt` (ruft `AdDetailRoute` auf).
7. `gradlew.bat assembleDebug testDebugUnitTest` ausgeführt. Ergebnis: **BUILD SUCCESSFUL**, keine Warnungen.
8. Auf dem Emulator (`emulator-5554`) installiert, „Taschenrechner TI-30X“ geöffnet, Screenshot gemacht. Ergebnis: Titel, Preis, Kategorie „Taschenrechner“, Zustand „Gut“, Übergabeort, Status „Verfügbar“, Beschreibung, Mülleimer oben rechts.
9. Mülleimer angetippt → Dialog „Anzeige löschen?“. Mit offenem Dialog ins Querformat gedreht → Dialog bleibt offen. Das Hilfsskript zum Antippen von „Abbrechen“ brach mit einem Bash-Rechenfehler ab (Fehler im Skript, nicht in der App) und wurde korrigiert.
10. „Abbrechen“ → Anzeige bleibt. Zurück ins Hochformat, erneut Mülleimer, „Löschen“ → zurück in der Liste, nur noch 5 Anzeigen. App beendet und neu gestartet → weiter 5 Anzeigen. `AndroidRuntime:E` leer.
11. `AI_USAGE.md`: dieses Ergebnis eingetragen.

**Kurzfazit im Antwortfeld der App:** „Schritt 6 ist umgesetzt: Die Detailseite lädt die Anzeige über AdDetailViewModel mit Loading/Error/Ready, und Löschen läuft über einen Bestätigungsdialog. Build und Emulator-Test (Dialog, Drehung, Abbrechen, Löschen, Neustart) liefen fehlerfrei. Der Paketname lässt sich ändern, das ist aber ein eigener Schritt. Es ist nichts committet.“ Frage: „Soll ich als Nächstes den Paketnamen ändern?“

#### Akzeptiert / Geändert / Abgelehnt

- **Akzeptiert:** alles
- **Geändert:** nichts
- **Abgelehnt:** nichts

#### Geprüft / Getestet

App gebaut, Löschfunktion getestet

#### Commits

- `ebec4e4` Detailansicht mit Löschen und Bestätigungsdialog

---

### Eintrag 8: Paketname auf `de.dhbw.campusmarket` ändern, Frage zum Projektfortschritt

- **Datum:** 22.11.2026
- **Autor:** eintragen
- **Tool:** Claude Code (Desktop-App), Claude Opus 5.5

#### Prompt 1

````text
Ja ändere den Paketnamen, dann Schritt 7. Wie viel Prozent vom Projekt sind momentan schon fertig?
````

Hinweis: Der Paketname `de.dhbw.campusmarket` stammt aus dem Vorschlag der KI in Eintrag 7 (Antwort-Button „Ja, de.dhbw.campusmarket“). Der Teil „dann Schritt 7“ wird in Eintrag 9 bearbeitet.

#### Ergebnis der KI zu Prompt 1

**Ausgeführte Aktionen (Paketname):**

1. `git status`, `git log --oneline -3`, `git diff HEAD --ignore-cr-at-eol --stat` ausgeführt, Ende von `AI_USAGE.md` gelesen, alle `.kt`-Dateien aufgelistet. Ergebnis: Schritt 6 ist committet (`ebec4e4`), Arbeitsverzeichnis sauber.
2. `AI_USAGE.md`: Eintrag 8 mit dem Prompt angelegt.
3. Ordner verschoben (`mv`, kein `git mv`): `app/src/main/java/com/example/campusmarket` → `app/src/main/java/de/dhbw/campusmarket`, ebenso unter `app/src/test`. Leere Ordner `com/example` gelöscht.
4. `app/src/androidTest/java/com/example/campusmarket/ExampleInstrumentedTest.kt` gelöscht (Beispielcode, prüfte fest auf `com.example.campusmarket`), der leere Ordner `app/src/androidTest` ist damit weg.
5. Per `sed` in allen 26 `.kt`-Dateien und in `app/build.gradle.kts` `com.example.campusmarket` durch `de.dhbw.campusmarket` ersetzt (`package`, `import`, `namespace`, `applicationId`). Danach `grep` nach `com.example`: keine Treffer mehr.
6. Nicht mehr gebrauchte Test-Bibliotheken entfernt: in `app/build.gradle.kts` `testInstrumentationRunner`, alle `androidTestImplementation`-Zeilen und `debugImplementation(libs.androidx.compose.ui.test.manifest)`; in `gradle/libs.versions.toml` `junitVersion`, `espressoCore`, `androidx-junit`, `androidx-espresso-core`, `androidx-compose-ui-test-manifest`, `androidx-compose-ui-test-junit4`. Leere Zeile in `defaultConfig` per Python entfernt.
7. `CLAUDE.md` geändert: Paketname `de.dhbw.campusmarket`; Regel zum Beispielcode ersetzt durch „Beispielcode ist entfernt, keine Instrumented Tests und keine Espresso-/Compose-Test-Bibliotheken“.
8. `gradlew.bat clean assembleDebug testDebugUnitTest` ausgeführt. Ergebnis: **BUILD SUCCESSFUL** (8 Minuten wegen `clean`), keine Warnungen, `de.dhbw.campusmarket.logic.AdSearchTest` 10/10 und `de.dhbw.campusmarket.ui.PriceFormatTest` 3/3 bestanden.
9. Auf dem Emulator `com.example.campusmarket` deinstalliert, neue APK installiert, gestartet. Erster Start: Emulator meldete „System UI isn't responding“ (Problem des Emulators). „Wait“ angetippt, App neu gestartet: Liste mit allen 6 Beispielanzeigen (neue, leere Datenbank), kein Absturz in Logcat.

Ältere Einträge in diesem Logbuch verlinken noch auf Pfade mit `com/example/campusmarket`. Sie bleiben so, weil sie den damaligen Stand dokumentieren.

#### Akzeptiert / Geändert / Abgelehnt

- **Akzeptiert:** eintragen
- **Geändert:** eintragen
- **Abgelehnt:** eintragen

#### Geprüft / Getestet

eintragen

#### Commits

- `db9f6a0` Paketname de.dhbw.campusmarket, Beispieltests entfernt

---

### Eintrag 9: Schritt 7 – Anzeige erstellen (Formular mit Validierung, ohne Foto)

- **Datum:** 22.11.2026
- **Autor:** Benjamin
- **Tool:** Claude Code (Desktop-App), Claude Opus 5.5

#### Prompt 1

Derselbe Prompt wie Eintrag 8, Prompt 1 (dort wortwörtlich). Der Teil „dann Schritt 7“ hat diesen Eintrag ausgelöst.

#### Rückfrage der KI und Antwort (Prompt 2)

Die KI hat nach der Umbenennung im Auswahldialog der App gefragt: „Die Umbenennung auf de.dhbw.campusmarket ist fertig: Clean Build und 13 Tests sind grün, und die App läuft auf dem Emulator. Damit sie ein eigener Commit bleibt, solltet ihr sie jetzt committen (Vorschlag: "refactor: Paketname de.dhbw.campusmarket, Beispieltest entfernt"). Sagt mir Bescheid, wenn der Commit durch ist, dann starte ich Schritt 7.“ Optionen: „Commit ist durch“ / „Ohne Commit weiter“.

Antwort im Dialog (gewählte Option):

````text
Commit ist durch
````

#### Ergebnis der KI zu Prompt 1 und 2

**Ausgeführte Aktionen (Schritt 7):**

1. `git status`, `git log --oneline -2`, `git show --stat HEAD` ausgeführt. Ergebnis: Umbenennung committet (`db9f6a0`), Git erkennt die verschobenen Dateien als Umbenennungen.
2. `AI_USAGE.md`: Eintrag 9 mit Verweis auf den Prompt und der Rückfrage angelegt.
3. Neu angelegt (unter `app/src/main/java/de/dhbw/campusmarket/`): `logic/AdValidation.kt` (`isValidTitle`, `isValidLocation`, `parsePrice`), `ui/adform/AdFormUiState.kt`, `ui/adform/AdFormViewModel.kt`, `ui/adform/AdFormScreen.kt`; außerdem `app/src/test/java/de/dhbw/campusmarket/logic/AdValidationTest.kt` (11 Tests) und `res/drawable/ic_add.xml`.
4. Per Python-Skript geändert: `ui/navigation/Destinations.kt` (neues Ziel `AdForm`), `ui/navigation/AppNavHost.kt` (`composable<AdForm>`, `onAddClick`), `ui/adlist/AdListScreen.kt` (FAB, Parameter `onAddClick`, auch in den 3 Previews), `res/values/strings.xml` (9 neue Texte).
5. `gradlew.bat assembleDebug testDebugUnitTest` ausgeführt. Ergebnis: **BUILD SUCCESSFUL**, keine Warnungen; `AdValidationTest` 11/11, `AdSearchTest` 10/10, `PriceFormatTest` 3/3.
6. Emulator: App installiert, FAB angetippt → Formular „Neue Anzeige“. Leer gespeichert → drei Fehlertexte (Titel, Preis, Übergabeort), Screenshot. Der Titel-Fehler „Bitte einen Titel mit höchstens 60 Zeichen eingeben.“ passte nicht zu einem leeren Feld und wurde per `sed` in „Bitte einen Titel eingeben (höchstens %1$d Zeichen).“ geändert.
7. Titel „Mathe Formelsammlung“, Preis „1e3“, Übergabeort „Mensa“, Kategorie „Skripte“, Zustand „Wie neu“ eingegeben und gespeichert → nur der Preisfehler erscheint.
8. Drehen per `user_rotation 1` hatte zuerst keine Wirkung, weil „Automatisch drehen“ im Emulator wieder an war. `accelerometer_rotation 0` gesetzt, danach Querformat (2400×1080): alle Eingaben, Chips und der Preisfehler bleiben erhalten.
9. Hochformat, Preis auf „7,50“ geändert, gespeichert → zurück in der Liste, „Mathe Formelsammlung · Mensa · 7,50 €“ steht oben. App beendet und neu gestartet → Anzeige ist noch da. Detail geöffnet → Kategorie „Skripte“, Zustand „Wie neu“. `AndroidRuntime:E` leer.
10. Dabei aufgefallen und behoben: Die Detailseite zeigte bei leerer Beschreibung die Überschrift „Beschreibung“ über leerem Text (`AdDetailScreen.kt`: jetzt nur bei `isNotBlank()`), und der FAB konnte den Preis der letzten Zeile verdecken (`AdListScreen.kt`: `contentPadding = PaddingValues(bottom = 88.dp)`).
11. `gradlew.bat assembleDebug testDebugUnitTest` erneut ausgeführt: **BUILD SUCCESSFUL**. Auf dem Emulator geprüft: Liste mit 7 Anzeigen und FAB, Detail ohne leere Überschrift.
12. `AI_USAGE.md`: Ergebnis in Eintrag 8 und hier eingetragen.

**Kurzfazit im Antwortfeld der App:** „Der Paketname ist jetzt de.dhbw.campusmarket (von euch committet), und Schritt 7 ist umgesetzt: FAB, Formular mit Validierung im ViewModel, 11 neue Tests. Build, 24 Tests und der Emulator-Test (Fehler, Drehung, Speichern, Neustart) liefen fehlerfrei. Das Projekt ist grob zu 45 % fertig. Schritt 7 ist nicht committet.“ Frage: „Soll ich mit Schritt 8 (Kamera) weitermachen?“

#### Akzeptiert / Geändert / Abgelehnt

- **Akzeptiert:** Alles
- **Geändert:** Nichts
- **Abgelehnt:** Nichts

#### Geprüft / Getestet

Neue Funktionen getestet, keine Fehler

#### Commits

- `b72b298` Anzeige erstellen mit Formular und Validierung

---

### Eintrag 10: Schritt 8 – Produktfoto mit der Kamera (Permission, FileProvider, Coil)

- **Datum:** 23.11.2026
- **Autor:** Benjamin
- **Tool:** Claude Code (Desktop-App), Claude Opus 5.5

#### Prompt 1

````text
Ja, Schritt 8
````

#### Ergebnis der KI zu Prompt 1

**Ausgeführte Aktionen:**

1. `git status`, `git log --oneline -3`, `git diff HEAD --ignore-cr-at-eol --stat` ausgeführt; Ende von `AI_USAGE.md`, `strings.xml`, `AndroidManifest.xml` und `res/xml` angesehen. Ergebnis: Schritt 7 ist committet (`b72b298`), Arbeitsverzeichnis sauber.
2. `AI_USAGE.md`: Eintrag 10 mit dem Prompt angelegt.
3. `AndroidManifest.xml` per Python geändert: `<uses-feature android.hardware.camera.any required="false">`, `<uses-permission CAMERA>`, `<provider androidx.core.content.FileProvider>` mit `${applicationId}.fileprovider`.
4. Neu angelegt: `res/xml/file_paths.xml`, `res/drawable/ic_image.xml`, `ui/adform/PhotoFile.kt` (`createPhotoUri`), `ui/AdImage.kt` (Coil-`AsyncImage` mit Platzhalter dahinter).
5. Ein Python-Skript direkt im Bash-Heredoc schlug fehl („unexpected EOF while looking for matching `''“); es wurde nichts geändert. Das Skript als `step8.py` im Scratchpad der Sitzung abgelegt und mit `python -I` ausgeführt. Geändert: `AdFormUiState.kt` (`imageUri`, `pendingPhotoUri`, `showPermissionHint`, `showNoCameraHint`), `AdFormViewModel.kt` (5 neue Funktionen, `imageUri` beim Speichern), `AdFormScreen.kt` (Launcher für `RequestPermission` und `TakePicture` in der Route, `PhotoSection`, Previews), `AdListScreen.kt` (Vorschaubild in jeder Zeile), `AdDetailScreen.kt` (großes Foto oben), `strings.xml` (6 neue Texte).
6. `gradlew.bat assembleDebug testDebugUnitTest` ausgeführt. Ergebnis: **BUILD SUCCESSFUL**, keine Warnungen.
7. Emulator: App installiert, Kamera-Berechtigung mit `pm revoke` und `pm clear-permission-flags` zurückgesetzt. FAB → „Foto aufnehmen“ → System-Dialog „Allow CampusMarket to take pictures and record video?“ → „Don’t allow“ → Hinweis „Ohne Kamera-Berechtigung …“ erscheint, Formular bleibt bedienbar.
8. Beim Ausfüllen hat das Testskript zweimal danebengelegen: Die Felder lagen wegen der neuen Foto-Fläche weiter unten, und „Zurück“ schloss nicht die Tastatur (Stylus-Leiste von Gboard), sondern verließ das Formular. Laut Logcat kein Absturz, die App lief weiter. Das Skript prüft jetzt, ob ein Element gefunden wurde, und benutzt kein „Zurück“ mehr.
9. Zweites Ablehnen → Hinweis. Dritter Tipp auf „Foto aufnehmen“ (Android fragt nach zweimal „Ablehnen“ nicht mehr) → kein Dialog, sofort Hinweis, kein Absturz. Titel „Ohne Foto“, Preis „3“, Ort „Mensa“ → gespeichert, steht oben in der Liste mit Platzhalter-Bild.
10. Berechtigungs-Flags zurückgesetzt, „Foto aufnehmen“ → „While using the app“. Danach war der Emulator nicht mehr erreichbar (`adb: no devices/emulators found`), der Emulator-Prozess war beendet. Die AVD nutzt `hw.camera.back=virtualscene`.
11. Emulator im Hintergrund neu gestartet mit `-camera-back emulated -no-snapshot-load` (AVD-Konfiguration nicht geändert). Auch dieser Start stürzte ab, laut Log „Showing crashdialog to get consent“. Beide Hintergrund-Aufgaben (Emulator-Start und Warten auf den Boot) mit `TaskStop` beendet.
12. **Nicht getestet:** Aufnahme eines Fotos, Vorschau im Formular, Vorschaubild in der Liste, großes Foto im Detail, Drehen während der Kamera-Aufnahme, „Neues Foto“ und „Foto entfernen“.
13. `AI_USAGE.md`: dieses Ergebnis eingetragen.

**Kurzfazit im Antwortfeld der App:** „Schritt 8 ist gebaut: Kamera-Berechtigung, Aufnahme über FileProvider, Hinweis bei Ablehnung und Fotos mit Coil in Formular, Liste und Detail. Der Build läuft, der Ablehnungs-Pfad ist auf dem Emulator getestet. Die eigentliche Aufnahme ist nicht getestet, weil der Emulator beim Öffnen der Kamera abgestürzt ist. Es ist nichts committet.“ Frage: „Könnt ihr die Aufnahme selbst testen und mir das Ergebnis sagen?“

#### Prompt 2

````text
Foto scheint soweit zu funktionieren. Im Emulator kommt bei der Fotoaufnahme ein grünes Beispielbild. Es ist allerdings noch nicht möglich, sich die Bilder im Großformat anzugucken. Das sollte später auf jeden Fall möglich sein. Außerdem kommt man bei der Fotoaufnahme nicht mehr zurück, ohne vorher ein Bild zu machen.
````

#### Ergebnis der KI zu Prompt 2

**Ausgeführte Aktionen:**

1. `git status`, `git log --oneline -2`, Überschriften in `AI_USAGE.md` gelesen, `adb devices` ausgeführt. Ergebnis: Schritt 8 noch nicht committet, Emulator `emulator-5554` läuft wieder.
2. `AI_USAGE.md`: Prompt 2 in Eintrag 10 eingetragen.
3. APK installiert, `dumpsys package` → `CAMERA: granted=true`. Formular → „Foto aufnehmen“ → Kamera-App `com.android.camera2/com.android.camera.CaptureActivity` geöffnet, Screenshot: Testbild (grünes Haus), nur ein Auslöser und ein „…“-Knopf, **kein eigener Abbrechen-Knopf**.
4. `input keyevent KEYCODE_BACK` → oberste Activity wieder `de.dhbw.campusmarket/.MainActivity`. `settings get secure navigation_mode` → `2` (Gesten-Navigation).
5. Kamera erneut geöffnet, Wischgeste vom linken Rand (`input swipe 2 1200 500 1200`) → wieder `MainActivity`, Formular mit Platzhalter, „Foto aufnehmen“. Kein Absturz.
6. Kein Code geändert.
7. `AI_USAGE.md`: dieses Ergebnis eingetragen.

**Kurzfazit im Antwortfeld der App:** „Schön, dass die Aufnahme klappt. Das Zurück aus der Kamera konnte ich nicht als App-Fehler nachstellen: Zurück-Taste und Wischgeste führen beide ins Formular zurück, die Kamera-App des Emulators hat nur keinen eigenen Abbrechen-Knopf. Die Vollbild-Ansicht plane ich als eigenen Schritt ein. Am Code ist nichts geändert, nichts committet.“ Frage: „Soll ich nach eurem Commit mit Schritt 9 (README) weitermachen?“

#### Akzeptiert / Geändert / Abgelehnt

- **Akzeptiert:** Alles
- **Geändert:** Nichts
- **Abgelehnt:** Nichts

#### Geprüft / Getestet

Fotoaufnahme getestet und Edgecases geprüft, keine Fehler

#### Commits

- `08036d4` Produktfoto mit Kamera, Permission und FileProvider

---

### Eintrag 11: Warnungen in AI_USAGE.md nach der Paket-Umbenennung

- **Datum:** 23.11.2026
- **Autor:** Benjamin
- **Tool:** Claude Code (Desktop-App), Claude Opus 5.5

#### Prompt 1

````text
Mir ist aufgefallen, dass durch die Änderung von com.exmaple zu de.dhbw in der AI_USAGE.md sehr viele Warnungen aufgetreten sind. Die sind zwar nicht kritisch, aber trotzdem werden sie bei jedem Commit angezeigt
````

#### Ergebnis der KI zu Prompt 1

**Ausgeführte Aktionen:**

1. `git status`, `git log --oneline -2`, Ende von `AI_USAGE.md` gelesen. Ergebnis: Schritt 8 ist committet (`08036d4`), Arbeitsverzeichnis sauber.
2. Alle relativen Links in `AI_USAGE.md` mit einem Shell-Skript geprüft (existiert das Ziel?). Ergebnis: 29 Links auf 23 verschiedene Dateien unter `app/src/main/java/com/example/…` und `app/src/test/java/com/example/…` zeigen ins Leere; alle anderen Links sind gültig. Der sichtbare Linktext enthält nie `com/example`. Erwähnungen in Backticks (z. B. `com.example.campusmarket`) sind kein Link und erzeugen keine Warnung.
3. `git config core.autocrlf` → `true`, keine `.gitattributes`. `git ls-files --eol` → 55 Dateien haben im Arbeitsverzeichnis LF-Zeilenenden, `AI_USAGE.md` hatte CRLF.
4. `AI_USAGE.md`: Eintrag 11 mit dem Prompt angelegt.
5. Sicherungskopie `AI_USAGE.vorher.md` im Scratchpad der Sitzung angelegt. Dann per `sed -E` nur in Linkzielen `app/src/main/java/com/example/campusmarket/` → `app/src/main/java/de/dhbw/campusmarket/` ersetzt (ebenso für `app/src/test`). Ergebnis: 0 Links mit `com/example`, 0 tote Links. Vergleich von vorher/nachher, bei dem alle Linkziele ausgeblendet sind: kein Unterschied, der sichtbare Text ist also unverändert. `sed` hat dabei die Zeilenenden von `AI_USAGE.md` auf LF umgestellt (wie bei den meisten anderen Dateien).
6. `AI_USAGE.md`: Hinweis zu den Links oben im Dokument ergänzt, dieses Ergebnis eingetragen.

**Kurzfazit im Antwortfeld der App:** „Die Warnungen kamen von 29 toten Links in AI_USAGE.md, die noch auf die alten com/example-Ordner zeigten. Ich habe nur die Linkziele umgestellt, der sichtbare Text ist gleich, und oben steht ein Hinweis dazu. Daneben gibt es noch Git-Hinweise zu Zeilenenden, die ich nicht angefasst habe. Es ist nichts committet.“ Frage: „Soll ich die Zeilenenden mit einer .gitattributes einheitlich machen?“

#### Prompt 2

Antwort auf die Frage der KI im Antwortfeld der App („Soll ich die Zeilenenden mit einer .gitattributes einheitlich machen?“, Buttons „Ja, anlegen“ / „Nein, so lassen“):

````text
Nein, so lassen
````

#### Ergebnis der KI zu Prompt 2

**Ausgeführte Aktionen:** `git status`, `git log --oneline -1` ausgeführt und den Aufbau von Eintrag 11 gelesen. Ergebnis: Nur `AI_USAGE.md` ist geändert, nichts committet. Prompt 2 und dieses Ergebnis in `AI_USAGE.md` eingetragen. Keine `.gitattributes` angelegt, am Code nichts geändert.

**Kurzfazit im Antwortfeld der App:** „Die Zeilenenden bleiben, wie sie sind, es gibt keine .gitattributes. Geändert ist nur AI_USAGE.md (Linkziele und Logbuch), noch nicht committet.“ Frage: „Soll ich nach eurem Commit mit Schritt 9 (README) weitermachen?“

#### Akzeptiert / Geändert / Abgelehnt

- **Akzeptiert:** Alles
- **Geändert:** Nichts
- **Abgelehnt:** Nichts

#### Geprüft / Getestet

-

#### Commits

- `dbaef6f` Tote Links in AI_USAGE.md nach Paket-Umbenennung korrigiert

---

### Eintrag 12: Schritt 9 – README mit Build- und Startschritten

- **Datum:** 23.11.2026
- **Autor:** Benjamin
- **Tool:** Claude Code (Desktop-App), Claude Opus 5.5

#### Prompt 1

````text
Ja, Schritt 9
````

#### Ergebnis der KI zu Prompt 1

**Ausgeführte Aktionen:**

1. `git status`, `git log --oneline -3`, `git remote -v` ausgeführt, Ende von `AI_USAGE.md` sowie `gradle-daemon-jvm.properties`, `gradle-wrapper.properties`, `gradle.properties` und `local.properties` gelesen, Umgebungsvariablen geprüft. Ergebnis: Link-Korrektur committet (`dbaef6f`), Remote `https://github.com/bennipauly/CampusMarket.git`, Daemon-JDK 25 per Foojay, `JAVA_HOME` zeigt auf ein Java-8-JRE, kein `ANDROID_HOME`.
2. `gradlew.bat --version` ausgeführt. Ergebnis: Launcher-JVM 1.8, Daemon-JVM „Compatible with Java 25“. Android Studio bringt Java 25.0.3 (JBR) mit. `product-info.json` von Android Studio: Build `AI-262.9437.185`; im SDK ist `android-37.0` installiert.
3. `AI_USAGE.md`: Eintrag 12 mit dem Prompt angelegt.
4. Neu angelegt: `README.md`.
5. **Sauberer Checkout:** Repo mit `git clone` in einen Ordner `clean` im Scratchpad geklont (Stand `dbaef6f`, ohne `local.properties` und ohne `build`), das neue `README.md` hineinkopiert, nur `ANDROID_HOME` gesetzt, `gradlew.bat assembleDebug testDebugUnitTest` ausgeführt. Ergebnis: **BUILD SUCCESSFUL** in 1 min 44 s, APK `app-debug.apk` (12,7 MB), `AdSearchTest` 10/10, `AdValidationTest` 11/11, `PriceFormatTest` 3/3.
6. Gegenprobe ohne `ANDROID_HOME`: **BUILD FAILED** mit „SDK location not found. Define a valid SDK location with an ANDROID_HOME environment variable or by setting the sdk.dir path in your project's local properties file …“, genau wie im README beschrieben.
7. Im Klon `gradlew.bat installDebug` ausgeführt: „Installed on 1 device“, App gestartet. Die erste Bildschirm-Abfrage schlug fehl („null root node“) und lieferte eine alte Datei; beim zweiten Versuch: Liste mit Suchfeld und Anzeigen, `MainActivity` im Vordergrund.
8. Test-Klon gelöscht.
9. `AI_USAGE.md`: dieses Ergebnis eingetragen.

**Kurzfazit im Antwortfeld der App:** „Das README ist geschrieben, mit Build- und Startschritten und dem Pflichtsatz. Ein frischer Klon baut mit den Schritten aus dem README, alle 24 Tests laufen, und die App startet auf dem Emulator. Damit sind alle fünf Mindestanforderungen erfüllt. Es ist nichts committet.“ Frage: „Soll ich nach eurem Commit mit Schritt 9a (Foto im Vollbild) weitermachen?“

#### Akzeptiert / Geändert / Abgelehnt

- **Akzeptiert:** Alles
- **Geändert:** Nichts
- **Abgelehnt:** Nichts

#### Geprüft / Getestet

Korrekturgelesen 

#### Commits

- `ecc3b9d` README mit Build- und Startschritten

---

### Eintrag 13: Schritt 9a – Foto im Vollbild

- **Datum:** 23.11.2026
- **Autor:** Semih
- **Tool:** Claude Code (Desktop-App), Claude Opus 5.5

#### Prompt 1

````text
Ja, Schritt 9a
````

#### Ergebnis der KI zu Prompt 1

**Ausgeführte Aktionen:**

1. `git status`, `git log --oneline -2` ausgeführt, Ende von `AI_USAGE.md`, `README.md` (Gruppe), `Destinations.kt`, `AppNavHost.kt` und `AdDetailScreen.kt` gelesen. Ergebnis: README committet (`ecc3b9d`), Arbeitsverzeichnis sauber.
2. `AI_USAGE.md`: Eintrag 13 mit dem Prompt angelegt.
3. Neu angelegt: `ui/photoviewer/PhotoViewerUiState.kt`, `ui/photoviewer/PhotoViewerViewModel.kt`, `ui/photoviewer/PhotoViewerScreen.kt` (unter `app/src/main/java/de/dhbw/campusmarket/`).
4. Python-Skript `step9a.py` im Scratchpad der Sitzung geschrieben und mit `python -I` ausgeführt. Geändert: `ui/navigation/Destinations.kt` (neues Ziel `PhotoViewer(adId)`), `ui/navigation/AppNavHost.kt` (`composable<PhotoViewer>`, `onPhotoClick`), `ui/addetail/AdDetailScreen.kt` (Parameter `onPhotoClick` in Route, Screen, Content und beiden Previews; Foto mit `clickable`), `res/values/strings.xml` (4 neue Texte).
5. `gradlew.bat assembleDebug testDebugUnitTest` ausgeführt. Ergebnis: **BUILD SUCCESSFUL**, keine Warnungen.
6. Emulator: App installiert, Anzeige „foto“ (Testanzeige des Teams mit Foto) geöffnet, Foto angetippt → Screen „Foto“, Testbild vollständig sichtbar (Screenshot). Ins Querformat gedreht → Foto bleibt, passt ganz auf den Bildschirm (Screenshot). Zurück ins Hochformat, zweimal Zurück-Pfeil → Detail, dann Liste. `AndroidRuntime:E` leer.
7. `README.md`: Funktion „Foto im Vollbild“ und Ordner `photoviewer/` ergänzt.
8. `AI_USAGE.md`: dieses Ergebnis eingetragen.

**Kurzfazit im Antwortfeld der App:** „Schritt 9a ist umgesetzt: Ein Tipp auf das Foto in der Detailansicht öffnet es im Vollbild, mit eigenem ViewModel und Loading/Error/Ready. Build und Emulator-Test (Antippen, Drehung, Zurück) liefen fehlerfrei, das README ist ergänzt. Es ist nichts committet.“ Frage: „Soll ich nach eurem Commit mit Schritt 10 (Bearbeiten) weitermachen?“

#### Akzeptiert / Geändert / Abgelehnt

- **Akzeptiert:** Alles
- **Geändert:** Nichts
- **Abgelehnt:** Nichts

#### Geprüft / Getestet

Vollbild getestet

#### Commits

- `bb8f9de` Foto der Anzeige im Vollbild ansehen

---

### Eintrag 14: Schritt 10 – Anzeige bearbeiten

- **Datum:** 23.11.2026
- **Autor:** Benjamin
- **Tool:** Claude Code (Desktop-App), Claude Opus 5.5

#### Prompt 1

````text
Ja, Schritt 10
````

#### Ergebnis der KI zu Prompt 1

**Ausgeführte Aktionen:**

1. `git status`, `git log --oneline -3` ausgeführt und das Ende von `AI_USAGE.md` gelesen. Ergebnis: Schritt 9a committet (`bb8f9de`), Arbeitsverzeichnis sauber.
2. `AI_USAGE.md`: Eintrag 14 mit dem Prompt angelegt.
3. Gelesen: `AdFormUiState.kt`, `AdFormViewModel.kt`, `AdFormScreen.kt`, `AdDetailScreen.kt`, `Destinations.kt`, `AppNavHost.kt`, `AdRepository.kt`, `Ad.kt`, `AdValidation.kt`, `AdValidationTest.kt`, `strings.xml`, `ic_delete.xml`.
4. Geändert (mit Python-Skripten im Scratchpad der Sitzung, Zeilenenden je Datei beibehalten): `logic/AdValidation.kt` (`priceToInput`), `AdValidationTest.kt` (2 Tests), `ui/adform/AdFormUiState.kt`, `ui/adform/AdFormViewModel.kt`, `ui/adform/AdFormScreen.kt`, `ui/navigation/Destinations.kt`, `ui/navigation/AppNavHost.kt`, `ui/addetail/AdDetailScreen.kt`, `res/values/strings.xml`. Neu: `res/drawable/ic_edit.xml`.
5. Ein Bash-Befehl blieb hängen (ein `cat` wartete auf Eingabe) und wurde mit TaskStop beendet. Am Projekt hat er nichts geändert.
6. `gradlew.bat assembleDebug testDebugUnitTest` ausgeführt. Ergebnis: **BUILD SUCCESSFUL**, keine Warnungen, 26/26 Tests grün.
7. Emulator: App installiert. „Mathe Formelsammlung“ bearbeitet (Formular ausgefüllt, Preis 7,50 → 8,00, Drehung, Speichern → Detail zeigt 8,00 €, Platz in der Liste gleich). „Analysis 1 – Lehrbuch“: Beschreibung geladen, leerer Titel → Fehlertext, Zurück → unverändert. App neu gestartet → 8,00 € bleibt. „foto“ bearbeitet und gespeichert → Foto bleibt. Absturz-Log leer.
8. `README.md`: Funktion „Anzeige bearbeiten“, Testtabelle und Ordner `adform/` ergänzt.
9. `AI_USAGE.md`: dieses Ergebnis eingetragen.

**Kurzfazit im Antwortfeld der App:** „Schritt 10 ist umgesetzt: Über das Stift-Symbol in der Detailansicht öffnet sich das Formular mit den gespeicherten Werten, Speichern überschreibt die Anzeige. Build, 26 Tests und der Emulator-Test liefen fehlerfrei. Es ist nichts committet.“ Frage: „Soll ich nach eurem Commit mit Schritt 11 (Favoriten) weitermachen?“

#### Prompt 2

````text
Die Rotation funktioniert nicht mehr. Die Anzeige bleibt immer im Hochformat. Den Commit von vorhin habe ich noch nicht gemacht
````

#### Ergebnis der KI zu Prompt 2

**Ausgeführte Aktionen:**

1. `AI_USAGE.md`: Prompt 2 in Eintrag 14 eingetragen.
2. Im Projekt nach `screenOrientation`, `requestedOrientation` und `configChanges` gesucht. Ergebnis: nichts gefunden, das Manifest ist unverändert.
3. Emulator-Einstellungen gelesen. Ergebnis: `accelerometer_rotation = 0` (Automatisch drehen aus, von der KI beim Drehtest in Prompt 1 gesetzt), `user_rotation = 0`.
4. `adb shell settings put system accelerometer_rotation 1` ausgeführt, danach `adb emu rotate` (viermal). Ergebnis: Anzeige wechselt auf `ROTATION_270` (Querformat) und zurück auf `ROTATION_0`.
5. `AI_USAGE.md`: dieses Ergebnis eingetragen. Am Code nichts geändert.

**Kurzfazit im Antwortfeld der App:** „Die Rotation war im Emulator gesperrt, weil ich für den Drehtest „Automatisch drehen“ ausgeschaltet und danach nicht wieder eingeschaltet hatte. Die Einstellung ist zurückgesetzt, und der Emulator dreht wieder. Am Code ist nichts geändert, Schritt 10 ist weiterhin nicht committet.“ Frage: „Soll ich nach eurem Commit mit Schritt 11 (Favoriten) weitermachen?“

#### Akzeptiert / Geändert / Abgelehnt

- **Akzeptiert:** Alles
- **Geändert:** Nichts
- **Abgelehnt:** Nichts

#### Geprüft / Getestet

Bearbeiten getestet, Rotation geht nicht mehr
-> Claude hat die automatische Rotation im Emulator ausgeschaltet

#### Commits

- `55650cf` Anzeige bearbeiten

---

### Eintrag 15: Schritt 11 – Favoriten

- **Datum:** 23.11.2026
- **Autor:** Semih
- **Tool:** Claude Code (Desktop-App), Claude Opus 5.5

#### Prompt 1

````text
Ja, Schritt 11
````

#### Ergebnis der KI zu Prompt 1

**Ausgeführte Aktionen:**

1. `git status`, `git log --oneline -2` ausgeführt. Ergebnis: Schritt 10 committet (`55650cf`), danach euer Commit `1d1fa34` (Tabellen in AI_USAGE.md), Arbeitsverzeichnis sauber.
2. `AI_USAGE.md`: Eintrag 15 mit dem Prompt angelegt.
3. Gelesen: `AdDao.kt`, `AdDetailUiState.kt`, `AdDetailViewModel.kt`, `AdListViewModel.kt`, `AdListScreen.kt`.
4. Ein erster Bash-Befehl mit eingebettetem Python-Skript brach mit „unexpected EOF“ ab, bevor etwas geändert wurde. Das Skript `step11.py` wurde dann mit dem Write-Werkzeug im Scratchpad der Sitzung angelegt und mit `python -I` ausgeführt. Geändert: `data/AdDao.kt`, `data/AdRepository.kt`, `ui/adlist/AdListViewModel.kt`, `ui/adlist/AdListScreen.kt`, `ui/addetail/AdDetailViewModel.kt`, `ui/addetail/AdDetailScreen.kt`, `res/values/strings.xml`. Neu: `ui/FavoriteButton.kt`, `res/drawable/ic_favorite.xml`, `res/drawable/ic_favorite_border.xml`.
5. `gradlew.bat assembleDebug testDebugUnitTest` ausgeführt. Ergebnis: **BUILD SUCCESSFUL**, keine Warnungen, 26/26 Tests grün.
6. Emulator: App installiert. Herz bei „Analysis 1 – Lehrbuch“ in der Liste angetippt → gefüllt, Liste bleibt offen (Screenshot). Detail: Herz gefüllt, zweimal umgeschaltet. Mit `adb emu rotate` ins Querformat und zurück → Herz bleibt. Bearbeiten und Speichern → bleibt Favorit. App neu gestartet → Herz in der Liste gefüllt. Absturz-Log leer.
7. `README.md`: Funktion „Favoriten“ ergänzt.
8. `AI_USAGE.md`: dieses Ergebnis eingetragen.

**Kurzfazit im Antwortfeld der App:** „Schritt 11 ist umgesetzt: Ein Herz in der Liste und in der Detailansicht merkt eine Anzeige als Favorit, gespeichert in Room. Build, 26 Tests und der Emulator-Test (Umschalten, Drehung, Bearbeiten, Neustart) liefen fehlerfrei. Es ist nichts committet.“ Frage: „Soll ich nach eurem Commit mit Schritt 12 (Status verfügbar/verkauft) weitermachen?“

#### Akzeptiert / Geändert / Abgelehnt

- **Akzeptiert:** Alles
- **Geändert:** Nichts
- **Abgelehnt:** Nichts

#### Geprüft / Getestet

Favoriten-Markierung getestet

#### Commits

- `313bdcf` Anzeigen als Favorit merken

---

### Eintrag 16: Schritt 12 – Status verfügbar/verkauft

- **Datum:** 23.11.2026
- **Autor:** Semih
- **Tool:** Claude Code (Desktop-App), Claude Opus 5.5

#### Prompt 1

````text
Ja, Schritt 12
````

#### Ergebnis der KI zu Prompt 1

**Ausgeführte Aktionen:**

1. `git status`, `git log --oneline -2` ausgeführt. Ergebnis: Schritt 11 committet (`313bdcf`), aber `AI_USAGE.md` war bereits ohne Commit geändert.
2. `AI_USAGE.md`: Eintrag 16 mit dem Prompt angelegt (Skript `log16.py` im Scratchpad, mit `sed` aus `log15.py` erzeugt).
3. Dabei aufgefallen: Eintrag 15 stand zweimal in der Datei. `git diff` und ein Vergleich mit dem Skript `dup15.py` zeigten: Die zweite Kopie ist der alte, ungeänderte Text der KI (alte Tabellenformatierung, Team-Felder „eintragen“), die erste ist der committete Stand des Teams. Die zweite Kopie wurde mit `dup15.py` entfernt.
4. Gelesen: `strings.xml` (Status-Texte) und `AdDetailScreen.kt` (Detail-Inhalt).
5. Skript `step12.py` im Scratchpad angelegt und mit `python -I` ausgeführt. Geändert: `data/AdDao.kt`, `data/AdRepository.kt`, `ui/addetail/AdDetailViewModel.kt`, `ui/addetail/AdDetailScreen.kt`, `ui/adlist/AdListScreen.kt`, `res/values/strings.xml`.
6. `gradlew.bat assembleDebug testDebugUnitTest` ausgeführt. Ergebnis: **BUILD SUCCESSFUL**, keine Warnungen, 26/26 Tests grün.
7. Emulator: App installiert. „Taschenrechner TI-30X“ als verkauft markiert → Status „Verkauft“, Knopftext gewechselt. Mit `adb emu rotate` ins Querformat und zurück → bleibt. Liste zeigt rot „Verkauft“ (Screenshot). Bearbeiten und Speichern → bleibt verkauft. App neu gestartet → bleibt verkauft. „Wieder als verfügbar markieren“ → „Verfügbar“. Absturz-Log leer.
8. `README.md`: Funktion „Status“ ergänzt.
9. `AI_USAGE.md`: dieses Ergebnis eingetragen.

**Kurzfazit im Antwortfeld der App:** „Schritt 12 ist umgesetzt: In der Detailansicht lässt sich eine Anzeige als verkauft markieren und wieder freigeben, die Liste zeigt dann „Verkauft“. Build, 26 Tests und der Emulator-Test liefen fehlerfrei. Außerdem habe ich eine doppelte, veraltete Kopie von Eintrag 15 aus AI_USAGE.md entfernt. Es ist nichts committet.“ Frage: „Soll ich nach eurem Commit mit Schritt 13 (Filter) weitermachen?“

#### Akzeptiert / Geändert / Abgelehnt

- **Akzeptiert:** Alles
- **Geändert:** Nichts
- **Abgelehnt:** Nichts

#### Geprüft / Getestet

Neuer Button (Verkauft) getestet

#### Commits

- `937e832` Anzeige als verkauft markieren

---

### Eintrag 17: Schritt 13 – Filter

- **Datum:** 24.11.2026
- **Autor:** Benjamin
- **Tool:** Claude Code (Desktop-App), Claude Opus 5.5

#### Prompt 1

````text
Ja, Schritt 13
````

#### Ergebnis der KI zu Prompt 1

**Ausgeführte Aktionen:**

1. `git status`, `git log --oneline -2` ausgeführt. Ergebnis: Schritt 12 committet (`937e832`), Arbeitsverzeichnis sauber, Eintrag 15 nur noch einmal vorhanden.
2. `AI_USAGE.md`: Eintrag 17 mit dem Prompt angelegt (Skript `log17.py` im Scratchpad).
3. Gelesen: `AdListUiState.kt`, `logic/AdSearch.kt`, `AdSearchTest.kt` (Anfang), `strings.xml` (Listen- und Kategorie-Texte), `ui/Labels.kt`.
4. Neu angelegt: `app/src/main/java/de/dhbw/campusmarket/logic/AdFilter.kt`, `app/src/test/java/de/dhbw/campusmarket/logic/AdFilterTest.kt`.
5. Skript `step13.py` im Scratchpad angelegt und mit `python -I` ausgeführt. Geändert: `ui/adlist/AdListUiState.kt`, `ui/adlist/AdListViewModel.kt`, `ui/adlist/AdListScreen.kt`, `res/values/strings.xml`.
6. `gradlew.bat assembleDebug testDebugUnitTest` ausgeführt. Ergebnis: **BUILD SUCCESSFUL**, keine Warnungen, 33/33 Tests grün (AdFilterTest 7, AdSearchTest 10, AdValidationTest 13, PriceFormatTest 3).
7. Emulator: App installiert. Der erste Tipp auf „Favoriten“ kam vor dem Laden der App an und wurde nicht ausgeführt. Danach getestet: „Favoriten“ + „Bücher“ → nur „Analysis 1 – Lehrbuch“ (Screenshot). Nur „Bücher“, nur „Nur verfügbare“ (verkaufte fehlen), Drehung mit `adb emu rotate` (Filter bleiben), Suche „Stuhl“ mit Filtern („Keine Treffer für „Stuhl“.“, nach Abwählen von „Favoriten“ erscheint „Schreibtischstuhl“), „Favoriten“ + „Skripte“ ohne Suchtext („Keine Anzeigen passen zu den gewählten Filtern.“). Absturz-Log leer.
8. `README.md`: Funktion „Filter“, Testklasse `AdFilterTest` und Ordnerbeschreibung `logic/` ergänzt.
9. `AI_USAGE.md`: dieses Ergebnis eingetragen.

**Kurzfazit im Antwortfeld der App:** „Schritt 13 ist umgesetzt: Filter-Chips unter dem Suchfeld (Favoriten, nur verfügbare, Kategorie), kombinierbar mit der Suche. Die Filterlogik ist eine reine Funktion mit 7 neuen JUnit-Tests. Build, 33 Tests und der Emulator-Test liefen fehlerfrei. Es ist nichts committet.“ Frage: „Soll ich nach eurem Commit mit Schritt 14 (Sortierung) weitermachen?“

#### Akzeptiert / Geändert / Abgelehnt

- **Akzeptiert:** Alles
- **Geändert:** Nichts
- **Abgelehnt:** Nichts

#### Geprüft / Getestet

Filter getestet, funktioniert

#### Commits

- `16b2cd8` Anzeigen nach Kategorie, Favoriten und Status filtern

---

### Eintrag 18: Schritt 14 – Sortierung und neue Bewertungsregeln

- **Datum:** 24.11.2026
- **Autor:** Semih
- **Tool:** Claude Code (Desktop-App), Claude Opus 5.5

#### Prompt 1

````text
Jetzt Schritt 14, es gibt aber noch ein paar Regeländerungen für die Bewertung:

In welcher spräche schrieben wir quellcode -> Englische Klassennamen, Kommentare auch auf Englisch, variable auf englisch
Wie sind die Klassen. Nicht zu viel Inhalt. Variablen in eine bestimmten Muster deklarieren und dieses Muster beibehalten. Klassenlänge nicht länger als 500 Zeilen… (+ Ausnahmen) -> clean code anhalten
Nicht hardcoded Labels
READ.ME wenn man paar Sachen voraussetzt.
Logs hinzugefügt?
In AI_USAGE Antwort der KI rausnehmen nur in welchem Commit das eingeflossen ist hinschreiben
````

#### Ergebnis der KI zu Prompt 1

**Ausgeführte Aktionen:**

1. `git status`, `git log --oneline -3` ausgeführt. Ergebnis: Schritt 13 committet (`16b2cd8`), Arbeitsverzeichnis sauber.
2. `AI_USAGE.md`: Eintrag 18 mit dem Prompt angelegt, schon im neuen Format (ohne Antworttext der KI).
3. `CLAUDE.md` gelesen und angepasst: Regel 2 und 3 (Logbuch ohne Antworttext, stattdessen Commit), neue Code-Stil-Regeln (höchstens 500 Zeilen pro Datei, Quellcode komplett Englisch, keine hartkodierten Labels, Benennung, feste Reihenfolge in ViewModel/UiState/Screen-Datei, Logging mit `android.util.Log`), README-Regel zu Voraussetzungen. Dateilängen geprüft (längste Datei `AdFormScreen.kt` mit 406 Zeilen) und nach `android.util.Log` gesucht (noch nirgends benutzt).
4. Neu angelegt: `logic/AdSort.kt` (`SortOrder`, `sortAds`), `test/…/logic/AdSortTest.kt` (7 Tests).
5. Gelesen und auf Englisch neu geschrieben: `ui/adlist/AdListScreen.kt` (Sortiermenü `SortMenu`, gemeinsame Preview-Hilfsfunktion, neue Preview „Sorted“), `ui/adlist/AdListViewModel.kt` (`onSortOrderChange`, `TAG` und Logs), `ui/adlist/AdListUiState.kt` (`sortOrder`), `ui/Labels.kt` (`SortOrder.labelRes()`).
6. Skript `step14.py` im Scratchpad: 6 Texte in `strings.xml`, neue Icons `ic_sort.xml` und `ic_check.xml`.
7. `gradlew.bat assembleDebug testDebugUnitTest` ausgeführt. Ergebnis: **BUILD SUCCESSFUL**, keine Warnungen, 40/40 Tests grün.
8. Emulator, erster Durchlauf: Bei „Titel (A–Z)“ blieb „foto test“ oben stehen, weil die Liste die Scroll-Position am ersten sichtbaren Eintrag festhielt. Behoben mit `key(uiState.sortOrder)` um die Liste in `AdListScreen.kt`, neu gebaut.
9. Emulator, zweiter Durchlauf: Menü mit Häkchen (Screenshot). „Preis aufsteigend“ → 3,00 … 25,00 €, Querformat und zurück → bleibt. „Preis absteigend“ → 100,00 … 5,00 €. „Titel (A–Z)“ → „Analysis 1 – Lehrbuch“, „dsdsds“, „foto test“ … von oben. Logcat zeigt `D AdListViewModel: Sort order changed to …`. Absturz-Log leer.
10. `README.md`: Funktion „Sortierung“, Testklasse `AdSortTest`, Ordnerbeschreibung `logic/` und Hinweis „Logs ansehen“ ergänzt.
11. `AI_USAGE.md`: dieses Ergebnis eingetragen.

**Kurzfazit im Antwortfeld der App:** „Schritt 14 ist umgesetzt: Sortiermenü oben rechts (neueste, Preis auf-/absteigend, Titel A–Z), die Logik ist eine reine Funktion mit 7 neuen Tests. Die neuen Regeln stehen in CLAUDE.md, das Listen-Paket ist schon auf Englisch und loggt. Build, 40 Tests und Emulator-Test liefen fehlerfrei. Der übrige Code und die alten Logbuch-Einträge sind noch nicht umgestellt. Es ist nichts committet.“ Frage: „Soll ich nach eurem Commit den restlichen Code und AI_USAGE.md auf die neuen Regeln umstellen?“

#### Akzeptiert / Geändert / Abgelehnt

- **Akzeptiert:** Alles
- **Geändert:** Nichts
- **Abgelehnt:** Nichts

#### Geprüft / Getestet

Alle Filter überprüft

#### Commits

- `ef13184` Anzeigen sortieren und neue Code-Regeln in CLAUDE.md

---

### Eintrag 19: Schritt 14a – Code und Logbuch auf die neuen Regeln umstellen

- **Datum:** 24.11.2026
- **Autor:** Benjamin
- **Tool:** Claude Code (Desktop-App), Claude Opus 5.5

#### Prompt 1

````text
Ja, umstellen
````

#### Ergebnis der KI zu Prompt 1

**Ausgeführte Aktionen:**

1. `git status`, `git log --oneline -2` ausgeführt. Ergebnis: Schritt 14 committet (`ef13184`), Arbeitsverzeichnis sauber.
2. `AI_USAGE.md`: Eintrag 19 mit dem Prompt angelegt (Skript `log19.py` im Scratchpad).
3. Mit `grep` alle Dateien mit deutschen Kommentaren gesucht (Kotlin, Manifest, Drawables, `file_paths.xml`, Tests) und alle Kommentarzeilen ausgegeben.
4. Skript `step14a_comments.py` im Scratchpad: 163 Kommentarzeilen in 41 Dateien ins Englische übersetzt, dazu die deutsche Meldung in `AdValidationTest.kt` („should be invalid“). Gegenprobe mit `grep` nach deutschen Wörtern: keine deutschen Kommentare mehr.
5. Skript `step14a_logs.py` im Scratchpad: `TAG` und `android.util.Log` in `CampusMarketApplication.kt` (neues try/catch um die Beispieldaten), `AdRepository.kt` (Beispieldaten eingefügt), `AdDetailViewModel.kt` (Laden, Favorit, Status, Löschen), `AdFormViewModel.kt` (Laden, nicht gefunden, angelegt, geändert, Fehler), `AdFormScreen.kt` (keine Kamera-App), `PhotoViewerViewModel.kt` (Laden). In `AdFormUiState.kt` `isEditing` hinter die Lade-Felder verschoben (Reihenfolge-Regel). Geprüft: Jede Datei mit `catch` loggt. Hartkodierte Texte nur in `SampleData.kt` und einer Preview (erlaubte Ausnahmen). Längste Datei: `AdFormScreen.kt` mit 410 Zeilen.
6. Sicherung `AI_USAGE.vor14a.md` im Scratchpad angelegt. Skript `step14a_usage.py`: 19 Antwortblöcke und 1 einzeiliger Antwortverweis entfernt, „(vollständig)“ aus 21 Überschriften entfernt, in 18 Einträgen Hash und Nachricht aus `git log` unter „Commits“ eingetragen (Eintrag 8 bekam `db9f6a0`), Hinweis zum neuen Format oben ergänzt, einen Verweis in Eintrag 1 angepasst. Geprüft: 23 öffnende und 23 schließende Codeblöcke.
7. `README.md`: Satz zur KI-Nutzung an das neue Logbuch-Format angepasst.
8. `gradlew.bat assembleDebug testDebugUnitTest` ausgeführt. Ergebnis: **BUILD SUCCESSFUL**, keine Warnungen, 40/40 Tests grün.
9. Emulator: Favorit, Status, Bearbeiten und leeres Formular durchgespielt, gedreht. Logcat zeigt `AdDetailViewModel: Ad 3: favorite set to true/false`, `sold set to true/false`, `AdFormViewModel: Ad 3 updated`. Absturz-Log leer. Nicht getestet: der Log beim Einfügen der Beispieldaten (dafür müsste die Datenbank geleert werden).
10. `AI_USAGE.md`: dieses Ergebnis eingetragen.

**Kurzfazit im Antwortfeld der App:** „Der gesamte Code ist jetzt auf Englisch kommentiert und loggt Fehler und wichtige Aktionen. In AI_USAGE.md sind die Antworttexte entfernt, unter „Commits“ stehen jetzt Hash und Nachricht. Build, 40 Tests und Emulator-Test liefen fehlerfrei. Es ist nichts committet.“ Frage: „Soll ich nach eurem Commit mit Schritt 15 (Galerie-Fallback) weitermachen?“

#### Akzeptiert / Geändert / Abgelehnt

- **Akzeptiert:** Alles
- **Geändert:** Nichts
- **Abgelehnt:** Nichts

#### Geprüft / Getestet

Alle Änderungen überprüft

#### Commits

- `3375c59` Kommentare auf Englisch, Logging und Logbuch ohne Antworttexte

---

### Eintrag 20: Schritt 15 – Foto aus der Galerie

- **Datum:** 24.11.2026
- **Autor:** Semih
- **Tool:** Claude Code (Desktop-App), Claude Opus 5.5

#### Prompt 1

````text
Ja, Schritt 15
````

#### Ergebnis der KI zu Prompt 1

**Ausgeführte Aktionen:**

1. `git status`, `git log --oneline -2` ausgeführt. Ergebnis: Schritt 14a committet (`3375c59`), Arbeitsverzeichnis sauber.
2. `AI_USAGE.md`: Eintrag 20 mit dem Prompt angelegt (Skript `log20.py`). In Eintrag 19 unter „Commits“ den Hash `3375c59` nachgetragen. Der erste Versuch dafür traf wegen eines zu weit greifenden Suchmusters den Commit-Abschnitt von Eintrag 20; das wurde sofort korrigiert und geprüft (Eintrag 19: Hash und Nachricht, Eintrag 20: „eintragen“).
3. Gelesen: `PhotoFile.kt`, `AdFormScreen.kt` (komplett), Foto-Funktionen in `AdFormViewModel.kt`, `AdFormUiState.kt`, Foto-Texte in `strings.xml`.
4. Geändert: `PhotoFile.kt` (neu `copyPhotoToAppStorage` auf `Dispatchers.IO`, gemeinsame private Hilfsfunktionen), `AdFormScreen.kt` (Launcher `PickVisualMedia`, Kopieren mit `rememberCoroutineScope`, `catch` für `IOException` und `SecurityException` mit Log, Knopf „Aus Galerie“, Knopfzeile als `FlowRow`, Fehlerhinweis, gemeinsame Preview-Hilfsfunktion), `AdFormViewModel.kt` (`onGalleryPhotoCopied` mit Log, `onGalleryPhotoFailed`), `AdFormUiState.kt` (`showGalleryError`), `strings.xml` (2 neue Texte, 2 Hinweise um die Galerie ergänzt, Skript `step15.py`).
5. `gradlew.bat assembleDebug testDebugUnitTest` ausgeführt. Ergebnis: **BUILD SUCCESSFUL**, keine Warnungen, 40/40 Tests grün.
6. Emulator: Testbild nach `/sdcard/Pictures/galerie_test.png` kopiert und per Media-Scanner in die Galerie aufgenommen. „Aus Galerie“ → Fotoauswahl, Bild gewählt, mit „Done“ bestätigt → Vorschau zeigt das Foto, Kopie `files/photos/photo_….jpg` (212263 Bytes) angelegt, Log `Gallery photo copied into the app storage`. Querformat und zurück → Foto bleibt. Anzeige „Galerietest“ gespeichert → Detail zeigt das Foto. Original in der Galerie gelöscht, App neu gestartet → Foto weiterhin sichtbar (Screenshot). Kamera-Berechtigung mit `pm revoke` entzogen, „Foto aufnehmen“ → „Don’t allow“ → neuer Hinweistext mit Galerie erscheint. Absturz-Log leer.
7. `README.md`: Funktion „Foto aus der Galerie“ und Emulator-Hinweis „Galerie“ ergänzt.
8. `AI_USAGE.md`: dieses Ergebnis eingetragen.

**Kurzfazit im Antwortfeld der App:** „Schritt 15 ist umgesetzt: Neben „Foto aufnehmen“ gibt es „Aus Galerie“. Das gewählte Foto wird in den App-Speicher kopiert und bleibt auch nach dem Löschen in der Galerie erhalten. Build, 40 Tests und Emulator-Test liefen fehlerfrei. Es ist nichts committet.“ Frage: „Soll ich nach eurem Commit mit dem Feinschliff weitermachen?“

#### Akzeptiert / Geändert / Abgelehnt

- **Akzeptiert:** Alles
- **Geändert:** Nichts
- **Abgelehnt:** Nichts

#### Geprüft / Getestet

Bildupload aus Galerie getestet

#### Commits

- `50aba60` Foto aus der Galerie wählen

---

### Eintrag 21: Feinschliff und README auf Englisch

- **Datum:** 25.11.2026
- **Autor:** Benjamin
- **Tool:** Claude Code (Desktop-App), Claude Opus 5.5

#### Prompt 1

````text
Ja Feinschliff, uns wurde aber noch gesagt, dass die Readme auf englisch sein soll
````

#### Ergebnis der KI zu Prompt 1

**Ausgeführte Aktionen:**

1. `git status`, `git log --oneline -3` ausgeführt. Ergebnis: Schritt 15 war noch nicht committet. Mit einer Rückfrage geklärt; das Team hat danach `50aba60` („Foto aus der Galerie wählen“) committet.
2. `AI_USAGE.md`: Eintrag 21 mit dem Prompt angelegt (Skript `log21.py`).
3. Danach fehlten in der Arbeitskopie von `AI_USAGE.md` Eintrag 20 und 21 sowie der Hash in Eintrag 19 (vermutlich hat ein Editor einen alten Stand gespeichert). Mit `git diff` geprüft: Die Arbeitskopie enthielt nichts, was nicht auch im Commit `50aba60` steht. Alter Stand als `AI_USAGE.stale.md` im Scratchpad gesichert, Datei mit `git checkout HEAD -- AI_USAGE.md` wiederhergestellt, Eintrag 21 neu angelegt und in Eintrag 20 den Hash `50aba60` eingetragen.
4. `gradlew.bat lintDebug` ausgeführt. Ergebnis: 0 Fehler, 19 Warnungen (7 ungenutzte Farben, überflüssiger Ordner `mipmap-anydpi-v26`, doppeltes `android:label` im Manifest, ein Plural-Hinweis, 9 Hinweise auf neuere Versionen).
5. Gelesen: `colors.xml`, `Type.kt`, `Theme.kt`, Manifest, Abhängigkeiten in `app/build.gradle.kts`.
6. Skript `polish.py` im Scratchpad: auskommentierte Vorlagen-Blöcke aus `Type.kt` und `Theme.kt` entfernt (Regel „kein toter Code“) und deren Kommentare angepasst, `android:label` und `android:theme` der Activity entfernt (gleich wie bei der Application), `colors.xml` gelöscht (alle 7 Farben ungenutzt), `mipmap-anydpi-v26` in `mipmap-anydpi` umbenannt.
7. `lifecycle-runtime-ktx` aus `app/build.gradle.kts` und `gradle/libs.versions.toml` entfernt (nirgends benutzt), Versionsschlüssel `lifecycleRuntimeKtx` in `lifecycle` umbenannt.
8. `gradlew.bat assembleDebug testDebugUnitTest lintDebug` ausgeführt. Ergebnis: **BUILD SUCCESSFUL**, 40/40 Tests grün, Lint 0 Fehler, 9 Warnungen (8 Versionshinweise, 1 Plural-Hinweis, beide bewusst nicht geändert).
9. `README.md` komplett auf Englisch neu geschrieben, mit neuem Abschnitt „Known limitations“ und dem Befehl `lintDebug`. Der Pflichtsatz steht wortwörtlich auf Deutsch, mit englischer Übersetzung in Klammern.
10. `CLAUDE.md`: Regel „README ist auf Englisch“ im Abschnitt „Abgabe“ ergänzt.
11. Emulator: App installiert und gestartet, Liste erscheint, Formular „Anzeige bearbeiten“ geöffnet, gedreht, kein Absturz. Mit `aapt2 dump badging` geprüft: App-Icon `ic_launcher` und Name „CampusMarket“ werden gefunden.
12. `AI_USAGE.md`: dieses Ergebnis eingetragen.

**Kurzfazit im Antwortfeld der App:** „Der Feinschliff ist fertig: Lint-Funde behoben (ungenutzte Farben, auskommentierte Vorlagen-Blöcke, überflüssiger Ordner, doppelte Manifest-Angaben, ungenutzte Bibliothek), das README ist komplett auf Englisch. Build, 40 Tests, Lint und Emulator-Test liefen fehlerfrei. Es ist nichts committet.“ Frage: „Soll ich nach eurem Commit einen Entwurf für die Reflexion schreiben?“

#### Akzeptiert / Geändert / Abgelehnt

- **Akzeptiert:** Alles
- **Geändert:** Nichts
- **Abgelehnt:** Nichts

#### Geprüft / Getestet

Geschaut, was entfernt wurde und warum

#### Commits

- `1ed2063` Lint-Funde behoben und README auf Englisch

---

### Eintrag 22: Schritt 16 – Testdaten über das Netzwerk laden

- **Datum:** 25.11.2026
- **Autor:** Benjamin
- **Tool:** Claude Code (Desktop-App), Claude Opus 5.5

#### Prompt 1

````text
Neue Änderung: Die Anzeigen sollen nicht mehr hart codiert sein, sondern als Testdaten über das Netzwerk geladen werden.

Entscheidungen:
1. Datenquelle: `testdata/anzeigen.json` plus Bilder in `testdata/images/` in einem öffentlichen GitHub-Repo, geladen über raw.githubusercontent.com.
   Base-URL: https://raw.githubusercontent.com/bennipauly/CampusMarket/main/
   Jede Anzeige hat eine `bildUrl`, die auf ihr Bild im Repo zeigt.
2. Fehler getrennt abfangen: `IOException` (kein Internet/Flugmodus → "Keine Internetverbindung."), `HttpException` (Serverfehler mit Code), `SerializationException` (JSON passt nicht). Kein pauschales `catch (e: Exception)`.
3. UI: Bei Laden ein Ladekreis. Bei Fehler die Meldung mit einem Button "Erneut versuchen", aber KEINE Anzeigen. Bei Erfolg die Liste. Dazu ein Refresh-Button in der TopAppBar.
4. `INTERNET`-Permission im Manifest.

Vorgehen:
- Bestehendes Model und Sample-Daten anpassen bzw. ersetzen statt doppelt anlegen. Sag mir, was du änderst.
- Dependencies über den Version Catalog eintragen, mit aktuellen stabilen Versionen.
- Erstelle `testdata/anzeigen.json` mit 5–6 Beispielanzeigen passend zu unserem Model und sag mir, welche Bilddateien wir in `testdata/images/` hochladen müssen.
- In kleinen Schritten mit Commit-Vorschlägen arbeiten. Am Ende prüfen, ob das Projekt baut.
- Erklär kurz pro Datei, was sie macht, für die Code-Verteidigung.
- Dokumentier das Ganze wie gewohnt in `AI_USAGE.md`.
````

#### Ergebnis der KI zu Prompt 1

**Ausgeführte Aktionen:**

1. `git status`, `git log --oneline -3` ausgeführt: Feinschliff committet (`1ed2063`), in Eintrag 21 nachgetragen. `git ls-remote --symref origin HEAD` ausgeführt. Ergebnis: Der Branch auf GitHub heißt **`master`**, nicht `main`. Die im Prompt genannte Base-URL `…/main/` hätte immer 404 geliefert, deshalb benutzt der Code `https://raw.githubusercontent.com/bennipauly/CampusMarket/master/`.
2. `AI_USAGE.md`: Eintrag 22 mit dem Prompt angelegt (Skript `log22.py`).
3. Neueste Versionen auf Maven Central abgefragt (`maven-metadata.xml`) und in den POM-Dateien geprüft, welche Kotlin-Version sie brauchen. Ergebnis: Retrofit 3.0.0 und converter-kotlinx-serialization 3.0.0 passen. kotlinx-serialization-json 1.12/1.13 brauchen Kotlin 2.4 und 1.10/1.11 Kotlin 2.3, also wird **1.9.0** verwendet. `coil-network-okhttp` bleibt wie Coil auf **3.3.0**. OkHttp 4.12.0 kommt über Retrofit.
4. **16a:** `gradle/libs.versions.toml` (Versionen `retrofit`, `kotlinxSerializationJson`, vier Bibliotheken), `app/build.gradle.kts` (vier `implementation`), `AndroidManifest.xml` (`INTERNET`-Permission), `CLAUDE.md` (Netzwerk-Regel statt „rein lokal“, Versionsgrenzen, Testdaten statt Beispieldaten, Fehlerarten, Flugmodus, Ausnahme für das Retrofit-Interface). Build und 40 Tests grün. `gradle dependencies` geprüft: kotlin-stdlib bleibt 2.2.10, serialization-core 1.9.0.
5. Rückfrage zum Commit von 16a: Das Team entschied, alles am Ende zu committen. Die Teilschritte wurden deshalb so geschnitten, dass keine Datei in zwei Commits vorkommt.
6. **16b:** Neu `testdata/anzeigen.json` (6 Anzeigen, Inhalte der bisherigen Beispieldaten, deutsche JSON-Schlüssel, `bildUrl` auf `testdata/images/…`), `data/network/AdDto.kt`, `data/network/AdApi.kt` (Retrofit-Interface, `adJson`, `createAdApi()`), `test/…/data/network/AdDtoTest.kt` (7 Tests, liest die echte JSON-Datei). Build grün, 47 Tests grün.
7. **16c:** `data/Ad.kt` (`remoteId` mit Unique-Index), `data/AppDatabase.kt` (Version 2, `MIGRATION_1_2`), `data/AdDao.kt` (`insertOrIgnore`, `count`/`insertAll` entfernt), neu `data/network/AdMapping.kt` (`toAds`) mit `AdMappingTest.kt` (5 Tests), `data/AdRepository.kt` (`refreshAdsFromNetwork`, `insertSampleDataIfEmpty` entfernt), `CampusMarketApplication.kt` (Migration, `createAdApi()`, kein Einfügen von Beispieldaten mehr), `data/SampleData.kt` gelöscht und als `ui/PreviewData.kt` (`previewAds()`, nur für Previews) neu angelegt, `AdDetailScreen.kt` und `AdListScreen.kt` (Previews), `AdFormViewModel.kt` (`remoteId` bleibt beim Bearbeiten erhalten), neu `ui/adlist/LoadError.kt`, `AdListUiState.kt` (`loadError` statt `hasError`), `AdListViewModel.kt` (`refreshAds` mit getrennten `catch` für `IOException`, `HttpException`, `SerializationException`, `SQLiteException`), `AdListScreen.kt` (Refresh-Button, Fehlertext je Fehlerart, Previews „NoInternet“ und „ServerError“), `strings.xml` (4 Texte), neu `ic_refresh.xml`.
8. `gradlew.bat assembleDebug testDebugUnitTest lintDebug` ausgeführt. Ergebnis: **BUILD SUCCESSFUL**, 52/52 Tests grün, Lint 0 Fehler (11 Warnungen: nur Versionshinweise und der bekannte Plural-Hinweis).
9. Emulator: `curl` auf die Raw-URL liefert 404, weil `testdata/` noch nicht gepusht ist. App über die alte Version installiert: Die Migration 1→2 lief ohne Absturz, und die Liste zeigt „Serverfehler (Code 404). Bitte später erneut versuchen.“ mit „Erneut versuchen“ und ohne Anzeigen (Log `HttpException: HTTP 404`). Flugmodus an, „Erneut versuchen“ → „Keine Internetverbindung.“. Gedreht → Meldung bleibt, kein neuer Ladeversuch. Refresh-Button → erneut „Keine Internetverbindung.“. Flugmodus wieder aus. Absturz-Log leer. **Nicht getestet:** der Erfolgsfall (Liste mit Testdaten und Bildern), weil die Dateien erst nach dem Push auf GitHub liegen.
10. **16d:** `README.md` ergänzt (Beschreibung, Funktionen, Internet zur Laufzeit, Testklassen, Abschnitt „Test data“ mit der Liste der Bilddateien, „Known limitations“, Code-Struktur).
11. `AI_USAGE.md`: dieses Ergebnis eingetragen.

**Kurzfazit im Antwortfeld der App:** „Die Anzeigen kommen jetzt als Testdaten aus testdata/anzeigen.json auf GitHub und werden in Room übernommen. Fehler werden nach Art getrennt angezeigt, mit Refresh-Button. Wichtig: Der Branch heißt master, nicht main. Build, 52 Tests, Lint und die Fehlerfälle auf dem Emulator sind geprüft. Der Erfolgsfall ist erst nach dem Push testbar. Es ist nichts committet.“ Frage: „Soll ich nach eurem Commit und Push den Erfolgsfall auf dem Emulator testen?“

#### Akzeptiert / Geändert / Abgelehnt

- **Akzeptiert:** Alles
- **Geändert:** Nichts
- **Abgelehnt:** Nichts

#### Geprüft / Getestet

Fehlermeldungen überprüft, Bilder aus Github Test erst nach Push möglich
->Nach Push: Testdaten werden geladen, aber man kann sie bearbeiten. Das sollte nicht so sein
#### Commits

- `66579a0` Retrofit, kotlinx-serialization-json, Coil-Netzwerk und INTERNET-Permission
- `cbcf904` Testdaten-JSON, DTO und Retrofit-Schnittstelle
- `5e5a0d2` Anzeigen aus dem Netz laden mit Fehlerarten und Refresh
- `dc903aa` README und Logbuch zu den Netzwerk-Testdaten

---

### Eintrag 23: Erfolgsfall testen, Testdaten schreibgeschützt, App-Icon

- **Datum:** 25.11.2026
- **Autor:** Semih
- **Tool:** Claude Code (Desktop-App), Claude Opus 5.5

#### Prompt 1

````text
Ja kannst testen. Außerdem ist uns aufgefallen, dass man die Testdaten aus GIthub bearbeiten kann. Das sollte nicht möglich sein.
Wir hätten auch noch gerne ein App Icon, kannst du da was bauen?
````

#### Ergebnis der KI zu Prompt 1

**Ausgeführte Aktionen:**

1. `git status`, `git log --oneline -6`, `git fetch` ausgeführt. Ergebnis: Schritt 16 in vier Commits committet und auf `master` gepusht. In der Arbeitskopie von `AI_USAGE.md` stand nur die neue Notiz des Teams in Eintrag 22, sie bleibt. In Eintrag 22 die vier Hashes unter „Commits“ eingetragen, Eintrag 23 mit dem Prompt angelegt (Skript `log23.py`).
2. Mit `curl` geprüft: `testdata/anzeigen.json` und alle 6 Bilder liefern HTTP 200.
3. Emulator, Erfolgsfall: App gestartet → Log `Loaded 6 test ads, 0 new` (schon beim Test des Teams eingefügt). Die Liste zeigt die Testanzeigen mit den Bildern aus GitHub (Screenshot). Refresh-Button → erneut `0 new`. Gefunden: Die Testanzeige „Analysis 1 – Lehrbuch“ war in der App zu „… test“ geändert worden.
4. Testdaten schreibgeschützt (Skript `step17.py`): `data/Ad.kt` (berechnete Eigenschaft `isTestData`), `data/AdDao.kt` (neu `updateTestData`, Kommentar zu `insertOrIgnore` angepasst), `data/AdRepository.kt` (`refreshAdsFromNetwork` übernimmt für vorhandene Testanzeigen Texte und Bild aus der JSON-Datei; Favorit, verkauft und Erstellzeit bleiben), `ui/addetail/AdDetailScreen.kt` (kein Stift bei Testanzeigen, Hinweistext unter dem Preis), `ui/adform/AdFormUiState.kt` (`isReadOnly`), `ui/adform/AdFormViewModel.kt` (Testanzeige → `isReadOnly`, Speichern gesperrt), `ui/adform/AdFormScreen.kt` (Hinweis statt Formular), `strings.xml` (2 Texte). `AdMappingTest.kt` um 1 Test und 1 Prüfung zu `isTestData` ergänzt.
5. App-Icon: `res/drawable/ic_launcher_background.xml` (violette Fläche) und `ic_launcher_foreground.xml` (weiße Einkaufstasche mit Doktorhut) neu gezeichnet, neu `ic_launcher_monochrome.xml` für Themed Icons. `mipmap-anydpi/ic_launcher.xml` und `ic_launcher_round.xml` zeigen für `monochrome` auf die neue Datei. Die alten Bitmaps `mipmap-hdpi` bis `mipmap-xxxhdpi` (`ic_launcher*.webp`, Android-Standard-Icon) gelöscht, weil sie bei minSdk 26 nie benutzt werden.
6. `gradlew.bat assembleDebug testDebugUnitTest lintDebug` ausgeführt. Ergebnis: **BUILD SUCCESSFUL**, 53/53 Tests grün, Lint 0 Fehler (11 Warnungen wie bisher: Versionshinweise, Plural-Hinweis).
7. Emulator: App installiert und gestartet → „Analysis 1 – Lehrbuch“ heißt nach dem Laden wieder so (Text aus GitHub übernommen). Detail einer Testanzeige: kein „Bearbeiten“, Hinweis „Testanzeige aus GitHub, nicht bearbeitbar“ (Screenshot). Detail der eigenen Anzeige „Galerietest“: „Bearbeiten“ vorhanden. App-Übersicht: neues Icon wird angezeigt (Screenshot). Absturz-Log leer.
8. `README.md`: schreibgeschützte Testanzeigen, Übernahme der Texte beim Aktualisieren, App-Icon, `AdMappingTest`, Icon-Dateien in der Code-Struktur.
9. `AI_USAGE.md`: dieses Ergebnis eingetragen.

**Kurzfazit im Antwortfeld der App:** „Der Erfolgsfall funktioniert: Die 6 Testanzeigen kommen mit Bildern von GitHub. Testanzeigen sind jetzt schreibgeschützt, und beim Aktualisieren gelten wieder die Texte aus GitHub. Dazu gibt es ein neues App-Icon (Einkaufstasche mit Doktorhut). Build, 53 Tests, Lint und Emulator-Test liefen fehlerfrei. Es ist nichts committet.“ Frage: „Sollen Testanzeigen auch nicht mehr gelöscht werden können?“

#### Akzeptiert / Geändert / Abgelehnt

- **Akzeptiert:** Alles
- **Geändert:** Nichts
- **Abgelehnt:** Nichts

#### Geprüft / Getestet

Icon der App und Testdaten geprüft  

#### Commits

App-Icon sowie Bearbeitungsfunktion überarbeitet

---

## 3. Reflexion

_Platzhalter: Wird zum Projektende ausgefüllt (1–2 Seiten)._
