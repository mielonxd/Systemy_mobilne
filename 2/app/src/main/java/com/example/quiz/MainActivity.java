package com.example.quiz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button trueButton;
    private Button falseButton;
    private Button nextButton;
    private Button hintButton;
    private TextView questionTextView;
    private int CurrentIndex = 0;
    private static final String KEY_CURRENT_INDEX = "CurrentIndex";
    public static final String KEY_EXTRA_ANSWER = "com.example.quiz.CorrectAnswer";
    private static final int REQUEST_CODE_PROMPT = 0;
    private boolean answerWasShown;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.d("MyActivity","Wywołana została metoda cyklu życia: onCreate");
        setContentView(R.layout.activity_main);
        if(savedInstanceState != null){
            CurrentIndex = savedInstanceState.getInt(KEY_CURRENT_INDEX);
        }

        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        nextButton = findViewById(R.id.next_button);
        questionTextView = findViewById(R.id.question_text_view);
        hintButton = findViewById(R.id.hint_button);

        trueButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                checkAnswerCorrectness(true);
            }
        });
        falseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                checkAnswerCorrectness(false);
            }
        });
        nextButton.setOnClickListener((v) -> {
                CurrentIndex = (CurrentIndex + 1) % questions.length;
                answerWasShown = false;
                setNextQuestion();
        });
        setNextQuestion();
        hintButton.setOnClickListener( (v) -> {
            Intent intent = new Intent(MainActivity.this, PromptActivity.class);
            boolean correctAnswer = questions[CurrentIndex].isTrueAnswer();
            intent.putExtra(KEY_EXTRA_ANSWER, correctAnswer);
            startActivityForResult(intent,REQUEST_CODE_PROMPT);
        });

    }
    @Override
    protected void onStart(){
        super.onStart();
        Log.d("MyActivity", "onStart: ");
    }
    @Override
    protected void onResume(){
        super.onResume();
        Log.d("MyActivity", "onResume: ");
    }
    @Override
    protected void onPause(){
        super.onPause();
        Log.d("MyActivity", "onPause: ");
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.d("MyActivity", "onStop: ");
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d("MyActivity", "onDestroy: ");
    }
    private Question[] questions = new Question[]{
            new Question(R.string.q_student, true),
            new Question(R.string.q_slon, true),
            new Question(R.string.q_danie, true),
            new Question(R.string.q_math, false),
            new Question(R.string.q_nauka, true)
    };
    private void checkAnswerCorrectness(boolean userAnswer){
        boolean correctAnswer = questions[CurrentIndex].isTrueAnswer();
        int resultMessageId = 0;
        if(answerWasShown){
            resultMessageId = R.string.answer_was_shown;
        } else{
            if (userAnswer == correctAnswer) {
                resultMessageId = R.string.correct_answer;
            } else {
                resultMessageId = R.string.incorrect_answer;
            }
        }
        Toast.makeText(this, resultMessageId, Toast.LENGTH_SHORT).show();
    }
    private void setNextQuestion(){
        questionTextView.setText(questions[CurrentIndex].getQuestionId());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("MyActivity", "onSaveInstanceState: ");
        outState.putInt(KEY_CURRENT_INDEX, CurrentIndex);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK){ return;}
        if(requestCode == REQUEST_CODE_PROMPT){
            if(data == null){ return; }
            answerWasShown = data.getBooleanExtra(PromptActivity.KEY_EXTRA_ANSWER_SHOWN,false);
        }
    }
}