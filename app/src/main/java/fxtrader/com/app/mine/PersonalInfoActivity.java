package fxtrader.com.app.mine;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import fxtrader.com.app.MainActivity;
import fxtrader.com.app.R;
import fxtrader.com.app.base.BaseActivity;
import fxtrader.com.app.config.LoginConfig;
import fxtrader.com.app.constant.IntentItem;
import fxtrader.com.app.db.helper.UserInfoHelper;
import fxtrader.com.app.entity.CommonResponse;
import fxtrader.com.app.entity.UploadAvatarEntity;
import fxtrader.com.app.entity.UserEntity;
import fxtrader.com.app.http.HttpConstant;
import fxtrader.com.app.http.ParamsUtil;
import fxtrader.com.app.http.RetrofitUtils;
import fxtrader.com.app.http.api.UserApi;
import fxtrader.com.app.http.manager.UserInfoManager;
import fxtrader.com.app.tools.Base64;
import fxtrader.com.app.tools.LogZ;
import fxtrader.com.app.tools.UIUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zhangyuzhu on 2016/11/26.
 */
public class PersonalInfoActivity extends BaseActivity implements View.OnClickListener{

    private static final File PHOTO_DIR = new File(
            Environment.getExternalStorageDirectory() + "/DCIM/Camera");

    private UserEntity mUser;

    private TextView mEditTv;

    private ImageView mAvatarIm;

    private TextView mNameTv;

    private TextView mAccountTv;

    private TextView mSexTv;

    private TextView mBindRemindTv1;

    private TextView mBindRemindTv2;

    private TextView mPhoneTv;

    private RelativeLayout mChangeAvatarLayout;

    private LinearLayout mInfoLayout;

    private LinearLayout mNicknameLayout;

    private EditText mNicknameEdt;

    private Spinner mSexSpinner;

    private String mSexChecked = "";

    private boolean mIsEditMode = false;

