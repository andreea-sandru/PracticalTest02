package ro.pub.cs.systems.eim.practicaltest02.network;

import android.util.Log;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import ro.pub.cs.systems.eim.practicaltest02.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02.general.Utilities;

public class ClientThread extends Thread {

    private String address;
    private int port;
    private String hour, time, commandType;
    private TextView showResultTextView;
    private Socket socket;

    public ClientThread(String address, int port, String hour, String time, TextView showResult, String commandType) {
        this.address = address;
        this.port = port;
        this.hour = hour;
        this.time = time;
        this.showResultTextView = showResult;
        this.commandType = commandType;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(address, port);
            if (socket == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
                return;
            }
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Buffered Reader / Print Writer are null!");
                return;
            }

            switch (commandType) {
                case "set":
                    printWriter.println(commandType);
                    printWriter.flush();
                    printWriter.println(address);
                    printWriter.flush();
                    printWriter.println(hour);
                    printWriter.flush();
                    printWriter.println(time);
                    printWriter.flush();
                    break;
                case "reset":
                    printWriter.println(commandType);
                    printWriter.flush();
                    printWriter.println(address);
                    printWriter.flush();
                    break;

                case "poll":
                    printWriter.println(commandType);
                    printWriter.flush();
                    printWriter.println(address);
                    printWriter.flush();
                    break;
                default:
                    break;
            }


            String result;

            while ((result = bufferedReader.readLine()) != null) {
                final String finalizedInformation = result;
                showResultTextView.post(new Runnable() {
                    @Override
                    public void run() {
                        showResultTextView.setText(finalizedInformation);
                    }
                });
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }

}