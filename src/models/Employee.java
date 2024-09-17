package models;

public class Employee extends User {
    String username;
    String password;
    /// methods ///
    //constructors
    public Employee(String username, String password) {
        setUsername(username);
        setPassword(password);
    }
    //getters & setters
    public String getUsername() {
        return this.username;
    }
    void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return this.password;
    }
    void setPassword(String password) {
        if(password.length()<=1) {
            throw new IllegalArgumentException("Password must be longer than 1 character");
        }
        this.password = password;
    }
    //other methods
    public String toString() {
        return "Employee:\n username: " + this.username + "\n password: " + "*".repeat(this.password.length())+ "\n";
    }

}
