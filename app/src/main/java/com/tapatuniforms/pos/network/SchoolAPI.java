package com.tapatuniforms.pos.network;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.civilmachines.drfapi.DjangoJSONArrayResponseRequest;
import com.civilmachines.drfapi.DjangoJSONObjectRequest;
import com.tapatuniforms.pos.helper.APIErrorListener;
import com.tapatuniforms.pos.helper.APIStatic;
import com.tapatuniforms.pos.helper.DatabaseSingleton;
import com.tapatuniforms.pos.helper.Validator;
import com.tapatuniforms.pos.helper.VolleySingleton;
import com.tapatuniforms.pos.model.Student;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SchoolAPI {

    private static final String TAG = "Billing";
    private static SchoolAPI instance;
    private Context context;

    private SchoolAPI(Context context) {
        this.context = context;
    }

    public static synchronized SchoolAPI getInstance(Context context) {
        if (instance == null) {
            instance = new SchoolAPI(context);
        }
        return instance;
    }

    /**
     * Method to make an API call to add student details in server
     */
    public void addStudentDetails(String studentId, String name, int school, String email, String mobile,
                                  String standard, String section, String gender, String fatherName,
                                  AlertDialog dialog) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(APIStatic.Key.studentId, studentId);
        jsonObject.put(APIStatic.Key.name, name);
        jsonObject.put(APIStatic.Key.school, school);
        jsonObject.put(APIStatic.Key.email, email);
        jsonObject.put(APIStatic.Key.mobile, mobile);
        jsonObject.put(APIStatic.Key.standard, standard);
        jsonObject.put(APIStatic.Key.section, section);
        jsonObject.put(APIStatic.Key.gender, gender);
        jsonObject.put(APIStatic.Key.fatherName, fatherName);

        DjangoJSONObjectRequest request = new DjangoJSONObjectRequest(
                Request.Method.POST,
                APIStatic.School.studentUrl,
                jsonObject,
                response -> {
                    Toast.makeText(context, "Details added successfully!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                },
                new APIErrorListener(context) {
                    @Override
                    public void onBadRequestError(JSONObject response) {
                        super.onBadRequestError(response);
                        Log.e(TAG, "error:" + response);
                    }
                }, context);

        request.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(context).getRequestQueue().add(request);
    }

    /**
     * Method to get student details by id
     *
     * @param id             unique id of the student
     * @param studentDetails array of Student of size 1,
     */
    public void getStudentDetails(int id, Student[] studentDetails, DatabaseSingleton db) throws JSONException {
        if (!Validator.isNetworkConnected(context)) {
            studentDetails[0] = db.studentDao().getStudent(id);
            Toast.makeText(context, "Order will be placed for: " + studentDetails[0].getName(), Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(APIStatic.Key.id, id);

        DjangoJSONObjectRequest request = new DjangoJSONObjectRequest(
                Request.Method.GET,
                APIStatic.School.studentUrl + id + "/",
                jsonObject,
                response -> {
                    studentDetails[0] = new Student(response);
                    Toast.makeText(context, "Order will be placed for: " + response.optString(APIStatic.Key.name), Toast.LENGTH_SHORT).show();
                },
                new APIErrorListener(context),
                context
        );

        request.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(context).getRequestQueue().add(request);
    }

    /**
     * Method to get student details by id
     */
    public void getStudents(ArrayList<Student> studentList, DatabaseSingleton db) {
        List<Student> localStudentList = db.studentDao().getAll();


        if (!Validator.isNetworkConnected(context)) {
            studentList.clear();
            studentList.addAll(localStudentList);
            return;
        }

        DjangoJSONArrayResponseRequest request = new DjangoJSONArrayResponseRequest(
                Request.Method.GET,
                APIStatic.School.studentUrl,
                null,
                response -> {
                    db.studentDao().deleteAll();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject studentJSON = response.getJSONObject(i);
                            studentList.add(new Student(studentJSON));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    db.studentDao().deleteAll();
                    db.studentDao().insertAll(studentList);
                },
                new APIErrorListener(context), context);

        request.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(context).getRequestQueue().add(request);
    }
}
