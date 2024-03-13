package ru.taste;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final CrptApi crptApi = new CrptApi(TimeUnit.MINUTES, 5);

    public static void main(String[] args) {
        while (true) {
            crptApi.sendRequest(System.out::println);
        }
    }
}