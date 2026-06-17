<h1 align="center">🔑 Key Hunt</h1>
 
<hr>

<h2>📝 Descrição</h2>

<p>
Key Hunt é um jogo 2D desenvolvido em Java com Programação Orientada a Objetos.
O objetivo é explorar um mapa labiríntico, coletar as 7 chaves espalhadas pelo cenário, enfrentar ou escapar dos inimigos que te perseguem e encontrar a saída.
</p>

<p>
O projeto aplica os principais conceitos de POO — herança, polimorfismo, encapsulamento, classes abstratas — além de recursos avançados da Java 2D API como animações por sprite sheet, sistema de câmera, efeito de escuridão com gradiente radial e renderização de tiles.
</p>

<hr>

<h2>🎮 Como Jogar</h2>

<table>
  <tr><th>Tecla</th><th>Ação</th></tr>
  <tr><td><code>W</code> / <code>↑</code></td><td>Mover para cima</td></tr>
  <tr><td><code>S</code> / <code>↓</code></td><td>Mover para baixo</td></tr>
  <tr><td><code>A</code> / <code>←</code></td><td>Mover para a esquerda</td></tr>
  <tr><td><code>D</code> / <code>→</code></td><td>Mover para a direita</td></tr>
  <tr><td><code>Espaço</code></td><td>Atacar inimigos</td></tr>
</table>

<p>
Colete as <strong>7 chaves</strong> espalhadas pelo mapa para vencer. Use a barra de <strong>Espaço</strong> para atacar e derrotar os inimigos antes que eles te alcancem. Cada toque de um inimigo remove uma vida sua. Você tem <strong>5 vidas</strong>, mas os inimigos também possuem <strong>vida própria</strong> — elimine-os para limpar o caminho! Se perder todas as suas vidas, é game over.
</p>

<hr>

<h2>🚀 Como Executar</h2>

<h3>✔️ Requisitos</h3>
<ul>
    <li>Java 21 instalado</li>
</ul>

<h3>▶️ Execução do JAR</h3>

<p>O jogo pode ser executado diretamente pelo arquivo JAR localizado na pasta <code>release</code>:</p>

<ol>
    <li>Abra o terminal na pasta do projeto.</li>
    <li>Entre na pasta <code>release</code>:</li>
</ol>

<pre>cd release</pre>

<ol start="3">
    <li>Execute o jogo:</li>
</ol>

<pre>java -jar ProjetoPOO_Jogo2D.jar</pre>

<hr>

<h2>🖼️ Screenshots</h2>

<h3>🎮 Gameplay Principal</h3>
<p>Explorando o labirinto em busca das chaves e combatendo inimigos.</p>
<img src="screenshots/GamePlay.png" alt="Tela de gameplay do Key Hunt" width="800">

<h3>🧍 Sprite do Jogador</h3>
<p>Sprite sheet do personagem principal com animações direcionais e de ataque.</p>
<img src="screenshots/SpriteJogador.png" alt="Sprite do jogador" width="200">

<h3>👾 Inimigo — Golem</h3>
<p>Inimigo com barra de vida e máquina de estados: fica parado até detectar o jogador, então persegue e ataca.</p>
<img src="screenshots/SpriteInimigo.png" alt="Sprite do inimigo Golem" width="200">

<h3>💀 Game Over</h3>
<p>Tela exibida quando o jogador perde todas as vidas.</p>
<img src="screenshots/TelaGameOver.png" alt="Tela de fim de jogo" width="800">

<h3>🏆 Vitória</h3>
<p>Tela exibida ao coletar todas as 7 chaves.</p>
<img src="screenshots/TelaVencedor.png" alt="Tela de vitória" width="800">

<hr>

<h2>🏗️ Estrutura do Sistema</h2>

<h3>📦 Pacotes e Classes</h3>

<table>
  <tr><th>Pacote</th><th>Classe</th><th>Responsabilidade</th></tr>
  <tr><td><code>tela</code></td><td><code>Tela</code></td><td>Ponto de entrada da aplicação. Cria o <code>JFrame</code> principal e inicia o jogo.</td></tr>
  <tr><td rowspan="5"><code>paineis</code></td><td><code>PainelJogo</code></td><td>Tela do menu principal. Gerencia a música e navega entre telas.</td></tr>
  <tr><td><code>PainelMapa</code></td><td>Tela de gameplay. Controla o loop do jogo via <code>Timer</code>, atualiza entidades, gerencia o combate e detecta condições de vitória/derrota.</td></tr>
  <tr><td><code>TelaOpcoes</code></td><td>Tela de configurações. Permite ligar/desligar a música e ajustar o volume.</td></tr>
  <tr><td><code>TelaVencedor</code></td><td>Tela exibida ao coletar todas as chaves.</td></tr>
  <tr><td><code>TelaDerrota</code></td><td>Tela exibida ao perder todas as vidas.</td></tr>
  <tr><td><code>paineis</code></td><td><code>MenuGame</code></td><td>Renderiza os botões do menu e expõe métodos de detecção de clique encapsulados.</td></tr>
  <tr><td rowspan="3"><code>entidades</code></td><td><code>Entidade</code></td><td>Classe abstrata base. Define atributos comuns (posição, tamanho, velocidade, vida) e contrato <code>atualizar()</code> / <code>desenhar()</code>.</td></tr>
  <tr><td><code>Jogador</code></td><td>Herda de <code>Entidade</code> e implementa <code>KeyListener</code>. Gerencia movimentação, colisão, execução do ataque (Espaço), vida e coleta de chaves.</td></tr>
  <tr><td><code>Inimigo</code></td><td>Herda de <code>Entidade</code>. Implementa sistema de vida própria, máquina de estados (PARADO, PERSEGUINDO, ATACANDO) e lógica para receber dano do jogador.</td></tr>
  <tr><td><code>camera</code></td><td><code>Camera</code></td><td>Calcula o offset de renderização para centralizar a visão no jogador dentro dos limites do mapa.</td></tr>
  <tr><td rowspan="3"><code>mapa</code></td><td><code>MapaMatriz</code></td><td>Define o mapa como matrizes estáticas de tiles e decorações, posições das chaves e lógica de colisão por tile.</td></tr>
  <tr><td><code>GeradorMapa</code></td><td>Carrega e renderiza os tiles, decorações e chaves usando a Java 2D API.</td></tr>
  <tr><td><code>Decoracao</code></td><td>Representa os tipos de decoração do mapa com id e nome de arquivo associado.</td></tr>
  <tr><td><code>sons</code></td><td><code>Musica</code></td><td>Encapsula a reprodução de áudio via <code>javax.sound</code>, com controle de loop e volume dinâmico.</td></tr>
