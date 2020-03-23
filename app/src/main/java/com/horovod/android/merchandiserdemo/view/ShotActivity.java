package com.horovod.android.merchandiserdemo.view;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.horovod.android.merchandiserdemo.R;
import com.horovod.android.merchandiserdemo.data.Data;
import com.horovod.android.merchandiserdemo.showable.Showable;
import com.squareup.picasso.Picasso;

import java.io.File;

public class ShotActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView nameTextView;
    private TextView commentTextView;
    private ImageView buttonScale;
    private ImageView buttonClose;

    private int showableId = -1;
    private Showable myShowable;

    private ImageMoveListener imageMoveListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shot_fragment);

        imageView = findViewById(R.id.shot_frag_imageview);
        nameTextView = findViewById(R.id.shot_frag_name);
        commentTextView = findViewById(R.id.shot_frag_comment);
        buttonScale = findViewById(R.id.shot_frag_button_scale);
        buttonClose = findViewById(R.id.shot_frag_button_close);

        imageMoveListener = new ImageMoveListener(imageView);
        imageView.setOnTouchListener(imageMoveListener);

        Intent intent = getIntent();
        showableId = intent.getIntExtra(Data.KEY_IDNUMBER, -1);

        if (showableId > 0) {
            myShowable = Data.getShowableByid(showableId);
        }

        if (myShowable != null) {
            File file = new File(getApplicationContext().getFilesDir() + File.separator + Data.photoFolder + File.separator + myShowable.getImage());
            if (file.exists()) {
                /*if (ShotOrientation.VERTICAL == myShowable.getOrientation()) {
                    Picasso.get().load(file).into(imageView);
                }
                else {
                    Picasso.get().load(file).rotate(-90).into(imageView);
                }*/

                Picasso.get().load(file).into(imageView);

            }

            //handleOrientation(myShowable.getOrientation());
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

        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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


    /*private void handleOrientation(ShotOrientation orientation) {
        if (ShotOrientation.HORIZONTAL == orientation) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }*/


    private void setLayoutParamsHeight(View view, int height) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = height;
        view.setLayoutParams(params);
    }

}
