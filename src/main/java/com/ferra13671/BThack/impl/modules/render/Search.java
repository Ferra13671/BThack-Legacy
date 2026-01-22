package com.ferra13671.BThack.impl.modules.render;

import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.BThack.core.client.Client;
import com.ferra13671.BThack.core.render.BThackRender;
import com.ferra13671.BThack.core.render.box.RenderBox;
import com.ferra13671.BThack.core.render.line.RenderLine;
import com.ferra13671.BThack.events.render.RenderWorldLastEvent;
import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.managers.impl.setting.Settings.BooleanSetting;
import com.ferra13671.BThack.managers.impl.setting.Settings.ColorSetting;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.utils.ChatUtils;
import com.ferra13671.BThack.api.utils.datalist.BlockList;
import com.ferra13671.BThack.api.utils.datalist.DataLists;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.awt.*;
import java.util.ArrayList;

@ModuleInfo(name = "Search", description = "lang.module.Search", category = "RENDER")
public class Search extends Module {

    public final ColorSetting boxColor = new ColorSetting("Box Color", this, new Color(255, 255, 255, 102));
    public final ColorSetting lineColor = new ColorSetting("Line Color", this, new Color(255, 255, 255, 255));
    public final BooleanSetting tracers = new BooleanSetting("Tracers", this, false);


    @Override
    public void onEnable() {
        super.onEnable();
        Managers.BLOCK_SEARCH_MANAGER.start();
        if (!nullCheck()) ChatUtils.sendMessage(Formatting.GRAY + "Use: " + Client.clientInfo.getChatPrefix() + DataLists.get("Search", BlockList.class).editDataListCommand.getAliases()[0]);
    }

    @EventSubscriber
    @SuppressWarnings("unused")
    public void onRender(RenderWorldLastEvent e) {

        ArrayList<RenderBox> boxes = new ArrayList<>();
        ArrayList<RenderLine> lines = new ArrayList<>();

        for (BlockPos pos : Managers.BLOCK_SEARCH_MANAGER.getResults()) {
            Box box = new Box(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);

            boxes.add(new RenderBox(
                    box,
                    lineColor.getValue().getRed() / 255f,
                    lineColor.getValue().getGreen() / 255f,
                    lineColor.getValue().getBlue() / 255f,
                    lineColor.getValue().getAlpha() / 255f,
                    boxColor.getValue().getRed() / 255f,
                    boxColor.getValue().getGreen() / 255f,
                    boxColor.getValue().getBlue() / 255f,
                    boxColor.getValue().getAlpha() / 255f
            ));
            lines.add(new RenderLine(
                    box.getCenter(),
                    lineColor.getValue().getRed() / 255f,
                    lineColor.getValue().getGreen() / 255f,
                    lineColor.getValue().getBlue() / 255f,
                    lineColor.getValue().getAlpha() / 255f
            ));
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
