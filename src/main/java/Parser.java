public class Parser {
    
    public static String getCommand(String input) {
        if (input.equals("bye") || input.equals("list")) {
            return input;
        } else if (input.startsWith("mark ")) {
            return "mark";
        } else if (input.startsWith("unmark ")) {
            return "unmark";
        } else if (input.startsWith("todo ")) {
            return "todo";
        } else if (input.startsWith("deadline ")) {
            return "deadline";
        } else if (input.startsWith("event ")) {
            return "event";
        } else {
            return "unknown";
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

    public static String getTodoDescription(String input) {
        return input.substring(5).trim();
    }

    public static String getDeadlineDescription(String input) {
        int byIndex = input.indexOf("/by");
        return input.substring(9, byIndex).trim();
    }

    public static String getDeadlineBy(String input) {
        int byIndex = input.indexOf("/by");
        return input.substring(byIndex + 3).trim();
    }

    public static String getEventDescription(String input) {
        int fromIndex = input.indexOf("/from");
        return input.substring(6, fromIndex).trim();
    }

    public static String getEventFrom(String input) {
        int fromIndex = input.indexOf("/from");
        int toIndex = input.indexOf("/to");
        return input.substring(fromIndex + 5, toIndex).trim();
    }

    public static String getEventTo(String input) {
        int toIndex = input.indexOf("/to");
        return input.substring(toIndex + 3).trim();
    }
}