    private boolean mIsUpdate = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_personal_info);
        initViews();
        setUser();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case CAMERA_WITH_DATA: {
                doCropPhoto(mCurrentPhotoFile);
                break;
            }
            case PHOTO_PICKED_WITH_DATA: {
                if (data != null) {
                    // isSavePhoto = true;
                    Bitmap photo = null;
                    if ("Lenovo K920".equals(android.os.Build.MODEL)
                            || "X9077".equals(android.os.Build.MODEL)
                            || "vivo Y22iL".equals(android.os.Build.MODEL)
                            || "H300".equals(android.os.Build.MODEL)
                            || "R7007".equals(android.os.Build.MODEL)
                            || "Coolpad 8021".equals(android.os.Build.MODEL)
                            || "Lenovo Z2".equals(android.os.Build.MODEL)) {
                        photo = decodeUriAsBitmap(Uri.fromFile(mCurrentPhotoFile1));
                    }
                    if (isTakephoto
                            && ("vivo Y27".equals(android.os.Build.MODEL) || "Hisense I639M"
                            .equals(android.os.Build.MODEL))) {
                        photo = decodeUriAsBitmap(Uri.fromFile(mCurrentPhotoFile1));
                    }
                    if (photo == null) {
                        photo = data.getParcelableExtra("data");
                    }
                    if ((photo == null) && (data.getData() != null)) {
                        photo = getMmsImage(data.getData(), this,
                                true, ICON_SIZE, ICON_SIZE);
                    }
                    setPhotoBitmap(photo);
                    if (mCameraDialog != null) {
                        mCameraDialog.dismiss();
                    }
                }
                break;

            }
        }
    }

    private void doCropPhoto(File f) {
        try {

            // Add the image to the media store
            MediaScannerConnection.scanFile(this,
                    new String[] { f.getAbsolutePath() },
                    new String[] { null }, null);

            // Launch gallery to crop the photo
            final Intent intent = getCropImageIntent(Uri.fromFile(f));
            startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
        } catch (Exception e) {
            showToastShort("手机上没有照片");
        }
    }

    public Intent getCropImageIntent(Uri photoUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(photoUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", ICON_SIZE);
        intent.putExtra("outputY", ICON_SIZE);
        if ("Lenovo K920".equals(android.os.Build.MODEL)
                || "X9077".equals(android.os.Build.MODEL)
                || "vivo Y22iL".equals(android.os.Build.MODEL)
                || "H300".equals(android.os.Build.MODEL)
                || "R7007".equals(android.os.Build.MODEL)
                || "Coolpad 8021".equals(android.os.Build.MODEL)
                || "LG-D858".equals(android.os.Build.MODEL)
                || "Lenovo Z2".equals(android.os.Build.MODEL)
                || "vivo Y27".equals(android.os.Build.MODEL)
                || "Hisense I632M".equals(android.os.Build.MODEL)
                || "Hisense I639M".equals(android.os.Build.MODEL)) {
            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(mCurrentPhotoFile1));
            intent.putExtra("outputFormat",
                    Bitmap.CompressFormat.JPEG.toString());
            intent.putExtra("noFaceDetection", true); // no face detection
        } else if ("AOLE T309".equals(android.os.Build.MODEL)
                || "R819T".equals(android.os.Build.MODEL)) {
            // intent.putExtra("return-data", true);
        } else {
            intent.putExtra("return-data", true);
        }
        return intent;
    }

    private Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver()
                    .openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    private byte[] headPhoto;
    private boolean mHasPhoto = false;// 判断是否有图片

    public void setPhotoBitmap(Bitmap photo) {

        if (photo == null) {
            return;
        }

        final int size = photo.getWidth() * photo.getHeight() * 4;
        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            photo.compress(Bitmap.CompressFormat.PNG, 100, out);
            headPhoto = out.toByteArray();
            out.flush();
            out.close();

            mAvatarIm.setImageBitmap(getCroppedBitmap(
                    photo, true));
            mHasPhoto = true;
            uploadAvatar();
        } catch (IOException e) {
            LogZ.e("Unable to serialize photo: " + e.toString());
        } finally {
            // photo.recycle();
        }

        // if (isSavePhoto) {
        //
        // saveContactPhoto();
        // }
    }

    private void initViews() {
        mEditTv = (TextView) findViewById(R.id.title_edt_tv);

        mAvatarIm = (ImageView) findViewById(R.id.person_info_avatar_im);
        mChangeAvatarLayout = (RelativeLayout) findViewById(R.id.person_info_change_avatar_layout);

        mInfoLayout = (LinearLayout) findViewById(R.id.personal_info_layout);
        mNameTv = (TextView) findViewById(R.id.personal_info_name_tv);
        mAccountTv = (TextView) findViewById(R.id.personal_info_account_tv);

        mNicknameLayout = (LinearLayout) findViewById(R.id.personal_info_nickname_layout);
        mNicknameEdt = (EditText) findViewById(R.id.personal_info_nickname_edt);

        mSexTv = (TextView) findViewById(R.id.personal_info_sex_tv);

        mSexSpinner = (Spinner) findViewById(R.id.personal_info_sex_spinner);
        String[] mItems = getResources().getStringArray(R.array.sex);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSexSpinner .setAdapter(adapter);
        mSexSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                String[] sexs = getResources().getStringArray(R.array.sex);
                mSexChecked = sexs[pos];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        mBindRemindTv1 = (TextView) findViewById(R.id.personal_info_remind_bind_1_tv);
        mBindRemindTv2 = (TextView) findViewById(R.id.personal_info_remind_bind_2_tv);
        mPhoneTv = (TextView) findViewById(R.id.personal_info_bind_reward_tv);

        findViewById(R.id.title_edt_tv).setOnClickListener(this);
        findViewById(R.id.person_info_update_avatar_tv).setOnClickListener(this);
        findViewById(R.id.personal_info_sex_layout).setOnClickListener(this);
        findViewById(R.id.personal_info_bind_layout).setOnClickListener(this);
        findViewById(R.id.personal_info_change_pwd_layout).setOnClickListener(this);
        findViewById(R.id.personal_info_login_out_layout).setOnClickListener(this);

        setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back(){
        if (mIsEditMode) {
            restoreUI();
        } else {
            Intent intent = getIntent();
            intent.putExtra(IntentItem.PERSONAL_INFO_UPDATE, mIsUpdate);
            intent.putExtra(IntentItem.NICKNAME, mNameTv.getText().toString().trim());
            intent.putExtra(IntentItem.AVATAR_URL, mUploadAvatarUrl);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_edt_tv:
                if (mIsEditMode) {
                    update();
                } else {
                    changeToEditMode();
                }
                break;
            case R.id.person_info_update_avatar_tv:
                PHOTO_DIR.mkdirs();
                mCurrentPhotoFile1 = new File(PHOTO_DIR, "temp.jpg");
                showTakePictureDialog();
                break;
            case R.id.personal_info_sex_layout://性别
                break;
            case R.id.personal_info_bind_layout://绑定手机
                break;
            case R.id.personal_info_change_pwd_layout://修改密码
                openActivity(ChangePwdActivity.class);
                break;
            case R.id.personal_info_login_out_layout:
                loginOut();
                break;
            default:
                break;
        }
    }

    private void changeToEditMode(){
        mIsEditMode = true;

        mEditTv.setText("保存");

        mChangeAvatarLayout.setVisibility(View.VISIBLE);

        mNicknameLayout.setVisibility(View.VISIBLE);
        mInfoLayout.setVisibility(View.GONE);

        mSexTv.setVisibility(View.GONE);
        mSexSpinner.setVisibility(View.VISIBLE);
    }

    private void restoreUI(){
        mIsEditMode = false;

        mEditTv.setText("编辑");

        mChangeAvatarLayout.setVisibility(View.GONE);

        mNicknameLayout.setVisibility(View.GONE);
        mInfoLayout.setVisibility(View.VISIBLE);

        mSexTv.setVisibility(View.VISIBLE);
        mSexSpinner.setVisibility(View.GONE);
    }

    private void setUser() {
        mUser = UserInfoHelper.getInstance().getEntity(LoginConfig.getInstance().getAccount());
        if (mUser  == null ) {
            requestUserInfo();
        } else {
            setUserView();
        }
    }

    private void requestUserInfo(){
        showProgressDialog();
        UserInfoManager.getInstance().get(new UserInfoManager.UserInfoListener() {
            @Override
            public void onSuccess(UserEntity user) {
                mUser = user;
                setUserView();
                dismissProgressDialog();
            }

            @Override
            public void onHttpFailure() {
                dismissProgressDialog();
            }
        });
    }

    private void setUserView(){
        Glide.with(this).load(mUser.getObject().getHeadimgurl()).into(mAvatarIm);
        mNameTv.setText(mUser.getObject().getNickname());
        mNicknameEdt.setText(mUser.getObject().getNickname());
        mAccountTv.setText("帐号余额：" + String.valueOf(mUser.getObject().getFunds()));
        int sex = mUser.getObject().getSex();
        if (sex == 1) {
            mSexTv.setText("男");
        } else {
            mSexTv.setText("女");
        }
        setSpinnerItemSelectedByValue(mSexSpinner, mSexTv.getText().toString().trim());
    }

    public static void setSpinnerItemSelectedByValue(Spinner spinner,String value){
        SpinnerAdapter apsAdapter= spinner.getAdapter(); //得到SpinnerAdapter对象
        int k= apsAdapter.getCount();
        for(int i=0;i<k;i++){
            if(value.equals(apsAdapter.getItem(i).toString())){
                spinner.setSelection(i,true);// 默认选中项
                break;
            }
        }
    }

    private void loginOut(){
        LoginConfig.getInstance().logOut();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(IntentItem.LOG_OUT, true);
        startActivity(intent);
    }

    private void update(){
        String nickname = mNicknameEdt.getText().toString().trim();
        if (TextUtils.isEmpty(nickname)) {
            showToastShort("请输入昵称");
            return;
        }
        showProgressDialog();
        UserApi userApi = RetrofitUtils.createApi(UserApi.class);
        String token = ParamsUtil.getToken();
        Call<CommonResponse> request = userApi.updateInfo(token, getParams());
        request.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                dismissProgressDialog();
                CommonResponse entity = response.body();
                if (entity != null) {
                    showToastShort(entity.getMessage());
                    if (entity.isSuccess()) {
                        mIsUpdate = true;
                        restoreUI();
                        setUI();
                    }
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                dismissProgressDialog();
            }
        });
    }

    private Map<String, String> getParams(){
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.users.updateUserInfo");
        if (TextUtils.isEmpty(mUploadAvatarUrl)) {
            mUploadAvatarUrl = mUser.getObject().getHeadimgurl();
        }
        params.put("headimgurl", mUploadAvatarUrl);
        String nickname = mNicknameEdt.getText().toString().trim();
        params.put("nickname", mNicknameEdt.getText().toString().trim());
        int sex;
        if ("男".equals(mSexChecked)) {
            sex = HttpConstant.SexType.MALE;
        } else {
            sex = HttpConstant.SexType.FEMALE;
        }
        params.put("sex", sex + "");
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }

    private void setUI(){
        mNameTv.setText(mNicknameEdt.getText().toString().trim());
        mSexTv.setText(mSexChecked);
    }

    Dialog mCameraDialog;


    private void showTakePictureDialog(){
        LogZ.i("showTakePictureDialog");
        mCameraDialog = new Dialog(this, R.style.TakePictureDialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.view_take_picture, null);
        DialogClickListener listener = new DialogClickListener();
        root.findViewById(R.id.btn_open_camera).setOnClickListener(listener);
        root.findViewById(R.id.btn_choose_img).setOnClickListener(listener);
        root.findViewById(R.id.btn_cancel).setOnClickListener(listener);
        mCameraDialog.setContentView(root);
        Window dialogWindow = mCameraDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.DialogAnimation); // 添加动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = -20; // 新位置Y坐标
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
//		lp.height = WindowManager.LayoutParams.WRAP_CONTENT; // 高度
//		lp.alpha = 9f; // 透明度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight() + 100;
        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
        mCameraDialog.show();
    }

    class DialogClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_open_camera: // 打开照相机
                    takePhoto();
                    break;
                // 打开相册
                case R.id.btn_choose_img:
                    doPickPhotoFromGallery();
                    break;
                // 取消
                case R.id.btn_cancel:
                    if (mCameraDialog != null) {
                        mCameraDialog.dismiss();
                    }
                    break;
            }
        }
    }

    private static final int CAMERA_WITH_DATA = 22;
    private boolean isTakephoto = false;
    private File mCurrentPhotoFile;
    private File mCurrentPhotoFile1;
    private void takePhoto() {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            showToastShort("SD卡不可用");
            return;
        }
        try {
            // Launch camera to take photo for selected contact
            // PHOTO_DIR.mkdirs();
            isTakephoto = true;
            mCurrentPhotoFile = new File(PHOTO_DIR, getPhotoFileName());
            final Intent intent = getTakePickIntent(mCurrentPhotoFile);
            startActivityForResult(intent, CAMERA_WITH_DATA);
        } catch (ActivityNotFoundException e) {
            showToastShort("手机上没有图片");
        }
    }

    public static Intent getTakePickIntent(File f) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        return intent;
    }

    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }

    private static final int PHOTO_PICKED_WITH_DATA = 3021;
    private void doPickPhotoFromGallery() {
        try {
            // Launch picker to choose photo for selected contact
            isTakephoto = false;
            final Intent intent = getPhotoPickIntent();
            startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
        } catch (ActivityNotFoundException e) {
            showToastShort("手机上没有照片");
        }
    }

    private static final int ICON_SIZE = 96;

    public Intent getPhotoPickIntent() {
        Intent intent = null;
        if ("vivo Y22iL".equals(android.os.Build.MODEL)
                || "H300".equals(android.os.Build.MODEL)
                || "S55t".equals(android.os.Build.MODEL)) {
            intent = new Intent("android.intent.action.PICK");
            intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI,
                    "image/*");
        } else {
            intent = new Intent(Intent.ACTION_GET_CONTENT, null);
            intent.setType("image/*");
        }
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", ICON_SIZE);
        intent.putExtra("outputY", ICON_SIZE);

        if ("Lenovo K920".equals(android.os.Build.MODEL)
                || "R7007".equals(android.os.Build.MODEL)
                || "vivo Y22iL".equals(android.os.Build.MODEL)
                || "H300".equals(android.os.Build.MODEL)
                || "X9077".equals(android.os.Build.MODEL)
                || "Coolpad 8021".equals(android.os.Build.MODEL)
                || "Lenovo Z2".equals(android.os.Build.MODEL)
                || "vivo Y27".equals(android.os.Build.MODEL)) {
            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(mCurrentPhotoFile1));
            intent.putExtra("outputFormat",
                    Bitmap.CompressFormat.JPEG.toString());
            intent.putExtra("noFaceDetection", true); // no face detection
        } else {
            intent.putExtra("return-data", true);
        }
        return intent;
    }

    public static Bitmap getMmsImage(Uri partURI, Context context, boolean bSample, float h, float w){ //读取图片附件
        int maxValue = 1024*1024*6;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream is = null;
        Bitmap bitmap=null;
        if (partURI!=null) {
            try {
                is = context.getContentResolver().openInputStream(partURI);
                if(maxValue < is.available()){
                    Toast.makeText(context, "很抱歉！不支持超过6M的图片。", Toast.LENGTH_LONG).show();
                }else {
                    byte[] buffer = new byte[256];
                    int len = -1;
                    while ((len = is.read(buffer)) != -1) {
                        baos.write(buffer, 0, len);
                    }
                    byte[] imageByte = baos.toByteArray();
                    if(bSample) {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        bitmap=BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length, options);    //这时获取到的bitmap是null的，尚未调用系统内存资源
                        options.inJustDecodeBounds = false;
                        int beH = (int)(options.outHeight / h);
                        int beW = (int)(options.outWidth / w);
                        int be = beH < beW ? beW : beH;
                        if (be <= 0)   be = 1;
                        options.inSampleSize = be;          //计算得到图片缩小倍数
                        bitmap = BitmapFactory.decodeByteArray(imageByte, 0,imageByte.length, options);
                    } else{
                        bitmap = BitmapFactory.decodeByteArray(imageByte, 0,imageByte.length);
                    }
                }

            }catch (IOException e) {
                e.printStackTrace();
            }finally{
                if (is != null){
                    try {
                        baos.close();
                        is.close();
                    }catch (IOException e){

                    }
                }
            }
        }

        return bitmap;
    }

    public static Bitmap getCroppedBitmap(Bitmap bitmap, boolean notStroke) {
        if (bitmap == null) {
            return null;
        }
        // 设置显示区域
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int min = (Math.min(width, height));
        // 传入图像为正方形
        // 传入图像为chang方形
        int differ = Math.abs(width - height) / 2;
        if (width > height) {
            bitmap = Bitmap.createBitmap(bitmap, differ, 0, min, min);
        } else if (width < height) {
            bitmap = Bitmap.createBitmap(bitmap, 0, differ, min, min);
        }

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        // 设置一个图片大小的矩形
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        // bm是一个刚好canvas大小的空Bitmap ，画完后应该会自动保存到bm
        Canvas cns = new Canvas(output);

        // 构造渲染器BitmapShader
        BitmapShader bitmapShader = new BitmapShader(bitmap,
                android.graphics.Shader.TileMode.CLAMP,
                android.graphics.Shader.TileMode.CLAMP);

        // 构建ShapeDrawable对象并定义形状为椭圆
        ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
        // 得到画笔并设置渲染器
        shapeDrawable.getPaint().setShader(bitmapShader);

        shapeDrawable.setBounds(1, 1, min - 1, min - 1);
        // 绘制shapeDrawable
        shapeDrawable.draw(cns);
        if (bitmap != output) {
            bitmap.recycle();
        }
        return output;
    }

    private String mUploadAvatarUrl = "";
    private void uploadAvatar(){
        showProgressDialog();
        UserApi userApi = RetrofitUtils.createApi(UserApi.class);
        Call<UploadAvatarEntity> request = userApi.uploadAvatar(getUploadAvatarParams());
        request.enqueue(new Callback<UploadAvatarEntity>() {
            @Override
            public void onResponse(Call<UploadAvatarEntity> call, Response<UploadAvatarEntity> response) {
                dismissProgressDialog();
                UploadAvatarEntity entity = response.body();
                if (entity != null ) {
                    if (entity.isSuccess()) {
                        mUploadAvatarUrl = entity.getObject().getHeadimgurl();
                    } else {
                        showToastShort(entity.getMessage());
                    }
                } else {
                    showToastShort("上传图片失败");
                }

            }

            @Override
            public void onFailure(Call<UploadAvatarEntity> call, Throwable t) {
                dismissProgressDialog();
                if (t != null && t.getMessage() != null) {
                    LogZ.e(t.getMessage());
                }
            }
        });
    }

    private Map<String, String> getUploadAvatarParams(){
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.users.fileUpload");
        byte[] encode = Base64.encode(headPhoto, Base64.DEFAULT);
        String encodeStr = new String(encode);
        params.put("headimgData", "jpg," + encodeStr);
        String oldUrl = mUser.getObject().getHeadimgurl();
        if (TextUtils.isEmpty(oldUrl)) {
            oldUrl = "";
        }
        LogZ.i("oldHeadimgUrl : " + oldUrl);
        params.put("oldHeadimgUrl", oldUrl);
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }

}
