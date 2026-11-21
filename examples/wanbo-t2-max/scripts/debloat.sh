#!/bin/bash
# Script de debloat e otimização para Wanbo T2 Max (Android 6.0 / MStar Mainz)
set -e

ADB="${ADB:-adb}"

echo "=== INICIANDO DEBLOAT E OTIMIZAÇÃO: WANBO T2 MAX ==="

# 1. Instalar o Wanbo Home Launcher caso ainda não esteja instalado
if [ -f "../launcher/wanbo-launcher.apk" ]; then
    echo "[1/4] Instalando Wanbo Home Launcher..."
    $ADB install -r "../launcher/wanbo-launcher.apk" || true
fi

# 2. Desativar bloatwares e telemetria
echo "[2/4] Desativando aplicativos indesejados e telemetria..."
PACKAGES=(
    "cn.zeasn.launcher.vstoresubclient.mstar358"
    "cn.zeasn.toptech.projector.msd358.vstoresubclient"
    "com.zeasn.services.general"
    "toptech.mid.sugar.appkillerservice"
    "com.toptech.factorytools"
    "mstar.factorymenu.ui"
    "com.android.inputmethod.pinyin"
    "com.android.printspooler"
    "com.android.galaxy4"
    "com.android.phasebeam"
    "com.android.noisefield"
    "com.android.wallpaper.livepicker"
)

for pkg in "${PACKAGES[@]}"; do
    echo "  Desativando: $pkg..."
    $ADB shell pm disable-user --user 0 "$pkg" 2>/dev/null || $ADB shell pm disable "$pkg" 2>/dev/null || true
done

# 3. Otimizações de renderização e animações
echo "[3/4] Acelerando animações da interface (0.0x)..."
$ADB shell settings put global window_animation_scale 0.0 || true
$ADB shell settings put global transition_animation_scale 0.0 || true
$ADB shell settings put global animator_duration_scale 0.0 || true

# 4. Ativar Wanbo Home como Home padrão
echo "[4/4] Definindo Wanbo Home como Launcher principal..."
$ADB shell am start -a android.intent.action.MAIN -c android.intent.category.HOME || true

echo "=== PROCESSO CONCLUÍDO COM SUCESSO! ==="
$ADB shell dumpsys meminfo | head -n 15
