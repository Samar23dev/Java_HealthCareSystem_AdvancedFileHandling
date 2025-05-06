import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    public String username, password, name;
    public User(String username, String password, String name) {
        this.username = username;
        this.password = password;
        this.name = name;
    }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getName() { return name; }
}
