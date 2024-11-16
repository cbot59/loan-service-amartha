package com.journal.loanserviceamartha;

import org.springframework.boot.SpringApplication;

public class TestLoanServiceAmarthaApplication {

    public static void main(String[] args) {
        SpringApplication.from(LoanServiceAmarthaApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
