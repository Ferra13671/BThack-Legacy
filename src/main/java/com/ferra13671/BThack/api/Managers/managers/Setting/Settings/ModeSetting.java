package com.ferra13671.BThack.api.Managers.managers.Setting.Settings;

import com.ferra13671.BThack.api.Module.Module;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.List;
import java.util.function.Supplier;

public class ModeSetting extends Setting<String> {

    private List<String> options;
    private int modeIndex;


    public ModeSetting(String name, Module module, List<String> options, Supplier<Boolean> dependence) {
        super(name, module, "", dependence);
        if (options.isEmpty())
            options.add("NULL");
        this.options = options;
        value = options.getFirst();
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

    public int getIndex() {
        return this.modeIndex;
    }

    public void setIndex(int index) {
        this.modeIndex = index;
    }

    @Override
    public void toDefault() {
        modeIndex = 0;
        value = options.getFirst();
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

    public ModeSetting defaultValue(String value) {
        for (int i = 0; i < options.size(); i++) {
            if (options.get(i).equals(value)) {
                setValue(value);
                setIndex(i);
            }
        }
        return this;
    }
}
