package com.example.littlebillapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RecordPage extends AppCompatActivity {
    private List<CostBean> mCostBeanList;
    private DatabaseHelper mDatabaseHelper;
    int width;
    int screenWidth;
    private TextView mItem_name;
    private ImageView mItem_icon;
    private EditText money_number;
    private Button add_bt,delete_bt;
    private StringBuilder money=new StringBuilder();
    private int signal=0;//为0 时表示刚输入状态；为1 时表示当前在输出结果上继续输入
    private ImageButton chart,back;
    private PageGridView pageGridView;
    private PageIndicator pageIndicator;
    private int index=0;

    List<String> data=new ArrayList<>();
    {
        String item_context[]={"Income","Recharge","Food","Beauty","Digital","Traffic","Cloth","Study","Fitness","Game","Gift","Travel","Amusement","Personal","Medicine","Others"};
        for (int i=0;i<16;i++){
            data.add(item_context[i]);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.record_page);

        mItem_icon=findViewById(R.id.item_kind);
        mItem_name=findViewById(R.id.item_name);
        money_number=findViewById(R.id.account_number);

        mDatabaseHelper=new DatabaseHelper(this);
        mCostBeanList=new ArrayList<>();

        pageIndicator= findViewById(R.id.pageIndicator);
        width = getResources().getDisplayMetrics().widthPixels / 4;
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        pageGridView = findViewById(R.id.pagingGridView);

        MyAdapter adapter = new MyAdapter(data);
        pageGridView.setAdapter(adapter);
        pageGridView.setOnItemClickListener(adapter);
        pageGridView.setPageIndicator(pageIndicator);

        initCostData();
        initButton();
        initKeyboard();
    }

    private void initCostData() {
        Cursor cursor=mDatabaseHelper.getAllCostData();
        if(cursor !=null)
        {
            while(cursor.moveToNext())
            {
                CostBean costBean=new CostBean();
                costBean.costTitle=cursor.getString(cursor.getColumnIndex("cost_title"));
                costBean.costDate=cursor.getString(cursor.getColumnIndex("cost_date"));
                costBean.costMoney=cursor.getString(cursor.getColumnIndex("cost_money"));
                mCostBeanList.add(costBean);
            }
            cursor.close();
        }
    }

    private void initButton(){
        back=findViewById(R.id.recordpage_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(RecordPage.this,LoginPage.class);
                startActivity(intent);
                RecordPage.this.finish();
            }
        });

        chart=findViewById(R.id.recordpage_forward);
        chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent=new Intent();
                intent.setClass(RecordPage.this,OverviewPage.class);
                intent.putExtra("cost_list",(Serializable) mCostBeanList);
                startActivity(intent);
                RecordPage.this.finish();
            }
        });

        add_bt=findViewById(R.id.finish_bt);
        add_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddAccount();
            }
        });

        delete_bt=findViewById(R.id.delete_bt);
        delete_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.custom_dialog);
        final AlertDialog dialog = builder.create();

        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

        View view = View.inflate(this, R.layout.delete_dialog, null);
        dialog.setView(view, 0, 0, 0, 0);
        dialog.setCancelable(false);

        Button btnCancel = view.findViewById(R.id.noDelete_button);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();// 隐藏dialog
            }
        });

        Button btnDelete = view.findViewById(R.id.yesDelete_button);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseHelper.deleteAllData();
                mCostBeanList.clear();
                dialog.dismiss();// 隐藏dialog
            }
        });
        dialog.show();
    }

    private void AddAccount(){
        CostBean costBean = new CostBean();
        DatePicker new_date=new DatePicker(RecordPage.this);
        costBean.costTitle = mItem_name.getText().toString();
        costBean.costMoney = money_number.getText().toString();
        costBean.costDate = new_date.getYear()+"/";
        if(new_date.getMonth()+1<10)   costBean.costDate+="0";
        costBean.costDate+=(new_date.getMonth()+1)+"/";
        if(new_date.getDayOfMonth()<10)    costBean.costDate+="0";
        costBean.costDate+=new_date.getDayOfMonth();
        if(money_number.getText().toString()=="0")
        {
            Toast.makeText(RecordPage.this,"Please Enter Your Sum",Toast.LENGTH_SHORT).show();
            return;
        }
        mDatabaseHelper.insertCost(costBean);
        mCostBeanList.add(costBean);
        index++;
        money_number.setText("0");
        money=new StringBuilder();
    }

    private void initKeyboard(){
        money=new StringBuilder();
        money_number.setCursorVisible(true);

        Button zero=findViewById(R.id.num0);
        Button one=findViewById(R.id.num1);
        Button two=findViewById(R.id.num2);
        Button three=findViewById(R.id.num3);
        Button four=findViewById(R.id.num4);
        Button five=findViewById(R.id.num5);
        Button six=findViewById(R.id.num6);
        Button seven=findViewById(R.id.num7);
        Button eight=findViewById(R.id.num8);
        Button nine=findViewById(R.id.num9);
        ImageButton delete=findViewById(R.id.delete);
        Button point=findViewById(R.id.point);

        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(money.toString().equals("0"))){
                    if(signal==0){
                        money.append("0");
                        money_number.setText(money);
                        money_number.setSelection(money_number.getText().length());
                    }else{
                        money.delete(0,money.length());
                        money.append("0");
                        money_number.setText(money);
                        money_number.setSelection(money_number.getText().length());
                        signal=0;
                    }
                }
            }
        });

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(signal==0){
                    money.append("1");
                    money_number.setText(money);
                    money_number.setSelection(money_number.getText().length());
                }else{
                    money.delete(0,money.length());
                    money.append("1");
                    money_number.setText(money);
                    money_number.setSelection(money_number.getText().length());
                    signal=0;
                }
            }
        });

        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(signal==0){
                    money.append("2");
                    money_number.setText(money);
                    money_number.setSelection(money_number.getText().length());
                }else{
                    money.delete(0,money.length());
                    money.append("2");
                    money_number.setText(money);
                    money_number.setSelection(money_number.getText().length());
                    signal=0;
                }
            }
        });

        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(signal==0){
                    money.append("3");
                    money_number.setText(money);
                    money_number.setSelection(money_number.getText().length());
                }else{
                    money.delete(0,money.length());
                    money.append("3");
                    money_number.setText(money);
                    money_number.setSelection(money_number.getText().length());
                    signal=0;
                }
            }
        });

        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(signal==0){
                    money.append("4");
                    money_number.setText(money);
                    money_number.setSelection(money_number.getText().length());
                }else{
                    money.delete(0,money.length());
                    money.append("4");
                    money_number.setText(money);
                    money_number.setSelection(money_number.getText().length());
                    signal=0;
                }
            }
        });

        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(signal==0){
                    money.append("5");
                    money_number.setText(money);
                    money_number.setSelection(money_number.getText().length());
                }else{
                    money.delete(0,money.length());
                    money.append("5");
                    money_number.setText(money);
                    money_number.setSelection(money_number.getText().length());
                    signal=0;
                }
            }
        });

        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(signal==0){
                    money.append("6");
                    money_number.setText(money);
                    money_number.setSelection(money_number.getText().length());
                }else{
                    money.delete(0,money.length());
                    money.append("6");
                    money_number.setText(money);
                    money_number.setSelection(money_number.getText().length());
                    signal=0;
                }
            }
        });

        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(signal==0){
                    money.append("7");
                    money_number.setText(money);
                    money_number.setSelection(money_number.getText().length());
                }else{
                    money.delete(0,money.length());
                    money.append("7");
                    money_number.setText(money);
                    money_number.setSelection(money_number.getText().length());
                    signal=0;
                }
            }
        });

        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(signal==0){
                    money.append("8");
                    money_number.setText(money);
                    money_number.setSelection(money_number.getText().length());
                }else{
                    money.delete(0,money.length());
                    money.append("8");
                    money_number.setText(money);
                    money_number.setSelection(money_number.getText().length());
                    signal=0;
                }
            }
        });

        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(signal==0){
                    money.append("9");
                    money_number.setText(money);
                    money_number.setSelection(money_number.getText().length());
                }else{
                    money.delete(0,money.length());
                    money.append("9");
                    money_number.setText(money);
                    money_number.setSelection(money_number.getText().length());
                    signal=0;
                }
            }
        });

        point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(signal==0){
                    String a=money.toString();
                    if(a.equals("")){
                        money.append(".");
                        money_number.setText(money);
                        money_number.setSelection(money_number.getText().length());
                    }else{
                        int i;
                        char t='0';
                        for(i=a.length();i>0;i--){
                            t=a.charAt(i-1);
                            if(t=='.')
                                break;
                        }
                        if(i==0){
                            money.append(".");
                            money_number.setText(money);
                            money_number.setSelection(money_number.getText().length());
                        }
                    }
                }else{
                    money.delete(0,money.length());
                    money.append(".");
                    money_number.setText(money);
                    money_number.setSelection(money_number.getText().length());
                    signal=0;
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                money.delete(0,money.length());
                signal=0;
                money_number.setText(money);
                mDatabaseHelper.deleteAllData();
                mCostBeanList.clear();
            }
        });
    }

    public class MyAdapter extends PageGridView.PagingAdapter<MyVH> implements PageGridView.OnItemClickListener {
        List<String> mData = new ArrayList<>();

        public MyAdapter(List<String> data) {
            this.mData.addAll(data);
        }

        @Override
        public MyVH onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(RecordPage.this).inflate(R.layout.layout_items, parent, false);
            ViewGroup.LayoutParams params = view.getLayoutParams();
            params.height = width;
            params.width = width;
            view.setLayoutParams(params);
            return new MyVH(view);
        }

        @Override
        public void onBindViewHolder(MyVH holder, int position) {
            if(TextUtils.isEmpty(mData.get(position))){
                holder.icon.setVisibility(View.GONE);
            }else{
                holder.icon.setVisibility(View.VISIBLE);
            }
            holder.title.setText(mData.get(position));

            int ItemId=0;
            String text= (String) holder.title.getText();
            switch (text){
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
            holder.icon.setImageResource(ItemId);
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        @Override
        public List getData() {
            return mData;
        }

        @Override
        public Object getEmpty() {
            return "";
        }

        @Override
        public void onItemClick(PageGridView pageGridView, int position) {
            String mkind=mData.get(position);
            int icon_id=R.drawable.income;
            switch (mkind){
                case "Recharge":
                    icon_id=R.drawable.recharge;
                    mItem_name.setText("Recharge");
                    break;
                case "Food":
                    icon_id=R.drawable.food;
                    mItem_name.setText("Food");
                    break;
                case "Beauty":
                    icon_id=R.drawable.cosmetics;
                    mItem_name.setText("Beauty");
                    break;
                case "Digital":
                    icon_id=R.drawable.electronics;
                    mItem_name.setText("Digital");
                    break;
                case "Traffic":
                    icon_id=R.drawable.traffic;
                    mItem_name.setText("Traffic");
                    break;
                case "Cloth":
                    icon_id=R.drawable.clothing;
                    mItem_name.setText("Cloth");
                    break;
                case "Study":
                    icon_id=R.drawable.study;
                    mItem_name.setText("Study");
                    break;
                case "Fitness":
                    icon_id=R.drawable.fitness;
                    mItem_name.setText("Fitness");
                    break;
                case "Game":
                    icon_id=R.drawable.game;
                    mItem_name.setText("Game");
                    break;
                case "Gift":
                    icon_id=R.drawable.gift;
                    mItem_name.setText("Gift");
                    break;
                case "Travel":
                    icon_id=R.drawable.travel;
                    mItem_name.setText("Travel");
                    break;
                case "Amusement":
                    icon_id=R.drawable.amusement;
                    mItem_name.setText("Amusement");
                    break;
                case "Personal":
                    icon_id=R.drawable.personal;
                    mItem_name.setText("Personal");
                    break;
                case "Medicine":
                    icon_id=R.drawable.medicine;
                    mItem_name.setText("Medicine");
                    break;
                case "Others":
                    icon_id=R.drawable.others;
                    mItem_name.setText("Others");
                    break;
                default:
                    break;
            }
            mItem_icon.setImageResource(icon_id);
        }
    }

    public static class MyVH extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView icon;
        public MyVH(View itemView) {
            super(itemView);
            title =itemView.findViewById(R.id.classification_context);
            icon= itemView.findViewById(R.id.classification_image);
        }
    }

}
