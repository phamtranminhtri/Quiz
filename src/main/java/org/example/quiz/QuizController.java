package org.example.quiz;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class QuizController {

    public record QuizResult(boolean success, String feedback) {}
    public record UserAnswer(Set<Integer> answer) {}

    private Map<Integer, Quiz> quizMap = new HashMap<>();

//    @GetMapping("/api/quiz")
//    public ResponseEntity<Quiz> getQuiz() {
//        Quiz quiz = new Quiz(
//                "The Java Logo",
//                "What is depicted on the Java logo?",
//                List.of("Robot", "Tea leaf", "Cup of coffee", "Bug")
//        );
//        return new ResponseEntity<>(quiz, HttpStatus.OK);
//    }
//
//    @PostMapping("/api/quiz")
//    public ResponseEntity<QuizResult> postQuiz(@RequestParam int answer) {
//        QuizResult quizResult = switch (answer) {
//            case 2 -> new QuizResult(true, "Congratulations, you're right!");
//            default -> new QuizResult(false, "Wrong answer! Please, try again.");
//        };
//        return new ResponseEntity<>(quizResult, HttpStatus.OK);
//    }

    @PostMapping("/api/quizzes")
    public ResponseEntity<Quiz> postQuizzes(@Valid @RequestBody Quiz quiz) {
        quizMap.put(quiz.getId(), quiz);
        return new ResponseEntity<>(quiz, HttpStatus.OK);
    }

    @GetMapping("/api/quizzes/{id}")
    public ResponseEntity<Quiz> getQuizzesId(@PathVariable int id) {
        if (!quizMap.containsKey(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Quiz quiz = quizMap.get(id);
        return new ResponseEntity<>(quiz, HttpStatus.OK);
    }

    @GetMapping("/api/quizzes")
    public ResponseEntity<List<Quiz>> getQuizMap() {
        List<Quiz> quizList = new ArrayList<>(quizMap.values());
        return new ResponseEntity<>(quizList, HttpStatus.OK);
    }

    @PostMapping("/api/quizzes/{id}/solve")
    public ResponseEntity<QuizResult> postQuizzesIdSolve(
            @PathVariable int id,
            @RequestBody UserAnswer userAnswer
    ) {
        if (!quizMap.containsKey(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Quiz quiz = quizMap.get(id);
        Set<Integer> quizAnswer = quiz.getAnswer();
        QuizResult result;
        if (userAnswer.answer().equals(quizAnswer)) {
            result = new QuizResult(true, "Congratulations, you're right!");
        } else {
            result = new QuizResult(false, "Wrong answer! Please, try again.");
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
