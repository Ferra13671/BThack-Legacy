package com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config;

import com.ferra13671.BThack.api.Gui.ActionBot.AbstractTaskGui;
import com.ferra13671.BThack.api.Gui.ActionBot.ActionBotConfigGui;
import com.ferra13671.BThack.api.Utils.System.BThackScreen;
import com.ferra13671.BThack.api.Utils.System.buttons.ModeButton;
import com.ferra13671.BThack.api.Utils.System.buttons.NumberFrameButton;
import com.ferra13671.BThack.api.Utils.System.buttons.SwitchButton;
import com.ferra13671.BThack.api.Utils.System.buttons.TextFrameButton;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTasks.*;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.Utils.ActionBotTaskData;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.Utils.TaskButton;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.Utils.TaskSettingButton;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.ferra13671.BThack.api.Gui.ActionBot.ActionBotConfigGui.*;

public class ActionBotConfig {
    public static final ActionBotTask startTask = new ActionBotTask("Start") {
        @Override
        public void save(JsonObject jsonObject) {}

        @Override
        public void load(JsonObject jsonObject) {}
    };
    public static final ActionBotTask endTask = new ActionBotTask("End") {
        @Override
        public void save(JsonObject jsonObject) {}

        @Override
        public void load(JsonObject jsonObject) {}
    };

    public static ArrayList<ActionBotTask> tasks = new ArrayList<>();

    private static final ArrayList<ActionBotTaskData> actionBotTasks = new ArrayList<>();

    public static void addFullActionBotTask(ActionBotTaskData actionBotTaskData) {
        actionBotTasks.add(actionBotTaskData);
    }

    public static List<ActionBotTaskData> getFullActionBotTasks() {
        return actionBotTasks;
    }


    public static void addActionTaskToList(ActionBotTask task) {
        tasks.remove(endTask);
        tasks.add(task);
        tasks.add(endTask);
    }

    public static void removeActionTaskFromList() {
        tasks.remove(endTask);
        tasks.remove(tasks.size() - 1);
        tasks.add(endTask);
    }

