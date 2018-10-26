package in.codefundo.io.rmd.dmapp.models;


import java.util.List;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Manual {
    private String name;
    @DrawableRes
    private int image;
    private List<String> tags;

    public Manual(@NonNull String name, @DrawableRes int image, @Nullable List<String> tags){
        this.name = name;
        this.image = image;
        this.tags = tags;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
