// Test Archive Feature
// This test will:
// 1. Add several tasks
// 2. List them
// 3. Archive all tasks
// 4. Verify the list is empty
// 5. Check that archive file was created

package sheng;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import sheng.storage.Storage;
import sheng.task.Task;
import sheng.task.TaskList;
import sheng.task.Todo;
import sheng.task.Deadline;
import sheng.exception.ShengException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the archive functionality.
 */
public class ArchiveTest {
    private Storage storage;
    private TaskList taskList;
    private Path tempDir;

    @BeforeEach
    public void setUp(@TempDir Path tempDir) {
        this.tempDir = tempDir;
        Path dataFile = tempDir.resolve("sheng.txt");
        storage = new Storage(dataFile.toString());
        taskList = new TaskList();
    }

    @Test
    public void testArchiveWithTasks() throws IOException, ShengException {
        // Add some tasks
        taskList.addTask(new Todo("Read book"));
        taskList.addTask(new Todo("Write essay"));
        taskList.addTask(new Deadline("Submit assignment", "2024-12-31 2359"));
        
        // Verify we have 3 tasks
        assertEquals(3, taskList.getTaskCount());
        
        // Archive all tasks
        String archiveFileName = storage.archiveAll(taskList.getAllTasks());
        
        // Verify archive file was created
        assertNotNull(archiveFileName);
        assertTrue(archiveFileName.startsWith("archive_"));
        assertTrue(archiveFileName.endsWith(".txt"));
        
        // Verify archive file exists
        Path archivePath = tempDir.resolve(archiveFileName);
        assertTrue(Files.exists(archivePath));
        
        // Verify archive file contains all tasks
        List<String> archiveLines = Files.readAllLines(archivePath);
        assertEquals(3, archiveLines.size());
        
        // Clear the task list
        taskList.clearAllTasks();
        assertEquals(0, taskList.getTaskCount());
        
        // Save the empty list
        storage.save(taskList.getAllTasks());
    }

    @Test
    public void testArchiveEmptyList() throws IOException {
        // Try to archive empty list
        assertEquals(0, taskList.getTaskCount());
        
        String archiveFileName = storage.archiveAll(taskList.getAllTasks());
        
        // Archive file should be created but empty
        assertNotNull(archiveFileName);
        Path archivePath = tempDir.resolve(archiveFileName);
        assertTrue(Files.exists(archivePath));
        
        List<String> archiveLines = Files.readAllLines(archivePath);
        assertEquals(0, archiveLines.size());
    }

    @Test
    public void testMultipleArchives() throws IOException {
        // First archive
        taskList.addTask(new Todo("Task 1"));
        String archive1 = storage.archiveAll(taskList.getAllTasks());
        taskList.clearAllTasks();
        
        // Wait a bit to ensure different timestamp
        try {
            Thread.sleep(1100); // Sleep for 1.1 seconds to ensure different timestamp
        } catch (InterruptedException e) {
            // Ignore
        }
        
        // Second archive
        taskList.addTask(new Todo("Task 2"));
        String archive2 = storage.archiveAll(taskList.getAllTasks());
        taskList.clearAllTasks();
        
        // Verify two different archive files were created
        assertNotEquals(archive1, archive2);
        assertTrue(Files.exists(tempDir.resolve(archive1)));
        assertTrue(Files.exists(tempDir.resolve(archive2)));
    }
}
