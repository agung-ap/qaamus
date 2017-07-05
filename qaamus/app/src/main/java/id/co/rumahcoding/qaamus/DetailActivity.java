package id.co.rumahcoding.qaamus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import id.co.rumahcoding.qaamus.models.Entry;
import io.realm.Realm;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //ambil data yang dikirim dari mainactivity
        long id = getIntent().getLongExtra("id",0);

        //ambil data dari database
        Realm realm = Realm.getDefaultInstance();
        Entry entry = realm.where(Entry.class).equalTo("id", id).findFirst();
        realm.close();

        TextView arabTextView = (TextView) findViewById(R.id.text_arab);
        TextView indonesiaTextView = (TextView) findViewById(R.id.text_indonesia);

        arabTextView.setText(entry.getArab());
        indonesiaTextView.setText(entry.getIndonesia());

    }
}
