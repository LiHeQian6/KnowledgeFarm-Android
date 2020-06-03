package com.li.knowledgefarm.Main;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Matrix;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.li.knowledgefarm.Login.LoginActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.li.knowledgefarm.MyFriends.FriendsPopUpWindow;
import com.li.knowledgefarm.R;
import com.li.knowledgefarm.Settings.SettingMessageActivity;
import com.li.knowledgefarm.Shop.ShopActivity;
import com.li.knowledgefarm.Study.SubjectListActivity;
import com.li.knowledgefarm.Study.Util.AppUtil;
import com.li.knowledgefarm.Study.Util.setDensityLand;
import com.li.knowledgefarm.Util.CustomerToast;
import com.li.knowledgefarm.Util.DisplayUtils;
import com.li.knowledgefarm.Util.FullScreen;
import com.li.knowledgefarm.Util.GuideHelper;
import com.li.knowledgefarm.Util.GuideHelper.TipData;
import com.li.knowledgefarm.Util.NavigationBarUtil;
import com.li.knowledgefarm.Util.OkHttpUtils;
import com.li.knowledgefarm.Util.UserUtil;
import com.li.knowledgefarm.bag.BagPopUpWindow;
import com.li.knowledgefarm.daytask.DayTaskPopUpWindow;
import com.li.knowledgefarm.entity.DoTaskBean;
import com.li.knowledgefarm.entity.EventBean;
import com.li.knowledgefarm.entity.User;
import com.li.knowledgefarm.entity.UserCropItem;
import com.li.knowledgefarm.entity.UserPetHouse;
import com.li.knowledgefarm.notify.NotifyActivity;
import com.li.knowledgefarm.pet.PetPopUpWindow;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private ImageView learn;
    private ImageView water;
    private TextView waterCount;
    private ImageView fertilizer;
    private TextView fertilizerCount;
    private ImageView bag;
    private ImageView shop;
    private ImageView pet;
    private ImageView setting;
    private ImageView photo;
    private ImageView harvest;
    private ImageView xzw;
    private ImageView xzf;
    private ImageView xzs;
    private ImageView myFriends;
    private TextView nickName;
    private TextView level;
    private TextView account;
    private TextView money;
    private ProgressBar experience;
    private TextView experienceValue;
    private FrameLayout lands;
    private FrameLayout land_background;
    private BagPopUpWindow bagPopUpWindow;
    private Dialog ifExtention;
    private OkHttpClient okHttpClient;
    private Handler bagMessagesHandler;
    private Gson gson;
    private List<UserCropItem> cropList;
    private Handler UpdataLands;
    private Handler cropMessagesHandler;
    private int selectLand = 0;//选中第几块土地
    private Handler plantMessagesHandler;
    private long lastClickTime = 0;
    private long FAST_CLICK_DELAY_TIME = 500;
    private Handler waterMessagesHandler;
    private int selected = -2;//选中的是水壶0，肥料-1，收获-2
    private Handler operatingHandleMessage;
    private int selectedPlant = 0;//选中的植物是第几块土地
    private int displayWidth;
    private int displayHeight;
    private int ExtensionLandMoney = 0;
    private ImageView notify;
    private NotifyActivity notifyActivity;
    private UserMessagePopUp userMessagePopUp;
    private ListView notify_list_view;
    private float LAND_WIDTH_2;
    private float LAND_HEIGHT_2;
    private Handler friendMessagesHandler;
    private ImageView dayTask;
    private DayTaskPopUpWindow dayTaskPopUpWindow;
    private ImageView notify_red;
    private ImageView daytask_red;
    public static List<Boolean> notifyStatus=new ArrayList<>(5);
    private Handler new_notification;
    private FriendsPopUpWindow friendsPopUpWindow;
    private PetPopUpWindow petPopUpWindow;
    private ImageView dog;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FullScreen.NavigationBarStatusBar(MainActivity.this,true);
        setContentView(R.layout.activity_main);
        okHttpClient = OkHttpUtils.getInstance(this);
        gson = new Gson();
        getViews();
        addListener();
        getCrop();
        guide();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        FullScreen.NavigationBarStatusBar(MainActivity.this,true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 启动服务播放背景音乐
//        intent = new Intent(MainActivity.this, BgSoundService.class);
//        String action = BgSoundService.ACTION_MUSIC;
//        // 设置action
//        intent.setAction(action);
//        startService(intent);
        FullScreen.NavigationBarStatusBar(MainActivity.this,true);
        setDensityLand.setDensity(getApplication());
        setDensityLand.setOrientation(this, AppUtil.HEIGHT);
        getUserInfo();
        showUserInfo();
        haveNewNotifications();
    }

    /**
     * 获取用户信息
     */
    private void getUserInfo() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                Request request = new Request.Builder()
                        .url(getResources().getString(R.string.URL) + "/user/findUserInfoByUserId")
                        .build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("用户信息", "请求失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        OkHttpUtils.unauthorized(response.code());
                        String result = response.body().string();
                        if (result.equals("{}")) {
                            Log.e("用户信息", "信息异常");
                        } else {
                            Log.e("用户信息", result);
                            Message message = new Message();
                            message.obj = LoginActivity.parsr(URLDecoder.decode(result), User.class);
                            UserUtil.setUser ((User)message.obj);
                            Message msg = new Message();
                            msg.obj = "true";
                            operatingHandleMessage.sendMessage(msg);
                        }

                    }
                });
            }
        }.start();
        operatingHandleMessage = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String messages = (String) msg.obj;
                Log.e("operating", messages);
                if (!messages.equals("Fail")) {
                    if (bagPopUpWindow != null) {
                        bagPopUpWindow.dismiss();
                        selectLand = 0;
                    }
                    showUserInfo();
                } else {
                    CustomerToast.getInstance(MainActivity.this, "网络异常！", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    /**
     * 展示用户信息
     */
    private void showUserInfo() {
        User user = UserUtil.getUser();
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.huancun2)
                .error(R.drawable.meigui)
                .fallback(R.drawable.meigui)
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(this).load(UserUtil.getUser().getPhoto()).apply(requestOptions).into(photo);
        nickName.setText(UserUtil.getUser().getNickName());
        account.setText("账号:" + UserUtil.getUser().getAccount());
        level.setText(" Lv" + UserUtil.getUser().getLevel());
        money.setText(" 金币:" + UserUtil.getUser().getMoney());
        waterCount.setText(UserUtil.getUser().getWater() + "");
        fertilizerCount.setText(UserUtil.getUser().getFertilizer() + "");
        int[] levelExperience = getResources().getIntArray(R.array.levelExperience);
        int l = UserUtil.getUser().getLevel();
        experience.setMax(levelExperience[l] - levelExperience[l - 1]);
        experience.setProgress((int) UserUtil.getUser().getExperience() - levelExperience[l - 1]);
        experienceValue.setText("" + UserUtil.getUser().getExperience() + "/" + levelExperience[l]);
        List<UserPetHouse> petHouses = UserUtil.getUser().getPetHouses();
        if (petHouses.size()!=0) {
            UserPetHouse userPetHouse = UserUtil.getUser().getPetHouses().get(0);
            String url = userPetHouse.getGrowPeriod() == 0 ? userPetHouse.getPet().getImg1() : userPetHouse.getGrowPeriod()==1? userPetHouse.getPet().getImg2() : userPetHouse.getPet().getImg3();
            Glide.with(this).load(url).error(R.drawable.dog).into(dog);
        }else
            Glide.with(this).load(R.drawable.dog).into(dog);
    }

    /**
     * 用户升级,弹窗提示
     */
    private void upLevel() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.up_level_dialog, null);
        //Button right = layout.findViewById(R.id.right);
        alertBuilder.setView(layout);
        final AlertDialog upDiaLog = alertBuilder.create();
        NavigationBarUtil.focusNotAle(upDiaLog.getWindow());
        upDiaLog.show();
        NavigationBarUtil.hideNavigationBar(upDiaLog.getWindow());
        NavigationBarUtil.clearFocusNotAle(upDiaLog.getWindow());
        WindowManager.LayoutParams attrs = upDiaLog.getWindow().getAttributes();
        if (upDiaLog.getWindow() != null) {
            //bagDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            upDiaLog.getWindow().setDimAmount(0f);//去除遮罩
        }
        attrs.gravity = Gravity.CENTER;
        final float scale = this.getResources().getDisplayMetrics().density;
        attrs.width = (int) (300 * scale + 0.5f);
        attrs.height = (int) (250 * scale + 0.5f);
        upDiaLog.getWindow().setAttributes(attrs);
        Window dialogWindow = upDiaLog.getWindow();
        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);
    }

    /**
     * 展示朋友信息
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showFriends() {
        myFriends.setVisibility(View.GONE);
        friendsPopUpWindow = new FriendsPopUpWindow(this);
        //获取屏幕显示区域尺寸
        WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics ds = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(ds);
        displayHeight = ds.heightPixels;
        displayWidth = ds.widthPixels;
        friendsPopUpWindow.setHeight((int)(displayHeight*0.95));
        friendsPopUpWindow.setWidth((int)(displayWidth*0.40));
        if (!isNavigationBarShow()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                friendsPopUpWindow.setIsClippedToScreen(true);
            }
        }
        friendsPopUpWindow.showAtLocation(myFriends,Gravity.RIGHT,0,0);
        friendsPopUpWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                myFriends.setVisibility(View.VISIBLE);
            }
        });

    }

    /**
     * @Author li
     * @param
     * @return boolean
     * @Description 虚拟按键是否显示
     * @Date 15:32 2020/5/5
     **/
    public boolean isNavigationBarShow(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            Point realSize = new Point();
            display.getSize(size);
            display.getRealSize(realSize);
            boolean  result  = realSize.y!=size.y;
            return realSize.y!=size.y;
        }else {
            boolean menu = ViewConfiguration.get(this).hasPermanentMenuKey();
            boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            if(menu || back) {
                return false;
            }else {
                return true;
            }
        }
    }

    /**
     * @param add
     * @return void
     * @Author li
     * @Description 展示添加删除好友弹窗
     * @Date 18:04 2020/4/24
     **/
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showDaiLog(final HashMap add) {
        final Dialog dialog = new Dialog(this);
        View view = View.inflate(this, R.layout.math_return_dialog, null);
        FullScreen.hideBottomUIMenu(view);
        TextView text = view.findViewById(R.id.waringText);
        ImageView cancel = view.findViewById(R.id.cancel_return);
        ImageView sure = view.findViewById(R.id.sure_return);
        if (((boolean) add.values().toArray()[0])) {
            text.setText("你确定添加ta为好友并发送申请吗？");
            sure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int option = 0;//0表示加好友
                    operateFriend((String) add.keySet().toArray()[0], option);
                    dialog.dismiss();
                }
            });
        } else {
            text.setText("你确定从好友列表删除ta吗？");
            sure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int option = 1;//1表示删好友
                    operateFriend((String) add.keySet().toArray()[0], option);
                    dialog.dismiss();
                }
            });
        }
        dialog.setContentView(view);
        NavigationBarUtil.focusNotAle(dialog.getWindow());
        dialog.show();
        NavigationBarUtil.hideNavigationBar(dialog.getWindow());
        NavigationBarUtil.clearFocusNotAle(dialog.getWindow());
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        WindowManager.LayoutParams attrs = dialog.getWindow().getAttributes();
        if (dialog.getWindow() != null) {
            //bagDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setDimAmount(0f);//去除遮罩
        }
        attrs.gravity = Gravity.CENTER;
        final float scale = this.getResources().getDisplayMetrics().density;
        attrs.width = (int) (300 * scale + 0.5f);
        attrs.height = (int) (200 * scale + 0.5f);
        dialog.getWindow().setAttributes(attrs);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);
    }

    /**
     * @param num
     * @param option
     * @return void
     * @Author li
     * @Description 用户对好友的操作
     * @Date 18:04 2020/4/24
     **/
    private void operateFriend(final String num, final int option) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                Request request = null;
                if (option == 0)
                    request = new Request.Builder().url(getResources().getString(R.string.URL) + "/notification/addUserFriendNotification?account=" + num).build();
                else if (option == 1) {
                    request = new Request.Builder().url(getResources().getString(R.string.URL) + "/userfriend/deleteUserFriend?account=" + num).build();
                }
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Message message = Message.obtain();
                        message.obj = "Fail";
                        friendMessagesHandler.sendMessage(message);
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        OkHttpUtils.unauthorized(response.code());
                        Message message = Message.obtain();
                        message.obj = response.body().string();
                        friendMessagesHandler.sendMessage(message);
                    }
                });
            }
        }.start();
        friendMessagesHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String messages = (String) msg.obj;
                if (!messages.equals("Fail")) {
                    if (messages.equals("false")) {
                        CustomerToast.getInstance(MainActivity.this, option == 0 ? "申请失败！" : "删除失败！", Toast.LENGTH_SHORT).show();
                    }else {
                        CustomerToast.getInstance(MainActivity.this, option == 0 ? "申请成功！" : "删除成功！", Toast.LENGTH_SHORT).show();
                        if (option == 1){
                            List<User> list = friendsPopUpWindow.friendsPage.getList();
                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i).getAccount().equals(num)) {
                                    list.remove(i);
                                    break;
                                }
                            }
                            friendsPopUpWindow.customerAdapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    CustomerToast.getInstance(MainActivity.this, "网络异常！", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }


    /**
     * @param
     * @return void
     * @Author li
     * @Description 展示每日任务弹窗
     * @Date 21:00 2020/4/23
     **/
    private void showDayTaskWindow() {
        dayTaskPopUpWindow = new DayTaskPopUpWindow(this);
        dayTaskPopUpWindow.showAtLocation(dayTask, Gravity.CENTER, 0, 0);
        dayTaskPopUpWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                getUserInfo();
            }
        });
    }

    /**
     * @param
     * @return void
     * @Author li
     * @Description 关闭每日任务弹窗
     * @Date 21:02 2020/4/23
     **/
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void closeDayTaskWindow(DoTaskBean doTaskBean) {
        dayTaskPopUpWindow.dismiss();
        if (doTaskBean.isToFriend()) {
            showFriends();
        }
    }


    /**
     * 获取种植的作物信息
     */
    private void getCrop() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                Request request = new Request.Builder().url(getResources().getString(R.string.URL) + "/usercrop/initUserCrop?userId="+UserUtil.getUser().getId()).build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Message message = Message.obtain();
                        message.obj = "Fail";
                        cropMessagesHandler.sendMessage(message);
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        OkHttpUtils.unauthorized(response.code());
                        Message message = Message.obtain();
                        message.obj = response.body().string();
                        cropMessagesHandler.sendMessage(message);
                    }
                });
            }
        }.start();
        cropMessagesHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String messages = (String) msg.obj;
                Log.e("cropList", messages);
                if (!messages.equals("Fail")) {
                    Type type = new TypeToken<List<UserCropItem>>() {
                    }.getType();
                    cropList = gson.fromJson(messages, type);
                    showLand();
                } else {
                    CustomerToast.getInstance(MainActivity.this, "网络异常！", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    /**
     * 初始化土地
     */
    private void showLand() {
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics ds = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(ds);
        displayHeight = (int) (ds.heightPixels/ds.density);
        displayWidth = (int) (ds.widthPixels/ds.density);
        lands.removeAllViews();
        int flag = 0;
        float x = 0;
        float y = 0;
        for (int i = 0; i < 18; i++) {
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            final View landGroup = inflater.inflate(R.layout.land_group, null);
            int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    displayWidth/6, getResources().getDisplayMetrics());
            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    displayWidth/12, getResources().getDisplayMetrics());
            LAND_WIDTH_2=width/2.1f;
            LAND_HEIGHT_2=height/2.1f;
            land_background.setTranslationX(-LAND_WIDTH_2*1.2f);
            land_background.getLayoutParams().width= (int) (LAND_WIDTH_2*12);
            FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(width, height);
            landGroup.setLayoutParams(param);
            int num = i + 1;
            if ((num - 1) % 3 == 0) {
                x = ((num - 1) / 3) * LAND_WIDTH_2 + LAND_WIDTH_2 * 2;
                y = ((num - 1) / 3) * LAND_HEIGHT_2;
                landGroup.setTranslationX(x);
                landGroup.setTranslationY(y);
            } else {
                x = x - LAND_WIDTH_2;
                landGroup.setTranslationX(x);
                y = y + LAND_HEIGHT_2;
                landGroup.setTranslationY(y);
            }
            landGroup.setTag("" + num);
            final ImageView land = landGroup.findViewWithTag("land");
            ImageView plant = landGroup.findViewWithTag("plant");
            TextView progressNum = landGroup.findViewWithTag("progressNum");
            ProgressBar progress = landGroup.findViewWithTag("progress");
            final ImageView animation = landGroup.findViewWithTag("animation");
            final int finalI = Integer.parseInt((String) landGroup.getTag());//第几块土地
            if (cropList.get(i) == null) {//cropItem为null表示土地未开垦，crop为null代表未种植，不为null为种植的对应植物，当第一次运行到的时候表示该块土地上是扩建牌
                if (flag == 0) {
                    plant.setVisibility(View.VISIBLE);
                    int w = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                            60, getResources().getDisplayMetrics());
                    int h = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                            60, getResources().getDisplayMetrics());
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(w, h);
                    params.gravity = Gravity.CENTER;
                    params.topMargin = -90;
                    plant.setLayoutParams(params);
                    plant.setImageResource(R.drawable.kuojian);
                    //扩建
                    plant.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                                System.out.println("x" + motionEvent.getX() + "   y" + motionEvent.getY());
                                if (isSelectLand(motionEvent.getX(), motionEvent.getY())) {
                                    System.out.println(landGroup.getTag());
                                    showIfExtensionLand(finalI);
                                    return true;
                                }
                            }
                            return false;
                        }
                    });
                    flag++;
                }
