# Wanbo T2 Max (T2 M+) · Guia de Otimização e Debloat

<p align="center">
  <img src="wanbo_t2_max_device.jpg" alt="Wanbo T2 Max Projector" width="280" style="border-radius: 10px;">
</p>

Perfil de otimização, debloat seguro, scripts de automação e launcher ultraleve (16 KB) desenvolvido especificamente para o projetor **Wanbo T2 Max** (plataforma **MStar Mainz / msd358** com Android 6.0 Marshmallow).

> 📖 **Para o manual técnico completo e aprofundado, acesse:** [`PASSO-A-PASSO.md`](PASSO-A-PASSO.md)

---

## 📸 Antes e Depois

| Captura Inicial (Tela Azul) | Antes (Launcher de Fábrica Zeasn) | Depois (Wanbo Home Limpo) |
| :---: | :---: | :---: |
| ![Captura Inicial](initial_blue_screen.png) | ![Antes](before_zeasn.png) | ![Depois](after.png) |

### 🚀 Com Aplicativos Instalados (Ex: Saimo TV) & Horário Corrigido
| Grade de Apps Completa | Horário e Fuso Corrigidos (UTC-3) |
| :---: | :---: |
| ![Saimo TV](with_saimotv.png) | ![Horário Corrigido](time_fixed.png) |

---

## 📊 Resultados Medidos no Aparelho

* **Memória RAM Livre:** Saltou de **173 MB** para **252 MB** (**+45% de RAM disponível**).
* **Tempo de Resposta da Interface:** Animações reduzidas para `0.0x`, resposta instantânea ao controle remoto.
* **Tamanho do Launcher Novo:** Apenas **16 KB** compilado em Java puro (contra dezenas de megabytes do launcher original).
* **Fuso Horário Corrigido:** Ajustado para `America/Bahia` (UTC-3) contornando o bug histórico do Android 6 que ativava horário de verão extinto.

---

## 🔍 Como Descobrir a Versão do Seu Projetor

O Wanbo T2 Max foi produzido em versões diferentes:
1. **Wanbo T2 Max Clássico (`T2 M+`)**: Plataforma MStar Mainz `msd358`, Android 6.0 Marshmallow. É a versão deste perfil.
2. **Wanbo T2 Max Atualizado (Blue/Focus)**: Plataforma Amlogic ou Allwinner com Android 9.0 Pie.

Para consultar a sua versão:
```bash
adb shell getprop ro.product.model
adb shell getprop ro.board.platform
adb shell getprop ro.build.version.release
```

---

## 🧠 Aplicando em Outros Modelos de Projetor

Se você possui outro modelo (como **HY300, HY320, Wanbo T6, Formovie, Dangbei, Xgimi, Magcubic**):
A mesma lógica é aplicada:
1. Conectar via rede ADB na porta 5555.
2. Fazer backup integral de partições e APKs antes de modificar.
3. Preservar rigorosamente os pacotes que controlam a correção de trapézio (Keystone 4D) e chaveamento de entrada HDMI.
4. Instalar um launcher leve testado para a API Android do seu aparelho.
5. Desativar os bloatwares sem risco de brick via `pm disable-user --user 0`.

---

## 🤖 Compatível com Agentes de IA

Este repositório foi construído para ser operado com o seu assistente de IA favorito:
* **Google Antigravity / Gemini CLI**
* **Claude Code**
* **OpenAI Codex / ChatGPT / Aider**
* **Cursor / VS Code AI**

Basta pedir para o agente:
> *"Conecte ao meu projetor via ADB no IP especificado, siga as instruções em examples/wanbo-t2-max/PASSO-A-PASSO.md, faça o backup de segurança, instale o launcher de 16KB e aplique o debloat seguro mantendo os aplicativos de trapézio e HDMI."*

---

## 📂 Conteúdo Deste Diretório

- **[`PASSO-A-PASSO.md`](PASSO-A-PASSO.md)**: Manual detalhado de manutenção, backup e debloat.
- **[`wanbo_t2_max_device.jpg`](wanbo_t2_max_device.jpg)**: Foto oficial do aparelho para identificação.
- **[`launcher/`](launcher/)**: Código-fonte Java completo, script de build e APK compilado de 16 KB (`wanbo-launcher.apk`).
- **[`scripts/`](scripts/)**:
  - `backup.sh`: Script de backup completo de partições MStar, APKs e configurações.
  - `debloat.sh`: Script de automação para desativação de bloatwares e ajuste de animações.
  - `restore.sh`: Script de reversão (rollback) para reativar todos os pacotes.
  - `fix_timezone.sh`: Script para ajuste e sincronização do fuso horário brasileiro no Android 6.
- **[`docs/`](docs/)**: Cópia integral do guia de referência base em PDF (*Projetor HY300*, goIAgo).
- **[`debloat.txt`](debloat.txt)**: Lista exata dos pacotes removidos e mantidos.
- **[`tv.json`](tv.json)**: Metadados estruturados do modelo para integração no CleanYourTV.

---

## 🛡️ O Que Foi Mantido Intacto (Hardware e Recursos)
* **Zoom e Keystone 4D (Correção de Trapézio):** `com.example.zoomsettings` mantido com atalho direto no topo.
* **Entrada HDMI e AV:** `com.mstar.tv.tvplayer.ui` mantido com atalho direto no topo.
* **Drivers de Imagem e Áudio:** MStar TV Services intactos.
* **Espelhamento de Tela:** EShare, AirPlay e Wi-Fi Display funcionais.
* **Google Play Store & Reprodutores de Mídia:** 100% preservados.

---

## 🛑 O Que Foi Removido / Desativado (Bloatware)
Consulte o arquivo [`debloat.txt`](debloat.txt) para a lista exata dos pacotes removidos (Loja Zeasn, telemetria, AppKiller, ferramentas de calibração de fábrica e live wallpapers pesados).

---

## 🙏 Créditos e Referências

* **Documento Base de Referência:** *"Projetor HY300: limpeza e otimização com Claude Code"* por **goIAgo / Iago** ([goiago.com](https://goiago.com)), Versão 1.0 (Novembro de 2026).
* **Gathin23** ([CleanYourTV](https://github.com/Gathin23/cleanyourtv)) — Estrutura e metodologia de debloat para TVs e projetores Android.
* **DLMIDIA / Daniel Silva** ([github.com/DLMIDIA](https://github.com/DLMIDIA)) — Diagnóstico, engenharia reversa do Wanbo T2 Max, criação do launcher personalizado em Java puro, correção do fuso horário brasileiro e documentação passo a passo.
