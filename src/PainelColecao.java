import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class PainelColecao extends JPanel {
    private App app;
    private JLabel labelStatus;

    private DefaultListModel<String> minhasFigurinhasModel;
    private JComboBox<String> comboUsuariosTroca;
    private DefaultListModel<String> figurinhasParceiroModel;
    private DefaultListModel<Troca> revisarTrocasModel;


    public PainelColecao(App app) {
        this.app = app;
        setOpaque(false);
        setLayout(new BorderLayout());

        JTabbedPane abas = new JTabbedPane();
        
        abas.addTab("Meus Pokémon", criarPainelFigurinhas());
        abas.addTab("Propor Troca", criarPainelProporTroca());
        abas.addTab("Revisar Trocas", criarPainelRevisarTrocas());
        add(abas, BorderLayout.CENTER);

        labelStatus = new JLabel(" ", SwingConstants.CENTER);
        labelStatus.setFont(new Font("SansSerif", Font.ITALIC, 14));
        add(labelStatus, BorderLayout.SOUTH);

        addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent e) {
                atualizarTodosOsDados();
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent e) {}
            public void ancestorMoved(javax.swing.event.AncestorEvent e) {}
        });
    }
    
    public void atualizarTodosOsDados() {
        if (app.getUsuarioLogado() == null) return;

        // Atualiza a lista da primeira aba
        minhasFigurinhasModel.clear();
        Map<Integer, Long> contagem = app.getUsuarioLogado().album.figurinhas.stream()
            .collect(Collectors.groupingBy(f -> f.numero, Collectors.counting()));
        Map<Integer, Figurinha> unicas = app.getUsuarioLogado().album.figurinhas.stream()
            .collect(Collectors.toMap(f -> f.numero, f -> f, (fig1, fig2) -> fig1));
        unicas.keySet().stream().sorted().forEach(numero -> {
            Figurinha f = unicas.get(numero);
            String statusStr = (f.status == 0) ? "Na Box" : (f.status == 1) ? "Registrado" : "Para Troca";
            String contagemStr = (contagem.get(numero) > 1) ? " (x" + contagem.get(numero) + ")" : "";
            minhasFigurinhasModel.addElement(String.format("#%03d %s%s (%s)", f.numero, f.nome, contagemStr, statusStr));
        });

        // Atualiza a lista de usuários para troca na segunda aba
        comboUsuariosTroca.removeAllItems();
        for (Usuario u : app.getUsuarios()) {
            if (!u.nomeDeUsuario.equals(app.getUsuarioLogado().nomeDeUsuario)) {
                comboUsuariosTroca.addItem(u.nomeDeUsuario);
            }
        }
        
        // Atualiza a lista de trocas a revisar na terceira aba
        revisarTrocasModel.clear();
        app.getUsuarioLogado().album.requisicoesTrocas.stream()
            .filter(t -> t.status == 0)
            .forEach(revisarTrocasModel::addElement);
    }

    private void mostrarStatus(String mensagem, Color cor) {
        labelStatus.setText(mensagem);
        labelStatus.setForeground(cor);
        Timer timer = new Timer(3000, e -> labelStatus.setText(" "));
        timer.setRepeats(false);
        timer.start();
    }

    private JPanel criarPainelFigurinhas() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        minhasFigurinhasModel = new DefaultListModel<>();
        JList<String> listaFigurinhas = new JList<>(minhasFigurinhasModel);
        painel.add(new JScrollPane(listaFigurinhas), BorderLayout.CENTER);
        
        JPanel painelAcoes = new JPanel(new GridLayout(0, 2, 10, 10));
        JTextField campoColar = new JTextField();
        JButton btnColar = new JButton("Registrar no Pokédex");
        JTextField campoDisponibilizar = new JTextField();
        JButton btnDisponibilizar = new JButton("Disponibilizar para Troca");

        painelAcoes.add(new JLabel("Número para Registrar:")); painelAcoes.add(campoColar);
        painelAcoes.add(new JLabel("")); painelAcoes.add(btnColar);
        painelAcoes.add(new JSeparator()); painelAcoes.add(new JSeparator());
        painelAcoes.add(new JLabel("Número para Disponibilizar:")); painelAcoes.add(campoDisponibilizar);
        painelAcoes.add(new JLabel("")); painelAcoes.add(btnDisponibilizar);
        painel.add(painelAcoes, BorderLayout.SOUTH);
        
        btnColar.addActionListener(e -> {
            try {
                int num = Integer.parseInt(campoColar.getText());
                Optional<Figurinha> figOpt = app.getUsuarioLogado().album.figurinhas.stream().filter(f -> f.numero == num && f.status != 1).findFirst();
                if (figOpt.isPresent()) {
                    figOpt.get().status = 1;
                    atualizarTodosOsDados();
                    mostrarStatus("Pokémon registrado com sucesso!", TemaPokemon.VERDE_STATUS);
                    campoColar.setText("");
                } else {
                    mostrarStatus("Você não possui este Pokémon ou ele já está registrado.", TemaPokemon.VERMELHO_ERRO);
                }
            } catch (NumberFormatException ex) {
                mostrarStatus("Por favor, digite um número válido.", TemaPokemon.VERMELHO_ERRO);
            }
        });

        btnDisponibilizar.addActionListener(e -> {
            try {
                int num = Integer.parseInt(campoDisponibilizar.getText());
                Optional<Figurinha> figOpt = app.getUsuarioLogado().album.figurinhas.stream().filter(f -> f.numero == num && f.status == 0).findFirst();
                if (figOpt.isPresent()) {
                    figOpt.get().status = 2;
                    atualizarTodosOsDados();
                    mostrarStatus("Pokémon disponibilizado para troca!", TemaPokemon.VERDE_STATUS);
                    campoDisponibilizar.setText("");
                } else {
                    mostrarStatus("Você não possui este Pokémon na Box para trocar.", TemaPokemon.VERMELHO_ERRO);
                }
            } catch (NumberFormatException ex) {
                mostrarStatus("Por favor, digite um número válido.", TemaPokemon.VERMELHO_ERRO);
            }
        });
        
        return painel;
    }

    private JPanel criarPainelProporTroca() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        figurinhasParceiroModel = new DefaultListModel<>();
        JList<String> listaFigurinhasDisponiveis = new JList<>(figurinhasParceiroModel);
        
        comboUsuariosTroca = new JComboBox<>();
        
        comboUsuariosTroca.addActionListener(e -> {
            figurinhasParceiroModel.clear();
            String nomeParceiro = (String) comboUsuariosTroca.getSelectedItem();
            if (nomeParceiro == null) return;
            
            app.getUsuarios().stream()
                .filter(u -> u.nomeDeUsuario.equals(nomeParceiro))
                .findFirst().ifPresent(parceiro -> {
                    parceiro.album.figurinhas.stream()
                        .filter(f -> f.status == 2)
                        .sorted((f1, f2) -> Integer.compare(f1.numero, f2.numero))
                        .forEach(f -> figurinhasParceiroModel.addElement(String.format("#%03d %s", f.numero, f.nome)));
                });
        });

        JPanel painelForm = new JPanel(new GridLayout(0, 2, 5, 5));
        JTextField campoFigurinhaQuero = new JTextField();
        JTextField campoFigurinhaOfereco = new JTextField();
        JButton btnPropor = new JButton("Propor Troca");

        painelForm.add(new JLabel("Trocar com o Treinador:")); painelForm.add(comboUsuariosTroca);
        painelForm.add(new JLabel("Pokémon que eu quero (No.):")); painelForm.add(campoFigurinhaQuero);
        painelForm.add(new JLabel("Meu Pokémon que ofereço (No.):")); painelForm.add(campoFigurinhaOfereco);
        painelForm.add(new JLabel("")); painelForm.add(btnPropor);

        btnPropor.addActionListener(e -> {
            String nomeParceiro = (String) comboUsuariosTroca.getSelectedItem();
            try {
                int quero = Integer.parseInt(campoFigurinhaQuero.getText());
                int ofereco = Integer.parseInt(campoFigurinhaOfereco.getText());

                Optional<Usuario> parceiroOpt = app.getUsuarios().stream().filter(u -> u.nomeDeUsuario.equals(nomeParceiro)).findFirst();
                if (parceiroOpt.isPresent()) {
                    parceiroOpt.get().album.requisicoesTrocas.add(new Troca(app.getUsuarioLogado().nomeDeUsuario, quero, ofereco));
                    mostrarStatus("Proposta de troca enviada para " + nomeParceiro, TemaPokemon.VERDE_STATUS);
                    campoFigurinhaQuero.setText("");
                    campoFigurinhaOfereco.setText("");
                }
            } catch (NumberFormatException ex) {
                mostrarStatus("Por favor, digite números válidos para os Pokémon.", TemaPokemon.VERMELHO_ERRO);
            }
        });

        painel.add(painelForm, BorderLayout.NORTH);
        painel.add(new JScrollPane(listaFigurinhasDisponiveis), BorderLayout.CENTER);
        
        return painel;
    }

    private JPanel criarPainelRevisarTrocas() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        revisarTrocasModel = new DefaultListModel<>();
        JList<Troca> listaTrocas = new JList<>(revisarTrocasModel);
        
        listaTrocas.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                Troca troca = (Troca) value;
                label.setText(String.format("Proposta de %s: Quer #%d / Oferece #%d", troca.nomeProponente, troca.figurinhaRequerida, troca.figurinhaDisponivel));
                return label;
            }
        });
        
        JButton btnAceitar = new JButton("Aceitar Troca");
        JButton btnRecusar = new JButton("Recusar Troca");
        JPanel painelBotoes = new JPanel();
        painelBotoes.add(btnAceitar);
        painelBotoes.add(btnRecusar);
        
        btnAceitar.addActionListener(e -> {
            Troca trocaSelecionada = listaTrocas.getSelectedValue();
            if (trocaSelecionada == null) {
                mostrarStatus("Selecione uma proposta para aceitar.", TemaPokemon.VERMELHO_ERRO);
                return;
            }
            
            Optional<Usuario> proponenteOpt = app.getUsuarios().stream().filter(u -> u.nomeDeUsuario.equals(trocaSelecionada.nomeProponente)).findFirst();
            if (proponenteOpt.isPresent()) {
                Usuario proponente = proponenteOpt.get();
                Optional<Figurinha> minhaFigOpt = app.getUsuarioLogado().album.figurinhas.stream().filter(f -> f.numero == trocaSelecionada.figurinhaRequerida && f.status == 2).findFirst();
                Optional<Figurinha> figProponenteOpt = proponente.album.figurinhas.stream().filter(f -> f.numero == trocaSelecionada.figurinhaDisponivel && f.status == 2).findFirst();

                if (minhaFigOpt.isPresent() && figProponenteOpt.isPresent()) {
                    Figurinha fig1 = minhaFigOpt.get();
                    Figurinha fig2 = figProponenteOpt.get();
                    
                    fig1.status = 0;
                    fig2.status = 0;
                    
                    app.getUsuarioLogado().album.figurinhas.remove(fig1);
                    proponente.album.figurinhas.remove(fig2);

                    app.getUsuarioLogado().album.figurinhas.add(fig2);
                    proponente.album.figurinhas.add(fig1);
                    
                    trocaSelecionada.status = 1;
                    mostrarStatus("Troca aceita com sucesso!", TemaPokemon.VERDE_STATUS);
                    atualizarTodosOsDados();
                } else {
                    mostrarStatus("Erro: Uma das figurinhas não está mais disponível.", TemaPokemon.VERMELHO_ERRO);
                    trocaSelecionada.status = 2;
                    atualizarTodosOsDados();
                }
            }
        });
        
        btnRecusar.addActionListener(e -> {
             if (listaTrocas.getSelectedValue() == null) {
                mostrarStatus("Selecione uma proposta para recusar.", TemaPokemon.VERMELHO_ERRO);
                return;
            }
            listaTrocas.getSelectedValue().status = 2;
            mostrarStatus("Troca recusada.", TemaPokemon.VERDE_STATUS);
            atualizarTodosOsDados();
        });

        painel.add(new JScrollPane(listaTrocas), BorderLayout.CENTER);
        painel.add(painelBotoes, BorderLayout.SOUTH);
        return painel;
    }
}