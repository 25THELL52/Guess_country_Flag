package com.example.guesscountry;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.os.Build;
import android.os.CountDownTimer;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

public class MediaManager {

    private static MediaPlayer _mediaPlayer;
    private static Context _context;
    private static int _pausedAt;
    private static CountDownTimer _countDownTimer;

    public static Bitmap GetBitmapFromAssets(Context c, String filename)
    {
        AssetManager am = c.getAssets();
        InputStream is = null;
        try
        {
            is = am.open(filename);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return BitmapFactory.decodeStream(is);
    }

    public static void InitMediaPlayer(Context c)
    {
        _context = c;
        _mediaPlayer = new MediaPlayer();
    }
    public static void LoadMusic(int fileId)
    {
        try
        {

            _mediaPlayer.reset();
            AssetFileDescriptor afd = _context.getResources().openRawResourceFd(fileId);
            _mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getDeclaredLength());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                _mediaPlayer.setPlaybackParams(new PlaybackParams().setSpeed(2));
            }
            _mediaPlayer.prepare();
            afd.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(_context, "Couldn't load the music, please check your data folder.", Toast.LENGTH_SHORT).show();
        }
    }
    public static void PauseMusic()
    {
        try
        {
            if (_mediaPlayer != null && _mediaPlayer.isPlaying())
            {
                _pausedAt = _mediaPlayer.getCurrentPosition();
                _mediaPlayer.pause();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public static void PlayMusic()
    {
        try
        {
            if (_mediaPlayer != null && !_mediaPlayer.isPlaying()) _mediaPlayer.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public static void ResumeMusic()
    {
        try
        {
            if (_mediaPlayer != null && !_mediaPlayer.isPlaying())
            {
                _mediaPlayer.seekTo(_pausedAt);
                _mediaPlayer.start();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public static void StopMusic()
    {
        try
        {
            if (_mediaPlayer != null && _mediaPlayer.isPlaying()) _mediaPlayer.stop();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public static void ReleaseMusic()
    {
        try
        {
            if (_mediaPlayer != null) _mediaPlayer.release();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
