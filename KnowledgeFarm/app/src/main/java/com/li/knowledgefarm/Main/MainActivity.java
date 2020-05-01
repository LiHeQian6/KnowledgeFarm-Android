package com.li.knowledgefarm.Main;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
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
import com.li.knowledgefarm.Settings.SettingActivity;
import com.li.knowledgefarm.Settings.SettingMessageActivity;
import com.li.knowledgefarm.Shop.ShopActivity;
import com.li.knowledgefarm.Study.SubjectListActivity;
import com.li.knowledgefarm.Util.FullScreen;
import com.li.knowledgefarm.daytask.DayTaskPopUpWindow;
import com.li.knowledgefarm.entity.DoTaskBean;
import com.li.knowledgefarm.entity.EventBean;
import com.li.knowledgefarm.entity.User;
import com.li.knowledgefarm.entity.UserCropItem;
import com.li.knowledgefarm.entity.UserPetHouse;
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
    private float LAND_WIDTH_2 = 224;
    private float LAND_HEIGHT_2 = 114;
    private Handler friendMessagesHandler;
    private ImageView dayTask;
    private DayTaskPopUpWindow dayTaskPopUpWindow;
    private ImageView notify_red;
    private ImageView daytask_red;
    private static List<Boolean> notifyStatus=new ArrayList<>(4);
    private Handler new_notification;
    private FriendsPopUpWindow friendsPopUpWindow;
    private PetPopUpWindow petPopUpWindow;
    private ImageView dog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FullScreen.NavigationBarStatusBar(MainActivity.this,true);
        setContentView(R.layout.activity_main);
        okHttpClient = new OkHttpClient();
        gson = new Gson();
        getViews();
        addListener();
        getCrop();
        JPushInterface.setAlias(this,1, LoginActivity.user.getAccount());
        haveNewNotifications();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();
        showUserInfo();
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
                        .url(getResources().getString(R.string.URL) + "/user/findUserInfoByUserId?userId=" + LoginActivity.user.getId())
                        .build();
                Call call = new OkHttpClient().newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("用户信息", "请求失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        if (result.equals("{}")) {
                            Log.e("用户信息", "信息异常");
                        } else {
                            Log.e("用户信息", result);
                            Message message = new Message();
                            message.obj = LoginActivity.parsr(URLDecoder.decode(result), User.class);
                            LoginActivity.user = (User) message.obj;
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
                    Toast.makeText(MainActivity.this, "网络异常！", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    /**
     * @Description 展示用户信息与设置
     * @Author 孙建旺
     * @Date 下午3:34 2020/04/20
     * @Param []
     * @return void
     */
//    private void showUserMessage(){
//        userMessagePopUp = new UserMessagePopUp(this,MainActivity.this);
//        WindowManager.LayoutParams lp = getWindow().getAttributes();
//        lp.alpha = (float) 0.5;
//        this.getWindow().setAttributes(lp);
//        userMessagePopUp.showAtLocation(photo,Gravity.CENTER,0,0);
//    }

    /**
     * 展示用户信息
     */
    private void showUserInfo() {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.huancun2)
                .error(R.drawable.meigui)
                .fallback(R.drawable.meigui)
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(this).load(LoginActivity.user.getPhoto()).apply(requestOptions).into(photo);
        nickName.setText(LoginActivity.user.getNickName());
        account.setText("账号:" + LoginActivity.user.getAccount());
        level.setText("Lv:" + LoginActivity.user.getLevel());
        money.setText("金币:" + LoginActivity.user.getMoney());
        waterCount.setText(LoginActivity.user.getWater() + "");
        fertilizerCount.setText(LoginActivity.user.getFertilizer() + "");
        int[] levelExperience = getResources().getIntArray(R.array.levelExperience);
        int l = LoginActivity.user.getLevel();
        experience.setMax(levelExperience[l] - levelExperience[l - 1]);
        experience.setProgress((int) LoginActivity.user.getExperience() - levelExperience[l - 1]);
        experienceValue.setText("" + LoginActivity.user.getExperience() + "/" + levelExperience[l]);
        List<UserPetHouse> petHouses = LoginActivity.user.getPetHouses();
        if (petHouses.size()!=0) {
            Glide.with(this).asGif().load(petHouses.get(0).getPet().getImg1()).error(R.drawable.dog).diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(dog);
        }else
            Glide.with(this).asGif().load(R.drawable.mydog).into(dog);
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
//        right.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                upDiaLog.cancel();
//            }
//        });

        upDiaLog.show();
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
        friendsPopUpWindow.showAtLocation(myFriends,Gravity.RIGHT,0,0);
        friendsPopUpWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                myFriends.setVisibility(View.VISIBLE);
            }
        });
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
        setDialogSize(view);
        dialog.setContentView(view);
        dialog.show();
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
        attrs.height = (int) (300 * scale + 0.5f);
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
                    request = new Request.Builder().url(getResources().getString(R.string.URL) + "/notification/addUserFriendNotification?userId=" + LoginActivity.user.getId() + "&account=" + num).build();
                else if (option == 1) {
                    request = new Request.Builder().url(getResources().getString(R.string.URL) + "/userfriend/deleteUserFriend?userId=" + LoginActivity.user.getId() + "&account=" + num).build();
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
                        Toast.makeText(MainActivity.this, option == 0 ? "申请失败！" : "删除失败！", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(MainActivity.this, option == 0 ? "申请成功！" : "删除成功！", Toast.LENGTH_SHORT).show();
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
                    Toast toast = Toast.makeText(MainActivity.this, "网络异常！", Toast.LENGTH_SHORT);
                    toast.show();
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
                Request request = new Request.Builder().url(getResources().getString(R.string.URL) + "/usercrop/initUserCrop?userId=" + LoginActivity.user.getId()).build();
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
                    Toast toast = Toast.makeText(MainActivity.this, "网络异常！", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        };
    }

    /**
     * 初始化土地
     */
    private void showLand() {
        lands.removeAllViews();
        int flag = 0;
        float x = 0;
        float y = 0;
        for (int i = 0; i < 18; i++) {
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            final View landGroup = inflater.inflate(R.layout.land_group, null);
            landGroup.setLayoutParams(new FrameLayout.LayoutParams(480, 240));
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
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(240, 240);
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
                                            Toast.makeText(MainActivity.this, "植物已经成熟哦！", Toast.LENGTH_SHORT).show();
                                            if (finalCrop.getStatus() == 0) {
                                                land.setImageResource(R.drawable.land_gan);
                                            } else
                                                land.setImageResource(R.drawable.land);
                                        } else {
                                            Glide.with(MainActivity.this).asGif().load(R.drawable.jiaoshui).into(animation);
                                            operating(0);//浇水
                                        }
                                    } else if (selected == -1) {
                                        selectedPlant = finalI;
                                        if (status == 1) {
                                            Toast.makeText(MainActivity.this, "植物已经成熟哦！", Toast.LENGTH_SHORT).show();
                                            if (finalCrop.getStatus() == 0) {
                                                land.setImageResource(R.drawable.land_gan);
                                            } else
                                                land.setImageResource(R.drawable.land);
                                        } else {
                                            Glide.with(MainActivity.this).asGif().load(R.drawable.shifei).into(animation);
                                            operating(-1);//施肥
                                        }
                                    } else {
                                        selectedPlant = finalI;
                                        if (status == 1) {
                                            Glide.with(MainActivity.this).asGif().load(R.drawable.shouhuog).into(animation);
                                            operating(-2);//成熟
                                        } else {
                                            Toast.makeText(MainActivity.this, "植物还没有成熟哦！", Toast.LENGTH_SHORT).show();
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
                                                    Toast.makeText(MainActivity.this, "操作失败！", Toast.LENGTH_SHORT).show();
                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            animation.setVisibility(View.GONE);
                                                        }
                                                    }, 1000);
                                                } else {
                                                    //Toast.makeText(MainActivity.this, "操作成功！", Toast.LENGTH_SHORT).show();
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
                                                Toast.makeText(MainActivity.this, "网络异常！", Toast.LENGTH_SHORT).show();
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
        setDialogSize(layout);
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
        ifExtention.show();
        WindowManager.LayoutParams attrs = ifExtention.getWindow().getAttributes();
        if (ifExtention.getWindow() != null) {
            //bagDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            ifExtention.getWindow().setDimAmount(0f);//去除遮罩
        }
        attrs.gravity = Gravity.CENTER;
        final float scale = this.getResources().getDisplayMetrics().density;
        attrs.width = (int) (300 * scale + 0.5f);
        attrs.height = (int) (300 * scale + 0.5f);
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
                        .add("userId", LoginActivity.user.getId() + "")
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
                    int newMoney = LoginActivity.user.getMoney() - ExtensionLandMoney;
                    LoginActivity.user.setMoney(newMoney);
                    money.setText("金币:" + newMoney + "");
                    ifExtention.dismiss();
//                    showLand();
                    getCrop();
                } else if (message.equals("false")) {
                    Toast toast = Toast.makeText(MainActivity.this, "没有扩建成功哦！", Toast.LENGTH_SHORT);
                    toast.show();
                } else if (message.equals("notEnoughMoney")) {
                    Toast toast = Toast.makeText(MainActivity.this, "你的钱不够了哦！", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(MainActivity.this, "没有找到你的土地哦！", Toast.LENGTH_SHORT);
                    toast.show();
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
                                Toast.makeText(MainActivity.this, "网络异常！", Toast.LENGTH_SHORT).show();
                            } else if (messages.equals("true")) {
                                Toast.makeText(MainActivity.this, "操作成功！", Toast.LENGTH_SHORT).show();
                                getUserInfo();
                            } else
                                Toast.makeText(MainActivity.this, "操作失败！", Toast.LENGTH_SHORT).show();
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
                Request request = new Request.Builder().url(getResources().getString(R.string.URL) + "/user/raiseCrop?userId=" + LoginActivity.user.getId() + "&cropId=" + id + "&landNumber=land" + selectLand).build();
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
                if (operating == 0)
                    request = new Request.Builder().url(getResources().getString(R.string.URL) + "/user/waterCrop?userId=" + LoginActivity.user.getId() + "&landNumber=land" + selectedPlant).build();
                else if (operating == -1) {
                    request = new Request.Builder().url(getResources().getString(R.string.URL) + "/user/fertilizerCrop?userId=" + LoginActivity.user.getId() + "&landNumber=land" + selectedPlant).build();
                } else {
                    request = new Request.Builder().url(getResources().getString(R.string.URL) + "/user/harvest?userId=" + LoginActivity.user.getId() + "&landNumber=land" + selectedPlant).build();
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
        bagPopUpWindow.setHeight((int) (displayHeight * 0.95));
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
        photo = findViewById(R.id.photo);
        nickName = findViewById(R.id.nickName);
        level = findViewById(R.id.level);
        money = findViewById(R.id.money);
        account = findViewById(R.id.account);
        lands = findViewById(R.id.lands);
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
        if (eventBean.getMessage().equals("notification")){
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
        if (eventBean.getMessage().equals("task")){
            daytask_red.setVisibility(View.VISIBLE);
        }
    }

    public void haveNewNotifications(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                Request request = new Request.Builder()
                        .url(getResources().getString(R.string.URL)+"/notification/isHavingNewNotification?userId="+LoginActivity.user.getId()).build();
                Call call = new OkHttpClient().newCall(request);
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
                    if (notifyStatus.contains(true)){
                        notify_red.setVisibility(View.VISIBLE);
                    }
                }else {
                    Toast.makeText(getApplication(), "网络异常！", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    /**
     * @Description 展示用户信息和设置弹窗
     * @Author 孙建旺
     * @Date 下午9:21 2020/04/28
     * @Param []
     * @return void
     */
    private void ShowSettingAndMessage(){
//        SettingMessageActivity messagePopUp = new SettingMessageActivity(this,MainActivity.this,LoginActivity.user);
//        WindowManager wm = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
//        DisplayMetrics ds = new DisplayMetrics();
//        wm.getDefaultDisplay().getMetrics(ds);
//        displayHeight = ds.heightPixels;
//        displayWidth = ds.widthPixels;
//        messagePopUp.setWidth((int) (displayWidth * 0.70));
//        messagePopUp.setHeight((int) (displayHeight * 0.8));
//        messagePopUp.showAtLocation(photo,Gravity.CENTER,0,0);
//        planting(bagPopUpWindow.getGridView());
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
                    petPopUpWindow.showAtLocation(dayTask, Gravity.CENTER, 0, 0);
                    break;
                case R.id.setting:
                    intent = new Intent();
                    intent.setClass(MainActivity.this, SettingActivity.class);
                    startActivity(intent);
                    break;
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

    /**
     * @return void
     * @Description 设置弹窗控件大小
     * @Auther 孙建旺
     * @Date 下午 4:13 2019/12/18
     * @Param [view]
     */
    private void setDialogSize(View view) {
//        获取屏幕显示区域尺寸
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics ds = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(ds);
        displayHeight = ds.heightPixels;
        displayWidth = ds.widthPixels;

        ImageView cancel = view.findViewById(R.id.cancel_return);
        ImageView sure = view.findViewById(R.id.sure_return);
        TextView warning = view.findViewById(R.id.waringText);
        LinearLayout panduan = view.findViewById(R.id.panduan);

        LinearLayout.LayoutParams params_cancel = new LinearLayout.LayoutParams((int) (displayWidth * 0.065), (int) (displayWidth * 0.065));
        params_cancel.setMargins(0, 0, (int) (displayWidth * 0.08), 0);
        cancel.setLayoutParams(params_cancel);

        LinearLayout.LayoutParams params_sure = new LinearLayout.LayoutParams((int) (displayWidth * 0.065), (int) (displayWidth * 0.065));
        sure.setLayoutParams(params_sure);

        warning.setTextSize((int) (displayWidth * 0.012));

        LinearLayout.LayoutParams params_layout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params_layout.setMargins(0, (int) (displayHeight * 0.12), 0, 0);
        panduan.setLayoutParams(params_layout);
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
            Toast.makeText(MainActivity.this, "再按一次退出游戏", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

}
