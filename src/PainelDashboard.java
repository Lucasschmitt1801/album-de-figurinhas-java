import javax.swing.*;
import java.awt.*;

public class PainelDashboard extends JPanel {
    private JLabel progressoLabel;
    private JProgressBar progressoBar;
    private App app;

    public PainelDashboard(App app) {
        this.app = app;
        setOpaque(false);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel labelTitulo = new JLabel("Bem-vindo ao seu Pokédex Virtual!");
        labelTitulo.setFont(TemaPokemon.FONTE_TITULO);
        labelTitulo.setForeground(TemaPokemon.TEXTO_CLARO);
        
        progressoLabel = new JLabel();
        progressoLabel.setFont(TemaPokemon.FONTE_NORMAL);
        progressoLabel.setForeground(TemaPokemon.TEXTO_CLARO);
        progressoBar = new JProgressBar(0, 100);

        JButton btnAbrirPacote = new JButton("Abrir um Booster Pack");
        btnAbrirPacote.setFont(TemaPokemon.FONTE_NORMAL.deriveFont(Font.BOLD, 20f));

        gbc.gridy = 0; add(labelTitulo, gbc);
        gbc.gridy = 1; add(progressoLabel, gbc);
        gbc.gridy = 2; add(progressoBar, gbc);
        gbc.insets = new Insets(20, 0, 0, 0);
        gbc.gridy = 3; add(btnAbrirPacote, gbc);

        btnAbrirPacote.addActionListener(e -> {
            app.abrirNovoPacote();
            atualizarEstatisticas();
        });

        addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent event) {
                atualizarEstatisticas();
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent event) {}
            public void ancestorMoved(javax.swing.event.AncestorEvent event) {}
        });
    }

    public void atualizarEstatisticas() {
        Usuario usuario = app.getUsuarioLogado();
        if (usuario == null) return;

        long colados = usuario.album.figurinhas.stream().filter(f -> f.status == 1).map(f -> f.numero).distinct().count();
        int total = app.getFigurinhasModelo().size();
        int porcentagem = (total > 0) ? (int)((colados * 100L) / total) : 0;
        
        progressoBar.setValue(porcentagem);
        progressoLabel.setText(String.format("Progresso do Pokédex: %d / %d (%d%%)", colados, total, porcentagem));
    }
}