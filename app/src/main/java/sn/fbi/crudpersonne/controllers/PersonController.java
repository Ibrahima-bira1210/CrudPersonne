package sn.fbi.crudpersonne.controllers;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Retrofit;
import sn.fbi.crudpersonne.activities.MainActivity;
import sn.fbi.crudpersonne.config.RetrofitConfig;
import sn.fbi.crudpersonne.config.Server;
import sn.fbi.crudpersonne.models.Person;

public class PersonController {

    private static ArrayList<Person> personList = new ArrayList<>();
    private static Person person;

    public static ArrayList<Person> getPersonList() {
        return personList;
    }

    public static ArrayList<Person> getPerson() {
        ArrayList<Person> arrayList = new ArrayList<>();
        arrayList.add(person);
        return arrayList;
    }

    public static void getAllPersonsVolley(final Context mainContext, final MainActivity current, final Method methodAfterFinished) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Server.BASE_URL + "students", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                Person[] array = gson.fromJson(response,Person[].class);

                personList.clear();
                Collections.addAll(personList, array);
                try {
                    methodAfterFinished.invoke(current);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                    Toast.makeText(mainContext, "Unexpected error. (status: 0013)", Toast.LENGTH_SHORT).show();
                    System.exit(0013);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mainContext, "Error communicating with server. Try again later.", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(mainContext);
        requestQueue.add(stringRequest);
    }

    public static void getPerson(String email, final Context mainContext, final MainActivity current, final Method methodAfterFinished) {

        Call<Person> call = new RetrofitConfig().getService().getPerson("fibrahimabirane@ept.sn",email);

        call.enqueue(new Callback<Person>() {
            @Override
            public void onResponse(retrofit.Response<Person> response, Retrofit retrofit) {
                if(response.isSuccess()){
                    person = response.body();
                    try {
                        methodAfterFinished.invoke(current);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                        Toast.makeText(mainContext, "Unexpected error. (status: 0013)", Toast.LENGTH_SHORT).show();
                        System.exit(0013);
                    }
                }
                else{
                    Toast.makeText(mainContext, "Error communicating with server. Try again later.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(mainContext, "Error communicating with server. Try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void getAllPersonsRetrofit(final Context mainContext, final MainActivity current, final Method methodAfterFinished) {

        Call<List<Person>> call = new RetrofitConfig().getService().getAllPersons("fibrahimabirane@ept.sn");

        call.enqueue(new Callback<List<Person>>() {
            @Override
            public void onResponse(retrofit.Response<List<Person>> response, Retrofit retrofit) {
                if(response.isSuccess()){
                    personList.clear();
                    for(int i = 0; i < response.body().size(); i++) {
                        personList.add((Person) response.body().get(i));
                    }
                    try {
                        methodAfterFinished.invoke(current);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                        Toast.makeText(mainContext, "Unexpected error. (status: 0013)", Toast.LENGTH_SHORT).show();
                        System.exit(0013);
                    }
                }
                else{
                    Toast.makeText(mainContext, "Error communicating with server. Try again later.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(mainContext, "Error communicating with server. Try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void insertPerson(final Context mainContext, String firstName, String lastName, String email){

        Person person = null;

        try {
            person = new Person(firstName, lastName,email);
        }
        catch (Exception e) {
            Toast.makeText(mainContext, "Student data was entered incorrectly.", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<Person> call = new RetrofitConfig().getService().addPerson("fibrahimabirane@ept.sn",person);
        call.enqueue(new Callback<Person>() {
            @Override
            public void onResponse(retrofit.Response<Person> response, Retrofit retrofit) {
                if(response.isSuccess()){
                    Toast.makeText(mainContext, "Student was included successfully", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(mainContext, "Error inserting student.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(mainContext, "Error communicating with server.", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    public static void updatePerson(final Context mainContext, String firstName, String lastName, String email){

        Person person = null;

        try {
            person = new Person(firstName, lastName, email);
        }
        catch (Exception e) {
            Toast.makeText(mainContext, "Student data was entered incorrectly.", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<Person> call = new RetrofitConfig().getService().updatePerson("fibrahimabirane@ept.sn", email,person);
        call.enqueue(new Callback<Person>() {
            @Override
            public void onResponse(retrofit.Response<Person> response, Retrofit retrofit) {
                if(response.isSuccess()){
                    Toast.makeText(mainContext, "Student was updated successfully", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(mainContext, "Error updating student.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(mainContext, "Error communicating with server.", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    public static void deletePerson(final Context mainContext, String email) {

        Call<Person> call = new RetrofitConfig().getService().deletePerson("fibrahimabiranne@ept.sn",email);
        call.enqueue(new Callback<Person>(){
            @Override
            public void onResponse(retrofit.Response<Person> response, Retrofit retrofit) {
                if(response.isSuccess()){
                    Toast.makeText(mainContext, "Student was deleted successfully", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(mainContext, "Error deleting student.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(mainContext, "Error communicating with server.", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }




}
