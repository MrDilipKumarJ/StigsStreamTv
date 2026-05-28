# 📺 Stigstream Android TV App

A fully native Android TV app for Stigstream — Watch Free Movies & TV Shows.

## ✨ Features

| Feature | Details |
|---|---|
| 🏠 Home Screen | Leanback rows: Featured, Trending, New Releases, Action, Comedy, Horror, Sci-Fi, TV Shows |
| 🎬 Movie Detail | Full backdrop, poster, metadata, Watch Now / Trailer / Watchlist actions |
| ▶️ Video Player | ExoPlayer/Media3 with HLS, DASH, MP4 support |
| 🔍 Search | Real-time D-pad friendly search with debounce |
| 🎮 TV Remote | Full D-pad navigation, play/pause, seek ±10s, back |
| 🖼️ Dynamic Colors | Palette API extracts poster colors for UI theming |
| ⚡ Splash Screen | Branded loading screen with red/black Stigstream theme |

## 🚀 Getting Started

### Requirements
- Android Studio Hedgehog or later
- Android SDK 21+ (minSdk), 34 (targetSdk)
- Java 17
- Gradle 8.3

### Setup

1. **Open in Android Studio**
   ```
   File → Open → Select StigstreamTV folder
   ```

2. **Sync Gradle**
   ```
   Android Studio will auto-prompt — click "Sync Now"
   ```

3. **Connect your Android TV device or emulator**
   - Physical TV: Enable ADB over network in Developer Options
   - Emulator: Use Android TV system image (API 29+)

4. **Run the app**
   ```
   Run → Run 'app'  (or Shift+F10)
   ```

## 🔌 Connecting to Stigstream Content

The app is pre-built with sample data. To connect to live Stigstream content:

### Option A — Stigstream API (if available)
Edit `StigstreamRepository.kt`:
```kotlin
const val BASE_URL = "https://stigstream.com"  // already set
```
If Stigstream has a REST API, replace the mock functions with Retrofit calls:
```kotlin
suspend fun getTrendingMovies(): List<Movie> {
    return api.getTrending().movies  // your API call
}
```

### Option B — Web Scraping with Jsoup
Add to `app/build.gradle`:
```gradle
implementation 'org.jsoup:jsoup:1.17.2'
```
Then scrape stigstream.com pages for movie data.

### Option C — WebView for playback
The stream URL in each movie points to `https://stigstream.com/watch/{id}`.
The PlayerActivity already supports loading these URLs via ExoPlayer. If
direct stream extraction is needed, you can use a WebView as a fallback
by loading the page and extracting the video source.

## 📁 Project Structure

```
app/src/main/
├── AndroidManifest.xml          # TV manifest with Leanback launcher
├── java/com/stigstream/tv/
│   ├── data/
│   │   ├── model/Movie.kt       # Data models
│   │   └── repository/          # Data source (API / scraper)
│   └── ui/
│       ├── splash/              # Branded splash screen
│       ├── home/                # Leanback BrowseFragment + card presenter
│       ├── detail/              # Movie detail with actions
│       ├── player/              # ExoPlayer full-screen playback
│       └── search/              # Leanback SearchFragment
└── res/
    ├── layout/                  # TV-optimized layouts
    ├── values/                  # Colors, strings, themes, dimens
    └── drawable/                # Icons, banners, placeholders
```

## 🎨 Branding

The app uses Stigstream's brand colors:
- **Primary red**: `#E50914`
- **Background**: `#0D0D0D` (near-black)
- **Cards**: `#1A1A1A`

To update branding, edit `res/values/colors.xml`.

## 📺 TV Remote Controls

| Button | Action |
|---|---|
| D-pad center / OK | Select / Play-Pause |
| D-pad left/right | Navigate cards / Seek ±10s in player |
| D-pad up/down | Navigate rows |
| Back | Go back / Stop player |
| Search button | Open search |

## 🏗️ Build Release APK

```bash
./gradlew assembleRelease
```

Output: `app/build/outputs/apk/release/app-release.apk`

> **Note**: Sign your APK before publishing to Google Play for Android TV.

## 📦 Dependencies

- **androidx.leanback** — TV UI framework (BrowseFragment, DetailsFragment, SearchFragment)
- **androidx.media3 / ExoPlayer** — Video playback (HLS, DASH, MP4)
- **Glide** — Image loading & caching
- **Retrofit + OkHttp** — Network calls
- **Palette** — Dynamic color extraction from posters
- **Kotlin Coroutines** — Async data loading

## 🛠️ Troubleshooting

**App not appearing in TV launcher?**
→ Make sure `LEANBACK_LAUNCHER` intent filter is in Manifest ✓

**Videos not playing?**
→ Check `android:usesCleartextTraffic="true"` in Manifest ✓
→ Verify stream URLs are valid HLS/DASH/MP4 links

**Images not loading?**
→ Check internet permission in Manifest ✓
→ Confirm poster URLs are accessible

## 📄 License

Built for Stigstream. All movie metadata and streams are property of their respective owners.
