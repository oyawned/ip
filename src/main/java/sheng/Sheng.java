package sheng;

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

public class Sheng {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

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

    public Sheng() {
        this("data/sheng.txt");
    }

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
}
