# KI-Nutzung im Projekt CampusMarket

Dieses Dokument legt die gesamte KI-Nutzung beider Gruppenmitglieder offen.
Alle Prompts stehen wortwörtlich, in Originalsprache und in der Reihenfolge, in der sie gesendet wurden.
Geheimnisse und personenbezogene Daten sind durch `[REDACTED]` ersetzt. Angehängte Dateien werden nur mit Namen genannt.

---

## 1. Setup

| Punkt | Angabe |
|---|---|
| IDE und Umgebung | Android Studio (Gradle 9.6.0, Android Gradle Plugin 9.4.1, Kotlin 2.2.10), Windows 11 |
| Assistenten und Modelle | Claude Code (Desktop-App, Code-Tab), Modell Claude Opus 5.5 (`claude-opus-5-5`) |
| Tools und Erweiterungen | eintragen (z. B. weitere Plugins in Android Studio, falls genutzt) |
| Instruction-File | [`CLAUDE.md`](CLAUDE.md) im Root des Repos |
| Teamregeln | Jeder Prompt wird sofort und wortwörtlich hier eingetragen. Die KI committet und pusht nie selbst; Commits machen wir, damit beide Mitglieder sinnvolle Commits haben. Jede Änderung wird von uns gelesen, verstanden und getestet, bevor sie committet wird. Jedes Mitglied muss jede Zeile ohne KI erklären können. |

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

#### Ergebnis der KI (Kurzfassung)

Die KI hat das Projekt angesehen (Paket `com.example.campusmarket`, Theme `CampusMarketTheme` / `Theme.CampusMarket`, AGP 9.4.1, Kotlin 2.2.10, Compose BOM 2026.02.01, compileSdk/targetSdk 37, minSdk 24), `CLAUDE.md` und diese Datei angelegt und einen Schrittplan vorgeschlagen. Es wurde noch kein App-Code geändert.

#### Akzeptiert / Geändert / Abgelehnt

- **Akzeptiert:** CLAUDE.md und AI_USAGE.md übernommen, weil sie die Vorgaben aus der Aufgabenstellung abbilden.
- **Geändert:** nichts
- **Abgelehnt:** nichts

#### Geprüft / Getestet

Beide Dateien gelesen und mit der Aufgabenstellung abgeglichen. Kein Build nötig, da kein Code geändert wurde.

#### Commits

CLAUDE.md und AI_USAGE.md anlegen

---

## 3. Reflexion

_Platzhalter: Wird zum Projektende ausgefüllt (1–2 Seiten)._
