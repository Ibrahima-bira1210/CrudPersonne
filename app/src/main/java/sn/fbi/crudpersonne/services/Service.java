package sn.fbi.crudpersonne.services;


import java.util.List;

import retrofit.Call;
import retrofit.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import sn.fbi.crudpersonne.models.Person;

public interface Service {

    @GET("/api/{key}/Persons")
    public Call<List<Person>> getAllPersons(@Path("key") String myKey);

    @GET("/api/{key}/Persons/{email}")
    public Call<Person> getPerson(@Path("key") String myKey, @Path("email") String email);

    @PUT("/api/{key}/Persons")
    public Call<Person> addPerson(@Path("key") String myKey, @Body Person newPerson);

    @PUT("/api/{key}/Persons/{email}")
    public Call<Person> updatePerson(@Path("key") String myKey, @Path("email") String email, @Body Person updatedPerson);

    @DELETE("/api/{key}/Persons/{email}")
    public Call<Person> deletePerson(@Path("key") String myKey, @Path("email") String email);
}
