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
 * Recycler View Adapter.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    /**
     * The Notes.
     */
    private Object[] notes;

    /**
     * The type View holder.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * The Title.
         */
        private TextView title;
        /**
         * The Image.
         */
        private ImageView image;

        /**
         * Instantiates a new View holder.
         *
         * @param item the item
         */
        public ViewHolder(View item) {
            super(item);
            this.title = (TextView) itemView.findViewById(R.id.title);
            this.image = (ImageView) itemView.findViewById(R.id.note_pic);
        }
    }

    /**
     * Instantiates a new Recycler view adapter.
     *
     * @param noteList the note list
     */
    public RecyclerViewAdapter(List<Note> noteList) {
        this.notes = noteList.toArray();
    }

    /**
     * On create view holder.
     */
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        View noteView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note, parent, false);

        return new ViewHolder(noteView);
    }

    /**
     * On bind view holder.
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(((Note)notes[position]).getTitle());
        Bitmap bitmap = BitmapFactory.decodeFile(((Note)notes[position]).getPicPath());
        holder.image.setImageBitmap(bitmap);
        holder.image.setTag(((Note)notes[position]).getPicPath());
    }

    /**
     * Get item count of notes.
     */
    @Override
    public int getItemCount() {
        return notes.length;
    }
}