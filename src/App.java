import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class App extends JFrame {
    private List<Usuario> usuarios = new ArrayList<>();
    private Map<Integer, Figurinha> figurinhasModelo = new HashMap<>();
    private List<Pagina> paginas = new ArrayList<>();
    private Usuario usuarioLogado = null;

    private CardLayout cardLayout = new CardLayout();
    private JPanel painelConteiner = new JPanel(cardLayout);
    private PainelPrincipal painelPrincipalUsuario;

    public App() {
        Persistencia.carregarDados(usuarios, figurinhasModelo, paginas);

        setTitle("Pokémon Sticker Album");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        PainelLogin painelLogin = new PainelLogin(this);
        painelPrincipalUsuario = new PainelPrincipal(this);
        
        painelConteiner.add(painelLogin, "LOGIN");
        painelConteiner.add(painelPrincipalUsuario, "PRINCIPAL");

        add(painelConteiner);
        cardLayout.show(painelConteiner, "LOGIN");

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                Persistencia.salvarDados(usuarios);
            }
        });
    }

    public void fazerLogin(String username, String password) {
        for (Usuario u : usuarios) {
            if (u.nomeDeUsuario.equals(username) && u.senha.equals(password)) {
                this.usuarioLogado = u;
                painelPrincipalUsuario.atualizarPainel();
                cardLayout.show(painelConteiner, "PRINCIPAL");
                return;
            }
        }
        
        boolean usuarioExiste = usuarios.stream().anyMatch(u -> u.nomeDeUsuario.equals(username));
        if (usuarioExiste) {
            JOptionPane.showMessageDialog(this, "Senha incorreta!", "Erro de Login", JOptionPane.ERROR_MESSAGE);
        } else {
            int resposta = JOptionPane.showConfirmDialog(this, "Treinador não encontrado. Deseja cadastrá-lo?", "Treinador Inexistente", JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                cadastrarUsuario(username, password);
            }
        }
    }

    public void cadastrarUsuario(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome e senha não podem estar em branco.", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }
        boolean existe = usuarios.stream().anyMatch(u -> u.nomeDeUsuario.equals(username));
        if (existe) {
            JOptionPane.showMessageDialog(this, "Este nome de treinador já está em uso.", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }
        usuarios.add(new Usuario(username, password));
        Persistencia.salvarDados(usuarios);
        JOptionPane.showMessageDialog(this, "Treinador '" + username + "' cadastrado! Tente fazer o login.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void fazerLogout() {
        this.usuarioLogado = null;
        cardLayout.show(painelConteiner, "LOGIN");
    }

    public void abrirNovoPacote() {
        Random rand = new Random();
        StringBuilder novasFigurinhasMsg = new StringBuilder("<html><b>Você tirou os seguintes Pokémon:</b><br>");
        for (int i = 0; i < 3; i++) {
            int numSorteado = rand.nextInt(getFigurinhasModelo().size()) + 1;
            Figurinha figModelo = getFigurinhasModelo().get(numSorteado);
            if(figModelo != null) {
                getUsuarioLogado().album.figurinhas.add(new Figurinha(figModelo.numero, figModelo.nome, figModelo.conteudo, figModelo.nroPagina, 0));
                novasFigurinhasMsg.append("<br>- #").append(numSorteado).append(": ").append(figModelo.nome);
            }
        }
        novasFigurinhasMsg.append("</html>");
        JOptionPane.showMessageDialog(this, novasFigurinhasMsg.toString(), "Novo Booster Pack!", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public Usuario getUsuarioLogado() { return this.usuarioLogado; }
    public List<Usuario> getUsuarios() { return this.usuarios; }
    public List<Pagina> getPaginas() { return this.paginas; }
    public Map<Integer, Figurinha> getFigurinhasModelo() { return this.figurinhasModelo; }
    
    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new App().setVisible(true));
    }
}