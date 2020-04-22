package in.codefundo.io.rmd.dmapp.models;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Person {
    private String name;
    private String age;
    private String gender;
    private String status;
    private String image;
    private String admitter;
    private String location;

    public Person(@NonNull String name,
                  @Nullable String age,
                  @Nullable String gender,
                  @NonNull String status,
                  @Nullable String image,
                  @Nullable String admitter,
                  @Nullable String location) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.status = status;
        this.image = image;
        this.admitter = admitter;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAdmitter() {
        return admitter;
    }

    public void setAdmitter(String admitter) {
        this.admitter = admitter;
    }
}
