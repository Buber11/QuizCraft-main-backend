package main.QuizCraft.projection;


public interface QuizProjection {
    Long getId();
    String getQuestion();
    String getCorrectAnswer();
    String getBadAnswer1();
    String getBadAnswer2();
    String getBadAnswer3();
}
