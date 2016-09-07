package com.boredream.designrescollection.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.boredream.bdcodehelper.utils.AppUtils;
import com.boredream.bdcodehelper.utils.StringUtils;
import com.boredream.designrescollection.R;
import com.boredream.designrescollection.base.BaseActivity;

public class AboutActivity extends BaseActivity {

    private TextView tv_version;
    private TextView tv_about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initView();
    }

    private void initView() {
        initBackTitle("关于我");

        tv_version = (TextView) findViewById(R.id.tv_version);
        tv_about = (TextView) findViewById(R.id.tv_about);

        tv_version.setText("Version " + AppUtils.getAppVersionName(this));

        SpannableString ss = getAboutString();
        tv_about.setMovementMethod(LinkMovementMethod.getInstance());
        tv_about.setText(ss);
    }

    private SpannableString getAboutString() {
        final String githubLink = getString(R.string.github);
        String format = String.format(getString(R.string.about), githubLink);
        SpannableString ss = new SpannableString(format);
        // 网址部分可点击,跳转到网页
        StringUtils.PrimaryClickableSpan span = new StringUtils.PrimaryClickableSpan(this) {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(githubLink));
                startActivity(intent);
            }
        };
        int start = format.indexOf(githubLink);
        ss.setSpan(span, start, start + githubLink.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }
}
