# KI-Nutzung im Projekt CampusMarket

Dieses Dokument legt die gesamte KI-Nutzung beider Gruppenmitglieder offen.
Alle Prompts stehen wortwörtlich, in Originalsprache und in der Reihenfolge, in der sie gesendet wurden.
Geheimnisse und personenbezogene Daten sind durch `[REDACTED]` ersetzt. Angehängte Dateien werden nur mit Namen genannt.

Hinweis zu den Links: Nach der Umbenennung des Pakets (Eintrag 8) zeigten Datei-Links in älteren Einträgen auf Ordner, die es nicht mehr gibt. In Eintrag 11 wurde deshalb nur das Linkziel (der Teil in runden Klammern) von `com/example/campusmarket` auf `de/dhbw/campusmarket` umgestellt. Der sichtbare Text aller Einträge ist unverändert.

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

#### Ergebnis der KI zu Prompt 1 (vollständig)

**Ausgeführte Aktionen:** Dateiliste und Gradle-Dateien gelesen (`settings.gradle.kts`, `build.gradle.kts`, `app/build.gradle.kts`, `gradle/libs.versions.toml`, `gradle.properties`, `gradle-wrapper.properties`), außerdem `MainActivity.kt`, `AndroidManifest.xml`, `themes.xml`, `strings.xml`, `Theme.kt`, `gradle-daemon-jvm.properties`, `rules.keep`. Danach `AI_USAGE.md` und `CLAUDE.md` neu angelegt. Kein App-Code geändert.

**Kurzfazit im Antwortfeld der App:** „CLAUDE.md und AI_USAGE.md liegen jetzt im Root, deine Nachricht steht wortwörtlich als Eintrag 1 drin. Am App-Code habe ich noch nichts geändert, und es ist nichts committet. Die Autorennamen fehlen noch.“ Frage: „Passt der Plan so, darf ich mit Schritt 1 anfangen?“

**Antwort der KI:**

````markdown
Ich habe das Projekt angesehen und die beiden Dateien angelegt. Am App-Code habe ich noch nichts geändert.

## Was im Projekt steht

|                        |                                                                    |
|------------------------|--------------------------------------------------------------------|
| Paket / applicationId  | `com.example.campusmarket`                                         |
| Theme                  | `CampusMarketTheme` (Compose), `Theme.CampusMarket` (XML)          |
| Gradle / AGP / Kotlin  | 9.6.0 / 9.4.1 / 2.2.10, Versionen in `gradle/libs.versions.toml`   |
| Compose BOM            | 2026.02.01                                                         |
| compileSdk / targetSdk | 37                                                                 |
| **minSdk**             | **24**, muss laut Vorgabe auf **26**. Das ändere ich in Schritt 1. |

Außerdem gibt es noch den Beispielcode von Android Studio (`Greeting`, `ExampleUnitTest`, `ExampleInstrumentedTest`). Der fliegt raus, sobald wir ihn ersetzen, damit kein toter Code bleibt. AGP 9 bringt Kotlin schon mit, deshalb brauchen wir kein extra `kotlin-android`-Plugin.

## Angelegte Dateien

- [AI_USAGE.md](AI_USAGE.md): Setup-Tabelle, Eintrag 1 mit deiner Nachricht wortwörtlich und einem Platzhalter für die Reflexion. Felder, die nur ihr kennt, stehen auf „eintragen“ oder haben einen markierten Vorschlag.
- [CLAUDE.md](CLAUDE.md): prüfbare Regeln zu Architektur, State, Screens, Navigation, Kamera, Tests, Robustheit und Abgabe. Die vier Pflichtregeln sind dabei, zum Beispiel „Nur Room-Zugriffe im Repository/DAO“.

## Plan (ein Schritt = ein Commit, der für sich baut)

**Mindestanforderungen**
1. **Build-Setup:** minSdk 26, Abhängigkeiten für Room + KSP, Navigation, Serialization, ViewModel-Compose und Coil.
2. **Room-Datenschicht:** `Ad`-Entity, `AdDao` mit Flows, `AppDatabase`, `AdRepository`, `CampusMarketApplication` mit Beispieldaten beim ersten Start.
3. **Navigation-Grundgerüst:** `@Serializable`-Destinations, `NavHost` in `MainActivity`, Beispielcode entfernen.
4. **Liste:** `AdListViewModel` und UiState mit Loading/Error/Ready, Route und Screen mit Preview.
5. **Suche:** reine Funktion `searchAds()` mit JUnit-Tests, Suchfeld in der Liste.
6. **Detail mit Löschen:** Detail-Screen (Infos, Bild, Zustand, Übergabeort), Bestätigungsdialog vor dem Löschen.
7. **Erstellen ohne Foto:** Formular mit Validierung im ViewModel; die Eingaben überleben eine Drehung.
8. **Kamera:** Permission, `TakePicture` mit `FileProvider`, Hinweis bei Ablehnung, Anzeige mit Coil.
9. **README** mit Build-Schritten und dem Pflichtsatz.

**Weitere Features**

10. **Bearbeiten:** das Formular aus Schritt 7 wiederverwenden, mit optionaler id.
11. **Favoriten:** Feld in Room, Herz-Button, Favoriten-Filter.
12. **Status verfügbar/verkauft:** Umschalten im Detail, Badge in der Liste.
13. **Filter** nach Kategorie, Zustand und Status als reine Funktion mit Tests.
14. **Sortierung** nach Preis und Datum als reine Funktion mit Tests.
15. **Galerie-Fallback:** `PickVisualMedia`, wenn die Kamera abgelehnt wurde oder fehlt.

Wenn danach noch Zeit ist, kommen Feinschliff (leere Liste, Texte, Icons) und die Reflexion.

## Commit-Vorschlag für jetzt

```
docs: CLAUDE.md und AI_USAGE.md mit Regeln und Logbuch anlegen
```

**Wie heißen die beiden Autoren?** Ich trage die Namen ein, sobald ich sie habe, und wer von euch Eintrag 1 geschickt hat. Im Logbuch schreibe ich nur die Namen hinein. E-Mail-Adressen würde ich durch `[REDACTED]` ersetzen.
````

#### Prompt 2 (11.11.2026)

````text
Ändere deine Anleitung so, dass du dein komplettes Ergebnis in die AI-USAGE einfügst. Mache das rückwirkend auch für die erste Nachricht.
Danach fangen wir mit Schritt 1 an
````

#### Ergebnis der KI zu Prompt 2 (vollständig)

Siehe Eintrag 2. Die Antwort auf Prompt 2 deckt die Regeländerung und Schritt 1 gemeinsam ab und steht dort vollständig.

#### Akzeptiert / Geändert / Abgelehnt

- **Akzeptiert:** CLAUDE.md und AI_USAGE.md übernommen, weil sie die Vorgaben aus der Aufgabenstellung abbilden.
- **Geändert:** nichts
- **Abgelehnt:** nichts

#### Geprüft / Getestet

Beide Dateien gelesen und mit der Aufgabenstellung abgeglichen. Kein Build nötig, da kein Code geändert wurde.

#### Commits

CLAUDE.md und AI_USAGE.md anlegen

---

### Eintrag 2: Schritt 1 – Build-Setup (minSdk 26, Room + KSP, Navigation, Serialization, ViewModel-Compose, Coil)

- **Datum:** 11.11.2026
- **Autor:** Benjamin
- **Tool:** Claude Code (Desktop-App), Claude Opus 5.5

#### Prompt 1

Derselbe Prompt wie Eintrag 1, Prompt 2 (dort wortwörtlich). Der letzte Satz „Danach fangen wir mit Schritt 1 an“ hat diesen Eintrag ausgelöst.

#### Ergebnis der KI zu Prompt 1 (vollständig)

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

**Antwort der KI:**

````markdown
Schritt 1 ist fertig: Debug-Build und Unit-Tests laufen durch. Geändert habe ich nur die drei Gradle-Dateien, am App-Code nichts.

## Logbuch

Die Regel „Komplettes KI-Ergebnis ins Logbuch“ steht als Punkt 2 in [CLAUDE.md](CLAUDE.md). In [AI_USAGE.md](AI_USAGE.md) steht jetzt die komplette Antwort zu Prompt 1 mit Aktionen und Kurzfazit. Deinen letzten Prompt habe ich als Prompt 2 in Eintrag 1 eingetragen, weil er die Logbuch-Regel ändert. Schritt 1 und diese Antwort stehen vollständig in Eintrag 2.

## Was sich geändert hat

| Datei                                                  | Änderung                                                                                     | Warum                                                                                  |
|--------------------------------------------------------|----------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------|
| [app/build.gradle.kts](app/build.gradle.kts)           | `minSdk` 24 → 26                                                                             | Vorgabe der Aufgabe                                                                    |
| [app/build.gradle.kts](app/build.gradle.kts)           | Plugins `kotlin.serialization` und `ksp`, neue `implementation`-Zeilen, `ksp(room.compiler)` | Die App nutzt die Bibliotheken ab Schritt 2                                            |
| [build.gradle.kts](build.gradle.kts)                   | Die zwei Plugins mit `apply false`                                                           | Gradle lädt die Plugin-Version einmal zentral, angewendet werden sie erst im App-Modul |
| [gradle/libs.versions.toml](gradle/libs.versions.toml) | Versionen und Einträge für alle neuen Bibliotheken                                           | Alle Versionen stehen an einer Stelle                                                  |

**Neue Bibliotheken und wofür wir sie brauchen:**

- **lifecycle-viewmodel-compose** (2.11.0): `viewModel(factory = …)` und `viewModelFactory { initializer { … } }` in der Route.
- **lifecycle-runtime-compose** (2.11.0): `collectAsStateWithLifecycle()`, damit die UI den StateFlow nur sammelt, solange sie sichtbar ist.
- **navigation-compose** (2.10.2): `NavHost` und typsichere Destinations.
- **Plugin kotlin-serialization**: macht `@Serializable` auf den Destinations möglich. Eine eigene JSON-Bibliothek brauchen wir nicht, weil Navigation `kotlinx-serialization-core` schon mitbringt.
- **room-runtime** + **room-compiler über KSP** (2.8.5, KSP 2.3.13): Die Datenbank; KSP erzeugt beim Build den Code für DAO und Datenbank. `room-ktx` habe ich wieder rausgenommen, weil Flow und `suspend` seit Room 2.7 schon in `room-runtime` stecken.
- **coil-compose** (3.3.0): `AsyncImage` zeigt die Fotos an. Wir laden nur lokale Dateien, deshalb ohne Netzwerk-Modul.

## Zwei Stolpersteine

1. **Coil-Version:** Mit der neuesten Version 3.6.3 ist der Build gebrochen („compiled with an incompatible version of Kotlin … metadata version is 2.4.0“). Coil 3.6.3 zieht Kotlin 2.4 und Compose 1.12 rein, der Compiler im Projekt ist aber Kotlin 2.2. Coil 3.3.0 ist mit Kotlin 2.2.0 gebaut und passt. Das habe ich als Regel in CLAUDE.md festgehalten.
2. **`.idea/` wird nicht ignoriert:** Ihr habt `.idea/` in `app/.gitignore` eingetragen. Diese Datei gilt aber nur für den Ordner `app/`, deshalb zeigt `git status` weiter `?? .idea/`. Die Zeile gehört in die `.gitignore` im Root. Ich habe sie nicht angefasst, weil die Änderung von euch kommt.

## Testanleitung

- In Android Studio „Sync Project with Gradle Files“, dann die App auf einem Emulator mit API 26 oder höher starten. Erwartung: Wie vorher erscheint „Hello Android!“.
- Im Terminal (Windows: `gradlew.bat`) muss am Ende `BUILD SUCCESSFUL` stehen:

```bash
./gradlew assembleDebug testDebugUnitTest
```

- Rotation, Flugmodus und App-Neustart: Verhalten unverändert, kein Absturz.
- Permission verweigert und ungültige Eingaben gibt es noch nicht; das testen wir ab Schritt 7 und 8.
- Auf Geräten mit API 24/25 lässt sich die App jetzt nicht mehr installieren. Das ist gewollt.

## Commit-Vorschläge

Am besten zwei Commits, damit Doku und Build getrennt sind:

```
docs: Regel für vollständige KI-Ergebnisse im Logbuch
```

(`CLAUDE.md`, `AI_USAGE.md`)

```
build: minSdk 26 und Abhängigkeiten für Room, Navigation und Coil
```

(`gradle/libs.versions.toml`, `build.gradle.kts`, `app/build.gradle.kts`)
````

#### Akzeptiert / Geändert / Abgelehnt

- **Akzeptiert:** Ganze Antwort
- **Geändert:** Coil von 3.6.3 auf 3.3.0 gesenkt, weil 3.6.3 mit Kotlin 2.2 nicht baut
- **Abgelehnt:** nichts

#### Geprüft / Getestet

`gradlew assembleDebug testDebugUnitTest` erfolgreich; App startet auf Emulator API ≥ 26.

#### Commits

1.  Regel für vollständige KI-Ergebnisse im Logbuch
2.  minSdk 26 und Abhängigkeiten für Room, Navigation und Coil

---

### Eintrag 3: Schritt 2 – Room-Datenschicht (Entity, DAO, Datenbank, Repository, Application-Klasse, Beispieldaten)

- **Datum:** 17.11.2026
- **Autor:** Semih
- **Tool:** Claude Code (Desktop-App), Claude Opus 5.5

#### Prompt 1

````text
Ja, Schritt 2
````

#### Ergebnis der KI zu Prompt 1 (vollständig)

**Ausgeführte Aktionen:**

1. `git status`, `git log`, die Überschriften von `AI_USAGE.md` und die Root-`.gitignore` gelesen. Ergebnis: Schritt 1 ist committet (`3b78ba6`), `app/.gitignore` geändert, `.idea/` weiter untracked.
2. `AI_USAGE.md`: Eintrag 3 mit dem Prompt angelegt.
3. Neu angelegt: `data/Category.kt`, `data/Condition.kt`, `data/Ad.kt`, `data/AdDao.kt`, `data/AppDatabase.kt`, `data/AdRepository.kt`, `data/SampleData.kt`, `CampusMarketApplication.kt` (alle unter `app/src/main/java/com/example/campusmarket/`).
4. Geändert: `app/src/main/AndroidManifest.xml` (`android:name=".CampusMarketApplication"`).
5. `gradlew.bat assembleDebug testDebugUnitTest` ausgeführt. Ergebnis: **BUILD SUCCESSFUL**. KSP hat `AdDao_Impl.kt` und `AppDatabase_Impl.kt` erzeugt.
6. `adb devices` ausgeführt. Ergebnis: kein Gerät/Emulator verbunden, daher kein Laufzeittest.
7. `AI_USAGE.md`: dieses Ergebnis eingetragen.

**Kurzfazit im Antwortfeld der App:** „Schritt 2 ist umgesetzt: Entity, DAO, Datenbank, Repository und CampusMarketApplication mit Beispieldaten sind angelegt, der Build läuft durch. Ob die Beispieldaten wirklich in der Datenbank landen, konnte ich nicht prüfen, weil kein Emulator lief. Es ist nichts committet.“ Frage: „Soll ich mit Schritt 3 (Navigation-Grundgerüst) weitermachen?“

**Antwort der KI:**

