package com.horovod.android.merchandiserdemo.showable;

import android.util.Log;

import com.horovod.android.merchandiserdemo.classifier.Classifier;
import com.horovod.android.merchandiserdemo.classifier.ClassifierType;
import com.horovod.android.merchandiserdemo.data.Data;

import java.util.ArrayList;
import java.util.List;

public class Shot implements Showable {

    private int idNumber = -1;
    private String preview = "";
    private String image = "";
    private String name = "";
    private String comment = "";
    private List<Classifier> classifiers = new ArrayList<>();
    private Showable parent;

    private Shot() {
    }

    public static Shot getInstance(Showable parent, String name) {
        Shot shot = new Shot();
        shot.setIdNumber(Data.incrementAndGetNewId());
        shot.setName(name);
        shot.setParent(parent); // Этим действием сразу и добавляем магазин в список (см. метод setParent() ---> parent.addShowable())
        return shot;
    }

    @Override
    public int getIdNumber() {
        return -1;
    }

    @Override
    public ShowableType getShowableType() {
        return ShowableType.SHOT;
    }

    @Override
    public void setIdNumber(int newIdNumber) {
        if (newIdNumber > 0) {
            this.idNumber = newIdNumber;
        }
    }

    @Override
    public String getPreview() {
        return this.preview;
    }

    @Override
    public void setPreview(String preview) {
        if (preview != null) {
            this.preview = preview;
        }
    }

    @Override
    public String getImage() {
        return null;
    }

    @Override
    public void setImage(String image) {

    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        if (name != null) {
            this.name = name;
        }
    }

    @Override
    public String getComment() {
        return this.comment;
    }

    @Override
    public void setComment(String comment) {
        if (comment != null) {
            this.comment = comment;
        }
    }

    @Override
    public List<Classifier> getClassifiers() {
        return this.classifiers;
    }

    @Override
    public List<Classifier> getClassifiersByType(ClassifierType type) {
        List<Classifier> result = new ArrayList<>();
        for (Classifier cl : this.classifiers) {
            if (cl.getClassifierType() == type) {
                result.add(cl);
            }
        }
        return result;
    }

    @Override
    public void setClassifiers(List<Classifier> list) {
        if (list != null) {
            this.classifiers = list;
        }
    }

    @Override
    public void addClassifier(Classifier classifier) {
        if (classifier != null) {
            this.classifiers.add(classifier);
        }
    }

    @Override
    public void clearClassifiers() {
        this.classifiers.clear();
    }

    @Override
    public List<Showable> getShowables() {
        return null;
    }

    @Override
    public Showable getShowableById(int id) {
        return null;
    }

    @Override
    public void setShowables(List<Showable> list) {

    }

    @Override
    public void addShowable(Showable showable) {
    }

    @Override
    public void clearShowables() {
    }

    @Override
    public Showable getParent() {
        return null;
    }

    @Override
    public void setParent(Showable parent) {
        if (parent != null) {
            this.parent = parent;
            if (!this.parent.getShowables().contains(this)) {
                this.parent.addShowable(this);
            }
        }
    }

    @Override
    public Showable cloneMe(Showable newParent) {
        Showable result = Shot.getInstance(newParent, this.name);
        result.setPreview(this.preview);
        result.setImage(this.image);
        result.setComment(this.comment);
        List<Classifier> newListClass = new ArrayList<>();
        for (Classifier cl : this.classifiers) {
            newListClass.add(cl.clonMe());
        }
        result.setClassifiers(newListClass);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Showable ");
        sb.append(this.name).append(", id = ").append(this.idNumber);
        return sb.toString();
    }


}
