# NEXUS Android

A lightweight Android WebView wrapper that loads the NEXUS interface directly from the repository's hosted raw HTML.

## Update the app without rebuilding
Edit and commit `app/src/main/assets/index.html` on the `main` branch. Installed apps will load the updated HTML the next time they start with an internet connection. HTML/CSS/JavaScript-only changes do not require a new APK.

Hosted page:
`https://raw.githubusercontent.com/BaijuRaval/Nexus/main/app/src/main/assets/index.html`

## Build the APK
The GitHub Actions workflow **Build Android APK** builds a debug APK on every push to `main` and publishes it as a prerelease asset.

- APK asset: `app-debug.apk`
- Package: `com.baijuraval.nexus`
- Minimum Android version: Android 6.0 (API 23)
- Internet access is required to load the hosted interface.

## Local build
Install JDK 17, Gradle, and Android SDK platform/build-tools 35, then run:

```sh
gradle assembleDebug
```

The APK will be at `app/build/outputs/apk/debug/app-debug.apk`.
