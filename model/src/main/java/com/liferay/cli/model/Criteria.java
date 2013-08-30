package com.liferay.cli.model;

public interface Criteria<T> {

    boolean meets(T t);
}
