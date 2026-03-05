package sheng;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import sheng.exception.ShengException;
import sheng.parser.Command;
import sheng.parser.Parser;
import sheng.storage.Storage;
import sheng.task.Deadline;
import sheng.task.Event;
import sheng.task.Task;
import sheng.task.TaskList;
import sheng.task.Todo;
import sheng.ui.Ui;

/**
 * Main class for the Sheng chatbot application.
 */
public class Sheng {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    /**
     * Constructs a Sheng instance with the specified file path.
     *
     * @param filePath The path to the data file.
     */
    public Sheng(String filePath) {
        assert filePath != null : "File path cannot be null";
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (Exception e) {
            ui.showError("Error loading tasks. Starting with empty task list.");
            tasks = new TaskList();
        }
        assert ui != null : "UI should be initialized";
        assert storage != null : "Storage should be initialized";
        assert tasks != null : "Tasks should be initialized";
    }

    /**
     * Constructs a Sheng instance with default file path.
     */
    public Sheng() {
        this("data/sheng.txt");
    }

    /**
     * Runs the main chatbot loop.
     */
    public void run() {
        ui.showWelcome();
        
        boolean isExit = false;
        while (!isExit) {
            try {
                String input = ui.readCommand();
                Command command = Parser.getCommand(input);
                isExit = processCommand(command, input);
            } catch (ShengException e) {
                ui.showError(e.getMessage());
            }
        }
        ui.close();
    }

    /**
     * Processes a command in CLI mode and returns whether to exit.
     *
     * @param command The command to process.
     * @param input The full user input string.
     * @return True if the application should exit, false otherwise.
     * @throws ShengException If command execution fails.
     */
    private boolean processCommand(Command command, String input) throws ShengException {
        switch (command) {
        case BYE:
            ui.showGoodbye();
            return true;
        case LIST:
            ui.showTaskList(tasks.getAllTasks());
            break;
        case MARK:
            handleMarkCommandCli(input);
            break;
        case UNMARK:
            handleUnmarkCommandCli(input);
            break;
        case DELETE:
            handleDeleteCommandCli(input);
            break;
        case TODO:
            handleTodoCommandCli(input);
            break;
        case DEADLINE:
            handleDeadlineCommandCli(input);
            break;
        case EVENT:
            handleEventCommandCli(input);
            break;
        case FIND:
            handleFindCommandCli(input);
            break;
        case ARCHIVE:
            handleArchiveCommandCli();
            break;
        }
        return false;
    }

    /**
     * Handles the mark command in CLI mode.
     *
     * @param input The user input string.
     * @throws ShengException If the task index is invalid.
     */
    private void handleMarkCommandCli(String input) throws ShengException {
        int markIndex = Parser.getTaskIndex(input, tasks.getTaskCount());
        assert markIndex >= 0 && markIndex < tasks.getTaskCount() : "Mark index should be valid";
        tasks.markTask(markIndex);
        storage.save(tasks.getAllTasks());
        ui.showTaskMarked(tasks.getTask(markIndex));
    }

    /**
     * Handles the unmark command in CLI mode.
     *
     * @param input The user input string.
     * @throws ShengException If the task index is invalid.
     */
    private void handleUnmarkCommandCli(String input) throws ShengException {
        int unmarkIndex = Parser.getTaskIndex(input, tasks.getTaskCount());
        assert unmarkIndex >= 0 && unmarkIndex < tasks.getTaskCount() : "Unmark index should be valid";
        tasks.unmarkTask(unmarkIndex);
        storage.save(tasks.getAllTasks());
        ui.showTaskUnmarked(tasks.getTask(unmarkIndex));
    }

    /**
     * Handles the delete command in CLI mode.
     *
     * @param input The user input string.
     * @throws ShengException If the task index is invalid.
     */
    private void handleDeleteCommandCli(String input) throws ShengException {
        int deleteIndex = Parser.getTaskIndex(input, tasks.getTaskCount());
        assert deleteIndex >= 0 && deleteIndex < tasks.getTaskCount() : "Delete index should be valid";
        Task deletedTask = tasks.deleteTask(deleteIndex);
        storage.save(tasks.getAllTasks());
        ui.showTaskDeleted(deletedTask, tasks.getTaskCount());
    }

