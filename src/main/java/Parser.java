public class Parser {
    
    public static String getCommand(String input) {
        if (input.equals("bye") || input.equals("list")) {
            return input;
        } else if (input.startsWith("mark ")) {
            return "mark";
        } else if (input.startsWith("unmark ")) {
            return "unmark";
        } else {
            return "add";
        }
    }

    public static int getTaskIndex(String input) {
        if (input.startsWith("mark ")) {
            return Integer.parseInt(input.substring(5)) - 1;
        } else if (input.startsWith("unmark ")) {
            return Integer.parseInt(input.substring(7)) - 1;
        }
        return -1;
    }
}
