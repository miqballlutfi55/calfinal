name: Build APK Otomatis Pintar

on:
  workflow_dispatch:

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
    - name: Ambil File
      uses: actions/checkout@v4

    - name: Setup Java
      uses: actions/setup-java@v4
      with:
        distribution: 'zulu'
        java-version: '17'

    - name: Ekstrak & Build APK
      run: |
        # Cek dan ekstrak file ZIP
        if ls *.zip 1> /dev/null 2>&1; then
          echo "Mengekstrak file..."
          unzip -q -o "*.zip"
        fi
        
        # Pindah ke folder Android
        GRADLE_DIR=$(dirname $(find . -name "gradlew" | head -1))
        cd "$GRADLE_DIR"
        
        # Izin eksekusi
        chmod +x gradlew
        
        # Rakit APK dengan Mode Hemat RAM (Maksimal 2GB RAM & Tanpa Daemon)
        ./gradlew assembleDebug --no-daemon -Dorg.gradle.jvmargs="-Xmx2g"

    - name: Serahkan APK ke User
      uses: actions/upload-artifact@v4
      with:
        name: Kalkulator-SMC-Tinggal-Install
        path: "**/app/build/outputs/apk/debug/*.apk"
