package tdtu.finalproject.salesmanager.Menu;

public class Menu_Items {
    // Same as variable in FireStore
    private String name,type,img_src;
    private int price;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return this.price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImg_src() {
        return this.img_src;
    }

    public void setImg_src(String img_src) {
        this.img_src = img_src;
    }
}
