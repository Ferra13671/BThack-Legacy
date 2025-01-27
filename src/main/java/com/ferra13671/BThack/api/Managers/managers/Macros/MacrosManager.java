package com.ferra13671.BThack.api.Managers.managers.Macros;

import com.ferra13671.BThack.api.Utils.Initializable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class MacrosManager implements Initializable {
    private final List<Macro> macros = new ArrayList<>();

    @Override
    public void init() {
    }

    public void addMacro(Macro macro) {
        macros.add(macro);
    }

    public void removeMacro(Macro macro) {
        macros.remove(macro);
    }

    public boolean isEmpty() {
        return macros.isEmpty();
    }

    public void forEach(Consumer<Macro> consumer) {
        macros.forEach(consumer);
    }

    public Stream<Macro> getMacros() {
        return macros.stream();
    }
}
