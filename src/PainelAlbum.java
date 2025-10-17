import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class PainelAlbum extends JPanel {
    private App app;
    private int paginaAtualIdx = 0;
    private JLabel labelTituloPagina;
    private JPanel painelGrade;

    public PainelAlbum(App app) {
        this.app = app;
        setOpaque(false);
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JPanel painelNavegacao = new JPanel();
        painelNavegacao.setOpaque(false);
        JButton btnAnterior = new JButton("<< Anterior");
        JButton btnProxima = new JButton("Próxima >>");
        labelTituloPagina = new JLabel();
        labelTituloPagina.setForeground(TemaPokemon.TEXTO_CLARO);
        painelNavegacao.add(btnAnterior);
        painelNavegacao.add(Box.createHorizontalStrut(20));
        painelNavegacao.add(labelTituloPagina);
        painelNavegacao.add(Box.createHorizontalStrut(20));
        painelNavegacao.add(btnProxima);
        add(painelNavegacao, BorderLayout.NORTH);

        painelGrade = new JPanel(new GridLayout(0, 5, 10, 10));
        painelGrade.setOpaque(false);
        
        JScrollPane scrollPane = new JScrollPane(painelGrade);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        btnAnterior.addActionListener(e -> {
            if (paginaAtualIdx > 0) {
                paginaAtualIdx--;
                atualizarVisualizacao();
            }
        });
        btnProxima.addActionListener(e -> {
            if (app.getPaginas() != null && paginaAtualIdx < app.getPaginas().size() - 1) {
                paginaAtualIdx++;
                atualizarVisualizacao();
            }
        });
        
        addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent event) {
                atualizarVisualizacao();
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent event) {}
            public void ancestorMoved(javax.swing.event.AncestorEvent event) {}
        });
    }

    private ImageIcon redimensionarIcone(ImageIcon original, int largura, int altura) {
        if (original == null || original.getImage() == null) return null;
        Image srcImg = original.getImage();
        BufferedImage resizedImg = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, largura, altura, null);
        g2.dispose();
        return new ImageIcon(resizedImg);
    }

    public void atualizarVisualizacao() {
        if (app.getPaginas() == null || app.getPaginas().isEmpty() || app.getUsuarioLogado() == null) return;
        Pagina paginaAtual = app.getPaginas().get(paginaAtualIdx);
        labelTituloPagina.setText(String.format("Página %d: %s", paginaAtualIdx + 1, paginaAtual.titulo));
        painelGrade.removeAll();

        for (int num = paginaAtual.minNro; num <= paginaAtual.maxNro; num++) {
            final int numeroFigurinha = num;
            Figurinha figurinhaDoUsuario = null;
            for (Figurinha f : app.getUsuarioLogado().album.figurinhas) {
                if (f.numero == numeroFigurinha) {
                    figurinhaDoUsuario = f;
                    break;
                }
            }
            JLabel slot = new JLabel();
            slot.setPreferredSize(new Dimension(100, 100));
            slot.setHorizontalAlignment(SwingConstants.CENTER);
            slot.setVerticalAlignment(SwingConstants.CENTER);
            ImageIcon icon;
            if (figurinhaDoUsuario != null) {
                icon = new ImageIcon("imagens/" + numeroFigurinha + ".png");
                slot.setToolTipText(String.format("#%d %s", numeroFigurinha, figurinhaDoUsuario.nome));
                if (figurinhaDoUsuario.status == 1) {
                    slot.setBorder(BorderFactory.createLineBorder(TemaPokemon.VERDE_STATUS, 3));
                } else {
                    slot.setBorder(BorderFactory.createLineBorder(TemaPokemon.AMARELO_POKEMON, 3));
                }
            } else {
                icon = TemaPokemon.ICONE_POKEBOLA;
                slot.setToolTipText("Pokémon não encontrado");
                slot.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            }
            slot.setIcon(redimensionarIcone(icon, 90, 90));
            painelGrade.add(slot);
        }
        painelGrade.revalidate();
        painelGrade.repaint();
        
    }
}