````markdown
Schritt 2 ist fertig: Die Room-Datenschicht steht, und der Build läuft durch. KSP hat beim Build `AdDao_Impl` und `AppDatabase_Impl` erzeugt, also versteht Room unsere Klassen. Sichtbar ist in der App noch nichts, die Liste kommt in Schritt 4. Ob die Beispieldaten wirklich in der Datenbank landen, konnte ich nicht prüfen, weil kein Emulator lief. Das bitte selbst kurz testen (siehe unten).

## Neue Dateien (`app/src/main/java/com/example/campusmarket/`)

| Datei                                                                                           | Was sie tut                                                                          |
|-------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------|
| [data/Category.kt](app/src/main/java/de/dhbw/campusmarket/data/Category.kt)                     | Enum mit den Kategorien (Bücher, Skripte, Taschenrechner, Technik, Möbel, Sonstiges) |
| [data/Condition.kt](app/src/main/java/de/dhbw/campusmarket/data/Condition.kt)                   | Enum für den Zustand (neu, wie neu, gut, gebraucht)                                  |
| [data/Ad.kt](app/src/main/java/de/dhbw/campusmarket/data/Ad.kt)                                 | `@Entity`: eine Anzeige = eine Zeile in der Tabelle `ads`                            |
| [data/AdDao.kt](app/src/main/java/de/dhbw/campusmarket/data/AdDao.kt)                           | Alle SQL-Zugriffe. Lesen liefert `Flow`, Schreiben ist `suspend`                     |
| [data/AppDatabase.kt](app/src/main/java/de/dhbw/campusmarket/data/AppDatabase.kt)               | `@Database` mit der Tabelle `ads`, Version 1                                         |
| [data/AdRepository.kt](app/src/main/java/de/dhbw/campusmarket/data/AdRepository.kt)             | Einziger Zugang der ViewModels zu den Daten, reicht an das DAO weiter                |
| [data/SampleData.kt](app/src/main/java/de/dhbw/campusmarket/data/SampleData.kt)                 | Sechs Beispielanzeigen, eine davon schon „verkauft“                                  |
| [CampusMarketApplication.kt](app/src/main/java/de/dhbw/campusmarket/CampusMarketApplication.kt) | Baut Datenbank und Repository genau einmal und fügt beim Start die Beispieldaten ein |

Außerdem steht in [AndroidManifest.xml](app/src/main/AndroidManifest.xml) jetzt `android:name=".CampusMarketApplication"`. Ohne diese Zeile würde Android unsere Klasse nie erzeugen.

## Warum so

- **Enums statt Strings für Kategorie und Zustand:** Es gibt keine Tippfehler wie „Buch“ gegen „Bücher“, und Filter in Schritt 13 können einfach mit `==` vergleichen. Room speichert Enums ohne extra Code als Text (`"BOOKS"`). Die deutschen Anzeigenamen kommen später in die UI über `strings.xml`, damit die Datenschicht keine Android-Ressourcen braucht.
- **`isFavorite`, `isSold` und `createdAt` schon jetzt in `Ad`:** Das weicht leicht vom Plan ab (dort erst in Schritt 11/12). Sonst müssten wir später die Datenbank-Version erhöhen und eine Migration schreiben, und das ist mehr Code zum Erklären. Die Felder haben Standardwerte, stören also nicht.
- **`Flow` beim Lesen:** Ändert sich die Tabelle, schickt Room die neue Liste von selbst. Damit ist die Datenbank die Single Source of Truth, und kein ViewModel muss nach dem Speichern „neu laden“.
- **Beispieldaten über `count() == 0`:** Sie werden nur bei leerer Tabelle eingefügt. Nach einem Neustart tauchen sie also nicht doppelt auf. Das ist einfacher als ein `RoomDatabase.Callback` mit rohem SQL.
- **`by lazy` in der Application:** Datenbank und Repository werden beim ersten Zugriff gebaut und danach immer wiederverwendet. Die ViewModel-Factories holen sich ab Schritt 4 das Repository über `application.repository`.
- **`MainScope().launch`:** Room führt `suspend`-Funktionen selbst auf einem Hintergrund-Thread aus. Der Start blockiert also nicht, auch wenn wir auf dem Main-Scope starten.
- **`exportSchema = false`:** Wir haben keine Migrationen, also brauchen wir keinen Schema-Export-Ordner. Ohne diese Angabe gäbe es eine Build-Warnung.
- **Preis als `Double`:** Für eine Anzeige mit zwei Nachkommastellen reicht das und ist leicht zu erklären. Gerechnet wird mit dem Preis nicht, nur angezeigt und sortiert.

## Testanleitung

- Build im Terminal (Windows: `gradlew.bat`), Erwartung `BUILD SUCCESSFUL`:

```bash
./gradlew assembleDebug testDebugUnitTest
```

- **Beispieldaten prüfen:** App auf dem Emulator starten, dann in Android Studio **View → Tool Windows → App Inspection → Database Inspector** öffnen. Unter `campusmarket.db` steht die Tabelle `ads` mit 6 Zeilen.
- **App-Neustart:** App schließen (aus „Zuletzt verwendet“ wegwischen) und neu starten. Es sind weiter genau 6 Zeilen, nicht 12.
- **Daten löschen:** Einstellungen → Apps → CampusMarket → Speicher leeren, dann neu starten. Die 6 Beispielzeilen sind wieder da.
- **Rotation, Flugmodus:** Die Oberfläche ist noch „Hello Android!“. Kein Absturz erwartet, die Datenbank braucht kein Netz.
- Permission verweigert und ungültige Eingaben gibt es noch nicht.

## Commit-Vorschlag

```
feat: Room-Datenschicht mit Repository und Beispieldaten
```

(`data/*`, `CampusMarketApplication.kt`, `AndroidManifest.xml`, `AI_USAGE.md`)

Noch offen von letztem Mal: `.idea/` steht weiter in `app/.gitignore` statt in der `.gitignore` im Root. Deshalb zeigt `git status` den Ordner noch als untracked.
````

#### Akzeptiert / Geändert / Abgelehnt

- **Akzeptiert:** Ganze Antwort
- **Geändert:** nichts
- **Abgelehnt:** nichts

#### Geprüft / Getestet

Build erfolgreich; im Database Inspector 6 Zeilen in `ads`, nach Neustart weiter 6.

#### Commits

Room-Datenschicht mit Repository und Beispieldaten.

---

### Eintrag 4: Schritt 3 – Navigation-Grundgerüst (Destinations, NavHost, Beispielcode entfernen)

- **Datum:** 18.11.2026
- **Autor:** Benjamin
- **Tool:** Claude Code (Desktop-App), Claude Opus 5.5

#### Prompt 1

````text
Schau dir nochmal das komplette Projekt an und mach dann weiter mit Schritt 3
````

#### Ergebnis der KI zu Prompt 1 (vollständig)

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

**Antwort der KI:**

````markdown
Schritt 3 ist fertig: Die App startet jetzt über einen NavHost, und man kann von der Startseite zur Detailseite und wieder zurück. Debug-Build und Unit-Tests laufen durch. Auf einem Gerät konnte ich es nicht testen, weil kein Emulator lief. Bitte kurz selbst prüfen (siehe unten).

## Projekt-Check vorab

- Schritt 1 und 2 sind sauber committet, die Datenschicht passt zu den Regeln in CLAUDE.md (Room nur in DAO/Repository, Flow beim Lesen, `suspend` beim Schreiben, Application-Klasse im Manifest).
- **`.idea/` ist weiter untracked.** Commit `82ad9da` hat den Ordner aus dem Repo entfernt, aber in der Root-`.gitignore` stehen nur einzelne Dateien wie `/.idea/workspace.xml`. Es fehlt eine Zeile `/.idea/` in der Root-`.gitignore`. Ich habe das nicht angefasst, weil es eure Entscheidung ist.
- `ExampleUnitTest` und `ExampleInstrumentedTest` bleiben noch. Sie fliegen raus, sobald es echte Tests gibt (Schritt 5).

## Neue Dateien (`app/src/main/java/com/example/campusmarket/`)

| Datei                                                                                                 | Was sie tut                                                                   |
|-------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------|
| [ui/navigation/Destinations.kt](app/src/main/java/de/dhbw/campusmarket/ui/navigation/Destinations.kt) | Die zwei Ziele: `AdList` (Startseite) und `AdDetail(adId)` (nur die id)       |
| [ui/navigation/AppNavHost.kt](app/src/main/java/de/dhbw/campusmarket/ui/navigation/AppNavHost.kt)     | Der `NavHost`. Einziger Ort mit `navController`                               |
| [ui/adlist/AdListScreen.kt](app/src/main/java/de/dhbw/campusmarket/ui/adlist/AdListScreen.kt)         | Platzhalter mit `TopAppBar` und Button „Beispielanzeige öffnen“, plus Preview |
| [ui/addetail/AdDetailScreen.kt](app/src/main/java/de/dhbw/campusmarket/ui/addetail/AdDetailScreen.kt) | Platzhalter mit Zurück-Pfeil, zeigt die übergebene id, plus Preview           |
| [res/drawable/ic_arrow_back.xml](app/src/main/res/drawable/ic_arrow_back.xml)                         | Zurück-Pfeil als Vector-Drawable                                              |

Geändert: [MainActivity.kt](app/src/main/java/de/dhbw/campusmarket/MainActivity.kt) (Beispielcode `Greeting` raus, ruft nur noch `AppNavHost()` auf) und [strings.xml](app/src/main/res/values/strings.xml) (Texte der Platzhalter, „Zurück“, „Details“). In [CLAUDE.md](CLAUDE.md) steht eine neue Regel zu Icons.

## Warum so

- **`@Serializable` statt String-Routen:** `navController.navigate(AdDetail(5))` prüft der Compiler. Bei `"detail/5"` würde ein Tippfehler erst zur Laufzeit auffallen.
- **Nur die id wird übergeben:** Die Detailseite holt sich die Anzeige ab Schritt 6 selbst aus Room. So zeigt sie immer den aktuellen Stand aus der Datenbank.
- **Screens bekommen nur Callbacks** (`onAdClick`, `onBack`): Die Screens kennen keinen `navController`, dadurch lassen sie sich in der Preview ohne Navigation anzeigen.
- **`navigateUp()` statt `popBackStack()`:** Tippt man zweimal schnell auf „Zurück“, könnte `popBackStack()` auch die Startseite entfernen, und der Bildschirm wäre leer. `navigateUp()` geht nie über die Startseite hinaus.
- **Platzhalter-Screens:** Sie liegen schon in den Dateien, in denen die echten Screens entstehen. In Schritt 4 und 6 kommen dort Route, ViewModel und UiState dazu, und die Platzhalter-Texte samt Button werden wieder entfernt. Deshalb gibt es jetzt auch noch keine `…Route`-Funktion: Ohne ViewModel hätte sie nichts zu tun.
- **Icon als Vector-Drawable statt `Icons.AutoMirrored.Filled.ArrowBack`:** Material 3 bringt die Icon-Bibliothek nicht mehr automatisch mit, und für ein paar Icons lohnt sich keine extra Bibliothek. `autoMirrored` dreht den Pfeil bei Sprachen, die von rechts nach links laufen.
- **Ein Build-Fehler unterwegs:** Die Vorlage für das Icon nutzte die Farbe `?attr/colorControlNormal`. Die gibt es nur mit AppCompat, das wir nicht haben. Ich habe die Zeile entfernt. Das Compose-`Icon` färbt das Bild sowieso selbst in der Theme-Farbe ein.
- **`@OptIn(ExperimentalMaterial3Api::class)`:** `TopAppBar` ist in Material 3 noch als experimentell markiert. Ohne die Annotation baut der Code nicht.

## Testanleitung

- Build im Terminal (Windows: `gradlew.bat`), Erwartung `BUILD SUCCESSFUL`:

```bash
./gradlew assembleDebug testDebugUnitTest
```

- **Navigation:** App starten. Oben steht „CampusMarket“, darunter der Platzhalter-Text und der Button. Button antippen → Seite „Details“ mit „Anzeige Nr. 1“. Pfeil oben links oder System-Zurück → wieder Startseite. Nochmal Zurück → App schließt sich.
- **Rotation:** Auf der Detailseite das Gerät drehen. Man bleibt auf „Anzeige Nr. 1“ und landet nicht auf der Startseite.
- **Doppelter Tipp:** Auf der Detailseite den Zurück-Pfeil zweimal schnell antippen. Die Startseite muss stehen bleiben, kein leerer Bildschirm.
- **Flugmodus:** Kein Absturz, es gibt keine Netzwerkzugriffe.
- **App-Neustart:** Die App startet wieder auf der Startseite. Die 6 Beispielzeilen im Database Inspector sind weiter da.
- **Preview:** In Android Studio `AdListScreen.kt` und `AdDetailScreen.kt` öffnen, die Previews müssen rendern.
- Permission verweigert und ungültige Eingaben gibt es noch nicht.

## Commit-Vorschlag

```
feat: typsichere Navigation mit Liste und Detail als Platzhalter
```

(`ui/navigation/*`, `ui/adlist/AdListScreen.kt`, `ui/addetail/AdDetailScreen.kt`, `MainActivity.kt`, `strings.xml`, `ic_arrow_back.xml`, `CLAUDE.md`, `AI_USAGE.md`)
````

#### Akzeptiert / Geändert / Abgelehnt

- **Akzeptiert:** Ganze Antwort
- **Geändert:** Keine Änderung
- **Abgelehnt:** Nichts

#### Geprüft / Getestet

App erfolgreich ohne Fehler gebaut

#### Commits

Typsichere Navigation mit Liste und Detail als Platzhalter

---

### Eintrag 5: Schritt 4 – Anzeigenliste mit ViewModel und UiState (Loading/Error/Ready)

- **Datum:** 18.11.2026
- **Autor:** Benjamin
- **Tool:** Claude Code (Desktop-App), Claude Opus 5.5

#### Prompt 1

````text
Ja, Schritt 4
````

#### Ergebnis der KI zu Prompt 1 (vollständig)

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

**Antwort der KI:**

````markdown
Schritt 4 ist fertig: Die Startseite zeigt jetzt die echten Anzeigen aus Room. Diesmal lief ein Emulator, deshalb habe ich auch auf dem Gerät getestet: Antippen, Drehung, Zurück, Flugmodus und Neustart liefen ohne Absturz.

## Neue und geänderte Dateien (`app/src/main/java/com/example/campusmarket/`)

| Datei                                                                                               | Was sie tut                                                                                                                                                           |
|-----------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [ui/adlist/AdListUiState.kt](app/src/main/java/de/dhbw/campusmarket/ui/adlist/AdListUiState.kt)     | `isLoading`, `hasError`, `ads`: damit sind Loading, Error und Ready abgebildet                                                                                        |
| [ui/adlist/AdListViewModel.kt](app/src/main/java/de/dhbw/campusmarket/ui/adlist/AdListViewModel.kt) | Sammelt `repository.getAllAds()` und schreibt das Ergebnis über `_uiState.update { … }` in den State. Die `Factory` holt das Repository aus `CampusMarketApplication` |
| [ui/adlist/AdListScreen.kt](app/src/main/java/de/dhbw/campusmarket/ui/adlist/AdListScreen.kt)       | `AdListRoute` (ViewModel + `collectAsStateWithLifecycle`) und der stateless `AdListScreen`, plus Previews für „Ready“ und „Error“                                     |
| [ui/PriceFormat.kt](app/src/main/java/de/dhbw/campusmarket/ui/PriceFormat.kt)                       | `formatPrice(12.5)` → „12,50 €“                                                                                                                                       |
| [PriceFormatTest.kt](app/src/test/java/de/dhbw/campusmarket/ui/PriceFormatTest.kt)                  | 3 JUnit-Tests für `formatPrice`                                                                                                                                       |

