#!/bin/bash
# Backup completo para Wanbo T2 Max (MStar msd358 / Android 6.0)
# Baseado na metodologia goIAgo HY300 v1.0 adaptada para MStar Mainz
set -e

ADB="${ADB:-adb}"
DEST="${1:-./backup-wanbo-$(date +%Y%m%d)}"

echo "=== INICIANDO BACKUP COMPLETO DO WANBO T2 MAX ==="
echo "Destino: $DEST"
mkdir -p "$DEST/firmware" "$DEST/apks" "$DEST/data" "$DEST/info"

echo "[1/5] Coletando propriedades e pacotes do sistema..."
$ADB shell "pm list packages -f -u" > "$DEST/info/packages.txt"
$ADB shell "pm list packages -d" > "$DEST/info/disabled.txt" 2>/dev/null || true
for ns in global secure system; do
    $ADB shell "settings list $ns" > "$DEST/info/settings-$ns.txt" 2>/dev/null || true
done
$ADB shell getprop > "$DEST/info/getprop.txt"
$ADB shell "cat /proc/partitions" > "$DEST/info/partitions.txt"

echo "[2/5] Fazendo backup de APKs de terceiros..."
for pkg in $($ADB shell pm list packages -3 | tr -d '\r' | sed 's/package://'); do
    for p in $($ADB shell pm path "$pkg" | tr -d '\r' | sed 's/package://'); do
        mkdir -p "$DEST/apks/$pkg"
        $ADB pull "$p" "$DEST/apks/$pkg/" 2>/dev/null || true
    done
done

echo "[3/5] Fazendo backup dos APKs alvos de debloat..."
SYSTEM_TARGETS=(
    "cn.zeasn.toptech.projector.msd358.vstoresubclient"
    "com.zeasn.services.general"
    "cn.zeasn.launcher.vstoresubclient.mstar358"
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

for pkg in "${SYSTEM_TARGETS[@]}"; do
    for p in $($ADB shell pm path "$pkg" | tr -d '\r' | sed 's/package://'); do
        mkdir -p "$DEST/apks/$pkg"
        $ADB pull "$p" "$DEST/apks/$pkg/" 2>/dev/null || true
    done
done

echo "[4/5] Fazendo backup das partições vitais do firmware MStar..."
PART_DIR="/dev/block/platform/mstar_mci.0/by-name"
for part in $($ADB shell "ls $PART_DIR" 2>/dev/null | tr -d '\r'); do
    case "$part" in
        userdata|cache|mmcblk0*)
            echo "  Pulando partição de dados do usuário: $part"
            continue
            ;;
    esac
    echo "  Extraindo partição $part..."
    $ADB exec-out "cat $PART_DIR/$part" > "$DEST/firmware/${part}.img" 2>/dev/null || true
done

echo "[5/5] Gerando somas de verificação SHA-256..."
(
    cd "$DEST"
    find . -type f ! -name "SHA256SUMS.txt" -print0 | xargs -0 shasum -a 256 > SHA256SUMS.txt 2>/dev/null || true
)

echo "=== BACKUP CONCLUÍDO COM SUCESSO! ==="
du -sh "$DEST"/*
