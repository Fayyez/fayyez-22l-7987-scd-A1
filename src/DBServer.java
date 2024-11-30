import java.io.*;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class DBServer {
    private static int portNo = 5000;
    private static String rootDir = "src/database/";
    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(portNo)) {
            while(true) {
                Socket client = server.accept();
                System.out.println("Client connected");
                new Thread(() -> {
                    handleClient(client);
                }).start();
            }
        } catch (Exception e) {
            System.out.println("Error in initialization: " + e.getMessage());
        }
    }

    private static String GetFilePath(String filename) {
        return rootDir + filename;
    }
    private synchronized static void handleClient(Socket client) {
        try (ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(client.getInputStream());) {

            String operation = in.readUTF(); // read the operation (read/write)
            String filename = in.readUTF(); // read the filename
            filename = GetFilePath(filename);

            if ("write".equalsIgnoreCase(operation)) {
                handleWriteRequests(in, filename);
                out.writeUTF("Write operation completed successfully.");
            } else if ("read".equalsIgnoreCase(operation)) {
                ArrayList<String> data = handleReadRequests(filename);
                out.writeObject(data);
            }
            if(!client.isClosed()) {
                out.flush();
                client.close();
            }
        } catch (IOException e) {
            System.out.println("IO Error in handling client: " + e.getMessage());
        }
    }

    private synchronized static void handleWriteRequests(ObjectInputStream in, String filepath) throws IOException {
        try (BufferedWriter br = new BufferedWriter(new FileWriter(filepath))) {
            @SuppressWarnings("unchecked")
            ArrayList<String> data = (ArrayList<String>) in.readObject();
            for(String line: data) {
                br.write(line);
                br.newLine();
            }

        } catch (IOException e) {
            throw new IOException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
    private static ArrayList<String> handleReadRequests(String filepath) throws IOException {
        try(BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            ArrayList<String> data = new ArrayList<>();
            String line;
            while((line = br.readLine()) != null) {
                data.add(line);
            }
            return data;
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

}
