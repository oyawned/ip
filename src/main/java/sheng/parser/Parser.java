package sheng.parser;

import sheng.exception.ShengException;

/**
 * Parses user input and extracts commands and parameters.
 */
public class Parser {
    private static final int MARK_PREFIX_LENGTH = 5;
    private static final int UNMARK_PREFIX_LENGTH = 7;
    private static final int DELETE_PREFIX_LENGTH = 7;
    private static final int TODO_PREFIX_LENGTH = 5;
    private static final int DEADLINE_PREFIX_LENGTH = 9;
    private static final int EVENT_PREFIX_LENGTH = 6;
    private static final int BY_OFFSET = 3;
    private static final int FROM_OFFSET = 5;
    private static final int TO_OFFSET = 3;
    
    /**
     * Parses the user input and returns the corresponding command.
     *
     * @param input The user input string.
     * @return The parsed Command.
     * @throws ShengException If the input is invalid.
     */
    public static Command getCommand(String input) throws ShengException {
        assert input != null : "Input cannot be null";
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
        } else if (input.startsWith("find")) {
            if (input.trim().equals("find")) {
                throw new ShengException("What would you like to find? Try: find <keyword>");
            }
            return Command.FIND;
        } else {
            throw new ShengException("Hmm, I'm not sure what you mean! Try: todo, deadline, event, list, mark, unmark, or delete :)");
        }
    }

    public static int getTaskIndex(String input, int taskCount) throws ShengException {
        try {
            int index;
            if (input.startsWith("mark ")) {
                String numberStr = input.substring(MARK_PREFIX_LENGTH).trim();
                if (numberStr.isEmpty()) {
                    throw new ShengException("Which task would you like to mark as done? Try: mark <number>");
                }
                index = Integer.parseInt(numberStr) - 1;
            } else if (input.startsWith("unmark ")) {
                String numberStr = input.substring(UNMARK_PREFIX_LENGTH).trim();
                if (numberStr.isEmpty()) {
                    throw new ShengException("Which task would you like to unmark? Try: unmark <number>");
                }
                index = Integer.parseInt(numberStr) - 1;
            } else if (input.startsWith("delete ")) {
                String numberStr = input.substring(DELETE_PREFIX_LENGTH).trim();
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
            assert index >= 0 && index < taskCount : "Index should be valid after range check";
            return index;
        } catch (NumberFormatException e) {
            throw new ShengException("That doesn't look like a number! Please use a number like 1, 2, 3...");
        }
    }

    public static String getTodoDescription(String input) throws ShengException {
        assert input != null : "Input cannot be null";
        String description = input.substring(TODO_PREFIX_LENGTH).trim();
        if (description.isEmpty()) {
            throw new ShengException("Oops! You forgot to tell me what the todo is!");
        }
        assert !description.isEmpty() : "Description should not be empty after validation";
        return description;
    }

    public static String getDeadlineDescription(String input) throws ShengException {
        assert input != null : "Input cannot be null";
        int byIndex = input.indexOf("/by");
        assert byIndex != -1 : "Input should contain /by marker";
        String description = input.substring(DEADLINE_PREFIX_LENGTH, byIndex).trim();
        if (description.isEmpty()) {
            throw new ShengException("What's the deadline for? Don't forget to add a description!");
        }
        assert !description.isEmpty() : "Description should not be empty after validation";
        return description;
    }

    public static String getDeadlineBy(String input) throws ShengException {
        assert input != null : "Input cannot be null";
        int byIndex = input.indexOf("/by");
        assert byIndex != -1 : "Input should contain /by marker";
        String by = input.substring(byIndex + BY_OFFSET).trim();
        if (by.isEmpty()) {
            throw new ShengException("When is this deadline? Don't forget to add the time after /by!");
        }
        assert !by.isEmpty() : "By string should not be empty after validation";
        return by;
    }

    public static String getEventDescription(String input) throws ShengException {
        assert input != null : "Input cannot be null";
        int fromIndex = input.indexOf("/from");
        assert fromIndex != -1 : "Input should contain /from marker";
        String description = input.substring(EVENT_PREFIX_LENGTH, fromIndex).trim();
        if (description.isEmpty()) {
            throw new ShengException("What event are you adding? Don't forget the description!");
        }
        assert !description.isEmpty() : "Description should not be empty after validation";
        return description;
    }

    public static String getEventFrom(String input) throws ShengException {
        assert input != null : "Input cannot be null";
        int fromIndex = input.indexOf("/from");
        int toIndex = input.indexOf("/to");
        assert fromIndex != -1 : "Input should contain /from marker";
        assert toIndex != -1 : "Input should contain /to marker";
        String from = input.substring(fromIndex + FROM_OFFSET, toIndex).trim();
        if (from.isEmpty()) {
            throw new ShengException("When does your event start? Add the time after /from!");
        }
        assert !from.isEmpty() : "From string should not be empty after validation";
        return from;
    }

    public static String getEventTo(String input) throws ShengException {
        assert input != null : "Input cannot be null";
        int toIndex = input.indexOf("/to");
        assert toIndex != -1 : "Input should contain /to marker";
        String to = input.substring(toIndex + TO_OFFSET).trim();
        if (to.isEmpty()) {
            throw new ShengException("When does your event end? Add the time after /to!");
        }
        assert !to.isEmpty() : "To string should not be empty after validation";
        return to;
    }

    public static String getFindKeyword(String input) throws ShengException {
        assert input != null : "Input cannot be null";
        String keyword = input.substring(5).trim();
        if (keyword.isEmpty()) {
            throw new ShengException("What would you like to find? Please provide a keyword!");
        }
        assert !keyword.isEmpty() : "Keyword should not be empty after validation";
        return keyword;
    }
}
