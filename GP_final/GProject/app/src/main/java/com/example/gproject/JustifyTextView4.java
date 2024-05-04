package com.example.gproject;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.speech.tts.TextToSpeech;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gproject.dictionary.MeaningAdapter;
import com.example.gproject.dictionary.RetrofitInstance;
import com.example.gproject.dictionary.WordResult;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

public class JustifyTextView4 extends AppCompatTextView {
    private Layout mLayout;
    private int mLineY;
    private int mViewWidth;
    public static final String TWO_CHINESE_BLANK = " ";
    Boolean indic=true;
    TextToSpeech tts;

    MeaningAdapter adapter;
    PopupWindow popupWindow;


    public JustifyTextView4(Context context, AttributeSet attrs) {
        super(context, attrs);
        int paddingPx = dpToPx(context, 40); // Convert dp to pixels
        setPadding(0, 0, 0, paddingPx);
        initializeTextToSpeech(context);
    }

    private void initializeTextToSpeech(Context context) {
        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.UK);
                } else {
                    Log.e("TTS", "TextToSpeech initialization failed");
                }
            }
        });
    }



    public static int dpToPx(Context context, float dpValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * density + 0.5f); // Cast the result to int
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mLayout = getLayout();
        if (mLayout == null) return;
        TextPaint paint = getPaint();
        paint.drawableState = getDrawableState();
        //mViewWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight(); // 計算可用於繪製文本的寬度，扣除了左右的內邊距
        mViewWidth=getMeasuredWidth()-76;
        String text = getText().toString();
        mLineY = 0;
        mLineY += getTextSize() + 38;
        Layout layout = getLayout();


        if (layout == null) {
            return;
        }

        Paint.FontMetrics fm = paint.getFontMetrics();

        int textHeight = (int) (Math.ceil(fm.descent - fm.ascent));
        textHeight = (int) (textHeight * layout.getSpacingMultiplier() + layout
                .getSpacingAdd());
        //解决了最后一行文字间距过大的问题
        for (int i = 0; i < layout.getLineCount(); i++) {
            int lineStart = layout.getLineStart(i);
            int lineEnd = layout.getLineEnd(i);
            float width = StaticLayout.getDesiredWidth(text, lineStart,
                    lineEnd, getPaint());
            String line = text.substring(lineStart, lineEnd);
            String line_new=line.replaceAll("[^a-zA-Z]", "");

            int customColor = Color.rgb(120, 59, 55);

            try {
                retrofit2.Call<List<WordResult>> call = RetrofitInstance.dictionaryApi.getMeaning(line_new);
                retrofit2.Response<List<WordResult>> response = call.execute();
                if (response.body() == null) {
                    indic = false;
                }else indic = true;

            } catch (Exception e) {
                //Toast.makeText(getApplicationContext(), "錯誤:"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            if(indic){
                //paint.setColor(customColor);
                paint.setFakeBoldText(true);  // 设置为粗体

            }else{
                //paint.setColor(Color.BLACK);
            }
            System.out.println(paint.getColor());


            if (i < layout.getLineCount() - 1) {
                if (needScale(line)) {
                    drawScaledText(canvas, lineStart, line, width,paint);
                } else {
                    canvas.drawText(line, getPaddingLeft()+38, mLineY, paint);

                }
            } else {

                canvas.drawText(line, getPaddingLeft()+38, mLineY, paint);

            }
            mLineY += textHeight;

        }

    }








    private void drawScaledText(Canvas canvas, int lineStart, String line, float lineWidth,Paint paint) {
        float x = 38;
        if (isFirstLineOfParagraph(lineStart, line)) {
            String blanks = " ";
            canvas.drawText(blanks, x, mLineY, paint);
            float bw = StaticLayout.getDesiredWidth(blanks, getPaint());
            x += bw;

            line = line.substring(3);
        }

        int gapCount = line.length() - 1;
        int i = 0;
        if (line.length() > 2 && line.charAt(0) == 12288
                && line.charAt(1) == 12288) {
            String substring = line.substring(0, 2);
            float cw = StaticLayout.getDesiredWidth(substring, getPaint());
            canvas.drawText(substring, x, mLineY, getPaint());
            x += cw;
            i += 2;
        }

        float d = (mViewWidth - lineWidth) / gapCount;
        for (; i < line.length(); i++) {
            String c = String.valueOf(line.charAt(i));
            float cw = StaticLayout.getDesiredWidth(c, getPaint());
            canvas.drawText(c, x, mLineY, getPaint());
            x += cw + d;
        }
    }



    private boolean isFirstLineOfParagraph(int lineStart, String line) {
        return line.length() > 3 && line.charAt(0) == ' '
                && line.charAt(1) == ' ';
    }

    private boolean needScale(String line) {
        if (line == null || line.length() == 0) {
            return false;
        } else {
            return line.charAt(line.length() - 1) != '\n';
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();

            int line = (int) Math.floor((y - 38) / getLineHeight());
            if (mLayout != null && line >= 0 && line < mLayout.getLineCount()) {
                int offset = mLayout.getOffsetForHorizontal(line, x - getPaddingLeft());
                CharSequence text = getText();
                if (offset >= 0 && offset < text.length()) {
                    int wordStart = findWordStart(text, offset);
                    int wordEnd = findWordEnd(text, offset);

                    if (wordStart != -1 && wordEnd != -1) {
                        CharSequence selectedWord = text.subSequence(wordStart, wordEnd);
                        String Word = selectedWord.toString();

                        // 使用正则表达式匹配只含有英文字母的部分
                        String word = Word.replaceAll("[^a-zA-Z]", "");
                        showToast(word.toString());



                        // 弹出 PopupWindow
                        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                        View popupView = inflater.inflate(R.layout.popup_layout, null);
                        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                        int height = 1200;
                        boolean focusable = true; // 让PopupWindow在失去焦点时自动关闭
                        popupWindow = new PopupWindow(popupView, width, height, focusable);


                        // 设置PopupWindow的内容
                        TextView voc = popupView.findViewById(R.id.Voc);
                        voc.setText(word);
                        TextView phonetic = popupView.findViewById(R.id.phonetics);
                        //voice
                        ImageButton voice = popupView.findViewById(R.id.voice);
                        voice.setImageResource(R.drawable.voice2);

                        voice.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                tts.speak(word, TextToSpeech.QUEUE_FLUSH, null);

                            }
                        });
                        //close
                        TextView close=popupView.findViewById(R.id.close);
                        close.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                popupWindow.dismiss();

                            }
                        });
                        //star
                        ImageView star = popupView.findViewById(R.id.star);
                        star.setImageResource(R.drawable.star_black);
                        star.setOnClickListener(new View.OnClickListener() {
                            boolean star_yellow=false;
                            @Override
                            public void onClick(View v) {
                                if (star_yellow) {
                                    star.setImageResource(R.drawable.star_black);
                                    star_yellow = false;
                                } else {
                                    star.setImageResource(R.drawable.star_yellow);
                                    star_yellow = true;
                                }


                            }
                        });
                        //dic adapter
                        RecyclerView meaningRecyclerView=popupView.findViewById(R.id.meaningRecyclerView);
                        adapter = new MeaningAdapter(Collections.emptyList());
                        meaningRecyclerView.setAdapter(adapter);
                        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
                        meaningRecyclerView.setLayoutManager(layoutManager);

                        getMeaning(word, phonetic);
                        //if(InDic(word))
                            //popupWindow.showAtLocation(this, Gravity.CENTER, 0, 0);


                        //if(InDic(word))

                        return true;
                    }
                }
            }
        }
        return super.onTouchEvent(event);
    }



    private int findWordStart(CharSequence text, int offset) {
        int start = offset;
        while (start > 0 && !Character.isWhitespace(text.charAt(start - 1))) {
            start--;
        }
        return start;
    }

    private int findWordEnd(CharSequence text, int offset) {
        int end = offset;
        int len = text.length();
        while (end < len && !Character.isWhitespace(text.charAt(end))) {
            end++;
        }
        return end;
    }

    private void showToast(String message) {
        Context context = getContext();
        if (context != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }



    private String[] getWordsList(String text) {
        StringTokenizer tokenizer = new StringTokenizer(text, " \t\n\r\f,.?!;:\"");
        int count = tokenizer.countTokens();
        String[] words = new String[count];
        int index = 0;
        while (tokenizer.hasMoreTokens()) {
            words[index++] = tokenizer.nextToken();
        }
        return words;
    }

    private  boolean InDic(String word){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    retrofit2.Call<List<WordResult>> call = RetrofitInstance.dictionaryApi.getMeaning(word);
                    retrofit2.Response<List<WordResult>> response = call.execute();



                    if (response.body() == null) {
                        indic = false;
                    }else indic = true;

                } catch (Exception e) {
                    //Toast.makeText(getApplicationContext(), "錯誤:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }).start();
        return indic;
    }

    private void getMeaning(String word, TextView phonetic) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    retrofit2.Call<List<WordResult>> call = RetrofitInstance.dictionaryApi.getMeaning(word);
                    retrofit2.Response<List<WordResult>> response = call.execute();

                    if (response.body() == null) {
                        throw new Exception();
                    }

                    // Update UI on the main (UI) thread
                    post(new Runnable() {
                        @Override
                        public void run() {
                            if (response.body() != null && !response.body().isEmpty()) {
                                setUI(response.body().get(0), phonetic);

//                                popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
                                popupWindow.showAtLocation(JustifyTextView4.this, Gravity.CENTER, 0, 0);
                                } else {
                                Toast.makeText(getContext(), "Failed: response body is null or empty", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (Exception e) {
                    // Handle exceptions on the main (UI) thread
                    post(new Runnable() {
                        @Override
                        public void run() {
                            if (word == null) {
                                Toast.makeText(getContext(), "No word selected", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Something went wrong: " + word, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        }).start();
    }
    private void setUI(WordResult response, TextView phonetic) {
        phonetic.setText(response.getPhonetic());
        adapter.updateNewData(response.getMeanings());
//        popupWindow.showAtLocation(this, Gravity.CENTER, 0, 0);
    }

    // 转换为普通的TextView
    public void  convertToNormalTextView() {
        // 获取当前TextView的内容和属性
        CharSequence originalText = getText();
        int originalGravity = getGravity();

        // 创建一个新的TextView，将原始内容和属性应用到新的TextView上
        TextView normalTextView = new TextView(getContext());
        normalTextView.setText(originalText);
        normalTextView.setGravity(originalGravity);

        // 将新的TextView替换当前的JustifyTextView4
        ViewGroup parentView = (ViewGroup) getParent();
        if (parentView != null) {
            int index = parentView.indexOfChild(this);
            parentView.removeView(this);
            parentView.addView(normalTextView, index);
        }
    }


}
