import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final int PORT = 9999;
    private static ServerSocket serverSocket;
    private static List<String> arguments = new ArrayList<>();

    /***
     * Application main entry
     * @param args an array of Strings
     */
    public static void main(String[] args) {
        // Try to bind port 9999 and store the first argument (file path)
        try {
            serverSocket = new ServerSocket(PORT);
            arguments.add(args[0]);
        } catch (IOException e) {
            // Not able to bind port 9999
            sendArgument(args[0]);
            System.exit(0);
        }

        // Start a new thread to handle socket connection and I/O
        // Receive arguments (file paths) from other processes if multiple files have been selected
        Thread thread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Socket socket = serverSocket.accept();
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                        String arg = reader.readLine();
                        arguments.add(arg);
                    } finally {
                        socket.close();
                    }
                }
            } catch (IOException e) {
                System.out.printf("%d files received.\nClosing server socket...\n", arguments.size());
            }
        });
        thread.start();

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.schedule(() -> {
            thread.interrupt();
            if (!serverSocket.isClosed()) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            executor.shutdown();
        }, 5, TimeUnit.SECONDS);

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 1. Convert a list of Strings (file paths) to an array of Strings with same size
        // 2. For each file path in the array:
        //      2.1 create a FileInfo object
        //      2.2 Check if the file path exists, filter out those not existed
        //      2.3 Generate the file information and format it for display
        String[] files = arguments.toArray(new String[arguments.size()]);
        String[][] data = Arrays.stream(files)
                                .map(FileInfo::new)
                                .filter(info -> info.validate())
                                .map(info -> info.generateInfo())
                                .toArray(String[][]::new);

        if (files.length != data.length) {
            JOptionPane.showMessageDialog(null, "One or more items selected are not files",
                    "Alert", JOptionPane.INFORMATION_MESSAGE);
        }

        // Display the final file(s) information
        GUI gui = new GUI(data);
    }

    /***
     * A private static method that will be called if the process wasn't able to bind port 9999,
     * the process will then send the argument to the process that bound port 9999.
     * @param arg the first argument passed into the main method
     */
    private static void sendArgument(String arg) {
        try (
                Socket socket = new Socket("localhost", PORT);
                PrintWriter writer = new PrintWriter(socket.getOutputStream())) {
            writer.println(arg);
            writer.flush();
        } catch (IOException e) {
            System.out.printf("Failed to send arg [%s] with error [%s]\n", arg, e.getMessage());
        }
    }
}