//                land.setImageResource(R.drawable.land_green);
            } else if (cropList.get(i).getCrop() == null) {
                land.setImageResource(R.drawable.land);
                //种植
                land.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                            if (isSelectLand(motionEvent.getX(), motionEvent.getY())) {
                                if (System.currentTimeMillis() - lastClickTime < FAST_CLICK_DELAY_TIME) {
                                    return true;
                                }
                                lastClickTime = System.currentTimeMillis();
                                land.setImageResource(R.drawable.land_lights);
                                selectLand = finalI;
                                showBagMessages();
                                return true;
                            }
                        }
                        return false;
                    }
                });
            } else {
                RequestOptions requestOptions = new RequestOptions()
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.meigui)
                        .fallback(R.drawable.meigui)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
                land.setImageResource(R.drawable.land);
                UserCropItem crop = cropList.get(i);
                if (crop != null) {
                    plant.setVisibility(View.VISIBLE);
                    progress.setVisibility(View.VISIBLE);
                    progressNum.setVisibility(View.VISIBLE);
                    //展示植物不同阶段
                    final double status = (crop.getProgress() + 0.0) / crop.getCrop().getMatureTime();
                    if (status < 0.2) {
                        plant.setTranslationY(80);
                        plant.setImageResource(R.drawable.seed);
                    } else if (status < 0.3) {
                        Glide.with(this).load(crop.getCrop().getImg1()).apply(requestOptions).into(plant);
                    } else if (status < 0.6) {
                        Glide.with(this).load(crop.getCrop().getImg2()).apply(requestOptions).into(plant);
                    } else if (status < 1) {
                        Glide.with(this).load(crop.getCrop().getImg3()).apply(requestOptions).into(plant);
                    } else if (status == 1) {
                        Glide.with(this).load(crop.getCrop().getImg4()).apply(requestOptions).into(plant);
                    }
                    //植物成长进度条
                    progress.setMax(crop.getCrop().getMatureTime());
                    progress.setProgress(crop.getProgress());

                    //植物成长值
                    progressNum.setText(crop.getProgress() + "/" + crop.getCrop().getMatureTime());

                    if (crop.getStatus() == 0) {
                        land.setImageResource(R.drawable.land_gan);
                    }
                    //浇水、施肥、收获
                    final UserCropItem finalCrop = crop;
                    land.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                                if (isSelectLand(motionEvent.getX(), motionEvent.getY())) {
                                    if (finalCrop.getStatus() == 0) {
                                        land.setImageResource(R.drawable.land_gan_light);
                                    } else
                                        land.setImageResource(R.drawable.land_lights);
                                    if (selected == 0) {
                                        selectedPlant = finalI;
                                        if (status == 1) {
                                            CustomerToast.getInstance(MainActivity.this, "植物已经成熟哦！", Toast.LENGTH_SHORT).show();
                                            if (finalCrop.getStatus() == 0) {
                                                land.setImageResource(R.drawable.land_gan);
                                            } else
                                                land.setImageResource(R.drawable.land);
                                        } else {
                                            Glide.with(MainActivity.this).asGif().load(R.drawable.jiaoshui).into(animation);
                                            if (UserUtil.getUser().getWater() == 0){
                                                CustomerToast.getInstance(MainActivity.this,"水不够了哦！快去学习中心获得吧！",Toast.LENGTH_SHORT).show();
                                                if (finalCrop.getStatus() == 0) {
                                                    land.setImageResource(R.drawable.land_gan);
                                                } else
                                                    land.setImageResource(R.drawable.land);
                                            }else{
                                                operating(0);//浇水
                                            }
                                        }
                                    } else if (selected == -1) {
                                        selectedPlant = finalI;
                                        if (status == 1) {
                                            CustomerToast.getInstance(MainActivity.this, "植物已经成熟哦！", Toast.LENGTH_SHORT).show();
                                            if (finalCrop.getStatus() == 0) {
                                                land.setImageResource(R.drawable.land_gan);
                                            } else
                                                land.setImageResource(R.drawable.land);
                                        } else {
                                            Glide.with(MainActivity.this).asGif().load(R.drawable.shifei).into(animation);
                                            if (UserUtil.getUser().getFertilizer() == 0){
                                                CustomerToast.getInstance(MainActivity.this,"肥料不够了哦！快去学习中心获得吧！",Toast.LENGTH_SHORT).show();
                                                if (finalCrop.getStatus() == 0) {
                                                    land.setImageResource(R.drawable.land_gan);
                                                } else
                                                    land.setImageResource(R.drawable.land);
                                            }else {
                                                operating(-1);//施肥
                                            }
                                        }
                                    } else {
                                        selectedPlant = finalI;
                                        if (status == 1) {
                                            Glide.with(MainActivity.this).asGif().load(R.drawable.shouhuog).into(animation);
                                            operating(-2);//成熟
                                        } else {
                                            CustomerToast.getInstance(MainActivity.this, "植物还没有成熟哦！", Toast.LENGTH_SHORT).show();
                                            if (finalCrop.getStatus() == 0) {
                                                land.setImageResource(R.drawable.land_gan);
                                            } else
                                                land.setImageResource(R.drawable.land);
                                        }
                                    }
                                    waterMessagesHandler = new Handler() {
                                        @Override
                                        public void handleMessage(@NonNull Message msg) {
                                            final String messages = (String) msg.obj;
                                            Log.e("Watering", messages);
                                            if (!messages.equals("Fail")) {
                                                if (messages.equals("false")) {
                                                    animation.setVisibility(View.VISIBLE);
                                                    CustomerToast.getInstance(MainActivity.this, "操作失败！", Toast.LENGTH_SHORT).show();
                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            animation.setVisibility(View.GONE);
                                                        }
                                                    }, 1000);
                                                } else {
                                                    animation.setVisibility(View.VISIBLE);
                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            getCrop();
                                                            getUserInfo();
                                                            if (messages.equals("up")) {
                                                                upLevel();
                                                            }
                                                            animation.setVisibility(View.GONE);
                                                        }
                                                    }, 1000);

                                                }
                                            } else {
                                                animation.setVisibility(View.VISIBLE);
                                                CustomerToast.getInstance(MainActivity.this, "网络异常！", Toast.LENGTH_SHORT).show();
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        animation.setVisibility(View.GONE);
                                                    }
                                                }, 1000);
                                            }
                                            if (finalCrop.getStatus() == 0) {
                                                land.setImageResource(R.drawable.land_gan);
                                            } else
                                                land.setImageResource(R.drawable.land);
                                        }
                                    };
                                    return true;
                                }
                            }
                            return false;
                        }
                    });
                }

            }
            lands.addView(landGroup);
        }

    }

    /**
     * @param x 横坐标
     * @param y 纵坐标
     * @return boolean
     * @Author li
     * @Description 判断是否选中该块土地(即点击的区域是否在菱形区域内)
     * @Date 12:19 2020/3/19
     **/
    public boolean isSelectLand(float x, float y) {
        float rx = (x - LAND_WIDTH_2) * LAND_HEIGHT_2;
        float ry = (LAND_HEIGHT_2 - y) * LAND_WIDTH_2;
        if ((Math.abs(rx) + Math.abs(ry)) <= (LAND_WIDTH_2 * 2 * LAND_HEIGHT_2 * 2 / 4)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 询问扩建
     *
     * @param position
     */
    private void showIfExtensionLand(final int position) {
        ifExtention = new Dialog(this);
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.math_return_dialog, null);
        ImageView cancel = layout.findViewById(R.id.cancel_return);
        ImageView sure = layout.findViewById(R.id.sure_return);
        TextView waring = layout.findViewById(R.id.waringText);
        ExtensionLandMoney = (200 * position - 800);
        waring.setText("你是否要花费" + ExtensionLandMoney + "金币来扩建这块土地？");
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ifExtention.dismiss();
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExtensionLand(position, (200 * position - 800));
                UpdateLand(position);
            }
        });
        ifExtention.setContentView(layout);
        NavigationBarUtil.focusNotAle(ifExtention.getWindow());
        ifExtention.show();
        NavigationBarUtil.hideNavigationBar(ifExtention.getWindow());
        NavigationBarUtil.clearFocusNotAle(ifExtention.getWindow());
        WindowManager.LayoutParams attrs = ifExtention.getWindow().getAttributes();
        if (ifExtention.getWindow() != null) {
            //bagDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            ifExtention.getWindow().setDimAmount(0f);//去除遮罩
        }
        attrs.gravity = Gravity.CENTER;
        final float scale = this.getResources().getDisplayMetrics().density;
        attrs.width = (int) (300 * scale + 0.5f);
        attrs.height = (int) (200 * scale + 0.5f);
        ifExtention.getWindow().setAttributes(attrs);
        Window dialogWindow = ifExtention.getWindow();
        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);
    }

    /**
     * @return void
     * @Description 扩建土地
     * @Auther 孙建旺
     * @Date 下午 2:39 2019/12/10
     * @Param []
     */
    private void ExtensionLand(final int position, final int money) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                FormBody formBody = new FormBody.Builder()
                        .add("landNumber", "land" + position)
                        .add("needMoney", money + "").build();
                Request request = new Request.Builder().post(formBody).url(getResources().getString(R.string.URL) + "/user/extensionLand").build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Message message = Message.obtain();
                        message.obj = "Fail";
                        UpdataLands.sendMessage(message);
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        OkHttpUtils.unauthorized(response.code());
                        String callBackMessage = response.body().string();
                        Message message = Message.obtain();
                        message.obj = callBackMessage;
                        UpdataLands.sendMessage(message);
                    }
                });
            }
        }.start();
    }

    /**
     * @return void
     * @Description 更新土地状态
     * @Auther 孙建旺
     * @Date 下午 2:54 2019/12/10
     * @Param []
     */
    private void UpdateLand(final int position) {
        UpdataLands = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String message = (String) msg.obj;
                if (message.equals("true")) {
//                    LoginActivity.user.setLandStauts(position,0);
                    int newMoney = UserUtil.getUser().getMoney() - ExtensionLandMoney;
                    UserUtil.getUser().setMoney(newMoney);
                    money.setText("金币:" + newMoney + "");
                    ifExtention.dismiss();
//                    showLand();
                    getCrop();
                } else if (message.equals("false")) {
                    CustomerToast.getInstance(MainActivity.this, "没有扩建成功哦！", Toast.LENGTH_SHORT).show();
                } else if (message.equals("notEnoughMoney")) {
                    CustomerToast.getInstance(MainActivity.this, "你的钱不够了哦！", Toast.LENGTH_SHORT).show();
                } else {
                    CustomerToast.getInstance(MainActivity.this, "没有找到你的土地哦！", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }


    /**
     * 种植作物
     *
     * @param gridView
     */
    public void planting(final GridView gridView) {
        bagPopUpWindow.getDataList();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                if (selectLand != 0) {
                    plant(selectLand, bagPopUpWindow.getDataList().get(i).getCrop().getId());
                    plantMessagesHandler = new Handler() {
                        @Override
                        public void handleMessage(@NonNull Message msg) {
                            super.handleMessage(msg);
                            String messages = (String) msg.obj;
                            Log.e("种植", messages);
                            if (messages.equals("Fail")) {
                                CustomerToast.getInstance(MainActivity.this, "网络异常！", Toast.LENGTH_SHORT).show();
                            } else if (messages.equals("true")) {
                                CustomerToast.getInstance(MainActivity.this, "操作成功！", Toast.LENGTH_SHORT).show();
                                getUserInfo();
                            } else
                                CustomerToast.getInstance(MainActivity.this, "操作失败！", Toast.LENGTH_SHORT).show();
                        }
                    };
                }
            }
        });
        bagPopUpWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                getCrop();
            }
        });