    /**
     * Handles the todo command in CLI mode.
     *
     * @param input The user input string.
     * @throws ShengException If the todo description is invalid.
     */
    private void handleTodoCommandCli(String input) throws ShengException {
        String todoDesc = Parser.getTodoDescription(input);
        assert todoDesc != null && !todoDesc.isEmpty() : "Todo description should be valid";
        Task todoTask = new Todo(todoDesc);
        tasks.addTask(todoTask);
        storage.save(tasks.getAllTasks());
        ui.showTaskAdded(todoTask, tasks.getTaskCount());
    }

    /**
     * Handles the deadline command in CLI mode.
     *
     * @param input The user input string.
     * @throws ShengException If the deadline description or time is invalid.
     */
    private void handleDeadlineCommandCli(String input) throws ShengException {
        String deadlineDesc = Parser.getDeadlineDescription(input);
        String by = Parser.getDeadlineBy(input);
        assert deadlineDesc != null && !deadlineDesc.isEmpty() : "Deadline description should be valid";
        assert by != null && !by.isEmpty() : "Deadline by should be valid";
        Task deadlineTask = new Deadline(deadlineDesc, by);
        tasks.addTask(deadlineTask);
        storage.save(tasks.getAllTasks());
        ui.showTaskAdded(deadlineTask, tasks.getTaskCount());
    }

    /**
     * Handles the event command in CLI mode.
     *
     * @param input The user input string.
     * @throws ShengException If the event description, start time, or end time is invalid.
     */
    private void handleEventCommandCli(String input) throws ShengException {
        String eventDesc = Parser.getEventDescription(input);
        String from = Parser.getEventFrom(input);
        String to = Parser.getEventTo(input);
        assert eventDesc != null && !eventDesc.isEmpty() : "Event description should be valid";
        assert from != null && !from.isEmpty() : "Event from should be valid";
        assert to != null && !to.isEmpty() : "Event to should be valid";
        Task eventTask = new Event(eventDesc, from, to);
        tasks.addTask(eventTask);
        storage.save(tasks.getAllTasks());
        ui.showTaskAdded(eventTask, tasks.getTaskCount());
    }

    /**
     * Handles the find command in CLI mode.
     *
     * @param input The user input string.
     * @throws ShengException If the find keyword is invalid.
     */
    private void handleFindCommandCli(String input) throws ShengException {
        String keyword = Parser.getFindKeyword(input);
        assert keyword != null && !keyword.isEmpty() : "Find keyword should be valid";
        ArrayList<Task> matchingTasks = tasks.findTasks(keyword);
        ui.showMatchingTasks(matchingTasks);
    }

