package treescript;

public abstract class TreeTask {

    /// A unique identifier for this task's position in the tree. Defaults to the class name.
    private String taskID = this.getClass().getSimpleName() + "--";

    /// Defines the procedure to determine whether to run the `successTask()` or `failureTask()`.
    public abstract boolean validate();

    /// The task that will be run if `validate()` returns true.
    public abstract TreeTask successTask();

    /// The task that will be run if `validate()` returns false.
    public abstract TreeTask failureTask();

    public abstract boolean isLeaf();

    public abstract int execute();

    public abstract String toString();

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }
}
