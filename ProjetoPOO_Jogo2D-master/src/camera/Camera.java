package camera;

import entidades.Jogador;
import mapa.MapaMatriz;

public class Camera {
    private int cameraX;
    private int cameraY;
    private final int larguraTela;
    private final int alturaTela;

    public Camera(int larguraTela, int alturaTela) {
        this.larguraTela = larguraTela;
        this.alturaTela = alturaTela;
        inicializarNaSpawn();
    }

    private void inicializarNaSpawn() {
        int mapaLargura = MapaMatriz.COLUNAS * MapaMatriz.TILE_SIZE;
        int mapaAltura = MapaMatriz.LINHAS  * MapaMatriz.TILE_SIZE;
        int spawnX = MapaMatriz.playerCol * MapaMatriz.TILE_SIZE;
        int spawnY = MapaMatriz.playerRow * MapaMatriz.TILE_SIZE;
        cameraX = spawnX - larguraTela / 2;
        cameraY = spawnY - alturaTela  / 2;
        cameraX = Math.max(0, Math.min(cameraX, mapaLargura - larguraTela));
        cameraY = Math.max(0, Math.min(cameraY, mapaAltura  - alturaTela));
    }

    public void atualizar(Jogador jogador) {
        int mapaLargura = MapaMatriz.COLUNAS * MapaMatriz.TILE_SIZE;
        int mapaAltura = MapaMatriz.LINHAS  * MapaMatriz.TILE_SIZE;
        cameraX = jogador.getX() - larguraTela / 2;
        cameraY = jogador.getY() - alturaTela  / 2;
        if (cameraX < 0) cameraX = 0;
        if (cameraY < 0) cameraY = 0;
        if (cameraX > mapaLargura - larguraTela) cameraX = mapaLargura - larguraTela;
        if (cameraY > mapaAltura  - alturaTela)  cameraY = mapaAltura  - alturaTela;
    }

    public int getCameraX() { return cameraX; }
    public int getCameraY() { return cameraY; }
}
