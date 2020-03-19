package com.horovod.android.merchandiserdemo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.horovod.android.merchandiserdemo.R;
import com.horovod.android.merchandiserdemo.data.Data;
import com.horovod.android.merchandiserdemo.showable.Showable;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ShotFragment extends Fragment {

    private ImageView imageView;
    //private String fileName = "";
    private int showableId = -1;
    private Showable myShowable;

    private FragmentManager myFragmentManager;
    private Context myContext;

    public void setMyFragmentManager(FragmentManager fragmentManager, Context context) {
        this.myFragmentManager = fragmentManager;
        this.myContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.shot_fragment, container, false);
        imageView = rootView.findViewById(R.id.shot_frag_imageview);

        Bundle args = getArguments();
        if (args != null) {
            showableId = args.getInt(Data.KEY_IDNUMBER, -1);
        }

        if (showableId > 0) {
            myShowable = Data.getShowableByid(showableId);
        }

        if (myShowable != null) {
            File file = new File(myContext.getFilesDir() + File.separator + Data.photoFolder + File.separator + myShowable.getImage());

            if (file.exists()) {
                if (myShowable.isHorizontal()) {
                    Picasso.get().load(file).rotate(-90).into(imageView);
                }
                else {
                    Picasso.get().load(file).into(imageView);
                }
            }
        }
        return rootView;
    }





}
