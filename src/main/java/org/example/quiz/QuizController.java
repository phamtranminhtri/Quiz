package org.example.quiz;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class QuizController {

    public record QuizResult(boolean success, String feedback) {}

    @GetMapping("/api/quiz")
    public ResponseEntity<Quiz> getQuiz() {
        Quiz quiz = new Quiz(
                "The Java Logo",
                "What is depicted on the Java logo?",
                List.of("Robot", "Tea leaf", "Cup of coffee", "Bug")
        );
        return new ResponseEntity<>(quiz, HttpStatus.OK);
    }

    @PostMapping("/api/quiz")
    public ResponseEntity<QuizResult> postQuiz(@RequestParam int answer) {
        QuizResult quizResult = switch (answer) {
            case 2 -> new QuizResult(true, "Congratulations, you're right!");
            default -> new QuizResult(false, "Wrong answer! Please, try again.");
        };
        return new ResponseEntity<>(quizResult, HttpStatus.OK);
    }

}
