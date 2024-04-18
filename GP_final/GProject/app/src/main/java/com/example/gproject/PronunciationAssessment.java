package com.example.gproject;



import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.gproject.Speaking.Speaking_part2_answer;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microsoft.cognitiveservices.speech.PronunciationAssessmentConfig;
import com.microsoft.cognitiveservices.speech.PronunciationAssessmentGradingSystem;
import com.microsoft.cognitiveservices.speech.PronunciationAssessmentGranularity;
import com.microsoft.cognitiveservices.speech.PronunciationAssessmentResult;
import com.microsoft.cognitiveservices.speech.PropertyId;
import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechRecognitionResult;
import com.microsoft.cognitiveservices.speech.SpeechRecognizer;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;

import java.util.Random;
import java.util.concurrent.Future;


public class PronunciationAssessment extends Activity {


    private static final String SpeechSubscriptionKey = "f8d71f0e97f6438db956ebf642eabf78";
    private static final String SpeechRegion = "eastus";

    // create config
    SpeechConfig speechConfig;


    TextView article;

    Button clear,change;


    ImageView help,home,pronunciationAssessmentFromStreamButton;

    String total_score,accuracy_score,completeness_score,fluency_score;

    String[] question={"We had a great time taking a long walk outside in the morning."
                        ,"It took me a long time to learn where he came from.",
                        "There was no possibility of taking a walk that day."};



    String referenceText;
    public static double Score;

    private MicrophoneStream microphoneStream;
    private MicrophoneStream createMicrophoneStream() {
        this.releaseMicrophoneStream();
        microphoneStream = new MicrophoneStream();
        //MicrophoneStream microphoneStream = new MicrophoneStream(this);

        return microphoneStream;
    }
    private void releaseMicrophoneStream() {
        if (microphoneStream != null) {
            microphoneStream.close();
            microphoneStream = null;
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pronunciation_assessment);


        pronunciationAssessmentFromStreamButton = findViewById(R.id.mic);
        article = findViewById(R.id.article1);
        help = findViewById(R.id.help);
        clear = findViewById(R.id.retry);
        change = findViewById(R.id.finish2);
        home = findViewById(R.id.home);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }

        setQuestion();




        try {
            // a unique number within the application to allow
            // correlating permission request responses with the request.
            int permissionRequestId = 5;

            // Request permissions needed for speech recognition
            ActivityCompat.requestPermissions(PronunciationAssessment.this, new String[]{RECORD_AUDIO, INTERNET, READ_EXTERNAL_STORAGE}, permissionRequestId);
        } catch (Exception ex) {
            Log.e("SpeechSDK", "could not init sdk, " + ex.toString());
            System.out.println("#Could not initialize: " + ex.toString());
        }



