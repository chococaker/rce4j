package org.rce4j.net;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

@Deprecated(since = "Was used for now-defunct websocket filesystem")
public class PortFileSender {
    public static int sendFileOverRandomPort(File file) {
        int port = UsablePortSearcher.get();
        sendFileOverPort(file, port);
        return port;
    }
    
    public static void sendFileOverPort(File file, int port) {
        if (!file.isFile())
            throw new UncheckedIOException(new FileNotFoundException("File not found: " + file.getName()));
        
        new Thread(() -> {
            try (ServerSocket server = new ServerSocket(port);
                 Socket client = server.accept();
                 InputStream in = new FileInputStream(file)) {
                
                int count;
                byte[] buffer = new byte[1024];
                while ((count = in.read(buffer)) > 0) {
                    client.getOutputStream().write(buffer, 0, count);
                }
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }).start();
    }
}