    /**
     * Handles the archive command in CLI mode.
     */
    private void handleArchiveCommandCli() {
        int archivedCount = tasks.getTaskCount();
        if (archivedCount == 0) {
            ui.showError("Your task list is already empty! Nothing to archive.");
        } else {
            try {
                String archiveFileName = storage.archiveAll(tasks.getAllTasks());
                tasks.clearAllTasks();
                storage.save(tasks.getAllTasks());
                ui.showArchiveComplete(archiveFileName, archivedCount);
            } catch (Exception e) {
                ui.showError("Oops! Failed to archive tasks: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new Sheng().run();
    }

    /**
     * Generates a response for the user's chat message.
     *
     * @param input The user's input.
     * @return The response from Sheng.
     */
    public String getResponse(String input) {
        try {
            Command command = Parser.getCommand(input);
            return executeCommand(command, input);
        } catch (ShengException e) {
            return e.getMessage();
        }
    }

    /**
     * Executes the given command and returns the appropriate response.
     *
     * @param command The command to execute.
     * @param input The full user input string.
     * @return The response message.
     * @throws ShengException If command execution fails.
     */
    private String executeCommand(Command command, String input) throws ShengException {
        switch (command) {
        case BYE:
            return ui.formatGoodbyeMessage();
        case LIST:
            return ui.formatTaskList(tasks.getAllTasks());
        case MARK:
            return handleMarkCommand(input);
        case UNMARK:
            return handleUnmarkCommand(input);
        case DELETE:
            return handleDeleteCommand(input);
        case TODO:
            return handleTodoCommand(input);
        case DEADLINE:
            return handleDeadlineCommand(input);
        case EVENT:
            return handleEventCommand(input);
        case FIND:
            return handleFindCommand(input);
        case ARCHIVE:
            return handleArchiveCommand();
        default:
            throw new ShengException("I don't understand that command.");
        }
    }

    /**
     * Handles the mark command by marking a task as done.
     *
     * @param input The user input string.
     * @return Formatted response message.
     * @throws ShengException If the task index is invalid.
     */
    private String handleMarkCommand(String input) throws ShengException {
        int index = Parser.getTaskIndex(input, tasks.getTaskCount());
        tasks.markTask(index);
        storage.save(tasks.getAllTasks());
        return ui.formatTaskMarked(tasks.getTask(index));
    }

    /**
     * Handles the unmark command by marking a task as not done.
     *
     * @param input The user input string.
     * @return Formatted response message.
     * @throws ShengException If the task index is invalid.
     */
    private String handleUnmarkCommand(String input) throws ShengException {
        int index = Parser.getTaskIndex(input, tasks.getTaskCount());
        tasks.unmarkTask(index);
        storage.save(tasks.getAllTasks());
        return ui.formatTaskUnmarked(tasks.getTask(index));
    }

    /**
     * Handles the delete command by removing a task from the list.
     *
     * @param input The user input string.
     * @return Formatted response message.
     * @throws ShengException If the task index is invalid.
     */
    private String handleDeleteCommand(String input) throws ShengException {
        int index = Parser.getTaskIndex(input, tasks.getTaskCount());
        Task deletedTask = tasks.deleteTask(index);
        storage.save(tasks.getAllTasks());
        return ui.formatTaskDeleted(deletedTask, tasks.getTaskCount());
    }

    /**
     * Handles the todo command by creating and adding a new todo task.
     *
     * @param input The user input string.
     * @return Formatted response message.
     * @throws ShengException If the todo description is invalid.
     */
    private String handleTodoCommand(String input) throws ShengException {
        String description = Parser.getTodoDescription(input);
        Task task = new Todo(description);
        return addTaskAndGetResponse(task);
    }

    /**
     * Handles the deadline command by creating and adding a new deadline task.
     *
     * @param input The user input string.
     * @return Formatted response message.
     * @throws ShengException If the deadline description or time is invalid.
     */
    private String handleDeadlineCommand(String input) throws ShengException {
        String description = Parser.getDeadlineDescription(input);
        String by = Parser.getDeadlineBy(input);
        Task task = new Deadline(description, by);
        return addTaskAndGetResponse(task);
    }

    /**
     * Handles the event command by creating and adding a new event task.
     *
     * @param input The user input string.
     * @return Formatted response message.
     * @throws ShengException If the event description, start time, or end time is invalid.
     */
    private String handleEventCommand(String input) throws ShengException {
        String description = Parser.getEventDescription(input);
        String from = Parser.getEventFrom(input);
        String to = Parser.getEventTo(input);
        Task task = new Event(description, from, to);
        return addTaskAndGetResponse(task);
    }

    /**
     * Handles the find command by searching for tasks containing the keyword.
     *
     * @param input The user input string.
     * @return Formatted list of matching tasks.
     * @throws ShengException If the find keyword is invalid.
     */
    private String handleFindCommand(String input) throws ShengException {
        String keyword = Parser.getFindKeyword(input);
        ArrayList<Task> matchingTasks = tasks.findTasks(keyword);
        return ui.formatMatchingTasks(matchingTasks);
    }

    /**
     * Adds a task to the task list, saves to storage, and returns a formatted response.
     *
     * @param task The task to add.
     * @return Formatted response message.
     */
    private String addTaskAndGetResponse(Task task) {
        tasks.addTask(task);
        storage.save(tasks.getAllTasks());
        return ui.formatTaskAdded(task, tasks.getTaskCount());
    }

    /**
     * Handles the archive command by saving all tasks to a timestamped file
     * and clearing the current task list.
     * AI-assisted: GitHub Copilot helped with:
     * 1. Error handling strategy using try-catch with ShengException
     * 2. Suggesting to check for empty list before archiving
     * 3. Recommending the proper order: archive → clear → save
     */
    private String handleArchiveCommand() throws ShengException {
        int archivedCount = tasks.getTaskCount();
        if (archivedCount == 0) {
            return "Your task list is already empty! Nothing to archive.";
        }
        try {
            String archiveFileName = storage.archiveAll(tasks.getAllTasks());
            tasks.clearAllTasks();
            storage.save(tasks.getAllTasks());
            return ui.formatArchiveComplete(archiveFileName, archivedCount);
        } catch (Exception e) {
            throw new ShengException("Oops! Failed to archive tasks: " + e.getMessage());
        }
    }
}
