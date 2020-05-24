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
    private Toast toast;
    private OkHttpClient okHttpClient;
    private Handler doAfterAdd;
    private PetItemPopUpWindow petItemPopUpWindow;
    private UtilItemPopUp utilItemPopUp;
    private PlantItemPopUp plantItemPopUp;
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
                        showSinglePlantPopUp(position);
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
        if(utilItemPopUp==null) {
            utilItemPopUp = new UtilItemPopUp(getContext(), petUtils.get(position));
        }
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
    private void showSinglePlantPopUp(int position){
        if(plantItemPopUp == null){
            plantItemPopUp = new PlantItemPopUp(getContext(),shopList.get(position));
        }
        plantItemPopUp.setHeight((int)(ds.heightPixels));
        plantItemPopUp.setWidth((int)(0.6*ds.widthPixels));
        plantItemPopUp.showAtLocation(gridView,Gravity.CENTER,0,0);
    }

    /**
     * @Description 点击宠物展示信息
     * @Author 孙建旺
     * @Date 下午8:28 2020/05/06
     * @Param [position]
     * @return void
     */
    private void ShowSinglePetPopUp(final int position){
        if(petItemPopUpWindow == null) {
            petItemPopUpWindow = new PetItemPopUpWindow(getContext(), pet_list.get(position));
        }
        petItemPopUpWindow.setHeight((int)(ds.heightPixels));
        petItemPopUpWindow.setWidth((int)(ds.widthPixels*0.6));
        petItemPopUpWindow.showAtLocation(gridView, Gravity.CENTER,0,0);
    }
}
