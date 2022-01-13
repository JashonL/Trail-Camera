package com.shuoxd.camera.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class DeviceSettingBean implements MultiItemEntity {

    //选项的类型，下拉、弹框等
    private int itemType;
    //设置项标题
    private String title;
    //设置项的真实值
    private String value;
    //设置项的显示内容
    private String valueStr;
    //设置项单位
    private String unit;
    //设置项弹框提示内容
    private String hint;
    //选项卡
    private String[] items;
    //选项卡对应的值
    private int[] items_value;
    //数据的倍数
    private float mul;
    //设置的key
    private String key;


    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValueStr() {
        return valueStr;
    }

    public void setValueStr(String valueStr) {
        this.valueStr = valueStr;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String[] getItems() {
        return items;
    }

    public void setItems(String[] items) {
        this.items = items;
    }


    public int[] getItems_value() {
        return items_value;
    }

    public void setItems_value(int[] items_value) {
        this.items_value = items_value;
    }

    public float getMul() {
        return mul;
    }

    public void setMul(float mul) {
        this.mul = mul;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}


