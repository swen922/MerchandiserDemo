package com.horovod.android.merchandiserdemo.data;

import android.content.Context;

import com.horovod.android.merchandiserdemo.R;
import com.horovod.android.merchandiserdemo.classifier.*;
import com.horovod.android.merchandiserdemo.showable.*;

import java.util.List;

public class Util {



    // Метод вытащен сюда, потому что код один и тот же для адаптера списка и для общего макета страинцы
    public static String formatClassifiers(Showable showable, Context myContext) {

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

            // Классификатор по шагу тоже ставим вперед, потому что он важен для Shot'ов
            List<Classifier> clSTEP = showable.getClassifiersByType(ClassifierType.STEP);
            if (!clSTEP.isEmpty()) {
                for (Classifier clST : clSTEP) {
                    sb.append("\n").append(myContext.getResources().getString(R.string.class_desc_step).toUpperCase()).append(" ");
                    sb.append(clST.getName());
                }
            }

            // А вот остальные классификаторы могут быть по несколько и по много в одном магазине
            // поэтому по ним ставим ограничение вручную: 2 штуки показываем, а далее – многоточие
            for (ClassifierType type : ClassifierType.values()) {
                if (type != ClassifierType.FORMAT && type != ClassifierType.STEP) {
                    List<Classifier> list = showable.getClassifiersByType(type);
                    if (!list.isEmpty()) {

                        sb.append("\n").append(type.getHeader(myContext.getResources()).toUpperCase()).append(" ");
                        if (list.size() > 2) {
                            int another = list.size() - 2;
                            sb.append(list.get(0).getName()).append(", ").append(list.get(1).getName()).append("...(+");
                            sb.append(another).append(")");
                        }
                        else if (list.size() == 2) {
                            sb.append(list.get(0).getName()).append(", ").append(list.get(1).getName());
                        }
                        else {
                            sb.append(list.get(0).getName());
                        }
                    }
                }
            }
        }
        return sb.toString().trim();
    }

}
