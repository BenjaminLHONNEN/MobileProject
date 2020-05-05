package com.ynov.mobileproject.models.todolist;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

// [START post_class]
@IgnoreExtraProperties
public class ToDoTask implements Serializable {
    public String uid;
    public Date date;
    public String title;
    public String category;

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("date", date);
        result.put("title", title);
        result.put("category", category);

        return result;
    }
}
