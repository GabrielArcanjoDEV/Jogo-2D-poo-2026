package entidades;

import camera.Camera;
import mapa.MapaMatriz;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class Inimigo extends Entidade {
    public enum Estado {
        PARADO,
        PERSEGUINDO,
        ATACANDO
    }

    private Estado estado = Estado.PARADO;
    private int dx, dy;

    private static final int ALCANCE_VISAO   = 500;
    private static final int DISTANCIA_ATAQUE = 80;
    private boolean danoAplicadoNesteAtaque = false;

    private final int origemX;
    private final int origemY;

    private static final int DANO = 1;
    private int cooldownDano = 0;
    private static final int INTERVALO_DANO = 60;

    private static BufferedImage[] framesIdle;
    private static BufferedImage[] framesRunning;
    private static BufferedImage[] framesKicking;

    private BufferedImage[] framesAtivos;
    private int frameAtual = 0;
    private int frameTimer = 0;
    private int FRAME_DELAY = 8;

    private boolean olhandoDireita = true;

    public Inimigo(int row, int col) {
        super(col * MapaMatriz.TILE_SIZE, row * MapaMatriz.TILE_SIZE,
                MapaMatriz.TILE_SIZE - 32, MapaMatriz.TILE_SIZE - 32,
                6, Color.RED);
        this.origemX = this.x;
        this.origemY = this.y;
        if (framesIdle == null) {
            carregarSprites();
        }
        framesAtivos = framesIdle;
    }

    private BufferedImage[] carregarSequencia(String pasta, String prefixo, int total) {
        BufferedImage[] frames = new BufferedImage[total];
        int carregados = 0;
        for (int i = 0; i < total; i++) {
            String caminho = "/imagens/Golem_3/PNG/" + pasta + "/" + String.format("%s_%03d.png", prefixo, i);
            try (InputStream is = getClass().getResourceAsStream(caminho)) {
                if (is == null) {
                    System.err.println("[ERRO] Sprite não encontrado: " + caminho);
                    continue;
                }
                frames[i] = ImageIO.read(is);
                if (frames[i] != null) carregados++;
            } catch (Exception e) {
                throw new RuntimeException("Erro ao carregar sprite: " + caminho, e);
            }
        }
        return carregados > 0 ? frames : null;
    }

    private void carregarSprites() {
        if (framesIdle != null) return;
        framesIdle = carregarSequencia("Idle",    "0_Golem_Idle",    18);
        framesRunning = carregarSequencia("Running", "0_Golem_Running", 12);
        framesKicking = carregarSequencia("Kicking", "0_Golem_Kicking", 12);
    }

    private void setAnimacao(BufferedImage[] novos, int delay) {
        if (novos == null || novos == framesAtivos) return;
        framesAtivos = novos;
        frameAtual = 0;
        frameTimer = 0;
        FRAME_DELAY = delay;
    }

    @Override
    public void atualizar() {
        if (framesAtivos != null) {
            frameTimer++;
            if (frameTimer >= FRAME_DELAY) {
                frameTimer = 0;
                frameAtual = (frameAtual + 1) % framesAtivos.length;
            }
        }
        if (cooldownDano > 0) cooldownDano--;
    }

    public void atualizarComJogador(int jogadorX, int jogadorY, Jogador jogador) {
        atualizar();

        if (!jogador.isVivo()) {
            estado = Estado.PARADO;
            dx = 0;
            dy = 0;
            setAnimacao(framesIdle, 10);
            return;
        }

        double distanciaJogador = calcularDistancia(jogadorX, jogadorY);

        if (distanciaJogador <= DISTANCIA_ATAQUE) {
            estado = Estado.ATACANDO;
            dx = 0;
            dy = 0;
            setAnimacao(framesKicking, 4);

        } else if (distanciaJogador <= ALCANCE_VISAO) {
            estado = Estado.PERSEGUINDO;
            danoAplicadoNesteAtaque = false;
            perseguir(jogadorX, jogadorY);
            setAnimacao(framesRunning, 6);

        } else {
            estado = Estado.PARADO;
            danoAplicadoNesteAtaque = false;
            dx = 0;
            dy = 0;
            setAnimacao(framesIdle, 10);
        }

        if (dx > 0) olhandoDireita = true;
        else if (dx < 0) olhandoDireita = false;

        int novoX = x + dx;
        int novoY = y + dy;

        if (!colidiu(novoX, y)) x = novoX;
        if (!colidiu(x, novoY)) y = novoY;

        if (estado == Estado.ATACANDO && cooldownDano == 0 && jogador.isVivo()) {
            if (frameAtual == 6 && !danoAplicadoNesteAtaque) {
                jogador.levarDano(DANO);
                danoAplicadoNesteAtaque = true;
                cooldownDano = INTERVALO_DANO;
            }
            if (frameAtual == 0) {
                danoAplicadoNesteAtaque = false;
            }
        }
    }

    private void perseguir(int jogadorX, int jogadorY) {
        dx = 0;
        dy = 0;
        if (x < jogadorX) dx = velocidade;
        else if (x > jogadorX) dx = -velocidade;
        if (y < jogadorY) dy = velocidade;
        else if (y > jogadorY) dy = -velocidade;
    }

    private double calcularDistancia(int alvoX, int alvoY) {
        int difX = alvoX - x;
        int difY = alvoY - y;
        return Math.sqrt(difX * difX + difY * difY);
    }

    private boolean colidiu(int novoX, int novoY) {
        int esquerdaTile = novoX / MapaMatriz.TILE_SIZE;
        int direitaTile = (novoX + largura - 1) / MapaMatriz.TILE_SIZE;
        int topoTile = novoY / MapaMatriz.TILE_SIZE;
        int baixoTile = (novoY + altura - 1) / MapaMatriz.TILE_SIZE;

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
    public void desenhar(Graphics2D g2, Camera camera) {
        if (framesAtivos == null || framesAtivos[frameAtual] == null) {
            g2.setColor(cor);
            g2.fillOval(x - camera.getCameraX(), y - camera.getCameraY(), largura, altura);
            return;
        }

        int tamanho = 128;
        int offX = (MapaMatriz.TILE_SIZE - tamanho) / 2;
        int offY = (MapaMatriz.TILE_SIZE - tamanho) / 2;

        int drawX = x - camera.getCameraX() + offX;
        int drawY = y - camera.getCameraY() + offY;

        BufferedImage frame = framesAtivos[frameAtual];

        if (!olhandoDireita) {
            g2.drawImage(frame, drawX + tamanho, drawY, -tamanho, tamanho, null);
        } else {
            g2.drawImage(frame, drawX, drawY, tamanho, tamanho, null);
        }
    }

    public Estado getEstado() { return estado;  }
    public int getOrigemX()   { return origemX; }
    public int getOrigemY()   { return origemY; }
}