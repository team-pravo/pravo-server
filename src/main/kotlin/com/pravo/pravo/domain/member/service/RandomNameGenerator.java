package com.pravo.pravo.domain.member.service;

import com.pravo.pravo.domain.member.repository.MemberRepository;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class RandomNameGenerator {

    private static final String ADJECTIVE_FILE = "src/main/kotlin/com/pravo/pravo/domain/member/resources/adjective.txt";
    private static final String NOUN_FILE = "src/main/kotlin/com/pravo/pravo/domain/member/resources/noun.txt";
    private final MemberRepository memberRepository;
    //    private final Random random;

    public RandomNameGenerator(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public String generateUniqueRandomName() {
        String nickname = "";
        do {
            try {
                Resource adjectiveResource = new FileSystemResource(ADJECTIVE_FILE);
                Resource nounResource = new FileSystemResource(NOUN_FILE);

                ArrayList<String> index = readFile(adjectiveResource);
                ArrayList<String> index2 = readFile(nounResource);

                int ran1max = index.size();
                int ran2max = index2.size();
                Random random = new Random();
                int ran = random.nextInt(ran1max);
                int ran2 = random.nextInt(ran2max);

                nickname =
                    index.get(ran) + " " + index2.get(ran2) + " " + (100 + random.nextInt(900));
            } catch (IOException e) {
                System.out.println("파일을 읽는 중 오류가 발생했습니다: " + e.getMessage());
                e.printStackTrace();
            }
            System.out.println("nickname: " + nickname);
        } while (memberRepository.existsByName(nickname));
        return nickname;
    }

    private static ArrayList<String> readFile(Resource resource) throws IOException {
        ArrayList<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
            resource.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }


}