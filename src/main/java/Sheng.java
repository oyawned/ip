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
                case "add":
                    Task newTask = new Task(input);
                    tasks.addTask(newTask);
                    ui.showTaskAdded(input);
                    break;
            }
        }
        
        ui.close();
    }

    public static void main(String[] args) {
        new Sheng().run();
    }
}
