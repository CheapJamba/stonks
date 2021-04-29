package com.bagandov.stonks.parser;

public interface Handler<T> {
    void handle(T input);
}
