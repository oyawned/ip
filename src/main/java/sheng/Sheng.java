package sheng;

import java.util.ArrayList;

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
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (Exception e) {
            ui.showError("Error loading tasks. Starting with empty task list.");
            tasks = new TaskList();
        }
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
                
                switch (command) {
                case BYE:
                    ui.showGoodbye();
                    isExit = true;
                    break;
                case LIST:
                    ui.showTaskList(tasks.getAllTasks());
                    break;
                case MARK:
                    int markIndex = Parser.getTaskIndex(input, tasks.getTaskCount());
                    tasks.markTask(markIndex);
                    storage.save(tasks.getAllTasks());
                    ui.showTaskMarked(tasks.getTask(markIndex));
                    break;
                case UNMARK:
                    int unmarkIndex = Parser.getTaskIndex(input, tasks.getTaskCount());
                    tasks.unmarkTask(unmarkIndex);
                    storage.save(tasks.getAllTasks());
                    ui.showTaskUnmarked(tasks.getTask(unmarkIndex));
                    break;
                case DELETE:
                    int deleteIndex = Parser.getTaskIndex(input, tasks.getTaskCount());
                    Task deletedTask = tasks.deleteTask(deleteIndex);
                    storage.save(tasks.getAllTasks());
                    ui.showTaskDeleted(deletedTask, tasks.getTaskCount());
                    break;
                case TODO:
                    String todoDesc = Parser.getTodoDescription(input);
                    Task todoTask = new Todo(todoDesc);
                    tasks.addTask(todoTask);
                    storage.save(tasks.getAllTasks());
                    ui.showTaskAdded(todoTask, tasks.getTaskCount());
                    break;
                case DEADLINE:
                    String deadlineDesc = Parser.getDeadlineDescription(input);
                    String by = Parser.getDeadlineBy(input);
                    Task deadlineTask = new Deadline(deadlineDesc, by);
                    tasks.addTask(deadlineTask);
                    storage.save(tasks.getAllTasks());
                    ui.showTaskAdded(deadlineTask, tasks.getTaskCount());
                    break;
                case EVENT:
                    String eventDesc = Parser.getEventDescription(input);
                    String from = Parser.getEventFrom(input);
                    String to = Parser.getEventTo(input);
                    Task eventTask = new Event(eventDesc, from, to);
                    tasks.addTask(eventTask);
                    storage.save(tasks.getAllTasks());
                    ui.showTaskAdded(eventTask, tasks.getTaskCount());
                    break;
                    case FIND:
                        String keyword = Parser.getFindKeyword(input);
                        ArrayList<Task> matchingTasks = tasks.findTasks(keyword);
                        ui.showMatchingTasks(matchingTasks);
                        break;
                }
            } catch (ShengException e) {
                ui.showError(e.getMessage());
            }
        }
        ui.close();
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
        default:
            throw new ShengException("I don't understand that command.");
        }
    }

    private String handleMarkCommand(String input) throws ShengException {
        int index = Parser.getTaskIndex(input, tasks.getTaskCount());
        tasks.markTask(index);
        storage.save(tasks.getAllTasks());
        return ui.formatTaskMarked(tasks.getTask(index));
    }

    private String handleUnmarkCommand(String input) throws ShengException {
        int index = Parser.getTaskIndex(input, tasks.getTaskCount());
        tasks.unmarkTask(index);
        storage.save(tasks.getAllTasks());
        return ui.formatTaskUnmarked(tasks.getTask(index));
    }

    private String handleDeleteCommand(String input) throws ShengException {
        int index = Parser.getTaskIndex(input, tasks.getTaskCount());
        Task deletedTask = tasks.deleteTask(index);
        storage.save(tasks.getAllTasks());
        return ui.formatTaskDeleted(deletedTask, tasks.getTaskCount());
    }

    private String handleTodoCommand(String input) throws ShengException {
        String description = Parser.getTodoDescription(input);
        Task task = new Todo(description);
        return addTaskAndGetResponse(task);
    }

    private String handleDeadlineCommand(String input) throws ShengException {
        String description = Parser.getDeadlineDescription(input);
        String by = Parser.getDeadlineBy(input);
        Task task = new Deadline(description, by);
        return addTaskAndGetResponse(task);
    }

    private String handleEventCommand(String input) throws ShengException {
        String description = Parser.getEventDescription(input);
        String from = Parser.getEventFrom(input);
        String to = Parser.getEventTo(input);
        Task task = new Event(description, from, to);
        return addTaskAndGetResponse(task);
    }

    private String handleFindCommand(String input) throws ShengException {
        String keyword = Parser.getFindKeyword(input);
        ArrayList<Task> matchingTasks = tasks.findTasks(keyword);
        return ui.formatMatchingTasks(matchingTasks);
    }

    private String addTaskAndGetResponse(Task task) {
        tasks.addTask(task);
        storage.save(tasks.getAllTasks());
        return ui.formatTaskAdded(task, tasks.getTaskCount());
    }
}
