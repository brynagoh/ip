package duke;

import duke.task.Deadline;
import duke.task.Event;
import duke.task.Task;
import duke.task.Todo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Storage {
    String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
        String[] directories = filePath.split("/");
        // check if the directory and file exists
        try {
            //check all directories. (ignores file)
            String currentPath = "";
            for (int i = 0; i < directories.length - 1; i++) {
                currentPath += directories[i] + "/";
                if (Files.notExists(Paths.get(currentPath))) {
                    Files.createDirectory(Paths.get(currentPath));
                }
            }

            if (Files.notExists(Paths.get(filePath))) {
                Files.createFile(Paths.get(filePath));
            }
        } catch (IOException e) {
            System.out.println("Exception occurred when creating directory/file at given path! :(");
        }
    }

    public void saveTasklist(ArrayList<Task> tasks) {
        try {
            FileWriter fw = new FileWriter(this.filePath);
            for (Task task : tasks) {
                fw.write(task.toString() + System.lineSeparator());
            }
            fw.close();
    }
        catch (IOException e) {
        System.out.println("Exception occurred when writing tasklist to file! :(");
    }

    }

    public ArrayList<Task> loadTasklist() {
        ArrayList<Task> tasks = new ArrayList<>();
        try {
            // check if the directory and file exists
            if (Files.exists(Paths.get(this.filePath))) {
                File f = new File(filePath);
                Scanner scanner = new Scanner(f);
                while (scanner.hasNext()) {
                    String taskString = scanner.nextLine();
                    if (taskString.startsWith("[E]")) {
                        String description = taskString.substring(7, taskString.indexOf('(')-1);
                        String timing = taskString.substring(taskString.indexOf('(') + 5, taskString.indexOf(')'));
                        tasks.add(Task.taskCount, new Event(description, timing));
                    } else if (taskString.startsWith("[D]")) {
                        String description = taskString.substring(7, taskString.indexOf('(')-1);
                        String timing = taskString.substring(taskString.indexOf('(') + 5, taskString.indexOf(')'));
                        tasks.add(Task.taskCount, new Deadline(description, timing));
                    } else if (taskString.startsWith("[T]")) {
                        String description = taskString.substring(7);
                        tasks.add(Task.taskCount, new Todo(description));
                    }
                    if (taskString.contains("[X]")) {
                        tasks.get(Task.taskCount - 1).markAsDone();
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Tasklist file not found! :(");
        }
        return tasks;
    }



}