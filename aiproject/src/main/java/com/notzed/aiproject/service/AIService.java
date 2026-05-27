package com.notzed.aiproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import org.springframework.ai.document.*;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AIService {

    private final VectorStore vectorStore;

    public void ingestDataToVectorStore() {
        List<Document> songs = List.of(
                new Document(
                        "A lonely soul wanders through a neon city at night, feeling emotionally disconnected, heartbroken, and invisible. The glowing lights feel cold and distant, mirroring the emptiness and sadness inside. Every street corner reminds them of someone they lost, leaving them feeling hollow and melancholic.",
                        Map.of("title", "Neon Afterglow", "genre", "Synthwave", "year", 2024)
                ),
                new Document(
                        "A tired, lost dreamer drives alone on empty roads with no destination, questioning the purpose of life. Feeling hopeless, directionless, and emotionally exhausted, they search for meaning but find nothing. The vast empty highway reflects their inner emptiness and desire to escape their painful reality.",
                        Map.of("title", "Dust and Denim", "genre", "Country", "year", 2023)
                ),
                new Document(
                        "Two childhood friends hold onto the last moments of a beautiful summer that is ending forever. There is a deep sadness in letting go, growing apart, and losing innocent joy. The feeling of nostalgia, bittersweet memories, and the pain of things that cannot last fills every moment.",
                        Map.of("title", "Paper Boats", "genre", "Indie Folk", "year", 2022)
                ),
                new Document(
                        "A young person from a poor neighborhood fights every day against struggle, injustice, and self-doubt. Despite feeling overlooked and underestimated, they push forward with ambition and raw determination. The streets are harsh and unforgiving, but their hunger for success and a better life keeps them going.",
                        Map.of("title", "Concrete Kingdom", "genre", "Hip-Hop", "year", 2025)
                ),
                new Document(
                        "A rebellious spirit refuses to be caged, breaking free from rules, expectations, and a suffocating life. Feeling alive, reckless, and unstoppable, they chase freedom at full speed through storms and darkness. The adrenaline of rebellion and the desire to live without limits drives every reckless decision.",
                        Map.of("title", "Crimson Horizon", "genre", "Rock", "year", 2021)
                ),
                new Document(
                        "A heartbroken person sits alone in silence, overwhelmed by sadness and grief after losing someone they deeply loved. They feel emotionally shattered, unable to move on, and consumed by painful memories of a lost relationship. The quiet of the night amplifies their loneliness, sorrow, and desire to give up on love.",
                        Map.of("title", "Velvet Silence", "genre", "Jazz", "year", 2020)
                ),
                new Document(
                        "Hundreds of people come together on a dance floor, losing themselves in music and forgetting their worries. The energy is electric, euphoric, and liberating as heavy beats erase stress, anxiety, and loneliness. In this moment of unity and rhythm, everyone feels free, alive, and connected to something bigger than themselves.",
                        Map.of("title", "Binary Heartbeat", "genre", "EDM", "year", 2024)
                ),
                new Document(
                        "Two people deeply in love write letters to each other through long cold winters and painful separations. Despite loneliness, heartache, and the passage of time, their love refuses to fade or break. The sadness of being apart is softened by the hope and warmth of knowing someone still cares and remembers.",
                        Map.of("title", "Winter Letters", "genre", "Acoustic Ballad", "year", 2019)
                ),
                new Document(
                        "Two lovers are separated by thousands of miles, living in different worlds and missing each other desperately. The pain of distance, longing, and not being able to hold the person you love is unbearable. Every airport goodbye, every silent night alone, and every unanswered call deepens the heartbreak and emotional exhaustion.",
                        Map.of("title", "Ocean Between Us", "genre", "Pop", "year", 2023)
                ),
                new Document(
                        "A spiritual seeker disconnected from modern life performs ancient rituals to reconnect with forgotten roots and inner peace. Feeling lost, purposeless, and spiritually empty in the modern world, they turn to ancestral wisdom for healing. The fire, drums, and darkness awaken something deep inside, bringing clarity and a sense of belonging.",
                        Map.of("title", "Firelight Ritual", "genre", "Alternative", "year", 2022)
                )
        );
        vectorStore.add(songs);
    }

    public List<Document> similaritySearch(String text){
        return vectorStore.similaritySearch(SearchRequest.builder()
                        .query(text)
                        .topK(2)
                        .similarityThreshold(0.7)
                .build());
    }
}