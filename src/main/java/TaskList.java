public class TaskList {
    private Task[] tasks;
    private int taskCount;

    public TaskList() {
        this.tasks = new Task[100];
        this.taskCount = 0;
    }

    public void addTask(Task task) {
        tasks[taskCount] = task;
        taskCount++;
    }

    public Task getTask(int index) {
        return tasks[index];
    }

    public int getTaskCount() {
        return taskCount;
    }

    public void markTask(int index) {
        tasks[index].markAsDone();
    }

    public void unmarkTask(int index) {
        tasks[index].markAsNotDone();
    }

    public Task[] getAllTasks() {
        Task[] result = new Task[taskCount];
        for (int i = 0; i < taskCount; i++) {
            result[i] = tasks[i];
        }
        return result;
    }
}