Außerdem: [AppNavHost.kt](app/src/main/java/de/dhbw/campusmarket/ui/navigation/AppNavHost.kt) ruft jetzt `AdListRoute` statt des Platzhalters auf. In [strings.xml](app/src/main/res/values/strings.xml) sind die Platzhalter-Texte raus, die Texte für den Fehlerfall und die leere Liste sind neu. `ExampleUnitTest.kt` ist gelöscht, weil es jetzt einen echten Test gibt.

## Warum so

- **Drei Zustände in einer data class:** Der Screen entscheidet per `when` der Reihe nach: lädt → Spinner, Fehler → Text + „Erneut versuchen“, leere Liste → Hinweis, sonst die Liste. `isLoading` startet mit `true`, damit vor der ersten Antwort von Room kein „Noch keine Anzeigen“ aufblitzt.
- **`hasError` statt Fehlertext im State:** Der ViewModel hat keinen `Context`, kann also keine Texte aus `strings.xml` holen. Der Screen wählt den Text selbst.
- **`.catch { … }` vor `.collect`:** Wirft Room einen Fehler (z. B. kaputte Datenbankdatei), landet die App im Error-Zustand statt abzustürzen.
- **`loadAds()` mit `loadJob?.cancel()`:** „Erneut versuchen“ startet das Sammeln neu, ohne dass zwei Sammler gleichzeitig laufen.
- **Flow bleibt offen:** Nach Löschen oder Erstellen (Schritt 6/7) kommt die neue Liste von selbst, ohne Neuladen.
- **`viewModel(factory = …)` in der Route:** Der ViewModel hängt am Back-Stack-Eintrag. Bei einer Drehung wird derselbe ViewModel wiederverwendet, die Liste muss nicht neu geladen werden.
- **`items(ads, key = { it.id })`:** Compose erkennt jede Anzeige an ihrer id wieder, wenn sich die Liste ändert.
- **`AdListContent` statt `AdList`:** So heißt schon unsere Destination, das wäre beim Lesen verwirrend gewesen.
- **Preis mit `Locale.GERMANY`:** Zuerst stand das Format in `strings.xml`. Auf dem Emulator (Sprache Englisch) kam dann „15.00 €“ heraus. Weil die ganze App deutsch ist, formatiert `formatPrice()` jetzt immer deutsch. Als reine Kotlin-Funktion ist sie leicht zu testen.
- **Noch nicht drin:** Bild (Schritt 8), Status-Badge „verkauft“ (Schritt 12) und der FAB zum Erstellen (Schritt 7). Ein FAB ohne Ziel wäre toter Code.

## Was ich auf dem Emulator getestet habe

- Start: 6 Anzeigen, neueste zuerst, Preise als „15,00 €“.
- „Taschenrechner TI-30X“ angetippt → „Anzeige Nr. 3“.
- Gedreht → bleibt auf „Anzeige Nr. 3“. Zurück → Liste im Querformat.
- Flugmodus an, App beendet und neu gestartet → Liste ist da, kein Absturz.
- Logcat: keine Abstürze (`AndroidRuntime:E` leer).

Hinweis: Für den Drehtest habe ich im Emulator „Automatisch drehen“ ausgeschaltet. Bei Bedarf in den Schnelleinstellungen wieder einschalten.

## Testanleitung für euch

- Build und Tests (Windows: `gradlew.bat`), Erwartung `BUILD SUCCESSFUL`:

```bash
./gradlew assembleDebug testDebugUnitTest
```

- **Liste:** App starten, 6 Anzeigen mit Titel, Übergabeort und Preis. Eine antippen → Detailseite mit passender Nummer.
- **Rotation:** In der Liste nach unten scrollen und drehen. Die Liste bleibt da, ohne dass kurz der Spinner erscheint.
- **Flugmodus und App-Neustart:** Liste erscheint wie gewohnt.
- **Leere Liste:** Im Database Inspector alle Zeilen in `ads` löschen → „Noch keine Anzeigen vorhanden.“ erscheint sofort, ohne Neustart (der Flow meldet die Änderung).
- **Previews:** In `AdListScreen.kt` müssen „Ready“ und „Error“ rendern.
- Den Error-Zustand kann man im laufenden Betrieb kaum auslösen. Dafür ist die Error-Preview da.
- Permission verweigert und ungültige Eingaben gibt es noch nicht.

## Commit-Vorschläge

Die `.gitignore`-Änderung (`.idea/`) ist von euch und gehört nicht zu Schritt 4. Am besten getrennt committen:

```
chore: .idea komplett ignorieren
```

(`.gitignore`)

```
feat: Anzeigenliste mit ViewModel, UiState und Preisformat
```

(`ui/adlist/*`, `ui/PriceFormat.kt`, `PriceFormatTest.kt`, gelöschte `ExampleUnitTest.kt`, `AppNavHost.kt`, `strings.xml`, `AI_USAGE.md`)
````

#### Akzeptiert / Geändert / Abgelehnt

- **Akzeptiert:** Ganze Antwort
- **Geändert:** Nichts geändert
- **Abgelehnt:** Nichts

#### Geprüft / Getestet

App gebaut und getestet, keine Fehler

#### Commits

1. .idea komplett ignorieren
2. Anzeigenliste mit ViewModel, UiState und Preisformat

---

### Eintrag 6: Schritt 5 – Suche als reine Funktion mit JUnit-Tests und Suchfeld in der Liste

- **Datum:** 19.11.2026
- **Autor:** Semih
- **Tool:** Claude Code (Desktop-App), Claude Opus 5.5

#### Prompt 1

````text
Ja, Schritt 5
````

#### Ergebnis der KI zu Prompt 1 (vollständig)

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

**Antwort der KI:**

````markdown
Schritt 5 ist fertig: Die Liste hat jetzt ein Suchfeld, und die Suchlogik ist eine reine Kotlin-Funktion mit 10 JUnit-Tests. Build und alle 13 Tests sind grün. Auf dem Emulator habe ich Suche, Drehung, Kreuz, „keine Treffer“ und die Tastatur getestet, alles ohne Absturz.

## Neue und geänderte Dateien

| Datei                                                                                     | Was sie tut                                                                                                                                      |
|-------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------|
| [logic/AdSearch.kt](app/src/main/java/de/dhbw/campusmarket/logic/AdSearch.kt)             | `searchAds(ads, query)`: sucht in Titel, Beschreibung und Übergabeort, ohne Rücksicht auf Groß-/Kleinschreibung. Leerer Suchtext → alle Anzeigen |
| [AdSearchTest.kt](app/src/test/java/de/dhbw/campusmarket/logic/AdSearchTest.kt)           | 10 Tests: leer, nur Leerzeichen, Treffer in jedem Feld, Groß-/Kleinschreibung, Leerzeichen außen, kein Treffer, Reihenfolge, leere Liste         |
| [AdListUiState.kt](app/src/main/java/de/dhbw/campusmarket/ui/adlist/AdListUiState.kt)     | Neues Feld `query` und die berechnete Eigenschaft `visibleAds`                                                                                   |
| [AdListViewModel.kt](app/src/main/java/de/dhbw/campusmarket/ui/adlist/AdListViewModel.kt) | `onQueryChange(query)` schreibt den Suchtext in den State                                                                                        |
| [AdListScreen.kt](app/src/main/java/de/dhbw/campusmarket/ui/adlist/AdListScreen.kt)       | Suchfeld mit Lupe und Kreuz, Hinweis „Keine Treffer für …“, neue Preview dafür                                                                   |
| `ic_search.xml`, `ic_close.xml`                                                           | Lupe und Kreuz als Vector-Drawables                                                                                                              |

## Warum so

- **Eigenes Paket `logic`:** Hier landen auch Filter (Schritt 13) und Sortierung (Schritt 14). Die Dateien darin importieren nichts von Android, deshalb laufen die Tests ohne Emulator in wenigen Millisekunden.
- **`visibleAds` wird berechnet, nicht gespeichert:** Im State liegen nur die Anzeigen aus der Datenbank (`ads`) und der Suchtext (`query`). Die Trefferliste ergibt sich daraus mit `get() = searchAds(ads, query)`. So können die beiden Listen nie auseinanderlaufen. Wird z. B. eine Anzeige gelöscht, stimmt die Trefferliste sofort. Bei ein paar Dutzend Anzeigen kostet das Neuberechnen nichts Spürbares.
- **Suchtext im ViewModel:** Er liegt im `UiState` und überlebt deshalb eine Drehung, wie es CLAUDE.md verlangt. Das Composable hält keinen eigenen Text-State.
- **`trim()`:** Wer aus Versehen ein Leerzeichen vor oder nach dem Wort tippt, bekommt trotzdem Treffer. Nur Leerzeichen zählen als leere Suche.
- **Zwei verschiedene „leer“-Hinweise:** Ist die Datenbank leer, steht wie bisher „Noch keine Anzeigen vorhanden.“ ohne Suchfeld. Gibt es Anzeigen, aber keine passt, bleibt das Suchfeld sichtbar und darunter steht „Keine Treffer für „…“.“. So kann man die Suche direkt ändern.
- **`EmptyContent` heißt jetzt `MessageContent(text)`:** Dieselbe zentrierte Textanzeige wird für beide Hinweise benutzt.
- **Kreuz nur bei Text, Lupe auf der Tastatur:** Das Kreuz leert das Feld über `onQueryChange("")`. `ImeAction.Search` zeigt auf der Tastatur eine Lupe, die nur die Tastatur schließt, weil schon beim Tippen gesucht wird. `LocalFocusManager` ist reiner UI-Zustand und gehört deshalb ins Composable, nicht in den ViewModel.

## Ein Stolperstein beim Testen

Beim ersten Versuch stand im Feld nur „RECH“ statt „RECHNER“, und nach der Drehung kam die nächste Eingabe nicht an. Ursache war nicht die App: Gboard hatte im Emulator seine Stylus-Einführung („Try out your stylus“) über die App gelegt und die Eingaben abgefangen. Nach dem Schließen kam „RECHNER“ vollständig an. Falls euch das im Emulator auch passiert: unten auf „Cancel“ tippen.

## Was ich auf dem Emulator getestet habe

- „RECHNER“ → nur „Taschenrechner TI-30X“ (Groß-/Kleinschreibung egal).
- Ins Querformat und zurück gedreht → Suchtext und Treffer bleiben.
- Kreuz → Feld leer, alle 6 Anzeigen.
- „Fahrrad“ → „Keine Treffer für „Fahrrad“.“
- Nur drei Leerzeichen → alle 6 Anzeigen, kein Absturz.
- Lupe auf der Tastatur → Tastatur schließt sich.
- Flugmodus habe ich diesmal nicht extra getestet. Die Suche läuft nur lokal im Speicher.

## Testanleitung für euch

- Build und Tests (Windows: `gradlew.bat`), Erwartung `BUILD SUCCESSFUL`:

```bash
./gradlew assembleDebug testDebugUnitTest
```

- Die Tests einzeln: In Android Studio `AdSearchTest.kt` öffnen und auf den grünen Pfeil neben `class AdSearchTest` klicken.
- **Suche:** „bibliothek“ eingeben → „Analysis 1 – Lehrbuch“ und „Programmieren in Kotlin“ (Treffer im Übergabeort).
- **Rotation:** Mit Suchtext drehen → Text und Treffer bleiben.
- **Ungültige Eingaben:** nur Leerzeichen, Sonderzeichen wie `%` oder `"` → kein Absturz.
- **Treffer antippen:** öffnet weiter die richtige Detailseite. Nach dem Zurück steht der Suchtext noch da, weil der ViewModel auf dem Back-Stack weiterlebt.
- **App-Neustart:** Das Suchfeld ist wieder leer. Das ist gewollt: Der Suchtext ist kein gespeicherter Wert, nur die Anzeigen liegen in Room.
- **Previews:** In `AdListScreen.kt` rendern „Ready“, „NoResults“ und „Error“.
- Permission verweigert gibt es noch nicht.

## Commit-Vorschlag

```
feat: Suche in der Anzeigenliste mit JUnit-Tests
```

(`logic/AdSearch.kt`, `AdSearchTest.kt`, `ui/adlist/*`, `ic_search.xml`, `ic_close.xml`, `strings.xml`, `AI_USAGE.md`)
````

#### Akzeptiert / Geändert / Abgelehnt

- **Akzeptiert:** alles
- **Geändert:** nichts
- **Abgelehnt:** nichts

#### Geprüft / Getestet

App gebaut, Suchfilter getestet

#### Commits

Suche in der Anzeigenliste mit JUnit-Tests

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

#### Ergebnis der KI zu Prompt 1 und 2 (vollständig)

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

**Antwort der KI:**

````markdown
Schritt 6 ist fertig: Die Detailseite zeigt die echte Anzeige aus Room, und Löschen geht nur über einen Bestätigungsdialog. Auf dem Emulator habe ich Detail, Dialog, Drehung, Abbrechen, Löschen und Neustart getestet, alles ohne Absturz. Zum Paketnamen steht unten eine eigene Antwort.

## Neue und geänderte Dateien (`app/src/main/java/com/example/campusmarket/`)

| Datei                                                                                                       | Was sie tut                                                                                                                                                                                  |
|-------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [ui/addetail/AdDetailUiState.kt](app/src/main/java/de/dhbw/campusmarket/ui/addetail/AdDetailUiState.kt)     | `isLoading`, `hasError`, `ad`, dazu `showDeleteDialog` und `isDeleted`                                                                                                                       |
| [ui/addetail/AdDetailViewModel.kt](app/src/main/java/de/dhbw/campusmarket/ui/addetail/AdDetailViewModel.kt) | Beobachtet `repository.getAd(adId)`, steuert den Dialog und löscht                                                                                                                           |
| [ui/addetail/AdDetailScreen.kt](app/src/main/java/de/dhbw/campusmarket/ui/addetail/AdDetailScreen.kt)       | `AdDetailRoute` + stateless `AdDetailScreen`: Titel, Preis, Kategorie, Zustand, Übergabeort, Status, Beschreibung, Mülleimer in der `TopAppBar`, Lösch-Dialog. Previews „Ready“ und „Dialog“ |
| [ui/StateContent.kt](app/src/main/java/de/dhbw/campusmarket/ui/StateContent.kt)                             | `LoadingContent`, `ErrorContent`, `MessageContent` für beide Screens. Vorher standen sie privat in `AdListScreen.kt`                                                                         |
| [ui/Labels.kt](app/src/main/java/de/dhbw/campusmarket/ui/Labels.kt)                                         | `Category.BOOKS` → „Bücher“, `Condition.LIKE_NEW` → „Wie neu“ usw.                                                                                                                           |

Außerdem: `ic_delete.xml` (Mülleimer), neue Texte in `strings.xml` (der Platzhalter „Anzeige Nr. …“ ist raus), `AppNavHost.kt` ruft `AdDetailRoute` auf.

## Warum so

