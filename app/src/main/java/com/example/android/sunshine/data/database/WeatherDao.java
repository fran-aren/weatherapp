package com.example.android.sunshine.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.Date;
import java.util.List;

@Dao
public interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(WeatherEntry... weather);

    @Query("SELECT * FROM weather WHERE date = :date")
    LiveData<WeatherEntry> getWeatherByDate(Date date);

    @Query("SELECT COUNT(id) FROM weather WHERE date >= :date")
    int countAllFutureWeather(Date date);

    /**
     * Deletes any weather data older than the given day
     *
     * @param date The date to delete all prior weather from (exclusive)
     */
    @Query("DELETE FROM weather WHERE date < :date")
    void deleteOldWeather(Date date);

    /**
     * Selects all {@link ListWeatherEntry} entries after a give date, inclusive. The LiveData will
     * be kept in sync with the database, so that it will automatically notify observers when the
     * values in the table change.
     *
     * @param date A {@link Date} from which to select all future weather
     * @return {@link LiveData} list of all {@link ListWeatherEntry} objects after date
     */
    @Query("SELECT id, weatherIconId, date, min, max FROM weather WHERE date >= :date")
    LiveData<List<ListWeatherEntry>> getCurrentWeatherForecasts(Date date);
}
