package com.horovod.android.merchandiserdemo.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.horovod.android.merchandiserdemo.R;
import com.horovod.android.merchandiserdemo.classifier.Classifier;
import com.horovod.android.merchandiserdemo.classifier.ClassifierType;
import com.horovod.android.merchandiserdemo.data.Data;
import com.horovod.android.merchandiserdemo.data.Util;
import com.horovod.android.merchandiserdemo.showable.Showable;
import com.horovod.android.merchandiserdemo.showable.ShowableType;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ListShowableAdapter extends ArrayAdapter<Showable> {

    private Context myContext;
    private int resourceID;
    private WindowManager windowManager;
    private boolean thisIsStore = true;

    public ListShowableAdapter(Context context, int resource, List<Showable> list, WindowManager manager) {
        super(context, resource, list);
        myContext = context;
        resourceID = resource;
        this.windowManager = manager;
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

        /** Высчитываем количество символов, которое помещается в одну строку viewHolder.comment
         * причем считаем только один раз и в других случаях применяем сохраненный результат */
        if (Data.maxLengthCommentList < 0) {
            calculateLength(viewHolder.comment);
        }

        String formatedClassifiers = Util.formatClassifiers(showable, myContext, (Data.maxLengthCommentList - 1));

        viewHolder.comment.setText(formatedClassifiers);

        if (!formatedClassifiers.isEmpty()) {
            setLayoutParamsHeight(viewHolder.comment, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        }
        else {
            setLayoutParamsHeight(viewHolder.comment, 0);
        }

        if (!showable.getPreview().isEmpty()) {
            String image = showable.getPreview();
            File file = new File(myContext.getFilesDir() + File.separator + Data.photoFolder + File.separator + image);
            if (file.exists()) {


                /** Нам требуется динамически установить высоту картинки согласно тому, как она впишется в ImageView.
                 * Однако, функция ConstraintLayout.LayoutParams.WRAP_CONTENT не работает нормально в старых версиях Андроида,
                 * причем в некоторых виртуальных моделях (Nexus S) даже и в более современных версиях (API 27).
                 * В реальном планшете у меня (Lenovo с API 23) тоже не работает, как надо.
                 * Получается высота ImageView сильно больше, чем картинка, практически во всю высоту экрана.
                 * И даже если работает (на телефоне Samsung A30), то добавляет небольшие поля сверху и снизу,
                 * причем разного размера для разных фото.
                 *
                 * Так что приходится подсчитывать высоту картинки вручную.
                 *
                 * Причем, каждую картинку приходится фактически загружать из домашней папки 2 раза:
                 * сначала Пикассо загружает ее сразу в ImageView, а потом еще раз грузим ее через Пикассо,
                 * чтобы получить объект Bitmap и подсчитать его высоту после масштабирования в ширину экрана,
                 * и назначить эту высоту для ImageView.
                 * А если пытаться не грузить первым действием картинку в ImageView через Пикассо,
                 * а вставить вручную полученный отдельно Bitmap в ImageView через метод setImageBitmap(Bitmap b),
                 * то тогда картинки очень плохо загружается в макет: на старте все отсутствуют, потом постепенно появляются, но не все...
                 * В общем, вставлять только через Пикассо! */

                Picasso.get().load(file).into(viewHolder.imageView);
                //viewHolder.imageView.setImageBitmap(bitmap); – так очень плохо загружается на странице, появляется не сразу

                int imageHeight = -1;
                Bitmap bitmap = null;
                if (showable.getPreviewWidth() > 0 && showable.getPreviewHeight() > 0) {
                    imageHeight = getImageHeight(showable.getPreviewWidth(), showable.getPreviewHeight());
                }
                else {
                    bitmap = getBitmap(file);

                    if (bitmap != null) {
                        showable.setPreviewWidth(bitmap.getWidth());
                        showable.setPreviewHeight(bitmap.getHeight());
                    }
                    imageHeight = getImageHeight(bitmap);
                }
                if (imageHeight > 0) {
                    setLayoutParamsHeight(viewHolder.imageView, imageHeight);
                }
            }
            else {
                setLayoutParamsHeight(viewHolder.imageView, 0);
            }
        }
        else {
            setLayoutParamsHeight(viewHolder.imageView, 0);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ShowableType.STORE == showable.getShowableType() && showable.getIdNumber() > 0) {
                    Intent intent = new Intent(Data.INTENT_REPLACE_SHOWABLE);
                    intent.putExtra(Data.KEY_IDNUMBER, showable.getIdNumber());
                    myContext.sendBroadcast(intent);
                }
                else if (ShowableType.SHOT == showable.getShowableType() && showable.getIdNumber() > 0) {
                    /*Intent intent = new Intent(Data.INTENT_SHOW_PHOTO);
                    intent.putExtra(Data.KEY_IDNUMBER, showable.getIdNumber());
                    myContext.sendBroadcast(intent);*/
                    Intent intent = new Intent(myContext, ShotActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(Data.KEY_IDNUMBER, showable.getIdNumber());
                    myContext.startActivity(intent);

                }

            }
        });

        return convertView;
    }


    private void setLayoutParamsHeight(View view, int height) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = height;
        view.setLayoutParams(params);
    }

    private void calculateLength(TextView textView) {
        String checkLength = myContext.getString(R.string.maxLength);
        textView.setText(checkLength);
        Data.maxLengthCommentList = textView.getPaint().breakText(checkLength, 0, checkLength.length(), true, textView.getWidth(), null);
        textView.setText("");
        Data.maxLengthCommentList = Data.maxLengthCommentList > Data.minLength ? Data.maxLengthCommentList : Data.minLength;
    }

    /*private Bitmap getBitmap(ImageView imageView) {
        imageView.setDrawingCacheEnabled(true);
        imageView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        imageView.layout(0, 0,
                imageView.getMeasuredWidth(), imageView.getMeasuredHeight());
        imageView.buildDrawingCache(true);
        Bitmap bitmap = null;
        *//*bitmap = Bitmap.createBitmap(imageView.getDrawingCache());
        imageView.setDrawingCacheEnabled(false);*//*
        Log.i("BITMAP ||||", "imageView.getDrawingCache() == null = " + (imageView.getDrawingCache() == null));
        return bitmap;
    }


    private int getImageHeight(Bitmap bm) {

        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        double widthScreen = metrics.widthPixels;
        double heightScreen = metrics.heightPixels;
        double result = heightScreen / 2;

        if (bm != null) {
            double widthFile = bm.getWidth();
            double heightFile = bm.getHeight();

            double ratio = widthScreen / widthFile;
            double heightScaled = heightFile * ratio;
            double limit = 600 < (heightScreen/2) ? 600 : (heightScreen/2);
            result = heightScaled < limit ? heightScaled : limit;
        }

        return (int) result;
    }*/

    /*private int getImageHeight(File file) {

        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        double widthScreen = metrics.widthPixels;
        double heightScreen = metrics.heightPixels;
        double result = heightScreen / 3;

        Log.i("IMAGE HEIGHT ||| ", "file.getName() = " + file.getName());
        Log.i("IMAGE HEIGHT ||| ", "widthScreen = " + widthScreen);
        Log.i("IMAGE HEIGHT ||| ", "heightScreen = " + heightScreen);
        Log.i("IMAGE HEIGHT ||| ", "resultFirst = " + result);

        Bitmap bm = getBitmap(file);

        if (bm != null) {
            double widthFile = bm.getWidth();
            double heightFile = bm.getHeight();

            double ratio = widthScreen / widthFile;
            double heightScaled = heightFile * ratio;
            double limit = 600 < (heightScreen/3) ? 600 : (heightScreen/3);
            result = heightScaled < limit ? heightScaled : limit;

            Log.i("IMAGE HEIGHT ||| ", "widthFile = " + widthFile);
            Log.i("IMAGE HEIGHT ||| ", "heightFile = " + heightFile);
            Log.i("IMAGE HEIGHT ||| ", "ratio = " + ratio);
            Log.i("IMAGE HEIGHT ||| ", "heightScaled = " + heightScaled);

            Log.i("IMAGE HEIGHT ||| ", "limit = " + limit);
            Log.i("IMAGE HEIGHT ||| ", "result = " + result);
            Log.i("---------------- ", "-------------------------------------------------");

        }

        return (int) result;
    }*/


    private int getImageHeight(Bitmap bm) {

        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        double widthScreen = metrics.widthPixels;
        double heightScreen = metrics.heightPixels;
        double result = heightScreen / 3;

        if (bm != null) {
            double widthFile = bm.getWidth();
            double heightFile = bm.getHeight();

            double ratio = widthScreen / widthFile;
            double heightScaled = heightFile * ratio;
            double limit = 600 < (heightScreen/3) ? 600 : (heightScreen/3);
            result = heightScaled < limit ? heightScaled : limit;
        }
        return (int) result;
    }

    private int getImageHeight(int bitmapWidth, int bitmapHeight) {

        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        double widthScreen = metrics.widthPixels;
        double heightScreen = metrics.heightPixels;
        double result = heightScreen / 3;

        double widthFile = bitmapWidth;
        double heightFile = bitmapHeight;

        double ratio = widthScreen / widthFile;
        double heightScaled = heightFile * ratio;
        double limit = 600 < (heightScreen/3) ? 600 : (heightScreen/3);
        result = heightScaled < limit ? heightScaled : limit;

        return (int) result;
    }

    private Bitmap getBitmap(File file) {
        final List<Bitmap> listBM = new ArrayList<>();
        try {
            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    listBM.add(bitmap);
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            Picasso.get().load(file).into(target);

        } catch (Exception e) {
            //Log.e("EXCEPTION!  |||| ", e.toString());
        }

        if (!listBM.isEmpty()) {
            return listBM.get(0);
        }
        return null;
    }


    class ViewHolder {
        TextView header;
        TextView comment;
        ImageView imageView;
    }


}
