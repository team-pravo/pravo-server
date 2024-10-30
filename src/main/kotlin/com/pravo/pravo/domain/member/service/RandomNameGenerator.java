package com.pravo.pravo.domain.member.service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class RandomNameGenerator {

    //    private final MemberRepository memberRepository;
//    private final Random random;
    private static final String ADJECTIVE_FILE = "adjective.txt";
    private static final String NOUN_FILE = "../resources/noun.txt";

    public String generateUniqueRandomName() {
        String nickname = "";
        try {
            // 파일 읽기
            ArrayList<String> index = readFile(
                "E:\\code\\uos\\pravo-server\\src\\main\\kotlin\\com\\pravo\\pravo\\domain\\member\\service\\adjective.txt");
            ArrayList<String> index2 = readFile(
                "E:\\code\\uos\\pravo-server\\src\\main\\kotlin\\com\\pravo\\pravo\\domain\\member\\resources\\noun.txt");

            int ran1max = index.size();
            int ran2max = index2.size();
            Random random = new Random();
            Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8.name());

            System.out.println("뭔가 이상한 닉네임 짓기 프로그램에 오신것을 환영합니다. \n생성을 시작하겠습니다.\n");

            int ran = random.nextInt(ran1max);
            int ran2 = random.nextInt(ran2max);

            nickname = index.get(ran) + " " + index2.get(ran2);

            scanner.close();

        } catch (IOException e) {
            System.out.println("파일을 읽는 중 오류가 발생했습니다: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("nickname: " + nickname);
        return nickname;
    }

    private static ArrayList<String> readFile(String filename) throws IOException {
        ArrayList<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
            new FileInputStream(filename), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }


}