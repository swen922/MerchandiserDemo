package com.horovod.android.merchandiserdemo.view;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.horovod.android.merchandiserdemo.R;
import com.horovod.android.merchandiserdemo.data.Data;
import com.horovod.android.merchandiserdemo.showable.Showable;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.InputStream;
import java.util.List;

public class ListShowableAdapter extends ArrayAdapter<Showable> {

    private Context myContext;
    private int resourceID;

    public ListShowableAdapter(Context context, int resource, List<Showable> list) {
        super(context, resource, list);
        myContext = context;
        resourceID = resource;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Showable showable = getItem(position);

        ViewHolder viewHolder;
        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(resourceID, parent, false);
            viewHolder.header = convertView.findViewById(R.id.list_sho_header);
            viewHolder.comment = convertView.findViewById(R.id.list_sho_comment);
            viewHolder.imageView = convertView.findViewById(R.id.list_sho_image);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.header.setText(showable.getName());
        viewHolder.comment.setText(showable.getComment());

        if (!showable.getComment().isEmpty()) {
            ViewGroup.LayoutParams params = viewHolder.comment.getLayoutParams();
            params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
            viewHolder.comment.setLayoutParams(params);
            //commentBack.setBackgroundColor(getResources().getColor(R.color.gray_2));
        }

        if (!showable.getPreview().isEmpty()) {
            String image = showable.getPreview();

            File file1 = new File(myContext.getFilesDir() + File.separator + Data.photoFolder + File.separator + image);
            if (file1.exists()) {
                //InputStream inn = this.myContext.openFileInput(file1.toString());
                //viewHolder.imageView.setImageBitmap();

                ViewGroup.LayoutParams params = viewHolder.imageView.getLayoutParams();
                params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
                viewHolder.imageView.setLayoutParams(params);

                Picasso.get().load(file1).into(viewHolder.imageView);

            }

        }

        return convertView;
    }


    class ViewHolder {
        TextView header;
        TextView comment;
        ImageView imageView;
    }
}