- **Nur die id kommt an:** `AdDetailViewModel.factory(adId)` baut den ViewModel mit der id aus der Navigation. Weil der ViewModel am Back-Stack-Eintrag hängt, wird er bei einer Drehung nicht neu gebaut.
- **Vier Fälle im Screen:** lädt → Spinner, Fehler → Text + „Erneut versuchen“, `ad == null` → „Diese Anzeige gibt es nicht mehr.“, sonst die Anzeige. Der dritte Fall greift, wenn die id nicht (mehr) in der Datenbank ist.
- **Dialog-Zustand im ViewModel:** `showDeleteDialog` liegt im UiState. Darum ist der Dialog nach einer Drehung noch offen. Ohne das würde er verschwinden, und man wüsste nicht, ob gelöscht wurde.
- **Erst Beobachten stoppen, dann löschen:** Der Flow aus Room würde nach dem Löschen `null` schicken, und der Screen würde kurz „gibt es nicht mehr“ zeigen. Deshalb ruft `onDeleteConfirm()` vorher `loadJob?.cancel()` auf.
- **Zurück nach dem Löschen:** Der ViewModel navigiert nicht selbst. Er setzt nur `isDeleted = true`. Die Route sieht das in einem `LaunchedEffect` und ruft `onBack()` auf, der NavHost macht dann `navigateUp()`. So bleibt die Regel „Nur der NavHost navigiert“ erfüllt.
- **`try/catch` beim Löschen:** Schlägt das Löschen fehl, zeigt der Screen den Fehlerzustand, statt abzustürzen.
- **Mülleimer nur bei geladener Anzeige:** Beim Laden oder bei „gibt es nicht mehr“ gibt es nichts zu löschen.
- **`Labels.kt` mit `when` ohne `else`:** Kommt später eine neue Kategorie dazu, meldet der Compiler sofort, dass ihr Text fehlt. Die Funktionen nutzen wir auch für Filter (Schritt 13) und das Formular (Schritt 7).
- **Status schon sichtbar:** `isSold` gibt es in der Datenbank seit Schritt 2. Umschalten kommt erst in Schritt 12, anzeigen kostet nur eine Zeile.
- **Bild fehlt noch:** Das kommt mit Kamera und Coil in Schritt 8.

## Was ich auf dem Emulator getestet habe

- „Taschenrechner TI-30X“ öffnen → alle Felder korrekt, Kategorie und Zustand auf Deutsch.
- Mülleimer → Dialog „Anzeige löschen?“ mit dem Titel.
- Mit offenem Dialog drehen → Dialog bleibt offen.
- „Abbrechen“ → Anzeige bleibt.
- „Löschen“ → zurück zur Liste, 5 statt 6 Anzeigen.
- App beenden und neu starten → weiter 5 Anzeigen.
- Logcat: kein Absturz.

Auf dem Emulator fehlt jetzt der Taschenrechner. Um die 6 Beispielanzeigen zurückzubekommen: Einstellungen → Apps → CampusMarket → Speicher → Speicher leeren.

## Testanleitung für euch

- Build und Tests (Windows: `gradlew.bat`):

```bash
./gradlew assembleDebug testDebugUnitTest
```

- **Detail:** „Programmieren in Kotlin“ öffnen → Status „Verkauft“.
- **Rotation:** auf der Detailseite und mit offenem Dialog drehen.
- **Abbrechen / außerhalb tippen / System-Zurück** bei offenem Dialog → Dialog zu, nichts gelöscht.
- **Löschen** → Liste ohne die Anzeige. Mit aktiver Suche: Die Treffer aktualisieren sich ebenfalls.
- **App-Neustart:** gelöschte Anzeige bleibt weg.
- **Flugmodus:** kein Unterschied, alles lokal.
- **Previews** in `AdDetailScreen.kt`: „Ready“ und „DeleteDialog“.

## Commit-Vorschlag

```
feat: Detailansicht mit Löschen und Bestätigungsdialog
```

(`ui/addetail/*`, `ui/StateContent.kt`, `ui/Labels.kt`, `ui/adlist/AdListScreen.kt`, `ui/navigation/AppNavHost.kt`, `ic_delete.xml`, `strings.xml`, `AI_USAGE.md`)

## Eure Frage: Kann man `com.example.campusmarket` einfach ändern?

Ja, aber nicht „einfach“ mit einer Zeile. Es sind zwei Dinge, die gerade zufällig gleich heißen:

|                               | Wo                                                                                              | Wofür                                                 |
|-------------------------------|-------------------------------------------------------------------------------------------------|-------------------------------------------------------|
| **applicationId**             | `app/build.gradle.kts`                                                                          | Die Kennung der App auf dem Gerät (und im Play Store) |
| **namespace + Kotlin-Pakete** | `app/build.gradle.kts`, `package …` in jeder `.kt`-Datei, die Ordner `com/example/campusmarket` | Wo der Code liegt und wie die `R`-Klasse heißt        |

**Muss man es ändern?** Für die Abgabe nicht, die App baut und läuft so. `com.example` ist aber nur ein Platzhalter von Android Studio (Google Play lehnt es sogar ab), und ein eigener Name wirkt sauberer. Üblich ist eine umgedrehte Domain, z. B. `de.dhbw.campusmarket`.

**So geht es in Android Studio:**

1. Im Projektfenster oben auf die Ansicht „Project“ (nicht „Android“) umstellen.
2. Unter `app/src/main/java` den Ordner `com.example.campusmarket` aufklappen, `example` markieren → Rechtsklick → **Refactor → Rename** (Shift+F6) → „Rename package“ wählen → neuen Namen eingeben. Für `com` dasselbe, falls auch das sich ändern soll. Android Studio passt alle `package`- und `import`-Zeilen und die Ordner an, auch unter `src/test` und `src/androidTest`.
3. In `app/build.gradle.kts` `namespace` und `applicationId` von Hand ändern.
4. „Sync Project with Gradle Files“, dann Build → Clean, danach neu bauen und die Tests laufen lassen.
5. `CLAUDE.md` (Projektdaten) anpassen.

**Was man wissen muss:**

- Eine neue `applicationId` ist für Android eine **andere App**. Die alte bleibt auf dem Emulator installiert und sollte deinstalliert werden. Die neue startet mit leerer Datenbank, also wieder mit den 6 Beispielanzeigen.
- `ExampleInstrumentedTest` prüft fest `"com.example.campusmarket"` und würde danach fehlschlagen. Er ist ohnehin noch Beispielcode und sollte bei der Gelegenheit raus.
- Fast jede Datei ändert sich. Das gehört in einen **eigenen Commit**, getrennt von Schritt 6. Je früher, desto weniger Dateien sind betroffen. Git erkennt die verschobenen Dateien als Umbenennungen, die Historie geht nicht verloren.

Ich habe noch nichts daran geändert. Wenn ihr wollt, mache ich das als eigenen Schritt, sobald Schritt 6 committet ist. Dafür brauche ich nur den gewünschten Namen.
````

#### Akzeptiert / Geändert / Abgelehnt

- **Akzeptiert:** alles
- **Geändert:** nichts
- **Abgelehnt:** nichts

#### Geprüft / Getestet

App gebaut, Löschfunktion getestet

#### Commits

Detailansicht mit Löschen und Bestätigungsdialog

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

#### Ergebnis der KI zu Prompt 1 (vollständig)

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

**Antwort der KI:** Die Antwort auf diesen Prompt (Paketname, Prozentfrage und Schritt 7) steht vollständig in Eintrag 9, weil eine einzige Antwort alle drei Teile abdeckt.

#### Akzeptiert / Geändert / Abgelehnt

- **Akzeptiert:** eintragen
- **Geändert:** eintragen
- **Abgelehnt:** eintragen

#### Geprüft / Getestet

eintragen

#### Commits

eintragen

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

#### Ergebnis der KI zu Prompt 1 und 2 (vollständig)

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

**Antwort der KI:**

````markdown
Paketname und Schritt 7 sind fertig. Die Umbenennung habt ihr schon committet (`db9f6a0`), Schritt 7 ist noch offen. Build und alle 24 Tests laufen durch, und auf dem Emulator lief alles ohne Absturz.

## Teil 1: Paketname `de.dhbw.campusmarket`

- Alle Ordner liegen jetzt unter `de/dhbw/campusmarket`, alle `package`/`import`-Zeilen und `namespace`/`applicationId` sind angepasst.
- `ExampleInstrumentedTest` ist gelöscht. Damit hatten die Bibliotheken für Instrumented Tests (Espresso, `androidx.test.ext:junit`, Compose-UI-Test) keine Aufgabe mehr. Ich habe sie aus `app/build.gradle.kts` und `libs.versions.toml` entfernt, wie CLAUDE.md es verlangt („Keine Bibliothek ohne Aufgabe“).
- CLAUDE.md nennt jetzt den neuen Paketnamen.
- Auf dem Emulator habe ich die alte App `com.example.campusmarket` deinstalliert. Die neue startet mit frischer Datenbank und den 6 Beispielanzeigen.
- Ältere Logbuch-Einträge verlinken noch auf `com/example/...`. Das bleibt so, weil sie den damaligen Stand zeigen.

## Teil 2: Schritt 7 – Anzeige erstellen

| Datei (`app/src/main/java/de/dhbw/campusmarket/`)                                                   | Was sie tut                                                                                                                                     |
|-----------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------|
| [logic/AdValidation.kt](app/src/main/java/de/dhbw/campusmarket/logic/AdValidation.kt)               | `isValidTitle` (1–60 Zeichen), `isValidLocation`, `parsePrice` („12,50“ → 12.5, Ungültiges → `null`)                                            |
| [AdValidationTest.kt](app/src/test/java/de/dhbw/campusmarket/logic/AdValidationTest.kt)             | 11 Tests, u. a. leer, nur Leerzeichen, genau 60 / 61 Zeichen, „-5“, „1e3“, „NaN“, „12,505“, „123456“                                            |
| [ui/adform/AdFormUiState.kt](app/src/main/java/de/dhbw/campusmarket/ui/adform/AdFormUiState.kt)     | Alle Eingaben als Text, `showErrors`, `isSaving`, `saveFailed`, `isSaved`; Fehler werden aus den Eingaben berechnet                             |
| [ui/adform/AdFormViewModel.kt](app/src/main/java/de/dhbw/campusmarket/ui/adform/AdFormViewModel.kt) | Eine `on…Change`-Funktion pro Feld, `onSaveClick()` prüft und speichert                                                                         |
| [ui/adform/AdFormScreen.kt](app/src/main/java/de/dhbw/campusmarket/ui/adform/AdFormScreen.kt)       | Route + stateless Screen: Titel, Preis, Übergabeort, Beschreibung, Kategorie- und Zustand-Chips, „Speichern“. Previews „leer“ und „mit Fehlern“ |

Außerdem: FAB (+) unten rechts in der Liste, neues Ziel `AdForm` im NavHost, `ic_add.xml`, 9 neue Texte in `strings.xml`.

### Warum so

- **Eingaben im ViewModel:** Jeder Tastendruck geht über `onTitleChange` usw. in den `UiState`. Darum überlebt alles eine Drehung, auch die gewählten Chips und die Fehlertexte.
- **Preis bleibt im State ein Text:** Sonst könnte man „12,“ nicht tippen, weil das kein gültiger `Double` ist. Umgewandelt wird erst beim Speichern mit `parsePrice()`.
- **Muster statt `toDoubleOrNull()`:** `"1e3".toDoubleOrNull()` ergibt 1000.0, `"NaN"` und `"-5"` gehen auch durch. Das Muster `\d{1,5}([.,]\d{1,2})?` lässt nur Zahlen wie „5“, „12,5“, „12.50“ bis 99999,99 zu. Komma und Punkt gehen beide.
- **Fehler erst nach dem ersten „Speichern“:** Ein leeres Formular soll beim Öffnen nicht rot sein. `titleError` usw. sind berechnete Eigenschaften (`showErrors && !isValidTitle(title)`), so verschwindet ein Fehler, sobald man ihn korrigiert.
- **Prüfung als reine Funktionen in `logic/`:** Dieselben Funktionen nutzen `UiState` (Anzeige) und `ViewModel` (Speichern). Sie sind ohne Android testbar.
- **Doppelklick:** `onSaveClick()` bricht ab, solange `isSaving` true ist, und der Button ist dann deaktiviert. So entstehen keine doppelten Anzeigen.
- **Nach dem Speichern zurück:** wie beim Löschen. Der ViewModel setzt `isSaved`, die Route ruft `onSaved()`, navigiert wird im NavHost. In der Liste steht die neue Anzeige oben, weil das DAO nach `createdAt` absteigend sortiert.
- **Chips statt Dropdown:** Alle Möglichkeiten sind auf einen Blick sichtbar, und es gibt keinen zusätzlichen „Menü offen“-Zustand. `ChipGroup<T>` ist eine generische Funktion, damit Kategorie und Zustand denselben Code nutzen.
- **`imePadding()` + `verticalScroll`:** Bei offener Tastatur kann man bis zum Button scrollen.
- **Zwei kleine Korrekturen beim Testen:** Die Detailseite blendet „Beschreibung“ aus, wenn keine da ist (sie ist im Formular optional). Die Liste hat unten 88 dp Platz, damit der FAB die letzte Anzeige nicht verdeckt.
- **Noch nicht drin:** Foto (Schritt 8) und Bearbeiten (Schritt 10, dafür wird dieses Formular wiederverwendet). Eine Rückfrage „Eingaben verwerfen?“ beim Zurück-Pfeil gibt es nicht. Das wäre eine Idee für den Feinschliff.

### Was ich auf dem Emulator getestet habe

- FAB → „Neue Anzeige“.
- Leer speichern → drei Fehler, nichts gespeichert.
- Preis „1e3“ → nur der Preisfehler.
- Querformat → alle Eingaben, Chips „Skripte“ / „Wie neu“ und der Fehler bleiben.
- Preis „7,50“ → gespeichert, Anzeige steht oben in der Liste.
- App-Neustart → Anzeige noch da, Detail zeigt „Skripte“ und „Wie neu“.
- Logcat: kein Absturz.

Hinweis: „Automatisch drehen“ war im Emulator wieder an. Für den Test habe ich es ausgeschaltet.

### Testanleitung für euch

- Build und Tests (Windows: `gradlew.bat`):

```bash
./gradlew assembleDebug testDebugUnitTest
```

- **Ungültige Eingaben:** leeres Formular, Titel nur aus Leerzeichen, 61 Zeichen Titel, Preis „-5“, „abc“, „12,505“, „100000“ → jeweils Fehler, kein Absturz.
- **Gültig:** Preis „0“ (verschenken), „12.5“, „12,50“.
- **Rotation:** mitten im Ausfüllen und mit sichtbaren Fehlern.
- **Zurück-Pfeil ohne Speichern:** Es wird nichts angelegt.
- **Doppelt auf „Speichern“ tippen:** nur eine neue Anzeige.
- **App-Neustart und Flugmodus:** Anzeige bleibt, kein Absturz.
- **Previews** in `AdFormScreen.kt`: „Empty“ und „Errors“.
- Permission verweigert gibt es erst mit der Kamera in Schritt 8.

### Commit-Vorschlag

```
feat: Anzeige erstellen mit Formular und Validierung
```

(`logic/AdValidation.kt`, `AdValidationTest.kt`, `ui/adform/*`, `ui/adlist/AdListScreen.kt`, `ui/addetail/AdDetailScreen.kt`, `ui/navigation/*`, `ic_add.xml`, `strings.xml`, `AI_USAGE.md`)

## Teil 3: Wie viel ist fertig?

Grob geschätzt **etwa 45 % des ganzen Projekts** und **etwa 75 % der Mindestanforderungen**. Die Zahl ist eine Schätzung nach Aufwand, nicht nach Anzahl der Schritte, weil die Schritte unterschiedlich groß sind:

