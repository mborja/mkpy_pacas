package com.makipuray.ui;

import java.util.Timer;
import java.util.TimerTask;

import com.pacasmayo.utilidades.Estilos;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.DrawTextParam;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;
import net.rim.device.api.ui.FocusChangeListener;

public class mkpyEditField extends EditField implements FocusChangeListener {
	private int backColor;
	private static final int PAD = 2;
	
	public mkpyEditField(String label, String defaultString, int len, long style) {
        super(label, defaultString, len, style);
        this.backColor = -1; 
        super.setFocusListener(this);
		super.setMargin(new XYEdges(PAD, PAD, PAD, PAD));
		super.setBorder(BorderFactory.createRoundedBorder(new XYEdges(PAD, PAD, PAD, PAD), Color.GRAY, Border.STYLE_DOTTED));
    }
	
	public mkpyEditField(String label, String defaultString, int len, long style, int backColor) {
        super(label, defaultString, len, style);
        this.backColor = backColor; 
		//super.setBorder(Field.VISUAL_STATE_NORMAL, BorderFactory.createRoundedBorder(new XYEdges(1, 1, 1, 1), Color.GRAY, Border.STYLE_DOTTED));
		//super.setBorder(BorderFactory.createRoundedBorder(new XYEdges(1, 1, 1, 1), Color.GRAY, Border.STYLE_DOTTED));
		super.setBackground(BackgroundFactory.createSolidBackground(backColor));
        super.setFocusListener(this);
		super.setMargin(new XYEdges(PAD, PAD, PAD, PAD));
		super.setBorder(BorderFactory.createRoundedBorder(new XYEdges(PAD, PAD, PAD, PAD), Color.GRAY, Border.STYLE_DOTTED));
    }
    
    public void focusChanged(Field field, int eventType) {
		if ( eventType == FOCUS_LOST ) {
			if ( backColor == -1 ) {
				field.setBackground(null);
			} else {
				field.setBackground(BackgroundFactory.createSolidBackground(backColor));
			}
			field.setBorder(BorderFactory.createRoundedBorder(new XYEdges(PAD, PAD, PAD, PAD), Color.GRAY, Border.STYLE_DOTTED));
		} else if(eventType == FOCUS_GAINED ) {
			field.setBackground(null);
			field.setBorder(BorderFactory.createRoundedBorder(new XYEdges(PAD, PAD, PAD, PAD), Estilos.getBGModulo(), Border.STYLE_SOLID));
		}
	}    
}
