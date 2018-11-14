package models.context;

public class IntrinsicContext extends Context {

    private Entity bearer;

    public IntrinsicContext() {
    }

    public IntrinsicContext(Entity bearer) {
        this.bearer = bearer;
    }

    public IntrinsicContext(Long cid, Entity bearer) {
        this.id = cid;
        this.bearer = bearer;
    }

    public Entity getBearer() {
        return bearer;
    }

    public void setBearer(Entity bearer) {
        this.bearer = bearer;
    }

}
