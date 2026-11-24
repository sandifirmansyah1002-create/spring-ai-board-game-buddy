# Praktikum Spring AI: Membangun & Mengevaluasi Aplikasi AI (Board Game Buddy)

Repositori ini berisi implementasi latihan dari buku **Spring AI in Action** (**Chapter 1: Getting Started with Spring AI** dan **Chapter 2: Evaluating Generated Responses**). Proyek ini membangun layanan REST API tanya-jawab berbasis Spring Boot dan Spring AI yang terintegrasi dengan model LLM lokal melalui **Ollama**, dilengkapi pengujian otomatis menggunakan **WireMock**, **AI Evaluators**, serta mekanisme **Runtime Self-Evaluation** dengan **Spring Retry**.

---

## 🛠️ Teknologi & Lingkungan Pengembangan

* **Bahasa Pemrograman:** Java 21
* **Framework:** Spring Boot 3.5.5 & Spring AI 1.0.1
* **Build Tool:** Gradle
* **LLM Lokal (Ollama):**
    * `gemma:2b` — Model utama untuk menjawab pertanyaan dan `RelevancyEvaluator`.
    * `bespoke-minicheck` — Model khusus untuk evaluasi akurasi fakta (`FactCheckingEvaluator`).
* **Testing & Resilience:** JUnit 5, AssertJ, WireMock, Spring Retry
* **IDE:** IntelliJ IDEA

---

## 📂 Struktur Proyek

```text
board-game-buddy/
├── src/
│   ├── main/
│   │   ├── java/com/example/boardgamebuddy/
│   │   │   ├── BoardGameBuddyApplication.java       # Entry point aplikasi (@EnableRetry)
│   │   │   ├── Question.java                        # Record model untuk request pertanyaan
│   │   │   ├── Answer.java                          # Record model untuk response jawaban
│   │   │   ├── BoardGameService.java                # Interface layanan tanya-jawab
│   │   │   ├── SpringAiBoardGameService.java        # Implementasi dasar ChatClient (Chapter 1)
│   │   │   ├── SelfEvaluatingBoardGameService.java  # Implementasi evaluasi mandiri & retry (Chapter 2)
│   │   │   ├── AnswerNotRelevantException.java      # Custom exception jika jawaban tidak relevan
│   │   │   └── AskController.java                   # REST Controller endpoint POST /ask
│   │   └── resources/
│   │       └── application.properties               # Konfigurasi model Ollama
│   └── test/
│       ├── java/com/example/boardgamebuddy/
│       │   ├── SpringAiBoardGameServiceWireMockTests.java  # Unit test menggunakan WireMock (Chapter 1)
│       │   └── SpringAiBoardGameServiceTests.java          # Integration test Evaluator (Chapter 2)
│       └── resources/
│           └── test-ollama-response.json            # Mock response JSON untuk pengujian WireMock
└── build.gradle