package fi.tamk.tiko.memo2000;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MemoView extends View {
    private ArrayList<Path> paths;
    private ArrayList<Path> undonePaths;
    //drawing path
    private Path drawPath;
    //drawing and canvas paint
    private Paint drawPaint;
    private Paint canvasPaint;
    //initial color
    private int paintColor;
    //size
    private int size = 4;
    //canvas
    private Canvas drawCanvas;
    //canvas bitmap
    private Bitmap canvasBitmap;
    private Map<Path, Integer> colorsMap;
    private Map<Path, Integer> sizeMap;
    private float startX;
    private float startY;
    private boolean eraseMode = false;

    public MemoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

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

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//view given size
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

//    @Override
//    protected void onDraw(Canvas canvas) {
//        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
//        canvas.drawPath(drawPath, drawPaint);
//    }
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//detect user touch
        float touchX = event.getX();
        float touchY = event.getY();

//        if (eraseMode) {
//            drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
//        } else {
//            drawPaint.setXfermode(null);
//            drawPaint.setAlpha(0xFF);
//        }

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

    public void setColor(String newColor){
//set color
        invalidate();
        paintColor = Color.parseColor(newColor);
        drawPaint.setColor(paintColor);
    }

    public void setsize(int size){
//set color
        invalidate();
        this.size = size;
        drawPaint.setStrokeWidth(this.size);
    }

    public void setBackground(String path){
        if (path != null){
            if (path.trim() != ""){
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
                setBackground(bitmapDrawable);
            }
        }
    }

    public void clearAll() {
        canvasBitmap.eraseColor(Color.WHITE);

        if (drawPath != null) {
            paths.clear();
        }

        invalidate();
    }

    public void undo() {
        if (paths.size() > 0) {
            undonePaths.add(paths.remove(paths.size() - 1));
            invalidate();
        }
    }

    public void redo() {
        if (undonePaths.size() > 0) {
            paths.add(undonePaths.remove(undonePaths.size() - 1));
            invalidate();
        }
    }

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

    public void setEraser(boolean eraseMode) {
        this.eraseMode = eraseMode;
    }
}