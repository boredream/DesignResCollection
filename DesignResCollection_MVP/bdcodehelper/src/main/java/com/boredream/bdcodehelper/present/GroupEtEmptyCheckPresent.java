package com.boredream.bdcodehelper.present;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

public class GroupEtEmptyCheckPresent {

    private EditText[] ets;

    public interface OnEtEmptyListener {
        void onEtEmpty(boolean hasOneEmpty);
    }

    public GroupEtEmptyCheckPresent(EditText... ets) {
        this.ets = ets;
    }

    public void check(final OnEtEmptyListener listener) {
        for(EditText et : ets) {
            et.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    checkAllEt(listener, ets);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }
    }

    private void checkAllEt(OnEtEmptyListener listener, EditText[] ets) {
        boolean hasEmpty = false;

        for(EditText et : ets) {
            String s = et.getText().toString().trim();
            if(TextUtils.isEmpty(s)) {
                hasEmpty = true;
                break;
            }
         }

        listener.onEtEmpty(hasEmpty);
    }

}
