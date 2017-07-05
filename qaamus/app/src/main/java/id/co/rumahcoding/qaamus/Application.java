package id.co.rumahcoding.qaamus;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import id.co.rumahcoding.qaamus.models.Entry;
import io.realm.Realm;
import io.realm.RealmConfiguration;
/**
 * Created by agungaprian on 28/05/17.
 */

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //init realm
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration
                .Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(1)
                .build();

        Realm.setDefaultConfiguration(realmConfiguration);

        initData();
    }

    private void initData () {
        Realm realm = Realm.getDefaultInstance();

        //ambil jumlah baris di tabel entry
        long count = realm.where(Entry.class).count();


        Log.d("Application","Jumlah Data:" + count);
        //jika barisnya kosong maka import ke CSV
        if (count == 0) {
            try {
                InputStream inputStream = getAssets().open("Qaamus.csv");
                InputStreamReader reader = new InputStreamReader(inputStream);
                final BufferedReader buffer = new BufferedReader(reader);



                realm.executeTransaction(
                        new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        String line = "";
                        try {
                            long id = 0;
                            while ((line = buffer.readLine()) != null) {
                                String [] lines = line.split(",");

                                id++;
                                Entry entry = new Entry();
                                entry.setId(id);
                                entry.setIndonesia(lines[1]);
                                entry.setArab(lines[0]);

                                realm.insert(entry);
                            }
                        }catch (IOException e) {

                        }



                    }
                });

            }catch (IOException e) {

            }
        }

    }

}
