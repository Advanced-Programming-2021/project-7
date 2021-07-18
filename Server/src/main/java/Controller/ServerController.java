package Controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerController {
    private static ServerController instance;

    private ServerController() {}

    public static ServerController getInstance() {
        if (instance == null)
            return new ServerController();
        return instance;
    }

     public void runServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(7755);
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(() -> {
                    try {
                        DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                        process(inputStream, outputStream);
                        inputStream.close();
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void process(DataInputStream inputStream, DataOutputStream outputStream) throws IOException {
        while (true) {
            String command;
            try {
                command = inputStream.readUTF();
            } catch (Exception e) {
                break;
            }
            String output = processCommands(command);
            outputStream.writeUTF(output);
            outputStream.flush();
        }
    }

    private String processCommands(String command) {
        System.out.println(command);
        if (command.matches("^RegisterController#register.+")) {
            String[] inputs = command.split("#");
            return RegisterController.register(inputs[ 2 ], inputs[ 3 ], inputs[ 4 ]);
        } else if (command.matches("^RegisterProfileController#selcet#.+")) {
            String[] inputs = command.split("#");
            return RegisterProfileController.select(Integer.parseInt(inputs[ 2 ]), inputs[ 3 ]);
        } else if (command.matches("^LoginController#login#.+")) {
            String[] inputs = command.split("#");
            return LoginController.login(inputs[2], inputs[3]);
        }
        return "";
    }
}
