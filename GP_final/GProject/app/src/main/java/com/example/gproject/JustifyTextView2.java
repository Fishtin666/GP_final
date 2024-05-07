package com.example.gproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.example.gproject.dictionary.RetrofitInstance;
import com.example.gproject.dictionary.WordResult;

import java.util.List;

public class JustifyTextView2 extends AppCompatTextView {
    private Layout mLayout;
    private int mLineY;
    private int mViewWidth;
    Boolean indic=true;
    public static final String TWO_CHINESE_BLANK = " ";

    public JustifyTextView2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    public static int dpToPx(Context context, float dpValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * density + 0.5f); // Cast the result to int
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
//                if (response.body() == null) {
//                    indic = false;
//                }else indic = true;

            } catch (Exception e) {
                //Toast.makeText(getApplicationContext(), "錯誤:"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }

//            if(indic){
//                //paint.setColor(customColor);
//                paint.setFakeBoldText(true);  // 设置为粗体
//
//            }else{
//                paint.setColor(Color.BLACK);
//            }
//            System.out.println(paint.getColor());


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
}