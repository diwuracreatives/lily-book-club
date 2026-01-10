package com.lilybookclub.service.impl;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import com.lilybookclub.enums.Category;
import com.lilybookclub.service.GeminiService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@AllArgsConstructor
@Slf4j
public class GeminiServiceImpl implements GeminiService {

    private final Client geminiClient;

    private String askGemini(String query){
        GenerateContentResponse response =
                geminiClient.models.generateContent(
                        "gemini-2.5-flash",
                        query,
                        null);

        log.info("Gemini response: {}", response.text());
        return response.text();
    }

    public String getBookSummary(String bookname, String bookAuthor){
        String query =  String.format("Write a summary of this book %s by %s in a paragraph using simple understandable english words in not greater than 50 words, don't add the author's name", bookname,bookAuthor);
        return askGemini(query);
    }

    public String getSuggestedCategories(String interest){
        String categories = Arrays.toString(Category.values());
        String query =  String.format("The user will describe what they love, their personality, the type of book club they want to join, or what they want to learn. Based on this input, match it to the most relevant categories from the following list: CATEGORIES: %s Return ONLY the matching categories in UPPERCASE, separated by a single space. Do not return any explanations or extra text. USER INPUT IS: %s", categories,  interest);
        return askGemini(query);
    }

}
