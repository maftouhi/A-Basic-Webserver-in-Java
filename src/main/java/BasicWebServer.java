import lombok.extern.java.Log;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Log
public class BasicWebServer {

    public static void main(String[] args) throws IOException {

        // For a Two-way communication between the server and the client we need to establish

        /* 1 - A socket on the server side that listens for incoming connections and requests from the client
         -> it's an endpoint defined by an IP address and a port number. */

        try (ServerSocket serverSocket = new ServerSocket(1234)){

            log.info("Server Started,Waiting for client connection and Listening to requests ...");

            while (true){
                
                /* To connect to the server, the client must create a Socket instance with
                 the correct IP address and port number to which the server is bound when creating its .
                The server identifies the client based on the source IP address and port number of the incoming connection.*/


                /* When client sends a connection request to the server on the specified port
                 2 -  The connection is established and the client socket is created containing the IP address and port of the client */


                try (Socket client = serverSocket.accept()){

                    // Now we can exchange data between client and server

                    log.info(client.toString());

                    StringBuilder request = getRequestInfo(client);
                    System.out.println(" [ REQUEST ]"+"\n");
                    System.out.println(request);
                    System.out.println("------------------------------------------------");
                    String[] requestArray = request.toString().split(" ");

                    // Server Response in the endpoint /picture

                    if ( requestArray[0].equals("GET") && requestArray[1].equals("/picture")) {


                        String imagePath = "src/main/resources/picture.jpeg";

                        // We should get the output stream of the client to send him resources

                        OutputStream ClientOutput = client.getOutputStream();
                        ClientOutput.write(("HTTP/1.1 200 OK\r\n").getBytes());
                        ClientOutput.write(("\r\n").getBytes());
                        ClientOutput.write(loadImageFromFile(imagePath));
                        ClientOutput.flush();
                    }
                }
            }
        }
    }

    // Displaying Request in a well Formatted output
    private static StringBuilder getRequestInfo(Socket client) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(client.getInputStream());
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder request = new StringBuilder();

        String line;
        line = bufferedReader.readLine();
        while(!line.isBlank()){
            request.append(line+"\r\n");
            line = bufferedReader.readLine();
        }
        return request;
    }

    // Get image bytes
    private static byte[] loadImageFromFile(String filePath) throws IOException {
        Path imagePath = Paths.get(filePath);
        return Files.readAllBytes(imagePath);
    }

}
