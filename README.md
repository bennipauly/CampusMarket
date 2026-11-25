# CampusMarket

CampusMarket is a native Android app where students can offer and find used items (books, lecture notes, calculators, tech, furniture).
The app is an exam project for the module "Entwicklung mobiler Applikationen" (mobile application development) at DHBW Mannheim.

The app works **completely offline**: there is no backend, no login and no network access. All ads are stored in a Room database on the device.
The user interface of the app is in German.

**Group:** eintragen

## Features

- List of all ads with search (title, description, handover location)
- Detail screen with photo, price, category, condition, handover location and status
- Create an ad with input validation (empty title, invalid price, missing handover location)
- Edit an ad (pencil icon on the detail screen) with the same validation as when creating it
- Delete an ad with a confirmation dialog
- Favorites: mark ads with the heart in the list or on the detail screen
- Status: mark an ad as sold on the detail screen (and make it available again); the list then shows "Verkauft" (sold)
- Filters below the search field: favorites, only available ads and category, combinable with the search
- Sorting via the icon at the top right: newest first, price ascending or descending, title A–Z
- Product photo with the device camera (asks for the camera permission; without it the ad can still be saved)
- Photo from the gallery via the Android photo picker (needs no permission). The photo is copied into the app storage, so it stays even if it is deleted from the gallery
- Full screen photo: tap the photo on the detail screen
- Local storage: all data is kept after a restart. Six sample ads are created on the first start.

## Requirements

| What | Version |
|---|---|
| Android Studio | A current version that supports AGP 9.4 (tested with build `AI-262.9437.185`) |
| Android SDK | Platform **API 37** (Android Studio offers to install it during the first sync) |
| Device or emulator | Android **8.0 (API 26)** or newer |
| Java | No separate JDK needed, see below |
| Internet | Only for the **first** build, to download Gradle, the libraries and the JDK |

**About the JDK:** Gradle builds with **Java 25**. This is set in `gradle/gradle-daemon-jvm.properties`. Android Studio already includes Java 25, and on the command line Gradle downloads it automatically during the first build. Any installed Java (tested from Java 8) is enough to start `gradlew`.

## Build and run in Android Studio

1. Clone the repository:

   ```
   git clone https://github.com/bennipauly/CampusMarket.git
   ```

2. Open Android Studio → **File → Open** → select the `CampusMarket` folder → trust the project ("Trust Project").
3. Wait until the **Gradle Sync** has finished. Android Studio creates the file `local.properties` with the path to the Android SDK. If API 37 is missing, accept the offered installation.
4. Select a device at the top (an emulator from the **Device Manager** or a phone with USB debugging).
5. Click **Run ▶**. The app starts with the list of ads and six sample ads.

## Build on the command line

The command line needs the path to the Android SDK. Either open the project once in Android Studio (this creates `local.properties`) or set the environment variable `ANDROID_HOME` to the SDK folder (on Windows usually `%LOCALAPPDATA%\Android\Sdk`).

In the project folder (on Windows use `gradlew.bat` instead of `./gradlew`):

```
./gradlew assembleDebug          # build the APK: app/build/outputs/apk/debug/app-debug.apk
./gradlew installDebug           # install the APK on a connected device or emulator
./gradlew testDebugUnitTest      # run the JUnit tests
./gradlew lintDebug              # run Android Lint: app/build/reports/lint-results-debug.html
```

## Tests

The logic without Android dependencies is tested with JUnit (`app/src/test`):

| Test class | Checks |
|---|---|
| `AdSearchTest` | Search: empty search text, upper/lower case, matches in title/description/location, no matches |
| `AdFilterTest` | Filters: category, only favorites, only available, all together, no matches |
| `AdSortTest` | Sorting: newest first, price ascending/descending, titles with umlauts, equal prices keep their order |
| `AdValidationTest` | Input validation: title, handover location, price (e.g. "12,50" valid; "-5", "1e3", "12,505" invalid) and the price as text for editing |
| `PriceFormatTest` | Price display in German format ("12,50 €") |

In Android Studio: right-click on `app/src/test/java` → **Run 'Tests in …'**.

## Emulator notes

- **Camera:** The emulator camera returns a test image. To cancel without a photo, use the back gesture or the ◁ button in the emulator side bar.
- If the emulator crashes when opening the camera: **Device Manager → ⋮ → Edit → Advanced Settings → Back camera: Emulated**, then **Cold Boot Now**.
- **Gallery:** A fresh emulator has no pictures. Drag and drop an image file onto the emulator window (it is saved in the "Download" folder) or take a picture once with the emulator's camera app. On newer Android versions the selection in the photo picker has to be confirmed with **Done**.
- **Reset the sample data:** Settings → Apps → CampusMarket → Storage → Clear storage.
- **Show the logs:** In Android Studio open **Logcat** at the bottom and filter by `package:mine`. The app logs errors and important actions (e.g. sorting, favorite changed) with the class name as tag, e.g. `AdListViewModel`.

## Known limitations

- Search, filters and sorting are not stored: after an app restart the list shows all ads again, newest first.
- When a photo is replaced or removed, or an ad is deleted, the old photo file stays in the app storage. It is not visible in the app; clearing the app storage removes it.

## Code structure

Layers: **UI → ViewModel → Repository → Room (DAO)**. Every screen consists of a route (gets the ViewModel), a stateless screen composable and a ViewModel with a `UiState` class.

```
app/src/main/java/de/dhbw/campusmarket/
├── CampusMarketApplication.kt   database and repository (exactly once), sample data
├── MainActivity.kt              sets the theme and the NavHost
├── data/                        Room: entity Ad, AdDao, AppDatabase, AdRepository, enums, sample data
├── logic/                       pure Kotlin functions: search, filters, sorting, input validation (with JUnit tests)
└── ui/
    ├── navigation/              type-safe destinations and NavHost
    ├── adlist/                  list of ads with search, filters and sorting
    ├── addetail/                detail screen with favorite, status, edit and delete
    ├── adform/                  form for creating and editing, camera and gallery
    ├── photoviewer/             full screen photo
    └── theme/                   colors, typography, theme
```

The rules for architecture and code are in [CLAUDE.md](CLAUDE.md) (in German).

## Use of AI

AI (Claude Code) was used during development. All prompts, the actions of the AI and the commits that contain the results are listed in [AI_USAGE.md](AI_USAGE.md) (in German).

Die Gruppe verantwortet jede Zeile des Projekts. (The group is responsible for every line of the project.)
