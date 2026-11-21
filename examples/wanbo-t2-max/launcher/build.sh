#!/bin/bash
# Script de compilação autônomo para o Wanbo Home Launcher (16 KB)
# Requisitos: Android SDK (aapt2, d8, zipalign, apksigner, android.jar)
set -e

cd "$(dirname "$0")"

# Localizar Android SDK
if [ -n "$ANDROID_HOME" ]; then
    SDK="$ANDROID_HOME"
elif [ -n "$ANDROID_SDK_ROOT" ]; then
    SDK="$ANDROID_SDK_ROOT"
elif [ -d "$HOME/Library/Android/sdk" ]; then
    SDK="$HOME/Library/Android/sdk"
elif [ -d "$HOME/Android/Sdk" ]; then
    SDK="$HOME/Android/Sdk"
else
    echo "Erro: ANDROID_HOME não configurado."
    exit 1
fi

# Detectar última versão de build-tools
BT=$(find "$SDK/build-tools" -mindepth 1 -maxdepth 1 -type d | sort -V | tail -n 1)
# Detectar última plataforma instalada
PLATFORM=$(find "$SDK/platforms" -name "android.jar" | sort -V | tail -n 1)

if [ -z "$BT" ] || [ -z "$PLATFORM" ]; then
    echo "Erro: build-tools ou android.jar não encontrados no SDK ($SDK)."
    exit 1
fi

echo "=== COMPILANDO WANBO HOME LAUNCHER ==="
echo "Build Tools: $BT"
echo "Android JAR: $PLATFORM"

rm -rf build && mkdir -p build/classes

echo "[1/6] Processando manifest com aapt2..."
"$BT/aapt2" link -o build/unsigned.apk -I "$PLATFORM" --manifest AndroidManifest.xml

echo "[2/6] Compilando classes Java..."
javac -g -source 1.8 -target 1.8 -cp "$PLATFORM" -d build/classes src/com/wanbo/launcher/*.java

echo "[3/6] Convertendo para DEX com d8..."
"$BT/d8" --release --lib "$PLATFORM" --min-api 21 --output build build/classes/com/wanbo/launcher/*.class

echo "[4/6] Adicionando classes.dex ao pacote..."
(cd build && zip -u unsigned.apk classes.dex >/dev/null)

echo "[5/6] Alinhando pacote com zipalign..."
"$BT/zipalign" -f -p 4 build/unsigned.apk build/aligned.apk

echo "[6/6] Assinando pacote com apksigner..."
if [ ! -f wanbo.jks ]; then
    keytool -genkeypair -keystore wanbo.jks -storepass wanbo123 -keypass wanbo123 \
        -alias wanbo -keyalg RSA -keysize 2048 -validity 10000 -dname "CN=Wanbo" 2>/dev/null
fi

"$BT/apksigner" sign --ks wanbo.jks --ks-pass pass:wanbo123 --out wanbo-launcher.apk build/aligned.apk

echo "=== SUCESSO! APK GERADO: $(pwd)/wanbo-launcher.apk ==="
ls -lh wanbo-launcher.apk
