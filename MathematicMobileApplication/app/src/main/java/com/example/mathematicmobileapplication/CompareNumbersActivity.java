package com.example.mathematicmobileapplication;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import java.util.Random;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;

public class CompareNumbersActivity extends AppCompatActivity {

    private TextView[] numberTexts = new TextView[4];
    private CardView[] numberCards = new CardView[4];
    private TextView resultText, instructionText;
    private Button nextButton, homeButton;
    private int[] numbers = new int[4];
    private int correctAnswerIndex;
    private int score = 0;
    private int questionsAnswered = 0;
    private Random random = new Random();
    private Animation scaleAnimation;
    private int questionType; // 0 = find largest, 1 = find smallest

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_numbers);

        // Initialize views
        numberTexts[0] = findViewById(R.id.number1Text);
        numberTexts[1] = findViewById(R.id.number2Text);
        numberTexts[2] = findViewById(R.id.number3Text);
        numberTexts[3] = findViewById(R.id.number4Text);

        numberCards[0] = findViewById(R.id.number1Card);
        numberCards[1] = findViewById(R.id.number2Card);
        numberCards[2] = findViewById(R.id.number3Card);
        numberCards[3] = findViewById(R.id.number4Card);

        resultText = findViewById(R.id.resultText);
        instructionText = findViewById(R.id.instructionText);
        nextButton = findViewById(R.id.nextButton);
        homeButton = findViewById(R.id.homeButton);

        TextView scoreText = findViewById(R.id.scoreText);
        scoreText.setText("Score: 0");

        // Load animations
        scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_animation);

        // Set up listeners for the number cards
        for (int i = 0; i < 4; i++) {
            final int cardIndex = i;
            numberCards[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.startAnimation(scaleAnimation);
                    checkAnswer(cardIndex);
                }
            });
        }

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetCardColors();
                generateNewQuestion();
                resultText.setText("");
                nextButton.setVisibility(View.INVISIBLE);
                enableAllCards(true);
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Generate initial numbers
        generateNewQuestion();
    }

    private void generateNewQuestion() {
        // Generate 4 random numbers between 1 and 999
        ArrayList<Integer> numbersList = new ArrayList<>();
        while (numbersList.size() < 4) {
            int num = random.nextInt(999) + 1;
            if (!numbersList.contains(num)) {
                numbersList.add(num);
            }
        }

        for (int i = 0; i < 4; i++) {
            numbers[i] = numbersList.get(i);
            numberTexts[i].setText(String.valueOf(numbers[i]));
        }

        // Randomly decide if we're looking for largest or smallest
        questionType = random.nextInt(2); // 0 or 1

        if (questionType == 0) {
            // Find largest
            instructionText.setText("Tap the LARGEST number!");
            int max = numbers[0];
            correctAnswerIndex = 0;
            for (int i = 1; i < 4; i++) {
                if (numbers[i] > max) {
                    max = numbers[i];
                    correctAnswerIndex = i;
                }
            }
        } else {
            // Find smallest
            instructionText.setText("Tap the SMALLEST number!");
            int min = numbers[0];
            correctAnswerIndex = 0;
            for (int i = 1; i < 4; i++) {
                if (numbers[i] < min) {
                    min = numbers[i];
                    correctAnswerIndex = i;
                }
            }
        }
    }

    private void checkAnswer(int selectedCardIndex) {
        boolean isCorrect = (selectedCardIndex == correctAnswerIndex);

        if (isCorrect) {
            resultText.setText("Correct! Good job!");
            score++;
            highlightCard(numberCards[selectedCardIndex], true);
        } else {
            resultText.setText("Try again!");
            highlightCard(numberCards[selectedCardIndex], false);
        }

        questionsAnswered++;
        TextView scoreText = findViewById(R.id.scoreText);
        scoreText.setText("Score: " + score + "/" + questionsAnswered);

        // Disable cards and show next button
        enableAllCards(false);
        nextButton.setVisibility(View.VISIBLE);
    }

    private void highlightCard(CardView card, boolean isCorrect) {
        if (isCorrect) {
            card.setCardBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        } else {
            card.setCardBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        }
    }

    private void resetCardColors() {
        for (CardView card : numberCards) {
            card.setCardBackgroundColor(getResources().getColor(android.R.color.white));
        }
    }

    private void enableAllCards(boolean enable) {
        for (CardView card : numberCards) {
            card.setEnabled(enable);
        }
    }
}