package sheng.task;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TaskListTest {
    private TaskList taskList;
    private Task todo;
    private Task deadline;

    @BeforeEach
    public void setUp() {
        taskList = new TaskList();
        todo = new Todo("read book");
        try {
            deadline = new Deadline("return book", "2024-01-01 1800");
        } catch (Exception e) {
            // Should not happen in tests
        }
    }

    @Test
    public void addTask_singleTask_taskCountIsOne() {
        taskList.addTask(todo);
        assertEquals(1, taskList.getTaskCount());
    }

    @Test
    public void addTask_multipleTasks_correctTaskCount() {
        taskList.addTask(todo);
        taskList.addTask(deadline);
        assertEquals(2, taskList.getTaskCount());
    }

    @Test
    public void getTask_validIndex_returnsCorrectTask() {
        taskList.addTask(todo);
        taskList.addTask(deadline);
        assertEquals(todo, taskList.getTask(0));
        assertEquals(deadline, taskList.getTask(1));
    }

    @Test
    public void markTask_unmarkedTask_taskBecomesMarked() {
        taskList.addTask(todo);
        taskList.markTask(0);
        assertTrue(taskList.getTask(0).getStatusIcon().equals("X"));
    }

    @Test
    public void unmarkTask_markedTask_taskBecomesUnmarked() {
        taskList.addTask(todo);
        taskList.markTask(0);
        taskList.unmarkTask(0);
        assertTrue(taskList.getTask(0).getStatusIcon().equals(" "));
    }

    @Test
    public void deleteTask_singleTask_taskListBecomesEmpty() {
        taskList.addTask(todo);
        taskList.deleteTask(0);
        assertEquals(0, taskList.getTaskCount());
    }

    @Test
    public void deleteTask_multipleTask_correctTaskRemoved() {
        taskList.addTask(todo);
        taskList.addTask(deadline);
        Task deleted = taskList.deleteTask(0);
        assertEquals(1, taskList.getTaskCount());
        assertEquals(todo, deleted);
        assertEquals(deadline, taskList.getTask(0));
    }

    @Test
    public void constructor_withExistingTasks_correctTaskCount() {
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(todo);
        tasks.add(deadline);
        TaskList newTaskList = new TaskList(tasks);
        assertEquals(2, newTaskList.getTaskCount());
    }

    @Test
    public void getAllTasks_multipleTasks_returnsAllTasks() {
        taskList.addTask(todo);
        taskList.addTask(deadline);
        ArrayList<Task> allTasks = taskList.getAllTasks();
        assertEquals(2, allTasks.size());
        assertEquals(todo, allTasks.get(0));
        assertEquals(deadline, allTasks.get(1));
    }

    @Test
    public void getTaskCount_emptyList_returnsZero() {
        assertEquals(0, taskList.getTaskCount());
    }
}
