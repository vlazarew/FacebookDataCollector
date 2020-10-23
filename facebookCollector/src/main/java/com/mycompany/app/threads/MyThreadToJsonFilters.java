package com.mycompany.app.threads;

import com.mycompany.app.Crawler;
import com.mycompany.app.data.InputFilters;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;

@Getter
public class MyThreadToJsonFilters implements Runnable {

    private InputFilters inputFilters;

    public MyThreadToJsonFilters(InputFilters inputFilters){
        this.inputFilters = inputFilters;
    }

    @Override
    @SneakyThrows
    public void run() {
        Crawler crawler = new Crawler();
        crawler.initializeOperaDriver(crawler.getLoginPageURL());
        TimeUnit.SECONDS.sleep(1);
        crawler.loginIntoFacebook();
        TimeUnit.SECONDS.sleep(1);
        crawler.fillNameLastName(this.inputFilters.getFirstName(), this.inputFilters.getLastName());
        TimeUnit.SECONDS.sleep(1);
        crawler.search(this.inputFilters);
    }
}
