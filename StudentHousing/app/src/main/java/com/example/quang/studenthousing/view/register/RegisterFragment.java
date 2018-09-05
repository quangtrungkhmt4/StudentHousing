package com.example.quang.studenthousing.view.register;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.quang.studenthousing.AccountActivity;
import com.example.quang.studenthousing.R;
import com.example.quang.studenthousing.presenter.register.PresenterImRegister;
import com.example.quang.studenthousing.presenter.register.PresenterLogicRegister;
import com.example.quang.studenthousing.view.login.LoginFragment;

public class RegisterFragment extends Fragment implements View.OnClickListener, ViewRegister {

    private EditText edtUser;
    private EditText edtPass;
    private EditText edtConfirmPass;
    private EditText edtName;
    private EditText edtPhone;
    private Button btnRegister;
    private Button btnBack;
    private AccountActivity activity;

    private PresenterLogicRegister presenterLogicRegister;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register,container,false);
        findID(view);
        initViews();
        return view;
    }

    private void findID(View view) {
        edtUser = view.findViewById(R.id.edtUserNameRegister);
        edtPass = view.findViewById(R.id.edtPasswordRegister);
        edtConfirmPass = view.findViewById(R.id.edtConfirmPasswordRegister);
        edtName = view.findViewById(R.id.edtNameRegister);
        edtPhone = view.findViewById(R.id.edtPhoneRegister);
        btnBack = view.findViewById(R.id.btnBackRegister);
        btnRegister = view.findViewById(R.id.btnRegister);
    }

    private void initViews() {
        activity = (AccountActivity) getActivity();
        btnBack.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

        presenterLogicRegister = new PresenterLogicRegister(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnBackRegister:
                activity.loadFragment(new LoginFragment());
                break;
            case R.id.btnRegister:
                String user = edtUser.getText().toString();
                String pass = edtPass.getText().toString();
                String name = edtName.getText().toString();
                String phone = edtPhone.getText().toString();
                String confirmPass = edtConfirmPass.getText().toString();

                if (user.isEmpty() || pass.isEmpty() || confirmPass.isEmpty() || name.isEmpty()
                        || phone.isEmpty()){
                    Snackbar snackbar = Snackbar
                            .make(edtPass, R.string.insert_info, Snackbar.LENGTH_LONG);
                    snackbar.show();
                    return;
                }

                if (!pass.equals(confirmPass)){
                    Snackbar snackbar = Snackbar
                            .make(edtPass, R.string.confirm_pass_fail, Snackbar.LENGTH_LONG);
                    snackbar.show();
                    return;
                }

                presenterLogicRegister.checkRegister(user,pass,name,phone,activity);

                break;
        }
    }

    @Override
    public void registerSuccess() {
        Snackbar snackbar = Snackbar
                .make(edtPass, R.string.success, Snackbar.LENGTH_LONG);
        snackbar.show();

        activity.loadFragment(new LoginFragment());
    }

    @Override
    public void registerFail() {
        Snackbar snackbar = Snackbar
                .make(edtPass, R.string.register_fail, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void userExists() {
        Snackbar snackbar = Snackbar
                .make(edtPass, R.string.user_exists, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}
