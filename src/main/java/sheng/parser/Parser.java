package sheng.parser;

import sheng.exception.ShengException;

/**
 * Parses user input and extracts commands and parameters.
 */
public class Parser {
    // Command prefixes
    private static final String COMMAND_BYE = "bye";
    private static final String COMMAND_LIST = "list";
    private static final String COMMAND_MARK = "mark";
    private static final String COMMAND_UNMARK = "unmark";
    private static final String COMMAND_DELETE = "delete";
    private static final String COMMAND_TODO = "todo";
    private static final String COMMAND_DEADLINE = "deadline";
    private static final String COMMAND_EVENT = "event";
    private static final String COMMAND_FIND = "find";
    
    // Delimiters
    private static final String DELIMITER_BY = "/by";
    private static final String DELIMITER_FROM = "/from";
    private static final String DELIMITER_TO = "/to";
    
    // Prefix lengths for parsing
    private static final int MARK_PREFIX_LENGTH = 5;
    private static final int UNMARK_PREFIX_LENGTH = 7;
    private static final int DELETE_PREFIX_LENGTH = 7;
    private static final int TODO_PREFIX_LENGTH = 5;
    private static final int DEADLINE_PREFIX_LENGTH = 9;
    private static final int EVENT_PREFIX_LENGTH = 6;
    private static final int FIND_PREFIX_LENGTH = 5;
    private static final int BY_OFFSET = 3;
    private static final int FROM_OFFSET = 5;
    private static final int TO_OFFSET = 3;
    
    // Error messages
    private static final String ERROR_EMPTY_INPUT = "Hey there! I'd love to help, but you didn't tell me what to do :)";
    private static final String ERROR_UNKNOWN_COMMAND = "Hmm, I'm not sure what you mean! Try: todo, deadline, event, list, mark, unmark, delete, or find :)";
    private static final String ERROR_MARK_NO_NUMBER = "Which task would you like to mark as done? Try: mark <number>";
    private static final String ERROR_UNMARK_NO_NUMBER = "Which task would you like to unmark? Try: unmark <number>";
    private static final String ERROR_DELETE_NO_NUMBER = "Which task would you like to delete? Try: delete <number>";
    private static final String ERROR_TODO_EMPTY = "Oops! You forgot to tell me what todo you want to add! Try: todo <description>";
    private static final String ERROR_DEADLINE_EMPTY = "Almost there! Don't forget to tell me what the deadline is for! Try: deadline <description> /by <time>";
    private static final String ERROR_DEADLINE_NO_BY = "Hmm, when is this deadline? Please use: deadline <task> /by <time>";
    private static final String ERROR_EVENT_EMPTY = "Exciting! What event do you want to add! Try: event <description> /from <start> /to <end>";
    private static final String ERROR_EVENT_NO_FROM = "When does this event start? Please use: event <task> /from <start> /to <end>";
    private static final String ERROR_EVENT_NO_TO = "When does this event end? Please use: event <task> /from <start> /to <end>";
    private static final String ERROR_FIND_EMPTY = "What would you like to find? Try: find <keyword>";
    private static final String ERROR_INVALID_NUMBER = "That doesn't look like a number! Please use a number like 1, 2, 3...";
    private static final String ERROR_OUT_OF_RANGE = "Oops! You only have %d %s in your list!";
    private static final String ERROR_TODO_NO_DESCRIPTION = "Oops! You forgot to tell me what the todo is!";
    private static final String ERROR_DEADLINE_NO_DESCRIPTION = "What's the deadline for? Don't forget to add a description!";
    private static final String ERROR_DEADLINE_NO_TIME = "When is this deadline? Don't forget to add the time after /by!";
    private static final String ERROR_EVENT_NO_DESCRIPTION = "What event are you adding? Don't forget the description!";
    private static final String ERROR_EVENT_NO_START = "When does your event start? Add the time after /from!";
    private static final String ERROR_EVENT_NO_END = "When does your event end? Add the time after /to!";
    private static final String ERROR_FIND_NO_KEYWORD = "What would you like to find? Please provide a keyword!";
    
