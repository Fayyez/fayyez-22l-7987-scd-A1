package utils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class DBClient {

    private static final String hostPath = "localhost";
    private static final int portNo = 5000;
    public static void wrtieToFile(String filename, ArrayList<String> allData) throws IOException {
        // send a request to the server to write all the data to the file
        Socket socket = new Socket(hostPath, portNo);
        // connect to server and send the data
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        for (String data: allData) {
            out.writeUTF(data);
        }
        socket.close();
    }
    public static ArrayList<String> readFromFile(String filename) {

    }
}