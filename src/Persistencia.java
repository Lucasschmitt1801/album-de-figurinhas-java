import java.io.*;
import java.util.List;
import java.util.Map;

public class Persistencia {
    private static final String ARQUIVO_USUARIOS = "usuarios.csv";
    private static final String ARQUIVO_FIGURINHAS = "figurinhas.csv";

    public static void carregarDados(List<Usuario> usuarios, Map<Integer, Figurinha> figurinhasModelo, List<Pagina> paginas) {
        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_USUARIOS))) {
            String line = br.readLine(); 
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length == 2) usuarios.add(new Usuario(values[0], values[1]));
            }
        } catch (IOException e) { /* Arquivo pode não existir */ }

        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_FIGURINHAS))) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] v = line.split(",");
                if (v.length == 6) {
                    for (Usuario u : usuarios) {
                        if (u.nomeDeUsuario.equals(v[0])) {
                            u.album.figurinhas.add(new Figurinha(Integer.parseInt(v[1]), v[2], v[3], Integer.parseInt(v[5]), Integer.parseInt(v[4])));
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) { /* Arquivo pode não existir */ }
        
        if (figurinhasModelo.isEmpty()) {
            figurinhasModelo.put(1, new Figurinha(1, "Bulbasaur", "Seed Pokémon", 1, 0));
            figurinhasModelo.put(2, new Figurinha(2, "Ivysaur", "Seed Pokémon", 1, 0));
            figurinhasModelo.put(3, new Figurinha(3, "Venusaur", "Seed Pokémon", 1, 0));
            figurinhasModelo.put(4, new Figurinha(4, "Charmander", "Lizard Pokémon", 1, 0));
            figurinhasModelo.put(5, new Figurinha(5, "Charmeleon", "Flame Pokémon", 1, 0));
            figurinhasModelo.put(6, new Figurinha(6, "Charizard", "Flame Pokémon", 1, 0));
            figurinhasModelo.put(7, new Figurinha(7, "Squirtle", "Tiny Turtle Pokémon", 1, 0));
            figurinhasModelo.put(8, new Figurinha(8, "Wartortle", "Turtle Pokémon", 1, 0));
            figurinhasModelo.put(9, new Figurinha(9, "Blastoise", "Shellfish Pokémon", 1, 0));
            figurinhasModelo.put(10, new Figurinha(10, "Caterpie", "Worm Pokémon", 1, 0));
            figurinhasModelo.put(11, new Figurinha(11, "Metapod", "Cocoon Pokémon", 2, 0));
            figurinhasModelo.put(12, new Figurinha(12, "Butterfree", "Butterfly Pokémon", 2, 0));
            figurinhasModelo.put(13, new Figurinha(13, "Weedle", "Hairy Bug Pokémon", 2, 0));
            figurinhasModelo.put(14, new Figurinha(14, "Kakuna", "Cocoon Pokémon", 2, 0));
            figurinhasModelo.put(15, new Figurinha(15, "Beedrill", "Poison Bee Pokémon", 2, 0));
            figurinhasModelo.put(16, new Figurinha(16, "Pidgey", "Tiny Bird Pokémon", 2, 0));
            figurinhasModelo.put(17, new Figurinha(17, "Pidgeotto", "Bird Pokémon", 2, 0));
            figurinhasModelo.put(18, new Figurinha(18, "Pidgeot", "Bird Pokémon", 2, 0));
            figurinhasModelo.put(19, new Figurinha(19, "Rattata", "Mouse Pokémon", 2, 0));
            figurinhasModelo.put(20, new Figurinha(20, "Raticate", "Mouse Pokémon", 2, 0));
        }

        if (paginas.isEmpty()) {
            paginas.add(new Pagina("Kanto Starters & Evolutions", 1, 10));
            paginas.add(new Pagina("Early Kanto Pokémon", 11, 20));
        }
    }

    public static void salvarDados(List<Usuario> usuarios) {
        try (FileWriter writer = new FileWriter(ARQUIVO_USUARIOS)) {
            writer.append("nomeDeUsuario,senha\n");
            for (Usuario u : usuarios) {
                writer.append(u.nomeDeUsuario).append(",").append(u.senha).append("\n");
            }
        } catch (IOException e) { e.printStackTrace(); }

        try (FileWriter writer = new FileWriter(ARQUIVO_FIGURINHAS)) {
            writer.append("nomeDeUsuario,numero,nome,conteudo,status,nroPagina\n");
            for (Usuario u : usuarios) {
                for (Figurinha f : u.album.figurinhas) {
                    writer.append(String.join(",", u.nomeDeUsuario, String.valueOf(f.numero), f.nome, f.conteudo, String.valueOf(f.status), String.valueOf(f.nroPagina))).append("\n");
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
    }
}