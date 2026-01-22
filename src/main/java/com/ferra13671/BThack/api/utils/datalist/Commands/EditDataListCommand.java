package com.ferra13671.BThack.api.utils.datalist.Commands;

import com.ferra13671.BThack.managers.impl.command.AbstractCommand;
import com.ferra13671.BThack.api.utils.datalist.DataList;

public abstract class EditDataListCommand<T, KEY> extends AbstractCommand {
    public final String descName;
    public final DataList<T, KEY> dataList;

    public EditDataListCommand(String descriptionKey, String descName, String alias, DataList<T, KEY> dataList) {
        super(descriptionKey, alias);
        this.descName = descName;
        this.dataList = dataList;
    }

    @Override
    public String getDescription() {
        return String.format(super.getDescription(), descName);
    }
}
