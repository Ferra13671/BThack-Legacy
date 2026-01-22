package com.ferra13671.BThack.managers.impl.setting;

import com.ferra13671.BThack.managers.impl.setting.Settings.Setting;
import com.ferra13671.BThack.api.utils.Initializable;
import com.ferra13671.BThack.api.module.Module;

import java.util.ArrayList;

public class SettingsManager implements Initializable {
	private final ArrayList<Setting<?>> moduleSettings = new ArrayList<>();

	public void addModuleSetting(Setting<?> in){
		this.moduleSettings.add(in);
	}
	
	public ArrayList<Setting<?>> getSettingsByModule(Module module){
		ArrayList<Setting<?>> out = new ArrayList<>(moduleSettings);
		out.removeIf(s -> s == null || !s.getModule().equals(module));
		return out;
	}

	@Override
	public void init() {}
}