        try {
            speechConfig = SpeechConfig.fromSubscription(SpeechSubscriptionKey, SpeechRegion);
//            speechConfig.setOutputFormat(OutputFormat.Detailed);  //設定語音輸出的格式為詳細模式
//            speechConfig.requestWordLevelTimestamps();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            displayException(ex);
            return;
        }

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setQuestion();
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                article.setText(referenceText);
            }
        });




        ///////////////////////////////////////////////////
        // pronunciation assessment from stream
        ///////////////////////////////////////////////////





    }

    public void helpClick(View view){
        AlertDialog.Builder score_show = new AlertDialog.Builder(PronunciationAssessment.this);
        score_show.setTitle("分數意義");
        score_show.setCancelable(false);
        score_show.setMessage("【準確度分數】: 語音的發音正確性。指出與原生說話者的發音有多接近。\n" +
                "【完整度分數】: 語音完整性，由發音單字與輸入參考文字的比例計算。\n" +
                "【流暢度分數】: 給定演講的流暢性。 流暢度表示語音與原生說話者在單字之間無聲中斷的使用程度相吻合。\n" +
                "*其中以黃色螢光標記之單字被判定為發音不正確。");
        score_show.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // 確定按鈕的點擊處理
            }
        });
        score_show.show();
    }

    public void  setQuestion(){
        Random random = new Random();
        int randomIndex = random.nextInt(question.length);
        referenceText = question[randomIndex];
        article.setText(referenceText);
    }

    public void micClick(View view){
        final String logTag = "pron";

        if (this.microphoneStream != null) {
            //如果microphoneStream存在，就停止錄音
            this.releaseMicrophoneStream();
            return;
        }




        try {
            createMicrophoneStream();
            //AudioConfig:設定音訊輸入配置
            final AudioConfig audioConfig = AudioConfig.fromStreamInput(createMicrophoneStream());
            final SpeechRecognizer reco = new SpeechRecognizer(speechConfig, "en-US", audioConfig);





            String[] words = referenceText.split(" "); // 拆分文本成單詞
            SpannableString spannableString = new SpannableString(referenceText);


            PronunciationAssessmentConfig pronConfig =
                    new PronunciationAssessmentConfig(referenceText,
                            PronunciationAssessmentGradingSystem.HundredMark,
                            PronunciationAssessmentGranularity.Phoneme);

            pronConfig.applyTo(reco);


            Future<SpeechRecognitionResult> future = reco.recognizeOnceAsync();
            SpeechRecognitionResult speechRecognitionResult = future.get();//(30, TimeUnit.SECONDS);
            PronunciationAssessmentResult pronResult = PronunciationAssessmentResult.fromResult(speechRecognitionResult);
            total_score = String.valueOf(pronResult.getPronunciationScore());
            accuracy_score = String.valueOf(pronResult.getPronunciationScore());
            completeness_score = String.valueOf(pronResult.getCompletenessScore());
            fluency_score = String.valueOf(pronResult.getFluencyScore());

            //SpeechRecognitionResult result = reco.recognizeOnceAsync().get();
            //String pronunciationAssessmentResultJson =result.getProperties().getProperty(PropertyId.SpeechServiceResponse_JsonResult);
            //if (result.getReason() == ResultReason.RecognizedSpeech) {


            reco.recognized.addEventListener((o, speechRecognitionResultEventArgs) -> {
                //PronunciationAssessmentResult pronResult = PronunciationAssessmentResult.fromResult(speechRecognitionResultEventArgs.getResult());
//                    AppendTextLine("Accuracy score: " + pronResult.getAccuracyScore() +
//                            ";  pronunciation score: " +  pronResult.getPronunciationScore() +
//                            ", completeness score: " + pronResult.getCompletenessScore() +
//                            ", fluency score: " + pronResult.getFluencyScore(), false);

                String pronunciationAssessmentResultJson = speechRecognitionResult.getProperties().getProperty(PropertyId.SpeechServiceResponse_JsonResult);


                int i=0;
                int start=0;


                JsonParser jsonParser = new JsonParser();
                JsonElement jsonElement = jsonParser.parse(pronunciationAssessmentResultJson);
                if (jsonElement.isJsonObject()) {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    JsonArray nBestArray = jsonObject.getAsJsonArray("NBest");

                    for (JsonElement nBestElement : nBestArray) {
                        JsonObject nBestObject = nBestElement.getAsJsonObject();
                        JsonArray wordsArray = nBestObject.getAsJsonArray("Words");

                        for (JsonElement wordElement : wordsArray) {
                            JsonObject wordObject = wordElement.getAsJsonObject();
                            JsonObject pronunciationAssessment = wordObject.getAsJsonObject("PronunciationAssessment");

                            if (pronunciationAssessment != null && pronunciationAssessment.has("ErrorType")) {

                                String errorType = pronunciationAssessment.get("ErrorType").getAsString();
                                System.out.println("ErrorType: " + errorType);

                                int end = start + words[i].length();

                                //System.out.println(start + "," + end);

                                if (errorType.trim().equals("None")) {
                                    //spannableString.setSpan(new BackgroundColorSpan(Color.GREEN), start, end, 0);
                                } else if (errorType.trim().equals("Mispronunciation")) {
                                    spannableString.setSpan(new BackgroundColorSpan(Color.YELLOW), start, end, 0);
                                } else if (errorType.trim().equals("Omission")) {
                                    spannableString.setSpan(new BackgroundColorSpan(Color.GRAY), start, end, 0);
                                } else {
                                    spannableString.setSpan(new BackgroundColorSpan(Color.RED), start, end, 0);
                                }
                                start = end + 1;

                            }
                            i++;


                        }

                    }article.setText(spannableString);
                }

            });

            reco.sessionStopped.addEventListener((o, s) -> {
                Log.i(logTag, "Session stopped.");
                System.out.println("答案:"+total_score);
                System.out.println("答案:"+accuracy_score);
                System.out.println("答案:"+completeness_score);
                System.out.println("答案:"+fluency_score);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder score_show = new AlertDialog.Builder(PronunciationAssessment.this);
                        score_show.setTitle("發音評定結果");
                        score_show.setCancelable(false);
                        score_show.setMessage("總分: " + total_score + "\n準確度分數: " + accuracy_score
                                + "\n完整度分數: " + completeness_score + "\n流暢度分數: " + fluency_score);
                        score_show.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // 確定按鈕的點擊處理
                            }
                        });
                        score_show.show();
                    }
                });

                reco.stopContinuousRecognitionAsync();

                this.releaseMicrophoneStream();
                //enableButtons();

            });

            //PronunciationAssessmentResult pronResult = PronunciationAssessmentResult.fromResult(speechRecognitionResult);
            //AppendTextLine("發音評定結果: " , false);
            //AppendTextLine("準確度分數: " + pronResult.getAccuracyScore() + "\n" +
            //"發音分數: " + pronResult.getPronunciationScore() + "\n" +
            // "完整度分數: " + pronResult.getCompletenessScore() + "\n" +
            //"流暢度分數: " + pronResult.getFluencyScore(), false);
            //total_score.setText(String.valueOf(pronResult.getPronunciationScore()));
            //accuracyScore.setText(String.valueOf(pronResult.getAccuracyScore()));
            //completenessScore.setText(String.valueOf(pronResult.getCompletenessScore()));
            //fluencyScore.setText(String.valueOf(pronResult.getFluencyScore()));

//                reco.stopContinuousRecognitionAsync();
//                this.releaseMicrophoneStream();




            // }

            reco.recognizeOnceAsync();
            //result.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            displayException(ex);
        }


    }



    private void displayException(Exception ex) {
        System.out.println("失敗因為:"+ex.getMessage() + System.lineSeparator() + TextUtils.join(System.lineSeparator(), ex.getStackTrace()));
    }


    public void homeClick(View v){
        Intent intent =new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    public void Previous_btn_Click(View view){
        article.setText("It took me a long time to learn where he came from.");
    }

    public void Next_btn_Click(View view){
        article.setText("Question the plausibility of one explanation.");
    }



}

