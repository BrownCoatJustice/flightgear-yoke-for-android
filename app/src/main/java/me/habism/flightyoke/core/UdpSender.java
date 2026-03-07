package me.habism.flightyoke.core;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UdpSender {

    private final String host;
    private final int port;
    private DatagramSocket sock;
    private InetAddress addr;
    private static final long SEND_INTERVAL_MS = 50; // 20 Hz
    private long lastSendTime = 0;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public UdpSender(String host, int port) {
        this.host = host;
        this.port  = port;

        try {
            sock = new DatagramSocket();
            addr = InetAddress.getByName(host);
        } catch(Exception e) {
            e.printStackTrace();
            // TODO: Implement better logging and error catching.
        }
    }

    public void send(float roll, float pitch) {

        long now = System.currentTimeMillis();
        if (now - lastSendTime < SEND_INTERVAL_MS) {
            return;
        }

        executor.execute(() -> {
            try {
                String message = roll + "," + pitch + "\n";

                byte[] data = message.getBytes(StandardCharsets.UTF_8);

                DatagramPacket packet =
                        new DatagramPacket(data, data.length, addr, port);
                sock.send(packet);
                AppLogger.d("Sent: " + roll + ',' + pitch);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void close() {
        if (sock != null && !sock.isClosed()) {
            sock.close();
        }
    }

}
