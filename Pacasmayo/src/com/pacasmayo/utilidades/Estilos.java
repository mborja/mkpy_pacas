/*
 * Estilos.java
 *
 * © <your company here>, 2003-2008
 * Confidential and proprietary.
 */

package com.pacasmayo.utilidades;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.TransitionContext;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.UiEngineInstance;
import net.rim.device.api.ui.container.MainScreen;

public final class Estilos {

	public static int getColorModulo() {
        return 0xFFFFFF;
    }
	
	public static int getBackGround() {
        return Color.WHITE; //0x6973b0;
    }

    public static int getBGModulo() {
        return 0xFFA500; //0x6a288a;
    }

    public static int getBGSubModulo() {
        return 0xFFD700; //0x989db9;
    }

    public static int getBGScreen() {
        return Color.WHITE; //0xe2e2e2;
    }
    public static int getFuente() {
        return Color.BLACK;
    }

    public static int getBGInterlinea(int linea) {
    	if ( linea % 2 == 0 ) {
            return Color.LIGHTGRAY; //Color.GRAY; //0xFFC000; //0xd2bedc;
    	} else {
            return Color.WHITE; //0xFFE000; //0xdbdada;
    	}
    }

    public static int getColorInterlinea(int linea) {
    	if ( linea % 2 == 0 ) {
            return Color.WHITE;
    	} else {
            return Color.BLACK;
    	}
    }

    public static int getBGSelected() {
        return Color.RED;
    }

	public static void pushScreen(MainScreen screen) {
        UiEngineInstance engine = Ui.getUiEngineInstance();

        TransitionContext transitionContextPush = new TransitionContext(TransitionContext.TRANSITION_SLIDE);
        transitionContextPush.setIntAttribute(TransitionContext.ATTR_DURATION, 200);
        transitionContextPush.setIntAttribute(TransitionContext.ATTR_DIRECTION, TransitionContext.DIRECTION_LEFT);

        TransitionContext transitionContextPop = new TransitionContext(TransitionContext.TRANSITION_SLIDE);
        transitionContextPop.setIntAttribute(TransitionContext.ATTR_DURATION, 200);
        transitionContextPop.setIntAttribute(TransitionContext.ATTR_DIRECTION, TransitionContext.DIRECTION_RIGHT);
        transitionContextPop.setIntAttribute(TransitionContext.ATTR_KIND, TransitionContext.KIND_OUT);

        engine.setTransition(null, screen, UiEngineInstance.TRIGGER_PUSH, transitionContextPush);
        engine.setTransition(screen, null, UiEngineInstance.TRIGGER_POP, transitionContextPop);

        UiApplication.getUiApplication().pushScreen(screen);	
	}
} 