| Bereich                                                                         | Stand                                                                       |
|---------------------------------------------------------------------------------|-----------------------------------------------------------------------------|
| Schritte 1–7 (Setup, Room, Navigation, Liste, Suche, Detail/Löschen, Erstellen) | ✅ fertig                                                                   |
| Schritt 8: Kamera mit Permission und FileProvider                               | offen, **größter einzelner Brocken**                                        |
| Schritt 9: README                                                               | offen, klein                                                                |
| Schritte 10–15: Bearbeiten, Favoriten, Status, Filter, Sortierung, Galerie      | offen, mittel; vieles baut auf Vorhandenem auf (Formular, Labels, `logic/`) |
| Feinschliff, Reflexion in AI_USAGE.md                                           | offen                                                                       |

Die 5 Mindestanforderungen im Einzelnen:

1. Baut aus sauberem Checkout → baut ✅, aber das **README fehlt** noch.
2. Zwei Screens mit Navigation, State überlebt Drehung → ✅ (sogar drei).
3. Echte Daten aus Room → ✅
4. Gerätefunktion hinter Permission → ❌ **Kamera fehlt** (Schritt 8).
5. Flugmodus ohne Absturz → ✅ bisher.

Bis zur Abgabe am 08.12. sind es noch 16 Tage. Wenn Schritt 8 und 9 als Nächstes kommen, sind alle Mindestanforderungen erfüllt, und der Rest ist „nur noch“ Bewertungspunkte.
````

#### Akzeptiert / Geändert / Abgelehnt

- **Akzeptiert:** Alles
- **Geändert:** Nichts
- **Abgelehnt:** Nichts

#### Geprüft / Getestet

Neue Funktionen getestet, keine Fehler

#### Commits

Anzeige erstellen mit Formular und Validierung

---

### Eintrag 10: Schritt 8 – Produktfoto mit der Kamera (Permission, FileProvider, Coil)

- **Datum:** 23.11.2026
- **Autor:** Benjamin
- **Tool:** Claude Code (Desktop-App), Claude Opus 5.5

#### Prompt 1

````text
Ja, Schritt 8
````

#### Ergebnis der KI zu Prompt 1 (vollständig)

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

**Antwort der KI:**

````markdown
Schritt 8 ist gebaut, aber **nur halb getestet**. Der Build läuft durch. Auf dem Emulator habe ich den Weg „Berechtigung verweigert“ vollständig geprüft. Die eigentliche Foto-Aufnahme konnte ich nicht testen: Der Emulator ist beim Öffnen der Kamera abgestürzt, und beim Neustart gleich noch einmal. Bitte testet die Aufnahme selbst, bevor ihr committet (Anleitung unten).

## Neue und geänderte Dateien

| Datei                                                                                                                                                                             | Was sie tut                                                                                                                                       |
|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------|
| [AndroidManifest.xml](app/src/main/AndroidManifest.xml)                                                                                                                           | `CAMERA`-Berechtigung, `uses-feature` mit `required="false"`, `FileProvider`                                                                      |
| [res/xml/file_paths.xml](app/src/main/res/xml/file_paths.xml)                                                                                                                     | Gibt nur den Ordner `photos` im internen Speicher frei                                                                                            |
| [ui/adform/PhotoFile.kt](app/src/main/java/de/dhbw/campusmarket/ui/adform/PhotoFile.kt)                                                                                           | `createPhotoUri()`: legt `photos/photo_<Zeit>.jpg` an und liefert eine `content://`-Uri                                                           |
| [ui/AdImage.kt](app/src/main/java/de/dhbw/campusmarket/ui/AdImage.kt)                                                                                                             | Foto mit Coil (`AsyncImage`), dahinter ein grauer Platzhalter mit Bild-Symbol                                                                     |
| [ui/adform/AdFormScreen.kt](app/src/main/java/de/dhbw/campusmarket/ui/adform/AdFormScreen.kt)                                                                                     | Route: die zwei Launcher (`RequestPermission`, `TakePicture`). Screen: Foto-Vorschau, „Foto aufnehmen“ / „Neues Foto“, „Foto entfernen“, Hinweise |
| [AdFormUiState.kt](app/src/main/java/de/dhbw/campusmarket/ui/adform/AdFormUiState.kt) / [AdFormViewModel.kt](app/src/main/java/de/dhbw/campusmarket/ui/adform/AdFormViewModel.kt) | `imageUri`, `pendingPhotoUri`, die zwei Hinweise und die passenden `on…`-Funktionen                                                               |
| `AdListScreen.kt`, `AdDetailScreen.kt`                                                                                                                                            | Vorschaubild (56 dp) in der Liste, großes Foto oben im Detail                                                                                     |

## So läuft es ab

1. „Foto aufnehmen“ → `permissionLauncher.launch(CAMERA)`. Ist die Berechtigung schon erteilt, kommt sofort `true` zurück, ohne Dialog.
2. **Erlaubt:** `createPhotoUri()` legt eine leere Datei an, der ViewModel merkt sich die Uri als `pendingPhotoUri`, dann startet `TakePicture` die Kamera-App mit dieser Uri.
3. **Kamera fertig:** `onPhotoResult(true)` → `imageUri = pendingPhotoUri`. Bei Abbruch (`false`) bleibt das alte Foto.
4. **Abgelehnt:** `onCameraPermissionDenied()` → roter Hinweis. „Speichern“ funktioniert weiter, nur eben ohne Foto.
5. **Speichern:** In Room landet nur die Uri als Text (`imageUri`), nicht das Bild selbst.

## Warum so

- **Launcher in der Route, nicht im Screen:** Sie brauchen Android (`Context`, Activity-Ergebnisse). Der Screen bleibt stateless und bekommt nur `onTakePhotoClick`, deshalb funktionieren die Previews weiter.
- **`pendingPhotoUri` im ViewModel:** Während die Kamera-App offen ist, kann unsere Activity neu erzeugt werden (z. B. weil man in der Kamera dreht). Läge die Uri in einer lokalen Variable im Composable, wäre sie danach weg und das Foto verloren.
- **Interner Speicher (`filesDir/photos`):** Andere Apps sehen die Fotos nicht, und wir brauchen keine Speicher-Berechtigung. Der `FileProvider` gibt der Kamera-App nur für diese eine Datei kurz Schreibrechte.
- **`content://` statt Dateipfad:** Seit Android 7 darf man anderen Apps keine `file://`-Pfade mehr geben, sonst stürzt die App ab (`FileUriExposedException`).
- **`try/catch (ActivityNotFoundException)`:** Hat ein Gerät keine Kamera-App, erscheint ein Hinweis statt eines Absturzes. `uses-feature … required="false"` sorgt dafür, dass sich die App auch auf solchen Geräten installieren lässt.
- **Platzhalter hinter dem Foto:** Gibt es kein Foto oder lässt es sich nicht laden (Datei gelöscht), sieht man das graue Bild-Symbol. So braucht es keine Fehlerbehandlung in Coil.
- **Hinweis auch nach „nicht mehr fragen“:** Nach zweimaligem Ablehnen zeigt Android keinen Dialog mehr, der Launcher liefert sofort `false`. Der Hinweis nennt deshalb die App-Einstellungen.

## Bekannte Grenze

Fotos, die nicht mehr gebraucht werden, bleiben als Datei im internen Speicher liegen: nach „Neues Foto“, „Foto entfernen“, Abbrechen des Formulars oder Löschen der Anzeige. Bei ein paar Fotos stört das nicht. Ein Aufräumen beim Löschen wäre eine Idee für den Feinschliff.

## Was ich getestet habe

- ✅ Build und alle 24 Unit-Tests.
- ✅ „Foto aufnehmen“ ohne Berechtigung → System-Dialog erscheint.
- ✅ „Don’t allow“ → roter Hinweis, Formular bleibt bedienbar.
- ✅ Nach zweimal „Ablehnen“: erneuter Tipp → kein Dialog, Hinweis, kein Absturz.
- ✅ Speichern ohne Foto → Anzeige in der Liste mit Platzhalter-Bild.
- ❌ **Nicht getestet:** „While using the app“ → Kamera → Foto. Direkt danach war der Emulator weg. Die AVD nutzt für die Rückkamera `virtualscene` (eine 3D-Szene), und die stürzt auf manchen Rechnern ab. Ein Neustart mit `-camera-back emulated` ist ebenfalls abgestürzt (Absturzbericht-Dialog vom Emulator). Ob das am Rechner, an der Grafik oder am Emulator liegt, konnte ich nicht klären.

## Bitte selbst testen, bevor ihr committet

Am besten auf einem **echten Handy** (USB-Debugging an, in Android Studio auswählen, ▶). Alternativ im Emulator: Device Manager → ⋮ beim Gerät → **Edit** → Advanced Settings → Back camera auf **Emulated** stellen → **Cold Boot Now**.

1. Neue Anzeige → „Foto aufnehmen“ → „Bei Nutzung der App zulassen“ → Kamera öffnet sich → Foto machen → bestätigen (Haken).
2. Erwartung: Das Foto erscheint oben im Formular, der Knopf heißt jetzt „Neues Foto“, daneben „Foto entfernen“.
3. **In der Kamera-App drehen**, dann bestätigen → das Foto muss trotzdem ankommen.
4. In der Kamera auf Abbrechen/Zurück → das vorherige Foto (oder der Platzhalter) bleibt.
5. Speichern → Vorschaubild links in der Liste, großes Foto im Detail.
6. **App-Neustart** → Foto noch da.
7. **Permission verweigert:** In den App-Einstellungen die Kamera auf „Nicht zulassen“ → Hinweis, Speichern ohne Foto geht.
8. **Flugmodus:** alles wie vorher, die Kamera braucht kein Netz.

Wenn dabei etwas schiefgeht: Logcat-Ausgabe (Filter `AndroidRuntime`) oder einen Screenshot schicken, dann behebe ich es.

## Commit-Vorschlag (erst nach eurem Test)

```
feat: Produktfoto mit Kamera, Permission und FileProvider
```

(`AndroidManifest.xml`, `res/xml/file_paths.xml`, `ic_image.xml`, `ui/AdImage.kt`, `ui/adform/*`, `ui/adlist/AdListScreen.kt`, `ui/addetail/AdDetailScreen.kt`, `strings.xml`, `AI_USAGE.md`)
````

#### Prompt 2

````text
Foto scheint soweit zu funktionieren. Im Emulator kommt bei der Fotoaufnahme ein grünes Beispielbild. Es ist allerdings noch nicht möglich, sich die Bilder im Großformat anzugucken. Das sollte später auf jeden Fall möglich sein. Außerdem kommt man bei der Fotoaufnahme nicht mehr zurück, ohne vorher ein Bild zu machen.
````

#### Ergebnis der KI zu Prompt 2 (vollständig)

**Ausgeführte Aktionen:**

1. `git status`, `git log --oneline -2`, Überschriften in `AI_USAGE.md` gelesen, `adb devices` ausgeführt. Ergebnis: Schritt 8 noch nicht committet, Emulator `emulator-5554` läuft wieder.
2. `AI_USAGE.md`: Prompt 2 in Eintrag 10 eingetragen.
3. APK installiert, `dumpsys package` → `CAMERA: granted=true`. Formular → „Foto aufnehmen“ → Kamera-App `com.android.camera2/com.android.camera.CaptureActivity` geöffnet, Screenshot: Testbild (grünes Haus), nur ein Auslöser und ein „…“-Knopf, **kein eigener Abbrechen-Knopf**.
4. `input keyevent KEYCODE_BACK` → oberste Activity wieder `de.dhbw.campusmarket/.MainActivity`. `settings get secure navigation_mode` → `2` (Gesten-Navigation).
5. Kamera erneut geöffnet, Wischgeste vom linken Rand (`input swipe 2 1200 500 1200`) → wieder `MainActivity`, Formular mit Platzhalter, „Foto aufnehmen“. Kein Absturz.
6. Kein Code geändert.
7. `AI_USAGE.md`: dieses Ergebnis eingetragen.

**Kurzfazit im Antwortfeld der App:** „Schön, dass die Aufnahme klappt. Das Zurück aus der Kamera konnte ich nicht als App-Fehler nachstellen: Zurück-Taste und Wischgeste führen beide ins Formular zurück, die Kamera-App des Emulators hat nur keinen eigenen Abbrechen-Knopf. Die Vollbild-Ansicht plane ich als eigenen Schritt ein. Am Code ist nichts geändert, nichts committet.“ Frage: „Soll ich nach eurem Commit mit Schritt 9 (README) weitermachen?“

**Antwort der KI:**

````markdown
Gut, dass die Aufnahme funktioniert. Das grüne Bild ist das Testbild der Emulator-Kamera, auf einem echten Handy kommt dort das echte Foto an.

## „Man kommt nicht zurück, ohne ein Bild zu machen“

Das konnte ich nicht als Fehler unserer App nachstellen:

- Die Kamera, die sich öffnet, ist **nicht unsere App**, sondern die Kamera-App des Emulators (`com.android.camera2`). Wir starten sie nur über `TakePicture` und bekommen am Ende „Foto gemacht“ oder „abgebrochen“ zurück. Wie ihre Oberfläche aussieht, können wir nicht beeinflussen.
- Diese Kamera-App hat **keinen eigenen Abbrechen-Knopf**, nur den Auslöser. Abbrechen geht nur über die System-Zurück-Funktion.
- Ich habe beides getestet: die **Zurück-Taste** und die **Wischgeste vom linken Bildschirmrand nach innen** (der Emulator nutzt Gesten-Navigation). In beiden Fällen landet man wieder im Formular, das bisherige Foto bzw. der Platzhalter bleibt, kein Absturz. In der App läuft dann `onPhotoResult(false)`.

Mit der Maus ist die Wischgeste im Emulator fummelig: Man muss **ganz am linken Rand** mit gedrückter Maustaste starten und nach rechts ziehen. Einfacher ist der **◁-Knopf in der Seitenleiste des Emulators** (rechts neben dem Gerät). Auf echten Handys haben die meisten Kamera-Apps zusätzlich ein ✕ zum Abbrechen.

Falls ihr etwas anderes gemeint habt (z. B. dass es auf einem echten Handy nicht geht oder dass nach dem Zurück etwas Falsches angezeigt wird), beschreibt es bitte kurz, dann schaue ich nochmal.

## Fotos im Großformat ansehen

Einverstanden, das gehört in die App. Ich nehme es als **eigenen Schritt** in den Plan auf, damit Schritt 8 ein sauberer Commit bleibt. Mein Vorschlag:

- Foto in der Detailansicht antippen → neuer Screen **„Foto“** mit schwarzem Hintergrund, das Bild ganz sichtbar (nicht abgeschnitten), Zurück-Pfeil oben links.
- Neues Navigationsziel `PhotoViewer(adId)`. Übergeben wird wie immer nur die id, der Screen holt die Uri selbst aus Room (Regel aus CLAUDE.md).
- Optional: mit zwei Fingern zoomen. Das kostet etwas mehr Code. Ich würde es erst ohne Zoom bauen und dann entscheiden.

Weil es klein ist und nichts Neues an Bibliotheken braucht, würde ich es **direkt nach Schritt 9 (README)** einschieben. Dann sind zuerst alle Mindestanforderungen erfüllt.

Aktualisierter Plan:

