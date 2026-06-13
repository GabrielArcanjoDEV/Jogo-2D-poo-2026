package paineis;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MenuGame {
    private BufferedImage imagemFundo;

    private final Rectangle btnJogar  = new Rectangle(189, 400, 247, 48);
    private final Rectangle btnConfig = new Rectangle(189, 464, 247, 48);
    private final Rectangle btnSair   = new Rectangle(189, 528, 247, 48);

    public MenuGame() {
        try {
            imagemFundo = ImageIO.read(getClass().getResource("/imagens/TelaInicio.png"));
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível carregar TelaInicio.png", e);
        }
    }

    public boolean clicouJogar(Point p) { return btnJogar.contains(p);  }
    public boolean clicouConfig(Point p) { return btnConfig.contains(p); }
    public boolean clicouSair(Point p) { return btnSair.contains(p);   }

    public void render(Graphics g) {
        if (imagemFundo != null) {
            g.drawImage(imagemFundo, 0, 0, PainelMapa.LARGURA_JOGO, PainelMapa.ALTURA_JOGO, null);
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(3));
        g2.setFont(new Font("Arial", Font.BOLD, 22));

        desenharBotao(g2, btnJogar,  "NOVO JOGO");
        desenharBotao(g2, btnConfig, "OPÇÕES");
        desenharBotao(g2, btnSair,   "SAIR");
    }


    private void desenharBotao(Graphics2D g2, Rectangle r, String texto) {
        g2.setColor(new Color(0, 0, 0, 170));
        g2.fillRoundRect(r.x, r.y, r.width, r.height, 16, 16);

        g2.setColor(new Color(218, 145, 0));
        g2.drawRoundRect(r.x, r.y, r.width, r.height, 16, 16);

        FontMetrics fm = g2.getFontMetrics();
        int textoX = r.x + (r.width  - fm.stringWidth(texto)) / 2;
        int textoY = r.y + ((r.height - fm.getHeight()) / 2) + fm.getAscent();

        g2.setColor(Color.WHITE);
        g2.drawString(texto, textoX, textoY);
    }
}