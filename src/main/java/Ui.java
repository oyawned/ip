import java.util.Scanner;
import java.util.ArrayList;

public class Ui {
    private static final String LINE = "____________________________________________________________";
    private Scanner scanner;

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
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i));
        }
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

    public void close() {
        scanner.close();
    }
}
