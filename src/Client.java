import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {
    public static void main(String[] args) throws UnknownHostException, IOException {
        Client client = new Client();
        ExecutorService executor = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 20000; i++) {
            executor.execute(client.getRunnable());
        }
        executor.shutdown();
    }

    public Runnable getRunnable() throws UnknownHostException, IOException {
        return new Runnable() {
            @Override
            public void run() {
                int port = 8010;
                int retries = 5;
                while (retries > 0) {
                    retries--;
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        InetAddress address = InetAddress.getByName("localhost");
                        Socket socket = new Socket(address, port);
                        try (
                                PrintWriter toSocket = new PrintWriter(socket.getOutputStream(), true);
                                BufferedReader fromSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()))
                        ) {
                            toSocket.println("Hello from Client " + socket.getLocalSocketAddress());
                            String line = fromSocket.readLine();
                            System.out.println("Response from Server " + line);
                        } finally {
                            try {
                                if (socket != null) {
                                    socket.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }
}
