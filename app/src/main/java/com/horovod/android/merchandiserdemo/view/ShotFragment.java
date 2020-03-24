package com.horovod.android.merchandiserdemo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatTextView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

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
    private TextView nameTextView;
    private TextView commentTextView;
    private ImageView buttonScale;
    private ImageView buttonClose;

    private int showableId = -1;
    private Showable myShowable;

    private FragmentManager myFragmentManager;
    private Context myContext;
    private ImageMoveListener imageMoveListener;

    public void setMyFragmentManager(FragmentManager fragmentManager, Context context) {
        this.myFragmentManager = fragmentManager;
        this.myContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.shot_fragment, container, false);
        imageView = rootView.findViewById(R.id.shot_frag_imageview);
        nameTextView = rootView.findViewById(R.id.shot_frag_name);
        //nametextViewHoriz = rootView.findViewById(R.id.shot_frag_name_horiz);
        //nametextViewHoriz = new VerticalTextView(myContext, )
        commentTextView = rootView.findViewById(R.id.shot_frag_comment);
        buttonScale = rootView.findViewById(R.id.shot_frag_button_scale);
        buttonClose = rootView.findViewById(R.id.shot_frag_button_close);
        //imageMoveListener = new ImageMoveListener(imageView);
        imageView.setOnTouchListener(imageMoveListener);

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
                /*if (ShotOrientation.VERTICAL == myShowable.getOrientation()) {
                    Picasso.get().load(file).into(imageView);
                }
                else {
                    Picasso.get().load(file).rotate(-90).into(imageView);
                }*/
            }

            handleTextViews(nameTextView, commentTextView);
        }

        buttonScale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View moveView = imageMoveListener.getView();
                moveView.setTranslationX(0);
                moveView.setTranslationY(0);
                moveView.setScaleX(1);
                moveView.setScaleY(1);
            }
        });

        /*buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFragmentManager.beginTransaction().remove(Data.shotFragment).commit();
                Data.shotFragment = null;
            }
        });*/

        return rootView;
    }

    private void handleTextViews(TextView nameView, TextView commentView) {
        if (!myShowable.getName().isEmpty()) {
            nameView.setText(myShowable.getName());
            setLayoutParamsHeight(nameView, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        }
        else {
            nameView.setText("");
            setLayoutParamsHeight(nameView, 0);
        }
        if (!myShowable.getComment().isEmpty()) {
            commentView.setText(myShowable.getComment());
            setLayoutParamsHeight(commentView, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        }
        else {
            commentView.setText("");
            setLayoutParamsHeight(commentView, 0);
        }
    }


    private void setLayoutParamsHeight(View view, int height) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = height;
        view.setLayoutParams(params);
    }


}
