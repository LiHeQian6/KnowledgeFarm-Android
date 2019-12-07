package com.li.knowledgefarm.Login.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.li.knowledgefarm.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class RegistAccountDialog extends DialogFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.regist_dialog,container,false);

        final EditText nickName = view.findViewById(R.id.registName);
        final EditText pwd = view.findViewById(R.id.registPwd);
        final EditText configPwd = view.findViewById(R.id.configPwd);
        final EditText bound = view.findViewById(R.id.boundQQ);
        Button button = view.findViewById(R.id.btnRegist);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nickName.equals("")||pwd.equals("")||configPwd.equals("")||bound.equals("")){
                    Toast.makeText(getContext(),"请完善注册信息！",Toast.LENGTH_SHORT);
                    return;
                }else if(!pwd.equals(configPwd)){
                    Toast.makeText(getContext(),"密码输入不一致！",Toast.LENGTH_SHORT);
                }else {
                    registToServer();
                }
            }
        });

        return view;
    }

    private void registToServer() {
    }
}