</table>

<hr>

<h2>🧩 Conceitos de POO Aplicados</h2>

<h3>Classes Abstratas</h3>
<p>
<code>Entidade</code> é uma classe abstrata que define os atributos e o contrato comum a todas as entidades do jogo. Ela não pode ser instanciada diretamente — existe para garantir que <code>Jogador</code> e <code>Inimigo</code> implementem obrigatoriamente <code>atualizar()</code> e <code>desenhar()</code>. A escolha de classe abstrata se justifica pela necessidade de compartilhar estado real (como posição, velocidade e o sistema de dano/vida) entre as subclasses.
</p>

<h3>Herança</h3>
<p>
<code>Jogador</code> e <code>Inimigo</code> herdam de <code>Entidade</code>, reutilizando os atributos de posicionamento, dimensões e o método <code>levarDano()</code>, especializando o comportamento de movimentação, combate e IA de acordo com suas regras de negócio.
</p>

<h3>Polimorfismo</h3>
<p>
Em <code>PainelMapa</code>, todas as entidades são armazenadas em uma <code>List&lt;Entidade&gt;</code>. O loop de renderização e atualização chama <code>desenhar(g2, camera)</code> e <code>atualizar()</code> em cada elemento — o Java resolve em tempo de execução qual implementação usar, seja a do jogador atacando ou a do inimigo perseguindo.
</p>

<h3>Encapsulamento</h3>
<p>
Os atributos de todas as classes são <code>private</code> ou <code>protected</code>, acessados somente por getters e setters controlados. Em <code>MenuGame</code>, os retângulos dos botões são <code>private</code> e expostos apenas por métodos como <code>clicouJogar(Point p)</code>, evitando que o estado interno do menu seja corrompido externamente.
</p>

<h3>Interface</h3>
<p>
<code>Jogador</code> implementa a interface <code>KeyListener</code> do Java, isolando a responsabilidade de captura das entradas de teclado (como movimentação e o comando de ataque na barra de espaço) da lógica interna da engine.
</p>

<h3>Composição</h3>
<p>
<code>PainelMapa</code> compõe um <code>Jogador</code>, uma lista de <code>Inimigo</code>, uma <code>Camera</code> e um <code>GeradorMapa</code> — garantindo que cada classe execute uma única responsabilidade de forma limpa e modular.
</p>

<hr>

<h2>✨ Funcionalidades</h2>

<ul>
    <li>5 telas distintas: Menu, Gameplay, Opções, Vitória e Derrota</li>
    <li>Movimentação suave com delta time (independente de framerate)</li>
    <li><strong>Sistema de combate ativo:</strong> jogador pode atacar utilizando a tecla <strong>Espaço</strong></li>
    <li><strong>Inimigos com vida própria:</strong> agora os monstros podem ser golpeados e eliminados do mapa</li>
    <li>Sistema de câmera seguindo o jogador com limites do mapa</li>
    <li>Efeito de escuridão com gradiente radial (Java 2D)</li>
    <li>Inimigos com máquina de estados: parado, perseguindo e atacando</li>
    <li>Animações por sprite sheet com troca dinâmica de estado (andando, parado, atacando)</li>
    <li>Sistema de vida com barra de HP no HUD para monitoramento em tempo real</li>
    <li>Coleta de 7 chaves como condição de vitória</li>
    <li>Mapa construído com matriz de tiles e camada de decoração</li>
    <li>Colisão por tile calibrada para jogador e inimigos</li>
    <li>Música com controle de liga/desliga e 5 níveis de volume</li>
</ul>

<hr>

<h2>🛠️ Tecnologias Utilizadas</h2>

<ul>
    <li>Java 21</li>
    <li>Java Swing (interface gráfica)</li>
    <li>Java 2D API (renderização, sprites, efeitos visuais)</li>
    <li>javax.sound (sistema de áudio)</li>
</ul>

<hr>

<h2>👨‍💻 Desenvolvedores</h2>

<ul>
    <li>Gabriel Arcanjo</li>
    <li>Ewerton Rocha</li>
    <li>Miguel dos Santos</li>
</ul>
