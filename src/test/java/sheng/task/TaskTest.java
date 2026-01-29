package sheng.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class TaskTest {

    @Test
    public void todo_newTask_isNotDone() {
        Task todo = new Todo("read book");
        assertEquals(" ", todo.getStatusIcon());
    }

    @Test
    public void todo_markAsDone_isDone() {
        Task todo = new Todo("read book");
        todo.markAsDone();
        assertEquals("X", todo.getStatusIcon());
    }

    @Test
    public void todo_markAsNotDone_isNotDone() {
        Task todo = new Todo("read book");
        todo.markAsDone();
        todo.markAsNotDone();
        assertEquals(" ", todo.getStatusIcon());
    }

    @Test
    public void todo_toString_correctFormat() {
        Task todo = new Todo("read book");
        assertEquals("[T][ ] read book", todo.toString());
    }

    @Test
    public void todo_toFileFormat_correctFormat() {
        Task todo = new Todo("read book");
        assertEquals("T | 0 | read book", todo.toFileFormat());
    }

    @Test
    public void todo_markedToFileFormat_correctFormat() {
        Task todo = new Todo("read book");
        todo.markAsDone();
        assertEquals("T | 1 | read book", todo.toFileFormat());
    }

    @Test
    public void deadline_toString_correctFormat() {
        try {
            Task deadline = new Deadline("return book", "2024-01-01 1800");
            assertTrue(deadline.toString().contains("[D]"));
            assertTrue(deadline.toString().contains("return book"));
            assertTrue(deadline.toString().contains("by:"));
        } catch (Exception e) {
            // Should not happen
        }
    }

    @Test
    public void deadline_toFileFormat_correctFormat() {
        try {
            Task deadline = new Deadline("return book", "2024-01-01 1800");
            assertEquals("D | 0 | return book | 2024-01-01 1800", deadline.toFileFormat());
        } catch (Exception e) {
            // Should not happen
        }
    }

    @Test
    public void event_toString_correctFormat() {
        try {
            Task event = new Event("meeting", "2024-01-01 1400", "2024-01-01 1600");
            assertTrue(event.toString().contains("[E]"));
            assertTrue(event.toString().contains("meeting"));
            assertTrue(event.toString().contains("from:"));
            assertTrue(event.toString().contains("to:"));
        } catch (Exception e) {
            // Should not happen
        }
    }

    @Test
    public void event_toFileFormat_correctFormat() {
        try {
            Task event = new Event("meeting", "2024-01-01 1400", "2024-01-01 1600");
            assertEquals("E | 0 | meeting | 2024-01-01 1400 | 2024-01-01 1600", event.toFileFormat());
        } catch (Exception e) {
            // Should not happen
        }
    }

    @Test
    public void getDescription_returnsCorrectDescription() {
        Task todo = new Todo("read book");
        assertEquals("read book", todo.getDescription());
    }
}
