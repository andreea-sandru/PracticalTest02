package ro.pub.cs.systems.eim.practicaltest02.network;


import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;
import ro.pub.cs.systems.eim.practicaltest02.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02.general.Utilities;
import ro.pub.cs.systems.eim.practicaltest02.models.TimeModel;

public class CommunicationThread extends Thread {

    private ServerThread serverThread;
    private Socket socket;

    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }

    @Override
    public void run() {
        if (socket == null) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!");
            return;
        }
        try {
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Buffered Reader / Print Writer are null!");
                return;
            }

            Log.i(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client");
            String command = bufferedReader.readLine();
            String clientAddress = bufferedReader.readLine();
            if (command == null || command.isEmpty() || clientAddress == null || clientAddress.isEmpty()) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error receiving parameters from client!");
                return;
            }

            HashMap<String, TimeModel> data = serverThread.getData();
            TimeModel timeModel = null;
            String result = null;

                if(command.equals("set")) {
                    Log.i(Constants.TAG, "[COMMUNICATION THREAD] Setting time and hour...");
                    if (data.containsKey(clientAddress)) {
                        Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the cache...");
                        timeModel = data.get(clientAddress);
                        String hour = bufferedReader.readLine();
                        String time = bufferedReader.readLine();
                        TimeModel newTimeModel = new TimeModel(hour, time);
                        result = "Overriding old data with new data" + hour + ":" + time;
                        serverThread.setData(clientAddress, newTimeModel);

                    }
                    else {
                        String hour = bufferedReader.readLine();
                        String time = bufferedReader.readLine();
                        timeModel = new TimeModel(hour, time);
                        serverThread.setData(clientAddress, timeModel);
                        result = "Alarm set for" + hour + ":" + time;
                    }
                }
                else if(command.equals("reset")) {
                    Log.i(Constants.TAG, "[COMMUNICATION THREAD] Resetting alarm for current client ...");
                    serverThread.setData(clientAddress, null);
                    result = "Reset client alarm to null";
                }
                else if(command.equals("poll")) {
                    Log.i(Constants.TAG, "[COMMUNICATION THREAD] Polling info...");

                    // open new tcp connection socket with server utcnist.colorado.edu, port 13
                    Integer eduPort = new Integer(13);
                    Socket s = new Socket("utcnist.colorado.edu", eduPort);


                    BufferedReader bufferedReader2 = Utilities.getReader(s);
                    bufferedReader2.readLine();
                    String line = bufferedReader2.readLine();

                    Log.d(Constants.TAG, "The server returned: " + line);

                    result = line;
                }

            if (result == null) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Result is null!");
                return;
            }

            printWriter.println(result);
            printWriter.flush();
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }

}