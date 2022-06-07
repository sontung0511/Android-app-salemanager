package tdtu.finalproject.salesmanager.History;

import com.google.firebase.firestore.Exclude;

public class History {
    @Exclude String id;
    private String bill;
    private String username;

    private History(){}

    public History(String name,String bill){
        this.username = name;
        this.bill = bill;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBill() {
        return bill;
    }

    public void setBill(String bill) {
        this.bill = bill;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
