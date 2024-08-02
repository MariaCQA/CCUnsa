package com.example.ccunsa.view.fragments;

import android.content.Intent;
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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.ccunsa.R;
import com.example.ccunsa.model.Pintura;
import com.example.ccunsa.service.AudioPlayService;
import com.example.ccunsa.viewmodel.PinturaViewModel;

public class PinturaFragment extends Fragment {
    private static final String TAG = "PinturaFragment";

    private TextView title, description;
    private ImageView image;
    private PinturaViewModel pinturaViewModel;
    private int pinturaId;
    private String audioFileName;

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

                    title.setText(pintura.getPaintingName());
                    description.setText(pintura.getDescription());
                    Glide.with(getContext())
                            .load(getResources().getIdentifier(pintura.getIconPath(), "drawable", getContext().getPackageName()))
                            .placeholder(R.drawable.placeholder)
                            .into(image);
                    audioFileName = pintura.getAudioPath();
                } else {
                    Log.d(TAG, "No pintura data found for ID: " + pinturaId);
                }
            }
        });

        // Configurar los botones de control de audio
        btnPlay.setOnClickListener(onClickListenerPlay());
        btnPause.setOnClickListener(onClickListenerPause());
        btnResume.setOnClickListener(onClickListenerResume());
        btnStop.setOnClickListener(onClickListenerStop());

        return view;
    }

    private View.OnClickListener onClickListenerPlay() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAudioService(AudioPlayService.PLAY);
            }
        };
    }

    private View.OnClickListener onClickListenerPause() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAudioService(AudioPlayService.PAUSE);
            }
        };
    }

    private View.OnClickListener onClickListenerResume() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAudioService(AudioPlayService.RESUME);
            }
        };
    }

    private View.OnClickListener onClickListenerStop() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAudioService(AudioPlayService.STOP);
            }
        };
    }

    private void startAudioService(String command) {
        Intent audioPlayServiceIntent = new Intent(getContext(), AudioPlayService.class);
        audioPlayServiceIntent.putExtra(AudioPlayService.FILENAME, audioFileName);
        audioPlayServiceIntent.putExtra(AudioPlayService.COMMAND, command);
        requireActivity().startService(audioPlayServiceIntent);
    }
}
