public class Usuario {
    public String nomeDeUsuario;
    public String senha;
    public Album album;

    public Usuario(String nomeDeUsuario, String senha) {
        this.nomeDeUsuario = nomeDeUsuario;
        this.senha = senha;
        this.album = new Album();
    }
}