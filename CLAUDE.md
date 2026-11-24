# CLAUDE.md – Regeln für CampusMarket

Prüfungsprojekt (DHBW Mannheim, „Entwicklung mobiler Applikationen“). Abgabe: 08.12.2026 über GitHub.
Jedes Gruppenmitglied muss in der Code Defence jede Zeile ohne KI erklären können.
Jede Regel unten ist so formuliert, dass man sie am Code prüfen kann.

## Projektdaten

- Paket / namespace / applicationId: `de.dhbw.campusmarket` (bis Eintrag 8 im Logbuch hieß es `com.example.campusmarket`)
- Compose-Theme: `CampusMarketTheme` (`ui/theme/Theme.kt`), XML-Theme: `Theme.CampusMarket`
- Build: Gradle 9.6.0, AGP 9.4.1, Kotlin 2.2.10, Version Catalog in `gradle/libs.versions.toml`
- compileSdk / targetSdk 37, **minSdk 26**
- Bibliotheksversionen müssen zu Kotlin 2.2 passen. Coil bleibt deshalb auf **3.3.0**, weil Coil 3.4 und neuer Kotlin 2.3/2.4 voraussetzen und der Build dann bricht.
- Keine Bibliothek ohne Aufgabe: kein `room-ktx` (Flow/suspend steckt seit Room 2.7 in `room-runtime`), keine eigene `kotlinx-serialization-json` (Navigation bringt `serialization-core` mit).
- Keine Netzwerkzugriffe, kein Login, kein Backend. Die App ist rein lokal.
- Icons liegen als Vector-Drawable in `res/drawable` (z. B. `ic_arrow_back.xml`) und werden mit `painterResource` geladen. Keine `material-icons`-Bibliothek. Weil es kein AppCompat gibt, nutzen Drawables keine `?attr/…`-Farben.

## Arbeitsweise mit der KI

1. **Prompts immer wortwörtlich ins Logbuch.** Jeder Prompt wird vor der Bearbeitung unverändert in `AI_USAGE.md` eingetragen (neue Aufgabe = neuer Eintrag, Rückfragen = Prompt 2, 3, … im selben Eintrag). Geheimnisse und personenbezogene Daten → `[REDACTED]`.
2. **Ergebnis ins Logbuch, ohne Antworttext.** Nach jedem Prompt kommt unter „Ergebnis der KI zu Prompt N“ eine Liste der ausgeführten Aktionen (gelesene, angelegte und geänderte Dateien, ausgeführte Befehle mit Ergebnis) und das Kurzfazit aus dem Antwortfeld der App. Die ausführliche Antwort der KI kommt **nicht** ins Logbuch. Stattdessen steht unter „Commits“, in welchen Commit das Ergebnis eingeflossen ist (Hash und Nachricht).
3. Felder, die nur das Team kennt (Autor, Akzeptiert/Geändert/Abgelehnt, Geprüft), bleiben „eintragen“ oder bekommen einen markierten Vorschlag. Den Commit trägt die KI nach, sobald das Team committet hat (aus `git log`). Vorher steht dort „eintragen“.
4. **Kein Code ohne kurze Erklärung.** Nach jeder Änderung: kurze Erklärung auf Deutsch (was und warum), Testanleitung (Rotation, Permission verweigert, Flugmodus, App-Neustart, ungültige Eingaben) und Commit-Vorschlag.
5. **Die KI committet und pusht nie selbst.** Commits macht das Team.
6. Ein Schritt = ein Commit, der für sich baut. Nach jedem Schritt auf das OK des Teams warten.
7. Diese Datei wird aktualisiert, sobald sich eine Regel ändert.

## Code-Stil

- Einfacher, gut lesbarer Kotlin-Code auf Studierenden-Niveau. Keine unnötigen Abstraktionen (keine Interfaces mit nur einer Implementierung, keine generischen Basisklassen, kein UseCase-Layer).
- Nur Bibliotheken, die wirklich gebraucht werden: Compose/Material 3, Lifecycle-ViewModel, Navigation Compose, kotlinx-serialization, Room + KSP, Coil, JUnit (+ kotlinx-coroutines-test, falls nötig).
- **Kein Hilt, kein Koin**, keine anderen DI-Frameworks.
- Kein toter Code: keine ungenutzten Funktionen, Imports, Ressourcen oder auskommentierten Blöcke. Der Android-Studio-Beispielcode ist entfernt. Es gibt keine Instrumented Tests (`src/androidTest`) und deshalb auch keine Espresso-/Compose-Test-Bibliotheken.
- Kleine Einheiten: eine Datei pro Screen-Teil (Route/Screen, ViewModel, UiState), Funktionen kurz halten.
- **Höchstens 500 Zeilen pro Datei** (Clean Code). Ausnahmen: `res/values/strings.xml` und Markdown-Dokumente (`README.md`, `AI_USAGE.md`, `CLAUDE.md`).
- **Quellcode komplett auf Englisch:** Klassen, Funktionen, Variablen, Parameter und Kommentare. Deutsch sind nur die Texte in `strings.xml`.
- **Keine hartkodierten Labels:** Alle für Nutzer sichtbaren Texte stehen in `res/values/strings.xml` (Deutsch) und werden mit `stringResource` bzw. `R.string` geladen. Ausnahmen: Beispieldaten (`SampleData.kt`) und Werte in `@Preview`- und Test-Funktionen.
- **Benennung:** camelCase für Variablen und Funktionen, PascalCase für Klassen. Booleans beginnen mit `is`, `has`, `show` oder `only`. Der private `MutableStateFlow` heißt `_uiState`, der öffentliche `uiState`. Callbacks heißen `onXxx` (z. B. `onSaveClick`, `onQueryChange`).
- **Feste Reihenfolge in jeder Datei:**
  - ViewModel: 1. `_uiState` und `uiState`, 2. weitere private Felder (z. B. `loadJob`), 3. `init`, 4. öffentliche Funktionen (`loadXxx`, `onXxx`), 5. private Hilfsfunktionen, 6. `companion object` mit `factory`/`Factory`.
  - UiState: zuerst Laden/Fehler (`isLoading`, `hasError`), dann Daten, dann Eingaben, dann einmalige Ereignisse (`isSaved`, `isDeleted`), am Ende berechnete Werte (`get()`).
  - Screen-Datei: `TAG`/Konstanten, Route, Screen, private Teil-Composables, Previews.
