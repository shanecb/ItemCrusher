package treescript;

public abstract class LeafTask extends TreeTask {

    public LeafTask() {}

    public final TreeTask successTask() {
        return null;
    }

    public final TreeTask failureTask() {
        return null;
    }

    public final boolean isLeaf() {
        return true;
    }

    public final boolean validate() {
        return true;
    }

    public String toString() {
        return String.format("LeafTask -- %s", this.getClass().getSimpleName());
    }

}
