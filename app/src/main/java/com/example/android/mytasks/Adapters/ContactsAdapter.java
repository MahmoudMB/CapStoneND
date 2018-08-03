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

import com.example.android.mytasks.Models.ToDo;
import com.example.android.mytasks.Models.User;
import com.example.android.mytasks.R;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by SG on 7/26/2018.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyviewHolder> {
    private final LayoutInflater inflator;
    private Context context;
    final private ContactsAdapter.ListItemClickListener mOnClickListener;
    List<User> data = Collections.emptyList();
List<User> TempData = Collections.emptyList();
    int orderid;


    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }


    public ContactsAdapter(Context context,List<User> data,ListItemClickListener  listener) {
        inflator =  LayoutInflater.from(context);
        this.data=data;
        mOnClickListener = listener;
        this.context= context;

        TempData = new ArrayList<>();
        TempData.addAll(data);
    }

    @Override
    public ContactsAdapter.MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflator.inflate(R.layout.contacts_items,parent,false);
        ContactsAdapter.MyviewHolder holder = new ContactsAdapter.MyviewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ContactsAdapter.MyviewHolder holder, int position) {

        final User currentUser = data.get(position);

      holder.contactEmail.setText(currentUser.getEmail());
      holder.contactName.setText(currentUser.getName());
      if(!TextUtils.isEmpty(currentUser.getPhotoUrl()))
     holder.ContactImage.setImageURI(Uri.parse(currentUser.getPhotoUrl()));
    }



    @Override
    public int getItemCount()
    {
        return data.size();
    }




    class MyviewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

        TextView contactName;
        TextView contactEmail;
        CircleImageView ContactImage;
        ImageView Status;




        public MyviewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            contactName = (TextView)itemView.findViewById(R.id.contactName);
            contactEmail = (TextView)itemView.findViewById(R.id.contactEmail);
            ContactImage = (CircleImageView)itemView.findViewById(R.id.ContactImage);
            Status = (ImageView)itemView.findViewById(R.id.InviteFreinds_TaskStatus);
        }


        @Override
        public void onClick(View v) {

                int clickedPosition = getAdapterPosition();
               // int id =   data.get(clickedPosition).getID();
                mOnClickListener.onListItemClick(clickedPosition);

        }






    }
    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        Log.v("charText",charText);
        data.clear();
        if (charText.length() == 0) {
            data.addAll(TempData);
        } else {
            for (User wp : TempData) {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)||wp.getEmail().toLowerCase(Locale.getDefault()).contains(charText)) {
                    data.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}

