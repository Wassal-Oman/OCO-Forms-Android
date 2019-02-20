package om.org.oco.androidocoforms.models;

public class Category {

    // attribute
    private int id;
    private String name;

    // constructors
    public Category() {

    }

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
