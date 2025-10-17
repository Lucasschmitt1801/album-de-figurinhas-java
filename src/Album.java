import java.util.ArrayList;
import java.util.List;

public class Album {
    public List<Figurinha> figurinhas;
    public List<Troca> requisicoesTrocas;

    public Album() {
        this.figurinhas = new ArrayList<>();
        this.requisicoesTrocas = new ArrayList<>();
    }
}