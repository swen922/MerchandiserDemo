package com.horovod.android.merchandiserdemo.classifier;

public class ClassifierOther implements Classifier {

    private String name = "";
    private String comment = "";

    public ClassifierOther(String name) {
        this.name = name;
    }

    @Override
    public ClassifierType getClassifierType() {
        return ClassifierType.OTHER;
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
    public Classifier clonMe() {
        Classifier clone = new ClassifierOther(this.name);
        clone.setComment(this.comment);
        return clone;
    }
}
