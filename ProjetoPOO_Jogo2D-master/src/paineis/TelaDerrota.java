package paineis;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class TelaDerrota extends JPanel {
    private static final int LARGURA_TELA = PainelMapa.LARGURA_JOGO;
    private static final int ALTURA_TELA = PainelMapa.ALTURA_JOGO;

    private static final Rectangle BOTAO_VOLTAR = new Rectangle(516, 632, 247, 48);
    private BufferedImage imagemFundo;
    private final JFrame raiz;

    public TelaDerrota(JFrame janela) {
        this.raiz = janela;
        setPreferredSize(new Dimension(LARGURA_TELA, ALTURA_TELA));
        setLayout(null);
        carregarImagem();
        configurarEventos();
        PainelJogo.musica.tocarUmaVez("/sons/musicaDerrota.wav");
    }

    private void carregarImagem() {
        try {
            imagemFundo = ImageIO.read(getClass().getResource("/imagens/TelaDerrota.png"));
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar TelaDerrota.png", e);
        }
    }

    private void configurarEventos() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (BOTAO_VOLTAR.contains(e.getPoint())) {
                    voltarInicio();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        if (imagemFundo != null) {
            g2.drawImage(imagemFundo, 0, 0, LARGURA_TELA, ALTURA_TELA, null);
        }
        desenharBotao(g2, BOTAO_VOLTAR, "VOLTAR");
    }

    private void desenharBotao(Graphics2D g2, Rectangle botao, String texto) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(3));
        g2.setFont(new Font("Arial", Font.BOLD, 22));

        g2.setColor(new Color(0, 0, 0, 170));
        g2.fillRoundRect(botao.x, botao.y, botao.width, botao.height, 16, 16);
        g2.setColor(new Color(102, 51, 153));
        g2.drawRoundRect(botao.x, botao.y, botao.width, botao.height, 16, 16);
        FontMetrics fm = g2.getFontMetrics();

        int textoX = botao.x + (botao.width - fm.stringWidth(texto)) / 2;
        int textoY = botao.y + ((botao.height - fm.getHeight()) / 2) + fm.getAscent();
        g2.setColor(Color.WHITE);
        g2.drawString(texto, textoX, textoY);
    }

    private void voltarInicio() {
        PainelJogo.musica.desligar();
        raiz.setContentPane(new PainelJogo());
        raiz.revalidate();
        raiz.repaint();
        raiz.requestFocusInWindow();
    }
}