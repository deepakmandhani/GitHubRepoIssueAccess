package sigtuple.com.sigtuple.di.modules;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sigtuple.com.sigtuple.network.GithubApiInterface;

@Singleton
@Module
public class NetworkModule {

    @Provides
    @Singleton
    @Named("baseUrl")
    String provideBaseUrl(){
        return "https://api.github.com/";
    }

    @Singleton
    @Provides
    Retrofit provideRetrofit(@Named("baseUrl") String baseUrl) {
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(getOkHttpLoggingIntercepter())
                .baseUrl(baseUrl)
                .build();
    }

    @Singleton
    @Provides
    GithubApiInterface provideGithubApiInterface(Retrofit retrofit) {
        return retrofit.create(GithubApiInterface.class);
    }


    public OkHttpClient getOkHttpLoggingIntercepter() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
        return client;
    }
}
