import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Font;

public class TemaPokemon {
    // Paleta de Cores
    public static final Color AMARELO_POKEMON = new Color(255, 203, 5);
    public static final Color CINZA_ESCURO = new Color(33, 33, 33);
    public static final Color CINZA_CLARO = new Color(55, 55, 55);
    public static final Color TEXTO_CLARO = Color.WHITE;
    public static final Color VERDE_STATUS = new Color(76, 175, 80);
    public static final Color VERMELHO_ERRO = new Color(244, 67, 54);
    
    // Fontes - Definidas com uma fonte padrão do sistema
    public static final Font FONTE_TITULO = new Font("SansSerif", Font.BOLD, 28);
    public static final Font FONTE_NORMAL = new Font("SansSerif", Font.PLAIN, 16);
    
    // Ícones da Interface
    public static ImageIcon LOGO_POKEMON;
    public static ImageIcon AVATAR_ASH;
    public static ImageIcon ICONE_POKEBOLA;

    static {
        // Apenas carrego as imagens
        LOGO_POKEMON = new ImageIcon("imagens/logoPokemon.png");
        AVATAR_ASH = new ImageIcon("imagens/ash.png");
        ICONE_POKEBOLA = new ImageIcon("imagens/pokebola.png");
    }
}