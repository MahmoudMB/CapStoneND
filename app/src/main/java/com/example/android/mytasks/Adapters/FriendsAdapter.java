package com.example.android.mytasks.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.mytasks.Models.User;
import com.example.android.mytasks.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by SG on 7/25/2018.
 */

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.MyviewHolder> {

    private final LayoutInflater inflator;
    private Context context;
    final private FriendsAdapter.ListItemClickListener mOnClickListener;
    List<User> data = Collections.emptyList();
    List<User> TempData = Collections.emptyList();
    int orderid;


    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }


    public FriendsAdapter(Context context,List<User> data,FriendsAdapter.ListItemClickListener listener) {
        inflator =  LayoutInflater.from(context);
        this.data=data;
        mOnClickListener = listener;
        this.context= context;
    }

    @Override
    public FriendsAdapter.MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflator.inflate(R.layout.friends_list_items,parent,false);
        FriendsAdapter.MyviewHolder holder = new FriendsAdapter.MyviewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final FriendsAdapter.MyviewHolder holder, int position) {

        final User currentUser = data.get(position);


        holder.Name.setText(currentUser.getName());
        holder.Email.setText(currentUser.getEmail());
        if (!TextUtils.isEmpty(currentUser.getPhotoUrl()))
            holder.Photo.setImageURI(Uri.parse(currentUser.getPhotoUrl()));

        holder.Status.setImageResource(R.drawable.ic_round_check_circle_outline_24px);
    }



    @Override
    public int getItemCount()
    {
        return data.size();
    }




    class MyviewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{


        TextView Name;
        TextView Email;
        CircleImageView Photo;
        ImageView Status;




        public MyviewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            Name = (TextView)itemView.findViewById(R.id.FriendName);
            Email = (TextView)itemView.findViewById(R.id.FriendEmail);
            Photo = (CircleImageView)itemView.findViewById(R.id.FriendImage);
            Status = (ImageView)itemView.findViewById(R.id.InviteFreinds_TaskStatus);
        }


        @Override
        public void onClick(View v) {

            int clickedPosition = getAdapterPosition();
            // int id =   data.get(clickedPosition).getID();
            mOnClickListener.onListItemClick(clickedPosition);

        }






    }


}

