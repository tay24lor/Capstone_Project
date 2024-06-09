package model;

public class User {

    private String name;

    private String pass;

    public User(String username, String password) {
        this.name = username;
        this.pass = password;
    }

    public String getName() {
        return name;
    }

    public String getPass() {
        return pass;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
