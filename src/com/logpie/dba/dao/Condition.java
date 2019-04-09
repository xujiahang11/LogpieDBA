package com.logpie.dba.dao;

public abstract class Condition {

    private Condition left;
    private Condition right;

    public Condition()
    {
    }

    public Condition(Condition left, Condition right)
    {
        this.left = left;
        this.right = right;
    }

    public Condition getLeft()
    {
        return left;
    }

    public Condition getRight()
    {
        return right;
    }

    public void setLeft(Condition left)
    {
        this.left = left;
    }

    public void setRight(Condition right)
    {
        this.right = right;
    }
}
