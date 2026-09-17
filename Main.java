import java.util.List;
import java.util.Scanner;

/**
 * UI/CLI class: owns the console menu, the prompts and the Scanner.
 * It is the only class that reads input or prints output.
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final TaskManager taskManager = new TaskManager();

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            printMenu();
            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    addTask();
                    break;
                case 2:
                    viewTasks();
                    break;
                case 3:
                    removeTask();
                    break;
                case 4:
                    updateTask();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
        scanner.close();
    }

    // ---------- Menu actions ----------

    private static void addTask() {
        String employee = readLine("Enter employee name: ");
        String title = readLine("Enter task title: ");
        String description = readLine("Enter task description: ");
        int timeRequired = selectTimeRequired();

        taskManager.addTask(employee, title, description, timeRequired);
        System.out.println("Task added for " + employee);
    }

    private static void viewTasks() {
        String employee = readLine("Enter employee name: ");
        displayTasks(employee);
    }

    private static void removeTask() {
        String employee = readLine("Enter employee name: ");
        displayTasks(employee); // show tasks before removing

        if (taskManager.getTasksByEmployee(employee).isEmpty()) {
            return;
        }

        int taskIndex = readInt("Enter task number to remove: ") - 1;
        boolean removed = taskManager.removeTask(employee, taskIndex);

        if (removed) {
            System.out.println("Task removed for " + employee);
        } else {
            System.out.println("Invalid task index for " + employee);
        }
    }

    private static void updateTask() {
        String employee = readLine("Enter employee name: ");
        displayTasks(employee); // show tasks before updating

        if (taskManager.getTasksByEmployee(employee).isEmpty()) {
            return;
        }

        int taskIndex = readInt("Enter task number to update: ") - 1;

        String newTitle = readLine("Enter new task title (leave blank to keep current): ");
        String newDescription = readLine("Enter new task description (leave blank to keep current): ");
        Integer newTimeRequired = readOptionalInt("Enter new time required (in hours), or leave blank to keep current: ");

        boolean updated = taskManager.updateTask(employee, taskIndex, newTitle, newDescription, newTimeRequired);

        if (updated) {
            System.out.println("Task updated for " + employee);
        } else {
            System.out.println("Invalid task index for " + employee);
        }
    }

    private static int selectTimeRequired() {
        while (true) {
            System.out.println("Select time required for the task:");
            System.out.println("1. 1 hour");
            System.out.println("2. 2 hours");
            System.out.println("3. 3 hours");
            System.out.println("4. 4 hours");
            System.out.println("5. Enter custom time");

            switch (readInt("Enter your choice: ")) {
                case 1:
                    return 1;
                case 2:
                    return 2;
                case 3:
                    return 3;
                case 4:
                    return 4;
                case 5:
                    return readInt("Enter custom time (in hours): ");
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    // ---------- Display helpers ----------

    private static void printMenu() {
        System.out.println("1. Add Task");
        System.out.println("2. View Tasks");
        System.out.println("3. Remove Task");
        System.out.println("4. Update Task");
        System.out.println("5. Exit");
    }

    private static void displayTasks(String employee) {
        List<Task> employeeTasks = taskManager.getTasksByEmployee(employee);

        if (employeeTasks.isEmpty()) {
            System.out.println("No tasks assigned to " + employee);
            return;
        }

        System.out.println("Tasks for " + employee + ":");
        for (int i = 0; i < employeeTasks.size(); i++) {
            // i + 1 because the list is zero-based but the user sees 1-based numbering
            System.out.println((i + 1) + ". " + employeeTasks.get(i));
        }
    }

    // ---------- Input helpers ----------

    /**
     * Reads a whole line of input and removes surrounding whitespace.
     */
    private static String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    /**
     * Reads an integer using Integer.parseInt on the whole line, inside a try-catch.
     * This avoids the classic Scanner.nextInt() bug where the leftover newline
     * character is consumed by the next read and produces an empty input.
     * Keeps re-prompting until a valid whole number is entered.
     */
    private static int readInt(String prompt) {
        while (true) {
            String input = readLine(prompt);
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter a whole number.");
            }
        }
    }

    /**
     * Same as readInt, but returns null when the user leaves the line blank.
     * Used for update prompts where blank means "keep the current value".
     */
    private static Integer readOptionalInt(String prompt) {
        while (true) {
            String input = readLine(prompt);
            if (input.isEmpty()) {
                return null;
            }
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter a whole number or leave blank.");
            }
        }
    }
}
