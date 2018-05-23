package com.polimi.insolesNoAPI.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.polimi.insolesNoAPI.R;
import com.polimi.insolesNoAPI.logic.insoles.InsolesManager;
import java.util.ArrayList;
import java.util.List;

public class DataPreviewActivity extends AppCompatActivity {
    private static final String TAG = DataPreviewActivity.class.getSimpleName();
    private static DataPreviewActivity self;

    private static final String SESSION_ID  = "sessionID";

    private GraphView mGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_preview);
        self = this;

        TextView tvTotTime = (TextView)findViewById(R.id.totTimeView);
        TextView tvAvgDiff = (TextView)findViewById(R.id.avgDiffView);
        TextView tvAvgDiffL = (TextView)findViewById(R.id.avgDiffViewL);
        TextView tvAvgDiffR = (TextView)findViewById(R.id.avgDiffViewR);
        mGraph = (GraphView) findViewById(R.id.graph);

        long sessionID = getIntent().getLongExtra(SESSION_ID, -100);

        InsolesManager insolesManager = new InsolesManager();
        List<String> rawDataList = insolesManager.getRawData(this, sessionID);
        List<Double> timeStampList = new ArrayList<>();
        List<Double> timeStampListL = new ArrayList<>();
        List<Double> timeStampListR = new ArrayList<>();
        for(String rawdata: rawDataList){
            List<Double> rowDataList = String2LongList(rawdata);
            timeStampList.add(rowDataList.get(0));
            if(rowDataList.get(1) != 0.0){
                timeStampListL.add(rowDataList.get(0));
            }
            if(rowDataList.get(2) != 0.0){
                timeStampListR.add(rowDataList.get(0));
            }
        }
        double totalTime = 0.0;
        totalTime = (timeStampList.get(timeStampList.size()-1) - timeStampList.get(0))/1000;

        tvTotTime.setText(String.format("Total time: %.2f seconds", totalTime));//(strTmp);

        tvAvgDiff.setText(String.format("Average Hz: %.2f Hz", 1000/AverageTimeDiff(timeStampList)));

        tvAvgDiffL.setText(String.format("Left Hz: %.2f Hz", 1000/AverageTimeDiff(timeStampListL)));

        tvAvgDiffR.setText(String.format("Right Hz: %.2f Hz", 1000/AverageTimeDiff(timeStampListR)));

        LineGraphSeries<DataPoint> series_diff = new LineGraphSeries<>();
        for(int i = 1; i!= timeStampList.size();i++){
            series_diff.appendData(new DataPoint((timeStampList.get(i)-timeStampList.get(0))/1000,timeStampList.get(i) - timeStampList.get(i-1)),true,timeStampList.size()-1);
        }
        mGraph.removeAllSeries();
        mGraph.addSeries(series_diff);
        mGraph.setTitle("Time gaps in second");
        //mGraph.setTitleTextSize(40);
    }

    private  List<Double> String2LongList(String strOrg){
        List<Double> rowData = new ArrayList<>();
        String[] separated = strOrg.split(",");
        for(String s: separated){
            if (!s.trim().isEmpty()) {
                rowData.add(Double.valueOf(s));
            }else{
                rowData.add(-500.0);
            }
        }
        return rowData;
    }

    private double AverageTimeDiff(List<Double> timeList){
        double avgDiff = 0.0;
        double sumTimeDiff = 0.0;
        int szTimeList = timeList.size();
        for(int i = 1; i!=szTimeList; i++){
            sumTimeDiff += (timeList.get(i) - timeList.get(i-1));
        }
        avgDiff = sumTimeDiff/(szTimeList -1);
        return avgDiff;
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    @Override
    protected void onResume(){
        super.onResume();
    }
}
