package com.li.knowledgefarm.Login.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.li.knowledgefarm.R;
import com.li.knowledgefarm.Util.UserUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class NotifyAccountDialog extends DialogFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notifyaccount_dialog,container,false);

        TextView account = view.findViewById(R.id.account);
        account.setText(UserUtil.getUser().getAccount());
        TextView nickName = view.findViewById(R.id.nName);
        nickName.setText(UserUtil.getUser().getNickName());

        Button button = view.findViewById(R.id.accountEnsure);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return view;
    }
}