//        {
//            @Override
//            public void onCancel(DialogInterface dialogInterface) {
//
//            }
//        });
    }

    /**
     * 请求种植
     *
     * @param selectLand
     * @param id
     */
    private void plant(final int selectLand, final int id) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                Request request = new Request.Builder().url(getResources().getString(R.string.URL) + "/user/raiseCrop?cropId=" + id + "&landNumber=land" + selectLand).build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Message message = Message.obtain();
                        message.obj = "Fail";
                        plantMessagesHandler.sendMessage(message);
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        OkHttpUtils.unauthorized(response.code());
                        Message message = Message.obtain();
                        message.obj = response.body().string();
                        plantMessagesHandler.sendMessage(message);
                    }
                });
            }
        }.start();
    }

    /**
     * 浇水、施肥、收获操作
     */
    private void operating(final int operating) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                Request request = null;
                if (operating == 0) {
                    request = new Request.Builder().url(getResources().getString(R.string.URL) + "/user/waterCrop?landNumber=land" + selectedPlant).build();
                }else if (operating == -1) {
                    request = new Request.Builder().url(getResources().getString(R.string.URL) + "/user/fertilizerCrop?landNumber=land" + selectedPlant).build();
                } else {
                    request = new Request.Builder().url(getResources().getString(R.string.URL) + "/user/harvest?landNumber=land" + selectedPlant).build();
                }
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Message message = Message.obtain();
                        message.obj = "Fail";
                        waterMessagesHandler.sendMessage(message);
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        OkHttpUtils.unauthorized(response.code());
                        Message message = Message.obtain();
                        message.obj = response.body().string();
                        waterMessagesHandler.sendMessage(message);
                    }
                });
            }
        }.start();
    }


    /**
     * @return void
     * @Description 背包弹出框
     * @Auther 孙建旺
     * @Date 下午 2:01 2019/12/08
     * @Param [position]
     */
    private void showBagMessages() {
        bagPopUpWindow = new BagPopUpWindow(this);
        WindowManager wm = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics ds = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(ds);
        displayHeight = ds.heightPixels;
        displayWidth = ds.widthPixels;
        bagPopUpWindow.setWidth((int) (displayWidth * 0.40));
        bagPopUpWindow.setHeight((int) (displayHeight));
        if (!isNavigationBarShow()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                bagPopUpWindow.setIsClippedToScreen(true);
            }
        }
        bagPopUpWindow.showAtLocation(bag,Gravity.RIGHT,0,0);
        planting(bagPopUpWindow.getGridView());
    }

    /**
     * @param
     * @return void
     * @Author li
     * @Description 添加监听器
     * @Date 17:50 2020/4/24
     **/
    private void addListener() {
        learn.setOnClickListener(new MainListener());
        water.setOnClickListener(new MainListener());
        fertilizer.setOnClickListener(new MainListener());
        bag.setOnClickListener(new MainListener());
        shop.setOnClickListener(new MainListener());
        pet.setOnClickListener(new MainListener());
        setting.setOnClickListener(new MainListener());
        harvest.setOnClickListener(new MainListener());
        myFriends.setOnClickListener(new MainListener());
        notify.setOnClickListener(new MainListener());
        photo.setOnClickListener(new MainListener());
        dayTask.setOnClickListener(new MainListener());
    }

    /**
     * @param
     * @return void
     * @Author li
     * @Description 获取控件
     * @Date 17:55 2020/4/24
     **/
    private void getViews() {
        learn = findViewById(R.id.learn);
        water = findViewById(R.id.water);
        waterCount = findViewById(R.id.waterCount);
        fertilizer = findViewById(R.id.fertilizer);
        fertilizerCount = findViewById(R.id.fertilizerCount);
        bag = findViewById(R.id.bag);
        shop = findViewById(R.id.shop);
        pet = findViewById(R.id.pet);
        setting = findViewById(R.id.setting);
        LinearLayout layout3=findViewById(R.id.settingbox);
        layout3.setVisibility(View.GONE);
        photo = findViewById(R.id.photo);
        nickName = findViewById(R.id.nickName);
        level = findViewById(R.id.level);
        money = findViewById(R.id.money);
        account = findViewById(R.id.account);
        lands = findViewById(R.id.lands);
        land_background = findViewById(R.id.land_background);
        experience = findViewById(R.id.experience);
        experienceValue = findViewById(R.id.experienceValue);
        xzw = findViewById(R.id.xzw);
        xzf = findViewById(R.id.xzf);
        xzs = findViewById(R.id.xzs);
        harvest = findViewById(R.id.harvest);
        Glide.with(this).asGif().load(R.drawable.xuanzhong4).into(xzw);
        Glide.with(this).asGif().load(R.drawable.xuanzhong4).into(xzf);
        Glide.with(this).asGif().load(R.drawable.xuanzhong4).into(xzs);
        myFriends = findViewById(R.id.friends);
        notify = findViewById(R.id.notify_img);
        notify_list_view = findViewById(R.id.notify_list_view);
        dayTask = findViewById(R.id.task);
        notify_red =findViewById(R.id.notify_red);
        daytask_red =findViewById(R.id.daytask_red);
        dog=findViewById(R.id.dog);
        ViewGroup.MarginLayoutParams params =(ViewGroup.MarginLayoutParams)lands.getLayoutParams();
        float density = getResources().getDisplayMetrics().density;
        float displayHeight = getResources().getDisplayMetrics().heightPixels/density;
        float displayWidth = getResources().getDisplayMetrics().widthPixels/density;
        System.out.println("width "+displayWidth+"height "+displayHeight);
        if (displayWidth>640){
            params.topMargin= (int) (displayHeight/2.4f);
            params.leftMargin=(int)displayWidth/5;
            lands.setLayoutParams(params);
            land_background.setLayoutParams(params);
        }else {
            int top = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    displayWidth/8, getResources().getDisplayMetrics());
            params.topMargin= top;
            lands.setLayoutParams(params);
            land_background.setLayoutParams(params);
        }
    }

    /**
     * @Author li
     * @param eventBean
     * @return void
     * @Description 消息红点的展示和在线时每种消息是否有新的消息的设置
     * @Date 15:11 2020/4/27
     **/
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setRedPoint(EventBean eventBean){
        if ("notification".equals(eventBean.getMessage())){
            switch (eventBean.getNotifyType()){
                case "system":
                    notifyStatus.set(0,true);
                    break;
                case "receive":
                    notifyStatus.set(1,true);
                    break;
                case "send":
                    notifyStatus.set(2,true);
                    break;
                case "message":
                    notifyStatus.set(3,true);
                    break;
            }
            notify_red.setVisibility(View.VISIBLE);
        }
        if ("task".equals(eventBean.getMessage())){
            daytask_red.setVisibility(View.VISIBLE);
        }
    }

    /**
     * @Author li
     * @param
     * @return void
     * @Description 获取是否有新消息
     * @Date 21:16 2020/5/22
     **/
    public void haveNewNotifications(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                Request request = new Request.Builder()
                        .url(getResources().getString(R.string.URL)+"/notification/isHavingNewNotification").build();
                Call call =okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.e("新通知信息", "请求失败");
                        Message message = Message.obtain();
                        message.obj = "Fail";
                        new_notification.sendMessage(message);
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        OkHttpUtils.unauthorized(response.code());
                        String notify_message = response.body().string();
                        Message message = Message.obtain();
                        message.obj = notify_message;
                        new_notification.sendMessage(message);
                    }
                });
            }
        }.start();
        new_notification= new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String message = (String)msg.obj;
                Log.e("新通知信息",message);
                if (!message.equals("Fail")){
                    Type type = new TypeToken<List<Boolean>>(){}.getType();
                    notifyStatus= gson.fromJson(message,type);
                    if (notifyStatus.subList(0,4).contains(true)){
                        notify_red.setVisibility(View.VISIBLE);
                    }
                    if(notifyStatus.get(4)){
                        daytask_red.setVisibility(View.VISIBLE);
                    }
                }else {
                    CustomerToast.getInstance(getApplication(), "网络异常！", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void pkEnd(EventBean status) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.pk_end, null);
        TextView text = layout.findViewById(R.id.text);
        final ImageView pet = layout.findViewById(R.id.pet);
        final UserPetHouse userPetHouse = UserUtil.getUser().getPetHouses().get(0);
        final String url = userPetHouse.getGrowPeriod() == 0 ? userPetHouse.getPet().getImg1() : userPetHouse.getGrowPeriod()==1? userPetHouse.getPet().getImg2() : userPetHouse.getPet().getImg3();
        Glide.with(this).load(url).into(pet);
        switch (status.getResult()){
            case "0":
                text.setText("要继续加油哦！");
                break;
            case "1":
                text.setText("宠物智力值提升啦！");
                break;
            case "2":
                new Handler(){
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        super.handleMessage(msg);
                        UserPetHouse userPetHouse = UserUtil.getUser().getPetHouses().get(0);
                        userPetHouse.setGrowPeriod(userPetHouse.getGrowPeriod()+1);
                        String url = userPetHouse.getGrowPeriod() == 0 ? userPetHouse.getPet().getImg1() : userPetHouse.getGrowPeriod()==1? userPetHouse.getPet().getImg2() : userPetHouse.getPet().getImg3();
                        Glide.with(MainActivity.this).load(url).into(pet);
                    }
                }.postDelayed(null,1000);
                text.setText("宠物进入下一成长阶段喽！");
                break;
        }
        alertBuilder.setView(layout);
        final AlertDialog upDiaLog = alertBuilder.create();
        NavigationBarUtil.focusNotAle(upDiaLog.getWindow());
        upDiaLog.show();
        NavigationBarUtil.hideNavigationBar(upDiaLog.getWindow());
        NavigationBarUtil.clearFocusNotAle(upDiaLog.getWindow());
        WindowManager.LayoutParams attrs = upDiaLog.getWindow().getAttributes();
        if (upDiaLog.getWindow() != null) {
            //bagDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            upDiaLog.getWindow().setDimAmount(0f);//去除遮罩
        }
        attrs.gravity = Gravity.CENTER;
        final float scale = this.getResources().getDisplayMetrics().density;
        attrs.width = (int) (300 * scale + 0.5f);
        attrs.height = (int) (250 * scale + 0.5f);
        upDiaLog.getWindow().setAttributes(attrs);
        Window dialogWindow = upDiaLog.getWindow();
        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);
    }

    /**
     * @Author li
     * @return null
     * @Description 监听器类
     * @Date 17:56 2020/4/24
     **/
    class MainListener implements View.OnClickListener {

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.learn:
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, SubjectListActivity.class);
                    startActivity(intent);
                    break;
                case R.id.water:
                    selected = 0;
                    xzw.setVisibility(View.VISIBLE);
                    xzf.setVisibility(View.GONE);
                    xzs.setVisibility(View.GONE);
                    break;
                case R.id.fertilizer:
                    selected = -1;
                    xzw.setVisibility(View.GONE);
                    xzf.setVisibility(View.VISIBLE);
                    xzs.setVisibility(View.GONE);
                    break;
                case R.id.harvest:
                    selected = -2;
                    xzw.setVisibility(View.GONE);
                    xzf.setVisibility(View.GONE);
                    xzs.setVisibility(View.VISIBLE);
                    break;
                case R.id.bag:
                    if (System.currentTimeMillis() - lastClickTime < FAST_CLICK_DELAY_TIME) {
                        return;
                    }
                    lastClickTime = System.currentTimeMillis();
                    showBagMessages();
                    break;
                case R.id.shop:
                    intent = new Intent();
                    intent.setClass(MainActivity.this, ShopActivity.class);
                    startActivity(intent);
                    break;
                case R.id.pet:
                    petPopUpWindow = new PetPopUpWindow(getApplicationContext());
                    petPopUpWindow.showAtLocation(pet, Gravity.CENTER, 0, 0);
                    petPopUpWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            getUserInfo();
                        }
                    });
                    break;
