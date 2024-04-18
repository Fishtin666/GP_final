package com.example.gproject;



import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import androidx.core.app.ActivityCompat;

import com.microsoft.cognitiveservices.speech.audio.AudioStreamFormat;
import com.microsoft.cognitiveservices.speech.audio.PullAudioInputStreamCallback;


/**
 * MicrophoneStream exposes the Android Microphone as an PullAudioInputStreamCallback
 * to be consumed by the Speech SDK.
 * It configures the microphone with 16 kHz sample rate, 16 bit samples, mono (single-channel).
 */
public class MicrophoneStream extends PullAudioInputStreamCallback {
    private final static int SAMPLE_RATE = 16000;
    private final AudioStreamFormat format;   //音訊格式
    private Context context;
    private AudioRecord recorder;



    public MicrophoneStream() {
        this.context = context;
        this.format = AudioStreamFormat.getWaveFormatPCM(SAMPLE_RATE, (short) 16, (short) 1);
        this.initMic();
    }




    public AudioStreamFormat getFormat() {
        return this.format;
    }

    //讀取錄音數據並將其傳回
    @Override
    public int read(byte[] bytes) {
        if (this.recorder != null) {
            long ret = this.recorder.read(bytes, 0, bytes.length);
            return (int) ret;
        }
        return 0;
    }

    //釋放AudioRecord資源
    @Override
    public void close() {
        this.recorder.release();
        this.recorder = null;
    }

    //檢查是否有錄音權限
//    private void initMic() {
//        // Note: currently, the Speech SDK support 16 kHz sample rate, 16 bit samples, mono (single-channel) only.
//
//        //指定音訊的格式和參數
//        AudioFormat af = new AudioFormat.Builder()
//                .setSampleRate(SAMPLE_RATE)  //音訊採樣率
//                .setEncoding(AudioFormat.ENCODING_PCM_16BIT)  //音訊的編碼格式
//                .setChannelMask(AudioFormat.CHANNEL_IN_MONO)    //音訊的聲道數
//                .build();   //建立物件
//
//        //檢查應用程式是否擁有錄音權限，如果沒有錄音權限，則返回，不繼續進行錄音初始化。
//        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//
//        this.recorder = new AudioRecord.Builder()
//                .setAudioSource(MediaRecorder.AudioSource.VOICE_RECOGNITION)
//                .setAudioFormat(af)
//                .build();
//
//        //開始從麥克風錄製音訊
//        this.recorder.startRecording();
//    }
    private void initMic() {
        // Note: currently, the Speech SDK support 16 kHz sample rate, 16 bit samples, mono (single-channel) only.
        AudioFormat af = new AudioFormat.Builder()
                .setSampleRate(SAMPLE_RATE)
                .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                .setChannelMask(AudioFormat.CHANNEL_IN_MONO)
                .build();
        this.recorder = new AudioRecord.Builder()
                .setAudioSource(MediaRecorder.AudioSource.VOICE_RECOGNITION)
                .setAudioFormat(af)
                .build();

        this.recorder.startRecording();
    }
}

