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
                    assert markIndex >= 0 && markIndex < tasks.getTaskCount() : "Mark index should be valid";
                    tasks.markTask(markIndex);
                    storage.save(tasks.getAllTasks());
                    ui.showTaskMarked(tasks.getTask(markIndex));
                    break;
                case UNMARK:
                    int unmarkIndex = Parser.getTaskIndex(input, tasks.getTaskCount());
                    assert unmarkIndex >= 0 && unmarkIndex < tasks.getTaskCount() : "Unmark index should be valid";
                    tasks.unmarkTask(unmarkIndex);
                    storage.save(tasks.getAllTasks());
                    ui.showTaskUnmarked(tasks.getTask(unmarkIndex));
                    break;
                case DELETE:
                    int deleteIndex = Parser.getTaskIndex(input, tasks.getTaskCount());
                    assert deleteIndex >= 0 && deleteIndex < tasks.getTaskCount() : "Delete index should be valid";
                    Task deletedTask = tasks.deleteTask(deleteIndex);
                    storage.save(tasks.getAllTasks());
                    ui.showTaskDeleted(deletedTask, tasks.getTaskCount());
                    break;
                case TODO:
                    String todoDesc = Parser.getTodoDescription(input);
                    assert todoDesc != null && !todoDesc.isEmpty() : "Todo description should be valid";
                    Task todoTask = new Todo(todoDesc);
                    tasks.addTask(todoTask);
                    storage.save(tasks.getAllTasks());
                    ui.showTaskAdded(todoTask, tasks.getTaskCount());
                    break;
                case DEADLINE:
                    String deadlineDesc = Parser.getDeadlineDescription(input);
                    String by = Parser.getDeadlineBy(input);
                    assert deadlineDesc != null && !deadlineDesc.isEmpty() : "Deadline description should be valid";
                    assert by != null && !by.isEmpty() : "Deadline by should be valid";
                    Task deadlineTask = new Deadline(deadlineDesc, by);
                    tasks.addTask(deadlineTask);
                    storage.save(tasks.getAllTasks());
                    ui.showTaskAdded(deadlineTask, tasks.getTaskCount());
                    break;
                case EVENT:
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
                    break;
                    case FIND:
                        String keyword = Parser.getFindKeyword(input);
                        assert keyword != null && !keyword.isEmpty() : "Find keyword should be valid";
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
            
            switch (command) {
            case BYE:
                return "Bye. Hope to see you again soon!";
            case LIST:
                ArrayList<Task> allTasks = tasks.getAllTasks();
                if (allTasks.isEmpty()) {
                    return "You have no tasks in your list.";
                }
                AtomicInteger listCounter = new AtomicInteger(1);
                return "Here are the tasks in your list:\n" +
                        allTasks.stream()
                                .map(task -> listCounter.getAndIncrement() + ". " + task)
                                .collect(Collectors.joining("\n"));
            case MARK:
                int markIndex = Parser.getTaskIndex(input, tasks.getTaskCount());
                assert markIndex >= 0 && markIndex < tasks.getTaskCount() : "Mark index should be valid";
                tasks.markTask(markIndex);
                storage.save(tasks.getAllTasks());
                return "Nice! I've marked this task as done:\n  " + tasks.getTask(markIndex);
            case UNMARK:
                int unmarkIndex = Parser.getTaskIndex(input, tasks.getTaskCount());
                assert unmarkIndex >= 0 && unmarkIndex < tasks.getTaskCount() : "Unmark index should be valid";
                tasks.unmarkTask(unmarkIndex);
                storage.save(tasks.getAllTasks());
                return "OK, I've marked this task as not done yet:\n  " + tasks.getTask(unmarkIndex);
            case DELETE:
                int deleteIndex = Parser.getTaskIndex(input, tasks.getTaskCount());
                assert deleteIndex >= 0 && deleteIndex < tasks.getTaskCount() : "Delete index should be valid";
                Task deletedTask = tasks.deleteTask(deleteIndex);
                storage.save(tasks.getAllTasks());
                return "Noted. I've removed this task:\n  " + deletedTask 
                        + "\nNow you have " + tasks.getTaskCount() + " tasks in the list.";
            case TODO:
                String todoDesc = Parser.getTodoDescription(input);
                assert todoDesc != null && !todoDesc.isEmpty() : "Todo description should be valid";
                Task todoTask = new Todo(todoDesc);
                tasks.addTask(todoTask);
                storage.save(tasks.getAllTasks());
                return "Got it. I've added this task:\n  " + todoTask 
                        + "\nNow you have " + tasks.getTaskCount() + " tasks in the list.";
            case DEADLINE:
                String deadlineDesc = Parser.getDeadlineDescription(input);
                String by = Parser.getDeadlineBy(input);
                assert deadlineDesc != null && !deadlineDesc.isEmpty() : "Deadline description should be valid";
                assert by != null && !by.isEmpty() : "Deadline by should be valid";
                Task deadlineTask = new Deadline(deadlineDesc, by);
                tasks.addTask(deadlineTask);
                storage.save(tasks.getAllTasks());
                return "Got it. I've added this task:\n  " + deadlineTask 
                        + "\nNow you have " + tasks.getTaskCount() + " tasks in the list.";
            case EVENT:
                String eventDesc = Parser.getEventDescription(input);
                String from = Parser.getEventFrom(input);
                String to = Parser.getEventTo(input);
                assert eventDesc != null && !eventDesc.isEmpty() : "Event description should be valid";
                assert from != null && !from.isEmpty() : "Event from should be valid";
                assert to != null && !to.isEmpty() : "Event to should be valid";
                Task eventTask = new Event(eventDesc, from, to);
                tasks.addTask(eventTask);
                storage.save(tasks.getAllTasks());
                return "Got it. I've added this task:\n  " + eventTask 
                        + "\nNow you have " + tasks.getTaskCount() + " tasks in the list.";
            case FIND:
                String keyword = Parser.getFindKeyword(input);
                assert keyword != null && !keyword.isEmpty() : "Find keyword should be valid";
                ArrayList<Task> matchingTasks = tasks.findTasks(keyword);
                if (matchingTasks.isEmpty()) {
                    return "No matching tasks found.";
                }
                AtomicInteger findCounter = new AtomicInteger(1);
                return "Here are the matching tasks in your list:\n" +
                        matchingTasks.stream()
                                .map(task -> findCounter.getAndIncrement() + ". " + task)
                                .collect(Collectors.joining("\n"));
            default:
                return "I don't understand that command.";
            }
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
