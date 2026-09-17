import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Pure business logic for managing {@link Task} instances.
 * Contains no I/O: every operation validates its arguments and reports its
 * outcome through a boolean or an {@link Optional}.
 */
class TaskManager {

    private final List<Task> tasks = new ArrayList<>();

    /**
     * Creates and stores a task for the given employee.
     *
     * @return the created task, or an empty {@code Optional} when the input is invalid
     */
    public Optional<Task> addTask(String employeeName, String title, String description, int timeRequired) {
        try {
            Task task = new Task(employeeName, title, description, timeRequired);
            tasks.add(task);
            return Optional.of(task);
        } catch (IllegalArgumentException invalidInput) {
            return Optional.empty();
        }
    }

    /**
     * Applies a partial update to an existing task. Validation happens before the
     * task list is touched, so a rejected update leaves the stored task unchanged.
     *
     * @return {@code true} when the task was updated
     */
    public boolean updateTask(String taskId, String title, String description, Integer timeRequired) {
        Optional<Task> existing = findById(taskId);
        if (existing.isEmpty()) {
            return false;
        }

        Task updated;
        try {
            Task current = existing.get();
            updated = current
                    .withTitle(title == null ? current.getTitle() : title)
                    .withDescription(description == null ? current.getDescription() : description)
                    .withTimeRequired(timeRequired == null ? current.getTimeRequired() : timeRequired);
        } catch (IllegalArgumentException invalidInput) {
            return false;
        }

        int index = indexOf(taskId);
        tasks.set(index, updated);
        return true;
    }

    /**
     * Removes the task with the given id.
     *
     * @return {@code true} when a task was removed
     */
    public boolean removeTask(String taskId) {
        int index = indexOf(taskId);
        if (index < 0) {
            return false;
        }
        tasks.remove(index);
        return true;
    }

    public Optional<Task> findById(String taskId) {
        return tasks.stream()
                .filter(task -> task.getId().equals(taskId))
                .findFirst();
    }

    public List<Task> getTasksByEmployee(String employeeName) {
        String key = normalize(employeeName);
        return tasks.stream()
                .filter(task -> normalize(task.getEmployeeName()).equals(key))
                .sorted(Comparator.comparing(Task::getTitle, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

    public List<String> getEmployees() {
        return tasks.stream()
                .map(Task::getEmployeeName)
                .distinct()
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .toList();
    }

    public List<Task> getAllTasks() {
        return List.copyOf(tasks);
    }

    public int size() {
        return tasks.size();
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    private int indexOf(String taskId) {
        if (taskId == null) {
            return -1;
        }
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId().equals(taskId)) {
                return i;
            }
        }
        return -1;
    }

    private static String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }
}
