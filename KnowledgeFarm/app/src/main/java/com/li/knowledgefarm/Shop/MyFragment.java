package com.li.knowledgefarm.Shop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.li.knowledgefarm.Login.LoginActivity;
import com.li.knowledgefarm.R;
import com.li.knowledgefarm.Util.FullScreen;
import com.li.knowledgefarm.Util.GuideHelper;
import com.li.knowledgefarm.Util.GuideHelper.TipData;
import com.li.knowledgefarm.Util.OkHttpUtils;
import com.li.knowledgefarm.Util.UserUtil;
import com.li.knowledgefarm.entity.EventBean;
import com.li.knowledgefarm.entity.Pet;
import com.li.knowledgefarm.entity.PetUtil;
import com.li.knowledgefarm.entity.PetVO;
import com.li.knowledgefarm.entity.ShopItemBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyFragment extends Fragment {

    private GridView gridView;
    private List<ShopItemBean> shopList;
    private List<PetVO> pet_list;
    private List<PetUtil> petUtils;
    public static final String SHOP = "POSITION";
    private ShopItemAdapter adapter; //植物展示Adapter
    private PetItemAdapter petItemAdapter; //宠物展示Adapter
    private PetUtilsAdapter petUtilsAdapter; //道具展示Adapter
    public static Boolean mIsScroll = false;
    private long lastClickTime = 0L;
    // 两次点击间隔不能少于1000ms
    private static final int FAST_CLICK_DELAY_TIME = 1000;
    private AlertDialog alertDialog = null;
    private ImageView imgBtnJian;
    private ImageView imgBtnPlus;
    private EditText shopNumber;
    private Toast toast;
    private OkHttpClient okHttpClient;
    private Handler doAfterAdd;
    private PetItemPopUpWindow petItemPopUpWindow;
    private UtilItemPopUp utilItemPopUp;
    private WindowManager wm;
    private DisplayMetrics ds;
    private int data;
    private Button button;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getViews();
        okHttpClient = OkHttpUtils.getInstance(getContext());
        View view = inflater.inflate(R.layout.plant_gridview_layout,container,false);
        gridView = view.findViewById(R.id.plant_gird_view);
        Bundle bundle = getArguments();
        data = bundle.getInt(SHOP);
        switch (data){
            case 1:
                shopList = (List<ShopItemBean>) bundle.getSerializable("plant");
                adapter = new ShopItemAdapter(getContext(),shopList,R.layout.shopitem_girdview);
                gridView.setAdapter(adapter);
                break;
            case 2:
                pet_list = (List<PetVO>)bundle.getSerializable("pet");
                petItemAdapter = new PetItemAdapter(getContext(),R.layout.shopitem_girdview,pet_list);
                gridView.setAdapter(petItemAdapter);
                break;
            case 3:
                petUtils = (List<PetUtil>)bundle.getSerializable("utils");
                petUtilsAdapter = new PetUtilsAdapter(petUtils,getContext(),R.layout.shopitem_girdview);
                gridView.setAdapter(petUtilsAdapter);
                break;
        }
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (System.currentTimeMillis() - lastClickTime < FAST_CLICK_DELAY_TIME){
                    return;
                }
                lastClickTime = System.currentTimeMillis();
                switch (data){
                    case 1:
                        showSingleAlertDialog(position);
                        break;
                    case 2:
                        ShowSinglePetPopUp(position);
                        break;
                    case 3:
                        ShowSingleUtilPopUp(position);
                        break;
                }
            }
        });
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
                    mIsScroll = false;
                }else{
                    mIsScroll = true;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * @Description 更新商店展示
     * @Author 孙建旺
     * @Date 上午10:19 2020/05/10
     * @Param [eventBean]
     * @return void
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void UpdatePet(EventBean eventBean){
        int pos = pet_list.indexOf(eventBean.getPetVO());
        this.pet_list.get(pos).setOwn(1);
        petItemAdapter.notifyDataSetChanged();
    }

    /**
     * @Description 点击道具弹出框
     * @Author 孙建旺
     * @Date 上午10:37 2020/05/09
     * @Param [position]
     * @return void
     */
    private void ShowSingleUtilPopUp(int position) {
        utilItemPopUp = new UtilItemPopUp(getContext(),petUtils.get(position));
        utilItemPopUp.setHeight((int)(ds.heightPixels));
        utilItemPopUp.setWidth((int)(ds.widthPixels*0.6));
        utilItemPopUp.showAtLocation(gridView, Gravity.CENTER,0,0);
    }

    /**
     * @Description 初始化
     * @Author 孙建旺
     * @Date 下午9:08 2020/05/06
     * @Param []
     * @return void
     */
    private void getViews() {
        wm = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        ds = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(ds);
    }

    /**
     * @Description  点击植物弹出框
     * @Auther 孙建旺
     * @Date 下午 5:04 2019/12/07
     * @Param [position]
     * @return void
     */
    @SuppressLint("ResourceType")
    private void showSingleAlertDialog(final int position){
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext(),R.style.dialog_soft_input);
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.shop_dialog, null);
        FullScreen.hideBottomUIMenu(layout);
        button = layout.findViewById(R.id.buy);
        Button cancel = layout.findViewById(R.id.btnCancel);
        TextView thisName = layout.findViewById(R.id.thisName);
        TextView thisPrice = layout.findViewById(R.id.thisPrice);
        TextView thisTime = layout.findViewById(R.id.thisTime);
        ImageView thisFlower = layout.findViewById(R.id.thisFlower);
        imgBtnJian = layout.findViewById(R.id.imgBtnJian);
        imgBtnPlus = layout.findViewById(R.id.imgBtnPlus);
        shopNumber = layout.findViewById(R.id.shopNum);
        setShopNumber();
        shopNumber.setText("1");
        alertBuilder.setView(layout);
        thisName.setText("名称："+shopList.get(position).getName());
        thisPrice.setText("单价："+shopList.get(position).getPrice()+"");
        thisTime.setText("成熟时间："+shopList.get(position).getMatureTime()+"");
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.huancun2)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(alertBuilder.getContext()).load(shopList.get(position).getImg4()).apply(requestOptions).into(thisFlower);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shopNumber.getText().toString().equals("")){
                    Toast.makeText(getContext(),"购买数量不能为空哦！",Toast.LENGTH_SHORT).show();
                }else{
                    buyFlowers(
                            UserUtil.getUser().getId(),
                            shopList.get(position).getId(),
                            Integer.parseInt(shopNumber.getText().toString().trim())
                    );
                }

            }
        });
        doAfterAdd = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String addCallBack = (String)msg.obj;
                if(addCallBack.equals("true")){
                    int newMoney = UserUtil.getUser().getMoney() - Integer.parseInt(shopNumber.getText().toString().trim())*shopList.get(position).getPrice();
                    UserUtil.getUser().setMoney(newMoney);
                    if(toast != null) {
                        toast.cancel();
                        toast = Toast.makeText(alertBuilder.getContext(), "购买成功！", Toast.LENGTH_SHORT);
                        toast.show();
                    }else{
                        toast = Toast.makeText(alertBuilder.getContext(), "购买成功！", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    alertDialog.dismiss();
                }else if(addCallBack.equals("notEnoughMoney")){
                    Toast toast = Toast.makeText(alertBuilder.getContext(),"你的钱不够了哦！",Toast.LENGTH_SHORT);
                    toast.show();
                }else{
                    Toast toast = Toast.makeText(alertBuilder.getContext(),"添加失败！",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        };
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog = alertBuilder.create();
        alertDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        alertDialog.show();
        alertDialog.getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                alertDialog.getWindow().setFlags(~WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
            }
        });
        WindowManager.LayoutParams attrs = alertDialog.getWindow().getAttributes();
        attrs.gravity = Gravity.CENTER;
        final float scale = this.getResources().getDisplayMetrics().density;
        attrs.width = (int)(300*scale+0.5f);
        attrs.height =(int)(300*scale+0.5f);
        Window dialogWindow = alertDialog.getWindow();
        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.getWindow().setAttributes(attrs);
    }

    /**
     * @Description 设置加减数量选择器
     * @Auther 孙建旺
     * @Date 下午 6:56 2019/12/07
     * @Param []
     * @return void
     */
    private void setShopNumber(){
        shopNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @SuppressLint("ResourceType")
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void afterTextChanged(Editable s) {
                if(!shopNumber.getText().toString().equals("")) {
                    if (Integer.parseInt(shopNumber.getText().toString()) >= 1)
                        imgBtnJian.setImageDrawable(getContext().getDrawable(R.drawable.jian));
                    if (Integer.parseInt(shopNumber.getText().toString()) <= 999)
                        imgBtnPlus.setImageDrawable(getContext().getDrawable(R.drawable.plus));
                }
                if(!shopNumber.getText().toString().equals("")) {
                    if (Integer.parseInt(shopNumber.getText().toString()) <= 0)
                        shopNumber.setText(1+"");
                    if (Integer.parseInt(shopNumber.getText().toString()) > 999)
                        shopNumber.setText(999+"");
                }
            }
        });

        imgBtnJian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!shopNumber.getText().toString().equals("")) {
                    if (Integer.parseInt(shopNumber.getText().toString()) >= 2) {
                        int num = Integer.parseInt(shopNumber.getText().toString()) - 1;
                        shopNumber.setText(num+"");
                    }
                }
            }
        });

        imgBtnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!shopNumber.getText().toString().equals("")) {
                    if (Integer.parseInt(shopNumber.getText().toString()) < 999) {
                        int num = Integer.parseInt(shopNumber.getText().toString()) + 1;
                        shopNumber.setText(num+"");
                    }
                }
            }
        });
    }

    /**
     * @Description 上传购买种子信息
     * @Auther 孙建旺
     * @Date 下午 3:08 2019/12/09
     * @Param []
     * @return void
     */
    private void buyFlowers(final int userID, final int cropId, final int num){
        new Thread(){
            @Override
            public void run() {
                super.run();
                Request request = new Request.Builder().url(getResources().getString(R.string.URL)+"/user/buyCrop?cropId="+cropId+"&number="+num).build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        String addCallback = "Fail";
                        Message message = Message.obtain();
                        message.obj = addCallback;
                        doAfterAdd.sendMessage(message);
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        OkHttpUtils.unauthorized(response.code());
                        String addCallback = response.body().string();
                        Message message = Message.obtain();
                        message.obj = addCallback;
                        doAfterAdd.sendMessage(message);
                    }
                });
            }
        }.start();
    }

    /**
     * @Description 点击宠物展示信息
     * @Author 孙建旺
     * @Date 下午8:28 2020/05/06
     * @Param [position]
     * @return void
     */
    private void ShowSinglePetPopUp(final int position){
        petItemPopUpWindow = new PetItemPopUpWindow(getContext(),pet_list.get(position));
        petItemPopUpWindow.setHeight((int)(ds.heightPixels));
        petItemPopUpWindow.setWidth((int)(ds.widthPixels*0.6));
        petItemPopUpWindow.showAtLocation(gridView, Gravity.CENTER,0,0);
    }
}
