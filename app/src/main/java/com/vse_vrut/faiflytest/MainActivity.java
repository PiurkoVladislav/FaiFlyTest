package com.vse_vrut.faiflytest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText mTextInputField;
    private Button mSplitButton;
    private TextView mSplitTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextInputField = (EditText) findViewById(R.id.text_input_field);
        mSplitButton = (Button) findViewById(R.id.split_button);
        mSplitTextView = (TextView) findViewById(R.id.split_text_view);
        mSplitTextView.setMovementMethod(new ScrollingMovementMethod());

    }


    public void onClickButtonSplit(View view) {
        String [] strings = mTextInputField.getText().toString().split(" +");
        StringBuilder splitText = new StringBuilder();

        for (int i = 0; i <strings.length; i++) {
            if ((i+1)%4 == 0){
                splitText.append(" "+strings[i]);
                splitText.append( System.getProperty("line.separator"));
            }else {
                splitText.append(" "+strings[i]);
            }

        }

        mSplitTextView.setText(splitText.toString());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("textView", mSplitTextView.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mSplitTextView.setText(savedInstanceState.getString("textView"));
    }
}
