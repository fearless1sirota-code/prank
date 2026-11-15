package com.prank;
import android.app.Activity;
import android.graphics.*;
import android.media.ToneGenerator;
import android.media.AudioManager;
import android.os.*;
import android.view.*;

public class MainActivity extends Activity {
    private ToneGenerator tg;
    private Handler h = new Handler();
    private EyeView eye;

    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (Build.VERSION.SDK_INT >= 27) { setShowWhenLocked(true); setTurnScreenOn(true); }
        eye = new EyeView(this);
        setContentView(eye);
        try {
            tg = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
            tg.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 10000);
        } catch (Exception e) {}
        h.postDelayed(() -> { eye.text = "Я ТЕБЯ\nВЗЛОМАЛ"; eye.invalidate(); }, 1500);
        h.postDelayed(() -> { eye.text = "ТЕБЯ\nЗАМЕТИЛИ"; eye.invalidate(); }, 4000);
        h.postDelayed(() -> { eye.text = "НЕ ПЫТАЙСЯ\nЗАКРЫТЬ"; eye.invalidate(); }, 7000);
    }
    @Override public void onBackPressed() {}
    @Override public boolean onKeyDown(int k, KeyEvent e) {
        if (k == KeyEvent.KEYCODE_VOLUME_DOWN || k == KeyEvent.KEYCODE_VOLUME_UP) return true;
        return super.onKeyDown(k, e);
    }
    @Override protected void onDestroy() { if (tg != null) tg.release(); super.onDestroy(); }

    class EyeView extends View {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        String text = "";
        EyeView(Activity a) { super(a); setBackgroundColor(Color.BLACK); }
        @Override protected void onDraw(Canvas c) {
            super.onDraw(c);
            float cx = getWidth()/2f, cy = getHeight()/2f;
            p.setColor(Color.RED); p.setStyle(Paint.Style.STROKE); p.setStrokeWidth(8);
            c.drawOval(cx-200, cy-120, cx+200, cy+120, p);
            p.setStyle(Paint.Style.FILL); p.setColor(Color.RED);
            c.drawCircle(cx, cy, 70, p);
            p.setColor(Color.WHITE); c.drawCircle(cx-25, cy-25, 18, p);
            if (!text.isEmpty()) {
                p.setColor(Color.GREEN); p.setTextSize(64); p.setTextAlign(Paint.Align.CENTER);
                String[] ls = text.split("\n"); float y = cy + 260;
                for (String l : ls) { c.drawText(l, cx, y, p); y += 80; }
            }
            invalidate();
        }
    }
}
