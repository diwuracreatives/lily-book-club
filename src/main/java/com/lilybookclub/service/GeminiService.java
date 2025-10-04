package com.lilybookclub.service;


public interface GeminiService {

    String getBookSummary(String bookname, String bookAuthor);

    String getSuggestedCategories(String interest);
}
