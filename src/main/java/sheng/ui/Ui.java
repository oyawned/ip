package sheng.ui;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import sheng.task.Task;

/**
 * Handles user interface interactions.
 */
public class Ui {
    private static final String LINE = "____________________________________________________________";
    private Scanner scanner;

    /**
     * Constructs a Ui object.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    public void showWelcome() {
        showLine();
        System.out.println(" Hello! I'm Sheng!");
        System.out.println(" What can I do for you today?");
        showLine();
    }

    public void showGoodbye() {
        showLine();
        System.out.println(" Bye! Hope to see you again soon! Have a great day!");
        showLine();
    }

    public void showLine() {
        System.out.println(LINE);
    }

    public String readCommand() {
        return scanner.nextLine();
    }

    public void showTaskAdded(Task task, int taskCount) {
        showLine();
        System.out.println(" Awesome! I've added this task:");
        System.out.println("   " + task);
        String taskWord = taskCount == 1 ? "task" : "tasks";
        System.out.println(" Now you have " + taskCount + " " + taskWord + " in the list.");
        showLine();
    }

    public void showTaskList(ArrayList<Task> tasks) {
        showLine();
        System.out.println(" Here are the tasks in your list:");
        AtomicInteger counter = new AtomicInteger(1);
        tasks.stream()
                .forEach(task -> System.out.println(" " + counter.getAndIncrement() + "." + task));
        showLine();
    }

    public void showTaskMarked(Task task) {
        showLine();
        System.out.println(" Woohoo! I've marked this task as done:");
        System.out.println("   " + task);
        showLine();
    }

    public void showTaskUnmarked(Task task) {
        showLine();
        System.out.println(" No worries! I've marked this task as not done yet:");
        System.out.println("   " + task);
        showLine();
    }

    public void showTaskDeleted(Task task, int taskCount) {
        showLine();
        System.out.println(" Okay, I've removed this task:");
        System.out.println("   " + task);
        String taskWord = taskCount == 1 ? "task" : "tasks";
        System.out.println(" Now you have " + taskCount + " " + taskWord + " in the list.");
        showLine();
    }

    public void showError(String message) {
        showLine();
        System.out.println(" " + message);
        showLine();
    }

    public void showMatchingTasks(ArrayList<Task> tasks) {
        showLine();
        if (tasks.isEmpty()) {
            System.out.println(" No matching tasks found!");
        } else {
            System.out.println(" Here are the matching tasks in your list:");
            AtomicInteger counter = new AtomicInteger(1);
            tasks.stream()
                    .forEach(task -> System.out.println(" " + counter.getAndIncrement() + "." + task));
        }
        showLine();
    }

    public void close() {
        scanner.close();
    }

    // Formatting methods for GUI responses

    /**
     * Formats a goodbye message.
     *
     * @return Formatted goodbye message.
     */
    public String formatGoodbyeMessage() {
        return "Bye! Hope to see you again soon! Have a great day!";
    }

    /**
     * Formats the task list for display.
     *
     * @param tasks The list of tasks.
     * @return Formatted task list string.
     */
    public String formatTaskList(ArrayList<Task> tasks) {
        if (tasks.isEmpty()) {
            return "You have no tasks in your list!";
        }
        StringBuilder sb = new StringBuilder("Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append((i + 1)).append(". ").append(tasks.get(i)).append("\n");
        }
        return sb.toString().trim();
    }

    /**
     * Formats a message for marking a task.
     *
     * @param task The task that was marked.
     * @return Formatted message.
     */
    public String formatTaskMarked(Task task) {
        return "Woohoo! I've marked this task as done:\n  " + task;
    }

    /**
     * Formats a message for unmarking a task.
     *
     * @param task The task that was unmarked.
     * @return Formatted message.
     */
    public String formatTaskUnmarked(Task task) {
        return "No worries! I've marked this task as not done yet:\n  " + task;
    }

    /**
     * Formats a message for deleting a task.
     *
     * @param task The task that was deleted.
     * @param taskCount The remaining number of tasks.
     * @return Formatted message.
     */
    public String formatTaskDeleted(Task task, int taskCount) {
        String taskWord = taskCount == 1 ? "task" : "tasks";
        return "Noted. I've removed this task:\n  " + task
                + "\nNow you have " + taskCount + " " + taskWord + " in the list.";
    }

    /**
     * Formats a message for adding a task.
     *
     * @param task The task that was added.
     * @param taskCount The total number of tasks.
     * @return Formatted message.
     */
    public String formatTaskAdded(Task task, int taskCount) {
        String taskWord = taskCount == 1 ? "task" : "tasks";
        return "Awesome! I've added this task:\n  " + task
                + "\nNow you have " + taskCount + " " + taskWord + " in the list.";
    }

    /**
     * Formats the list of matching tasks.
     *
     * @param tasks The list of matching tasks.
     * @return Formatted matching tasks string.
     */
    public String formatMatchingTasks(ArrayList<Task> tasks) {
        if (tasks.isEmpty()) {
            return "No matching tasks found!";
        }
        StringBuilder sb = new StringBuilder("Here are the matching tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append((i + 1)).append(". ").append(tasks.get(i)).append("\n");
        }
        return sb.toString().trim();
    }
}
