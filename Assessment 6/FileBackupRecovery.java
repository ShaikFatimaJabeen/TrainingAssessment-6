import java.io.*;
import java.nio.file.*;
import java.util.Scanner;

public class FileBackupRecovery {
    private static final String BACKUP_FOLDER = "backup/";
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        new File(BACKUP_FOLDER).mkdir(); // Ensure backup directory exists
        
        while (true) {
            System.out.println("\n1. Backup File\n2. Restore File\n3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1 -> {
                    System.out.print("Enter file path to backup: ");
                    String filePath = scanner.nextLine();
                    backupFile(filePath);
                }
                case 2 -> {
                    System.out.print("Enter backup file name to restore: ");
                    String backupFileName = scanner.nextLine();
                    restoreFile(backupFileName);
                }
                case 3 -> {
                    System.out.println("Exiting...");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }
    
    private static void backupFile(String filePath) {
        File originalFile = new File(filePath);
        if (!originalFile.exists()) {
            System.out.println("File not found!");
            return;
        }
        
        String backupFileName = BACKUP_FOLDER + originalFile.getName() + ".bak";
        try (InputStream in = new FileInputStream(originalFile);
             OutputStream out = new FileOutputStream(backupFileName)) {
            
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            
            System.out.println("Backup successful: " + backupFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void restoreFile(String backupFileName) {
        File backupFile = new File(BACKUP_FOLDER + backupFileName);
        if (!backupFile.exists()) {
            System.out.println("Backup file not found!");
            return;
        }
        
        String restoredFileName = "restored_" + backupFileName.replace(".bak", "");
        try (InputStream in = new FileInputStream(backupFile);
             OutputStream out = new FileOutputStream(restoredFileName)) {
            
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            
            System.out.println("File restored successfully: " + restoredFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
