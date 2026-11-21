#!/bin/bash
# Script de restauração / rollback para Wanbo T2 Max
set -e

ADB="${ADB:-adb}"

echo "=== RESTAURANDO PACOTES DE FÁBRICA: WANBO T2 MAX ==="

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
    echo "  Reativando: $pkg..."
    $ADB shell pm enable "$pkg" 2>/dev/null || true
done

echo "Restaurando escalas de animação..."
$ADB shell settings put global window_animation_scale 1.0 || true
$ADB shell settings put global transition_animation_scale 1.0 || true
$ADB shell settings put global animator_duration_scale 1.0 || true

echo "=== RESTAURAÇÃO CONCLUÍDA ==="
