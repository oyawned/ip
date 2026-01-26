public class Sheng {
    private TaskList tasks;
    private Ui ui;

    public Sheng() {
        ui = new Ui();
        tasks = new TaskList();
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
                        ui.showTaskMarked(tasks.getTask(markIndex));
                        break;
                    case UNMARK:
                        int unmarkIndex = Parser.getTaskIndex(input, tasks.getTaskCount());
                        tasks.unmarkTask(unmarkIndex);
                        ui.showTaskUnmarked(tasks.getTask(unmarkIndex));
                        break;
                    case DELETE:
                        int deleteIndex = Parser.getTaskIndex(input, tasks.getTaskCount());
                        Task deletedTask = tasks.deleteTask(deleteIndex);
                        ui.showTaskDeleted(deletedTask, tasks.getTaskCount());
                        break;
                    case TODO:
                        String todoDesc = Parser.getTodoDescription(input);
                        Task todoTask = new Todo(todoDesc);
                        tasks.addTask(todoTask);
                        ui.showTaskAdded(todoTask, tasks.getTaskCount());
                        break;
                    case DEADLINE:
                        String deadlineDesc = Parser.getDeadlineDescription(input);
                        String by = Parser.getDeadlineBy(input);
                        Task deadlineTask = new Deadline(deadlineDesc, by);
                        tasks.addTask(deadlineTask);
                        ui.showTaskAdded(deadlineTask, tasks.getTaskCount());
                        break;
                    case EVENT:
                        String eventDesc = Parser.getEventDescription(input);
                        String from = Parser.getEventFrom(input);
                        String to = Parser.getEventTo(input);
                        Task eventTask = new Event(eventDesc, from, to);
                        tasks.addTask(eventTask);
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
