package com.boredream.designrescollection.utils;


import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.boredream.bdcodehelper.entity.AppUpdateInfo;
import com.boredream.bdcodehelper.entity.ListResponse;
import com.boredream.bdcodehelper.net.ObservableDecorator;
import com.boredream.bdcodehelper.utils.AppUtils;
import com.boredream.bdcodehelper.utils.DialogUtils;
import com.boredream.bdcodehelper.utils.NetUtils;
import com.boredream.bdcodehelper.utils.ToastUtils;
import com.boredream.designrescollection.R;
import com.boredream.designrescollection.base.BaseActivity;
import com.boredream.designrescollection.net.HttpRequest;
import com.boredream.designrescollection.net.SimpleSubscriber;

import java.util.ArrayList;
import java.util.Locale;

import rx.Observable;

/**
 * 检查更新工具类
 * <p>
 * 可以注册下载完成广播，在其中进行提示跳转安装等行为
 * <p>
 * <li>注册广播</li>
 * <pre>
 *      context.registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
 * </pre>
 * <p>
 * <li>使用广播</li>
 * <pre>
 *      String action = intent.getAction();
 *      if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
 *          long enqueueId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
 *          Uri uri = UpdateUtils.getDownloadUriById(context, enqueueId);
 *          // do something
 *      }
 * </pre>
 * </p>
 */
public class UpdateUtils {

    public static final int DOWNLOAD_STATUS_NEED_LOAD = 1;
    public static final int DOWNLOAD_STATUS_RUNNING = 2;
    public static final int DOWNLOAD_STATUS_LOADED = 3;

    /**
     * 检测版本更新
     *
     * @param context
     * @param isForceCheck 是否强制检测更新
     *                     true强制 - 无论什么网络环境都会提示更新
     *                     false非强制 - WiFi情况下才提示更新
     */
    public static void checkUpdate(final BaseActivity context, final boolean isForceCheck) {
        if (!NetUtils.isConnected(context)) {
            // 无网络时
            if (isForceCheck) {
                // 手动强制检测更新时，提示文字
                ToastUtils.showToast(context, "请检查网络连接");
            } else {
                // 非强制不做操作
            }
            return;
        }

        // 开始检测更新

        if (isForceCheck) {
            // 强制更新时，才提示进度框
            context.showProgressDialog();
        }

        Observable<ListResponse<AppUpdateInfo>> observable = HttpRequest.getInstance().service.getAppUpdateInfo();
        ObservableDecorator.decorate(observable).subscribe(
                new SimpleSubscriber<ListResponse<AppUpdateInfo>>(context) {
                    @Override
                    public void onError(Throwable throwable) {
                        if (isForceCheck) {
                            // 强制更新时，才提示错误
                            super.onError(throwable);
                        }
                        context.dismissProgressDialog();
                    }

                    @Override
                    public void onNext(ListResponse<AppUpdateInfo> response) {
                        context.dismissProgressDialog();

                        ArrayList<AppUpdateInfo> results = response.getResults();
                        if (results.size() == 0) {
                            if (isForceCheck) {
                                ToastUtils.showToast(context, "当前已经是最新版本");
                            }
                        }


                        AppUpdateInfo newestUpdateInfo = results.get(0);
                        for (AppUpdateInfo updateInfo : results) {
                            if (updateInfo.getVersion() > newestUpdateInfo.getVersion()) {
                                // 取最大版本号的作为最新版本
                                newestUpdateInfo = updateInfo;
                            }
                        }

                        if (newestUpdateInfo.getVersion() <= AppUtils.getAppVersionCode(context)) {
                            if (isForceCheck) {
                                ToastUtils.showToast(context, "当前已经是最新版本");
                            }
                        } else {
                            showUpdateConfirmDialog(context, newestUpdateInfo);
                        }
                    }
                }
        );
    }

