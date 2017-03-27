package com.organize4event.organize.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.organize4event.organize.R;
import com.organize4event.organize.commons.AppApplication;
import com.organize4event.organize.commons.CustomValidate;
import com.organize4event.organize.commons.PreferencesManager;
import com.organize4event.organize.controllers.FirstAccessControll;
import com.organize4event.organize.controllers.TokenControll;
import com.organize4event.organize.enuns.AccessPlatformEnum;
import com.organize4event.organize.enuns.LoginTypeEnum;
import com.organize4event.organize.listeners.ControllResponseListener;
import com.organize4event.organize.listeners.CustomDialogListener;
import com.organize4event.organize.models.AccessPlatform;
import com.organize4event.organize.models.FirstAccess;
import com.organize4event.organize.models.LoginType;
import com.organize4event.organize.models.Token;
import com.organize4event.organize.models.User;

import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements Validator.ValidationListener{
    private Context context;
    private int code_enum;
    private int code_enum_platform;
    private boolean keep_logged = false;
    private int keep_logged_int = 0;

    private Validator validator;
    private CustomValidate customValidate;

    private User user;
    private User newUser;
    private FirstAccess firstAccess;
    private Token token;
    private LoginType loginType;
    private AccessPlatform accessPlatform;

    @Bind(R.id.containerLoginEmail)
    RelativeLayout containerLoginEmail;

    @Bind(R.id.containerForgotPassword)
    RelativeLayout containerForgotPassword;

    @Order(1)
    @NotEmpty(trim = true, sequence = 1, messageResId = R.string.validate_required_field)
    @Email(sequence = 2, messageResId = R.string.validate_mail)
    @Bind(R.id.txtMail)
    EditText txtMail;

    @Order(2)
    @NotEmpty(trim = true, sequence = 1, messageResId = R.string.validate_required_field)
    @Password(min = 6, sequence = 2, scheme = Password.Scheme.ALPHA_NUMERIC, messageResId = R.string.validate_password)
    @Bind(R.id.txtPassword)
    EditText txtPassword;

    @Bind(R.id.txtMailForgotPassword)
    EditText txtMailForgotPassword;

    @Bind(R.id.cbxKeepLogged)
    CheckBox cbxKeepLogged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        context = LoginActivity.this;
        code_enum_platform = AccessPlatformEnum.ANDROID.getValue();

        user = AppApplication.getUser();
        firstAccess = AppApplication.getFirstAccess();
        token = AppApplication.getToken();

        if (token == null){
            token = new Token();
        }
        else if(token.isKeep_logged()){
            if (PreferencesManager.isHideWelcome()){
                startActivity(new Intent(context, HomeActivity.class));
            }
            else{
                startActivity(new Intent(context, WelcomeActivity.class));
            }
        }

        validator = new Validator(context);
        validator.setValidationListener(this);
    }

    @OnClick({R.id.btnLoginEmail, R.id.btnLoginFacebook, R.id.txtForgotPassword})
    public void actionOnClickLogin(View view){
        switch (view.getId()){
            case R.id.btnLoginEmail:
                code_enum = LoginTypeEnum.EMAIL.getValue();
                getAccessPlatform();
                break;
            case R.id.btnLoginFacebook:
                code_enum = LoginTypeEnum.FACEBOOK.getValue();
                getAccessPlatform();
                break;
            case R.id.txtForgotPassword:
                containerForgotPassword.setVisibility(View.VISIBLE);
                break;
        }
    }

    @OnCheckedChanged(R.id.cbxKeepLogged)
    public void actionKeepLogged(){
        keep_logged = cbxKeepLogged.isChecked();
        if (keep_logged){
            keep_logged_int = 1;
        }
    }

    @OnClick({R.id.btnLogin, R.id.btnCancel, R.id.btnForgotPasswordSend, R.id.btnForgotPasswordCancel, R.id.txtIsNotRegistered})
    public void actionButtonClick(View view){
        switch (view.getId()){
            case R.id.btnLogin:
                validator.validate();
                break;
            case R.id.btnCancel:
                containerLoginEmail.setVisibility(View.GONE);
                break;
            case R.id.btnForgotPasswordSend:
                forgotPassword();
                break;
            case R.id.btnForgotPasswordCancel:
                containerForgotPassword.setVisibility(View.GONE);
                break;
            case R.id.txtIsNotRegistered:
                startActivity(new Intent(context, ApresentationActivity.class));
                break;
        }
    }

    public void loginEmail(){
        hideLoading();
        containerLoginEmail.setVisibility(View.VISIBLE);
        if(token.is_new()){
            token.setFirstAccess(firstAccess);
            token.setLogin_type(loginType);
            token.setAccess_platform(accessPlatform);
            token.setAccess_date(new Date());
            token.setKeep_logged(keep_logged);
        }
        else{
            token.setLogin_type(loginType);
            token.setAccess_platform(accessPlatform);
            token.setAccess_date(new Date());
            token.setKeep_logged(keep_logged);
        }
    }

    public void login(){
        showLoading();
        new TokenControll(context).login(txtMail.getText().toString(), txtPassword.getText().toString(), new ControllResponseListener() {
            @Override
            public void sucess(Object object) {
                hideLoading();
                newUser = (User) object;
                if (!newUser.is_new()){
                    containerLoginEmail.setVisibility(View.GONE);
                    showDialogMessage(1, context.getString(R.string.app_name), newUser.getMessage(), new CustomDialogListener() {
                        @Override
                        public void positiveOnClick(MaterialDialog dialog) {
                            dialog.dismiss();
                            containerLoginEmail.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void negativeOnClidck(MaterialDialog dialog) {

                        }
                    });
                }
                else{
                    user = newUser;
                    user.setIs_new(false);
                    token.setUser(user);
                    if (token.is_new()){
                        saveToken();
                    }
                    else{
                        updateToken();
                    }
                }
            }

            @Override
            public void fail(Error error) {
                hideLoading();
                containerLoginEmail.setVisibility(View.GONE);
                returnErrorMessage(error, context);
            }
        });
    }

    public void saveToken(){
        new TokenControll(context).saveToken(token, keep_logged_int, new ControllResponseListener() {
            @Override
            public void sucess(Object object) {
                hideLoading();
                token = (Token) object;
                if (PreferencesManager.isHideWelcome()){
                    startActivity(new Intent(context, HomeActivity.class));
                }
                else{
                    startActivity(new Intent(context, WelcomeActivity.class));
                }
                finish();
            }

            @Override
            public void fail(Error error) {
                hideLoading();
                containerLoginEmail.setVisibility(View.GONE);
                returnErrorMessage(error, context);
            }
        });
    }

    public void updateToken(){
        new TokenControll(context).updateToken(token, keep_logged_int, new ControllResponseListener() {
            @Override
            public void sucess(Object object) {
                hideLoading();
                token = (Token) object;
                if (PreferencesManager.isHideWelcome()){
                    startActivity(new Intent(context, HomeActivity.class));
                }
                else{
                    startActivity(new Intent(context, WelcomeActivity.class));
                }
                finish();
            }

            @Override
            public void fail(Error error) {
                hideLoading();
                containerLoginEmail.setVisibility(View.GONE);
                returnErrorMessage(error, context);
            }
        });
    }

    public void loginFacebook(){
        hideLoading();
        showToastMessage(context, "Implementar Login com Facebook");
        //TODO: IMPLEMENTAR LOGIN COM FACEBOOK
    }

    public void forgotPassword(){
        customValidate = new CustomValidate();
        if(!customValidate.validadeEmail(txtMailForgotPassword.getText().toString())){
            txtMailForgotPassword.setError(context.getString(R.string.validate_mail));
        }
        else{
            //TODO: IMPLEMENTAR ROTA DE ALTERAR SENHA POR E-MAIL
        }
    }

    public void getAccessPlatform(){
        showLoading();
        new FirstAccessControll(context).getAccessPlatform(firstAccess.getLocale(), code_enum_platform, new ControllResponseListener() {
            @Override
            public void sucess(Object object) {
                accessPlatform = (AccessPlatform) object;
                getLoginType();
            }

            @Override
            public void fail(Error error) {
                returnErrorMessage(error, context);
            }
        });
    }

    public void getLoginType(){
        new TokenControll(context).getLoginType(firstAccess.getLocale(), code_enum, new ControllResponseListener() {
            @Override
            public void sucess(Object object) {
                loginType = (LoginType) object;
                if (loginType.getCode_enum() == LoginTypeEnum.EMAIL.getValue()){
                    loginEmail();
                }
                else if (loginType.getCode_enum() == LoginTypeEnum.FACEBOOK.getValue()){
                    loginFacebook();
                }
            }

            @Override
            public void fail(Error error) {
                returnErrorMessage(error, context);
            }
        });
    }

    @Override
    public void onValidationSucceeded() {
        login();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        validateError(errors);
    }
}
