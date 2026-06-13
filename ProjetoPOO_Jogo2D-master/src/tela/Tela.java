package tela;

import paineis.PainelJogo;
import paineis.PainelMapa;

import javax.swing.*;
import java.awt.*;

public class Tela extends JFrame {

    public Tela() {
        setTitle("Key Hunt");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        PainelJogo painel = new PainelJogo();
        painel.setPreferredSize(new Dimension(PainelMapa.LARGURA_JOGO, PainelMapa.ALTURA_JOGO));
        setContentPane(painel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Tela::new);
    }
}