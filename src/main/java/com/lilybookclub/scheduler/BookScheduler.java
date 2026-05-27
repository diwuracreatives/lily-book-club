package com.lilybookclub.scheduler;

import com.lilybookclub.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookScheduler {

    private final BookService bookService;

    /* * send email to user to read book at 9am */
    @Scheduled(cron = "0 0 9 * * *", zone = "Africa/Lagos")
    public void sendUsersWeeklyBookRecommendations(){
        bookService.sendBookRecommendationEmail();
    }

}
