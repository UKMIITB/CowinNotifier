package com.example.cowinnotifier.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.cowinnotifier.helper.Endpoint
import com.example.cowinnotifier.repository.APIRepository
import com.example.cowinnotifier.repository.retrofit.APIService
import com.example.cowinnotifier.repository.room.AppDatabase
import com.example.cowinnotifier.repository.room.Dao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, AppDatabase::class.java, "myDB")
            .addMigrations(MIGRATION_1_2)
            .build()

    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("create table if not exists `Search` (`id` INTEGER NOT NULL, `vaccineList` TEXT NOT NULL, `dose` TEXT NOT NULL, `minAge` INTEGER NOT NULL, `locationKey` TEXT NOT NULL, `locationValue` TEXT NOT NULL, PRIMARY KEY(`id`))")
        }
    }

    @Provides
    @Singleton
    fun provideDao(database: AppDatabase) = database.dao()

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

        return Retrofit.Builder().baseUrl(Endpoint.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideAPIService(retrofit: Retrofit) = retrofit.create(APIService::class.java)

    @Provides
    @Singleton
    fun provideRepository(apiService: APIService, dao: Dao) = APIRepository(apiService, dao)
}