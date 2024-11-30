package utils;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class DBClient {

    private static final String hostPath = "localhost";
    private static final int portNo = 5000;
    public static void wrtieToFile(String filename, ArrayList<String> allData) throws IOException {
        try (Socket socket = new Socket(hostPath, portNo);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeUTF("write"); // specify the operation
            out.writeUTF(filename); // send the filename
            out.writeObject(allData); // send the serialized ArrayList
            out.flush();

            String response = in.readUTF(); // read server response
            System.out.println(response);

        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
    public static ArrayList<String> readFromFile(String filename) throws IOException, ClassNotFoundException {
        try (Socket socket = new Socket(hostPath, portNo);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeUTF("read"); // specify the operation
            out.writeUTF(filename); // send the filename
            out.flush();

            @SuppressWarnings("unchecked")
            ArrayList<String> data = (ArrayList<String>) in.readObject(); // read the serialized ArrayList
            if (!data.isEmpty()) {
                return data;
            }
            return null;
        }
    }
}