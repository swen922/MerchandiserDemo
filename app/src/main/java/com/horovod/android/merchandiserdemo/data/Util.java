package com.horovod.android.merchandiserdemo.data;

import android.content.Context;
import android.util.Log;

import com.horovod.android.merchandiserdemo.R;
import com.horovod.android.merchandiserdemo.classifier.*;
import com.horovod.android.merchandiserdemo.showable.*;

import java.util.List;

public class Util {



    // Метод вытащен сюда, потому что код один и тот же для адаптера списка и для общего макета страинцы
    public static String formatClassifiers(Showable showable, Context myContext, int max) {

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

                            /** Рассчитываем включить в строчку не менее двух классификаторов.
                             * Но при этом, если классификаторы короткие, и есть еще место,
                             * то добавляем еще классификаторов из списка, пока не превысит указанное
                             * максимальное кол-во символов (аргумент int max), за вычетом 6 символов в конце – "...(+X)"*/

                            max = max - 6;
                            max = max < Data.minLength ? Data.minLength : max; // Чисто на всякий случай
                            StringBuilder sb2 = new StringBuilder("");
                            int counter = list.size();

                            while (counter >= 2) {
                                sb2 = new StringBuilder("");

                                for (int i = 0; i < counter; i++) {
                                    sb2.append(list.get(i).getName()).append(", ");
                                }
                                counter--;
                                String input = sb2.toString();
                                input = input.trim();
                                input = input.substring(0, (input.length() - 1));
                                sb2 = new StringBuilder(input);

                                if (goodLength(input, max)) {
                                    break;
                                }
                            }

                            int another = list.size() - counter - 1;
                            if (another > 0) {
                                sb2.append("...(+");
                                sb2.append(another).append(")");
                            }

                            sb.append(sb2.toString());

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

    private static boolean goodLength(String input, int max) {

        /*Log.i("GOOD LENGTH ||| ", "input.length() = " + input.length());
        Log.i("GOOD LENGTH ||| ", "max = " + max);*/

        if (input.trim().length() < max) {
            return true;
        }
        return false;
    }


}