| Nr.   | Schritt                                                    | Stand                                                  |
|-------|------------------------------------------------------------|--------------------------------------------------------|
| 1–7   | Setup bis Erstellen                                        | ✅ committet                                           |
| 8     | Kamera                                                     | ✅ fertig, von euch getestet, **noch nicht committet** |
| 9     | README                                                     | als Nächstes                                           |
| 9a    | **Foto im Vollbild**                                       | neu                                                    |
| 10–15 | Bearbeiten, Favoriten, Status, Filter, Sortierung, Galerie | offen                                                  |

## Commit

Am Code habe ich diesmal nichts geändert. Schritt 8 kann so committet werden:

```
feat: Produktfoto mit Kamera, Permission und FileProvider
```

(`AndroidManifest.xml`, `res/xml/file_paths.xml`, `ic_image.xml`, `ui/AdImage.kt`, `ui/adform/*`, `ui/adlist/AdListScreen.kt`, `ui/addetail/AdDetailScreen.kt`, `strings.xml`, `AI_USAGE.md`)
````

#### Akzeptiert / Geändert / Abgelehnt

- **Akzeptiert:** Alles
- **Geändert:** Nichts
- **Abgelehnt:** Nichts

#### Geprüft / Getestet

Fotoaufnahme getestet und Edgecases geprüft, keine Fehler

#### Commits

Produktfoto mit Kamera, Permission und FileProvider

---

### Eintrag 11: Warnungen in AI_USAGE.md nach der Paket-Umbenennung

- **Datum:** 23.11.2026
- **Autor:** Benjamin
- **Tool:** Claude Code (Desktop-App), Claude Opus 5.5

#### Prompt 1

````text
Mir ist aufgefallen, dass durch die Änderung von com.exmaple zu de.dhbw in der AI_USAGE.md sehr viele Warnungen aufgetreten sind. Die sind zwar nicht kritisch, aber trotzdem werden sie bei jedem Commit angezeigt
````

#### Ergebnis der KI zu Prompt 1 (vollständig)

**Ausgeführte Aktionen:**

1. `git status`, `git log --oneline -2`, Ende von `AI_USAGE.md` gelesen. Ergebnis: Schritt 8 ist committet (`08036d4`), Arbeitsverzeichnis sauber.
2. Alle relativen Links in `AI_USAGE.md` mit einem Shell-Skript geprüft (existiert das Ziel?). Ergebnis: 29 Links auf 23 verschiedene Dateien unter `app/src/main/java/com/example/…` und `app/src/test/java/com/example/…` zeigen ins Leere; alle anderen Links sind gültig. Der sichtbare Linktext enthält nie `com/example`. Erwähnungen in Backticks (z. B. `com.example.campusmarket`) sind kein Link und erzeugen keine Warnung.
3. `git config core.autocrlf` → `true`, keine `.gitattributes`. `git ls-files --eol` → 55 Dateien haben im Arbeitsverzeichnis LF-Zeilenenden, `AI_USAGE.md` hatte CRLF.
4. `AI_USAGE.md`: Eintrag 11 mit dem Prompt angelegt.
5. Sicherungskopie `AI_USAGE.vorher.md` im Scratchpad der Sitzung angelegt. Dann per `sed -E` nur in Linkzielen `app/src/main/java/com/example/campusmarket/` → `app/src/main/java/de/dhbw/campusmarket/` ersetzt (ebenso für `app/src/test`). Ergebnis: 0 Links mit `com/example`, 0 tote Links. Vergleich von vorher/nachher, bei dem alle Linkziele ausgeblendet sind: kein Unterschied, der sichtbare Text ist also unverändert. `sed` hat dabei die Zeilenenden von `AI_USAGE.md` auf LF umgestellt (wie bei den meisten anderen Dateien).
6. `AI_USAGE.md`: Hinweis zu den Links oben im Dokument ergänzt, dieses Ergebnis eingetragen.

**Kurzfazit im Antwortfeld der App:** „Die Warnungen kamen von 29 toten Links in AI_USAGE.md, die noch auf die alten com/example-Ordner zeigten. Ich habe nur die Linkziele umgestellt, der sichtbare Text ist gleich, und oben steht ein Hinweis dazu. Daneben gibt es noch Git-Hinweise zu Zeilenenden, die ich nicht angefasst habe. Es ist nichts committet.“ Frage: „Soll ich die Zeilenenden mit einer .gitattributes einheitlich machen?“

**Antwort der KI:**

````markdown
Gefunden und behoben: Die Warnungen kamen von **29 toten Links** in `AI_USAGE.md`.

## Ursache

In den Einträgen 3 bis 7 stehen Links mit dem Text `data/Ad.kt` und dem Ziel `app/src/main/java/com/example/campusmarket/data/Ad.kt`. Seit der Umbenennung liegen die Dateien unter `de/dhbw/campusmarket`, die Linkziele zeigten also ins Leere (23 verschiedene Dateien, 29 Links). Android Studio prüft beim Commit auch Markdown-Dateien und meldet jedes nicht auffindbare Linkziel als Warnung. Bei der Umbenennung hatte ich die Links bewusst stehen lassen, weil sie den damaligen Stand zeigen. Die Warnungen hatte ich dabei nicht bedacht.

## Was ich geändert habe

- **Nur die Linkziele**, also der Teil in den runden Klammern, zeigen jetzt auf `de/dhbw/campusmarket`. Danach gibt es keinen toten Link mehr in der Datei.
- **Der sichtbare Text ist unverändert.** Ich habe die Datei vorher und nachher verglichen, mit ausgeblendeten Linkzielen: kein Unterschied. Die Regel „Antworten wortwörtlich“ bleibt also erfüllt, auch Stellen wie `com.example.campusmarket` in Backticks oder in eurem Prompt bleiben so, wie sie waren.
- **Oben in `AI_USAGE.md` steht ein Hinweis**, dass und warum die Linkziele geändert wurden. Damit ist es für den Prüfer nachvollziehbar.

## Mögliche zweite Quelle: Zeilenenden

Falls ihr beim Commit zusätzlich Meldungen wie `LF will be replaced by CRLF` seht: Die kommen nicht von der Umbenennung, sondern von Git-Einstellungen. Bei euch ist `core.autocrlf=true` (Windows-Standard), aber 55 Dateien haben LF-Zeilenenden (meist die, die ich geschrieben habe). Git meldet bei jeder solchen Datei, dass es sie beim nächsten Mal umwandelt. Im Repository selbst ist alles einheitlich LF, es ist also nur ein Hinweis.

Beheben ließe sich das mit einer Datei `.gitattributes` im Root:

```
* text=auto eol=lf
```

Dann gilt für alle Dateien LF, egal wie Git auf dem jeweiligen Rechner eingestellt ist, und die Hinweise verschwinden. Android Studio kommt mit LF problemlos zurecht. Das ist eine Einstellung für das ganze Repo, deshalb habe ich sie nicht ohne euch angelegt.

## Testen

- In Android Studio `AI_USAGE.md` öffnen: Die Links sind nicht mehr unterstrichen bzw. markiert. Ein Klick auf z. B. `data/Ad.kt` in Eintrag 3 öffnet die Datei.
- Beim Commit (mit „Analyze code“) sollten keine Warnungen zu `AI_USAGE.md` mehr kommen.

## Commit-Vorschlag

```
docs: tote Links in AI_USAGE.md nach Paket-Umbenennung korrigiert
```

(`AI_USAGE.md`)
````

#### Prompt 2

Antwort auf die Frage der KI im Antwortfeld der App („Soll ich die Zeilenenden mit einer .gitattributes einheitlich machen?“, Buttons „Ja, anlegen“ / „Nein, so lassen“):

````text
Nein, so lassen
````

#### Ergebnis der KI zu Prompt 2 (vollständig)

**Ausgeführte Aktionen:** `git status`, `git log --oneline -1` ausgeführt und den Aufbau von Eintrag 11 gelesen. Ergebnis: Nur `AI_USAGE.md` ist geändert, nichts committet. Prompt 2 und dieses Ergebnis in `AI_USAGE.md` eingetragen. Keine `.gitattributes` angelegt, am Code nichts geändert.

**Kurzfazit im Antwortfeld der App:** „Die Zeilenenden bleiben, wie sie sind, es gibt keine .gitattributes. Geändert ist nur AI_USAGE.md (Linkziele und Logbuch), noch nicht committet.“ Frage: „Soll ich nach eurem Commit mit Schritt 9 (README) weitermachen?“

**Antwort der KI:**

````markdown
Alles klar, die Zeilenenden bleiben, wie sie sind. Ich lege keine `.gitattributes` an. Die Meldungen `LF will be replaced by CRLF` sind nur Hinweise von Git und ändern nichts am Inhalt des Repositorys.

Geändert ist damit nur `AI_USAGE.md` (die korrigierten Linkziele und das Logbuch). Commit-Vorschlag wie zuvor:

```
docs: tote Links in AI_USAGE.md nach Paket-Umbenennung korrigiert
```
````

#### Akzeptiert / Geändert / Abgelehnt

- **Akzeptiert:** Alles
- **Geändert:** Nichts
- **Abgelehnt:** Nichts

#### Geprüft / Getestet

-

#### Commits

Tote Links in AI_USAGE.md nach Paket-Umbenennung korrigiert

---

### Eintrag 12: Schritt 9 – README mit Build- und Startschritten

- **Datum:** 23.11.2026
- **Autor:** Benjamin
- **Tool:** Claude Code (Desktop-App), Claude Opus 5.5

#### Prompt 1

````text
Ja, Schritt 9
````

#### Ergebnis der KI zu Prompt 1 (vollständig)

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

**Antwort der KI:**

````markdown
Schritt 9 ist fertig: [README.md](README.md) liegt im Root. Ich habe die Anleitung an einem frisch geklonten Repo geprüft, sie funktioniert. Damit sind **alle fünf Mindestanforderungen erfüllt**.

## Inhalt des README

- Kurzbeschreibung der App (rein lokal, kein Backend)
- **Gruppe: eintragen.** Bitte eure Namen selbst eintragen, ich wollte nichts raten.
- Funktionen nach aktuellem Stand (Liste, Suche, Detail, Erstellen, Löschen, Kamera, lokale Speicherung)
- Voraussetzungen: Android Studio, SDK API 37, Gerät ab Android 8.0, Internet beim ersten Build
- **Build und Start in Android Studio** in 5 Schritten (klonen → öffnen → Sync → Gerät → Run)
- **Build über die Kommandozeile** (`assembleDebug`, `installDebug`, `testDebugUnitTest`)
- Tabelle der Unit-Tests und wie man sie startet
- Hinweise zum Emulator (Kamera-Testbild, Zurück aus der Kamera, Absturz der Emulator-Kamera, Beispieldaten zurücksetzen)
- Aufbau des Codes (Schichten und Ordner) mit Verweis auf CLAUDE.md
- KI-Nutzung mit Verweis auf AI_USAGE.md
- Der Pflichtsatz: **„Die Gruppe verantwortet jede Zeile des Projekts.“**

## Zwei Dinge, die ich beim Prüfen gelernt habe

- **Java:** Bei euch zeigt `JAVA_HOME` auf ein altes Java 8. Trotzdem baut alles, weil `gradlew` nur zum Starten Java braucht. Den eigentlichen Build macht Gradle mit **Java 25**, das in `gradle/gradle-daemon-jvm.properties` festgelegt ist und automatisch geladen wird (Android Studio hat es ohnehin dabei). Deshalb steht im README „kein eigenes JDK nötig“.
- **SDK-Pfad:** `local.properties` ist (richtig) nicht im Git. Ein frischer Klon kennt deshalb den SDK-Pfad nicht. Android Studio legt die Datei beim ersten Öffnen selbst an. Auf der Kommandozeile muss man `ANDROID_HOME` setzen. Ohne das kommt „SDK location not found“. Das habe ich nachgestellt und ins README geschrieben.

## Was ich geprüft habe

| Prüfung                                                                                                                     | Ergebnis                                               |
|-----------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------|
| Frischer `git clone` (ohne `local.properties`, ohne `build`), nur `ANDROID_HOME` gesetzt, `assembleDebug testDebugUnitTest` | ✅ BUILD SUCCESSFUL, 24/24 Tests                       |
| Derselbe Klon ohne `ANDROID_HOME`                                                                                           | ❌ „SDK location not found“, wie im README angekündigt |
| `installDebug` aus dem Klon, App starten                                                                                    | ✅ Liste erscheint                                     |

Nicht geprüft: der Weg über **Android Studio auf einem fremden Rechner** (öffnen, Sync, Run). Das wäre der Test, den ein Prüfer macht. Am besten klont einer von euch das Repo nach dem Push einmal in einen neuen Ordner und folgt dem README Schritt für Schritt.

## Testanleitung

- README auf GitHub ansehen (nach dem Push): Tabellen und Codeblöcke müssen sauber dargestellt werden.
- Den Pflichtsatz suchen: steht ganz unten.
- Das README wächst mit den nächsten Schritten mit (Bearbeiten, Favoriten, Filter usw. kommen in die Funktionsliste).
- Rotation, Permission, Flugmodus, Neustart, ungültige Eingaben: nicht betroffen, am App-Code ist nichts geändert.

## Commit-Vorschlag

```
docs: README mit Build- und Startschritten
```

(`README.md`, `AI_USAGE.md`)
````

#### Akzeptiert / Geändert / Abgelehnt

- **Akzeptiert:** Alles
- **Geändert:** Nichts
- **Abgelehnt:** Nichts

#### Geprüft / Getestet

Korrekturgelesen 

#### Commits

README mit Build- und Startschritten

---

### Eintrag 13: Schritt 9a – Foto im Vollbild

- **Datum:** 23.11.2026
- **Autor:** Semih
- **Tool:** Claude Code (Desktop-App), Claude Opus 5.5

#### Prompt 1

````text
Ja, Schritt 9a
````

#### Ergebnis der KI zu Prompt 1 (vollständig)

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

**Antwort der KI:**

````markdown
Schritt 9a ist fertig: In der Detailansicht öffnet ein Tipp auf das Foto einen eigenen Screen „Foto“, auf dem das ganze Bild so groß wie möglich zu sehen ist. Build und Test auf dem Emulator liefen ohne Fehler.

## Neue und geänderte Dateien (`app/src/main/java/de/dhbw/campusmarket/`)

| Datei                                                                                                                   | Was sie tut                                                                                                      |
|-------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------|
| [ui/photoviewer/PhotoViewerUiState.kt](app/src/main/java/de/dhbw/campusmarket/ui/photoviewer/PhotoViewerUiState.kt)     | `isLoading`, `hasError`, `imageUri`                                                                              |
| [ui/photoviewer/PhotoViewerViewModel.kt](app/src/main/java/de/dhbw/campusmarket/ui/photoviewer/PhotoViewerViewModel.kt) | Lädt über die id die Anzeige aus Room und übernimmt nur die Foto-Uri                                             |
| [ui/photoviewer/PhotoViewerScreen.kt](app/src/main/java/de/dhbw/campusmarket/ui/photoviewer/PhotoViewerScreen.kt)       | Route + stateless Screen: `TopAppBar` „Foto“ mit Zurück-Pfeil, darunter das Foto mit `ContentScale.Fit`, Preview |
| [ui/navigation/Destinations.kt](app/src/main/java/de/dhbw/campusmarket/ui/navigation/Destinations.kt)                   | Neues Ziel `PhotoViewer(adId)`                                                                                   |
| [ui/navigation/AppNavHost.kt](app/src/main/java/de/dhbw/campusmarket/ui/navigation/AppNavHost.kt)                       | `composable<PhotoViewer>`, und die Detailseite bekommt `onPhotoClick`                                            |
| [ui/addetail/AdDetailScreen.kt](app/src/main/java/de/dhbw/campusmarket/ui/addetail/AdDetailScreen.kt)                   | Das Foto ist jetzt antippbar (`clickable`)                                                                       |

