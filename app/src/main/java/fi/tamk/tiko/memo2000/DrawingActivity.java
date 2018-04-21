package fi.tamk.tiko.memo2000;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

    /**
     * The Drawing view.
     */
    private MemoView drawingView;
    /**
     * The Size palette.
     */
    private LinearLayout sizePalette;
    /**
     * The Color palette.
     */
    private LinearLayout colorPalette;
    /**
     * The Size picker button.
     */
    private Button sizePickerButton;
    /**
     * The Color picker button.
     */
    private Button colorPickerButton;
    /**
     * The Size palette on boolean.
     */
    private boolean sizePaletteOn;
    /**
     * The Color palette on boolean.
     */
    private boolean colorPaletteOn;
    /**
     * The Current color.
     */
    private String currentColor;
    /**
     * The share action provider.
     */
    private ShareActionProvider mShareActionProvider;

    /**
     * On create.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeStatusBarColor();
        setContentView(R.layout.activity_drawing);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(getString(R.string.colorActionbar))));
        setup();
        initializeColorButtons();
        if (getIntent().getStringExtra("path") != null){
            drawingView.setBackground(getIntent().getStringExtra("path"));
        }
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
        getMenuInflater().inflate(R.menu.drawing_menu, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        return true;
    }

    /**
     * On options item selected.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save:
                new Handler().postDelayed(() -> drawingView.save(getContentResolver()), 100);
                break;

            case R.id.clear_all:
                drawingView.clearAll();
                break;

            case R.id.menu_item_share:
                System.out.println("toimiiko");
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_STREAM, drawingView.getUri(getContentResolver()));
                intent.setType("image/*");
                startActivity(Intent.createChooser(intent, "SHARE"));
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
     * Setup colors and palettes and sizes.
     */
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

    /**
     * On click listener for buttons.
     *
     * @param view the view
     */
    public void onClick(View view) {
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

    /**
     * Initialize color buttons.
     */
    public void initializeColorButtons(){
        for (int i = 0; i < colorPalette.getChildCount(); i++){
            Button button = (Button) colorPalette.getChildAt(i);
            GradientDrawable drawable = (GradientDrawable) button.getBackground();
            drawable.setColor(Color.parseColor(button.getTag().toString()));
        }
    }

    /**
     * On click color.
     *
     * @param view the view
     */
    public void onClickColor(View view){
        currentColor = ((Button) view).getTag().toString();
        setColorButtonColor(currentColor);
        drawingView.setColor(currentColor);
    }

    /**
     * Set color button color.
     *
     * @param color the color
     */
    public void setColorButtonColor(String color){
        GradientDrawable drawable = (GradientDrawable) colorPickerButton.getBackground();
        drawable.setColor(Color.parseColor(color));
    }

    /**
     * Set size button color.
     */
    public void setSizeButtonColor(){
        GradientDrawable drawable = (GradientDrawable) sizePickerButton.getBackground();
        drawable.setColor(Color.parseColor("#ffffff"));
    }

    /**
     * On click size.
     *
     * @param view the view
     */
    public void onClickSize(View view){
        Integer size = Integer.parseInt(((Button) view).getText().toString());

        sizePickerButton.setText(size.toString());
        drawingView.setSize(size);
    }
}
