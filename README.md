# NEXUS Android

A lightweight Android WebView wrapper around the NEXUS single-page interface in `app/src/main/assets/index.html`.

## Build the APK
The GitHub Actions workflow **Build Android APK** builds a debug APK on every push to `main` and publishes it as a prerelease asset.

- APK asset: `app-debug.apk`
- Package: `com.baijuraval.nexus`
- Minimum Android version: Android 6.0 (API 23)

## Local build
Install JDK 17, Gradle, and Android SDK platform/build-tools 35, then run:

```sh
gradle assembleDebug
```

The APK will be at `app/build/outputs/apk/debug/app-debug.apk`.
