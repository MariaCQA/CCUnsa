package com.example.ccunsa.view.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.ccunsa.R;
import com.example.ccunsa.model.Pintura;
import com.example.ccunsa.service.AudioPlayService;
import com.example.ccunsa.viewmodel.PinturaViewModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PinturaFragment extends Fragment {
    private static final String TAG = "PinturaFragment";

    private TextView title, description;
    private ImageView image;
    private PinturaViewModel pinturaViewModel;
    private int pinturaId;
    private String audioFileName;
    private String imageFilePath;
    private ExecutorService executorService;

    private BroadcastReceiver audioCommandReceiver;

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pintura, container, false);
        title = view.findViewById(R.id.title);
        description = view.findViewById(R.id.description);
        image = view.findViewById(R.id.image);

        Button btnPlay = view.findViewById(R.id.play_button);
        Button btnPause = view.findViewById(R.id.pause_button);
        Button btnResume = view.findViewById(R.id.resume_button);
        Button btnStop = view.findViewById(R.id.stop_button);

        // Inicializar ExecutorService
        executorService = Executors.newSingleThreadExecutor();

        // Obtener el argumento pinturaId y agregar un log para verificar el valor
        if (getArguments() != null) {
            pinturaId = getArguments().getInt("pinturaId", -1);
            Log.d(TAG, "Received pinturaId: " + pinturaId);
        } else {
            Log.d(TAG, "No arguments received");
        }

        // Inicializar el ViewModel con el pinturaId y observar los cambios en los datos
        pinturaViewModel = new ViewModelProvider(this, new PinturaViewModel.Factory(requireActivity().getApplication(), pinturaId)).get(PinturaViewModel.class);
        pinturaViewModel.getPintura().observe(getViewLifecycleOwner(), new Observer<Pintura>() {
            @Override
            public void onChanged(Pintura pintura) {
                if (pintura != null) {
                    // Log para verificar los datos de la pintura
                    Log.d(TAG, "Pintura data received: " + pintura.toString());

                    // Mostrar los datos de la pintura
                    title.setText(pintura.getPaintingName());
                    description.setText(pintura.getDescription());

                    // Cargar imagen desde el path proporcionado
                    imageFilePath = pintura.getIconPath();
                    if (imageFilePath != null && !imageFilePath.isEmpty()) {
                        // Convertir el nombre del recurso drawable a un identificador de recurso
                        int resId = getResources().getIdentifier(imageFilePath, "drawable", getContext().getPackageName());
                        if (resId != 0) {
                            image.setImageResource(resId); // Usar el identificador del recurso
                        } else {
                            Log.d(TAG, "Drawable resource not found for path: " + imageFilePath);
                            image.setImageResource(R.drawable.placeholder); // Imagen por defecto si no se encuentra
                        }
                    } else {
                        image.setImageResource(R.drawable.placeholder); // Imagen por defecto si no hay path
                    }

                    // Configurar el archivo de audio
                    audioFileName = pintura.getAudioPath();
                } else {
                    // Mostrar mensaje de error si no se encuentra la pintura
                    Log.d(TAG, "No pintura data found for ID: " + pinturaId);
                    title.setText("No existe pintura");
                    description.setText("");
                    image.setImageResource(R.drawable.placeholder); // Mostrar imagen por defecto
                }
            }
        });

        // Configurar los botones de control de audio
        btnPlay.setOnClickListener(onClickListenerPlay());
        btnPause.setOnClickListener(onClickListenerPause());
        btnResume.setOnClickListener(onClickListenerResume());
        btnStop.setOnClickListener(onClickListenerStop());

        // Registrar BroadcastReceiver
        audioCommandReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String command = intent.getAction();
                handleAudioCommand(command);
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(AudioPlayService.PLAY);
        filter.addAction(AudioPlayService.PAUSE);
        filter.addAction(AudioPlayService.RESUME);
        filter.addAction(AudioPlayService.STOP);
        requireContext().registerReceiver(audioCommandReceiver, filter, Context.RECEIVER_NOT_EXPORTED);

        return view;
    }

    private View.OnClickListener onClickListenerPlay() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Play button clicked");
                startAudioService(AudioPlayService.PLAY);
            }
        };
    }

    private View.OnClickListener onClickListenerPause() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Pause button clicked");
                startAudioService(AudioPlayService.PAUSE);
            }
        };
    }

    private View.OnClickListener onClickListenerResume() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Resume button clicked");
                startAudioService(AudioPlayService.RESUME);
            }
        };
    }

    private View.OnClickListener onClickListenerStop() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Stop button clicked");
                startAudioService(AudioPlayService.STOP);
            }
        };
    }

    private void startAudioService(String command) {
        executorService.execute(() -> {
            Intent audioPlayServiceIntent = new Intent(getContext(), AudioPlayService.class);
            audioPlayServiceIntent.putExtra(AudioPlayService.FILENAME, audioFileName);
            audioPlayServiceIntent.putExtra(AudioPlayService.COMMAND, command);
            requireActivity().startService(audioPlayServiceIntent);
        });
    }

    private void handleAudioCommand(String command) {
        switch (command) {
            case AudioPlayService.PLAY:
                Log.d(TAG, "Handling PLAY command from notification");
                onClickListenerPlay().onClick(null);
                break;
            case AudioPlayService.PAUSE:
                Log.d(TAG, "Handling PAUSE command from notification");
                onClickListenerPause().onClick(null);
                break;
            case AudioPlayService.RESUME:
                Log.d(TAG, "Handling RESUME command from notification");
                onClickListenerResume().onClick(null);
                break;
            case AudioPlayService.STOP:
                Log.d(TAG, "Handling STOP command from notification");
                onClickListenerStop().onClick(null);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        executorService.shutdown(); // Asegurarse de que el ExecutorService se cierre correctamente
        requireContext().unregisterReceiver(audioCommandReceiver);
    }
}
