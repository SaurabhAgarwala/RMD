package io.codefundo.rmd.dmapp;

import android.os.Bundle;
import android.util.Log;
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
import de.hdodenhof.circleimageview.CircleImageView;
import in.codefundo.io.rmd.dmapp.models.Person;

import com.android.volley.Response;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.squareup.picasso.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CasualtyFragment extends Fragment {

    private static final String TAG = CasualtyFragment.class.getName();
    public static String BASE_URL = "http://192.168.43.226:8000";
    RecyclerView recyclerView;

    public CasualtyFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_casualty, container, false);
        recyclerView = rootView.findViewById(R.id.casualty_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        StringRequest stringRequest = new StringRequest(BASE_URL + "/managing/applist/",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG,response);
                        List<Person> casualtyList = new ArrayList<>();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Person person = new Person(
                                        jsonObject.getString("name"),
                                        jsonObject.getString("age"),
                                        jsonObject.getString("gender"),
                                        jsonObject.getString("status"),
                                        BASE_URL + jsonObject.getString("image"),
                                        jsonObject.getString("admitter"),
                                        jsonObject.getString("location")
                                );
                                casualtyList.add(person);
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "Bad json : " + e.getMessage());
                        }
                        recyclerView.setAdapter(new RecyclerViewAdapter(casualtyList));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "volley error : " + error.getMessage());
                    }
                });
        VolleyHelper.getInstance(getContext()).addToRequestQueue(stringRequest);
        return rootView;
    }

    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

        List<Person> casualtyList;

        RecyclerViewAdapter(List<Person> casualtyList) {
            this.casualtyList = casualtyList;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            public TextView name;
            public TextView age;
            public TextView gender;
            public TextView status;
            public TextView admitter;
            public TextView location;
            public CircleImageView imageView;

            public ViewHolder(View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.person_tv_name);
                age = itemView.findViewById(R.id.person_tv_age);
                gender = itemView.findViewById(R.id.person_tv_gender);
                status = itemView.findViewById(R.id.person_tv_status);
                admitter = itemView.findViewById(R.id.person_tv_admitter);
                location = itemView.findViewById(R.id.person_tv_location);
                imageView = itemView.findViewById(R.id.person_civ_profile_image);
            }

        }

        @NonNull
        @Override
        public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.person_item, viewGroup, false);

            return new RecyclerViewAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder viewHolder, int pos) {
            viewHolder.name.setText(casualtyList.get(pos).getName());
            viewHolder.age.setText(casualtyList.get(pos).getAge());
            viewHolder.gender.setText(casualtyList.get(pos).getGender());
            viewHolder.status.setText(casualtyList.get(pos).getStatus());
            viewHolder.admitter.setText(casualtyList.get(pos).getAdmitter());
            viewHolder.location.setText(casualtyList.get(pos).getLocation());
            Picasso.get()
                    .load(casualtyList.get(pos).getImage())
                    .error(R.drawable.ic_launcher_foreground)
                    .into(viewHolder.imageView);
        }

        @Override
        public int getItemCount() {
            return casualtyList.size();
        }
    }
}
