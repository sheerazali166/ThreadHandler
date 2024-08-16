package com.example.threadhandler;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    HandlerThread handlerThread;
    Handler handler;
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    ArrayList<String> pokemonArrayList = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView1);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerViewAdapter = new RecyclerViewAdapter(pokemonArrayList);
        recyclerView.setAdapter(recyclerViewAdapter);


        // TODO: Create a new Handler Thread
        handlerThread = new HandlerThread("MyHandler");
        handlerThread.start();


        // TODO: Attach a handler
        handler = new Handler(Looper.getMainLooper()) {

            @Override
            public void handleMessage(@NonNull Message message) {

                Bundle bundle = message.getData();
                String _message = bundle.getString("message");
                pokemonArrayList.add(_message);
                recyclerViewAdapter.notifyDataSetChanged();

                Log.d("Message: ", _message);

            }
        };

        final Button pokemonButton = findViewById(R.id.pokemonButton);
        pokemonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendNewMessage();
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // TODO: This method simulate that a sender send a new pokemon name to the app
    // TODO: Then we send a message to the handler thread
    private void sendNewMessage() {

        Message message = handler.obtainMessage();

        ArrayList<String> newPokemonsArrayList = new ArrayList<String>();

        newPokemonsArrayList.add("Pikachu");
        newPokemonsArrayList.add("Bulbasaur");
        newPokemonsArrayList.add("Charmander");

        int index = new Random().nextInt(newPokemonsArrayList.size());
        String pokemonName = newPokemonsArrayList.get(index);

        Bundle bundle = new Bundle();
        bundle.putString("message", pokemonName);

        message.setData(bundle);
        handler.sendMessage(message);

    }

    @Override
    protected void onStop() {
        super.onStop();
        handlerThread.quit();
    }
}