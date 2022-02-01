package sn.fbi.crudpersonne.config;

import com.squareup.okhttp.OkHttpClient;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import sn.fbi.crudpersonne.services.Service;

public class RetrofitConfig {

    private final Retrofit retrofit;

    public  RetrofitConfig(){

        OkHttpClient okHttpClient = new OkHttpClient();
        this.retrofit = new Retrofit.Builder()
                .baseUrl(Server.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    public Service getService(){
        return this.retrofit.create(Service.class);
    }

}