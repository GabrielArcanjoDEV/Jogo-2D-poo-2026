package entidades;

import camera.Camera;
import mapa.MapaMatriz;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Jogador extends Entidade implements KeyListener {
    private static final double VELOCIDADE_PPS = 380.0;

    private static final int DIRECOES = 4;
    private static final int FRAMES_POR_DIRECAO = 8;
    private static final int MARGEM_COLISAO = 22;

    private double dx;
    private double dy;

    private double posX;
    private double posY;

    private int chavesColetadas = 0;
    private boolean vivo = true;

    private int direcaoLinha = 0;
    private boolean movendo = false;
    private int vida = 5;

    private int frameAtual = 0;
    private double frameTimer = 0;
    private final double FRAME_DELAY = 0.1;

    private final int larguraVisual = 128;
    private final int alturaVisual = 128;

    private final BufferedImage[][] playerSprites = new BufferedImage[DIRECOES][FRAMES_POR_DIRECAO];

    // Novas variáveis de ataque adicionadas
    private boolean atacando = false;
    private long ultimoAtaque = 0;

    public Jogador(int row, int col) {
        super(col * MapaMatriz.TILE_SIZE, row * MapaMatriz.TILE_SIZE,
                MapaMatriz.TILE_SIZE - 16, MapaMatriz.TILE_SIZE - 16,
                0, Color.CYAN);
        posX = x;
        posY = y;
        carregarPersonagem();
    }

    private void carregarPersonagem() {
        try {
            BufferedImage spriteSheet = ImageIO.read(Objects.requireNonNull(
                    getClass().getResource("/imagens/Jogador.png")));

            int larguraFrame = spriteSheet.getWidth() / FRAMES_POR_DIRECAO;
            int alturaFrame = spriteSheet.getHeight() / DIRECOES;

            for (int linha = 0; linha < DIRECOES; linha++) {
                for (int coluna = 0; coluna < FRAMES_POR_DIRECAO; coluna++) {
                    playerSprites[linha][coluna] = spriteSheet.getSubimage(
                            coluna * larguraFrame, linha * alturaFrame,
                            larguraFrame, alturaFrame);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar o sprite do jogador.", e);
        }
    }

    @Override
    public void atualizar() {
        atualizar(1.0 / 60.0);
    }

    public void atualizar(double deltaTime) {
        if (!vivo) return;

        double novoX = posX + dx * deltaTime;
        double novoY = posY + dy * deltaTime;

        movendo = (dx != 0 || dy != 0);

        if (!colidiu((int) novoX, (int) posY)) posX = novoX;
        if (!colidiu((int) posX, (int) novoY)) posY = novoY;

        x = (int) posX;
        y = (int) posY;

        if (movendo) {
            frameTimer += deltaTime;
            if (frameTimer >= FRAME_DELAY) {
                frameTimer -= FRAME_DELAY;
                frameAtual = (frameAtual + 1) % FRAMES_POR_DIRECAO;
            }
        } else {
            frameAtual = 0;
            frameTimer = 0;
        }
    }

    @Override
    public void desenhar(Graphics2D g2, Camera camera) {
        BufferedImage frame = playerSprites[direcaoLinha][frameAtual];
        int offsetX = (larguraVisual - largura) / 2;
        int offsetY = (alturaVisual - altura) / 2;

        g2.drawImage(frame,
                x - camera.getCameraX() - offsetX,
                y - camera.getCameraY() - offsetY,
                larguraVisual, alturaVisual, null);
    }

    public boolean colidiu(int novoX, int novoY) {
        int esquerdaTile = (novoX + MARGEM_COLISAO) / MapaMatriz.TILE_SIZE;
        int direitaTile = (novoX + largura - MARGEM_COLISAO - 1) / MapaMatriz.TILE_SIZE;
        int topoTile = (novoY + MARGEM_COLISAO) / MapaMatriz.TILE_SIZE;
        int baixoTile = (novoY + altura - MARGEM_COLISAO - 1) / MapaMatriz.TILE_SIZE;

        if (esquerdaTile < 0 || direitaTile >= MapaMatriz.COLUNAS ||
                topoTile < 0 || baixoTile >= MapaMatriz.LINHAS) {
            return true;
        }

        return MapaMatriz.isSolid(MapaMatriz.MAP[topoTile][esquerdaTile])
                || MapaMatriz.isSolid(MapaMatriz.MAP[topoTile][direitaTile])
                || MapaMatriz.isSolid(MapaMatriz.MAP[baixoTile][esquerdaTile])
                || MapaMatriz.isSolid(MapaMatriz.MAP[baixoTile][direitaTile]);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W, KeyEvent.VK_UP    -> { dx = 0; dy = -VELOCIDADE_PPS; direcaoLinha = 3; }
            case KeyEvent.VK_S, KeyEvent.VK_DOWN  -> { dx = 0; dy =  VELOCIDADE_PPS; direcaoLinha = 0; }
            case KeyEvent.VK_A, KeyEvent.VK_LEFT  -> { dx = -VELOCIDADE_PPS; dy = 0; direcaoLinha = 1; }
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> { dx =  VELOCIDADE_PPS; dy = 0; direcaoLinha = 2; }
            case KeyEvent.VK_SPACE                 -> atacando = true; // Modificação do espaço adicionada
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W, KeyEvent.VK_UP    -> { if (dy < 0) dy = 0; }
            case KeyEvent.VK_S, KeyEvent.VK_DOWN  -> { if (dy > 0) dy = 0; }
            case KeyEvent.VK_A, KeyEvent.VK_LEFT  -> { if (dx < 0) dx = 0; }
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> { if (dx > 0) dx = 0; }
            case KeyEvent.VK_SPACE                 -> atacando = false; // Modificação do espaço adicionada
        }
    }

    public int getVida() { return vida; }
    public boolean isVivo() { return vivo; }
    public int getChavesColetadas() { return chavesColetadas; }

    public void levarDano(int dano) {
        if (!vivo) return;
        vida -= dano;
        if (vida <= 0) {
            vida = 0;
            vivo = false;
        }
    }

    public void coletarChaves() { chavesColetadas++; }
    public void morrer() { vivo = false; }

    // Novos métodos de ataque adicionados no final
    public boolean isAtacando() {
        return atacando;
    }

    public boolean podeAtacar() {
        long agora = System.currentTimeMillis();

        if (agora - ultimoAtaque >= 500) {
            ultimoAtaque = agora;
            return true;
        }

        return false;
    }
}