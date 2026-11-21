# 📽️ Wanbo T2 Max (T2 M+) · Atualização, Limpeza e Novo Launcher

Projeto completo de debloat (remoção de anúncios e bloatwares), otimização de memória RAM (+45% de ganho real) e desenvolvimento de launcher ultraleve (16 KB) para o projetor **Wanbo T2 Max** (plataforma **MStar Mainz / msd358** com Android 6.0 Marshmallow).

> 📚 **Guia Passo a Passo Completo:** Veja [`examples/wanbo-t2-max/PASSO-A-PASSO.md`](examples/wanbo-t2-max/PASSO-A-PASSO.md)  
> 📑 **Documento Base de Referência (PDF):** [`examples/wanbo-t2-max/docs/guia-referencia-hy300-claude-code.pdf`](examples/wanbo-t2-max/docs/guia-referencia-hy300-claude-code.pdf) (*Projetor HY300: limpeza e otimização com Claude Code*, por goIAgo, nov/2026)  
> 🌐 **Repositório Oficial:** [github.com/DLMIDIA/Wanbo-T2-Max---update-projetor](https://github.com/DLMIDIA/Wanbo-T2-Max---update-projetor)

---

## 📸 Antes e Depois

| Captura Inicial (Tela Azul) | Antes (Launcher de Fábrica) | Depois (Novo Launcher Wanbo Home Limpo) |
| :---: | :---: | :---: |
| ![Captura Inicial](examples/wanbo-t2-max/initial_blue_screen.png) | ![Antes](examples/wanbo-t2-max/before_zeasn.png) | ![Depois](examples/wanbo-t2-max/after.png) |

### 🚀 Com Aplicativos Instalados (Ex: Saimo TV) & Horário Corrigido
| Grade de Apps Completa | Fuso e Relógio Corrigidos |
| :---: | :---: |
| ![Saimo TV](examples/wanbo-t2-max/with_saimotv.png) | ![Horário Corrigido](examples/wanbo-t2-max/time_fixed.png) |

---

## ⚡ Resultados Medidos no Aparelho

* **Memória RAM Livre:** Saltou de **173 MB** para **252 MB** (**+45% de memória livre disponível**).
* **Tempo de Resposta da Interface:** Escala de animação zerada (`0.0x`), resposta instantânea aos comandos do controle remoto.
* **Tamanho do Launcher Novo:** Apenas **16 KB** compilado em Java puro (inicialização em ~50ms, sem bibliotecas pesadas).
* **Fuso Horário Corrigido:** Ajustado para `America/Bahia` (UTC-3), eliminando o bug histórico do Android 6 que adicionava 1 hora de horário de verão extinto.

---

## 📂 Estrutura do Projeto

```
.
├── examples/wanbo-t2-max/
│   ├── README.md               # Visão geral do perfil Wanbo T2 Max
│   ├── PASSO-A-PASSO.md        # Guia completo de manutenção e engenharia reversa
│   ├── tv.json                 # Metadados do modelo e hardware
│   ├── debloat.txt             # Lista exata de pacotes desativados
│   ├── before_zeasn.png        # Print do estado de fábrica
│   ├── after.png               # Print do estado limpo com Wanbo Home
│   ├── with_saimotv.png        # Print com Saimo TV e apps instalados
│   ├── time_fixed.png          # Print comprovando horário e data sincronizados
│   ├── docs/
│   │   └── guia-referencia-hy300-claude-code.pdf # PDF de referência base (goIAgo)
│   ├── launcher/
│   │   ├── AndroidManifest.xml # Configuração com LEANBACK e HOME
│   │   ├── build.sh            # Script autônomo de compilação SDK
│   │   ├── wanbo-launcher.apk  # Binário compilado e assinado de 16 KB
│   │   └── src/com/wanbo/launcher/Home.java # Código-fonte Java nativo
│   └── scripts/
│       ├── backup.sh           # Backup completo de partições MStar e APKs
│       ├── debloat.sh          # Execução do debloat e otimização
│       ├── restore.sh          # Rollback e restauração de fábrica
│       └── fix_timezone.sh     # Correção de fuso horário brasileiro
```

---

## 🛡️ O Que Foi Mantido Intacto (Hardware e Recursos)
Diferente de scripts genéricos de TV que quebram projetores, mantivemos as funções essenciais do hardware MStar:
* **Zoom e Keystone 4D (Correção de Trapézio):** `com.example.zoomsettings` mantido com atalho direto no topo.
* **Entrada HDMI e AV:** `com.mstar.tv.tvplayer.ui` mantido com atalho direto no topo.
* **Drivers de Imagem e Áudio:** MStar TV Services intactos.
* **Espelhamento de Tela:** EShare, AirPlay e Wi-Fi Display funcionais.
* **Google Play Store & VLC:** 100% preservados.

---

## 🛑 O Que Foi Removido / Desativado (Bloatware)
Consulte o arquivo [`examples/wanbo-t2-max/debloat.txt`](examples/wanbo-t2-max/debloat.txt) para a lista exata dos pacotes removidos:
* Loja paralela Zeasn (`cn.zeasn.toptech.projector.msd358.vstoresubclient`)
* Launcher padrão com anúncios (`cn.zeasn.launcher.vstoresubclient.mstar358`)
* Telemetria Zeasn (`com.zeasn.services.general`)
* AppKiller em segundo plano (`toptech.mid.sugar.appkillerservice`)
* Ferramentas de fábrica e calibração (`com.toptech.factorytools`, `mstar.factorymenu.ui`)
* Teclado chinês Pinyin e serviço de impressão (`com.android.inputmethod.pinyin`, `com.android.printspooler`)
* Live Wallpapers pesados (`galaxy4`, `phasebeam`, `noisefield`, `livepicker`)

---

## 📱 Novo Launcher (Wanbo Home)
O código fonte do launcher está disponível em [`examples/wanbo-t2-max/launcher/`](examples/wanbo-t2-max/launcher/):
* Desenvolvido em Java puro sem Gradle para máxima leveza.
* Compilado nativamente com Android Build-Tools e compatível com Android 6.0 (API 21+).
* Mapeamento nativo do botão HOME do controle remoto.
* Atalhos diretos de hardware: HDMI, Keystone 4D e Configurações do Sistema.

---

## 🚀 Como Executar no Seu Aparelho

1. Conecte ao projetor via rede:
   ```bash
   adb connect <IP_DO_PROJETOR>:5555
   ```
2. Realize o backup de segurança:
   ```bash
   cd examples/wanbo-t2-max/scripts
   ./backup.sh
   ```
3. Execute o debloat e otimização:
   ```bash
   ./debloat.sh
   ```
4. Corrija o horário brasileiro (se necessário):
   ```bash
   ./fix_timezone.sh America/Bahia
   ```

---

## 🙏 Créditos e Agradecimentos

* **DLMIDIA / Daniel Silva** ([github.com/DLMIDIA](https://github.com/DLMIDIA)) — Diagnóstico, engenharia reversa do Wanbo T2 Max, criação do launcher personalizado em Java puro, correção do fuso horário brasileiro e documentação passo a passo.
* **goIAgo / Iago** ([goiago.com](https://goiago.com)) — Pelo excelente guia base do projetor HY300 (*Projetor HY300: limpeza e otimização com Claude Code*, v1.0, 2026).
* **Gathin23** ([CleanYourTV](https://github.com/Gathin23/cleanyourtv)) — Pela estrutura e metodologia de debloat de Smart TVs.
