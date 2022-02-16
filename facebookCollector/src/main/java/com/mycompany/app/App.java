package com.mycompany.app;

import com.mycompany.app.data.InputFilters;
import com.mycompany.app.threads.MyThreadToJsonFilters;
import lombok.SneakyThrows;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class App {
    @SneakyThrows
    public static void main(String[] args) {
        List<InputFilters> inputFilters = InputFilters.readInputFiltersFromFolder();

        for (InputFilters inputFilter : inputFilters) {
            Runnable runnable = new MyThreadToJsonFilters(inputFilter);
            new Thread(runnable).start();
            TimeUnit.SECONDS.sleep(2);
        }

    }
}
