package org.example;


import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;


public class Main {

    private static int nextToken;

    private static int int_Lit = 0;
    private static int identifier = 1;
    private static int letter = 2;
    private static int digit = 3;
    private static int assignOp = 4;
    private static int addOp = 5;
    private static int subOp = 6;
    private static int multOp = 7;
    private static int divOp = 8;
    private static int leftParent = 9;
    private static int rightParent = 10;
    private static int end = 11;
    private static int IF = 12;
    private static int LOOP = 13;
    private static int unknown = 99;
    private static ArrayList<Character> turkishAlphabet = new ArrayList<>(Arrays.asList(
            'A', 'B', 'C', 'Ç', 'D', 'E', 'F', 'G', 'Ğ', 'H',
            'I', 'İ', 'J', 'K', 'L', 'M', 'N', 'O', 'Ö', 'P',
            'R', 'S', 'Ş', 'T', 'U', 'Ü', 'V', 'Y', 'Z',
            'a', 'b', 'c', 'ç', 'd', 'e', 'f', 'g', 'ğ', 'h',
            'i', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'ö', 'p',
            'r', 's', 'ş', 't', 'u', 'ü', 'v', 'y', 'z'
    ));
    private static ArrayList<Character> numbers = new ArrayList<>(Arrays.asList('1','2','3','4','5','6','7','8','9'));
    private static ArrayList<Integer> token = new ArrayList<>();
    private static ArrayList<String> value = new ArrayList<>();
    private static BufferedReader bufferedReader;

    static {
        try {
            bufferedReader = new BufferedReader(new FileReader(new File("src/main/resources/IDE")));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) {

        int counter = 0;
        while (lookup(getChar())!=end){
        }
        lexer();


    }

    private static void lexer(){
        for(int i = 0; i<token.size(); i++){


            // digit initialization analysis
            if (token.get(i) == digit) {
                StringBuilder stringBuilder = new StringBuilder();
                int u = 0;

                // merging characters to create whole new identifier or int_lit
                while (i + u < token.size()) {
                    if (token.get(i + u) == letter) {
                        // unknown addition according to syntax rule
                        for (int j = u; j >= 0; j--) {
                            token.remove(i + j);
                            value.remove(i + j);
                        }
                        for (int j = 0; j <= u; j++) {
                            token.add(i + j, unknown);
                            value.add(i + j, "?");
                        }
                        break;
                    } else if (token.get(i + u) == digit) {
                        stringBuilder.append(value.get(i + u));
                        u++;
                    } else {
                        // deletion backwards
                        for (int j = u - 1; j >= 0; j--) {
                            token.remove(i + j);
                            value.remove(i + j);
                        }
                        token.add(i, int_Lit);
                        value.add(i, stringBuilder.toString());
                        break;
                    }
                }
            }


            //letter initilizaton analysis
            else if (token.get(i) == letter) {
                StringBuilder stringBuilder = new StringBuilder();
                int u = 0;

                // merging characters to create whole new identifier or int_lit
                while (i + u < token.size() && (token.get(i + u) == digit || token.get(i + u) == letter)) {
                    stringBuilder.append(value.get(i + u));
                    u++;
                }

                // Removing characters from the list for avoiding index shifts
                for (int j = u - 1; j >= 0; j--) {
                    token.remove(i + j);
                    value.remove(i + j);
                }

                // adding new characters
                token.add(i, identifier);
                value.add(i, stringBuilder.toString());
            }

        }


        //unknown setter
        for (int i = 0; i<token.size(); i++) {
            if(token.get(i)==unknown){
                for (int u = 0; u<token.size()-i; u++){
                    token.set(i+u,unknown);
                    value.set(i+u,"?");
                }
            }
        }

        //customizable if and while keys
        for (int i = 0; i<token.size(); i++) {
            if(token.get(i)==identifier&&value.get(i).equals("velevKi")){
                token.set(i, IF);
            }
            else if(token.get(i)==identifier&&value.get(i).equals("döngü")){
                token.set(i, LOOP);
            }
        }

        //console
        for (int i = 0; i<token.size(); i++) {
            System.out.println("TOKEN KEY:  " + token.get(i)+ "  VALUE:  " + value.get(i));
        }
    }



    private static int lookup(char ch) {
        switch (ch) {
            case '(':
                nextToken = leftParent;
                addToken(ch);
                break;
            case ')':
                nextToken = rightParent;
                addToken(ch);
                break;
            case '+':
                nextToken = addOp;
                addToken(ch);
                break;
            case '-':
                nextToken = subOp;
                addToken(ch);
                break;
            case '*':
                nextToken = multOp;
                addToken(ch);
                break;
            case '/':
                nextToken = divOp;
                addToken(ch);
                break;
            case '=':
                nextToken = assignOp;
                addToken(ch);
                break;

        }
        if (turkishAlphabet.contains(ch)){
            nextToken = letter;
            addToken(ch);
        }
        else if (numbers.contains(ch)){
            nextToken = digit;
            addToken(ch);
        }
        else if (ch!='/'&&ch!='*'&&ch!='-'&&ch!='+'&&ch!=')'&&ch!='('){
            nextToken = end;
        }
        return nextToken;

    }


    public static Character getChar(){
        try {
            return (char)bufferedReader.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addToken(Character character){
        token.add(nextToken);
        value.add(String.valueOf(character));
    }


}