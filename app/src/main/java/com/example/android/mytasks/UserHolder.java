package com.example.android.mytasks;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by SG on 7/25/2018.
 */

public class UserHolder extends RecyclerView.ViewHolder {


    TextView Name;
    TextView Email;
    CircleImageView Photo;
    ImageView Status;


    public UserHolder(View itemView) {
        super(itemView);
        Name = (TextView)itemView.findViewById(R.id.FriendName);
        Email = (TextView)itemView.findViewById(R.id.FriendEmail);
        Photo = (CircleImageView)itemView.findViewById(R.id.FriendImage);
        Status = (ImageView)itemView.findViewById(R.id.InviteFreinds_TaskStatus);

    }


}
