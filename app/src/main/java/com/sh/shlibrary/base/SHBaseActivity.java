package com.sh.shlibrary.base;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sh.shlibrary.R;
import com.sh.shlibrary.callback.PerssionCallBack;
import com.sh.shlibrary.widget.ToolBarHelper;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import java.util.ArrayList;
import java.util.List;
import rx.Subscription;


/**
 * 尚好Android baseActivity
 * 提供公共的权限加载 和标题栏预先加载
 * @Auther lm
 */
public abstract class SHBaseActivity extends AppCompatActivity {
    protected Activity mActivity;
    private static final int TAKE_PERMISSION_RQUEST_CODE = 110;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
    }

    //标题栏相关控件
    public Toolbar toolbar;
    protected TextView toolBarTitle, toolBarRight;
    protected ImageView toolBarBack, tool_bar_right_icon, tool_bar_right_icon_2;

    /**
     *  根据情况选择是否加载固定 标题栏  相关控件自定义修改
     * @Auther lm
     */
    public void initTooBar(int layoutResID) {
        ToolBarHelper mToolBarHelper = new ToolBarHelper(this, layoutResID);
        toolbar = mToolBarHelper.getToolBar();
        toolBarTitle = (TextView) toolbar.findViewById(R.id.tool_bar_title);
        toolBarRight = (TextView) toolbar.findViewById(R.id.tool_bar_right);
        toolBarBack = (ImageView) toolbar.findViewById(R.id.tool_bar_back);
        tool_bar_right_icon = (ImageView) toolbar.findViewById(R.id.tool_bar_right_icon);
        tool_bar_right_icon_2 = (ImageView) toolbar.findViewById(R.id.tool_bar_right_icon_2);
        toolBarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setContentView(mToolBarHelper.getContentView());
        /*自定义的一些操作*/
        onCreateCustomToolBar(toolBarBack, toolBarTitle, toolBarRight);
        /*把 toolbar 设置到Activity 中*/
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public abstract void onCreateCustomToolBar(ImageView toolBarBack, TextView toolBarTitle, TextView toolBarRight);
//    /**
//     * 简单解析网络请求返回错误并提示
//     *
//     * @param e     异常
//     * @param toast 默认提示内容
//     */
//    public void showHttpError(Throwable e, String toast) {
//        e.printStackTrace();
//        if (e instanceof ServerInfoException) {
//
//            toast(e.getMessage());
//        } else if (e instanceof ConnectException) {
//            toast.show("请检查网络连接");
//        } else {
//            toast.show(toast);
//        }
//    }


    @Override
    protected void onStop() {
        super.onStop();

    }





    /**
     * 显示一个DialogFragment
     *
     * @param clazz  需要显示的DialogFragment的class
     * @param bundle 初始化参数
     * @param <E>    extends DialogFragment
     * @return 创建的DialogFragment
     */
    public <E extends DialogFragment> E showDialog(Class<E> clazz, Bundle bundle) {
        E dialog = (E) getFragmentManager().findFragmentByTag(clazz.getName());
        if (dialog == null) {
            try {
                dialog = clazz.newInstance();
                if (bundle != null) {
                    dialog.setArguments(bundle);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (dialog != null) {
            dialog.show(getFragmentManager(), clazz.getName());
        }
        return dialog;
    }

    public <D extends DialogFragment> void dismissDialog(Class<D> dClass) {
        if (dClass == null) {
            return;
        }
        D dialog = (D) getFragmentManager().findFragmentByTag(dClass.getName());
        if (dialog == null) {
            return;
        }
        dialog.dismiss();
    }



    private PerssionCallBack callBack;
    public void applyPermission1(String permission[], PerssionCallBack callBack){
        this.callBack=callBack;
        List<String> noPermission=new ArrayList<>();
        for (int i=0;i<permission.length;i++){
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.
                    checkSelfPermission(this,permission[i])) {
                noPermission.add(permission[i]);
            }
        }
        //下载文件需要权限,针对6.0+权限适配处理 读写文件权限    如果acitivity父类继承AppCompatActivity 是不需要判断版本的 系统会自动判定版本,
        if (noPermission.size()==0) {
            callBack.success();

        } else {
            AndPermission.with(this) //权限申请
                    .requestCode(TAKE_PERMISSION_RQUEST_CODE)
                    .permission(noPermission.toArray(new String[noPermission.size()]))
                    .send();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 只需要调用这一句，其它的交给AndPermission吧，最后一个参数是PermissionListener。
        AndPermission.onRequestPermissionsResult(requestCode, permissions, grantResults, listener);
    }

    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // 权限申请成功回调。
            if (requestCode == TAKE_PERMISSION_RQUEST_CODE) {
                // TODO 相应代码。
                if (callBack!=null){
                    callBack.success();
                }
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // 权限申请失败回调。

            AndPermission.defaultSettingDialog(mActivity, requestCode)
                    .setTitle("权限申请失败")
                    .setMessage("我们需要的一些权限被您拒绝或者系统发生错误申请失败，请您到设置页面手动授权，否则功能无法正常使用！")
                    .setPositiveButton("好，去设置")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            toast("你没有同意该权限,将暂时不能使用该功能",Toast.LENGTH_SHORT);
                        }
                    })
                    .show();

        }
    };


    public void  toast(String content,int type){
        Toast.makeText(mActivity,content,type).show();
    }
}
