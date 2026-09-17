import java.util.Scanner;

/** Entry point: wires the dependencies together and starts the console UI. */
final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        TaskManager manager = new TaskManager();
        try (Scanner scanner = new Scanner(System.in)) {
            new ConsoleUI(manager, scanner).run();
        }
    }
}
