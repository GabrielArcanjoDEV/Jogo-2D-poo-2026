package mapa;

import camera.Camera;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class GeradorMapa {
    private static final int TILE_ARBUSTO = 2;
    private static final int TILE_PEDRA = 3;
    private static final int TILE_GRAMA = 5;
    private static final int TILE_ARVORE = 6;
    private static final int TILE_CABANA = 7;
    private static final int TILE_CASA_ARVORE = 8;

    private static final int TAMANHO_ARBUSTO = 120;
    private static final int TAMANHO_DECORACAO = 80;
    private static final int TAMANHO_CHAVE = 48;

    private final BufferedImage[] tiles;
    private final BufferedImage[] imagensDecoracao;
    private BufferedImage imagemChave;

    private final String[] nomesTiles = {
            "vazio.png",
            "chaomarrom.png",
            "arbusto_topo.png",
            "pedra.png",
            "agua.png",
            "grama.png",
            "arvore.png",
            "cabana.png",
            "casaArvore.png"
    };

    public GeradorMapa() {
        tiles = new BufferedImage[nomesTiles.length];
        imagensDecoracao = new BufferedImage[Decoracao.TIPOS.length];
        carregarTiles();
        carregarDecoracoes();
        carregarChave();
    }

    private void carregarTiles() {
        try {
            for (int i = 0; i < nomesTiles.length; i++) {
                tiles[i] = ImageIO.read(
                        Objects.requireNonNull(
                                getClass().getResource("/tiles/" + nomesTiles[i])
                        )
                );
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar os tiles do mapa.", e);
        }
    }

    private void carregarDecoracoes() {
        try {
            for (int i = 1; i < Decoracao.TIPOS.length; i++) {
                if (Decoracao.TIPOS[i] == null) {
                    continue;
                }

                String caminho = "/tiles/" + Decoracao.TIPOS[i].getNomeArquivo();
                var stream = getClass().getResourceAsStream(caminho);

                if (stream == null) {
                    System.err.println("[ERRO] Arquivo não encontrado: " + caminho);
                    continue;
                }
                BufferedImage imgLida = ImageIO.read(stream);

                if (imgLida == null) {
                    System.err.println("[ERRO] Formato inválido: " + caminho);
                    continue;
                }
                BufferedImage imgConvertida = new BufferedImage(
                        imgLida.getWidth(),
                        imgLida.getHeight(),
                        BufferedImage.TYPE_INT_ARGB
                );

                Graphics2D g2 = imgConvertida.createGraphics();
                g2.drawImage(imgLida, 0, 0, null);
                g2.dispose();
                imagensDecoracao[i] = imgConvertida;
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar as decorações.", e);
        }
    }

    private void carregarChave() {
        try {
            var stream = getClass().getResourceAsStream("/tiles/chavemapa.png");
            if (stream == null) {
                System.err.println("[ERRO] Imagem da chave não encontrada.");
                return;
            }
            BufferedImage imgLida = ImageIO.read(stream);

            imagemChave = new BufferedImage(
                    imgLida.getWidth(),
                    imgLida.getHeight(),
                    BufferedImage.TYPE_INT_ARGB
            );
            Graphics2D g2 = imagemChave.createGraphics();
            g2.drawImage(imgLida, 0, 0, null);
            g2.dispose();

        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar imagem da chave.", e);
        }
    }

    public BufferedImage getImagemGrama() {
        return tiles[TILE_GRAMA];
    }

    public BufferedImage getImagemChave() {
        return imagemChave;
    }

    public void desenhar(Graphics2D g2, Camera camera) {
        desenharMapaBase(g2, camera);
        desenharDecoracoes(g2, camera);
        desenharChaves(g2, camera);
    }

    private void desenharMapaBase(Graphics2D g2, Camera camera) {
        for (int lin = 0; lin < MapaMatriz.LINHAS; lin++) {
            for (int col = 0; col < MapaMatriz.COLUNAS; col++) {
                int tileId = MapaMatriz.MAP[lin][col];
                int x = col * MapaMatriz.TILE_SIZE - camera.getCameraX();
                int y = lin * MapaMatriz.TILE_SIZE - camera.getCameraY();
                int decoracao = MapaMatriz.DECORATION_MAP[lin][col];

                if (decoracao > 0 || tileId == TILE_ARBUSTO || tileId == TILE_PEDRA
                        || tileId == TILE_ARVORE
                        || tileId == TILE_CABANA
                        || tileId == TILE_CASA_ARVORE) {

                    g2.drawImage(tiles[TILE_GRAMA], x, y, MapaMatriz.TILE_SIZE, MapaMatriz.TILE_SIZE,
                            null
                    );
                }

                if (tileId >= 0 && tileId < tiles.length && tiles[tileId] != null
                        && tileId != TILE_ARVORE) {
                    if (tileId == TILE_ARBUSTO) {
                        int deslocamento = (MapaMatriz.TILE_SIZE - TAMANHO_ARBUSTO) / 2;
                        g2.drawImage(tiles[tileId], x + deslocamento, y + deslocamento,
                                TAMANHO_ARBUSTO,
                                TAMANHO_ARBUSTO,
                                null
                        );
                    } else {
                        g2.drawImage(tiles[tileId], x, y, MapaMatriz.TILE_SIZE, MapaMatriz.TILE_SIZE,
                                null
                        );
                    }
                }
            }
        }
    }

    private void desenharDecoracoes(Graphics2D g2, Camera camera) {
        for (int lin = 0; lin < MapaMatriz.LINHAS; lin++) {
            for (int col = 0; col < MapaMatriz.COLUNAS; col++) {
                int decId = MapaMatriz.DECORATION_MAP[lin][col];
                if (decId <= 0 || decId >= imagensDecoracao.length || imagensDecoracao[decId] == null) {
                    continue;
                }

                int x = col * MapaMatriz.TILE_SIZE - camera.getCameraX();
                int y = lin * MapaMatriz.TILE_SIZE - camera.getCameraY();
                BufferedImage img = imagensDecoracao[decId];

                if (decId == 1 || decId == 2) {
                    int deslocamentoX = (MapaMatriz.TILE_SIZE - TAMANHO_DECORACAO) / 2;
                    int deslocamentoY = MapaMatriz.TILE_SIZE - TAMANHO_DECORACAO;

                    g2.drawImage(img, x + deslocamentoX, y + deslocamentoY, TAMANHO_DECORACAO,
                            TAMANHO_DECORACAO,
                            null
                    );

                } else {
                    g2.drawImage(img, x, y, MapaMatriz.TILE_SIZE, MapaMatriz.TILE_SIZE, null);
                }
            }
        }
    }

    private void desenharChaves(Graphics2D g2, Camera camera) {
        if (imagemChave == null) {
            return;
        }

        for (int i = 0; i < MapaMatriz.keys.length; i++) {
            if (MapaMatriz.keysColetadas[i]) {
                continue;
            }

            int lin = MapaMatriz.keys[i][0];
            int col = MapaMatriz.keys[i][1];

            int x = col * MapaMatriz.TILE_SIZE - camera.getCameraX();
            int y = lin * MapaMatriz.TILE_SIZE - camera.getCameraY();

            int offX = (MapaMatriz.TILE_SIZE - TAMANHO_CHAVE) / 2;
            int offY = (MapaMatriz.TILE_SIZE - TAMANHO_CHAVE) / 2;

            g2.drawImage(imagemChave, x + offX, y + offY, TAMANHO_CHAVE, TAMANHO_CHAVE, null);
        }
    }
}