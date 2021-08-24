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
                    //把账号、密码和账号标识保存到sp里面
                    saveRegisterInfo(username, psw);
                    //注册成功后把账号传递到LoginActivity.java中
                    // 返回值到loginActivity显示
                    Intent data = new Intent();
                    data.putExtra("userName", username);
                    setResult(RESULT_OK, data);
                    //RESULT_OK为Activity系统常量，状态码为-1，
                    // 表示此页面下的内容操作成功将data返回到上一页面，如果是用back返回过去的则不存在用setResult传递data值
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
        //获取密码
        String spPsw=sp.getString(userName, "");//传入用户名获取密码
        //如果密码不为空则确实保存过这个用户名
        if(!TextUtils.isEmpty(spPsw)) {
            has_userName=true;
        }
        return has_userName;
    }

    private void saveRegisterInfo(String userName,String psw){
        String md5Psw = MD5Utils.md5(psw);//把密码用MD5加密
        //loginInfo表示文件名
        SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        //获取编辑器
        SharedPreferences.Editor editor=sp.edit();
        //以用户名为key，密码为value保存在SharedPreferences中
        //key,value,如键值对，editor.putString(用户名，密码）;
        editor.putString(userName, md5Psw);
        //提交修改
        editor.commit();
    }
}
