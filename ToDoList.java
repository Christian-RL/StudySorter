import java.util.*;
import java.io.*;
import java.time.LocalDate;

public class ToDoList {
    private static List<Task> tasks = new ArrayList<>();
    private static boolean running = false;
    private static final String FILE_NAME = "tasks.txt";

    static {
        loadTasksFromFile();
    }

    private static void saveTasksToFile() {
        try (PrintWriter writer = new PrintWriter(FILE_NAME)) {
            for (Task task : tasks) {
                writer.printf("%s,%d,%.2f%n",
                        task.name(), task.dueDate(), task.worth());
            }
        } catch (IOException e) {
            System.err.println("Error saving tasks: " + e.getMessage());
        }
    }

    private static void loadTasksFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length != 3) {
                    System.err.println("Skipping invalid line: " + line);
                    continue;
                }

                try {
                    tasks.add(new Task(
                            parts[0],
                            Integer.parseInt(parts[1]),
                            Double.parseDouble(parts[2])
                    ));
                } catch (NumberFormatException e) {
                    System.err.println("Skipping invalid task: " + line);
                }
            }
            sort();
        } catch (FileNotFoundException e) {
            System.err.println("Task file not found: " + e.getMessage());
        }
    }

    private static void assignTask() {
        Scanner input = new Scanner(System.in);
        System.out.print("Name: ");
        String name = input.nextLine().trim();

        // Read and validate the date as a 6-digit string
        int date = 0;
        boolean validDate = false;
        while (!validDate) {
            System.out.print("Due date (DDMMYY, 6 digits): ");
            String dateStr = input.nextLine().trim();
            if (dateStr.length() != 6) {
                System.out.println("Date must be 6 digits (e.g., 311223).");
                continue;
            }
            try {
                date = Integer.parseInt(dateStr);
                validDate = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid date. Must be numeric.");
            }
        }

        System.out.print("Grade worth: ");
        double worth = input.nextDouble();
        input.nextLine(); // Consume newline

        tasks.add(new Task(name, date, worth));
        sort();
        saveTasksToFile();
        System.out.println("Assigned task: " + name);
    }

    private static void completeTask() {
        Scanner input = new Scanner(System.in);
        System.out.print("Name: ");
        String name = input.nextLine().trim();

        tasks.removeIf(task -> task.name().equalsIgnoreCase(name));
        saveTasksToFile();
        System.out.println("Completed task: " + name);
    }

    private static void viewNextTask() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks available.");
            return;
        }
        System.out.println(tasks.getFirst().useString());
    }

    private static void viewAllTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks available.");
            return;
        }
        for (Task task : tasks) {
            System.out.println(task.useString());
        }
    }

    private static void sort() {
        tasks.sort(Comparator.comparing(Task::getDueLocalDate).thenComparing(Comparator.comparingDouble(Task::worth).reversed()));
    }

    private static void run() {
        running = true;
        Scanner input = new Scanner(System.in);

        while (running) {
            System.out.println("What action would you like to do?: " +
                    "Assign Task, Complete Task, View Next Task, See All Tasks, or Exit?");

            String answer = input.nextLine().trim().toLowerCase();

            switch (answer) {
                case "assign task":
                    assignTask();
                    break;
                case "complete task":
                    completeTask();
                    break;
                case "view next task":
                    viewNextTask();
                    break;
                case "see all tasks":
                    viewAllTasks();
                    break;
                case "exit":
                    saveTasksToFile();
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
        input.close();
    }

    public static void main(String[] args) {
        run();
    }
}

