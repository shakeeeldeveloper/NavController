package com.example.navcontroller.di

import android.content.Context
import com.example.navcontroller.repositories.TranslationRepository
import com.example.navcontroller.viewmodels.HistoryManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTranslationRepository(
        @ApplicationContext context: Context,

    ): TranslationRepository = TranslationRepository(context)

    @Singleton
    @Provides
    fun provideHistoryManager(@ApplicationContext context: Context): HistoryManager {
        return HistoryManager(context)
    }

/*    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "translation_history_db"
        ).build()
    }

    @Provides
    fun provideHistoryDao(database: AppDatabase): HistoryDao {
        return database.historyDao()
    }*/
}

