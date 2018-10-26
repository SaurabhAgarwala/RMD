package io.codefundo.rmd.dmapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.codefundo.io.rmd.dmapp.models.Contact;

public class EmergencyContactFragment extends Fragment {

    public EmergencyContactFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_emergency_contact, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.helpline_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        List<Contact> contactList = new ArrayList<>();
        contactList.add(new Contact("National Emergency Number","112"));
        contactList.add(new Contact("Police","100"));
        contactList.add(new Contact("Fire","101"));
        contactList.add(new Contact("Ambulance","102"));
        contactList.add(new Contact("Disaster Management Services","108"));
        contactList.add(new Contact("Women Helpline","1091"));
        contactList.add(new Contact("Air Ambulance","9540161344"));
        contactList.add(new Contact("Earthquake/Flood/Disaster","011-24363260"));
        contactList.add(new Contact("Disaster Distress Helpline","18009855990"));
        contactList.add(new Contact("Police","100"));
        contactList.add(new Contact("Police","100"));
        recyclerView.setAdapter(new RecyclerViewAdapter(contactList));
        return rootView;
    }

    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

        List<Contact> contactList;

        RecyclerViewAdapter(List<Contact> contactList) {
            this.contactList = contactList;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            public TextView name;
            public TextView number;
            public TextView icon;

            public ViewHolder(View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.helpline_name);
                number = itemView.findViewById(R.id.helpline_number);
                icon = itemView.findViewById(R.id.helpline_icon);
            }

        }

        @NonNull
        @Override
        public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.helpline_item, viewGroup, false);

            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int pos) {
            viewHolder.name.setText(contactList.get(pos).getName());
            viewHolder.number.setText(contactList.get(pos).getNumber());
            viewHolder.icon.setText(String.valueOf(contactList.get(pos).getName().charAt(0)));
        }

        @Override
        public int getItemCount() {
            return contactList.size();
        }
    }

}
