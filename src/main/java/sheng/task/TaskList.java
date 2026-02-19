package sheng.task;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Manages a list of tasks.
 */
public class TaskList {
    private ArrayList<Task> tasks;

    /**
     * Constructs a TaskList with existing tasks.
     *
     * @param tasks The list of tasks.
     */
    public TaskList(ArrayList<Task> tasks) {
        assert tasks != null : "Tasks list cannot be null";
        this.tasks = tasks;
    }

    /**
     * Constructs an empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Adds a task to the list.
     *
     * @param task The task to add.
     */
    public void addTask(Task task) {
        assert task != null : "Task to add cannot be null";
        tasks.add(task);
    }

    public Task getTask(int index) {
        assert index >= 0 && index < tasks.size() : "Index out of bounds: " + index;
        return tasks.get(index);
    }

    public int getTaskCount() {
        return tasks.size();
    }

    public void markTask(int index) {
        assert index >= 0 && index < tasks.size() : "Index out of bounds: " + index;
        tasks.get(index).markAsDone();
    }

    public void unmarkTask(int index) {
        assert index >= 0 && index < tasks.size() : "Index out of bounds: " + index;
        tasks.get(index).markAsNotDone();
    }

    public Task deleteTask(int index) {
        assert index >= 0 && index < tasks.size() : "Index out of bounds: " + index;
        return tasks.remove(index);
    }

    public ArrayList<Task> getAllTasks() {
        return tasks;
    }

    public ArrayList<Task> findTasks(String keyword) {
        assert keyword != null : "Search keyword cannot be null";
        return tasks.stream()
                .filter(task -> task.getDescription().contains(keyword))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Clears all tasks from the list.
     * AI-assisted: GitHub Copilot suggested using ArrayList's built-in clear() method
     * which is more efficient than manually removing items one by one.
     */
    public void clearAllTasks() {
        tasks.clear();
    }
}
