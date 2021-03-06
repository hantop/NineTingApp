package com.zzti.lsy.ninetingapp.home.generalmanager;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.gson.Gson;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.entity.ConditionEntity;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.entity.PactInfo;
import com.zzti.lsy.ninetingapp.entity.ProjectEntity;
import com.zzti.lsy.ninetingapp.entity.RepairTypeEntity;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.event.EventMessage;
import com.zzti.lsy.ninetingapp.home.SuccessActivity;
import com.zzti.lsy.ninetingapp.home.adapter.ConditionAdapter;
import com.zzti.lsy.ninetingapp.home.adapter.ProjectAdapter;
import com.zzti.lsy.ninetingapp.home.adapter.RepairTypeAdapter;
import com.zzti.lsy.ninetingapp.home.parts.PartsInputActivity;
import com.zzti.lsy.ninetingapp.network.OkHttpManager;
import com.zzti.lsy.ninetingapp.network.Urls;
import com.zzti.lsy.ninetingapp.utils.DateUtil;
import com.zzti.lsy.ninetingapp.utils.DensityUtils;
import com.zzti.lsy.ninetingapp.utils.ParseUtils;
import com.zzti.lsy.ninetingapp.utils.SpUtils;
import com.zzti.lsy.ninetingapp.utils.StringUtil;
import com.zzti.lsy.ninetingapp.utils.UIUtils;
import com.zzti.lsy.ninetingapp.view.MAlertDialog;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author：anxin on 2018/10/12 20:06
 * 合同录入界面
 */
public class PactInputActivity extends BaseActivity implements AdapterView.OnItemClickListener, PopupWindow.OnDismissListener {
    @BindView(R.id.et_pactName)
    EditText etPactName;
    @BindView(R.id.et_pactContent)
    EditText etPactContent;
    @BindView(R.id.tv_pactType)
    TextView tvPactType;
    @BindView(R.id.tv_project)
    TextView tvProject;
    @BindView(R.id.tv_pactSchedule)
    TextView tvPactSchedule;
    @BindView(R.id.et_pactMoney)
    EditText etPactMoney;
    @BindView(R.id.et_pactRealMoney)
    EditText etPactRealMoney;
    @BindView(R.id.ll_addMoney)
    LinearLayout llAddMoney;
    @BindView(R.id.et_addMoney)
    EditText etAddMoney;
    @BindView(R.id.et_pactInMoney)
    EditText etPactInMoney;
    @BindView(R.id.et_pactOutMoney)
    EditText etPactOutMoney;
    @BindView(R.id.tv_pactTime)
    TextView tvPactTime;
    @BindView(R.id.btn_inputPact)
    Button btnInputPact;

    private PopupWindow popupWindowPactType;
    private ListView lvPactType;
    private ConditionAdapter conditionAdapterPactType;
    private List<ConditionEntity> conditionsPactTypes;

    private PopupWindow popupWindowPactSchedule;
    private ListView lvPactSchedule;
    private ConditionAdapter conditionAdapterPactSchedule;
    private List<ConditionEntity> conditionsPactSchedules;

    private PopupWindow popupWindowProject;
    private ListView mListViewProject;
    private ProjectAdapter projectAdapter;
    private List<ProjectEntity> projectEntities;

    @Override
    public int getContentViewId() {
        return R.layout.activity_pact_input;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();

    }

    private int tag;

    private void initView() {
        setTitle("录入合同");
        etPactInMoney.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etPactMoney.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etPactOutMoney.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etPactRealMoney.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etAddMoney.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        tag = UIUtils.getInt4Intent(this, "TAG");
        if (tag == 0) {//录入
            initPopPactType();
            initPopPactSchedule();
            initPop();
            initData();
            btnInputPact.setText("录入合同");
        } else if (tag == 1) {//修改
            btnInputPact.setText("修改");
            tvPactType.setEnabled(false);
            tvPactSchedule.setEnabled(false);
            tvProject.setEnabled(false);
            etPactRealMoney.setEnabled(false);
            etPactOutMoney.setEnabled(false);
            etPactContent.setEnabled(false);
            etPactMoney.setEnabled(false);
            etPactName.setEnabled(false);
            llAddMoney.setVisibility(View.VISIBLE);
            PactInfo pactInfo = (PactInfo) getIntent().getSerializableExtra("PACTINFO");
            setData(pactInfo);
        }

    }

