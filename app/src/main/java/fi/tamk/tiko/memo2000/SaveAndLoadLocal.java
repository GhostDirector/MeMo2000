package fi.tamk.tiko.memo2000;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by ghost on 29.3.2018.
 */

public class SaveAndLoadLocal {

    public SaveAndLoadLocal(){

    }

    public static boolean saveImg(ContentResolver contentResolver, Bitmap bitmap) {
        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/MeMo2000");

        if (!folder.exists()) {
            folder.mkdirs();
        }

        File file = new File(folder, UUID.randomUUID().toString()+".png");

        FileOutputStream outputStream;

        try {
            outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            System.out.println("Saved to !");
            String imgSaved = MediaStore.Images.Media.insertImage(contentResolver,file.getAbsolutePath(),file.getName(), "MeMo2000");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Something went wrong :C");
            return false;
        }
    }

    public static ArrayList<Note> load(){
        ArrayList<Note> notes = new ArrayList<>();
        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/MeMo2000");
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles != null){
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    notes.add(new Note(listOfFiles[i].getName(), listOfFiles[i].getAbsolutePath()));
                    System.out.println("File: " + listOfFiles[i].getName());
                    System.out.println("File's path: " + listOfFiles[i].getAbsolutePath());
                }
            }
        }
        return notes;
    }
}
