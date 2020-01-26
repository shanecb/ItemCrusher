package treescript;

import org.rspeer.script.Script;
import org.rspeer.ui.Log;

public abstract class TreeScript extends Script {

    public static boolean shouldStop = false;

    private TreeTask task;

    public TreeScript() {}

    @Override
    public int loop() {
        if (shouldStop) {
            Log.severe("Terminating script.");
            return -1;
        }
        if (task == null) {
            task = onCreateRoot();
            if (task == null) { throw new IllegalStateException("Root task is null."); }
        }

        // used to generate a unique String identifier based on position in tree for each task.
        String currentTaskID = task.getTaskID();

        while (!task.isLeaf()) {
            Log.info(String.format("Current task is: %s", task.toString()));
            if (task.validate()) {
                currentTaskID += "t";
                task = task.successTask();
                if (task == null) { throw new IllegalStateException("Success task is null."); }
            }
            else {
                currentTaskID += "f";
                task = task.failureTask();
                if (task == null) { throw new IllegalStateException("Failure task is null."); }
            }
            task.setTaskID(currentTaskID);
        }
        Log.info(String.format("Current task is: %s", task.toString()));
        int returnValue = task.execute();
        task = null;
        return returnValue;
    }

    public abstract TreeTask onCreateRoot();

    public void setRootTask(TreeTask task) {
        this.task = task;
    }

}
