package me.habism.flightyoke.core;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UdpSender {

    private final String host;
    private final int port;
    private DatagramSocket sock;
    private InetAddress addr;
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
        executor.execute(() -> {
            try {
                String message = roll + "," + pitch + "\n";

                byte[] data = message.getBytes();

                DatagramPacket packet =
                        new DatagramPacket(data, data.length, addr, port);
                System.out.println("Sending: " + message);
                sock.send(packet);
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