Dazu 4 neue Texte in `strings.xml` und im [README](README.md) die neue Funktion und der Ordner `photoviewer/`.

## Warum so

- **Eigener Screen statt Dialog:** So gilt dasselbe Muster wie überall (Route, Screen, ViewModel, UiState), der System-Zurück-Knopf funktioniert von selbst, und die Ansicht überlebt eine Drehung, weil der NavHost den Back-Stack speichert.
- **Nur die id wird übergeben,** nicht die Uri. Das verlangt CLAUDE.md, und es hat einen Vorteil: Der Screen liest immer den aktuellen Stand aus Room. Wird die Anzeige inzwischen gelöscht, zeigt er „Zu dieser Anzeige gibt es kein Foto.“ statt eines kaputten Bildes.
- **Eigener ViewModel,** obwohl er dem Detail-ViewModel ähnelt: CLAUDE.md sieht einen ViewModel pro Screen vor, und jeder Screen braucht seinen eigenen Loading/Error/Ready-Zustand.
- **`ContentScale.Fit`:** Das ganze Foto ist sichtbar, nichts wird abgeschnitten, im Hoch- wie im Querformat. In der Detailansicht bleibt `Crop`, damit der 240-dp-Streifen ausgefüllt ist.
- **Normaler heller Hintergrund statt Schwarz:** Bei schwarzem Hintergrund wären die dunklen Symbole der Statusleiste (Uhrzeit, Akku) im hellen Theme nicht mehr zu sehen. Das zu ändern bräuchte zusätzlichen Code für die Systemleisten. So bleibt die App einheitlich.
- **`onClickLabel = "Foto vergrößern"`:** TalkBack liest das vor, damit man weiß, was der Tipp bewirkt.
- **Zoom mit zwei Fingern** ist noch nicht drin. Wenn ihr das wollt, wäre es eine kleine Ergänzung im Screen (`Modifier.transformable`), ohne neue Bibliothek.
- **Im Formular** ist die Vorschau nicht antippbar, weil die Anzeige dort noch keine id hat. Vor dem Speichern sieht man das Foto ja schon groß genug.

## Was ich auf dem Emulator getestet habe

- Anzeige „foto“ öffnen → Foto antippen → Screen „Foto“, Testbild vollständig sichtbar.
- Querformat → Foto bleibt, passt ganz auf den Bildschirm.
- Zurück-Pfeil → Detail, nochmal → Liste.
- Logcat: kein Absturz.

## Testanleitung für euch

- Build und Tests:

```bash
./gradlew assembleDebug testDebugUnitTest
```

- Anzeige **mit** Foto öffnen → Foto antippen → Vollbild. Mit Zurück-Pfeil und System-Zurück zurück.
- Anzeige **ohne** Foto öffnen → es gibt kein Foto zum Antippen, nichts passiert.
- **Rotation** im Vollbild: Foto bleibt sichtbar.
- **App-Neustart** und **Flugmodus**: kein Unterschied, alles lokal.
- **Preview** in `PhotoViewerScreen.kt` zeigt den Fall „kein Foto“ (eine echte Bilddatei gibt es in der Preview nicht).
- Permission und ungültige Eingaben sind von diesem Schritt nicht betroffen.

## Commit-Vorschlag

```
feat: Foto der Anzeige im Vollbild ansehen
```

(`ui/photoviewer/*`, `ui/navigation/*`, `ui/addetail/AdDetailScreen.kt`, `strings.xml`, `README.md`, `AI_USAGE.md`)
````

#### Akzeptiert / Geändert / Abgelehnt

- **Akzeptiert:** Alles
- **Geändert:** Nichts
- **Abgelehnt:** Nichts

#### Geprüft / Getestet

Vollbild getestet

#### Commits

Foto der Anzeige im Vollbild ansehen

---

### Eintrag 14: Schritt 10 – Anzeige bearbeiten

- **Datum:** 23.11.2026
- **Autor:** Benjamin
- **Tool:** Claude Code (Desktop-App), Claude Opus 5.5

#### Prompt 1

````text
Ja, Schritt 10
````

#### Ergebnis der KI zu Prompt 1 (vollständig)

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

**Antwort der KI:**

````markdown
Schritt 10 ist fertig: In der Detailansicht gibt es oben ein Stift-Symbol. Es öffnet das bekannte Formular, schon ausgefüllt mit der gespeicherten Anzeige. „Speichern“ überschreibt die Anzeige, danach geht es zurück zur Detailseite, die sofort die neuen Werte zeigt.

## Was sich geändert hat (`app/src/main/…/campusmarket/`)

| Datei                                     | Änderung                                                                                                                          |
|-------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------|
| `ui/navigation/Destinations.kt`           | Neues Ziel `AdEdit(adId)`                                                                                                         |
| `ui/navigation/AppNavHost.kt`             | `composable<AdEdit>` nutzt dieselbe `AdFormRoute`, nur mit `adId` statt `null`. Die Detailseite bekommt `onEditClick`             |
| `ui/addetail/AdDetailScreen.kt`           | Stift-Symbol vor dem Mülleimer, Parameter `onEditClick`                                                                           |
| `ui/adform/AdFormViewModel.kt`            | Bekommt `adId: Long?`. `loadAd()` füllt das Formular, `onSaveClick` ruft `addAd` oder `updateAd` auf                              |
| `ui/adform/AdFormUiState.kt`              | Neue Felder `isEditing`, `isLoading`, `hasError`, `notFound`                                                                      |
| `ui/adform/AdFormScreen.kt`               | Titel „Anzeige bearbeiten“, Zustände Laden/Fehler/nicht gefunden, Eingabefelder in `FormContent` ausgelagert, neue Preview „Edit“ |
| `logic/AdValidation.kt` + Test            | `priceToInput(12.5)` ergibt `"12,50"`, dazu 2 neue Tests (jetzt 26)                                                               |
| `res/drawable/ic_edit.xml`, `strings.xml` | Stift-Icon und 2 neue Texte                                                                                                       |

## Warum so

- **Ein Formular für beides:** Erstellen und Bearbeiten unterscheiden sich nur darin, ob am Anfang geladen wird und ob am Ende `addAd` oder `updateAd` aufgerufen wird. Ein zweiter Screen wäre fast eine Kopie.
- **Eigenes Ziel `AdEdit(adId)`** statt `AdForm(adId: Long? = null)`: Beide Wege sind im NavHost sofort zu sehen, und die Route braucht keinen nullable Parameter.
- **`first()` statt `collect`:** Das Formular wird nur einmal gefüllt. Mit `collect` würde jede Änderung in Room die gerade getippten Eingaben überschreiben. Weil `loadAd()` im `init` läuft, wird nach einer Drehung auch nicht neu geladen, eure Eingaben bleiben.
- **`originalAd` im ViewModel:** Das Formular kennt Favorit, Status „verkauft“ und Erstellzeitpunkt nicht. Beim Speichern werden diese Werte aus der geladenen Anzeige übernommen. Sonst würde Bearbeiten später einen Favoriten oder den Status „verkauft“ zurücksetzen, und die Anzeige würde in der Liste nach oben springen.
- **Laden, Fehler, nicht gefunden:** Beim Bearbeiten lädt der Screen etwas, darum braucht er diese Zustände (Regel aus CLAUDE.md). Die Texte „konnte nicht geladen werden“ und „gibt es nicht mehr“ sind dieselben wie in der Detailansicht, deshalb nutze ich deren Strings mit.
- **`priceToInput` in `logic/`:** Aus 7.5 muss „7,50“ werden, damit `parsePrice` es wieder annimmt. Der neue Test prüft genau diesen Hin- und Rückweg.

**Bekannte Einschränkung** (wie schon bei Schritt 8): Wer beim Bearbeiten ein neues Foto aufnimmt oder das Foto entfernt, lässt die alte Bilddatei im App-Speicher liegen. In der App sieht man davon nichts.

## Auf dem Emulator getestet

- „Mathe Formelsammlung“ → Stift: Das Formular ist ausgefüllt, der Titel lautet „Anzeige bearbeiten“.
- Preis auf 8,00 geändert, ins Querformat und zurück gedreht: Die Eingabe bleibt 8,00.
- Speichern: zurück zur Detailseite, sie zeigt 8,00 €. In der Liste steht die Anzeige an derselben Stelle.
- „Analysis 1 – Lehrbuch“: Die Beschreibung wird mitgeladen. Titel geleert und gespeichert → Fehlertext, nichts gespeichert. Mit Zurück verlassen → Anzeige unverändert.
- App beendet und neu gestartet: 8,00 € ist noch da.
- Eure Anzeige „foto“ bearbeitet: Das Foto ist im Formular zu sehen und bleibt nach dem Speichern erhalten.
- Absturz-Log leer.

Achtung: Bei eurer Testanzeige „Mathe Formelsammlung“ steht jetzt 8,00 € statt 7,50 €.

## Testanleitung für euch

```powershell
.\gradlew.bat assembleDebug testDebugUnitTest
```

- **Ungültige Eingaben:** Beim Bearbeiten den Titel leeren oder als Preis „abc“ eingeben → Fehlertext, kein Absturz.
- **Rotation:** Im Formular etwas ändern und drehen → die Änderung bleibt.
- **Permission verweigert:** Beim Bearbeiten „Neues Foto“ tippen und die Kamera verweigern → Hinweis erscheint, Speichern geht trotzdem, das alte Foto bleibt.
- **Flugmodus:** kein Unterschied, alles ist lokal.
- **App-Neustart:** Die geänderten Werte bleiben erhalten.
- Zusätzlich: Etwas ändern und mit Zurück verlassen → nichts wird gespeichert.

Prompt und Antwort stehen als Eintrag 14 in AI_USAGE.md.

## Commit-Vorschlag

```
feat: Anzeige bearbeiten
```

(`logic/AdValidation.kt`, `ui/adform/*`, `ui/addetail/AdDetailScreen.kt`, `ui/navigation/*`, `ic_edit.xml`, `strings.xml`, `AdValidationTest.kt`, `README.md`, `AI_USAGE.md`)
````

#### Prompt 2

````text
Die Rotation funktioniert nicht mehr. Die Anzeige bleibt immer im Hochformat. Den Commit von vorhin habe ich noch nicht gemacht
````

#### Ergebnis der KI zu Prompt 2 (vollständig)

**Ausgeführte Aktionen:**

1. `AI_USAGE.md`: Prompt 2 in Eintrag 14 eingetragen.
2. Im Projekt nach `screenOrientation`, `requestedOrientation` und `configChanges` gesucht. Ergebnis: nichts gefunden, das Manifest ist unverändert.
3. Emulator-Einstellungen gelesen. Ergebnis: `accelerometer_rotation = 0` (Automatisch drehen aus, von der KI beim Drehtest in Prompt 1 gesetzt), `user_rotation = 0`.
4. `adb shell settings put system accelerometer_rotation 1` ausgeführt, danach `adb emu rotate` (viermal). Ergebnis: Anzeige wechselt auf `ROTATION_270` (Querformat) und zurück auf `ROTATION_0`.
5. `AI_USAGE.md`: dieses Ergebnis eingetragen. Am Code nichts geändert.

**Kurzfazit im Antwortfeld der App:** „Die Rotation war im Emulator gesperrt, weil ich für den Drehtest „Automatisch drehen“ ausgeschaltet und danach nicht wieder eingeschaltet hatte. Die Einstellung ist zurückgesetzt, und der Emulator dreht wieder. Am Code ist nichts geändert, Schritt 10 ist weiterhin nicht committet.“ Frage: „Soll ich nach eurem Commit mit Schritt 11 (Favoriten) weitermachen?“

**Antwort der KI:**

````markdown
Das lag nicht an der App, sondern an einer Einstellung im Emulator, die ich selbst verstellt hatte.

## Ursache

Für den Drehtest in Schritt 10 habe ich im Emulator „Automatisch drehen“ ausgeschaltet (`accelerometer_rotation = 0`) und die Drehung per Befehl gesetzt. Danach habe ich vergessen, die Einstellung wieder einzuschalten. Ohne „Automatisch drehen“ bleibt Android im Hochformat, egal wie man den Emulator dreht. Das betrifft alle Apps, nicht nur CampusMarket.

Im Code gibt es keine Sperre: Weder im Manifest noch im Code steht `screenOrientation` oder `requestedOrientation`.

## Was ich gemacht habe

- „Automatisch drehen“ im Emulator wieder eingeschaltet (`accelerometer_rotation = 1`).
- Mit dem Drehbefehl des Emulators geprüft: Die Anzeige wechselt ins Querformat und wieder zurück ins Hochformat.
- Am Projekt ist nichts geändert, nur dieser Eintrag im Logbuch.

Falls es bei euch trotzdem noch hängt: Im Emulator von oben die Schnelleinstellungen herunterziehen und „Automatisch drehen“ antippen.

## Zum Commit

Der Commit-Vorschlag von Schritt 10 bleibt gleich. Diese Antwort steht als Prompt 2 in Eintrag 14 in AI_USAGE.md und kommt mit in denselben Commit:

```
feat: Anzeige bearbeiten
```
````

#### Akzeptiert / Geändert / Abgelehnt

- **Akzeptiert:** Alles
- **Geändert:** Nichts
- **Abgelehnt:** Nichts

#### Geprüft / Getestet

Bearbeiten getestet, Rotation geht nicht mehr
-> Claude hat die automatische Rotation im Emulator ausgeschaltet

#### Commits

Anzeige bearbeiten

---

### Eintrag 15: Schritt 11 – Favoriten

- **Datum:** 23.11.2026
- **Autor:** Semih
- **Tool:** Claude Code (Desktop-App), Claude Opus 5.5

#### Prompt 1

````text
Ja, Schritt 11
````

#### Ergebnis der KI zu Prompt 1 (vollständig)

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

**Antwort der KI:**

````markdown
Schritt 11 ist fertig: Jede Anzeige hat jetzt ein Herz. Ein Tipp darauf merkt sie als Favorit (gefülltes, farbiges Herz), ein zweiter Tipp nimmt sie wieder heraus. Das Herz gibt es in der Liste rechts neben dem Preis und in der Detailansicht oben rechts. Build und Tests liefen ohne Fehler, und auf dem Emulator gab es keinen Absturz.

## Was sich geändert hat (`app/src/main/…/campusmarket/`)

| Datei                                                                         | Änderung                                                                                                       |
|-------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------|
| `data/AdDao.kt`                                                               | Neue Abfrage `setFavorite(id, isFavorite)`, ändert nur die Spalte `isFavorite`                                 |
| `data/AdRepository.kt`                                                        | Reicht `setFavorite` an das DAO weiter                                                                         |
| `ui/FavoriteButton.kt` (neu)                                                  | Herz-Knopf, den Liste und Detailansicht gemeinsam nutzen                                                       |
| `ui/adlist/AdListViewModel.kt`, `AdListScreen.kt`                             | `onFavoriteClick(ad)`. Rechts in jeder Zeile stehen Preis und Herz. Die Preview zeigt eine Anzeige als Favorit |
| `ui/addetail/AdDetailViewModel.kt`, `AdDetailScreen.kt`                       | `onFavoriteClick()`, Herz vor Stift und Mülleimer                                                              |
| `res/drawable/ic_favorite.xml`, `ic_favorite_border.xml` (neu), `strings.xml` | Gefülltes Herz, Herz-Umriss und 2 Texte                                                                        |

