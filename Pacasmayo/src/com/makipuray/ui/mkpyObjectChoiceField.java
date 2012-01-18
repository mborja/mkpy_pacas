package com.makipuray.ui;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.decor.BackgroundFactory;

public class mkpyObjectChoiceField extends ObjectChoiceField implements FieldChangeListener {
	public mkpyObjectChoiceField(String label, Object[] choices,
			int initialIndex, long style, int backColor) {
		super(label, choices, initialIndex, style);
		super.setBackground(BackgroundFactory.createSolidBackground(backColor));		
		//super.setChangeListener(this);
	}

	public void fieldChanged(Field field, int context) {
		super.fieldChangeNotify(context);
	}

}
