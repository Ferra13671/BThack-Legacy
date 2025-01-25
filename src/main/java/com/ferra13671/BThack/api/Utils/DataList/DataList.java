package com.ferra13671.BThack.api.Utils.DataList;

import com.ferra13671.BThack.Core.FileSystem.ConfigSystem.ConfigUtils;
import com.ferra13671.BThack.Core.FileSystem.FileSystem;
import com.ferra13671.BThack.api.Utils.DataList.Commands.AbstractDataListCommand;
import com.ferra13671.BThack.api.Utils.DataList.Commands.EditDataListCommand;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;

public abstract class DataList<T, KEY> {
    public final String txtName;

    public EditDataListCommand<T, KEY> editDataListCommand;
    public AbstractDataListCommand abstractDataListCommand;

    public final ArrayList<T> values = new ArrayList<>();
    public final ArrayList<String> valueNames = new ArrayList<>();

    public DataList(String txtName) {
        this.txtName = txtName;
        try {
            FileSystem.registerFile(txtName, "", FileSystem.FileType.JSON);
        } catch (Exception ignored) {}
    }

    public final void initEditDataListCommand(EditDataListCommand<T, KEY> editDataListCommand) {
        if (this.editDataListCommand == null) this.editDataListCommand = editDataListCommand;
    }

    public final void initAbstractDataListCommand(AbstractDataListCommand abstractDataListCommand) {
        if (this.abstractDataListCommand == null) this.abstractDataListCommand = abstractDataListCommand;
    }

    public final void saveInFile() throws IOException {
        ConfigUtils.saveInJson(txtName, "", this::save);
    }

    public final void loadFromFile() throws IOException {
        ConfigUtils.loadFromJson(txtName, "", this::load, () -> {});
    }

    protected abstract void save(JsonObject jsonObject);

    protected abstract void load(JsonObject jsonObject);

    public abstract void addToList(KEY key);

    public abstract void removeFromList(KEY key);

    public abstract void clearList();

    public abstract void sendAllList();

    public void postAction() {}
}
