import java.io.*;
import java.util.concurrent.*;

public class LargeFileProcessor {
    private static final String INPUT_FILE = "large_input.txt";
    private static final String OUTPUT_FILE = "processed_output.txt";
    private static final int NUM_THREADS = 4;

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        
        try (BufferedReader reader = new BufferedReader(new FileReader(INPUT_FILE));
             BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FILE))) {
            
            String line;
            while ((line = reader.readLine()) != null) {
                String finalLine = line;
                executor.submit(() -> processLine(finalLine, writer));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("Processing completed!");
    }

    private static synchronized void processLine(String line, BufferedWriter writer) {
        try {
            writer.write(line.toUpperCase()); // Example processing: Convert to uppercase
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
