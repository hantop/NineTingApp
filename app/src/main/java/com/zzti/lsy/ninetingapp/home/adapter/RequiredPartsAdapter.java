package com.zzti.lsy.ninetingapp.home.adapter;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.entity.RequiredParts;
import com.zzti.lsy.ninetingapp.utils.StringUtil;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import java.util.List;

/**
 * 维修记录明细
 */
public class RequiredPartsAdapter extends BaseQuickAdapter<RequiredParts, BaseViewHolder> {
    private List<RequiredParts> carMaintenanceEntities;

    public RequiredPartsAdapter(List<RequiredParts> carMaintenanceEntities) {
        super(R.layout.item_required_parts, carMaintenanceEntities);
        this.carMaintenanceEntities = carMaintenanceEntities;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final RequiredParts item) {
        helper.setText(R.id.tv_partsIndex, "配件明细（" + (helper.getAdapterPosition() + 1) + ")");
        final EditText etPartName = helper.getView(R.id.et_partsName);
        final EditText etPrice = helper.getView(R.id.et_price);
        RadioGroup radioGroup = helper.getView(R.id.radioGroup);
        final ImageView imageView = helper.getView(R.id.imageView);
        radioGroup.setOnCheckedChangeListener(null);
        radioGroup.clearCheck();
        if (item.getType() == 0) {
            radioGroup.check(R.id.rb_exist);
            etPartName.setHint("请选择");
            etPartName.setCursorVisible(false);
            etPartName.setFocusable(false);
            etPartName.setFocusableInTouchMode(false);
            etPartName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (partNameClickListener != null) {
                        partNameClickListener.partNameClick(helper.getAdapterPosition());
                    }
                }
            });
            etPrice.setHint("");
            etPrice.setEnabled(false);
        } else {
            radioGroup.check(R.id.rb_purchase);
            etPartName.setHint("请输入所需配件型号#名称");
            etPartName.setCursorVisible(true);
            etPartName.setFocusable(true);
            etPartName.setFocusableInTouchMode(true);
            etPartName.setOnClickListener(null);
            etPrice.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            etPrice.setHint("请输入");
            etPrice.setEnabled(true);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (groupSelectListener != null) {
                    groupSelectListener.radioSelect(checkedId, helper.getAdapterPosition());
                }
            }
        });
        if (StringUtil.isNullOrEmpty(item.getPartsName())) {
            etPartName.setText("");
        } else {
            etPartName.setText(item.getPartsModel() + "#" + item.getPartsName());
        }
        if (!StringUtil.isNullOrEmpty(item.getPurchasedPrice())) {//配件单价
            etPrice.setText(item.getPurchasedPrice());
        } else {
            etPrice.setText("");
        }
        if (type == 2) {//记录
            if (!StringUtil.isNullOrEmpty(item.getNumber())) { //所需配件数量
                helper.setText(R.id.tv_partsAmount, item.getNumber());
            } else {
                helper.setText(R.id.tv_partsAmount, "");
            }
        } else {
            if (!StringUtil.isNullOrEmpty(item.getRpNumber())) { //所需配件数量
                helper.setText(R.id.tv_partsAmount, item.getRpNumber());
            } else {
                helper.setText(R.id.tv_partsAmount, "");
            }
        }
        if (!StringUtil.isNullOrEmpty(item.getPartsNumber())) {//配件库存
            helper.setText(R.id.tv_amount, "库存" + item.getPartsNumber());
        } else {
            helper.setText(R.id.tv_amount, "");
        }
        if (type == 1) {//录入的时候部分按钮可以操作
            enableRadioGroup(radioGroup);
            if (helper.getAdapterPosition() >= 1) {
                helper.getView(R.id.tv_delete).setVisibility(View.VISIBLE);
            } else {
                helper.getView(R.id.tv_delete).setVisibility(View.GONE);
            }
            helper.getView(R.id.ib_sub).setVisibility(View.VISIBLE);
            helper.getView(R.id.ib_add).setVisibility(View.VISIBLE);
            if (item.getType() == 0) {
                imageView.setVisibility(View.VISIBLE);
            } else {
                imageView.setVisibility(View.GONE);
            }
            helper.getView(R.id.tv_amount).setVisibility(View.VISIBLE);
            helper.addOnClickListener(R.id.ib_sub).addOnClickListener(R.id.ib_add).addOnClickListener(R.id.tv_delete);
            helper.getView(R.id.view).setVisibility(View.GONE);
            helper.getView(R.id.ll_cancelling).setVisibility(View.GONE);
        } else {
            helper.getView(R.id.view).setVisibility(View.VISIBLE);
            helper.getView(R.id.ll_cancelling).setVisibility(View.VISIBLE);
            ((EditText) helper.getView(R.id.et_number)).setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            ((EditText) helper.getView(R.id.et_number)).addTextChangedListener(new MyTextWatcher(item));
            disableRadioGroup(radioGroup);
            helper.getView(R.id.imageView).setVisibility(View.GONE);
            helper.getView(R.id.tv_delete).setVisibility(View.GONE);
            helper.getView(R.id.ib_sub).setVisibility(View.GONE);
            helper.getView(R.id.ib_add).setVisibility(View.GONE);
            helper.getView(R.id.tv_amount).setVisibility(View.GONE);
        }
    }


    private void disableRadioGroup(RadioGroup testRadioGroup) {
        for (int i = 0; i < testRadioGroup.getChildCount(); i++) {
            testRadioGroup.getChildAt(i).setEnabled(false);
        }
    }

    private void enableRadioGroup(RadioGroup testRadioGroup) {
        for (int i = 0; i < testRadioGroup.getChildCount(); i++) {
            testRadioGroup.getChildAt(i).setEnabled(true);
        }
    }

    class MyTextWatcher implements TextWatcher {
        private RequiredParts requiredParts;

        public MyTextWatcher(RequiredParts requiredParts) {
            this.requiredParts = requiredParts;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (!StringUtil.isNullOrEmpty(editable.toString())) {
                if (Integer.parseInt(editable.toString()) > Integer.parseInt(requiredParts.getNumber())) {
                    UIUtils.showT("输入数量不能大于当前剩余数量");
                    editable.clear();
                } else {
                    requiredParts.setRpNumber(editable.toString());
                }
            } else {
                requiredParts.setRpNumber("0");
            }
        }
    }

    private int type; //1代表录入  2代表记录

    public void setType(int type) {
        this.type = type;
    }

    GroupSelectListener groupSelectListener;

    public void setGroupSelectListener(GroupSelectListener groupSelectListener) {
        this.groupSelectListener = groupSelectListener;
    }

    public interface GroupSelectListener {
        void radioSelect(int checkedId, int position);
    }

    PartNameClickListener partNameClickListener;

    public void setPartNameClickListener(PartNameClickListener partNameClickListener) {
        this.partNameClickListener = partNameClickListener;
    }

    public interface PartNameClickListener {
        void partNameClick(int position);
    }


}
