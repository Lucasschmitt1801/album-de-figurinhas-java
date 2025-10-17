import javax.swing.*;
import java.awt.*;

public class PainelPrincipal extends JPanel {
    private CardLayout cardLayout = new CardLayout();
    private JPanel painelConteudo;
    private JLabel labelUsuario;
    private PainelDashboard painelDashboard;

    public PainelPrincipal(App app) {
        this.app = app; // Armazena a referência ao App
        setLayout(new BorderLayout(10, 10));
        setBackground(TemaPokemon.CINZA_ESCURO);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- SIDEBAR ---
        JPanel painelSidebar = new JPanel();
        painelSidebar.setLayout(new BoxLayout(painelSidebar, BoxLayout.Y_AXIS));
        painelSidebar.setOpaque(false);
        
        JLabel labelAvatar = new JLabel(new ImageIcon(TemaPokemon.AVATAR_ASH.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
        labelAvatar.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelUsuario = new JLabel("Treinador: ");
        labelUsuario.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelUsuario.setForeground(TemaPokemon.TEXTO_CLARO);

        JButton btnDashboard = new JButton("Dashboard");
        JButton btnVerAlbum = new JButton("Meu Álbum");
        JButton btnGerenciar = new JButton("Minha Coleção");
        JButton btnLogout = new JButton("Logout");

        painelSidebar.add(labelAvatar);
        painelSidebar.add(labelUsuario);
        painelSidebar.add(Box.createRigidArea(new Dimension(0, 20)));
        painelSidebar.add(btnDashboard);
        painelSidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        painelSidebar.add(btnVerAlbum);
        painelSidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        painelSidebar.add(btnGerenciar);
        painelSidebar.add(Box.createVerticalGlue());
        painelSidebar.add(btnLogout);
        add(painelSidebar, BorderLayout.WEST);

        // --- PAINEL DE CONTEÚDO (com CardLayout) ---
        painelConteudo = new JPanel(cardLayout);
        painelConteudo.setOpaque(false);
        
        painelDashboard = new PainelDashboard(app);
        PainelAlbum painelAlbum = new PainelAlbum(app);
        PainelColecao painelColecao = new PainelColecao(app);

        painelConteudo.add(painelDashboard, "DASHBOARD");
        painelConteudo.add(painelAlbum, "ALBUM");
        painelConteudo.add(painelColecao, "COLECAO");
        add(painelConteudo, BorderLayout.CENTER);

        // Ações dos botões da sidebar agora trocam o painel visível
        btnDashboard.addActionListener(e -> cardLayout.show(painelConteudo, "DASHBOARD"));
        btnVerAlbum.addActionListener(e -> cardLayout.show(painelConteudo, "ALBUM"));
        btnGerenciar.addActionListener(e -> cardLayout.show(painelConteudo, "COLECAO"));
        btnLogout.addActionListener(e -> app.fazerLogout());
        
        addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent event) {
                atualizarPainel();
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent event) {}
            public void ancestorMoved(javax.swing.event.AncestorEvent event) {}
        });
    }
    
    public void atualizarPainel() {
        if (app.getUsuarioLogado() != null) {
            labelUsuario.setText("Treinador: " + app.getUsuarioLogado().nomeDeUsuario);
            painelDashboard.atualizarEstatisticas();
        }
    }

    private App app; // Adicione esta linha
}