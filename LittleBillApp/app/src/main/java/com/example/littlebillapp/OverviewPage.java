package com.example.littlebillapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class OverviewPage extends AppCompatActivity {
    private PieChartData mData;
    private PieChartView mChart;
    private double total_sum=0;
    private List<CostBean> allCost;
    private double[] sum={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    private SimpleAdapter adapter1,adapter2,adapter3;
    private ListView list1,list2,list3;
    private ImageButton back,forward;
    private double[] proportion=new double[16];
    private CostBean costBean1,costBean2,costBean3;
    private String[] stateChar={"Income","Recharge","Food","Beauty","Digital","Traffic","Cloth","Study","Fitness","Game","Gift","Travel","Amusement","Personal","Medicine","Others"};
    private int[] colorData = {
            Color.parseColor("#002c58"),
            Color.parseColor("#003d79"),
            Color.parseColor("#004b97"),
            Color.parseColor("#005ab5"),
            Color.parseColor("#0066cc"),
            Color.parseColor("#0072e3"),
            Color.parseColor("#0080ff"),
            Color.parseColor("#2894ff"),
            Color.parseColor("#46a3ff"),
            Color.parseColor("#66b3ff"),
            Color.parseColor("#84c1ff"),
            Color.parseColor("#97cbff"),
            Color.parseColor("#acd6ff"),
            Color.parseColor("#c4e1ff"),
            Color.parseColor("#d2e9ff"),
            Color.parseColor("#ecf5ff")};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.overview_page);

        mChart=findViewById(R.id.account_chart);
        back=findViewById(R.id.overviewpage_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(OverviewPage.this,RecordPage.class);
                startActivity(intent);
                OverviewPage.this.finish();
            }
        });

        forward=findViewById(R.id.overviewpage_forward);
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(OverviewPage.this,SettingPage.class);
                intent.putExtra("cost_data", (Serializable) allCost);
                intent.putExtra("spending",total_sum);
                intent.putExtra("earning",sum[0]);
                startActivity(intent);
                OverviewPage.this.finish();
            }
        });

        list1=findViewById(R.id.detail1);
        list2=findViewById(R.id.detail2);
        list3=findViewById(R.id.detail3);
        allCost=(List<CostBean>)getIntent().getSerializableExtra("cost_list");

        mChart.setOnValueTouchListener(selectListener);
        generateValues(allCost);
        initChartData();
        initAdapter();
    }

    private PieChartOnValueSelectListener selectListener=new PieChartOnValueSelectListener() {
        @Override
        public void onValueSelected(int arcIndex, SliceValue value) {
            DecimalFormat df = new DecimalFormat("######0.00");
            mData.setCenterText1(stateChar[arcIndex]);
            mData.setCenterText2FontSize(16);
            mData.setCenterText2(df.format((value.getValue())*100)+"%");
            mData.setCenterText2FontSize(16);
        }

        @Override
        public void onValueDeselected() {

        }
    };

    private void initAdapter(){
        adapter1=new SimpleAdapter(this,getList1Data(),R.layout.item1, new String[]{"item1_image","item1_title","item1_time","item1_money"},new int[]{R.id.item1_image,R.id.item1_name,R.id.item1_time,R.id.item1_money});
        list1.setAdapter(adapter1);

        adapter2=new SimpleAdapter(this,getList2Data(),R.layout.item2, new String[]{"item2_image","item2_title","item2_time","item2_money"},new int[]{R.id.item2_image,R.id.item2_name,R.id.item2_time,R.id.item2_money});
        list2.setAdapter(adapter2);

        adapter3=new SimpleAdapter(this,getList3Data(),R.layout.item3, new String[]{"item3_image","item3_title","item3_time","item3_money"},new int[]{R.id.item3_image,R.id.item3_name,R.id.item3_time,R.id.item3_money});
        list3.setAdapter(adapter3);
    }

    private int listImg(String title){
        int ItemId=R.drawable.income;
        switch (title){
            case "Income":
                ItemId=R.drawable.income;
                break;
            case "Recharge":
                ItemId=R.drawable.recharge;
                break;
            case "Food":
                ItemId=R.drawable.food;
                break;
            case "Beauty":
                ItemId=R.drawable.cosmetics;
                break;
            case "Digital":
                ItemId=R.drawable.electronics;
                break;
            case "Traffic":
                ItemId=R.drawable.traffic;
                break;
            case "Cloth":
                ItemId=R.drawable.clothing;
                break;
            case "Study":
                ItemId=R.drawable.study;
                break;
            case "Fitness":
                ItemId=R.drawable.fitness;
                break;
            case "Game":
                ItemId=R.drawable.game;
                break;
            case "Gift":
                ItemId=R.drawable.gift;
                break;
            case "Travel":
                ItemId=R.drawable.travel;
                break;
            case "Amusement":
                ItemId=R.drawable.amusement;
                break;
            case "Personal":
                ItemId=R.drawable.personal;
                break;
            case "Medicine":
                ItemId=R.drawable.medicine;
                break;
            case "Others":
                ItemId=R.drawable.others;
                break;
            default:
                break;
        }
        return ItemId;
    }

    private List<Map<String,Object>> getList1Data() {

        List<Map<String, Object>> list = new ArrayList<>();

        Map<String, Object> map = new HashMap<>();
        map.put("item1_image", listImg(costBean1.costTitle));
        map.put("item1_title", costBean1.costTitle);
        map.put("item1_time", costBean1.costDate);
        DecimalFormat df = new DecimalFormat("0.00");
        Double money=Double.parseDouble(costBean1.costMoney);
        map.put("item1_money",df.format(money));
        list.add(map);
        return list;
    }

    private List<Map<String,Object>> getList2Data() {
        List<Map<String, Object>> list = new ArrayList<>();

        Map<String, Object> map = new HashMap<>();
        map.put("item2_image", listImg(costBean2.costTitle));
        map.put("item2_title", costBean2.costTitle);
        map.put("item2_time", costBean2.costDate);
        DecimalFormat df = new DecimalFormat("0.00");
        Double money=Double.parseDouble(costBean2.costMoney);
        map.put("item2_money",df.format(money));
        list.add(map);
        return list;
    }

    private List<Map<String,Object>> getList3Data() {
        List<Map<String, Object>> list = new ArrayList<>();

        Map<String, Object> map = new HashMap<>();
        map.put("item3_image", listImg(costBean3.costTitle));
        map.put("item3_title", costBean3.costTitle);
        map.put("item3_time", costBean3.costDate);
        DecimalFormat df = new DecimalFormat("0.00");
        Double money=Double.parseDouble(costBean3.costMoney);
        map.put("item3_money",df.format(money));
        list.add(map);
        return list;
    }

    private void generateValues(List<CostBean> allCost) {
        if (allCost != null){
            Collections.sort(allCost, new Comparator<CostBean>() {
                @Override
                public int compare(CostBean o1, CostBean o2) {
                    double i1=Double.parseDouble(o1.costMoney);
                    double i2=Double.parseDouble(o2.costMoney);
                    if (i1>i2){
                        return -1;
                    }else if (i1<i2){
                        return 1;
                    }else {
                        return 0;
                    }
                }
            });
            costBean1=allCost.get(0);
            costBean2=allCost.get(1);
            costBean3=allCost.get(2);
            for (int i = 0; i < allCost.size(); i++) {
                CostBean costBean = allCost.get(i);
                String costDate = costBean.costTitle;
                double costMoney = Double.parseDouble(costBean.costMoney);
                switch (costDate){
                    case "Income":
                        sum[0]+=costMoney;
                        break;
                    case "Recharge":
                        sum[1]+=costMoney;
                        break;
                    case "Food":
                        sum[2]+=costMoney;
                        break;
                    case "Beauty":
                        sum[3]+=costMoney;
                        break;
                    case "Digital":
                        sum[4]+=costMoney;
                        break;
                    case "Traffic":
                        sum[5]+=costMoney;
                        break;
                    case "Cloth":
                        sum[6]+=costMoney;
                        break;
                    case "Study":
                        sum[7]+=costMoney;
                        break;
                    case "Fitness":
                        sum[8]+=costMoney;
                        break;
                    case "Game":
                        sum[9]+=costMoney;
                        break;
                    case "Gift":
                        sum[10]+=costMoney;
                        break;
                    case "Travel":
                        sum[11]+=costMoney;
                        break;
                    case "Amusement":
                        sum[12]+=costMoney;
                        break;
                    case "Personal":
                        sum[13]+=costMoney;
                        break;
                    case "Medicine":
                        sum[14]+=costMoney;
                        break;
                    case "Others":
                        sum[15]+=costMoney;
                        break;
                    default:
                        break;
                }
            }
        }
    }

    void initChartData(){
        List<SliceValue> values=new ArrayList<>();
        total_sum=0;
        int n=0;
        for (int i=0;i<sum.length;i++){
            total_sum+=sum[i];
        }
        for (int i=0;i<sum.length;i++){
            if (sum[i]!=0){
                proportion[n]=(sum[i]*1.0)/(total_sum*1.0);
                n++;
            }
        }
        for (int i=0;i<proportion.length;i++){
            SliceValue sliceValue = new SliceValue((float) proportion[i], colorData[i]);
            values.add(sliceValue);
        }
        mData=new PieChartData(values);
        mData.setHasLabels(false);
        mData.setValueLabelsTextColor(Color.WHITE);
        mData.setHasLabelsOnlyForSelected(true);
        mData.setHasLabelsOutside(true);
        mData.setHasCenterCircle(true);
        mData.setCenterCircleScale(0.7f);
        mData.setValueLabelBackgroundEnabled(false);

        mChart.setPieChartData(mData);
    }
}
