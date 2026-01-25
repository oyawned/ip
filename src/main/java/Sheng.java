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
            String input = ui.readCommand();
            String command = Parser.getCommand(input);
            
            switch (command) {
                case "bye":
                    ui.showGoodbye();
                    isExit = true;
                    break;
                case "list":
                    ui.showTaskList(tasks.getAllTasks());
                    break;
                case "mark":
                    int markIndex = Parser.getTaskIndex(input);
                    tasks.markTask(markIndex);
                    ui.showTaskMarked(tasks.getTask(markIndex));
                    break;
                case "unmark":
                    int unmarkIndex = Parser.getTaskIndex(input);
                    tasks.unmarkTask(unmarkIndex);
                    ui.showTaskUnmarked(tasks.getTask(unmarkIndex));
                    break;
                case "todo":
                    String todoDesc = Parser.getTodoDescription(input);
                    Task todoTask = new Todo(todoDesc);
                    tasks.addTask(todoTask);
                    ui.showTaskAdded(todoTask, tasks.getTaskCount());
                    break;
                case "deadline":
                    String deadlineDesc = Parser.getDeadlineDescription(input);
                    String by = Parser.getDeadlineBy(input);
                    Task deadlineTask = new Deadline(deadlineDesc, by);
                    tasks.addTask(deadlineTask);
                    ui.showTaskAdded(deadlineTask, tasks.getTaskCount());
                    break;
                case "event":
                    String eventDesc = Parser.getEventDescription(input);
                    String from = Parser.getEventFrom(input);
                    String to = Parser.getEventTo(input);
                    Task eventTask = new Event(eventDesc, from, to);
                    tasks.addTask(eventTask);
                    ui.showTaskAdded(eventTask, tasks.getTaskCount());
                    break;
            }
        }
        
        ui.close();
    }

    public static void main(String[] args) {
        new Sheng().run();
    }
}
