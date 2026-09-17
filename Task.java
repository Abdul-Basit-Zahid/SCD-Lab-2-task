/**
 * Model class representing a single task assigned to an employee.
 * This class only holds data (plus getters/setters) and knows nothing
 * about input, output, or how tasks are stored.
 */
public class Task {

    private String employee;
    private String title;
    private String description;
    private int timeRequired; // in hours

    public Task(String employee, String title, String description, int timeRequired) {
        this.employee = employee;
        this.title = title;
        this.description = description;
        this.timeRequired = timeRequired;
    }

    // ---------- Getters ----------

    public String getEmployee() {
        return employee;
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

    // ---------- Setters ----------

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTimeRequired(int timeRequired) {
        this.timeRequired = timeRequired;
    }

    @Override
    public String toString() {
        return "Title: " + title
                + ", Description: " + description
                + ", Time Required: " + timeRequired + " hours";
    }
}
