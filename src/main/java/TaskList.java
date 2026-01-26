import java.util.ArrayList;

public class TaskList {
    private ArrayList<Task> tasks;
    private Storage storage;

    public TaskList() {
        this.storage = new Storage();
        this.tasks = storage.load();
    }

    public void addTask(Task task) {
        tasks.add(task);
        storage.save(tasks);
    }

    public Task getTask(int index) {
        return tasks.get(index);
    }

    public int getTaskCount() {
        return tasks.size();
    }

    public void markTask(int index) {
        tasks.get(index).markAsDone();
        storage.save(tasks);
    }

    public void unmarkTask(int index) {
        tasks.get(index).markAsNotDone();
        storage.save(tasks);
    }

    public Task deleteTask(int index) {
        Task deleted = tasks.remove(index);
        storage.save(tasks);
        return deleted;
    }

    public ArrayList<Task> getAllTasks() {
        return tasks;
    }
}
