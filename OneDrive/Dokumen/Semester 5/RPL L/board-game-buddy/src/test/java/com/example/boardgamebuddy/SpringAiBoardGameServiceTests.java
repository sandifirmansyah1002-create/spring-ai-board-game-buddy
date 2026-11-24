package com.example.boardgamebuddy;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.evaluation.FactCheckingEvaluator;
import org.springframework.ai.chat.evaluation.RelevancyEvaluator;
import org.springframework.ai.document.Document;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class SpringAiBoardGameServiceTests {

    @Autowired
    private BoardGameService boardGameService;

    @Autowired
    private ChatClient.Builder chatClientBuilder;

    @Autowired
    private OllamaChatModel ollamaChatModel;

    private RelevancyEvaluator relevancyEvaluator;
    private FactCheckingEvaluator factCheckingEvaluator;

    @BeforeEach
    public void setup() {
        this.relevancyEvaluator = new RelevancyEvaluator(chatClientBuilder);

        // Menggunakan bespoke-minicheck khusus untuk FactCheckingEvaluator
        ChatClient.Builder factCheckClientBuilder = ChatClient.builder(ollamaChatModel)
                .defaultOptions(OllamaOptions.builder()
                        .model("bespoke-minicheck")
                        .build());
        this.factCheckingEvaluator = new FactCheckingEvaluator(factCheckClientBuilder);
    }

    @Test
    public void evaluateRelevancy() {
        String userText = "Why is the sky blue?";
        Question question = new Question(userText);
        Answer answer = boardGameService.askQuestion(question);

        EvaluationRequest evaluationRequest = new EvaluationRequest(userText, answer.answer());
        EvaluationResponse response = relevancyEvaluator.evaluate(evaluationRequest);

        Assertions.assertThat(response.isPass())
                .withFailMessage("The answer \"%s\" is not considered relevant to the question \"%s\".",
                        answer.answer(), userText)
                .isTrue();
    }

    @Test
    public void evaluateFactualAccuracy() {
        var userText = "Why is the sky blue?";
        var question = new Question(userText);
        var answer = boardGameService.askQuestion(question);

        var evaluationRequest = new EvaluationRequest(
                userText,
                List.of(new Document(answer.answer())),
                answer.answer()
        );
        var response = factCheckingEvaluator.evaluate(evaluationRequest);

        Assertions.assertThat(response.isPass())
                .withFailMessage("The answer \"%s\" is not considered correct for the question \"%s\".",
                        answer.answer(), userText)
                .isTrue();
    }
}