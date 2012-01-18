package com.makipuray.ui;

import com.pacasmayo.utilidades.Estilos;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FocusChangeListener;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.PasswordEditField;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;

public class mkpyPasswordEditField extends PasswordEditField implements FocusChangeListener {
	private static final int PAD = 2;

	public mkpyPasswordEditField(String label, String initialValue, int maxNumChars, long style) {
		super(label, initialValue, maxNumChars, style);
		super.setFocusListener(this);
		super.setMargin(new XYEdges(PAD, PAD, PAD, PAD));
		super.setBorder(BorderFactory.createRoundedBorder(new XYEdges(PAD, PAD, PAD, PAD), Color.GRAY, Border.STYLE_DOTTED));
	}
	
	public void focusChanged(Field field, int eventType) {
		if ( eventType == FOCUS_LOST ) {
			field.setBorder(BorderFactory.createRoundedBorder(new XYEdges(PAD, PAD, PAD, PAD), Color.GRAY, Border.STYLE_DOTTED));
		} else if(eventType == FOCUS_GAINED ) {
			field.setBorder(BorderFactory.createRoundedBorder(new XYEdges(PAD, PAD, PAD, PAD), Estilos.getBGModulo(), Border.STYLE_SOLID));
		}
	}
}
