package com.simo.minesweeper.minesweeper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationConfiguration implements CommandLineRunner {

    private final MineSweeper mineSweeper;

    @Autowired
    public ApplicationConfiguration(MineSweeper mineSweeper) {
        this.mineSweeper = mineSweeper;
    }

    @Override
    public void run(String... args) throws Exception {
        this.mineSweeper.init();
    }
}
