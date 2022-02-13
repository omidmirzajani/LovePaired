package dimo.applycaran.View.Adapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import dimo.applycaran.Models.Game;
import dimo.applycaran.R;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.MyViewHolder> {
    public ArrayList<Game> mGameList;
    private Activity mActivity;
    public View mView;

    public GameAdapter(ArrayList<Game> gl,Activity a) {
        mGameList = gl;
        mActivity = a;
    }

    @NonNull
    @Override
    public GameAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from( viewGroup.getContext() )
                .inflate( R.layout.user_item, viewGroup, false );
        return new MyViewHolder( view );
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageView;
        TextView textView;
        public MyViewHolder(View view) {
            super( view );
            mView=view;
            imageView=view.findViewById( R.id.ImageIcon );
            textView=view.findViewById( R.id.TextNameUser );

        }
    }

    @Override
    public void onBindViewHolder(@NonNull GameAdapter.MyViewHolder holder, int position) {
        Glide.with(mActivity)
                .load(mGameList.get(position).GameName)
                .into(holder.imageView);
        holder.textView.setText(mGameList.get(position).GameName);
    }

    @Override
    public int getItemCount() {
        return mGameList.size();
    }

}
