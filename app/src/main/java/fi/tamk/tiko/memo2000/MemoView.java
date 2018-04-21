package fi.tamk.tiko.memo2000;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Memo view.
 */
public class MemoView extends View {
    /**
     * The Paths.
     */
    private ArrayList<Path> paths;
    /**
     * The Undone paths.
     */
    private ArrayList<Path> undonePaths;
    /**
     * The Draw path.
     */
    private Path drawPath;
    /**
     * The Draw paint.
     */
    private Paint drawPaint;
    /**
     * The Canvas paint.
     */
    private Paint canvasPaint;
    /**
     * The Paint color.
     */
    private int paintColor;
    /**
     * The Size.
     */
    private int size = 4;
    /**
     * The Canvas bitmap.
     */
    private Bitmap canvasBitmap;
    /**
     * The Colors map.
     */
    private Map<Path, Integer> colorsMap;
    /**
     * The Size map.
     */
    private Map<Path, Integer> sizeMap;
    /**
     * The Start x.
     */
    private float startX;
    /**
     * The Start y.
     */
    private float startY;

    /**
     * Instantiates a new Memo view.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public MemoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    /**
     * Setup brush color and size and canvas.
     */
    public void setup(){
        setBackgroundColor(Color.WHITE);
        drawPath = new Path();
        drawPaint = new Paint();
        paths = new ArrayList<>();
        undonePaths = new ArrayList<>();
        colorsMap = new HashMap<>();
        sizeMap = new HashMap<>();

        drawPaint.setColor(paintColor);

        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(size);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    /**
     * On size change.
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
    }

    /**
     * On draw.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);

        for (Path p : paths) {
            drawPaint.setColor(colorsMap.get(p));
            drawPaint.setStrokeWidth(sizeMap.get(p));
            canvas.drawPath(p, drawPaint);
        }

        drawPaint.setColor(paintColor);
        drawPaint.setStrokeWidth(size);
        canvas.drawPath(drawPath, drawPaint);
    }

    /**
     * On touch event. Register touches as strokes that are saved for undo and redo.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.reset();
                drawPath.moveTo(touchX, touchY);
                startX = touchX;
                startY = touchY;
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(touchX - startX) >= 10 || Math.abs(touchY - startY) >= 10) {
                    drawPath.quadTo(startX, startY, (touchX + startX) / 2, (touchY + startY) / 2);
                    startX = touchX;
                    startY = touchY;
                }

                break;
            case MotionEvent.ACTION_UP:
                drawPath.lineTo(startX, startY);
                paths.add(drawPath);
                colorsMap.put(drawPath, paintColor);
                sizeMap.put(drawPath, size);
                drawPath = new Path();
                drawPath.reset();
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }

    /**
     * Set color of brush.
     *
     * @param newColor the new color
     */
    public void setColor(String newColor){
        invalidate();
        paintColor = Color.parseColor(newColor);
        drawPaint.setColor(paintColor);
    }

    /**
     * Set size of brush.
     *
     * @param size the size
     */
    public void setSize(int size){
        invalidate();
        this.size = size;
        drawPaint.setStrokeWidth(this.size);
    }

    /**
     * Set background if drawing on an old picture.
     *
     * @param path the path
     */
    public void setBackground(String path){
        if (path != null && !path.trim().equals("")){
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
            setBackground(bitmapDrawable);
        }
    }

    /**
     * Clear all.
     */
    public void clearAll() {
        canvasBitmap.eraseColor(Color.WHITE);

        if (drawPath != null) {
            paths.clear();
        }

        invalidate();
    }

    /**
     * Undo last stroke.
     */
    public void undo() {
        if (paths.size() > 0) {
            undonePaths.add(paths.remove(paths.size() - 1));
            invalidate();
        }
    }

    /**
     * Redo last undone.
     */
    public void redo() {
        if (undonePaths.size() > 0) {
            paths.add(undonePaths.remove(undonePaths.size() - 1));
            invalidate();
        }
    }

    /**
     * Save bitmap to local storage.
     *
     * @param resolver the resolver
     */
    public void save(ContentResolver resolver) {
        this.setDrawingCacheEnabled(true);
        Bitmap bitmap = this.getDrawingCache();

        boolean success = SaveAndLoadLocal.saveImg(resolver, bitmap);

        if (success) {
            Toast.makeText(getContext(),"Saved!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(),"Something went wrong :C", Toast.LENGTH_SHORT).show();
        }

        this.destroyDrawingCache();
    }

    /**
     * Gets uri for img share.
     *
     * @param resolver the resolver
     * @return the uri
     */
    public Uri getUri(ContentResolver resolver) {
        this.setDrawingCacheEnabled(true);
        String imgPath = MediaStore.Images.Media.insertImage(resolver, this.getDrawingCache(), "MeMo2000", null);
        this.destroyDrawingCache();
        return Uri.parse(imgPath);
    }
}