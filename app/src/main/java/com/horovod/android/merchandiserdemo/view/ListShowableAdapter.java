package com.horovod.android.merchandiserdemo.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.horovod.android.merchandiserdemo.R;
import com.horovod.android.merchandiserdemo.showable.Showable;

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


            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.header.setText(showable.getName());

        return convertView;
    }


    class ViewHolder {
        TextView header;
    }
}
