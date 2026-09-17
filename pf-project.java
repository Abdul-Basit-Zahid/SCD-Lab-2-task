import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//.scanner is used for taking input
// arraylist (cuz more flexible since no need to define length) and list is used for storing the tasks
// btw we could have used import java.util.*; as well but this is to show what is in it


public class PROJECT {
//two class used for the projext
    
    private List<Task> tasks;
// this will be the list holding the tasks
    
    
    public PROJECT() {
        tasks = new ArrayList<>();
// initializes the tasks arrayList to take either string or integer cuz of <>, btw it is empty rn
        
    }

    public void addTask(String employee, String title, String description, int timeRequired) {
// one of the functions in the class to add the task
        Task task = new Task(employee, title, description, timeRequired);
        tasks.add(task);
//this is how we are adding the new task in the list which is called tasks
//tasks is new list
//Task is list that contains employee, title, description, timeRequired
//task is the name to crreate the task for Task
//so basically tasks>Task>task

        System.out.println("Task added for " + employee);
    }

    public void viewTasks(String employee) {
// second function and only by employee will the person be called out
        List<Task> employeeTasks = getTasksByEmployee(employee);
//this is accessing the employeeTasks from the list in the 1st function
        if (employeeTasks.isEmpty()) {
//built in method for if the number of tasks in employeeTasks are zero
            System.out.println("No tasks assigned to " + employee);
        } else {
            System.out.println("Tasks for " + employee + ":");
            for (int i = 0; i < employeeTasks.size(); i++) {
                System.out.println((i + 1) + ". " + employeeTasks.get(i).toString());
// this is basically iterating through the list and i+1 is done because the list starts from 0 
            }
        }
    }

    public void removeTask(String employee, int taskIndex) {
// third functino basically it is requiring the employee name and task numberto remove the task
        List<Task> employeeTasks = getTasksByEmployee(employee);
        if (taskIndex >= 0 && taskIndex < employeeTasks.size()) {
//.size() is built in function for the list
            tasks.remove(employeeTasks.get(taskIndex));
//.remove too is a built in function for the list
            System.out.println("Task removed for " + employee);
        } else {
            System.out.println("Invalid task index for " + employee);
        }
    }

    public void updateTask(String employee, int taskIndex, Scanner scanner) {
        List<Task> employeeTasks = getTasksByEmployee(employee);
        if (taskIndex >= 0 && taskIndex < employeeTasks.size()) {
            Task task = employeeTasks.get(taskIndex);
            System.out.print("Enter new task title (leave blank to keep current): ");
            String newTitle = scanner.nextLine();
            if (!newTitle.isEmpty()) {
                task.setTitle(newTitle);
            }
            System.out.print("Enter new task description (leave blank to keep current): ");
            String newDescription = scanner.nextLine();
            if (!newDescription.isEmpty()) {
                task.setDescription(newDescription);
            }
            System.out.print("Enter new time required (in hours), or leave blank to keep current: ");
            String newTimeRequired = scanner.nextLine();
            if (!newTimeRequired.isEmpty()) {
                int timeRequired = Integer.parseInt(newTimeRequired);
                task.setTimeRequired(timeRequired);
            }
            System.out.println("Task updated for " + employee);
        } else {
            System.out.println("Invalid task index for " + employee);
        }
    }

    public int selectTimeRequired(Scanner scanner) {
        while (true) {
            System.out.println("Select time required for the task:");
            System.out.println("1. 1 hour");
            System.out.println("2. 2 hours");
            System.out.println("3. 3 hours");
            System.out.println("4. 4 hours");
            System.out.println("5. Enter custom time");
            System.out.print("Enter your choice: ");
            int timeChoice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (timeChoice) {
                case 1:
                    return 1;
                case 2:
                    return 2;
                case 3:
                    return 3;
                case 4:
                    return 4;
                case 5:
                    System.out.print("Enter custom time (in hours): ");
                    int customTime = scanner.nextInt();
                    scanner.nextLine(); // consume newline
                    return customTime;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private List<Task> getTasksByEmployee(String employee) {
        List<Task> employeeTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getEmployee().equals(employee)) {
                employeeTasks.add(task);
            }
        }
        return employeeTasks;
    }

    public static void main(String[] args) {
        PROJECT system = new PROJECT();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Add Task");
            System.out.println("2. View Tasks");
            System.out.println("3. Remove Task");
            System.out.println("4. Update Task");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter employee name: ");
                    String employee = scanner.nextLine();
                    System.out.print("Enter task title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter task description: ");
                    String description = scanner.nextLine();
                    int timeRequired = system.selectTimeRequired(scanner);
                    system.addTask(employee, title, description, timeRequired);
                    break;
                case 2:
                    System.out.print("Enter employee name: ");
                    employee = scanner.nextLine();
                    system.viewTasks(employee);
                    break;
                case 3:
                    System.out.print("Enter employee name: ");
                    employee = scanner.nextLine();
                    system.viewTasks(employee); // show tasks before removing
                    System.out.print("Enter task number to remove: ");
                    int taskIndex = scanner.nextInt() - 1;
                    system.removeTask(employee, taskIndex);
                    break;
                case 4:
                    System.out.print("Enter employee name: ");
                    employee = scanner.nextLine();
                    system.viewTasks(employee); // show tasks before updating
                    System.out.print("Enter task number to update: ");
                    int updateTaskIndex = scanner.nextInt() - 1;
                    scanner.nextLine(); // consume newline
                    system.updateTask(employee, updateTaskIndex, scanner);
                    break;
                case 5:
                    System.out.println("Exiting...");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    
    static class Task {
// I chatgpt'd this since a new class is being used and idk y but it has used static class instead of public static
        private String employee;
        private String title;
        private String description;
        private int timeRequired; // would be in hours tho

        public Task(String employee, String title, String description, int timeRequired) {
            this.employee = employee;
            this.title = title;
            this.description = description;
            this.timeRequired = timeRequired;
        }

        public String getEmployee() {
            return employee;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getTimeRequired() {
            return timeRequired;
        }

        public void setTimeRequired(int timeRequired) {
            this.timeRequired = timeRequired;
        }

     @Override
// if i dont do this then it just gives me my file location in viewTasks method
public String toString() {
    return "Title: " + title + ", Description: " + description + ", Time Required: " + timeRequired + " hours";
}
    }}