    /**
     * Parses the user input and returns the corresponding command.
     *
     * @param input The user input string.
     * @return The parsed Command.
     * @throws ShengException If the input is invalid.
     */
    public static Command getCommand(String input) throws ShengException {
        String trimmedInput = input.trim();
        
        if (trimmedInput.isEmpty()) {
            throw new ShengException(ERROR_EMPTY_INPUT);
        assert input != null : "Input cannot be null";
        if (input.trim().isEmpty()) {
            throw new ShengException("Hey there! I'd love to help, but you didn't tell me what to do :)");
        }
        
        if (trimmedInput.equals(COMMAND_BYE)) {
            return Command.BYE;
        }
        
        if (trimmedInput.equals(COMMAND_LIST)) {
            return Command.LIST;
        }
        
        if (trimmedInput.startsWith(COMMAND_MARK)) {
            return parseMarkCommand(trimmedInput);
        }
        
        if (trimmedInput.startsWith(COMMAND_UNMARK)) {
            return parseUnmarkCommand(trimmedInput);
        }
        
        if (trimmedInput.startsWith(COMMAND_DELETE)) {
            return parseDeleteCommand(trimmedInput);
        }
        
        if (trimmedInput.startsWith(COMMAND_TODO)) {
            return parseTodoCommand(trimmedInput);
        }
        
        if (trimmedInput.startsWith(COMMAND_DEADLINE)) {
            return parseDeadlineCommand(trimmedInput);
        }
        
        if (trimmedInput.startsWith(COMMAND_EVENT)) {
            return parseEventCommand(trimmedInput);
        }
        
        if (trimmedInput.startsWith(COMMAND_FIND)) {
            return parseFindCommand(trimmedInput);
        }
        
        throw new ShengException(ERROR_UNKNOWN_COMMAND);
    }

    private static Command parseMarkCommand(String input) throws ShengException {
        if (input.equals(COMMAND_MARK)) {
            throw new ShengException(ERROR_MARK_NO_NUMBER);
        }
        return Command.MARK;
    }

    private static Command parseUnmarkCommand(String input) throws ShengException {
        if (input.equals(COMMAND_UNMARK)) {
            throw new ShengException(ERROR_UNMARK_NO_NUMBER);
        }
        return Command.UNMARK;
    }

    private static Command parseDeleteCommand(String input) throws ShengException {
        if (input.equals(COMMAND_DELETE)) {
            throw new ShengException(ERROR_DELETE_NO_NUMBER);
        }
        return Command.DELETE;
    }

    private static Command parseTodoCommand(String input) throws ShengException {
        if (input.trim().equals(COMMAND_TODO)) {
            throw new ShengException(ERROR_TODO_EMPTY);
        }
        return Command.TODO;
    }

    private static Command parseDeadlineCommand(String input) throws ShengException {
        if (input.trim().equals(COMMAND_DEADLINE)) {
            throw new ShengException(ERROR_DEADLINE_EMPTY);
        }
        if (!input.contains(DELIMITER_BY)) {
            throw new ShengException(ERROR_DEADLINE_NO_BY);
        }
        return Command.DEADLINE;
    }

    private static Command parseEventCommand(String input) throws ShengException {
        if (input.trim().equals(COMMAND_EVENT)) {
            throw new ShengException(ERROR_EVENT_EMPTY);
        }
        if (!input.contains(DELIMITER_FROM)) {
            throw new ShengException(ERROR_EVENT_NO_FROM);
        }
        if (!input.contains(DELIMITER_TO)) {
            throw new ShengException(ERROR_EVENT_NO_TO);
        }
        return Command.EVENT;
    }

    private static Command parseFindCommand(String input) throws ShengException {
        if (input.trim().equals(COMMAND_FIND)) {
            throw new ShengException(ERROR_FIND_EMPTY);
        }
        return Command.FIND;
    }

    public static int getTaskIndex(String input, int taskCount) throws ShengException {
        try {
            String numberStr = extractTaskNumber(input);
            int index = Integer.parseInt(numberStr) - 1;
            validateTaskIndex(index, taskCount);
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
            throw new ShengException(ERROR_INVALID_NUMBER);
        }
    }

    private static String extractTaskNumber(String input) throws ShengException {
        if (input.startsWith(COMMAND_MARK + " ")) {
            String numberStr = input.substring(MARK_PREFIX_LENGTH).trim();
            if (numberStr.isEmpty()) {
                throw new ShengException(ERROR_MARK_NO_NUMBER);
            }
            return numberStr;
        } else if (input.startsWith(COMMAND_UNMARK + " ")) {
            String numberStr = input.substring(UNMARK_PREFIX_LENGTH).trim();
            if (numberStr.isEmpty()) {
                throw new ShengException(ERROR_UNMARK_NO_NUMBER);
            }
            return numberStr;
        } else if (input.startsWith(COMMAND_DELETE + " ")) {
            String numberStr = input.substring(DELETE_PREFIX_LENGTH).trim();
            if (numberStr.isEmpty()) {
                throw new ShengException(ERROR_DELETE_NO_NUMBER);
            }
            return numberStr;
        }
        throw new ShengException(ERROR_INVALID_NUMBER);
    }

    private static void validateTaskIndex(int index, int taskCount) throws ShengException {
        if (index < 0 || index >= taskCount) {
            String taskWord = taskCount == 1 ? "task" : "tasks";
            throw new ShengException(String.format(ERROR_OUT_OF_RANGE, taskCount, taskWord));
        }
    }

    public static String getTodoDescription(String input) throws ShengException {
        assert input != null : "Input cannot be null";
        String description = input.substring(TODO_PREFIX_LENGTH).trim();
        if (description.isEmpty()) {
            throw new ShengException(ERROR_TODO_NO_DESCRIPTION);
        }
        assert !description.isEmpty() : "Description should not be empty after validation";
        return description;
    }

    public static String getDeadlineDescription(String input) throws ShengException {
        int byIndex = input.indexOf(DELIMITER_BY);
        assert input != null : "Input cannot be null";
        int byIndex = input.indexOf("/by");
        assert byIndex != -1 : "Input should contain /by marker";
        String description = input.substring(DEADLINE_PREFIX_LENGTH, byIndex).trim();
        if (description.isEmpty()) {
            throw new ShengException(ERROR_DEADLINE_NO_DESCRIPTION);
        }
        assert !description.isEmpty() : "Description should not be empty after validation";
        return description;
    }

    public static String getDeadlineBy(String input) throws ShengException {
        int byIndex = input.indexOf(DELIMITER_BY);
        assert input != null : "Input cannot be null";
        int byIndex = input.indexOf("/by");
        assert byIndex != -1 : "Input should contain /by marker";
        String by = input.substring(byIndex + BY_OFFSET).trim();
        if (by.isEmpty()) {
            throw new ShengException(ERROR_DEADLINE_NO_TIME);
        }
        assert !by.isEmpty() : "By string should not be empty after validation";
        return by;
    }

    public static String getEventDescription(String input) throws ShengException {
        int fromIndex = input.indexOf(DELIMITER_FROM);
        assert input != null : "Input cannot be null";
        int fromIndex = input.indexOf("/from");
        assert fromIndex != -1 : "Input should contain /from marker";
        String description = input.substring(EVENT_PREFIX_LENGTH, fromIndex).trim();
        if (description.isEmpty()) {
            throw new ShengException(ERROR_EVENT_NO_DESCRIPTION);
        }
        assert !description.isEmpty() : "Description should not be empty after validation";
        return description;
    }

    public static String getEventFrom(String input) throws ShengException {
        int fromIndex = input.indexOf(DELIMITER_FROM);
        int toIndex = input.indexOf(DELIMITER_TO);
        assert input != null : "Input cannot be null";
        int fromIndex = input.indexOf("/from");
        int toIndex = input.indexOf("/to");
        assert fromIndex != -1 : "Input should contain /from marker";
        assert toIndex != -1 : "Input should contain /to marker";
        String from = input.substring(fromIndex + FROM_OFFSET, toIndex).trim();
        if (from.isEmpty()) {
            throw new ShengException(ERROR_EVENT_NO_START);
        }
        assert !from.isEmpty() : "From string should not be empty after validation";
        return from;
    }

    public static String getEventTo(String input) throws ShengException {
        int toIndex = input.indexOf(DELIMITER_TO);
        assert input != null : "Input cannot be null";
        int toIndex = input.indexOf("/to");
        assert toIndex != -1 : "Input should contain /to marker";
        String to = input.substring(toIndex + TO_OFFSET).trim();
        if (to.isEmpty()) {
            throw new ShengException(ERROR_EVENT_NO_END);
        }
        assert !to.isEmpty() : "To string should not be empty after validation";
        return to;
    }

    public static String getFindKeyword(String input) throws ShengException {
        String keyword = input.substring(FIND_PREFIX_LENGTH).trim();
        assert input != null : "Input cannot be null";
        String keyword = input.substring(5).trim();
        if (keyword.isEmpty()) {
            throw new ShengException(ERROR_FIND_NO_KEYWORD);
        }
        assert !keyword.isEmpty() : "Keyword should not be empty after validation";
        return keyword;
    }
}
