public class Parser {
    
    public static Command getCommand(String input) throws ShengException {
        if (input.trim().isEmpty()) {
            throw new ShengException("Hey there! I'd love to help, but you didn't tell me what to do :)");
        }
        
        if (input.equals("bye")) {
            return Command.BYE;
        } else if (input.equals("list")) {
            return Command.LIST;
        } else if (input.equals("mark")) {
            throw new ShengException("Which task would you like to mark as done? Try: mark <number>");
        } else if (input.startsWith("mark ")) {
            return Command.MARK;
        } else if (input.equals("unmark")) {
            throw new ShengException("Which task would you like to unmark? Try: unmark <number>");
        } else if (input.startsWith("unmark ")) {
            return Command.UNMARK;
        } else if (input.equals("delete")) {
            throw new ShengException("Which task would you like to delete? Try: delete <number>");
        } else if (input.startsWith("delete ")) {
            return Command.DELETE;
        } else if (input.startsWith("todo")) {
            if (input.trim().equals("todo")) {
                throw new ShengException("Oops! You forgot to tell me what todo you want to add! Try: todo <description>");
            }
            return Command.TODO;
        } else if (input.startsWith("deadline")) {
            if (input.trim().equals("deadline")) {
                throw new ShengException("Almost there! Don't forget to tell me what the deadline is for! Try: deadline <description> /by <time>");
            }
            if (!input.contains("/by")) {
                throw new ShengException("Hmm, when is this deadline? Please use: deadline <task> /by <time>");
            }
            return Command.DEADLINE;
        } else if (input.startsWith("event")) {
            if (input.trim().equals("event")) {
                throw new ShengException("Exciting! What event do you want to add! Try: event <description> /from <start> /to <end>");
            }
            if (!input.contains("/from")) {
                throw new ShengException("When does this event start? Please use: event <task> /from <start> /to <end>");
            }
            if (!input.contains("/to")) {
                throw new ShengException("When does this event end? Please use: event <task> /from <start> /to <end>");
            }
            return Command.EVENT;
        } else {
            throw new ShengException("Hmm, I'm not sure what you mean! Try: todo, deadline, event, list, mark, unmark, or delete :)");
        }
    }

    public static int getTaskIndex(String input, int taskCount) throws ShengException {
        try {
            int index;
            if (input.startsWith("mark ")) {
                String numberStr = input.substring(5).trim();
                if (numberStr.isEmpty()) {
                    throw new ShengException("Which task would you like to mark as done? Try: mark <number>");
                }
                index = Integer.parseInt(numberStr) - 1;
            } else if (input.startsWith("unmark ")) {
                String numberStr = input.substring(7).trim();
                if (numberStr.isEmpty()) {
                    throw new ShengException("Which task would you like to unmark? Try: unmark <number>");
                }
                index = Integer.parseInt(numberStr) - 1;
            } else if (input.startsWith("delete ")) {
                String numberStr = input.substring(7).trim();
                if (numberStr.isEmpty()) {
                    throw new ShengException("Which task would you like to delete? Try: delete <number>");
                }
                index = Integer.parseInt(numberStr) - 1;
            } else {
                return -1;
            }
            
            if (index < 0 || index >= taskCount) {
                String taskWord = taskCount == 1 ? "task" : "tasks";
                throw new ShengException("Oops! You only have " + taskCount + " " + taskWord + " in your list!");
            }
            return index;
        } catch (NumberFormatException e) {
            throw new ShengException("That doesn't look like a number! Please use a number like 1, 2, 3...");
        }
    }

    public static String getTodoDescription(String input) throws ShengException {
        String description = input.substring(5).trim();
        if (description.isEmpty()) {
            throw new ShengException("Oops! You forgot to tell me what the todo is!");
        }
        return description;
    }

    public static String getDeadlineDescription(String input) throws ShengException {
        int byIndex = input.indexOf("/by");
        String description = input.substring(9, byIndex).trim();
        if (description.isEmpty()) {
            throw new ShengException("What's the deadline for? Don't forget to add a description!");
        }
        return description;
    }

    public static String getDeadlineBy(String input) throws ShengException {
        int byIndex = input.indexOf("/by");
        String by = input.substring(byIndex + 3).trim();
        if (by.isEmpty()) {
            throw new ShengException("When is this deadline? Don't forget to add the time after /by!");
        }
        return by;
    }

    public static String getEventDescription(String input) throws ShengException {
        int fromIndex = input.indexOf("/from");
        String description = input.substring(6, fromIndex).trim();
        if (description.isEmpty()) {
            throw new ShengException("What event are you adding? Don't forget the description!");
        }
        return description;
    }

    public static String getEventFrom(String input) throws ShengException {
        int fromIndex = input.indexOf("/from");
        int toIndex = input.indexOf("/to");
        String from = input.substring(fromIndex + 5, toIndex).trim();
        if (from.isEmpty()) {
            throw new ShengException("When does your event start? Add the time after /from!");
        }
        return from;
    }

    public static String getEventTo(String input) throws ShengException {
        int toIndex = input.indexOf("/to");
        String to = input.substring(toIndex + 3).trim();
        if (to.isEmpty()) {
            throw new ShengException("When does your event end? Add the time after /to!");
        }
        return to;
    }
}
