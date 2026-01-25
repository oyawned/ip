import java.util.Scanner;

public class Sheng {
    public static void main(String[] args) {
        String line = "____________________________________________________________";
        Scanner scanner = new Scanner(System.in);
        String[] tasks = new String[100];
        int taskCount = 0;
        
        System.out.println(line);
        System.out.println(" Hello! I'm Sheng!");
        System.out.println(" What can I do for you?");
        System.out.println(line);
        
        String input = "";
        while (!input.equals("bye")) {
            input = scanner.nextLine();
            System.out.println(line);
            if (input.equals("bye")) {
                System.out.println(" Bye. Hope to see you again soon!");
            } else if (input.equals("list")) {
                for (int i = 0; i < taskCount; i++) {
                    System.out.println(" " + (i + 1) + ". " + tasks[i]);
                }
            } else {
                tasks[taskCount] = input;
                taskCount++;
                System.out.println(" added: " + input);
            }
            System.out.println(line);
        } 
        scanner.close();
    }
}
