import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Text based front end. Owns the {@link Scanner}, renders menus and translates
 * user input into calls on {@link TaskManager}. No business rules live here.
 */
class ConsoleUI {

    private static final int MENU_MIN = 1;
    private static final int MENU_MAX = 6;

    private final Scanner scanner;
    private final TaskManager manager;
    private boolean inputExhausted;

    public ConsoleUI(TaskManager manager, Scanner scanner) {
        this.manager = manager;
        this.scanner = scanner;
    }

    /** Runs the menu loop until the user exits or the input stream closes. */
    public void run() {
        System.out.println("=== Employee Task Tracker ===");
        boolean running = true;
        while (running && hasMoreInput()) {
            printMenu();
            int choice = readInt("Enter your choice: ", MENU_MIN, MENU_MAX);
            switch (choice) {
                case 1 -> addTask();
                case 2 -> viewTasks();
                case 3 -> updateTask();
                case 4 -> removeTask();
                case 5 -> listEmployees();
                case 6 -> running = false;
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
        System.out.println("Goodbye.");
    }

    private void printMenu() {
        System.out.println();
        System.out.println("1. Add Task");
        System.out.println("2. View Tasks");
        System.out.println("3. Update Task");
        System.out.println("4. Remove Task");
        System.out.println("5. List Employees");
        System.out.println("6. Exit");
    }

    private void addTask() {
        String employee = readNonBlank("Enter employee name: ");
        String title = readNonBlank("Enter task title: ");
        String description = readLine("Enter task description (optional): ");
        int timeRequired = selectTimeRequired();

        Optional<Task> created = manager.addTask(employee, title, description, timeRequired);
        if (created.isPresent()) {
            System.out.println("Task added for " + created.get().getEmployeeName()
                    + " (id=" + created.get().getId() + ").");
        } else {
            System.out.println("Could not add the task. Check the title and time required.");
        }
    }

    private void viewTasks() {
        String employee = readNonBlank("Enter employee name: ");
        List<Task> tasks = manager.getTasksByEmployee(employee);

        if (tasks.isEmpty()) {
            System.out.println("No tasks assigned to " + employee);
            return;
        }
        System.out.println("Tasks for " + employee + ":");
        printNumberedTasks(tasks);
    }

    private void updateTask() {
        String employee = readNonBlank("Enter employee name: ");
        List<Task> tasks = manager.getTasksByEmployee(employee);
        if (tasks.isEmpty()) {
            System.out.println("No tasks assigned to " + employee);
            return;
        }

        System.out.println("Tasks for " + employee + ":");
        printNumberedTasks(tasks);
        Task selected = promptTaskSelection(tasks);
        if (selected == null) {
            return;
        }

        String title = emptyToNull(readOptional("New title (blank keeps \"" + selected.getTitle() + "\"): "));
        String description = emptyToNull(readOptional("New description (blank keeps current): "));
        Integer timeRequired = readOptionalTimeRequired();

        boolean updated = manager.updateTask(
                selected.getId(),
                title,
                description,
                timeRequired);

        System.out.println(updated
                ? "Task updated for " + employee + "."
                : "Task was not updated. Check the values you entered.");
    }

    private void removeTask() {
        String employee = readNonBlank("Enter employee name: ");
        List<Task> tasks = manager.getTasksByEmployee(employee);
        if (tasks.isEmpty()) {
            System.out.println("No tasks assigned to " + employee);
            return;
        }

        System.out.println("Tasks for " + employee + ":");
        printNumberedTasks(tasks);
        Task selected = promptTaskSelection(tasks);
        if (selected == null) {
            return;
        }

        System.out.println(selected);
        if (!readConfirmation("Remove this task? (y/n): ")) {
            System.out.println("Removal cancelled.");
            return;
        }

        System.out.println(manager.removeTask(selected.getId())
                ? "Task removed for " + employee + "."
                : "Task was not found.");
    }

    private void listEmployees() {
        List<String> employees = manager.getEmployees();
        if (employees.isEmpty()) {
            System.out.println("No employees have tasks yet.");
            return;
        }
        System.out.println("Employees with assigned tasks:");
        for (String employee : employees) {
            System.out.printf("- %s (%d task(s))%n", employee, manager.getTasksByEmployee(employee).size());
        }
    }

    private int selectTimeRequired() {
        while (true) {
            System.out.println("Select time required for the task:");
            System.out.println("1. 1 hour");
            System.out.println("2. 2 hours");
            System.out.println("3. 3 hours");
            System.out.println("4. 4 hours");
            System.out.println("5. Enter custom time");
            int choice = readInt("Enter your choice: ", 1, 5);

            if (choice >= 1 && choice <= 4) {
                return choice;
            }
            int custom = readHours("Enter custom time (in hours): ");
            if (custom > 0) {
                return custom;
            }
            if (inputExhausted) {
                return 1;
            }
        }
    }

    private void printNumberedTasks(List<Task> tasks) {
        for (int i = 0; i < tasks.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, tasks.get(i));
        }
    }

    /**
     * @return the selected task, or {@code null} when the user chooses to cancel
     *         or supplies an out of range number
     */
    private Task promptTaskSelection(List<Task> tasks) {
        int chosen = readInt("Enter task number: ", 1, tasks.size());
        if (chosen < 1) {
            return null;
        }
        return tasks.get(chosen - 1);
    }

    private Integer readOptionalTimeRequired() {
        while (true) {
            String raw = readLine("New time required in hours (blank keeps current): ");
            if (raw.isEmpty()) {
                return null;
            }
            Optional<Integer> parsed = tryParseInt(raw);
            if (parsed.isPresent() && parsed.get() > 0) {
                return parsed.get();
            }
            if (inputExhausted) {
                return null;
            }
            System.out.println("Please enter a positive whole number of hours, or leave blank.");
        }
    }

    private int readHours(String prompt) {
        while (true) {
            String raw = readLine(prompt);
            Optional<Integer> parsed = tryParseInt(raw);
            if (parsed.isPresent() && parsed.get() > 0) {
                return parsed.get();
            }
            if (inputExhausted) {
                return -1;
            }
            System.out.println("Please enter a positive whole number of hours.");
        }
    }

    private String readNonBlank(String prompt) {
        while (true) {
            String value = readLine(prompt);
            if (!value.isEmpty()) {
                return value;
            }
            if (inputExhausted) {
                return "";
            }
            System.out.println("This value cannot be empty.");
        }
    }

    /** Reads a trimmed line, allowing an empty value. */
    private String readOptional(String prompt) {
        return readLine(prompt);
    }

    private int readInt(String prompt, int min, int max) {
        while (true) {
            String raw = readLine(prompt);
            Optional<Integer> parsed = tryParseInt(raw);
            if (parsed.isPresent() && parsed.get() >= min && parsed.get() <= max) {
                return parsed.get();
            }
            if (inputExhausted) {
                return -1;
            }
            System.out.printf("Please enter a number between %d and %d.%n", min, max);
        }
    }

    private boolean readConfirmation(String prompt) {
        String answer = readLine(prompt);
        return answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("yes");
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        if (inputExhausted || !scanner.hasNextLine()) {
            inputExhausted = true;
            return "";
        }
        return scanner.nextLine().trim();
    }

    private boolean hasMoreInput() {
        return !inputExhausted && scanner.hasNextLine();
    }

    private static Optional<Integer> tryParseInt(String raw) {
        try {
            return Optional.of(Integer.parseInt(raw));
        } catch (NumberFormatException notANumber) {
            return Optional.empty();
        }
    }

    private static String emptyToNull(String value) {
        return value.isEmpty() ? null : value;
    }
}
