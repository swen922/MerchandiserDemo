package com.horovod.android.merchandiserdemo.view;

import android.content.Context;
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

import java.util.List;

public class KeepersSpinnerAdapter extends ArrayAdapter<Showable> {

    private Context myContext;
    private int layoutID;
    private int textViewId;
    private LayoutInflater inflater;

    public KeepersSpinnerAdapter(Context context, int resource, int textViewResourceId, List<Showable> objects) {
        super(context, resource, textViewResourceId, objects);
        this.myContext = context;
        this.layoutID = resource;
        this.textViewId = textViewResourceId;
        this.inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolderKeeper viewHolderKeeper;

        if (convertView == null) {
            viewHolderKeeper = new ViewHolderKeeper();
            convertView = inflater.inflate(layoutID, parent, false);
            viewHolderKeeper.imageView = convertView.findViewById(R.id.keeper_spinner_row_imageview);
            convertView.setTag(viewHolderKeeper);
        }
        else {
            viewHolderKeeper = (ViewHolderKeeper) convertView.getTag();
        }

        return convertView;

    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        ViewHolderKeeperDrop viewHolderKeeperDrop;

        if (convertView == null) {
            viewHolderKeeperDrop = new ViewHolderKeeperDrop();
            convertView = inflater.inflate(textViewId, parent, false);
            viewHolderKeeperDrop.textView = convertView.findViewById(R.id.keeper_spinner_row_drop_textview);
            convertView.setTag(viewHolderKeeperDrop);
        }
        else {
            viewHolderKeeperDrop = (ViewHolderKeeperDrop) convertView.getTag();
        }

        viewHolderKeeperDrop.textView.setText(Data.getKeepersList().get(position).getName());
        return convertView;

    }

    class ViewHolderKeeper {
        ImageView imageView;
    }

    class ViewHolderKeeperDrop {
        TextView textView;
    }



}
