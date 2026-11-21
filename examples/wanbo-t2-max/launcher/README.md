# Wanbo Home Launcher (16 KB)

Launcher nativo, ultraleve e desenvolvido especificamente para projetores **Wanbo T2 Max (T2 M+)** com plataforma **MStar Mainz (msd358)** e **Android 6.0 Marshmallow (API 23)**.

---

## 🚀 Principais Destaques

- **Tamanho Mínimo:** Apenas **16 KB** de APK final assinado.
- **Boot Instantâneo:** Carrega em ~50ms, sem travamentos ou tela preta.
- **Consumo de Memória:** Menos de 8 MB de RAM em execução (contra >50 MB do Zeasn stock).
- **Sem Dependências Pesadas:** Desenvolvido em Java puro sem AndroidX, Jetpack ou bibliotecas externas.
- **Navegação por Controle Remoto (D-Pad):** Foco suave com escala de 1.08x e contorno laranja oficial Wanbo (`#FF7A00`).
- **Atalhos Rápidos de Hardware no Cabeçalho:**
  - **HDMI / Entradas:** Abre diretamente o reprodutor nativo MStar (`com.mstar.tv.tvplayer.ui/.RootActivity`).
  - **Trapézio / Foco:** Abre a ferramenta de calibração 4D Keystone da MStar (`com.example.zoomsettings/.MainActivity`).
  - **Configurações:** Abre o painel do sistema Android (`com.android.settings/.Settings`).
- **Relógio e Data:** Formato 24h e data por extenso em português.
- **Grade Dinâmica de Aplicativos:** Detecta automaticamente todos os aplicativos `LEANBACK_LAUNCHER` e `LAUNCHER` padrão.

---

## 🛠️ Como Compilar

Caso queira alterar as cores, títulos ou atalhos:

```bash
chmod +x build.sh
./build.sh
```

O script utiliza as ferramentas oficiais do Android SDK (`aapt2`, `javac`, `d8`, `zipalign`, `apksigner`) e gera o arquivo `wanbo-launcher.apk` assinado e pronto para instalação.

---

## 📲 Como Instalar via ADB

```bash
# 1. Instalar o launcher
adb install -r wanbo-launcher.apk

# 2. Pressionar o botão Home do controle e selecionar Wanbo Home como "Sempre"
# Ou disparar a Intent via ADB:
adb shell am start -a android.intent.action.MAIN -c android.intent.category.HOME
```
