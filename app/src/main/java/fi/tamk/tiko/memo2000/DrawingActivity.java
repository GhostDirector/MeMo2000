package fi.tamk.tiko.memo2000;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * DrawingActivity
 *
 * @author Jyri Virtaranta jyri.virtaranta@cs.tamk.fi
 * @version 29.3.2018
 * @since 1.8
 */
public class DrawingActivity extends AppCompatActivity {

    private MemoView drawingView;

    private LinearLayout sizePalette;
    private LinearLayout colorPalette;

    private Button sizePickerButton;
    private Button colorPickerButton;

    private boolean sizePaletteOn;
    private boolean colorPaletteOn;

    private boolean isEraserOn;

    private String currentColor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing);
        setup();
        if (getIntent().getStringExtra("path") != null){
            drawingView.setBackground(getIntent().getStringExtra("path"));
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.drawing_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        drawingView.setEraser(false);
        switch (item.getItemId()){
            case R.id.save:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        drawingView.save(getContentResolver());
                    }
                }, 100);
                break;

            case R.id.clear_all:
                drawingView.clearAll();
                break;

            case R.id.drawing_settings:
                break;
        }
        return false;
    }


    public void setup(){

        currentColor = "#000000";

        drawingView = findViewById(R.id.drawing_area);

        colorPickerButton = findViewById(R.id.color_picker);

        colorPaletteOn = false;
        colorPalette = findViewById(R.id.color_palette);
        colorPalette.setVisibility(View.GONE);

        setColorButtonColor(currentColor);
        drawingView.setColor(currentColor);

        sizePickerButton = findViewById(R.id.size_picker);
        setSizeButtonColor();

        sizePaletteOn = false;
        sizePalette = findViewById(R.id.size_palette);
        sizePalette.setVisibility(View.GONE);

        sizePickerButton.setText("4");
    }

    public void onClick(View view) {
        drawingView.setEraser(false);
        switch (view.getId()) {
            case R.id.undo:
                drawingView.undo();
                break;

            case R.id.redo:
                drawingView.redo();
                break;

            case R.id.color_picker:
                if (colorPaletteOn){
                    colorPalette.setVisibility(View.GONE);
                    colorPaletteOn = false;
                } else if (!colorPaletteOn){
                    if (sizePaletteOn){
                        sizePalette.setVisibility(View.GONE);
                        sizePaletteOn = false;
                    }
                    colorPalette.setVisibility(View.VISIBLE);
                    colorPaletteOn = true;
                }
                break;

            case R.id.size_picker:
                if (sizePaletteOn){
                    sizePalette.setVisibility(View.GONE);
                    sizePaletteOn = false;
                } else if (!sizePaletteOn){
                    if (colorPaletteOn){
                        colorPalette.setVisibility(View.GONE);
                        colorPaletteOn = false;
                    }
                    sizePalette.setVisibility(View.VISIBLE);
                    sizePaletteOn = true;
                }
                break;
        }
    }

    public void onClickColor(View view){
        drawingView.setEraser(false);
        currentColor = ((Button) view).getTag().toString();
        setColorButtonColor(currentColor);
        drawingView.setColor(currentColor);
    }

    public void setColorButtonColor(String color){
        GradientDrawable drawable = (GradientDrawable) colorPickerButton.getBackground();
        drawable.setColor(Color.parseColor(color));
    }

    public void setSizeButtonColor(){
        GradientDrawable drawable = (GradientDrawable) sizePickerButton.getBackground();
        drawable.setColor(Color.parseColor("#ffffff"));
    }


    public void onClickSize(View view){
        drawingView.setEraser(false);
        Integer size = Integer.parseInt(((Button) view).getText().toString());

        sizePickerButton.setText(size.toString());
        drawingView.setsize(size);
    }
}
