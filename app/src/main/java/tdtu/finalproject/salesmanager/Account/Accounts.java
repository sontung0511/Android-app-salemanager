package tdtu.finalproject.salesmanager.Account;

import com.google.firebase.firestore.Exclude;

public class Accounts {
    @Exclude private String id;
    // Same as variable in FireStore
    private String username,password,country,nickname,sex,lastUpdated;

    private Accounts(){}

    public Accounts(String name, String pass, String country, String nickname, String sex, String lastUpdated){
        this.username = name;
        this.password = pass;
        this.country = country;
        this.nickname = nickname;
        this.sex = sex;
        this.lastUpdated = lastUpdated;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getLastUpdated() {
        return this.lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
