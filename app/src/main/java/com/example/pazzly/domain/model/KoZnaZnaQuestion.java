package com.example.pazzly.domain.model;

import java.util.List;

public class KoZnaZnaQuestion {
    private List<String> options;
    private String correctAnswer;
    private String question;

    public KoZnaZnaQuestion(List<String> options, String correctAnswer, String question) {
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.question = question;
    }

    public KoZnaZnaQuestion() {
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
