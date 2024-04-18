package com.example.gproject.Writing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.gproject.R;

public class Writing_T1answer2 extends AppCompatActivity {
    int QuesNum;

    TextView Ans;
    Bundle bundle=new Bundle();

    public static String PassAns;


    String ans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.writing_t1answer2);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }

       // QuesNum=getIntent().getIntExtra("QuesNum",1);
        Ans=findViewById(R.id.editText);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ans = extras.getString("Ans");



        }
        Ans.setText(ans);



        Ans.setText("The chart shows the percentages of paper and cardboard, glass containers, aluminium" +
                "cans and plastics that were recycled in one country between 1982 and 2010." +
                "In 1982, about 65% of paper and cardboard was recycled. This figure fluctuated before" +
                "rising steeply to reach a peak of 80% in 1994. From then on, however, it decreased" +
               "steadily to a level of 70% in 2010. In 1982, half of all glass containers were recycled;" +
               "after dipping to a low of 40% in 1990, the glass recycling rate gradually increased to 60%" +
                "by 2010.");

    }


    public void previousCLick(View v){
//        Intent intent=new Intent(this, Writing_T1answer1.class);
//        bundle.putInt("QuesNum",QuesNum);
//        bundle.putString("Ans",Ans.getText().toString());
//        startActivity(intent);
        PassAns = Ans.getText().toString();
        finish();
    }

    public void finishClick(View v){
        bundle.putString("Ans",Ans.getText().toString());
        Intent intent =new Intent(this, W_Judge_P1.class);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    public void homeClick(View v){
        Intent intent = new Intent(this,W_topic.class);
        startActivity(intent);
    }

}