import java.util.Objects;
import java.util.UUID;

/**
 * Immutable domain model for a single unit of work assigned to an employee.
 * Use {@link #withTitle}, {@link #withDescription} and {@link #withTimeRequired}
 * to derive updated copies instead of mutating shared state.
 *
 * <p>Note: declared package-private because the containing file uses the
 * {@code ai-made-} prefix required by the exercise.</p>
 */
final class Task {

    private final String id;
    private final String employeeName;
    private final String title;
    private final String description;
    private final int timeRequired;

    public Task(String employeeName, String title, String description, int timeRequired) {
        this(UUID.randomUUID().toString(), employeeName, title, description, timeRequired);
    }

    public Task(String id, String employeeName, String title, String description, int timeRequired) {
        this.id = requireText(id, "id");
        this.employeeName = requireText(employeeName, "employeeName");
        this.title = requireText(title, "title");
        this.description = description == null ? "" : description.trim();
        this.timeRequired = requirePositive(timeRequired);
    }

    public String getId() {
        return id;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getTimeRequired() {
        return timeRequired;
    }

    public Task withTitle(String newTitle) {
        return new Task(id, employeeName, newTitle, description, timeRequired);
    }

    public Task withDescription(String newDescription) {
        return new Task(id, employeeName, title, newDescription, timeRequired);
    }

    public Task withTimeRequired(int newTimeRequired) {
        return new Task(id, employeeName, title, description, newTimeRequired);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Task)) {
            return false;
        }
        return id.equals(((Task) other).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Title: %s, Description: %s, Time Required: %d hour(s) [id=%s]",
                title, description, timeRequired, id);
    }

    private static String requireText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        return value.trim();
    }

    private static int requirePositive(int value) {
        if (value <= 0) {
            throw new IllegalArgumentException("timeRequired must be a positive number of hours");
        }
        return value;
    }
}
