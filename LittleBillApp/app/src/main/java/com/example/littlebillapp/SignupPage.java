package com.example.littlebillapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

public class SignupPage extends AppCompatActivity {
    private ImageButton back;
    private Button button;
    private TextView warning;
    private EditText et_user_name,et_psw,et_psw_again;
    private String username,psw,pswAgain;
    private RadioButton check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.signup_page);

        check=findViewById(R.id.signup_radioButton);
        warning=findViewById(R.id.signup_tips);
        et_user_name=findViewById(R.id.signup_name);
        et_psw=findViewById(R.id.sign_password);
        et_psw_again=findViewById(R.id.signup_password_again);

        back=findViewById(R.id.signuppage_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(SignupPage.this,LoginPage.class);
                startActivity(intent);
                SignupPage.this.fileList();
            }
        });

        button=findViewById(R.id.signuopage_Button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditString();
                if(TextUtils.isEmpty(username)){
                    warning.setText("Please Enter Username");
                    return;
                }else if(TextUtils.isEmpty(psw)){
                    warning.setText("Please Enter Password");
                    return;
                }else if(TextUtils.isEmpty(pswAgain)){
                    warning.setText("Please Enter Password Again");
                    return;
                }else if(!psw.equals(pswAgain)){
                    warning.setText("Wrong Password");
                    return;
                }else if(isExistUserName(username)){
                    warning.setText("Existent Account");
                    return;
                }else if (check.isChecked()==false){
                    warning.setText("Please Confirm The Doucument First");
                    return;
                }else{
                    warning.setText("Registration Successful");
                    //??????????????????????????????????????????sp??????
                    saveRegisterInfo(username, psw);
                    //?????????????????????????????????LoginActivity.java???
                    // ????????????loginActivity??????
                    Intent data = new Intent();
                    data.putExtra("userName", username);
                    setResult(RESULT_OK, data);
                    //RESULT_OK???Activity???????????????????????????-1???
                    // ??????????????????????????????????????????data????????????????????????????????????back??????????????????????????????setResult??????data???
                    SignupPage.this.finish();
                }
            }
        });
    }
    private void getEditString(){
        username=et_user_name.getText().toString().trim();
        psw=et_psw.getText().toString().trim();
        pswAgain=et_psw_again.getText().toString().trim();
    }

    private boolean isExistUserName(String userName){
        boolean has_userName=false;
        SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        //????????????
        String spPsw=sp.getString(userName, "");//???????????????????????????
        //??????????????????????????????????????????????????????
        if(!TextUtils.isEmpty(spPsw)) {
            has_userName=true;
        }
        return has_userName;
    }

    private void saveRegisterInfo(String userName,String psw){
        String md5Psw = MD5Utils.md5(psw);//????????????MD5??????
        //loginInfo???????????????
        SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        //???????????????
        SharedPreferences.Editor editor=sp.edit();
        //???????????????key????????????value?????????SharedPreferences???
        //key,value,???????????????editor.putString(?????????????????????;
        editor.putString(userName, md5Psw);
        //????????????
        editor.commit();
    }
}
