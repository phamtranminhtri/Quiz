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

//    private final Map<Integer, Quiz> quizMap = new HashMap<>();
    private final QuizRepository quizRepository;

    public QuizController(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    @PostMapping("/api/quizzes")
    public ResponseEntity<Quiz> postQuizzes(@Valid @RequestBody Quiz quiz) {
        quizRepository.save(quiz);
        return new ResponseEntity<>(quiz, HttpStatus.OK);
    }

    @GetMapping("/api/quizzes/{id}")
    public ResponseEntity<Quiz> getQuizzesId(@PathVariable int id) {
        Optional<Quiz> quiz = quizRepository.findById(id);
        if (quiz.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(quiz.get(), HttpStatus.OK);
    }

    @GetMapping("/api/quizzes")
    public ResponseEntity<List<Quiz>> getQuizMap() {
        Iterable<Quiz> quizIterable = quizRepository.findAll();
        List<Quiz> quizList = new ArrayList<>();
        quizIterable.forEach(quizList::add);
        return new ResponseEntity<>(quizList, HttpStatus.OK);
    }

    @PostMapping("/api/quizzes/{id}/solve")
    public ResponseEntity<QuizResult> postQuizzesIdSolve(
            @PathVariable int id,
            @RequestBody UserAnswer userAnswer
    ) {
        Optional<Quiz> quiz = quizRepository.findById(id);
        if (quiz.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Set<Integer> quizAnswer = quiz.get().getAnswer();
        QuizResult result;
        if (userAnswer.answer().equals(quizAnswer)) {
            result = new QuizResult(true, "Congratulations, you're right!");
        } else {
            result = new QuizResult(false, "Wrong answer! Please, try again.");
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
