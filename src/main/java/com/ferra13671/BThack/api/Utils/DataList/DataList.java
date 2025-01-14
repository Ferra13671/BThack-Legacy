package com.ferra13671.BThack.api.Utils.DataList;

import com.ferra13671.BThack.Core.FileSystem.FileSystem;
import com.ferra13671.BThack.api.Utils.DataList.Commands.AbstractDataListCommand;
import com.ferra13671.BThack.api.Utils.DataList.Commands.EditDataListCommand;

import java.io.IOException;
import java.util.ArrayList;

public abstract class DataList<T, KEY> {
    public final String txtName;

    public EditDataListCommand<T, KEY> editDataListCommand;
    public AbstractDataListCommand abstractDataListCommand;

    public final ArrayList<T> values = new ArrayList<>();
    public final ArrayList<String> valueNames = new ArrayList<>();

    public DataList(String descName, String txtName) {
        this.txtName = txtName;
        try {
            FileSystem.registerFolder(descName, "");
            FileSystem.registerFile(txtName, descName, "txt");
        } catch (Exception ignored) {}
    }

    public final void initEditDataListCommand(EditDataListCommand<T, KEY> editDataListCommand) {
        if (this.editDataListCommand == null) this.editDataListCommand = editDataListCommand;
    }

    public final void initAbstractDataListCommand(AbstractDataListCommand abstractDataListCommand) {
        if (this.abstractDataListCommand == null) this.abstractDataListCommand = abstractDataListCommand;
    }

    public abstract void saveInFile() throws IOException;

    public abstract void loadFromFile() throws IOException;

    public abstract void addToList(KEY key);

    public abstract void removeFromList(KEY key);

    public abstract void clearList();

    public abstract void sendAllList();

    public void postAction() {}
}
