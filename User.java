public class User {
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
