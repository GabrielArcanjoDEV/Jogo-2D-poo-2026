package paineis;

import sons.Musica;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PainelJogo extends JPanel {
    private final MenuGame menu = new MenuGame();
    public static Musica musica = new Musica();

    public PainelJogo() {
        musica.tocar("/sons/musicaJogo.wav");

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point ponto = e.getPoint();
                JFrame janela = (JFrame) SwingUtilities.getWindowAncestor(PainelJogo.this);

                if (menu.clicouJogar(ponto)) {
                    janela.setContentPane(new PainelMapa());
                    janela.revalidate();
                }
                if (menu.clicouConfig(ponto)) {
                    janela.setContentPane(new TelaOpcoes(musica));
                    janela.revalidate();
                }
                if (menu.clicouSair(ponto)) {
                    System.exit(0);
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        menu.render(g);
    }
}