//                case R.id.setting:
//                    intent = new Intent();
//                    intent.setClass(MainActivity.this, SettingActivity.class);
//                    startActivity(intent);
//                    break;
                case R.id.friends:
                    if (System.currentTimeMillis() - lastClickTime < FAST_CLICK_DELAY_TIME) {
                        return;
                    }
                    lastClickTime = System.currentTimeMillis();
                    showFriends();
                    myFriends.setVisibility(View.GONE);
                    break;
                case R.id.notify_img:
                    intent = new Intent();
                    intent.setClass(MainActivity.this, NotifyActivity.class);
                    startActivity(intent);
//                    overridePendingTransition(R.anim.notify_pop_in, 0);
                    notify_red.setVisibility(View.GONE);
                    break;
                case R.id.photo:
                    intent = new Intent();
                    intent.setClass(MainActivity.this, SettingMessageActivity.class);
                    startActivity(intent);
                    break;
                case R.id.task:
                    showDayTaskWindow();
                    daytask_red.setVisibility(View.GONE);
                    break;
            }
        }
    }

    //退出时的时间
    private long mExitTime;

    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            CustomerToast.getInstance(MainActivity.this, "再按一次退出游戏", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

    public void guide(){
        SharedPreferences guide = getSharedPreferences("guide", MODE_PRIVATE);
        boolean main = guide.getBoolean("main", true);
        if (!main){
            return;
        }
        final GuideHelper guideHelper = new GuideHelper(MainActivity.this);
        //第一页
        TextView hello = new TextView(this);
        hello.setText("  欢迎来到知识农场！\n接下来将进入新手引导！\n    点击屏幕继续");
        hello.setTextSize(30);
        TipData tipData = new TipData(hello, Gravity.CENTER);
        TextView close = new TextView(this);
        close.setText("跳过");
        close.setTextSize(26);
        TipData tipData1 = new TipData(close, Gravity.TOP | Gravity.END);
        tipData1.setLocation(-50,50);
        tipData1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guideHelper.dismiss();
            }
        });
        guideHelper.addPage(tipData,tipData1);
        //第二页
        TextView shop = new TextView(this);
        shop.setText("\n在商店可以购买到种子！");
        shop.setTextSize(26);
        TipData tipData2 = new TipData(shop,Gravity.CENTER);
        TipData tipData3 = new TipData(R.drawable.top,Gravity.BOTTOM,this.shop);
        tipData3.setLocation(DisplayUtils.dipToPix(this,40),0);
