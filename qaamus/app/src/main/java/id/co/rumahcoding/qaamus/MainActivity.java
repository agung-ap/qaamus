package id.co.rumahcoding.qaamus;

import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.provider.BaseColumns;
import android.speech.RecognizerIntent;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;

import id.co.rumahcoding.qaamus.models.Entry;
import io.realm.Case;
import io.realm.ObjectChangeSet;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private SimpleCursorAdapter adapter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchView = (SearchView) findViewById(R.id.search_view);
        ImageButton voiceImageButton = (ImageButton) findViewById(R.id.search_voice_button);

        final String [] from = new String[] {"indonesia"};
        final int [] to = new int[] {android.R.id.text1};

        adapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_dropdown_item
        ,null,from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        searchView.setSuggestionsAdapter(adapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                MatrixCursor mc = new MatrixCursor(new String[] {BaseColumns._ID,"indonesia"});

                //ambil data dari realm
                Realm realm = Realm.getDefaultInstance();
                RealmResults<Entry> results = realm.where(Entry.class)
                        .contains("indonesia",newText, Case.INSENSITIVE)
                        .findAll();

                for (int i=0; i<results.size();i++) {
                    Entry entry = results.get(i);
                    Object[] row = new Object[] {entry.getId(),entry.getIndonesia()};
                    mc.addRow(row);
                }
                realm.close();

                adapter.changeCursor(mc);
                return true;
            }
        });

        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                Cursor cursor =  adapter.getCursor();
                cursor.moveToPosition(position);
                long id = cursor.getLong(0);
                //update textbox
                searchView.setQuery(cursor.getString(1),false);

                //tampilkan screen detail activity
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);

                return true;
            }
        });

        voiceImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tampilkan speech listener
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar_SA");

                startActivityForResult(intent,1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        searchView.setQuery(result.get(0),false);
    }
}
