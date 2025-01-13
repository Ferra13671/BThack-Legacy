package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Box.RenderBox;
import com.ferra13671.BThack.Core.Render.Line.RenderLine;
import com.ferra13671.BThack.api.Events.Render.RenderWorldLastEvent;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.List.BlockList.BlockLists;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.util.ArrayList;

public class Search extends Module {

    public final NumberSetting searchRed = new NumberSetting("Search Red", this, 255, 0, 255, false);
    public final NumberSetting searchGreen = new NumberSetting("Search Green", this, 255, 0, 255, false);
    public final NumberSetting searchBlue = new NumberSetting("Search Blue", this, 255, 0, 255, false);
    public final BooleanSetting tracers = new BooleanSetting("Tracers", this, false);

    public Search() {
        super("Search",
                "lang.module.Search",
                KeyboardUtils.RELEASE,
                MCategory.RENDER,
                false
        );

        initSettings(
                searchRed,
                searchGreen,
                searchBlue,
                tracers
        );
    }

    @Override
    public void onEnable() {
        super.onEnable();
        Managers.BLOCK_SEARCH_MANAGER.start();
        if (!nullCheck()) ChatUtils.sendMessage(Formatting.GRAY + "Use: " + Client.clientInfo.getChatPrefix() + BlockLists.get("Search").editBlockListCommand.getAliases()[0]);
    }

    @EventSubscriber
    @SuppressWarnings("unused")
    public void onRender(RenderWorldLastEvent e) {

        ArrayList<RenderBox> boxes = new ArrayList<>();
        ArrayList<RenderLine> lines = new ArrayList<>();

        float red = ((int) searchRed.getValue()) / 255f;
        float green = ((int) searchGreen.getValue()) / 255f;
        float blue = ((int) searchBlue.getValue()) / 255f;

        for (BlockPos pos : Managers.BLOCK_SEARCH_MANAGER.getResults()) {
            Box box = new Box(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);

            boxes.add(new RenderBox(box, red, green, blue, 0.6f, red, green, blue, 0.4f));
            lines.add(new RenderLine(box.getCenter(), red, green, blue, 1f));
        }

        BThackRender.boxRender.prepareBoxRender();
        BThackRender.boxRender.renderBoxes(boxes);
        BThackRender.boxRender.stopBoxRender();

        if (tracers.getValue()) {
            BThackRender.lineRender.prepareLineRenderer();
            BThackRender.lineRender.renderLines(lines);
            BThackRender.lineRender.stopLineRenderer();
        }
    }
}
