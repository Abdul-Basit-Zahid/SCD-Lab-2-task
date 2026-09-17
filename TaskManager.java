import java.util.ArrayList;
import java.util.List;

/**
 * Logic/service class that owns the task list and the core operations.
 * It performs no console I/O: no Scanner and no System.out.println here.
 * Every method that can fail reports the result back to the caller with a boolean.
 */
public class TaskManager {

    private final List<Task> tasks;

    public TaskManager() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Adds a new task to the list.
     */
    public void addTask(String employee, String title, String description, int timeRequired) {
        tasks.add(new Task(employee, title, description, timeRequired));
    }

    /**
     * Returns a new list containing every task that belongs to the given employee.
     * The comparison is case-insensitive, so "john" and "John" match.
     */
    public List<Task> getTasksByEmployee(String employee) {
        List<Task> employeeTasks = new ArrayList<>();
        if (employee == null) {
            return employeeTasks;
        }
        for (Task task : tasks) {
            if (task.getEmployee().equalsIgnoreCase(employee)) {
                employeeTasks.add(task);
            }
        }
        return employeeTasks;
    }

    /**
     * Returns a copy of all tasks currently stored.
     */
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    /**
     * Removes the task at {@code taskIndex} from the given employee's task list.
     *
     * @return true if the task was removed, false if the index was invalid
     */
    public boolean removeTask(String employee, int taskIndex) {
        List<Task> employeeTasks = getTasksByEmployee(employee);
        if (!isValidIndex(taskIndex, employeeTasks)) {
            return false;
        }
        tasks.remove(employeeTasks.get(taskIndex));
        return true;
    }

    /**
     * Updates the task at {@code taskIndex} for the given employee.
     * A null or blank value means "keep the current value".
     *
     * @return true if the task was found and updated, false if the index was invalid
     */
    public boolean updateTask(String employee, int taskIndex, String newTitle,
                              String newDescription, Integer newTimeRequired) {
        List<Task> employeeTasks = getTasksByEmployee(employee);
        if (!isValidIndex(taskIndex, employeeTasks)) {
            return false;
        }

        Task task = employeeTasks.get(taskIndex);

        if (newTitle != null && !newTitle.isEmpty()) {
            task.setTitle(newTitle);
        }
        if (newDescription != null && !newDescription.isEmpty()) {
            task.setDescription(newDescription);
        }
        if (newTimeRequired != null) {
            task.setTimeRequired(newTimeRequired);
        }
        return true;
    }

    private boolean isValidIndex(int index, List<Task> employeeTasks) {
        return index >= 0 && index < employeeTasks.size();
    }
}
