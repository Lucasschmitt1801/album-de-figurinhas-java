import javax.swing.*;
import java.awt.*;

public class PainelLogin extends JPanel {
    public PainelLogin(App app) {
        setLayout(new GridBagLayout());
        setBackground(TemaPokemon.CINZA_ESCURO);
        
        JPanel painelCentral = new JPanel(new GridBagLayout());
        painelCentral.setBackground(TemaPokemon.CINZA_CLARO);
        painelCentral.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(TemaPokemon.AMARELO_POKEMON, 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel labelLogo = new JLabel(TemaPokemon.LOGO_POKEMON);
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        painelCentral.add(labelLogo, gbc);

        JTextField campoUsuario = new JTextField(15);
        campoUsuario.setFont(TemaPokemon.FONTE_NORMAL);
        JPasswordField campoSenha = new JPasswordField(15);
        campoSenha.setFont(TemaPokemon.FONTE_NORMAL);
        
        JLabel labelUser = new JLabel("Treinador:");
        labelUser.setFont(TemaPokemon.FONTE_NORMAL);
        JLabel labelPass = new JLabel("Senha:");
        labelPass.setFont(TemaPokemon.FONTE_NORMAL);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        painelCentral.add(labelUser, gbc);
        gbc.gridx = 1;
        painelCentral.add(campoUsuario, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        painelCentral.add(labelPass, gbc);
        gbc.gridx = 1;
        painelCentral.add(campoSenha, gbc);
        
        JButton btnEntrar = new JButton("Entrar");
        btnEntrar.setFont(TemaPokemon.FONTE_NORMAL);
        
        JButton btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.setFont(TemaPokemon.FONTE_NORMAL);

        JPanel painelBotoes = new JPanel(new GridLayout(1, 2, 10, 0));
        painelBotoes.setOpaque(false);
        painelBotoes.add(btnEntrar);
        painelBotoes.add(btnCadastrar);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        painelCentral.add(painelBotoes, gbc);

        campoSenha.addActionListener(e -> btnEntrar.doClick());
        btnEntrar.addActionListener(e -> app.fazerLogin(campoUsuario.getText(), new String(campoSenha.getPassword())));
        btnCadastrar.addActionListener(e -> app.cadastrarUsuario(campoUsuario.getText(), new String(campoSenha.getPassword())));

        add(painelCentral);
    }
}