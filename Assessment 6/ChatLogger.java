import java.io.*;
import java.util.Scanner;

public class ChatLogger {
    private static final String LOG_FILE = "chat_log.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Simple Chat Logger - Type 'exit' to quit");
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true));
             BufferedReader reader = new BufferedReader(new FileReader(LOG_FILE))) {
            
            // Display previous chat logs
            System.out.println("Previous Chat History:");
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            
            // Accept new messages
            while (true) {
                System.out.print("You: ");
                String message = scanner.nextLine();
                if ("exit".equalsIgnoreCase(message)) break;
                
                writer.write("You: " + message);
                writer.newLine();
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        System.out.println("Chat session ended. Messages saved to " + LOG_FILE);
    }
}
