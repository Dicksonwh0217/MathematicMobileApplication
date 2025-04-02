package com.example.mathematicmobileapplication;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class CompareNumbersActivity extends AppCompatActivity {

    private TextView number1Text, number2Text, resultText;
    private ImageView greaterButton, lessButton;
    private Button nextButton, homeButton;
    private int number1, number2;
    private int score = 0;
    private int questionsAnswered = 0;
    private Random random = new Random();
    private Animation scaleAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_numbers);

        // Initialize views
        number1Text = findViewById(R.id.number1Text);
        number2Text = findViewById(R.id.number2Text);
        resultText = findViewById(R.id.resultText);
        greaterButton = findViewById(R.id.greaterButton);
        lessButton = findViewById(R.id.lessButton);
        nextButton = findViewById(R.id.nextButton);
        homeButton = findViewById(R.id.homeButton);

        TextView scoreText = findViewById(R.id.scoreText);
        scoreText.setText("Score: 0");

        // Load animations
        scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_animation);

        // Set up listeners
        greaterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
                v.startAnimation(scaleAnimation);
            }
        });

        lessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
                v.startAnimation(scaleAnimation);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateNewNumbers();
                resultText.setText("");
                nextButton.setVisibility(View.INVISIBLE);
                greaterButton.setEnabled(true);
                lessButton.setEnabled(true);
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Generate initial numbers
        generateNewNumbers();
    }

    private void generateNewNumbers() {
        // Generate random numbers between 1 and 999
        number1 = random.nextInt(999) + 1;

        // Ensure numbers are different
        do {
            number2 = random.nextInt(999) + 1;
        } while (number1 == number2);

        number1Text.setText(String.valueOf(number1));
        number2Text.setText(String.valueOf(number2));
    }

    private void checkAnswer(boolean userSelectedFirstGreater) {
        boolean isCorrect = (number1 > number2 && userSelectedFirstGreater) ||
                (number1 < number2 && !userSelectedFirstGreater);

        if (isCorrect) {
            resultText.setText("Correct! Good job!");
            score++;
        } else {
            resultText.setText("Try again!");
        }

        questionsAnswered++;
        TextView scoreText = findViewById(R.id.scoreText);
        scoreText.setText("Score: " + score + "/" + questionsAnswered);

        // Disable buttons and show next button
        greaterButton.setEnabled(false);
        lessButton.setEnabled(false);
        nextButton.setVisibility(View.VISIBLE);
    }
}