- **Logging mit `android.util.Log`:** Jede Datei, die loggt, hat oben `private const val TAG = "<Klassenname>"`. Jeder `catch`-Block loggt mit `Log.e(TAG, "...", e)`. Wichtige Ereignisse (Beispieldaten eingefügt, Anzeige gespeichert, gelöscht, Favorit/Status geändert) loggen mit `Log.d`/`Log.i`. In Logs stehen nur ids und Zustände, keine Nutzereingaben. Reine Funktionen in `logic/` loggen nicht, damit sie ohne Android mit JUnit testbar bleiben.

## Architektur

- Schichten: **UI → ViewModel → Repository → Room (DAO)**. Keine Schicht überspringt eine andere.
- **Nur Room-Zugriffe im Repository/DAO.** Composables und ViewModels importieren weder `RoomDatabase` noch DAOs.
- Die Room-Datenbank ist die **Single Source of Truth**. Das DAO liefert Lese-Abfragen als `Flow`, Schreibzugriffe sind `suspend`-Funktionen.
- Datenbank und Repository werden **genau einmal** in einer eigenen `Application`-Klasse (`CampusMarketApplication`) erzeugt und im Manifest eingetragen.
- Beim ersten Start (leere Datenbank) werden Beispieldaten eingefügt.
- Room läuft mit **KSP** (kein kapt).

## State und ViewModels

- Jeder Screen hat **einen ViewModel** mit **einer `UiState`-data-class**, veröffentlicht als `StateFlow`.
- Intern: `private val _uiState = MutableStateFlow(...)`, öffentlich `val uiState: StateFlow<...> = _uiState.asStateFlow()`. Änderungen nur über `_uiState.update { it.copy(...) }`.
- **Nur der ViewModel ändert den State.** Jeder State hat genau einen Besitzer. Composables halten keinen fachlichen State (höchstens reinen UI-State wie ein offenes Dropdown über `rememberSaveable`).
- ViewModels werden über `viewModelFactory { initializer { ... } }` gebaut; das Repository kommt aus der Application-Klasse.
- Der State überlebt eine Bildschirmdrehung (ViewModel; Eingaben im Formular liegen im UiState des ViewModels).
- **Jeder ladende Screen hat Loading/Error/Ready.** Das wird im UiState abgebildet und im Screen sichtbar dargestellt (z. B. `CircularProgressIndicator`, Fehlertext, Inhalt).

## Screens

- Jeder Screen besteht aus
  - einer **Route**-Composable (`XyzRoute`): holt den ViewModel, sammelt den State mit `collectAsStateWithLifecycle()` und reicht Werte und Callbacks weiter;
  - einem **stateless Screen**-Composable (`XyzScreen`): bekommt nur Werte und Lambdas, kennt keinen ViewModel;
  - mindestens einer **`@Preview`** für das Screen-Composable.
- UX nach Android-Konventionen: Material 3, `Scaffold` mit `TopAppBar`, Zurück-Pfeil auf Unterseiten, FAB zum Erstellen, Bestätigungsdialog vor dem Löschen.

## Navigation

- Typsichere **Navigation Compose** mit `@Serializable`-Destinations.
- Zwischen Screens wird **nur die id** übergeben, nie ganze Objekte.
- **Nur der NavHost navigiert.** Screens bekommen Callbacks wie `onAdClick(id)` oder `onBack()` und rufen nie selbst `navController` auf.

## Kamera und Bilder

- Ablauf: zuerst `ActivityResultContracts.RequestPermission()` für `Manifest.permission.CAMERA`.
- Bei Erfolg: `ActivityResultContracts.TakePicture()` mit einer Uri aus einem `FileProvider`.
- Bei Ablehnung: sichtbarer Hinweis; Speichern ohne Foto bleibt möglich.
- Bilder werden mit **Coil** (`AsyncImage`) angezeigt. Gespeichert wird nur der Pfad/die Uri in Room.

## Logik und Tests

- Such-, Filter- und Sortierlogik sind **reine Kotlin-Funktionen** ohne Android-Abhängigkeiten.
- Für diese Funktionen gibt es **JUnit-Tests** unter `app/src/test`.
- Eingaben werden validiert (z. B. leerer Titel, ungültiger Preis); ungültige Eingaben führen nie zum Absturz.

## Robustheit (wird bei jeder Änderung geprüft)

- Kein Absturz bei leerer Eingabe, verweigerter Permission oder Rotation.
- Im Flugmodus kein Absturz und kein eingefrorener Screen.
- Daten bleiben nach App-Neustart erhalten.

## Abgabe

- README beschreibt Build- und Startschritte aus einem sauberen Checkout und enthält den Satz: „Die Gruppe verantwortet jede Zeile des Projekts.“
- README nennt alles, was vorausgesetzt wird (Android Studio, SDK, JDK, Gerät/Emulator, Internet beim ersten Build), und wird bei jeder neuen Voraussetzung ergänzt.
- `AI_USAGE.md` ist vollständig (Setup, alle Einträge, Reflexion).
