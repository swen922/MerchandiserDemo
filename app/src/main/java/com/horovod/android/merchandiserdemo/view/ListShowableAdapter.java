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
import com.horovod.android.merchandiserdemo.classifier.Classifier;
import com.horovod.android.merchandiserdemo.classifier.ClassifierType;
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

        StringBuilder sb = new StringBuilder("");

        if (!showable.getClassifiers().isEmpty()) {
            // Классификаторы по формату торговли всегда идут первыми, и добавляются все, которые есть
            // (в реальности обычно будет только один, потому что магазин не может быть
            // одновременно универсамом и традицией, например...)
            List<Classifier> clFORMAT = showable.getClassifiersByType(ClassifierType.FORMAT);
            if (!clFORMAT.isEmpty()) {
                for (Classifier clFR : clFORMAT) {
                    sb.append("\n").append(myContext.getResources().getString(R.string.class_desc_format).toUpperCase()).append(" ");
                    sb.append(clFR.getName());
                }
            }

            // А вот остальные классификаторы могут быть по несколько и по много в одном магазине
            // поэтому по ним ставим ограничение вручную: 2 штуки показываем, а далее – многоточие
            for (ClassifierType type : ClassifierType.values()) {
                if (type != ClassifierType.FORMAT) {
                    List<Classifier> list = showable.getClassifiersByType(type);
                    if (!list.isEmpty()) {
                        sb.append("\n");
                        if (list.size() > 2) {
                            sb.append(list.get(0).getName()).append("; ").append(list.get(1).getName()).append("...");
                        }
                        else if (list.size() == 2) {
                            sb.append(list.get(0).getName()).append("; ").append(list.get(1).getName());
                        }
                        else {
                            sb.append(list.get(0).getName());
                        }
                    }
                }
            }
        }
        viewHolder.comment.setText(sb.toString().trim());

        if (!sb.toString().isEmpty()) {
            setLayoutParamsHeight(viewHolder.comment, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        }
        else {
            setLayoutParamsHeight(viewHolder.comment, 0);
        }

        if (!showable.getPreview().isEmpty()) {
            setLayoutParamsHeight(viewHolder.imageView, ConstraintLayout.LayoutParams.WRAP_CONTENT);
            String image = showable.getPreview();
            File file1 = new File(myContext.getFilesDir() + File.separator + Data.photoFolder + File.separator + image);
            if (file1.exists()) {
                Picasso.get().load(file1).into(viewHolder.imageView);
            }
            else {
                setLayoutParamsHeight(viewHolder.imageView, 0);
            }
        }
        else {
            setLayoutParamsHeight(viewHolder.imageView, 0);
        }

        return convertView;
    }


    private void setLayoutParamsHeight(View view, int height) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = height;
        view.setLayoutParams(params);
    }

    class ViewHolder {
        TextView header;
        TextView comment;
        ImageView imageView;
    }
}
