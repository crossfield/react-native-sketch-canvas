package com.terrylinla.rnsketchcanvas;

import android.graphics.PointF;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class SketchCanvasManager extends SimpleViewManager<SketchCanvas> {
    public static final int COMMAND_ADD_POINT = 1;
    public static final int COMMAND_NEW_PATH = 2;
    public static final int COMMAND_CLEAR = 3;
    public static final int COMMAND_ADD_PATH = 4;
    public static final int COMMAND_DELETE_PATH = 5;
    public static final int COMMAND_SAVE = 6;
    public static final int COMMAND_END_PATH = 7;

    public static SketchCanvas Canvas = null;

    private static final String PROPS_IMAGE_PATH = "localSourceImagePath";

    @Override
    public String getName() {
        return "RNSketchCanvas";
    }

    @Override
    protected SketchCanvas createViewInstance(ThemedReactContext context) {
        SketchCanvasManager.Canvas = new SketchCanvas(context);
        return SketchCanvasManager.Canvas;
    }

    @ReactProp(name = PROPS_IMAGE_PATH)
    public void setLocalSourceImagePath(SketchCanvas viewContainer, String localSourceImagePath) {
        viewContainer.openImageFile(localSourceImagePath);
    }

    @Override
    public Map<String,Integer> getCommandsMap() {
        Map<String, Integer> map = new HashMap<>();

        map.put("addPoint", COMMAND_ADD_POINT);
        map.put("newPath", COMMAND_NEW_PATH);
        map.put("clear", COMMAND_CLEAR);
        map.put("addPath", COMMAND_ADD_PATH);
        map.put("deletePath", COMMAND_DELETE_PATH);
        map.put("save", COMMAND_SAVE);
        map.put("endPath", COMMAND_END_PATH);

        return map;
    }

    @Override
    protected void addEventEmitters(ThemedReactContext reactContext, SketchCanvas view) {

    }

    @Override
    public void receiveCommand(SketchCanvas view, int commandType, @Nullable ReadableArray args) {
        switch (commandType) {
            case COMMAND_ADD_POINT: {
                view.addPoint(args.getInt(0), (float)args.getDouble(1), (float)args.getDouble(2));
                return;
            }
            case COMMAND_NEW_PATH: {
                view.newPath(args.getInt(0), args.getInt(1), (float)args.getDouble(2));
                return;
            }
            case COMMAND_CLEAR: {
                view.clear();
                return;
            }
            case COMMAND_ADD_PATH: {
                ReadableArray path = args.getArray(3);
                ArrayList<PointF> pointPath = new ArrayList<PointF>(path.size());
                for (int i=0; i<path.size(); i++) {
                    String[] coor = path.getString(i).split(",");
                    pointPath.add(new PointF(Float.parseFloat(coor[0]), Float.parseFloat(coor[1])));
                }
                view.addPath(args.getInt(0), args.getInt(1), (float)args.getDouble(2), pointPath);
                return;
            }
            case COMMAND_DELETE_PATH: {
                view.deletePath(args.getInt(0));
                return;
            }
            case COMMAND_SAVE: {
                view.save(args.getString(0), args.getString(1), args.getString(2), args.getBoolean(3));
                return;
            }
            case COMMAND_END_PATH: {
                view.end();
                return;
            }
            default:
                throw new IllegalArgumentException(String.format(
                        "Unsupported command %d received by %s.",
                        commandType,
                        getClass().getSimpleName()));
        }
    }
}
