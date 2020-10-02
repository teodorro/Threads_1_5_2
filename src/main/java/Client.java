import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
    public void start(int port){
        try {
            InetSocketAddress socketAddress = new InetSocketAddress("localhost", port);
            final SocketChannel socketChannel = SocketChannel.open();
            socketChannel.connect(socketAddress);
            try (Scanner scanner = new Scanner(System.in)) {
                final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);
                String msg;
                while (true) {
                    System.out.println("Enter string with spaces...");
                    msg = scanner.nextLine();
                    if ("end".equals(msg)) break;
                    socketChannel.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));
                    Thread.sleep(200);
                    int bytesCount = socketChannel.read(inputBuffer);
                    System.out.println(new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8).trim());
                    inputBuffer.clear();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                socketChannel.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
