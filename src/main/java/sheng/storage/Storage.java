package sheng.storage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import sheng.exception.ShengException;
import sheng.task.Deadline;
import sheng.task.Event;
import sheng.task.Task;
import sheng.task.Todo;

/**
 * Handles loading and saving tasks to a file.
 */
public class Storage {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final int MIN_TASK_PARTS = 3;
    private static final int MIN_DEADLINE_PARTS = 4;
    private static final int MIN_EVENT_PARTS = 5;
    private static final int TASK_TYPE_INDEX = 0;
    private static final int TASK_DONE_INDEX = 1;
    private static final int TASK_DESC_INDEX = 2;
    private static final int TASK_DATETIME_INDEX = 3;
    private static final int EVENT_TO_INDEX = 4;
    private final Path filePath;

    public Storage(String filePath) {
        assert filePath != null : "File path cannot be null";
        this.filePath = Paths.get(filePath);
    }

    public Storage() {
        this("data/sheng.txt");
    }

    public ArrayList<Task> load() {
        ArrayList<Task> tasks = new ArrayList<>();
        
        try {
            File directory = filePath.getParent().toFile();
            if (!directory.exists()) {
                directory.mkdirs();
            }
            
            File file = filePath.toFile();
            if (!file.exists()) {
                file.createNewFile();
                return tasks;
            }
            
            List<String> lines = Files.readAllLines(filePath);
            tasks = lines.stream()
                    .map(line -> {
                        try {
                            return parseTask(line);
                        } catch (Exception e) {
                            System.out.println("Warning: Skipping corrupted line: " + line);
                            return null;
                        }
                    })
                    .filter(task -> task != null)
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (IOException e) {
            System.out.println("Error loading tasks: " + e.getMessage());
        }
        
        assert tasks != null : "Task list should never be null";
        return tasks;
    }

    public void save(ArrayList<Task> tasks) {
        assert tasks != null : "Tasks list cannot be null";
        try {
            File directory = filePath.getParent().toFile();
            if (!directory.exists()) {
                directory.mkdirs();
            }
            
            FileWriter writer = new FileWriter(filePath.toFile());
            for (Task task : tasks) {
                writer.write(task.toFileFormat() + System.lineSeparator());
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }

    private Task parseTask(String line) throws ShengException {
        assert line != null : "Line to parse cannot be null";
        String[] parts = line.split(" \\| ");
        
        if (parts.length < MIN_TASK_PARTS) {
            throw new ShengException("Invalid task format!");
        }
        assert parts.length >= MIN_TASK_PARTS : "Parts array should have minimum length after validation";
        
        String type = parts[TASK_TYPE_INDEX];
        boolean isDone = parts[TASK_DONE_INDEX].equals("1");
        String description = parts[TASK_DESC_INDEX];
        
        Task task;
        switch (type) {
        case "T":
            task = new Todo(description);
            break;
        case "D":
            if (parts.length < MIN_DEADLINE_PARTS) {
                throw new ShengException("Invalid deadline format!");
            }
            try {
                LocalDateTime by = LocalDateTime.parse(parts[TASK_DATETIME_INDEX], FORMATTER);
                task = new Deadline(description, by);
            } catch (DateTimeParseException e) {
                throw new ShengException("Invalid date format! Please use: yyyy-MM-dd HHmm");
            }
            break;
        case "E":
            if (parts.length < MIN_EVENT_PARTS) {
                throw new ShengException("Invalid event format!");
            }
            try {
                LocalDateTime from = LocalDateTime.parse(parts[TASK_DATETIME_INDEX], FORMATTER);
                LocalDateTime to = LocalDateTime.parse(parts[EVENT_TO_INDEX], FORMATTER);
                task = new Event(description, from, to);
            } catch (DateTimeParseException e) {
                throw new ShengException("Invalid date format! Please use: yyyy-MM-dd HHmm");
            }
            break;
        default:
            throw new ShengException("Unknown task type: " + type);
        }
        
        if (isDone) {
            task.markAsDone();
        }
        
        return task;
    }
}
