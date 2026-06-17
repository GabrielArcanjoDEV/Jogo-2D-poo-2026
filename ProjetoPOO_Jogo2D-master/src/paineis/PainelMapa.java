package paineis;

import camera.Camera;
import entidades.Inimigo;
import entidades.Jogador;
import mapa.GeradorMapa;
import mapa.MapaMatriz;
import entidades.Entidade;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class PainelMapa extends JPanel {
    public static final int LARGURA_JOGO = 1280;
    public static final int ALTURA_JOGO  = 720;

    private GeradorMapa mapa;
    private Jogador jogador;
    private Camera camera;
    private final List<Inimigo> inimigos = new ArrayList<>();
    private final List<Entidade> entidades = new ArrayList<>();
    private Timer timer;
    private boolean fimDeJogoMostrado = false;

    private long ultimoTempo = System.nanoTime();

    public PainelMapa() {
        setFocusable(true);
        setOpaque(true);
        setPreferredSize(new Dimension(LARGURA_JOGO, ALTURA_JOGO));

        MapaMatriz.keysColetadas = new boolean[MapaMatriz.keys.length];

        mapa = new GeradorMapa();
        jogador = new Jogador(MapaMatriz.playerRow, MapaMatriz.playerCol);
        camera = new Camera(LARGURA_JOGO, ALTURA_JOGO);
        entidades.add(jogador);

        for (int[] pos : MapaMatriz.enemies) {
            Inimigo inimigo = new Inimigo(pos[0], pos[1]);
            inimigos.add(inimigo);
            entidades.add(inimigo);
        }

        addKeyListener(jogador);
        SwingUtilities.invokeLater(this::requestFocusInWindow);

        ultimoTempo = System.nanoTime();

        timer = new Timer(16, e -> {
            long agora = System.nanoTime();
            double deltaTime = (agora - ultimoTempo) / 1_000_000_000.0;
            ultimoTempo = agora;
            if (deltaTime > 0.05) deltaTime = 0.05;

            jogador.atualizar(deltaTime);
            camera.atualizar(jogador);
            verificarColetaChaves();

            for (Inimigo inimigo : inimigos) {
                inimigo.atualizarComJogador(
                        jogador.getX(),
                        jogador.getY(),
                        jogador
                );
            }

            // Sistema de ataque por distância adicionado logo após o loop dos inimigos
            if (jogador.isAtacando() && jogador.podeAtacar()) {
                for (Inimigo inimigo : inimigos) {
                    double distancia = Math.hypot(
                            jogador.getX() - inimigo.getX(),
                            jogador.getY() - inimigo.getY()
                    );

                    if (distancia <= 100) {
                        inimigo.levarDano(1);
                    }
                }
            }

            if (!jogador.isVivo() && !fimDeJogoMostrado) {
                fimDeJogoMostrado = true;
                timer.stop();
                PainelJogo.musica.parar();
                JFrame janela = (JFrame) SwingUtilities.getWindowAncestor(this);
                SwingUtilities.invokeLater(() -> {
                    janela.setContentPane(new TelaDerrota(janela));
                    janela.revalidate();
                    janela.repaint();
                });
            }

            if (jogador.getChavesColetadas() >= 7 && !fimDeJogoMostrado) {
                fimDeJogoMostrado = true;
                timer.stop();
                PainelJogo.musica.parar();
                JFrame janela = (JFrame) SwingUtilities.getWindowAncestor(this);
                SwingUtilities.invokeLater(() -> {
                    janela.setContentPane(new TelaVencedor(janela));
                    janela.revalidate();
                    janela.repaint();
                });
            }

            repaint();
        });
        timer.start();
    }

    private void verificarColetaChaves() {
        final int MARGEM = 22;

        int jogX1 = jogador.getX() + MARGEM;
        int jogY1 = jogador.getY() + MARGEM;
        int jogX2 = jogador.getX() + MapaMatriz.TILE_SIZE - MARGEM;
        int jogY2 = jogador.getY() + MapaMatriz.TILE_SIZE - MARGEM;

        for (int i = 0; i < MapaMatriz.keys.length; i++) {
            if (MapaMatriz.keysColetadas[i]) continue;
            int lin = MapaMatriz.keys[i][0];
            int col = MapaMatriz.keys[i][1];

            int chaveX1 = col * MapaMatriz.TILE_SIZE + MARGEM;
            int chaveY1 = lin * MapaMatriz.TILE_SIZE + MARGEM;
            int chaveX2 = col * MapaMatriz.TILE_SIZE + MapaMatriz.TILE_SIZE - MARGEM;
            int chaveY2 = lin * MapaMatriz.TILE_SIZE + MapaMatriz.TILE_SIZE - MARGEM;

            if (jogX1 < chaveX2 && jogX2 > chaveX1 &&
                    jogY1 < chaveY2 && jogY2 > chaveY1) {
                MapaMatriz.keysColetadas[i] = true;
                jogador.coletarChaves();
            }
        }
    }

    private void desenharHUD(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRoundRect(10, 10, 355, 50, 12, 12);

        if (mapa.getImagemChave() != null) {
            g2.drawImage(mapa.getImagemChave(), 16, 14, 26, 26, null);
        }

        g2.setColor(Color.YELLOW);
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.drawString(jogador.getChavesColetadas() + " / 7", 50, 34);

        g2.setColor(Color.DARK_GRAY);
        g2.fillRoundRect(148, 14, 200, 24, 8, 8);

        g2.setColor(Color.RED);
        g2.fillRoundRect(148, 14, jogador.getVida() * 40, 24, 8, 8);

        g2.setColor(Color.WHITE);
        g2.drawRoundRect(148, 14, 200, 24, 8, 8);

        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.drawString("Vida: " + jogador.getVida() + "/5", 205, 31);
    }

    private void desenharEscuridao(Graphics2D g2) {
        int jogadorCentroX = jogador.getX() - camera.getCameraX() + MapaMatriz.TILE_SIZE / 2;
        int jogadorCentroY = jogador.getY() - camera.getCameraY() + MapaMatriz.TILE_SIZE / 2;

        int raioLuz = 175;

        BufferedImage escuridao = new BufferedImage(LARGURA_JOGO, ALTURA_JOGO, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gEsc = escuridao.createGraphics();

        gEsc.setColor(new Color(0, 0, 0, 170));
        gEsc.fillRect(0, 0, LARGURA_JOGO, ALTURA_JOGO);

        gEsc.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_OUT));
        java.awt.RadialGradientPaint gradiente = new java.awt.RadialGradientPaint(
                jogadorCentroX, jogadorCentroY, raioLuz,
                new float[]{0.0f, 1.0f},
                new Color[]{new Color(0, 0, 0, 255), new Color(0, 0, 0, 0)}
        );
        gEsc.setPaint(gradiente);
        gEsc.fillOval(jogadorCentroX - raioLuz, jogadorCentroY - raioLuz, raioLuz * 2, raioLuz * 2);
        gEsc.dispose();

        g2.drawImage(escuridao, 0, 0, null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        mapa.desenhar(g2, camera);
        for (Entidade entidade : entidades) {
            if (entidade instanceof Inimigo inimigo) {
                inimigo.desenhar(g2, camera);
            }
            if (entidade instanceof Jogador jog) {
                jog.desenhar(g2, camera);
            }
        }
        desenharEscuridao(g2);
        desenharHUD(g2);
        g2.dispose();
    }
}