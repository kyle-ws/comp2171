package user_management;

public class User {
    private String userID;
    private String fName, mName, lName, userName, key;
    private Role role;

    public User() {
    }

    public User(String userID, String fName, String mName, String lName, String userName, String key) {
        this.userID = userID;
        this.fName = fName;
        this.mName = mName;
        this.lName = lName;
        this.userName = userName;
        this.key = key;
        if (userID.charAt(0)=='D') {
            this.role = new Role("Operator");
        }
        else if (userID.charAt(0) == 'R') {
            this.role = new Role("Spectator");
        }
    }

    // Getters

    public String getFName() {
        return fName;
    }

    public String getMName() {
        return mName;
    }

    public String getLName() {
        return lName;
    }

    public String getID() {
        return userID;
    }

    public Role getRole() {
        return role;
    }

    public String getRoleTitle() {
        return role.getTitle();
    }

    public String getUserName() {
        return userName;
    }

    // Setters

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
