package com.example.ccunsa.view.fragments.Detail;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.ccunsa.R;

public class RoomDetailFragment extends Fragment {

    private static final String ARG_ROOM_NAME = "room_name";
    private String roomName;

    public RoomDetailFragment() {
        // Required empty public constructor
    }

    public static RoomDetailFragment newInstance(String roomName) {
        RoomDetailFragment fragment = new RoomDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ROOM_NAME, roomName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            roomName = getArguments().getString(ARG_ROOM_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_room_detail, container, false);
        TextView roomNameTextView = view.findViewById(R.id.roomNameTextView);
        roomNameTextView.setText(roomName);
        return view;
    }
}
