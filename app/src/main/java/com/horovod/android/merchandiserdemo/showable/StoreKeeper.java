package com.horovod.android.merchandiserdemo.showable;

import android.util.Log;

import com.horovod.android.merchandiserdemo.classifier.Classifier;
import com.horovod.android.merchandiserdemo.classifier.ClassifierType;

import java.util.ArrayList;
import java.util.List;

public class StoreKeeper implements Showable {

    private String name = "";
    private String comment = "";
    private List<Showable> showables = new ArrayList<>();

    public StoreKeeper(String name) {
        this.name = name;
    }

    @Override
    public int getIdNumber() {
        return -1;
    }

    @Override
    public ShowableType getShowableType() {
        return ShowableType.STORE_KEEPER;
    }

    @Override
    public void setIdNumber(int newIdNumber) {

    }

    @Override
    public String getPreview() {
        return null;
    }

    @Override
    public void setPreview(String preview) {

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
        return new ArrayList<>();
    }

    @Override
    public void setClassifiers(List<Classifier> list) {

    }

    @Override
    public List<Classifier> getClassifiersByType(ClassifierType type) {
        return null;
    }

    @Override
    public void addClassifier(Classifier classifier) {

    }

    @Override
    public void clearClassifiers() {
    }

    @Override
    public List<Showable> getShowables() {
        return this.showables;
    }

    @Override
    public Showable getShowableById(int id) {
        Showable result = null;
        if (!this.showables.isEmpty()) {
            for (Showable sh : showables) {
                if (sh.getIdNumber() == id) {
                    result = sh;
                    break;
                }
            }
        }
        return result;
    }

    @Override
    public void setShowables(List<Showable> list) {
        if (list != null) {
            this.showables = list;
        }
    }

    @Override
    public void addShowable(Showable showable) {
        if (showable != null) {
            this.showables.add(showable);
        }
    }

    @Override
    public void clearShowables() {
        this.showables.clear();
    }

    @Override
    public Showable getParent() {
        return null;
    }

    @Override
    public void setParent(Showable parent) {

    }

    @Override
    public Showable cloneMe(Showable newParent) {
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Showable StoreKeeper");
        return sb.toString();
    }
}
