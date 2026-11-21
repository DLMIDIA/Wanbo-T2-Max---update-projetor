#!/bin/bash
# Correção do bug de fuso horário / horário de verão brasileiro no Android 6
set -e

ADB="${ADB:-adb}"
TZ="${1:-America/Bahia}"

echo "=== AJUSTANDO FUSO HORÁRIO NO WANBO T2 MAX ==="
echo "Configurando para: $TZ (UTC-3 contínuo sem horário de verão extinto)"

$ADB shell settings put global auto_time_zone 0 || true
$ADB shell setprop persist.sys.timezone "$TZ" || true

echo "Data e hora atual no dispositivo:"
$ADB shell date
