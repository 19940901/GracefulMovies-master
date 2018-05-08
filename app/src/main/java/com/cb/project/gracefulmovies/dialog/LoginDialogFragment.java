package com.cb.project.gracefulmovies.dialog;

import android.app.*;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.cb.project.gracefulmovies.R;
import com.cb.project.gracefulmovies.model.LoginResult;
import com.cb.project.gracefulmovies.view.activity.MainActivity;
import com.cb.project.gracefulmovies.view.service.RequestManager;
import com.cb.project.gracefulmovies.view.service.UserService;

public class LoginDialogFragment extends DialogFragment implements View.OnFocusChangeListener,
        View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.et_phone)
    EditText mEtPhone;
    @BindView(R.id.et_pwd)
    EditText et_pwd;
    @BindView(R.id.btn_next)
    Button mBtnNext;
    @BindView(R.id.tv_login)
    TextView tv_login;
    @BindView(R.id.tv_register)
    TextView tv_register;

    @BindView(R.id.btn_close)
    ImageButton btn_close;
    @BindView(R.id.iv_display)
    CheckBox iv_display;
    @BindView(R.id.loading)
    ProgressBar loading;

    private String phone;
    private String themeString;
    RequestManager manager;
    UserService service;


    public static LoginDialogFragment newInstance(String phone) {
        LoginDialogFragment mFragment = new LoginDialogFragment();
        // Bundle bundle = new Bundle();
        // bundle.putString("phone", phone);
        // mFragment.setArguments(bundle);
        return mFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.login_layout, null);
        ButterKnife.bind(this, view);
        initView();
        builder.setView(view);
        return builder.create();
    }

    private void initView() {
        //phone = getArguments().getString("phone");
        //mEtPhone.setText(phone);
        loading.setVisibility(View.GONE);
        //mEtPhone.setOnFocusChangeListener(this);
        mEtPhone.addTextChangedListener(adapter);
        mBtnNext.setOnClickListener(this);
        tv_login.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        btn_close.setOnClickListener(this);
        iv_display.setOnCheckedChangeListener(this);
    }

    @Override
    public void onFocusChange(View view, boolean b) {
//		PhoneDialogFragment dialog = new PhoneDialogFragment();
//		dialog.show(getFragmentManager(), "phonedailog");
//		dismiss();
    }

    TextWatcherAdapter adapter = new TextWatcherAdapter() {
        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 11 && s.toString().matches("^1[34578]\\d{9}$")) {
                mBtnNext.setEnabled(true);
            } else {
                mBtnNext.setEnabled(false);
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //忘记密码
            case R.id.tv_login:
                VcodeDialogFragment dialogFragment = VcodeDialogFragment.newInstance(phone);
                dialogFragment.show(getFragmentManager(), "VcodeDialogFragment");
                dismiss();
                break;
            //注册
            case R.id.tv_register:
                PhoneDialogFragment phoneDialogFragment = new PhoneDialogFragment();
                phoneDialogFragment.show(getFragmentManager(), "phone");
                dismiss();
                break;
            case R.id.btn_close:
                dismiss();
                break;
            case R.id.btn_next:
                service = new UserService(mEtPhone.getText().toString(), et_pwd.getText().toString(), this);
                service.loadData(getActivity());
               // dismiss();
                break;

        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            et_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            et_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }

    public void diplay(LoginResult result) {
        if (null != result && result.getState().equals("true")) {
            Activity mActivity = getActivity();
            SharedPreferences user = mActivity.getSharedPreferences("user", Application.MODE_PRIVATE);
            SharedPreferences.Editor editor = user.edit();
            editor.putString("id", result.getId());
            editor.putString("name", result.getName());
            editor.commit();
            dismiss();
        } else {
            //LoginDialogFragment fragment = newInstance("login");
            //fragment.show(getFragmentManager(),"login");
           // dismiss();
            Toast.makeText(getActivity(),"用户名或者密码错误",Toast.LENGTH_LONG).show();
        }
    }
}
