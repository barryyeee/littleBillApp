package com.example.littlebillapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class LoginPage extends Activity implements View.OnClickListener {
    private ImageButton back;
    private Button button,text;
    private String username,psw,spPsw;
    private EditText et_user,et_psw;
    private TextView tip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login_page);

        tip=findViewById(R.id.warning);
        et_user=findViewById(R.id.et_user);
        et_psw=findViewById(R.id.et_password);

        text=findViewById(R.id.text);
        text.setOnClickListener(this);
        back=findViewById(R.id.loginpage_back);
        back.setOnClickListener(this);
        button=findViewById(R.id.loginpage_Button);
        button.setOnClickListener(this);
    }

    public void onClick(View view) {
        Intent intent = new Intent();
        switch(view.getId()){
            case R.id.loginpage_back:
                intent.setClass(LoginPage.this,WelcomePage.class);
                startActivity(intent);
                break;
            case R.id.text:
                intent.setClass(LoginPage.this,SignupPage.class);
                startActivity(intent);
                break;
            case R.id.loginpage_Button:
                loginUser();
                break;
        }
    }

    private void loginUser(){
        //开始登录，获取用户名和密码 getText().toString().trim();
        username=et_user.getText().toString().trim();
        psw=et_psw.getText().toString().trim();
        //对当前用户输入的密码进行MD5加密再进行比对判断, MD5Utils.md5( ); psw 进行加密判断是否一致
        String md5Psw= MD5Utils.md5(psw);
        // md5Psw ; spPsw 为 根据从SharedPreferences中用户名读取密码
        // 定义方法 readPsw为了读取用户名，得到密码
        spPsw=readPsw(username);
        if(TextUtils.isEmpty(username)){
            tip.setText("Please Enter Username");
            return;
        }else if(TextUtils.isEmpty(psw)){
            tip.setText("Please Enter Password");
            return;
            // md5Psw.equals(); 判断，输入的密码加密后，是否与保存在SharedPreferences中一致
        }else if(md5Psw.equals(spPsw)){
            //一致登录成功
            tip.setText("Login Successful");
            //保存登录状态，在界面保存登录的用户名 定义个方法 saveLoginStatus boolean 状态 , userName 用户名;
            saveLoginStatus(true, username);
            //登录成功后关闭此页面进入主页
            Intent data=new Intent();
            data.putExtra("isLogin",true);
            //RESULT_OK为Activity系统常量，状态码为-1
            // 表示此页面下的内容操作成功将data返回到上一页面，如果是用back返回过去的则不存在用setResult传递data值
            setResult(RESULT_OK,data);
            //销毁登录界面
            LoginPage.this.finish();
            //跳转到主界面，登录成功的状态传递到 MainActivity 中
            startActivity(new Intent(LoginPage.this, RecordPage.class));
            return;
        }else if((spPsw!=null&&!TextUtils.isEmpty(spPsw)&&!md5Psw.equals(spPsw))){
            tip.setText("The Username And Password Do Not Match");
            return;
        }else{
            tip.setText("Nonexistent Account");
        }
    }

    /**
     *从SharedPreferences中根据用户名读取密码
     */
    private String readPsw(String userName){
        //"loginInfo",mode_private; MODE_PRIVATE表示可以继续写入
        SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        return sp.getString(userName , "");
    }
    /**
     *保存登录状态和登录用户名到SharedPreferences中
     */
    private void saveLoginStatus(boolean status,String userName){
        //loginInfo表示文件名
        SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        //获取编辑器
        SharedPreferences.Editor editor=sp.edit();
        //存入boolean类型的登录状态
        editor.putBoolean("isLogin", status);
        //存入登录状态时的用户名
        editor.putString("loginUserName", userName);
        //提交修改
        editor.commit();
    }
    /**
     * 注册成功的数据返回至此
     * @param requestCode 请求码
     * @param resultCode 结果码
     * @param data 数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            //是获取注册界面回传过来的用户名
            String userName=data.getStringExtra("userName");
            if(!TextUtils.isEmpty(userName)){
                //设置用户名到 et_user_name 控件
                et_user.setText(userName);
                //et_user_name控件的setSelection()方法来设置光标位置
                et_user.setSelection(userName.length());
            }
        }
    }
}
