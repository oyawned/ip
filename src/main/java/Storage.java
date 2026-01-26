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

public class Storage {
    private static final String FILE_PATH = "data/sheng.txt";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private final Path filePath;

    public Storage() {
        this.filePath = Paths.get(FILE_PATH);
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
            for (String line : lines) {
                try {
                    Task task = parseTask(line);
                    if (task != null) {
                        tasks.add(task);
                    }
                } catch (Exception e) {
                    System.out.println("Warning: Skipping corrupted line: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading tasks: " + e.getMessage());
        }
        
        return tasks;
    }

    public void save(ArrayList<Task> tasks) {
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
        String[] parts = line.split(" \\| ");
        
        if (parts.length < 3) {
            throw new ShengException("Invalid task format!");
        }
        
        String type = parts[0];
        boolean isDone = parts[1].equals("1");
        String description = parts[2];
        
        Task task;
        switch (type) {
            case "T":
                task = new Todo(description);
                break;
            case "D":
                if (parts.length < 4) {
                    throw new ShengException("Invalid deadline format!");
                }
                try {
                    LocalDateTime by = LocalDateTime.parse(parts[3], FORMATTER);
                    task = new Deadline(description, by);
                } catch (DateTimeParseException e) {
                    throw new ShengException("Invalid date format! Use: yyyy-MM-dd HHmm");
                }
                break;
            case "E":
                if (parts.length < 5) {
                    throw new ShengException("Invalid event format!");
                }
                try {
                    LocalDateTime from = LocalDateTime.parse(parts[3], FORMATTER);
                    LocalDateTime to = LocalDateTime.parse(parts[4], FORMATTER);
                    task = new Event(description, from, to);
                } catch (DateTimeParseException e) {
                    throw new ShengException("Invalid date format! Use: yyyy-MM-dd HHmm");
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
