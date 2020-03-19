package com.horovod.android.merchandiserdemo.showable;

import com.horovod.android.merchandiserdemo.classifier.Classifier;
import com.horovod.android.merchandiserdemo.classifier.ClassifierType;

import java.util.List;

public interface Showable {

    public int getIdNumber();
    public ShowableType getShowableType();
    public void setIdNumber(int newIdNumber);
    public String getPreview();
    public void setPreview(String preview);
    public String getImage();
    public void setImage(String image);
    public boolean isHorizontal();
    public void setHorizontal(boolean horizontal);
    public String getName();
    public void setName(String name);
    public String getComment();
    public void setComment(String comment);
    public List<Classifier> getClassifiers();
    public void setClassifiers(List<Classifier> list);
    public List<Classifier> getClassifiersByType(ClassifierType type);
    public void addClassifier(Classifier classifier);
    public void clearClassifiers();
    public List<Showable> getShowables();
    public Showable getShowableById(int id);
    public void setShowables(List<Showable> list);
    public void addShowable(Showable showable);
    public void clearShowables();
    public Showable getParent();
    public void setParent(Showable parent);
    public Showable cloneMe(Showable newParent);

}