//        ImageView block = new ImageView(this);
//        Glide.with(this).asGif().load(R.drawable.xuanzhong4).override(DisplayUtils.dipToPix(this,70),DisplayUtils.dipToPix(this,70)).into(block);
//        block.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                intent = new Intent();
//                intent.setClass(MainActivity.this, ShopActivity.class);
//                startActivity(intent);
//                new Timer().schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        guideHelper.nextPage();
//                    }
//                },1000);
//            }
//        });
//        TipData tipData4 = new TipData(block,Gravity.CENTER,this.shop);
        guideHelper.addPage(tipData2,tipData3);
        //第三页
        TextView textView1 = new TextView(this);
        textView1.setTextSize(26);
        textView1.setText("查看购买到的种子");
        TipData tipData5 = new TipData(textView1,Gravity.END|Gravity.TOP,bag);
        tipData5.setLocation(0,-DisplayUtils.dipToPix(this,40));
        TipData tipData6 = new TipData(R.drawable.down,Gravity.END|Gravity.TOP,bag);
        tipData6.setLocation(-DisplayUtils.dipToPix(this,50),0);
        guideHelper.addPage(tipData5,tipData6);

        //第四页
        TextView textView2 = new TextView(this);
        textView2.setTextSize(26);
        textView2.setText("点击空地，然后在背包中选择要种植的种子\n            就可以完成种植啦！");
        TipData tipData7 = new TipData(textView2, Gravity.CENTER);
        guideHelper.addPage(tipData7);

        //第五页
        TextView textView3 = new TextView(this);
        textView3.setTextSize(26);
        textView3.setText("选中水壶可以进行连续浇水！");
        TipData tipData8 = new TipData(textView3,Gravity.END|Gravity.TOP,water);
        tipData8.setLocation(0,-DisplayUtils.dipToPix(this,50));
        TipData tipData9 = new TipData(R.drawable.down,Gravity.END|Gravity.TOP,water);
        tipData9.setLocation(-DisplayUtils.dipToPix(this,50),0);
        guideHelper.addPage(tipData8,tipData9);

        //第六页
        TextView textView4 = new TextView(this);
        textView4.setTextSize(26);
        textView4.setText("选中肥料可以进行连续施肥！");
        TipData tipData10 = new TipData(textView4,Gravity.END|Gravity.TOP,fertilizer);
        tipData10.setLocation(0,-DisplayUtils.dipToPix(this,50));
        TipData tipData11 = new TipData(R.drawable.down,Gravity.END|Gravity.TOP,fertilizer);
        tipData11.setLocation(-DisplayUtils.dipToPix(this,50),0);
        guideHelper.addPage(tipData10,tipData11);

        //第七页
        TextView textView5 = new TextView(this);
        textView5.setTextSize(26);
        textView5.setText("选中手可以进行连续收获！");
        TipData tipData12 = new TipData(textView5,Gravity.END|Gravity.TOP,harvest);
        tipData12.setLocation(0,-DisplayUtils.dipToPix(this,50));
        TipData tipData13 = new TipData(R.drawable.down,Gravity.END|Gravity.TOP,harvest);
        tipData13.setLocation(-DisplayUtils.dipToPix(this,50),0);
        guideHelper.addPage(tipData12,tipData13);

        //第八页
        TextView textView6 = new TextView(this);
        textView6.setTextSize(26);
        textView6.setText("查看每日任务！");
        TipData tipData14 = new TipData(textView6,Gravity.END|Gravity.TOP,dayTask);
        tipData14.setLocation(0,-DisplayUtils.dipToPix(this,50));
        TipData tipData15 = new TipData(R.drawable.down,Gravity.END|Gravity.TOP,dayTask);
        tipData15.setLocation(-DisplayUtils.dipToPix(this,50),0);
        guideHelper.addPage(tipData14,tipData15);

        //第九页
        TextView textView7 = new TextView(this);
        textView7.setTextSize(26);
        textView7.setText("进入学习中心，通过答题获得水和肥料！");
        TipData tipData16 = new TipData(textView7,Gravity.END|Gravity.TOP,learn);
        tipData16.setLocation(0,-DisplayUtils.dipToPix(this,50));
        TipData tipData17 = new TipData(R.drawable.down,Gravity.END|Gravity.TOP,learn);
        tipData17.setLocation(-DisplayUtils.dipToPix(this,50),0);
        guideHelper.addPage(tipData16,tipData17);

        //第十页
        TextView textView8 = new TextView(this);
        textView8.setTextSize(26);
        textView8.setText("查看和修改个人信息！");
        TipData tipData18 = new TipData(textView8,Gravity.END|Gravity.BOTTOM,photo);
        tipData18.setLocation(0,DisplayUtils.dipToPix(this,50));
        TipData tipData19 = new TipData(R.drawable.left,Gravity.END|Gravity.BOTTOM,photo);
        tipData19.setLocation(-DisplayUtils.dipToPix(this,50),0);
        guideHelper.addPage(tipData18,tipData19);

        //第十一页
        TextView textView9 = new TextView(this);
        textView9.setTextSize(26);
        textView9.setText("查看通知和好友申请！");
        TipData tipData20 = new TipData(textView9,Gravity.START|Gravity.BOTTOM,notify);
        tipData20.setLocation(0,DisplayUtils.dipToPix(this,50));
        TipData tipData21 = new TipData(R.drawable.top,Gravity.START|Gravity.BOTTOM,notify);
        tipData21.setLocation(DisplayUtils.dipToPix(this,50),0);
        guideHelper.addPage(tipData20,tipData21);

        //第十二页
        TextView textView10 = new TextView(this);
        textView10.setTextSize(26);
        textView10.setText("查看和喂养宠物！");
        TipData tipData22 = new TipData(textView10,Gravity.START|Gravity.BOTTOM,pet);
        tipData22.setLocation(0,DisplayUtils.dipToPix(this,50));
        TipData tipData23 = new TipData(R.drawable.top,Gravity.START|Gravity.BOTTOM,pet);
        tipData23.setLocation(DisplayUtils.dipToPix(this,50),0);
        guideHelper.addPage(tipData22,tipData23);

        //第十三页
        TextView textView11 = new TextView(this);
        textView11.setTextSize(26);
        textView11.setText("新手引导结束啦！");
        TipData tipData24 = new TipData(textView11,Gravity.CENTER);
        guideHelper.addPage(tipData24);

        guideHelper.show(false);
        guide.edit().putBoolean("main",false).apply();
    }

}
