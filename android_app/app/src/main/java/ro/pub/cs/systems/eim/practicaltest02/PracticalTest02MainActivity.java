package ro.pub.cs.systems.eim.practicaltest02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import ro.pub.cs.systems.eim.practicaltest02.network.ClientThread;
import ro.pub.cs.systems.eim.practicaltest02.network.ServerThread;
import ro.pub.cs.systems.eim.practicaltest02.general.Constants;

public class PracticalTest02MainActivity extends AppCompatActivity {

    private TextView showResult;
    private EditText serverPort, clientAddr, clientPort, clientHour, clientMinute;
    private Button serverConnect, clientSet, clientReset, clientPoll;

    private ServerThread serverThread = null;
    private ClientThread clientThread = null;

    private ButtonClickListener buttonClickListenerServer = new ButtonClickListener();
    private class ButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            String port = serverPort.getText().toString();
            if(port.isEmpty() || port == null) {
                Toast.makeText(getApplicationContext(), "Please fill server port", Toast.LENGTH_SHORT).show();
            }

            serverThread = new ServerThread(Integer.parseInt(port));

            if (serverThread.getServerSocket() == null) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread!");
                return;
            }
            serverThread.start();
        }
    }

    private ButtonClickListenerSet buttonClickListenerSet = new ButtonClickListenerSet();
    private class ButtonClickListenerSet implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            String port = clientPort.getText().toString();
            String addr = clientAddr.getText().toString();
            String minute = clientMinute.getText().toString();
            String hour = clientHour.getText().toString();

            if(port.isEmpty() || port == null) {
                Toast.makeText(getApplicationContext(), "Please fill client port", Toast.LENGTH_SHORT).show();
                return;
            }
            if(addr.isEmpty() || addr == null) {
                Toast.makeText(getApplicationContext(), "Please fill client addr", Toast.LENGTH_SHORT).show();
                return;
            }
            if(minute.isEmpty() || minute == null || hour.isEmpty() || hour == null) {
                Toast.makeText(getApplicationContext(), "Please fill minute and hour fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }

            clientThread = new ClientThread(
                    addr, Integer.parseInt(port), hour, minute, showResult, "set"
            );
            clientThread.start();
        }
    }


    private ButtonClickListenerReSet buttonClickListenerReSet = new ButtonClickListenerReSet();
    private class ButtonClickListenerReSet implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            String port = clientPort.getText().toString();
            String addr = clientAddr.getText().toString();

            if(port.isEmpty() || port == null) {
                Toast.makeText(getApplicationContext(), "Please fill client port", Toast.LENGTH_SHORT).show();
                return;
            }
            if(addr.isEmpty() || addr == null) {
                Toast.makeText(getApplicationContext(), "Please fill client addr", Toast.LENGTH_SHORT).show();
                return;
            }

            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }

            clientThread = new ClientThread(
                    addr, Integer.parseInt(port), "", "", showResult, "reset"
            );
            clientThread.start();
        }
    }

    private ButtonClickListenerPoll buttonClickListenerPoll = new ButtonClickListenerPoll();
    private class ButtonClickListenerPoll implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            String port = clientPort.getText().toString();
            String addr = clientAddr.getText().toString();

            if(port.isEmpty() || port == null) {
                Toast.makeText(getApplicationContext(), "Please fill client port", Toast.LENGTH_SHORT).show();
                return;
            }
            if(addr.isEmpty() || addr == null) {
                Toast.makeText(getApplicationContext(), "Please fill client addr", Toast.LENGTH_SHORT).show();
                return;
            }

            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }

            clientThread = new ClientThread(
                    addr, Integer.parseInt(port), "", "", showResult, "poll"
            );
            clientThread.start();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);

        serverPort = findViewById(R.id.serverPort);
        serverConnect = findViewById(R.id.serverConnect);
        clientAddr = findViewById(R.id.clientAddress);
        clientPort = findViewById(R.id.clientPort);
        clientHour = findViewById(R.id.clientHour);
        clientMinute = findViewById(R.id.clientMinute);
        clientSet = findViewById(R.id.clientSet);
        clientReset = findViewById(R.id.clientReset);
        clientPoll = findViewById(R.id.clientPoll);

        showResult = findViewById(R.id.showResult);

        serverConnect.setOnClickListener(buttonClickListenerServer);

        clientSet.setOnClickListener(buttonClickListenerSet);
        clientReset.setOnClickListener(buttonClickListenerReSet);
        clientPoll.setOnClickListener(buttonClickListenerPoll);

    }

    @Override
    protected void onDestroy() {
        Log.i("COLOCVIU TAG", "[MAIN ACTIVITY] onDestroy() callback method has been invoked");
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }
}