package vvat;

import org.apache.commons.net.telnet.TelnetServer;
import org.apache.commons.net.telnet.TelnetServerSocket;
import org.apache.commons.net.telnet.TelnetSocket;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BreakpointTelnetServer {
    private static final int DEFAULT_PORT = 2323;
    private final int port;
    private TelnetServer server;
    private volatile boolean running = false;

    public BreakpointTelnetServer(int port) {
        this.port = port;
    }

    public void start() {
        if (running) return;

        try {
            server = new TelnetServer(port) {
                @Override
                protected void handleConnection(@NotNull TelnetServerSocket telnetServerSocket) {
                    try {
                        TelnetSocket telnetSocket = telnetServerSocket.accept();
                        new ClientHandler(telnetSocket).start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            server.start();
            running = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (!running) return;

        try {
            server.stop();
            running = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private final TelnetSocket socket;

        ClientHandler(TelnetSocket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (InputStream in = socket.getInputStream();
                 OutputStream out = socket.getOutputStream()) {

                out.write("Breakpoint Manager Telnet Interface\r\n".getBytes());
                out.write("Commands: ebp - enable all breakpoints, dbp - disable all breakpoints, exit - quit\r\n".getBytes());
                out.flush();

                byte[] buffer = new byte[1024];
                int bytesRead;

                while ((bytesRead = in.read(buffer)) != -1) {
                    String command = new String(buffer, 0, bytesRead).trim().toLowerCase();

                    if ("exit".equals(command)) {
                        break;
                    } else if ("ebp".equals(command)) {
                        BreakpointManager.enableAllBreakpoints();
                        out.write("All breakpoints enabled\r\n".getBytes());
                    } else if ("dbp".equals(command)) {
                        BreakpointManager.disableAllBreakpoints();
                        out.write("All breakpoints disabled\r\n".getBytes());
                    } else {
                        out.write(("Unknown command: " + command + "\r\n").getBytes());
                    }
                    out.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
