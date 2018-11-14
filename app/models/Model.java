package models;

public abstract class Model {
    protected Long id;

    public Model() { }

    public Model(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
