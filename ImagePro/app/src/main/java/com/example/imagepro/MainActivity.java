package com.example.imagepro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;


//public class MainActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//    }
//}


public class MainActivity extends AppCompatActivity {

    private final String IMAGE_TYPE = "image/*";
    private final int IMAGE_CODE = 0; // 这里的IMAGE_CODE是自己任意定义的
    private final int IMAGE_CUT = 1;
    private final int IMAGE_SELECT = 2;
    private static ImageView ImageView_Casda;
    private Button addPic;
    private Button addPic2;
    private ImageView imgShow;
    private TextView imgPath;
    private EditText mEditText;
    private Bitmap mBitmap = null;
    private Bitmap mBitmap02 = null;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE };
    private Uri iconUri;
    private Uri cropImageUri;

    private boolean nfcFlag =true;

//    NfcAdapter mNfcAdapter;//NFC
//    boolean mAndroidBeamAvailable  = false;// Flag to indicate that Android Beam is available

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //从源文件中取出一张图片
        requestAllPower();//获取动态权限
      //  verifyStoragePermissions(this);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic);
        mBitmap02 = mBitmap;        //保存用于重置
        init();

//        if (!PackageManager.hasSystemFeature(PackageManager.FEATURE_NFC)) {
//            /*
//             * Disable NFC features here.
//             * For example, disable menu items or buttons that activate
//             * NFC-related features
//             */
//
//            // Android Beam file transfer isn't supported
//        } else if (Build.VERSION.SDK_INT <
//                Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            // If Android Beam isn't available, don't continue.
//            mAndroidBeamAvailable = false;
//            /*
//             * Disable Android Beam file transfer features here.
//             */
//
//            // Android Beam file transfer is available, continue
//        } else {
//            mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
//
//        }
    }

    //组件
    private void init() {
        addPic = findViewById(R.id.btn_add);
        addPic2 = findViewById(R.id.btn_add3);
        imgPath = findViewById(R.id.img_path);
        imgShow = findViewById(R.id.imgShow);
        mEditText = findViewById(R.id.EditText);
        ImageView_Casda = findViewById(R.id.ImageView_Casda);
        addPic.setOnClickListener(listener);
        addPic2.setOnClickListener(listener);
        //findViewById(R.id.btn_add5).setOnClickListener(listener);
        imgShow.setImageBitmap(mBitmap);

    }

    //动态获取内存存储权限
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

    //请求应用所需所有权限
    public void requestAllPower() {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            //处理图片按照手机的屏幕大小显示
//            if (requestCode == IMAGE_CODE) {
//                Uri uri = data.getData();//获得图片的路径
//                Display display = getWindowManager().getDefaultDisplay();
//                Point point = new Point();
//                display.getSize(point);
//                int width = point.x;//获得屏幕的宽度
//                int height = point.y ;//屏幕高度
//                try {
//                    //实现对图片的裁剪的类，是一个匿名内部类
//                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    options.inJustDecodeBounds = false;
//                    //对图片的宽度和高度对应手机的屏幕进行匹配
//                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);
//                    //如果大于1表示图片的高度大于手机屏幕的高度
//                    int hRatio = (int) Math.ceil(options.outHeight / (float) height);//（int）Math.ceil是下取整
//                    //如果大于1表示图片的宽度大于手机屏幕的宽度
//                    int wRatio = (int) Math.ceil(options.outWidth / (float) width);
//                    //如果hRatio或wRatio大于1，则把图片缩放到1/radio的尺寸和1/radio^2的像素
//                    if (hRatio > 1 || wRatio > 1) {
//                        if (hRatio > wRatio) {
//                            options.inSampleSize = hRatio;
//                        } else {
//                            options.inSampleSize = wRatio;
//                        }
//                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);
//                        ImageView_Casda.setImageBitmap(bitmap);
//                    }else{
//                        //如果hRatio与wRatio为0，直接输出
//                        ImageView_Casda.setImageBitmap(bitmap);
//                    }
//                } catch (Exception e) {
//                }
//                //表示裁剪图片
//            } else if (requestCode == IMAGE_CUT) {
//                Bitmap bitmap = data.getParcelableExtra("data");
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("image/*");//获取任意的图片类型 Set an explicit MIME data type.每个MIME类型由两部分组成，前面是数据的大类别，例如声音audio、图象image等，后面定义具体的种类。
//                intent.putExtra("crop", "true");//滑动选中图片区域
//                intent.putExtra("aspectX", 1);//表示剪切框的比例1:1的效果
//                intent.putExtra("aspectY", 1);
//                intent.putExtra("outputX", 80);//指定输出图片的大小
//                intent.putExtra("outputY", 80);
//                intent.putExtra("return-data", true);//有返回值
//                ImageView_Casda.setImageBitmap(bitmap);
//            }
//        }
//    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // RESULT_OK 是系统自定义得一个常量
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            L.e("onActivityResult,返回的resultCode出错");
            return;
        }
        // 外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
        ContentResolver resolver = getContentResolver();
        // 判断接收的Activity是不是选择图片的
        if (requestCode == IMAGE_CODE) {


            try {
                // 获得图片的地址Uri
                Uri originalUri = data.getData();
                // 新建一个字符串数组用于存储图片地址数据。
                String[] proj = {MediaStore.Images.Media.DATA};
                // android系统提供的接口，用于根据uri获取数据
                Cursor cursor = managedQuery(originalUri, proj, null, null, null);
                // 获得用户选择图片的索引值
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                L.e("获得用户选择图片的索引值:" + column_index);
                // 将游标移至开头 ，防止引起队列越界
                cursor.moveToFirst();
                // 根据图片的URi生成bitmap
                mBitmap = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                mBitmap02 = mBitmap; //保存用于重置
                // 显得到bitmap图片
                imgShow.setImageBitmap(mBitmap);
                imgPath.setText(String.valueOf(originalUri));
            } catch (IOException e) {
                Log.e("getImg", e.toString());
            }
        }
        else if (requestCode == IMAGE_SELECT) {
            if (data.getData() != null) {
                iconUri = data.getData();
                try {
                    startCropImage(iconUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else if (requestCode == IMAGE_CUT) {
            imgPath.setText(String.valueOf(cropImageUri));
            verifyStoragePermissions(this);
            try {
                //mBitmap = MediaStore.Images.Media.getBitmap(resolver, cropImageUri);
               mBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(cropImageUri));
                imgShow.setImageBitmap(mBitmap); // 将裁剪后的照片显示出来
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    //按钮监听
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            Button btn = (Button) v;
            switch (btn.getId()) {
                case R.id.btn_add:
                    setImage();
                    break;
                case R.id.btn_add3:
                    Intent intent = new Intent(Intent.ACTION_PICK, null);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(intent, IMAGE_SELECT);
                    break;
//                case R.id.btn_add5:
//                    System.out.println("666");
//                    Intent intent2 =new Intent(MainActivity.this,page1.class);
//                    startActivity(intent2);
//                    break;
            }
        }
        //相册
        private void setImage() {
            // TODO Auto-generated method stub
            // 使用intent调用系统提供的相册功能，使用startActivityForResult是为了获取用户选择的图片的地址
            Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
            getAlbum.setType(IMAGE_TYPE);
            startActivityForResult(getAlbum, IMAGE_CODE);
        }
    };

    //根据当前时间生成图片名
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".bmp";
    }

    //裁减图片操作
    private void startCropImage(Uri uri) throws IOException {
        ContentResolver resolver = getContentResolver();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        mBitmap = MediaStore.Images.Media.getBitmap(resolver, uri);
        mBitmap02 = mBitmap; //保存用于重置
        // 显得到bitmap图片
        imgShow.setImageBitmap(mBitmap);
        // 使图片处于可裁剪状态
        intent.putExtra("crop", "true");
        // 裁剪框的比例（根据需要显示的图片比例进行设置）
        if (Build.MANUFACTURER.contains("HUAWEI")) {
            //硬件厂商为华为的，默认是圆形裁剪框，这里让它无法成圆形
            intent.putExtra("aspectX", 9999);
            intent.putExtra("aspectY", 9998);
        }else{
            //其他手机一般默认为方形
            intent.putExtra("aspectX", 2);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 240);
            intent.putExtra("outputY", 120);
        }
        // 设置裁剪区域的形状，默认为矩形，也可设置为圆形，可能无效
//        intent.putExtra("circleCrop", true);
        // 让裁剪框支持缩放
        intent.putExtra("scale", true);
        String imName=getPhotoFileName();
        // 传递原图路径
        File cropFile = new File(Environment.getExternalStorageDirectory().getPath() + "/DCIM/" +imName);

//        try {
//            if (cropFile.exists()) {
//                cropFile.delete();
//            }
//            cropFile.createNewFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        System.out.println("path");
        System.out.println(cropFile.getAbsoluteFile());
        cropImageUri = Uri.fromFile(cropFile.getAbsoluteFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropImageUri);

        // 设置图片的输出格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        // return-data=true传递的为缩略图，小米手机默认传递大图，所以会导致onActivityResult调用失败
        intent.putExtra("return-data", false);

        startActivityForResult(intent, IMAGE_CUT);
    }

    //运行二值化按钮
    public void btnRun(View view) {

        //值域限制
        if (mEditText.getText().toString().equals("")) {
            Toast.makeText(MainActivity.this, "请先输入值域", Toast.LENGTH_SHORT).show();
        } else {
            Float value = Float.valueOf(mEditText.getText().toString());
            if (value >= 255) {
                value = 255F;
            }
            if (value <= 0) {
                value = 0F;
            }

            // 将图片转化成黑白图片
            mBitmap = convertToBMW(mBitmap, value);
            // 显得到bitmap图片
            ImageView_Casda.setImageBitmap(mBitmap);
        }
    }

    //运行灰度按钮
    public void btnRun2(View view) {

       // mBitmap = mBitmap02; //重置
        // 将图片转化成黑白图片
        mBitmap = convertToGREY(mBitmap);
        // 显得到bitmap图片
        ImageView_Casda.setImageBitmap(mBitmap);

    }

    //运行抖动按钮
    public void btnRun3(View view) {

       // mBitmap = mBitmap02; //重置
        // 将图片转化成黑白图片
        mBitmap = convertToGREY(mBitmap);
        mBitmap=convertGreyImgByFloyd(mBitmap);
        // 显得到bitmap图片
        ImageView_Casda.setImageBitmap(mBitmap);


    }

    //运行保存按钮
    public void btnRun4(View view) throws IOException {

        String imName=getPhotoFileName();
        OutputStream fOut = null;
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/DCIM/",imName);
        fOut = new FileOutputStream(file);
        mBitmap.compress(Bitmap.CompressFormat.PNG, 0, fOut);
        fOut.flush();
        fOut.close();
        Toast.makeText(MainActivity.this,"保存成功",Toast.LENGTH_SHORT).show();


    }

    //运行读卡按钮
    public void btnRun5(View view) {
//        this.setContentView(R.layout.page1);

        Intent intent=new Intent(MainActivity.this,page1.class);

        //intent.putExtra("cc",new User());
        startActivity(intent);
        //startActivityForResult(intent,RESULT_OK);


//        Intent intent = new Intent();
//        intent.setClass(MainActivity.this,page1.class);
//        startActivity(intent);
    }

    //运行重置按钮
    public void btnReset(View view) {
        ImageView_Casda.setImageBitmap(mBitmap02);
        mBitmap = mBitmap02; //重置
    }

    // 转为二值图像
    public static Bitmap convertToBMW(Bitmap bmp, Float tmp) {
        int width = bmp.getWidth(); // 获取位图的宽
        int height = bmp.getHeight(); // 获取位图的高
        int[] pixels = new int[width * height]; // 通过位图的大小创建像素点数组
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        int alpha = 0xFF << 24;
        String Colors = null;
        for (int i = 0; i < height; i++) {
            StringBuilder sb = new StringBuilder();    //创建一个 StringBuilder 对象，用来储存矩阵数据
            for (int j = 0; j < width; j++) {
                int grey = pixels[width * i + j];
                // 分离三原色
                alpha = ((grey & 0xFF000000) >> 24);
                int red = ((grey & 0x00FF0000) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);
                if (red > tmp) {
                    red = 255;
                } else {
                    red = 0;
                }
                if (blue > tmp) {
                    blue = 255;
                } else {
                    blue = 0;
                }
                if (green > tmp) {
                    green = 255;
                } else {
                    green = 0;
                }
                pixels[width * i + j] = alpha << 24 | red << 16 | green << 8 | blue;
                if (pixels[width * i + j] == -1) {
                    pixels[width * i + j] = -1;
                    Colors = ".";
                } else {
                    pixels[width * i + j] = -16777216;
                    Colors = "0";
                }
                sb.append(Colors);
            }
            // L.e("" + sb.toString());
        }
        L.e("二值化的域值:" + tmp);
        // 新建图片
        Bitmap newBmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        // 设置图片数据
        newBmp.setPixels(pixels, 0, width, 0, 0, width, height);
        Bitmap resizeBmp = ThumbnailUtils.extractThumbnail(newBmp, width, height);

        return resizeBmp;
    }

    //转为灰度图像
    public static Bitmap convertToGREY(Bitmap bmp) {
        int width = bmp.getWidth(); // 获取位图的宽
        int height = bmp.getHeight(); // 获取位图的高
        int[] pixels = new int[width * height]; // 通过位图的大小创建像素点数组

        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        int alpha = 0xFF << 24;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int grey = pixels[width * i + j];

                //分离三原色
                int red = ((grey & 0x00FF0000) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);

                //转化成灰度像素
                grey = (int) (red * 0.3 + green * 0.59 + blue * 0.11);
                grey = alpha | (grey << 16) | (grey << 8) | grey;
                pixels[width * i + j] = grey;
            }
        }
        //新建图片
        Bitmap newBmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        //设置图片数据
        newBmp.setPixels(pixels, 0, width, 0, 0, width, height);
        //居中
