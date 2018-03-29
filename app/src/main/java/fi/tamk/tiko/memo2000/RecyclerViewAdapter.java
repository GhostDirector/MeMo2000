package fi.tamk.tiko.memo2000;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ghost on 29.3.2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Object[] notes;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView image;

        public ViewHolder(View item) {
            super(item);
            this.title = (TextView) itemView.findViewById(R.id.title);
            this.image = (ImageView) itemView.findViewById(R.id.note_pic);
        }
    }

    public RecyclerViewAdapter(List<Note> noteList) {
        this.notes = noteList.toArray();
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        View noteView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note, parent, false);

        return new ViewHolder(noteView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(((Note)notes[position]).getTitle());
        Bitmap bitmap = BitmapFactory.decodeFile(((Note)notes[position]).getPicPath());
        holder.image.setImageBitmap(bitmap);
        holder.image.setTag(((Note)notes[position]).getPicPath());
    }

    @Override
    public int getItemCount() {
        return notes.length;
    }
}