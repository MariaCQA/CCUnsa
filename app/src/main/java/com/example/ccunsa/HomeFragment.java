package com.example.ccunsa;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.ccunsa.AdapterTabs;
import com.example.ccunsa.ListFragment;
import com.example.ccunsa.MapFragment;
import com.example.ccunsa.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflar el layout del fragmento
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializar los componentes de UI
        tabLayout = view.findViewById(R.id.tabs);
        viewPager = view.findViewById(R.id.viewpager);

        // Usar getActivity() en lugar de getChildFragmentManager() y getLifecycle()
        AdapterTabs adapterTabs = new AdapterTabs(requireActivity());
        adapterTabs.addFragment(new ListFragment(), "Lista");
        adapterTabs.addFragment(new MapFragment(), "Mapa");
        viewPager.setAdapter(adapterTabs);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(adapterTabs.getPageTitle(position))
        ).attach();
    }
}
