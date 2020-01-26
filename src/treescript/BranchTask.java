package treescript;

public abstract class BranchTask extends TreeTask {

    /// `BranchTask`s don't perform any actions of their own.
    public final int execute() {
        return 0;
    }

    public final boolean isLeaf() {
        return false;
    }

    public String toString() {
        return String.format("BranchTask -- %s", this.getClass().getSimpleName());
    }

}
