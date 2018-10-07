package kuckuck.de.statisticallydrinking;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class HitCounter extends AppCompatActivity {

    private String playerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hit_counter);
        playerName = "Me, Myself";

        Resources res = getResources();
        TypedArray buttons = res.obtainTypedArray(R.array.cup_buttons);
        for(int i = 0; i<getNumButtons() ;i++){
            final int cupNum = i;
            int buttonID = buttons.getResourceId(i,-1);
            final Button button = findViewById(buttonID);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        int[] count = getHitCount();
                        count[cupNum]++;
                        int cupCount = count[cupNum];
                        setHitCount(count);
                        button.setText(Integer.toString(cupCount));
                        calculateHitRate();
                    } catch (IOException e) {
                    }
                }
            });
            try {
                int[] count = getHitCount();
                int cupCount = count[cupNum];
                button.setText(Integer.toString(cupCount));
                calculateHitRate();
            } catch (IOException e) {
            }
        }
    }

    private void calculateHitRate() throws IOException {
        TextView statsView = findViewById(R.id.stats);
        int[] hitCounts = getHitCount();
        double hits = 0;
        for(int i=0; i<hitCounts.length-1;i++){
            hits += hitCounts[i];
        }
        double hitRate;
        if(hitCounts[hitCounts.length-1]+hits > 0)
            hitRate = hits/(hits+hitCounts[hitCounts.length-1]);
        else hitRate = 0;
        statsView.setText("Hitrate: "+ round(hitRate*100, 2) + "%");
    }

    private int[] getHitCount() throws IOException {
        File path = new File(getApplicationContext().getFilesDir(), getFileName());
        if(!path.exists())
            return new int[getNumButtons()];
        Gson gson = new Gson();
        int[] ret = gson.fromJson(getFileData(path), int[].class);
        if(ret == null)throw new IOException();
        return ret;
    }

    public void setHitCount(int[] hits) throws IOException {
        String filename = getFileName();
        Gson gson = new Gson();
        FileOutputStream outputStream = new FileOutputStream(
                getApplicationContext().getFilesDir() + File.separator + filename);
        outputStream.write(gson.toJson(hits).getBytes());
        outputStream.close();
    }

    private String getFileName() {
        return playerName+".data";
    }

    private static String getFileData(File file) throws IOException {
        StringBuilder text = new StringBuilder();

        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;

        while ((line = br.readLine()) != null) {
            text.append(line);
            text.append('\n');
        }
        br.close();

        return text.toString();
    }

    private int getNumButtons(){
        return getResources().obtainTypedArray(R.array.cup_buttons).length();
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