    public static void loadActionBotTasksData() {
        //---------Move Task---------//
        addFullActionBotTask(new ActionBotTaskData() {
            @Override
            public ActionBotTask getTask() {
                return new MoveTask(0,0, false, MoveTask.Type.Default);
            }

            @Override
            public BThackScreen getTaskScreen(TaskButton instance, boolean edit) {
                return new AbstractTaskGui(instance, edit) {
                    @Override
                    public List<TaskSettingButton> getSettingButtons() {
                        return Arrays.asList(
                                new TaskSettingButton(new NumberFrameButton(1, 60, scaledResolution.getScaledHeight() / 2 - 46, 40, 10), "X (Relative to the player)"),
                                new TaskSettingButton(new NumberFrameButton(2, 60, scaledResolution.getScaledHeight() / 2 - 24, 40, 10), "Z (Relative to the player)"),
                                new TaskSettingButton(new SwitchButton(3, 60, scaledResolution.getScaledHeight() / 2 - 2, 40, 10, false, "Scaffold"), ""),
                                new TaskSettingButton(new ModeButton(4, 90, scaledResolution.getScaledHeight() / 2 + 20, 70, 10, "Mode", new ArrayList<>(Arrays.asList("Default", "With AutoJump", "Through Obstacles"))), "")
                        );
                    }

                    @Override
                    public ActionBotTask getAddingTask() {
                        NumberFrameButton xFrame = (NumberFrameButton) getButtonFromId(1);
                        NumberFrameButton zFrame = (NumberFrameButton) getButtonFromId(2);

                        SwitchButton scaffoldButton = (SwitchButton) getButtonFromId(3);
                        ModeButton modeButton = (ModeButton) getButtonFromId(4);

                        MoveTask.Type moveType = modeButton.getsVal().equals("Default") ? MoveTask.Type.Default : modeButton.getsVal().equals("With AutoJump") ? MoveTask.Type.AutoJump : MoveTask.Type.Through_Obstacles;


                        return new MoveTask(xFrame.getNumber(), zFrame.getNumber(), scaffoldButton.getBVal(), moveType);
                    }
                };
            }
        });
        //---------------------------//

        //---------Jump Task---------//
        addFullActionBotTask(new ActionBotTaskData() {
            @Override
            public ActionBotTask getTask() {
                return new JumpTask();
            }

            @Override
            public BThackScreen getTaskScreen(TaskButton instance, boolean edit) {
                if (!edit) ActionBotConfig.addActionTaskToList(new JumpTask());
                return new ActionBotConfigGui();
            }
        });
        //---------------------------//

        //---------Send Message Task---------//
        addFullActionBotTask(new ActionBotTaskData() {
            @Override
            public ActionBotTask getTask() {
                return new SendMessageTask("");
            }

            @Override
            public BThackScreen getTaskScreen(TaskButton instance, boolean edit) {
                return new AbstractTaskGui(instance, edit) {
                    @Override
                    public List<TaskSettingButton> getSettingButtons() {
                        return List.of(
                                new TaskSettingButton(new TextFrameButton(1, (scaledResolution.getScaledWidth() / 2), (int) ((scaledResolution.getScaledHeight() / 2) - (heightFactor * 2.5)), (int) (widthFactor * 8.5), (int) heightFactor), "Message")
                        );
                    }

                    @Override
                    public ActionBotTask getAddingTask() {
                        TextFrameButton frameButton = (TextFrameButton) getButtonFromId(1);

                        return new SendMessageTask(frameButton.getText());
                    }
                };
            }
        });
        //-----------------------------------//

        //---------Tunnel Task---------//
        addFullActionBotTask(new ActionBotTaskData() {
            @Override
            public ActionBotTask getTask() {
                return new TunnelTask(TunnelTask.Direction.X_MINUS, 0);
            }

            @Override
            public BThackScreen getTaskScreen(TaskButton instance, boolean edit) {
                return new AbstractTaskGui(instance, edit) {
                    @Override
                    public List<TaskSettingButton> getSettingButtons() {
                        return Arrays.asList(
                                new TaskSettingButton(new ModeButton(1, 60, scaledResolution.getScaledHeight() / 2 - 2, 40, 10, "Direction",
                                        new ArrayList<>(Arrays.asList(
                                                "X+",
                                                "X-",
                                                "Z+",
                                                "Z-"
                                        ))) ,""),
                                new TaskSettingButton(new NumberFrameButton(2, 60, scaledResolution.getScaledHeight() / 2 + 20, 40, 10), "Length")
                        );
                    }

                    @Override
                    public ActionBotTask getAddingTask() {
                        ModeButton directionButton = (ModeButton) getButtonFromId(1);
                        NumberFrameButton lengthButton = (NumberFrameButton) getButtonFromId(2);

                        TunnelTask.Direction direction = switch (directionButton.getsVal()) {
                            case "X+" -> TunnelTask.Direction.X_PLUS;
                            case "X-" -> TunnelTask.Direction.X_MINUS;
                            case "Z+" -> TunnelTask.Direction.Z_PLUS;
                            default -> TunnelTask.Direction.Z_MINUS;
                        };

                        return new TunnelTask(direction, lengthButton.getNumber());
                    }
                };
            }
        });
        //-----------------------------//

        //---------Wait Task---------//
        addFullActionBotTask(new ActionBotTaskData() {
            @Override
            public ActionBotTask getTask() {
                return new WaitTask(0);
            }

            @Override
            public BThackScreen getTaskScreen(TaskButton instance, boolean edit) {
                return new AbstractTaskGui(instance, edit) {
                    @Override
                    public List<TaskSettingButton> getSettingButtons() {
                        return List.of(
                                new TaskSettingButton(new NumberFrameButton(1, (scaledResolution.getScaledWidth() / 2), (int) ((scaledResolution.getScaledHeight() / 2) - (heightFactor * 2.5)), (int) (widthFactor * 8.5), (int) heightFactor), "Seconds")
                        );
                    }

                    @Override
                    public ActionBotTask getAddingTask() {
                        NumberFrameButton frameButton = (NumberFrameButton) getButtonFromId(1);
                        return new WaitTask(frameButton.getNumber());
                    }
                };
            }
        });
        //---------------------------//

        //---------Break Task---------//
        addFullActionBotTask(new ActionBotTaskData() {
            @Override
            public ActionBotTask getTask() {
                return new BreakTask(0, 0, 0);
            }

            @Override
            public BThackScreen getTaskScreen(TaskButton instance, boolean edit) {
                return new AbstractTaskGui(instance, edit) {
                    @Override
                    public List<TaskSettingButton> getSettingButtons() {
                        return Arrays.asList(
                                new TaskSettingButton(new NumberFrameButton(1, 60, scaledResolution.getScaledHeight() / 2 - 46, 40, 10), "X (Relative to the player)"),
                                new TaskSettingButton(new NumberFrameButton(2, 60, scaledResolution.getScaledHeight() / 2 - 24, 40, 10), "Y (Relative to the player)"),
                                new TaskSettingButton(new NumberFrameButton(3, 60, scaledResolution.getScaledHeight() / 2 - 2, 40, 10), "Z (Relative to the player)")
                        );
                    }

                    @Override
                    public ActionBotTask getAddingTask() {
                        NumberFrameButton xFrame = (NumberFrameButton) getButtonFromId(1);
                        NumberFrameButton yFrame = (NumberFrameButton) getButtonFromId(2);
                        NumberFrameButton zFrame = (NumberFrameButton) getButtonFromId(3);
                        return new BreakTask((int) xFrame.getNumber(), (int) yFrame.getNumber(), (int) zFrame.getNumber());
                    }
                };
            }
        });
        //----------------------------//

        //---------Place Task---------//
        addFullActionBotTask(new ActionBotTaskData() {
            @Override
            public ActionBotTask getTask() {
                return new PlaceTask(0,0,0);
            }

            @Override
            public BThackScreen getTaskScreen(TaskButton instance, boolean edit) {
                return new AbstractTaskGui(instance, edit) {
                    @Override
                    public List<TaskSettingButton> getSettingButtons() {
                        return Arrays.asList(
                                new TaskSettingButton(new NumberFrameButton(1, 60, scaledResolution.getScaledHeight() / 2 - 46, 40, 10), "X (Relative to the player)"),
                                new TaskSettingButton(new NumberFrameButton(2, 60, scaledResolution.getScaledHeight() / 2 - 24, 40, 10), "Y (Relative to the player)"),
                                new TaskSettingButton(new NumberFrameButton(3, 60, scaledResolution.getScaledHeight() / 2 - 2, 40, 10), "Z (Relative to the player)")
                        );
                    }

                    @Override
                    public ActionBotTask getAddingTask() {
                        NumberFrameButton xFrame = (NumberFrameButton) getButtonFromId(1);
                        NumberFrameButton yFrame = (NumberFrameButton) getButtonFromId(2);
                        NumberFrameButton zFrame = (NumberFrameButton) getButtonFromId(3);
                        return new PlaceTask((int) xFrame.getNumber(), (int) yFrame.getNumber(), (int) zFrame.getNumber());
                    }
                };
            }
        });
        //----------------------------//

        //---------EnableModule Task---------//
        addFullActionBotTask(new ActionBotTaskData() {
            @Override
            public ActionBotTask getTask() {
                return new EnableModuleTask("", false);
            }

            @Override
            public BThackScreen getTaskScreen(TaskButton instance, boolean edit) {
                return new AbstractTaskGui(instance, edit) {
                    @Override
                    public List<TaskSettingButton> getSettingButtons() {
                        return Arrays.asList(
                                new TaskSettingButton(new TextFrameButton(1, 60, scaledResolution.getScaledHeight() / 2 - 12, 40, 10), "Module name"),
                                new TaskSettingButton(new SwitchButton(2, 60, scaledResolution.getScaledHeight() / 2 + 12, 40, 10, false, "Quietly"), "")
                        );
                    }

                    @Override
                    public ActionBotTask getAddingTask() {
                        TextFrameButton textFrame = (TextFrameButton) getButtonFromId(1);
                        SwitchButton switchButton = (SwitchButton) getButtonFromId(2);
                        return new EnableModuleTask(textFrame.getText(), switchButton.getBVal());
                    }
                };
            }
        });
        //-----------------------------------//

        //---------DisableModule Task---------//
        addFullActionBotTask(new ActionBotTaskData() {
            @Override
            public ActionBotTask getTask() {
                return new DisableModuleTask("", false);
            }

            @Override
            public BThackScreen getTaskScreen(TaskButton instance, boolean edit) {
                return new AbstractTaskGui(instance, edit) {
                    @Override
                    public List<TaskSettingButton> getSettingButtons() {
                        return Arrays.asList(
                                new TaskSettingButton(new TextFrameButton(1, 60, scaledResolution.getScaledHeight() / 2 - 12, 40, 10), "Module name"),
                                new TaskSettingButton(new SwitchButton(2, 60, scaledResolution.getScaledHeight() / 2 + 12, 40, 10, false, "Quietly"), "")
                        );
                    }

                    @Override
                    public ActionBotTask getAddingTask() {
                        TextFrameButton textFrame = (TextFrameButton) getButtonFromId(1);
                        SwitchButton switchButton = (SwitchButton) getButtonFromId(2);
                        return new DisableModuleTask(textFrame.getText(), switchButton.getBVal());
                    }
                };
            }
        });
        //------------------------------------//

        //---------CloseScreen Task---------//
        addFullActionBotTask(new ActionBotTaskData() {
            @Override
            public ActionBotTask getTask() {
                return new CloseScreenTask();
            }

            @Override
            public BThackScreen getTaskScreen(TaskButton instance, boolean edit) {
                if (!edit) ActionBotConfig.addActionTaskToList(new CloseScreenTask());
                return new ActionBotConfigGui();
            }
        });
        //----------------------------------//
    }
}
