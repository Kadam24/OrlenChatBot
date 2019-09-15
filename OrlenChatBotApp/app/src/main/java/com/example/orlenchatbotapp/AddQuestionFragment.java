package com.example.orlenchatbotapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


public class AddQuestionFragment extends Fragment {

    private Button add;
    private Button cancel;
    private String q;
    private String a;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_add_to_db, container, false);
        final EditText question = root.findViewById(R.id.question);
        final EditText answer = root.findViewById(R.id.answer);
        add = root.findViewById(R.id.add_q);
        cancel = root.findViewById(R.id.cancel);
        Bundle args = getArguments();
        a = (String) args
                .getSerializable(a);
        q = (String) args
                .getSerializable(q);

        question.setText(q);
        answer.setText(a);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add to db
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cofanie
            }
        });

        question.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                q = s.toString();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                q = s.toString();
            }
        });

        answer.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                a = s.toString();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                a = s.toString();
            }
        });

        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}