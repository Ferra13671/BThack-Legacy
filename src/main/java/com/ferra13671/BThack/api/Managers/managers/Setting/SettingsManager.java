package com.ferra13671.BThack.api.Managers.managers.Setting;

import com.ferra13671.BThack.api.Managers.Manager;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.Setting;
import com.ferra13671.BThack.api.Module.Module;

import java.util.ArrayList;
import java.util.Objects;

public class SettingsManager implements Manager {
	private final ArrayList<Setting> moduleSettings = new ArrayList<>();

	@Override
	public void init() {
		//no action
	}

	public void addModuleSetting(Setting in){
		this.moduleSettings.add(in);
	}
	
	public ArrayList<Setting> getModuleSettings(){
		return this.moduleSettings;
	}
	
	public ArrayList<Setting> getSettingsByMod(Module mod){
		ArrayList<Setting> out = new ArrayList<>();
		for(Setting s : getModuleSettings()) {
			if (s != null) {
				if (s.getModule().equals(mod)) {
					out.add(s);
				}
			}
		}
		if(out.isEmpty()) {
			return null;
		}
		return out;
	}
	
	public Setting getModuleSettingByName(String mod, String name){
		for(Setting set : getModuleSettings()) {
			if (set == null) continue;
			if(set.getName().equalsIgnoreCase(name) && Objects.equals(set.getModule().name, mod)){
				return set;
			}
		}
		return null;
	}
}
