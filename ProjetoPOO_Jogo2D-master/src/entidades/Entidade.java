package entidades;

import camera.Camera;

import java.awt.*;

public abstract class Entidade {
    protected int x, y;
    protected int largura, altura;
    protected int velocidade;
    protected Color cor;

    public Entidade(int x, int y, int largura, int altura, int velocidade, Color cor) {
        this.x = x;
        this.y = y;
        this.altura = altura;
        this.largura = largura;
        this.velocidade = velocidade;
        this.cor = cor;
    }

    public abstract void atualizar();
    public abstract void desenhar(Graphics2D g2, Camera camera);

    public Rectangle getBounds() {
        return new Rectangle(x, y, largura, altura);
    }

    public int getX() { return x; }
    public int getY() { return y; }
}