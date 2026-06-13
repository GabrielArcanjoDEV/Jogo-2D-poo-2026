package paineis;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class TelaVencedor extends JPanel {
    private static final int LARGURA_TELA = PainelMapa.LARGURA_JOGO;
    private static final int ALTURA_TELA = PainelMapa.ALTURA_JOGO;
    private BufferedImage imagemFundo;
    private final JFrame raiz;
    private final Rectangle btnVoltar = new Rectangle(516, 608, 247, 48);

    public TelaVencedor(JFrame janela) {
        this.raiz = janela;
        setPreferredSize(new Dimension(LARGURA_TELA, ALTURA_TELA));
        setLayout(null);
        carregarImagem();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (btnVoltar.contains(e.getPoint())) {
                    voltarInicio();
                }
            }
        });
        PainelJogo.musica.tocarUmaVez("/sons/musicaVitoria.wav");
    }

    private void carregarImagem() {
        try {
            URL recurso = getClass().getResource("/imagens/TelaVitoria.png");
            if (recurso == null) {
                throw new RuntimeException("Arquivo não encontrado: /imagens/TelaVitoria.png");
            }
            imagemFundo = ImageIO.read(recurso);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar a imagem da tela de vitória.", e);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        if (imagemFundo != null) {
            g2.drawImage(imagemFundo, 0, 0, LARGURA_TELA, ALTURA_TELA, null);
        }
        desenharBotao(g2, btnVoltar, "VOLTAR");
    }

    private void desenharBotao(Graphics2D g2, Rectangle r, String texto) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(3));
        g2.setFont(new Font("Arial", Font.BOLD, 22));

        g2.setColor(new Color(0, 0, 0, 170));
        g2.fillRoundRect(r.x, r.y, r.width, r.height, 16, 16);
        g2.setColor(new Color(218, 145, 0));
        g2.drawRoundRect(r.x, r.y, r.width, r.height, 16, 16);

        FontMetrics fm = g2.getFontMetrics();
        int textoX = r.x + (r.width - fm.stringWidth(texto)) / 2;
        int textoY = r.y + ((r.height - fm.getHeight()) / 2) + fm.getAscent();
        g2.setColor(Color.WHITE);
        g2.drawString(texto, textoX, textoY);
    }

    private void voltarInicio() {
        PainelJogo.musica.desligar();
        raiz.setContentPane(new PainelJogo());
        raiz.revalidate();
        raiz.repaint();
        raiz.requestFocus();
    }
}