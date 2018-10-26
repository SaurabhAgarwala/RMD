package io.codefundo.rmd.dmapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.codefundo.io.rmd.dmapp.models.Contact;

public class HelpManualFragment extends Fragment {

    public HelpManualFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_help_manual, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.help_manual_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new RecyclerViewAdapter());
        return rootView;
    }

    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

        int images[] = {
                R.drawable.help_flood,
                R.drawable.help_lightning,
                R.drawable.help_tornado,
                R.drawable.help_cpr,
                R.drawable.help_ice,
                R.drawable.help_car,
                R.drawable.help_cool};

        RecyclerViewAdapter() {
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView imageView;

            public ViewHolder(View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.help_manual_iv);
            }

        }

        @NonNull
        @Override
        public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.help_manual_item, viewGroup, false);

            return new RecyclerViewAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder viewHolder, int pos) {
            Picasso.get()
                    .load(images[pos])
                    .into(viewHolder.imageView);
        }

        @Override
        public int getItemCount() {
            return images.length;
        }
    }
}
