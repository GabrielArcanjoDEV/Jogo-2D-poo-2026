package paineis;

import sons.Musica;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class TelaOpcoes extends JPanel {

    private static final int LARGURA_TELA = PainelMapa.LARGURA_JOGO;
    private static final int ALTURA_TELA  = PainelMapa.ALTURA_JOGO;

    private BufferedImage imagem;
    private boolean musicaLigada;
    private int volume;
    private Musica musica;

    private static final int CX = LARGURA_TELA / 2;

    private static final int BTN_W = 123;
    private static final int BTN_H = 48;
    private static final int BTN_GAP = 24;

    public final Rectangle btnOn = new Rectangle(CX - BTN_W - BTN_GAP / 2, 272, BTN_W, BTN_H);
    public final Rectangle btnOff = new Rectangle(CX + BTN_GAP / 2,272, BTN_W, BTN_H);
    public final Rectangle btnVoltar = new Rectangle(507, 576, 264, 72);

    private final Rectangle[] barrasVolume = new Rectangle[5];

    public TelaOpcoes(Musica musica) {
        this.musica = musica;
        setPreferredSize(new Dimension(LARGURA_TELA, ALTURA_TELA));

        this.musicaLigada = musica.isMusicaLigada();
        this.volume = musica.getVolumeAtual();

        try {
            imagem = ImageIO.read(Objects.requireNonNull(getClass().getResource("/imagens/TelaOpcoes.png")));
        } catch (IOException e) {
            throw new RuntimeException("Arquivo não encontrado: /imagens/TelaOpcoes.png", e);
        }

        int larguraBarra = 40;
        int alturaBarra = 52;
        int espacamento = 14;
        int larguraTotal = (5 * larguraBarra) + (4 * espacamento);
        int startX = CX - larguraTotal / 2;

        for (int i = 0; i < 5; i++) {
            barrasVolume[i] = new Rectangle(
                    startX + i * (larguraBarra + espacamento),
                    424, larguraBarra, alturaBarra
            );
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point p = e.getPoint();

                if (btnOn.contains(p))  { musicaLigada = true;  TelaOpcoes.this.musica.ligar();    repaint(); }
                if (btnOff.contains(p)) { musicaLigada = false; TelaOpcoes.this.musica.desligar(); repaint(); }

                for (int i = 0; i < barrasVolume.length; i++) {
                    if (barrasVolume[i].contains(p)) {
                        volume = i + 1;
                        TelaOpcoes.this.musica.setVolume(volume);
                        repaint();
                    }
                }
                if (btnVoltar.contains(p)) {
                    JFrame janela = (JFrame) SwingUtilities.getWindowAncestor(TelaOpcoes.this);
                    janela.setContentPane(new PainelJogo());
                    janela.revalidate();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        if (imagem != null) g2.drawImage(imagem, 0, 0, LARGURA_TELA, ALTURA_TELA, null);

        g2.setFont(new Font("Georgia", Font.BOLD, 26));
        g2.setColor(new Color(60, 30, 10));
        desenharTextoCentralized(g2, "MÚSICA", CX, 240);

        desenharBotao(g2, btnOn,  "ON",  musicaLigada  ? new Color(80, 170, 80)  : new Color(160, 160, 160));
        desenharBotao(g2, btnOff, "OFF", !musicaLigada ? new Color(200, 70, 70)  : new Color(160, 160, 160));

        g2.setFont(new Font("Georgia", Font.BOLD, 26));
        g2.setColor(new Color(60, 30, 10));
        desenharTextoCentralized(g2, "VOLUME", CX, 393);

        for (int i = 0; i < barrasVolume.length; i++) {
            Rectangle barra = barrasVolume[i];

            g2.setColor(new Color(0, 0, 0, 60));
            g2.fillRoundRect(barra.x + 2, barra.y + 2, barra.width, barra.height, 6, 6);

            if (i < volume) {
                g2.setColor(new Color(Math.min(255, 60 + i * 35), Math.max(60, 180 - i * 20), 40));
            } else {
                g2.setColor(new Color(200, 190, 160));
            }
            g2.fillRoundRect(barra.x, barra.y, barra.width, barra.height, 6, 6);

            g2.setColor(new Color(80, 50, 20));
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(barra.x, barra.y, barra.width, barra.height, 6, 6);
        }
    }

    private void desenharBotao(Graphics2D g2, Rectangle r, String texto, Color cor) {
        g2.setColor(new Color(0, 0, 0, 80));
        g2.fillRoundRect(r.x + 3, r.y + 3, r.width, r.height, 14, 14);

        g2.setColor(cor);
        g2.fillRoundRect(r.x, r.y, r.width, r.height, 14, 14);

        g2.setColor(new Color(255, 255, 255, 60));
        g2.fillRoundRect(r.x + 4, r.y + 3, r.width - 8, r.height / 2, 10, 10);

        g2.setColor(new Color(60, 30, 10));
        g2.setStroke(new BasicStroke(2f));
        g2.drawRoundRect(r.x, r.y, r.width, r.height, 14, 14);

        g2.setFont(new Font("Georgia", Font.BOLD, 22));
        FontMetrics fm = g2.getFontMetrics();
        int tx = r.x + (r.width  - fm.stringWidth(texto)) / 2;
        int ty = r.y + (r.height - fm.getHeight()) / 2 + fm.getAscent();
        g2.setColor(new Color(0, 0, 0, 120));
        g2.drawString(texto, tx + 1, ty + 1);
        g2.setColor(Color.WHITE);
        g2.drawString(texto, tx, ty);
    }

    private void desenharTextoCentralized(Graphics2D g2, String texto, int cx, int y) {
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(texto, cx - fm.stringWidth(texto) / 2, y);
    }
}