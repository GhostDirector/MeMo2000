package fi.tamk.tiko.memo2000;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


/**
 * The type Main activity.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * The Recycler view.
     */
    private RecyclerView recyclerView;
    /**
     * The Adapter.
     */
    private RecyclerView.Adapter adapter;
    /**
     * The Layout manager.
     */
    private RecyclerView.LayoutManager layoutManager;

    /**
     * On create.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeStatusBarColor();
        setContentView(R.layout.activity_main);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(getString(R.string.colorActionbar))));
        permissions();
    }

    /**
     * Populate list of notes.
     */
    public void populateList() {
        recyclerView = findViewById(R.id.notes);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerViewAdapter(SaveAndLoadLocal.load());
        recyclerView.setAdapter(adapter);
    }

    /**
     * On prepare options menu.
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * On create options menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * On options item selected.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.new_note:
                break;
        }
        return false;
    }

    /**
     * Change status bar color.
     */
    public void changeStatusBarColor(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorStatusBar));
        }
    }

    /**
     * Start drawing activity.
     *
     * @param view the view
     */
    public void startDrawing(View view) {
        String path = view.getTag().toString();

        if (path != null && !path.equals("new") && !path.trim().equals("")){
            Intent i = new Intent(this, DrawingActivity.class);
            i.putExtra("path", path);
            System.out.println(path);
            startActivity(i);
        } else {
            startActivity(new Intent(this, DrawingActivity.class));
        }
    }

    /**
     * Permissions checker.
     */
    public void permissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        } else {
            populateList();
        }
    }

    /**
     * Permission result handler.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    populateList();
                } else {
                    permissions();
                }
            }
        }
    }
}
