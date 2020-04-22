package in.codefundo.io.rmd.dmapp.models;



import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Contact {
    private String name;
    private String number;
    private List<String> tags;

    public Contact(@NonNull String name , @NonNull String number, @Nullable List<String> tags){
        this.name = name;
        this.number = number;
        this.tags = tags;
    }
    public Contact(@NonNull String name , @NonNull String number){
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
