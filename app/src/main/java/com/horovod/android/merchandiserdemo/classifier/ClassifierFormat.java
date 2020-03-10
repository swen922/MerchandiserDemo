package com.horovod.android.merchandiserdemo.classifier;

public class ClassifierFormat implements Classifier {

    private String name;
    private String comment;

    public ClassifierFormat(String name) {
        this.name = name;
    }


    @Override
    public ClassifierType getClassifierType() {
        return ClassifierType.FORMAT;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        if (name != null && !name.isEmpty()) {
            this.name = name;
        }
    }

    @Override
    public String getComment() {
        return comment;
    }


    @Override
    public void setComment(String comment) {
        if (comment != null && !comment.isEmpty()) {
            this.comment = comment;
        }
    }

}
