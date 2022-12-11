package com.example.ekz.main.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ekz.R;
import com.example.ekz.adapter.RecyclerCustomAdapter;
import com.example.ekz.databinding.FragmentNotificationsBinding;
import com.example.ekz.models.FeelingsResponse;
import com.example.ekz.models.LoginResponse;
import com.example.ekz.network.NetworkServices;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;


    private View root;
    private LoginResponse loginResponse;

    private RecyclerView recyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        if (getArguments() != null) {
            loginResponse = (LoginResponse) getArguments().getSerializable("tokenModel");
        }

        findViews();
        return root;
    }

    private void findViews() {
        recyclerView = root.findViewById(R.id.feelings_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        ((TextView) root.findViewById(R.id.menu_user_name)).setText(String.format("С возвращением, %s", loginResponse.getNickName()));
        Glide.with(requireContext()).load(loginResponse.getAvatar()).into((ImageView) root.findViewById(R.id.menu_user_image));
        getFeelings();
    }

    private void getFeelings() {
        NetworkServices.getInstance().getJSONApi().feelings().enqueue(new Callback<FeelingsResponse>() {
            @Override
            public void onResponse(Call<FeelingsResponse> call, Response<FeelingsResponse> response) {
                if (response.code() / 100 == 2)
                    recyclerView.setAdapter(new RecyclerCustomAdapter(getContext(), response.body().getData()));
                else Toast.makeText(getContext(), response.code() + " ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<FeelingsResponse> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}