    /**
     * 设置数据
     *
     * @param pactInfo
     */
    private void setData(PactInfo pactInfo) {
        pactID = pactInfo.getPactID();
        tvProject.setText(pactInfo.getProjectName());
        tvPactSchedule.setText(pactInfo.getPactSchedule());
        tvPactType.setText(pactInfo.getPactType());
        etPactMoney.setText(pactInfo.getPactMoney());
        tvPactTime.setText(pactInfo.getPactTime().split("T")[0]);
        etPactName.setText(pactInfo.getPactName());
        etPactInMoney.setText(pactInfo.getPactInMoney());
        etPactContent.setText(pactInfo.getPactContent());
        etPactOutMoney.setText(pactInfo.getPactOutMoney());
        etPactRealMoney.setText(pactInfo.getPactRealMoney());
    }


    private void initPop() {
        View contentview = getLayoutInflater().inflate(R.layout.popup_list, null);
        contentview.setFocusable(true); // 这个很重要
        contentview.setFocusableInTouchMode(true);
        popupWindowProject = new PopupWindow(contentview, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindowProject.setFocusable(true);
        popupWindowProject.setOutsideTouchable(true);
        //设置消失监听
        popupWindowProject.setOnDismissListener(this);
        popupWindowProject.setBackgroundDrawable(new ColorDrawable(0x00000000));
        contentview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    popupWindowProject.dismiss();
                    return true;
                }
                return false;
            }
        });
        mListViewProject = contentview.findViewById(R.id.pop_list);
        projectEntities = new ArrayList<>();
        projectAdapter = new ProjectAdapter(projectEntities);
        mListViewProject.setAdapter(projectAdapter);
        mListViewProject.setOnItemClickListener(this);
        popupWindowProject.setAnimationStyle(R.style.anim_bottomPop);
    }

    private void initPopPactSchedule() {
        View contentview = getLayoutInflater().inflate(R.layout.popup_list, null);
        contentview.setFocusable(true); // 这个很重要
        contentview.setFocusableInTouchMode(true);
        popupWindowPactSchedule = new PopupWindow(contentview, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindowPactSchedule.setFocusable(true);
        popupWindowPactSchedule.setOutsideTouchable(true);
        //设置消失监听
        popupWindowPactSchedule.setOnDismissListener(this);
        popupWindowPactSchedule.setBackgroundDrawable(new ColorDrawable(0x00000000));
        contentview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    popupWindowPactSchedule.dismiss();
                    return true;
                }
                return false;
            }
        });
        lvPactSchedule = contentview.findViewById(R.id.pop_list);
        conditionsPactSchedules = new ArrayList<>();
        ConditionEntity conditionEntity0 = new ConditionEntity();
        conditionEntity0.setName("已结清");
        ConditionEntity conditionEntity1 = new ConditionEntity();
        conditionEntity1.setName("未结清");

        conditionsPactSchedules.add(conditionEntity0);
        conditionsPactSchedules.add(conditionEntity1);

        conditionAdapterPactSchedule = new ConditionAdapter(conditionsPactSchedules);
        conditionAdapterPactSchedule.setTag(1);//背景色为黑色
        lvPactSchedule.setAdapter(conditionAdapterPactSchedule);
        lvPactSchedule.setOnItemClickListener(this);
        popupWindowPactSchedule.setAnimationStyle(R.style.anim_bottomPop);
    }


    private void initPopPactType() {
        View contentview = getLayoutInflater().inflate(R.layout.popup_list, null);
        contentview.setFocusable(true); // 这个很重要
        contentview.setFocusableInTouchMode(true);
        popupWindowPactType = new PopupWindow(contentview, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindowPactType.setFocusable(true);
        popupWindowPactType.setOutsideTouchable(true);
        //设置消失监听
        popupWindowPactType.setOnDismissListener(this);
        popupWindowPactType.setBackgroundDrawable(new ColorDrawable(0x00000000));
        contentview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    popupWindowPactType.dismiss();
                    return true;
                }
                return false;
            }
        });
        lvPactType = contentview.findViewById(R.id.pop_list);
        conditionsPactTypes = new ArrayList<>();
        ConditionEntity conditionEntity0 = new ConditionEntity();
        conditionEntity0.setName("项目合同");
        ConditionEntity conditionEntity1 = new ConditionEntity();
        conditionEntity1.setName("外单合同");

        conditionsPactTypes.add(conditionEntity0);
        conditionsPactTypes.add(conditionEntity1);

        conditionAdapterPactType = new ConditionAdapter(conditionsPactTypes);
        conditionAdapterPactType.setTag(1);
        lvPactType.setAdapter(conditionAdapterPactType);
        lvPactType.setOnItemClickListener(this);
        popupWindowPactType.setAnimationStyle(R.style.anim_bottomPop);
    }

    private void initData() {
        showDia();
        getProject();
    }

    private void getProject() {
        OkHttpManager.postFormBody(Urls.POST_GETPROJECT, null, tvPactSchedule, new OkHttpManager.OnResponse<String>() {
            @Override
            public String analyseResult(String result) {
                return result;
            }

            @Override
            public void onSuccess(String s) {
                cancelDia();
                MsgInfo msgInfo = ParseUtils.parseJson(s, MsgInfo.class);
                if (msgInfo.getCode() == 200) {
                    try {
                        JSONArray jsonArray = new JSONArray(msgInfo.getData());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            ProjectEntity projectEntity = ParseUtils.parseJson(jsonArray.getString(i), ProjectEntity.class);
                            projectEntities.add(projectEntity);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msgInfo.getCode() == C.Constant.HTTP_UNAUTHORIZED) {
                    loginOut();
                } else {
                    UIUtils.showT(msgInfo.getMsg());
                }
            }
        });
    }

    private int condition = 1;

    @OnClick({R.id.tv_pactType, R.id.tv_project, R.id.tv_pactSchedule, R.id.tv_pactTime, R.id.btn_inputPact})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.tv_pactType://合同类型
                condition = 1;
                setBackgroundAlpha(0.5f);
                popupWindowPactType.showAtLocation(etPactContent, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.tv_project:
                condition = 3;
                setBackgroundAlpha(0.5f);
                popupWindowProject.showAtLocation(etPactContent, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.tv_pactSchedule://合同进度
                condition = 2;
                setBackgroundAlpha(0.5f);
                popupWindowPactSchedule.showAtLocation(etPactContent, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.tv_pactTime://合同到期时间
                showCustomTime();
                break;
            case R.id.btn_inputPact://录入合同
                if (tag == 0) {//录入合同
                    if (StringUtil.isNullOrEmpty(etPactName.getText().toString())) {
                        UIUtils.showT("合同名称/编号不能为空");
                        break;
                    }
                    if (StringUtil.isNullOrEmpty(tvPactType.getText().toString())) {
                        UIUtils.showT("合同类型不能为空");
                        break;
                    }
                    if (StringUtil.isNullOrEmpty(tvProject.getText().toString())) {
                        UIUtils.showT("项目部不能为空");
                        break;
                    }
                    if (StringUtil.isNullOrEmpty(tvPactSchedule.getText().toString())) {
                        UIUtils.showT("合同进度不能为空");
                        break;
                    }
                    if (StringUtil.isNullOrEmpty(etPactContent.getText().toString())) {
                        UIUtils.showT("合同简介不能为空");
                        break;
                    }

                    if (StringUtil.isNullOrEmpty(etPactMoney.getText().toString())) {
                        UIUtils.showT("合同总金额不能为空");
                        break;
                    }
                    if (StringUtil.isNullOrEmpty(etPactRealMoney.getText().toString())) {
                        UIUtils.showT("合同应收金额不能为空");
                        break;
                    }
                    if (StringUtil.isNullOrEmpty(etPactInMoney.getText().toString())) {
                        UIUtils.showT("合同已收金额不能为空");
                        break;
                    }
                    if (StringUtil.isNullOrEmpty(etPactOutMoney.getText().toString())) {
                        UIUtils.showT("合同未收金额不能为空");
                        break;
                    }
                    if (StringUtil.isNullOrEmpty(tvPactTime.getText().toString())) {
                        UIUtils.showT("合同到期时间不能为空");
                        break;
                    }
                    PactInfo pactInfo = new PactInfo();
                    pactInfo.setPactName(etPactName.getText().toString());
                    pactInfo.setPactContent(etPactContent.getText().toString());
                    pactInfo.setPactMoney(etPactMoney.getText().toString());
                    pactInfo.setPactInMoney(etPactInMoney.getText().toString());
                    pactInfo.setPactOutMoney(etPactOutMoney.getText().toString());
                    pactInfo.setPactTime(tvPactTime.getText().toString());
                    pactInfo.setPactRealMoney(etPactRealMoney.getText().toString());
                    pactInfo.setPactSchedule(tvPactSchedule.getText().toString());
                    pactInfo.setPactType(tvPactType.getText().toString());
                    pactInfo.setProjectID(projectID);
                    showDia();
                    submitPact(pactInfo);
                } else if (tag == 1) {//修改合同
                    if (StringUtil.isNullOrEmpty(etAddMoney.getText().toString())) {
                        UIUtils.showT("本次收款不能为空");
                        break;
                    }
                    MAlertDialog.show(this, "提示", "是否确认已收款" + etAddMoney.getText().toString() + "元？", false, "确定", "取消", new MAlertDialog.OnConfirmListener() {
                        @Override
                        public void onConfirmClick(String msg) {
                            showDia();
                            alertPact();
                        }

                        @Override
                        public void onCancelClick() {

                        }
                    }, true);

                }
                break;
        }
    }

    /**
     * 显示时间选择器
     */
    private void showCustomTime() {
        Calendar instance = Calendar.getInstance();
        instance.set(DateUtil.getCurYear(), DateUtil.getCurMonth(), DateUtil.getCurDay());
        //时间选择器
        TimePickerView pvTime = new TimePickerBuilder(PactInputActivity.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                tvPactTime.setText(DateUtil.getDate(date));

            }
        }).setDate(instance).setType(new boolean[]{true, true, true, false, false, false})
                .setLabel(" 年", " 月", " 日", "", "", "")
                .isCenterLabel(false).build();
        pvTime.show();

    }

    private String pactID;

    /**
     * 更新合同
     */
    private void alertPact() {
        HashMap<String, String> params = new HashMap<>();
        params.put("pactID", pactID);
        params.put("addMoney", etAddMoney.getText().toString());
        OkHttpManager.postFormBody(Urls.ADMIN_UPDATEPACT, params, tvPactSchedule, new OkHttpManager.OnResponse<String>() {
            @Override
            public String analyseResult(String result) {
                return result;
            }

            @Override
            public void onSuccess(String s) {
                cancelDia();
                MsgInfo msgInfo = ParseUtils.parseJson(s, MsgInfo.class);
                if (msgInfo.getCode() == 200) {
                    setResult(2);
                    finish();
                } else if (msgInfo.getCode() == C.Constant.HTTP_UNAUTHORIZED) {
                    loginOut();
                } else {
                    UIUtils.showT(msgInfo.getMsg());
                }
            }

            @Override
            public void onFailed(int code, String msg, String url) {
                super.onFailed(code, msg, url);
                cancelDia();
            }
        });
    }

    /**
     * 录入合同
     *
     * @param pactInfo
     */
    private void submitPact(PactInfo pactInfo) {
        HashMap<String, String> params = new HashMap<>();
        params.put("pactJson", new Gson().toJson(pactInfo));
        OkHttpManager.postFormBody(Urls.ADMIN_ADDPACT, params, tvPactSchedule, new OkHttpManager.OnResponse<String>() {
            @Override
            public String analyseResult(String result) {
                return result;
            }

            @Override
            public void onSuccess(String s) {
                cancelDia();
                MsgInfo msgInfo = ParseUtils.parseJson(s, MsgInfo.class);
                if (msgInfo.getCode() == 200) {
                    Intent intent = new Intent(PactInputActivity.this, SuccessActivity.class);
                    intent.putExtra("TAG", 9);
                    startActivity(intent);
                } else if (msgInfo.getCode() == C.Constant.HTTP_UNAUTHORIZED) {
                    loginOut();
                } else {
                    UIUtils.showT(msgInfo.getMsg());
                }
            }

            @Override
            public void onFailed(int code, String msg, String url) {
                super.onFailed(code, msg, url);
                cancelDia();
            }
        });
    }

    private String projectID;

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (condition == 1) {//合同类型
            tvPactType.setText(conditionsPactTypes.get(i).getName());
            popupWindowPactType.dismiss();
        } else if (condition == 2) {//合同进度
            tvPactSchedule.setText(conditionsPactSchedules.get(i).getName());
            popupWindowPactSchedule.dismiss();
        } else if (condition == 3) {//项目部
            tvProject.setText(projectEntities.get(i).getProjectName());
            projectID = projectEntities.get(i).getProjectID();
            popupWindowProject.dismiss();
        }
    }

    @Override
    protected boolean openEventBus() {
        return true;
    }

    @Override
    protected void onEventComing(EventMessage paramEventCenter) {
        super.onEventComing(paramEventCenter);
        if (paramEventCenter.getEventCode() == C.EventCode.A && (Boolean) paramEventCenter.getData()) {
            finish();
        }
    }

    @Override
    public void onDismiss() {
        setBackgroundAlpha(1);
    }
}
