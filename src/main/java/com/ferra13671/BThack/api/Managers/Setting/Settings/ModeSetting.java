package com.ferra13671.BThack.api.Managers.Setting.Settings;

import com.ferra13671.BThack.api.Module.Module;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.List;
import java.util.function.Supplier;

public class ModeSetting extends Setting {

    private List<String> options;
    private String modeVal;
    private int modeIndex;


    public ModeSetting(String name, Module module, List<String> options, Supplier<Boolean> dependence) {
        super(name, module, dependence);
        if (options.isEmpty())
            options.add("NULL");
        this.options = options;
        modeVal = options.get(0);
        modeIndex = 0;
    }

    public ModeSetting(String name, Module module, List<String> options) {
        this(name, module, options, null);
    }

    public List<String> getOptions(){
        return this.options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public String getValue(){
        return modeVal;
    }

    public void setValue(String value){
        modeVal = value;
    }

    public int getIndex() {
        return this.modeIndex;
    }

    public void setIndex(int index) {
        this.modeIndex = index;
    }

    @Override
    public void toDefault() {
        modeIndex = 0;
        modeVal = options.get(0);
    }

    @Override
    public void load(JsonObject jsonObject, JsonElement jsonElement) {
        JsonElement indexObject = jsonObject.get(getName() + " Index");
        setValue(jsonElement.getAsString());
        setIndex(indexObject.getAsInt());
    }

    @Override
    public void save(JsonObject jsonObject) {
        jsonObject.add(getName(), new JsonPrimitive(getValue()));
        jsonObject.add(getName() + " Index", new JsonPrimitive(getIndex()));
    }
}
