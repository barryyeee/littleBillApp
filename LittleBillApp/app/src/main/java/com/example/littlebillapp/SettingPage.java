package com.example.littlebillapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingPage extends Activity {
    private String budget_number="0.00";
    private ImageButton back;
    private EditText et_budget;
    private  List<CostBean> allCost;
    private CornerListView list1,list2,list3;
    private SimpleAdapter adapter1,adapter2,adapter3;
    private TextView spending,earning,budget,overdraft;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        allCost=(List<CostBean>)getIntent().getSerializableExtra("cost_data");
        getList1Data();
        getList2Data();
        getList3Data();
        initView();
        initAdapter();
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.custom_dialog);
        final AlertDialog dialog = builder.create();

        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

        View view = View.inflate(this, R.layout.dialog, null);
        dialog.setView(view, 0, 0, 0, 0);
        dialog.setCancelable(false);

        ImageButton btnCancel = view.findViewById(R.id.dialog_close);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();// 隐藏dialog
            }
        });
        dialog.show();
    }

    private void showBillDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.custom_dialog);
        final AlertDialog dialog = builder.create();

        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

        View view = View.inflate(this, R.layout.bill_dialog, null);
        dialog.setView(view, 0, 0, 0, 0);
        dialog.setCancelable(false);

        spending=view.findViewById(R.id.tv_spending);
        earning=view.findViewById(R.id.tv_earning);
        budget=view.findViewById(R.id.tv_budget);
        overdraft=view.findViewById(R.id.tv_overdraft);

        Double spend_money=getIntent().getDoubleExtra("spending",0);
        Double earn_money=getIntent().getDoubleExtra("earning",0);
        Double budeget_money=Double.parseDouble(budget_number);
        DecimalFormat df = new DecimalFormat("######0.00");
        spending.setText("Spending: "+df.format(spend_money));
        earning.setText("Earning: "+df.format(earn_money));
        budget.setText("Budget: "+df.format(budeget_money));
        if (spend_money>budeget_money){
            Double over_money=spend_money-budeget_money;
            overdraft.setText("Overdraft: "+df.format(over_money));
        }

        Button detail=view.findViewById(R.id.detail_bt);
        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent=new Intent();
                intent.setClass(SettingPage.this,DetailPage.class);
                startActivity(intent);
                SettingPage.this.finish();*/
                showDetail();
            }
        });

        ImageButton btnCancel = view.findViewById(R.id.bill_close);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showDetail(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.custom_dialog);
        final AlertDialog dialog = builder.create();

        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

        View view = View.inflate(this, R.layout.detail_page, null);
        dialog.setView(view, 0, 0, 0, 0);
        dialog.setCancelable(false);

        ListView mListView=view.findViewById(R.id.listview);
        initListView(mListView);

        ImageButton btnCancel = view.findViewById(R.id.detailpage_back);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();// 隐藏dialog
            }
        });
        dialog.show();
    }

    private void initListView(ListView mListView){
        SimpleAdapter simpleAdapter=new SimpleAdapter(this,getListData(),R.layout.detail_item, new String[]{"list_name","list_date","list_money"},new int[]{R.id.listview_item_name,R.id.listview_item_time,R.id.listview_item_money});
        mListView.setAdapter(simpleAdapter);
    }

    private List<Map<String,Object>> getListData() {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map=null;
        int index=0;
        String[][] costData=new String[100][3];
        if (allCost!=null){
            for (int i=0;i<allCost.size();i++){
                CostBean costBean=allCost.get(i);
                String costDate=costBean.costDate;
                String costMoney=costBean.costMoney;
                String costKind=costBean.costTitle;
                costData[i][0]=costKind;
                costData[i][1]=costDate;
                costData[i][2]=costMoney;
                index++;
            }
        }
        for (int i=0;i<index;i++) {
            map = new HashMap<>();
            Double money=Double.parseDouble(costData[i][2]);
            DecimalFormat df = new DecimalFormat("0.00");

            map.put("list_name", costData[i][0]);
            map.put("list_date", costData[i][1]);
            map.put("list_money",df.format(money));
            list.add(map);
        }
        return list;
    }

    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.custom_dialog);
        final AlertDialog dialog = builder.create();

        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

        View view = View.inflate(this, R.layout.about_dialog, null);
        dialog.setView(view, 0, 0, 0, 0);
        dialog.setCancelable(false);

        ImageButton btnCancel = view.findViewById(R.id.about_close);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();// 隐藏dialog
            }
        });
        dialog.show();
    }

    private void showBudgetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.custom_dialog);
        final AlertDialog dialog = builder.create();

        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

        View view = View.inflate(this, R.layout.budget_dialog, null);
        dialog.setView(view, 0, 0, 0, 0);
        dialog.setCancelable(false);

        budget_number=" ";
        et_budget =view.findViewById(R.id.budget_text);
        et_budget.setText(budget_number);
        Button clear=view.findViewById(R.id.clear_button);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_budget.setText("");
            }
        });

        Button ok=view.findViewById(R.id.ok_button);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                budget_number= et_budget.getText().toString();
                dialog.dismiss();
            }
        });

        ImageButton btnCancel = view.findViewById(R.id.budget_close);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();// 隐藏dialog
            }
        });
        dialog.show();
    }

    private void initView(){
        setContentView(R.layout.setting_page);

        list1=findViewById(R.id.settings_list1);
        list2=findViewById(R.id.settings_list2);
        list3=findViewById(R.id.settings_list3);

        list1.setOnItemClickListener(new List1OnItemListSelectedListener());
        list2.setOnItemClickListener(new List2OnItemListSelectedListener());
        list3.setOnItemClickListener(new List3OnItemListSelectedListener());

        back=findViewById(R.id.settingpage_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(SettingPage.this,RecordPage.class);
                startActivity(intent);
                SettingPage.this.finish();
            }
        });

    }

    private void initAdapter(){
        adapter1=new SimpleAdapter(this,getList1Data(),R.layout.list1_item, new String[]{"list1_image","list1_title","list1_arrow"},new int[]{R.id.list1_image,R.id.list1_name,R.id.list1_arrow});
        list1.setAdapter(adapter1);

        adapter2=new SimpleAdapter(this,getList2Data(),R.layout.list2_item, new String[]{"list2_image","list2_title","list2_arrow"}, new int[]{R.id.list2_image,R.id.list2_name,R.id.list2_arrow});
        list2.setAdapter(adapter2);

        adapter3=new SimpleAdapter(this,getList3Data(),R.layout.list3_item, new String[]{"list3_image","list3_title","list3_arrow"}, new int[]{R.id.list3_image,R.id.list3_name,R.id.list3_arrow});
        list3.setAdapter(adapter3);
    }

    private class List1OnItemListSelectedListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0:
                    showBillDialog();
                    break;
                default:
                    break;
            }
        }
    }

    private class List2OnItemListSelectedListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0:
                    showBudgetDialog();
                    break;
                default:
                    break;
            }
        }
    }

    private class List3OnItemListSelectedListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0:
                    showDialog();
                    break;
                case 1:
                    showDialog();
                    break;
                case 2:
                    showAboutDialog();
                    break;
                default:
                    break;
            }
        }
    }

    private List<Map<String,Object>> getList1Data() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list1_image", R.drawable.ic_budget);
        map.put("list1_title", "My Bill");
        map.put("list1_arrow", R.drawable.ic_arrow_forward);
        list.add(map);
        return list;
    }

    private List<Map<String,Object>> getList2Data() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list2_image",R.drawable.ic_currency);
        map.put("list2_title","Budget");
        map.put("list2_arrow",R.drawable.ic_arrow_forward);
        list.add(map);
        return list;
    }

    private List<Map<String,Object>> getList3Data() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list3_image",R.drawable.ic_feedback);
        map.put("list3_title","Feedback");
        map.put("list3_arrow",R.drawable.ic_arrow_forward);
        list.add(map);

        map=new HashMap<>();
        map.put("list3_image",R.drawable.ic_support);
        map.put("list3_title","Support");
        map.put("list3_arrow",R.drawable.ic_arrow_forward);
        list.add(map);

        map=new HashMap<>();
        map.put("list3_image", R.drawable.ic_about);
        map.put("list3_title","About us");
        map.put("list3_arrow",R.drawable.ic_arrow_forward);
        list.add(map);
        return list;
    }
}