//        Bitmap resizeBmp = ThumbnailUtils.extractThumbnail(newBmp, 380, 460);
        return newBmp;
    }

     //图像抖动
    public static Bitmap convertGreyImgByFloyd(Bitmap img) {


        int width = img.getWidth();         //获取位图的宽
        int height = img.getHeight();       //获取位图的高


        int[] pixels = new int[width * height]; //通过位图的大小创建像素点数组


        img.getPixels(pixels, 0, width, 0, 0, width, height);
        int[] gray=new int[height*width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int grey = pixels[width * i + j];
                int red = ((grey  & 0x00FF0000 ) >> 16);
                gray[width*i+j]=red;
            }
        }


        int e=0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int g=gray[width*i+j];
                if (g>=128) {
                    pixels[width*i+j]=0xffffffff;
                    e=g-255;


                }else {
                    pixels[width*i+j]=0xff000000;
                    e=g-0;
                }
                if (j<width-1&&i<height-1) {

                    gray[width*i+j+1]+=3*e/8;//右边像素处理

                    gray[width*(i+1)+j]+=3*e/8;//下

                    gray[width*(i+1)+j+1]+=e/4;//右下
                }else if (j==width-1&&i<height-1) {//靠右或靠下边的像素的情况

                    gray[width*(i+1)+j]+=3*e/8;//下方像素处理
                }else if (j<width-1&&i==height-1) {

                    gray[width*(i)+j+1]+=e/4;//右边像素处理
                }
            }


        }

        Bitmap mBitmap=Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        mBitmap.setPixels(pixels, 0, width, 0, 0, width, height);

        return mBitmap;
    }



//    @Override
//    protected void onResume() {
//        super.onResume();
//        //开启前台调度系统
//        NfcUtils.mNfcAdapter.enableForegroundDispatch(this, NfcUtils.mPendingIntent, NfcUtils.mIntentFilter, NfcUtils.mTechList);
//    }
//    @Override
//    protected void onPause() {
//        super.onPause();
//        //关闭前台调度系统
//        NfcUtils.mNfcAdapter.disableForegroundDispatch(this);
//    }
//
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        //当该Activity接收到NFC标签时，运行该方法
//        //调用工具方法，读取NFC数据
//        try {
//            String str = NfcUtils.readNFCFromTag(intent);
//            imgPath.setText(str);
//        } catch (UnsupportedEncodingException e) {
//
//            e.printStackTrace();
//        }
//    }

}