Die Datenbank musste nicht geändert werden: Die Spalte `isFavorite` gibt es seit Schritt 2.

## Warum so

- **Eigene Abfrage `UPDATE … SET isFavorite` statt `updateAd(ad.copy(...))`:** Die Abfrage ändert nur die eine Spalte. Mit `updateAd` würde die ganze Zeile mit dem Stand geschrieben, den der Screen gerade kennt. Wäre dieser Stand veraltet, gingen andere Änderungen verloren.
- **Der ViewModel setzt das Herz nicht selbst:** Er schreibt nur in Room. Weil Liste und Detail Room über einen Flow beobachten, kommt der neue Stand von selbst zurück. Room bleibt so die einzige Quelle der Wahrheit, und Liste und Detail zeigen immer dasselbe.
- **Ein gemeinsamer `FavoriteButton`:** Das Herz sieht an beiden Stellen gleich aus und hat dieselben Texte für TalkBack („Zu Favoriten hinzufügen“ bzw. „Aus Favoriten entfernen“). So steht der Code nur einmal im Projekt.
- **Das Herz in der Liste ist ein eigener Knopf:** Ein Tipp darauf schaltet nur den Favoriten um und öffnet nicht die Detailseite.
- **Bearbeiten behält den Favoriten:** Das war schon in Schritt 10 vorbereitet (`originalAd`).

**Noch nicht dabei:** eine Ansicht „nur Favoriten“. Die passt zu Schritt 13 (Filter) und kommt dort als Filter-Chip dazu.

## Auf dem Emulator getestet

- **Liste:** Herz bei „Analysis 1 – Lehrbuch“ angetippt. Es wird gefüllt, die Liste bleibt offen.
- **Detail:**
  - Das Herz ist auch hier gefüllt.
  - Antippen nimmt den Favoriten heraus, erneutes Antippen setzt ihn wieder.
- **Querformat:** Das Herz bleibt gefüllt.
- **Bearbeiten:** Nach dem Speichern bleibt die Anzeige Favorit.
- **App-Neustart:** Das Herz in der Liste ist noch gefüllt.
- Absturz-Log leer.

## Testanleitung für euch

```powershell
.\gradlew.bat assembleDebug testDebugUnitTest
```

- **Favorit setzen:** In der Liste ein Herz antippen, dann die Anzeige öffnen. Das Herz ist auch dort gefüllt. In der Detailansicht wieder entfernen und zurück zur Liste: Das Herz ist dort leer.
- **Rotation:** Ein Favorit bleibt beim Drehen Favorit.
- **App-Neustart:** Favoriten bleiben erhalten.
- **Flugmodus:** kein Unterschied, alles ist lokal.
- **Ungültige Eingaben, Permission verweigert:** nicht betroffen. Bearbeiten und Kamera funktionieren wie vorher.
- **TalkBack (optional):** Das Herz wird als „Zu Favoriten hinzufügen“ bzw. „Aus Favoriten entfernen“ vorgelesen.

Prompt und Antwort stehen als Eintrag 15 in AI_USAGE.md.

## Commit-Vorschlag

```
feat: Anzeigen als Favorit merken
```

(`data/AdDao.kt`, `data/AdRepository.kt`, `ui/FavoriteButton.kt`, `ui/adlist/*`, `ui/addetail/*`, `ic_favorite*.xml`, `strings.xml`, `README.md`, `AI_USAGE.md`)
````

#### Akzeptiert / Geändert / Abgelehnt

- **Akzeptiert:** Alles
- **Geändert:** Nichts
- **Abgelehnt:** Nichts

#### Geprüft / Getestet

Favoriten-Markierung getestet

#### Commits

Anzeigen als Favorit merken

---

### Eintrag 16: Schritt 12 – Status verfügbar/verkauft

- **Datum:** 23.11.2026
- **Autor:** Semih
- **Tool:** Claude Code (Desktop-App), Claude Opus 5.5

#### Prompt 1

````text
Ja, Schritt 12
````

#### Ergebnis der KI zu Prompt 1 (vollständig)

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

**Antwort der KI:**

````markdown
Schritt 12 ist fertig: Eine Anzeige lässt sich jetzt als verkauft markieren und wieder freigeben. In der Detailansicht steht dafür unter dem Status ein Knopf, und in der Liste steht unter dem Ort ein rotes „Verkauft“. Build und Tests liefen ohne Fehler, und auf dem Emulator gab es keinen Absturz.

**Vorher noch ein Hinweis zu AI_USAGE.md:** Bevor ich angefangen habe, war die Datei schon geändert, ohne Commit. Unter eurem fertigen Eintrag 15 stand eine zweite, ältere Kopie davon: mein ursprünglicher Text mit den alten Tabellen und leeren Team-Feldern. Das kommt vermutlich von einem Editor, der die Datei noch im alten Stand offen hatte und wieder gespeichert hat. Ich habe die Kopie entfernt. Euer committeter Eintrag 15 ist unverändert. Falls AI_USAGE.md bei euch in Android Studio offen ist, ladet sie bitte neu, bevor ihr sie bearbeitet.

## Was sich geändert hat (`app/src/main/…/campusmarket/`)

| Datei                              | Änderung                                                                                      |
|------------------------------------|-----------------------------------------------------------------------------------------------|
| `data/AdDao.kt`                    | Neue Abfrage `setSold(id, isSold)`, ändert nur die Spalte `isSold`                            |
| `data/AdRepository.kt`             | Reicht `setSold` an das DAO weiter                                                            |
| `ui/addetail/AdDetailViewModel.kt` | `onSoldClick()` schaltet zwischen verfügbar und verkauft um                                   |
| `ui/addetail/AdDetailScreen.kt`    | Knopf „Als verkauft markieren“ bzw. „Wieder als verfügbar markieren“ unter der Zeile „Status“ |
| `ui/adlist/AdListScreen.kt`        | Rotes „Verkauft“ unter dem Ort. Die Preview zeigt eine verkaufte Anzeige                      |
| `res/values/strings.xml`           | 2 neue Texte                                                                                  |

Die Datenbank musste nicht geändert werden: Die Spalte `isSold` und die Zeile „Status“ in der Detailansicht gibt es seit Schritt 2 bzw. 5.

## Warum so

- **Gleiches Muster wie bei den Favoriten:** Eine Abfrage ändert nur die eine Spalte. Der ViewModel schreibt nur in Room, und der neue Stand kommt über den Flow in Liste und Detail an.
- **Ein Knopf im Inhalt statt eines Symbols oben:** Oben rechts stehen schon Herz, Stift und Mülleimer. Ein vierter Knopf ohne Text wäre schwer zu verstehen. Ein Knopf mit Text erklärt sich selbst.
- **Der Knopftext beschreibt die Aktion:** Der aktuelle Zustand steht schon in der Zeile darüber („Status: Verkauft“). Der Knopf sagt, was beim Tippen passiert („Wieder als verfügbar markieren“).
- **Kein Bestätigungsdialog:** Anders als beim Löschen geht nichts verloren, ein zweiter Tipp macht es rückgängig.
- **Verkaufte Anzeigen bleiben in der Liste:** Ein Filter „nur verfügbare“ passt zu Schritt 13 und kommt dort zusammen mit „nur Favoriten“ dazu.
- **Bearbeiten behält den Status:** Das war schon in Schritt 10 vorbereitet (`originalAd`).

## Auf dem Emulator getestet

- **„Taschenrechner TI-30X“:** „Als verkauft markieren“ angetippt. Der Status wechselt auf „Verkauft“, der Knopf auf „Wieder als verfügbar markieren“.
- **Querformat:** Der Status bleibt „Verkauft“.
- **Liste:** Unter „Haupteingang“ steht rot „Verkauft“ (Screenshot).
- **Bearbeiten:** Nach dem Speichern bleibt die Anzeige verkauft.
- **App-Neustart:** Die Anzeige ist noch verkauft.
- **Zurücksetzen:** „Wieder als verfügbar markieren“ ändert den Status zurück auf „Verfügbar“.
- Absturz-Log leer.

## Testanleitung für euch

```powershell
.\gradlew.bat assembleDebug testDebugUnitTest
```

- **Umschalten:** Eine Anzeige öffnen, „Als verkauft markieren“ antippen, zurück zur Liste. Dort steht „Verkauft“. Wieder öffnen und auf „verfügbar“ zurückstellen.
- **Rotation:** Der Status bleibt beim Drehen erhalten.
- **App-Neustart:** Der Status bleibt erhalten.
- **Flugmodus:** kein Unterschied, alles ist lokal.
- **Ungültige Eingaben, Permission verweigert:** nicht betroffen. Formular und Kamera funktionieren wie vorher.

Prompt und Antwort stehen als Eintrag 16 in AI_USAGE.md.

## Commit-Vorschlag

```
feat: Anzeige als verkauft markieren
```

(`data/AdDao.kt`, `data/AdRepository.kt`, `ui/addetail/*`, `ui/adlist/AdListScreen.kt`, `strings.xml`, `README.md`, `AI_USAGE.md`)
````

#### Akzeptiert / Geändert / Abgelehnt

- **Akzeptiert:** Alles
- **Geändert:** Nichts
- **Abgelehnt:** Nichts

#### Geprüft / Getestet

Neuer Button (Verkauft) getestet

#### Commits

Anzeige als verkauft markieren

---

### Eintrag 17: Schritt 13 – Filter

- **Datum:** 24.11.2026
- **Autor:** Benjamin
- **Tool:** Claude Code (Desktop-App), Claude Opus 5.5

#### Prompt 1

````text
Ja, Schritt 13
````

#### Ergebnis der KI zu Prompt 1 (vollständig)

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

**Antwort der KI:**

````markdown
Schritt 13 ist fertig: Unter dem Suchfeld steht jetzt eine Zeile mit Filter-Chips: „Favoriten“, „Nur verfügbare“ und je ein Chip pro Kategorie. Die Zeile lässt sich seitlich wischen. Mehrere Filter gelten gleichzeitig und lassen sich mit der Suche kombinieren. Build und alle 33 Tests (7 davon neu) liefen ohne Fehler, und auf dem Emulator gab es keinen Absturz.

## Was sich geändert hat (`app/src/…/campusmarket/`)

| Datei                                | Änderung                                                                                                                  |
|--------------------------------------|---------------------------------------------------------------------------------------------------------------------------|
| `logic/AdFilter.kt` (neu)            | Reine Funktion `filterAds(ads, category, onlyFavorites, onlyAvailable)`                                                   |
| `test/…/logic/AdFilterTest.kt` (neu) | 7 JUnit-Tests: kein Filter, je ein Filter, alle zusammen, kein Treffer, leere Liste                                       |
| `ui/adlist/AdListUiState.kt`         | Neue Felder `categoryFilter`, `onlyFavorites`, `onlyAvailable`. `visibleAds` wendet erst die Suche und dann die Filter an |
| `ui/adlist/AdListViewModel.kt`       | `onCategoryFilterClick`, `onOnlyFavoritesClick`, `onOnlyAvailableClick`                                                   |
| `ui/adlist/AdListScreen.kt`          | `FilterRow` mit `FilterChip`s, eigener Hinweis, wenn nur die Filter nichts übrig lassen, neue Preview „Filter“            |
| `res/values/strings.xml`             | 3 neue Texte                                                                                                              |

## Warum so

- **Filterlogik als reine Funktion in `logic/`:** Wie die Suche hat sie keine Android-Abhängigkeit und ist deshalb mit JUnit testbar (Regel aus CLAUDE.md).
- **Gefilterte Liste wird berechnet, nicht gespeichert:** `visibleAds` wird jedes Mal aus `ads`, Suchtext und Filtern berechnet. Ändert sich eine Anzeige in Room (z. B. Herz entfernt), passt die gefilterte Liste sofort, ohne extra Code.
- **Filter im UiState des ViewModels:** Damit überstehen sie eine Drehung. Nur die Wisch-Position der Chip-Zeile liegt im Composable (`rememberScrollState`), weil sie reiner UI-State ist.
- **Kategorie ist eine Einzelauswahl:** Ein zweiter Tipp auf dieselbe Kategorie hebt den Filter wieder auf, statt einen eigenen Chip „Alle“ zu brauchen. „Favoriten“ und „Nur verfügbare“ sind An/Aus-Schalter.
- **Zwei verschiedene Hinweise bei leerer Liste:** Mit Suchtext erscheint wie bisher „Keine Treffer für „…“.“. Ohne Suchtext würde dort „Keine Treffer für „“.“ stehen, darum gibt es dann „Keine Anzeigen passen zu den gewählten Filtern.“
- **Filter werden nicht dauerhaft gespeichert:** Nach einem App-Neustart sind sie wie der Suchtext wieder aus. Dauerhaft gespeichert werden nur die Anzeigen selbst.

## Auf dem Emulator getestet

- **„Favoriten“ und „Bücher“ zusammen:** Nur „Analysis 1 – Lehrbuch“ bleibt übrig (Screenshot).
- **Nur „Bücher“:** Alle Anzeigen der Kategorie Bücher erscheinen, auch eine verkaufte.
- **Nur „Nur verfügbare“:** Die beiden verkauften Anzeigen („foto test“, „Programmieren in Kotlin“) fehlen.
- **Querformat:** Filter und Liste bleiben gleich.
- **Suche „Stuhl“ mit „Favoriten“ und „Nur verfügbare“:** „Keine Treffer für „Stuhl“.“ Nach dem Abwählen von „Favoriten“ erscheint „Schreibtischstuhl“.
- **„Favoriten“ und „Skripte“ ohne Suchtext:** „Keine Anzeigen passen zu den gewählten Filtern.“
- Absturz-Log leer.

## Testanleitung für euch

```powershell
.\gradlew.bat assembleDebug testDebugUnitTest
```

- **Filter:** Chips einzeln und zusammen antippen, auch nach rechts wischen bis „Sonstiges“. Eine Kategorie zweimal antippen hebt sie wieder auf.
- **Ungültige Eingaben:** Filter, die nichts übrig lassen, zeigen einen Hinweis, keinen leeren Bildschirm und keinen Absturz.
- **Rotation:** Filter einschalten und drehen. Die Chips bleiben ausgewählt.
- **App-Neustart:** Filter sind wieder aus, die Anzeigen bleiben erhalten.
- **Flugmodus:** kein Unterschied, alles ist lokal.
- **Permission verweigert:** nicht betroffen.
- **Zusammenspiel:** Bei aktivem Filter „Favoriten“ ein Herz entfernen. Die Anzeige verschwindet sofort aus der Liste.

Prompt und Antwort stehen als Eintrag 17 in AI_USAGE.md.

## Commit-Vorschlag

```
feat: Anzeigen nach Kategorie, Favoriten und Status filtern
```

(`logic/AdFilter.kt`, `AdFilterTest.kt`, `ui/adlist/*`, `strings.xml`, `README.md`, `AI_USAGE.md`)
````

#### Akzeptiert / Geändert / Abgelehnt

- **Akzeptiert:** Alles
- **Geändert:** Nichts
- **Abgelehnt:** Nichts

#### Geprüft / Getestet

Filter getestet, funktioniert

#### Commits

Anzeigen nach Kategorie, Favoriten und Status filtern

---

## 3. Reflexion

_Platzhalter: Wird zum Projektende ausgefüllt (1–2 Seiten)._
