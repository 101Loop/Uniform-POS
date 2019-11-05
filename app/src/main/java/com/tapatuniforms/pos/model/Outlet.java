package com.tapatuniforms.pos.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.tapatuniforms.pos.helper.APIStatic;

import org.json.JSONObject;

import static androidx.room.ForeignKey.CASCADE;

@Entity(indices = @Index(value = "schoolId"), foreignKeys = @ForeignKey(entity = School.class,
        parentColumns = "id",
        childColumns = "schoolId",
        onDelete = CASCADE))
public class Outlet {
    @PrimaryKey
    private int id;
    private int schoolId;
    private String shortName;

    public Outlet(int id, int schoolId, String shortName) {
        this.id = id;
        this.schoolId = schoolId;
        this.shortName = shortName;
    }

    @Ignore
    public Outlet(JSONObject object) {
        this.id = object.optInt(APIStatic.Key.id);
        this.shortName = object.optString(APIStatic.Key.shortName);

        JSONObject schoolJson = object.optJSONObject(APIStatic.Key.school);
        if (schoolJson != null)
            this.schoolId = schoolJson.optInt(APIStatic.Key.id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}
