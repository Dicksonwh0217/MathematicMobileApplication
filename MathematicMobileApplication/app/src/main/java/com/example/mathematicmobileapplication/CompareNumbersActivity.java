package com.example.mathematicmobileapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import java.util.Random;
import java.util.ArrayList;

public class CompareNumbersActivity extends AppCompatActivity {

    private TextView[] numberTexts = new TextView[2];
    private CardView[] numberCards = new CardView[2];
    private TextView resultText, instructionText;
    private Button nextButton, homeButton;
    private ImageButton tutorialButton;
    private int[] numbers = new int[2];
    private int correctAnswerIndex;
    private int score = 0;
    private int questionsAnswered = 0;
    private Random random = new Random();
    private Animation scaleAnimation;
    private int questionType; // 0 = find largest, 1 = find smallest
    private static final String TUTORIAL_VIDEO_URL = "https://www.youtube.com/watch?v=E34PAOGYRNk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_numbers);

        // Initialize views
        numberTexts[0] = findViewById(R.id.number1Text);
        numberTexts[1] = findViewById(R.id.number2Text);

        numberCards[0] = findViewById(R.id.number1Card);
        numberCards[1] = findViewById(R.id.number2Card);

        resultText = findViewById(R.id.resultText);
        instructionText = findViewById(R.id.instructionText);
        nextButton = findViewById(R.id.nextButton);
        homeButton = findViewById(R.id.homeButton);
        tutorialButton = findViewById(R.id.tutorialButton);

        TextView scoreText = findViewById(R.id.scoreText);
        scoreText.setText("Score: 0");

        // Load animations
        scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_animation);

        // Set up listeners for the number cards
        for (int i = 0; i < 2; i++) {
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

        // Set up tutorial button to open YouTube video
        tutorialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTutorialVideo();
            }
        });

        // Generate initial numbers
        generateNewQuestion();
    }

    private void openTutorialVideo() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(TUTORIAL_VIDEO_URL));
        intent.setPackage("com.google.android.youtube");

        // Try to start with YouTube app first
        try {
            startActivity(intent);
        } catch (Exception e) {
            // If YouTube app is not installed, open in browser instead
            intent.setPackage(null);
            startActivity(intent);
        }
    }

    private void generateNewQuestion() {
        // Generate 2 different random numbers between 1 and 999
        ArrayList<Integer> numbersList = new ArrayList<>();
        while (numbersList.size() < 2) {
            int num = random.nextInt(999) + 1;
            if (!numbersList.contains(num)) {
                numbersList.add(num);
            }
        }

        for (int i = 0; i < 2; i++) {
            numbers[i] = numbersList.get(i);
            numberTexts[i].setText(String.valueOf(numbers[i]));
        }

        // Randomly decide if we're looking for largest or smallest
        questionType = random.nextInt(2); // 0 or 1

        if (questionType == 0) {
            // Find largest
            instructionText.setText("Tap the LARGER number!");
            correctAnswerIndex = (numbers[0] > numbers[1]) ? 0 : 1;
        } else {
            // Find smallest
            instructionText.setText("Tap the SMALLER number!");
            correctAnswerIndex = (numbers[0] < numbers[1]) ? 0 : 1;
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