    /**
     * 无Wifi状态确认更新对话框
     */
    private static void showNoWifiConfirmDialog(final BaseActivity context, final AppUpdateInfo updateInfo) {
        DialogUtils.showCommonDialog(context, "没有wifi连接，是否继续选择更新？",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startDownload(context, updateInfo);
                    }
                });
    }

    /**
     * 显示更新对话框,包含版本相关信息
     */
    private static void showUpdateConfirmDialog(final BaseActivity context, final AppUpdateInfo updateInfo) {
        String content = String.format(Locale.CHINESE,
                context.getResources().getString(R.string.update_info),
                updateInfo.getVersionName(),
                updateInfo.getUpdateInfo() == null ? "无" : updateInfo.getUpdateInfo());

        new AlertDialog.Builder(context)
                .setTitle("发现新版本")
                .setMessage(content)
                .setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (NetUtils.isWifi(context)) {
                            startDownload(context, updateInfo);
                        } else {
                            showNoWifiConfirmDialog(context, updateInfo);
                        }
                    }
                })
                .setNegativeButton("以后再说", null)
                .show();
    }

    /**
     * 开始下载
     *
     * @param context
     * @param updateInfo
     */
    private static void startDownload(BaseActivity context, AppUpdateInfo updateInfo) {
        int status = getDownloadStatus(context, updateInfo);
        if (status != DOWNLOAD_STATUS_NEED_LOAD) {
            // 不用下载则无需下列操作
            return;
        }

        ToastUtils.showToast(context, "开始下载安装包...");

        // parse url
        Uri mUri = Uri.parse(updateInfo.getFileUrl());

        // create request
        DownloadManager.Request r = new DownloadManager.Request(mUri);

        // set request property
        String apkName = getDownloadApkName(context, updateInfo);
        r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, apkName);
        r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        // create manager
        DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        // key code, set mine type
        r.setMimeType("application/vnd.android.package-archive");

        // add to queue
        dm.enqueue(r);
    }

    @NonNull
    private static String getDownloadApkName(BaseActivity context, AppUpdateInfo updateInfo) {
        return context.getString(R.string.app_name) + "_" + updateInfo.getVersionName() + ".apk";
    }

    /**
     * 判断当前版本文件下载状态
     *
     * @param context
     * @param updateInfo
     * @return
     */
    private static int getDownloadStatus(BaseActivity context, AppUpdateInfo updateInfo) {
        DownloadManager.Query query = new DownloadManager.Query();
        DownloadManager dm = (DownloadManager) context.getSystemService(Activity.DOWNLOAD_SERVICE);
        Cursor c = dm.query(query);

        if (!c.moveToFirst()) {
            // 无下载内容
            return DOWNLOAD_STATUS_NEED_LOAD;
        }

        do {
            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            String title = c.getString(c.getColumnIndex(DownloadManager.COLUMN_TITLE));
            String apkName = getDownloadApkName(context, updateInfo);
            if (title.equals(apkName)) {
                // 如果下载列表中文件是当前版本应用，则继续判断下载状态
                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                    // 如果已经下载，返回状态，同时直接提示安装
                    String uri = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                    AppUtils.promptInstall(context, Uri.parse(uri));
                    return DOWNLOAD_STATUS_LOADED;
                } else if (status == DownloadManager.STATUS_RUNNING
                        || status == DownloadManager.STATUS_PAUSED
                        || status == DownloadManager.STATUS_PENDING) {
                    return DOWNLOAD_STATUS_RUNNING;
                } else {
                    // 失败也视为可以再次下载
                    return DOWNLOAD_STATUS_NEED_LOAD;
                }
            }
        } while (c.moveToNext());

        return DOWNLOAD_STATUS_NEED_LOAD;
    }

    /**
     * 根据下载队列id获取下载Uri
     *
     * @param enqueueId
     * @return null-获取不到
     */
    public static Uri getDownloadUriById(Context context, long enqueueId) {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(enqueueId);
        DownloadManager dm = (DownloadManager) context.getSystemService(Activity.DOWNLOAD_SERVICE);
        Cursor c = dm.query(query);
        if (c.moveToFirst()) {
            int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
            if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {
                String uri = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                return Uri.parse(uri);
            }
        }
        return null;
    }

}
