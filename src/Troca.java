public class Troca {
    public String nomeProponente;
    public int figurinhaRequerida;
    public int figurinhaDisponivel;
    public int status; // 0: Pendente, 1: Aceita, 2: Recusada

    public Troca(String proponente, int requerida, int disponivel) {
        this.nomeProponente = proponente;
        this.figurinhaRequerida = requerida;
        this.figurinhaDisponivel = disponivel;
        this.status = 0;
    }
}