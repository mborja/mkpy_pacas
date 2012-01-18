package com.makipuray.ui;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;

public class mkpyImageEditField extends HorizontalFieldManager {
	private mkpyEditField mkpyEdit;
	
	public mkpyImageEditField(Bitmap img, String value, int size, long style, int color, int backcolor) {
		super(HorizontalFieldManager.NO_HORIZONTAL_SCROLL 
                | HorizontalFieldManager.NO_VERTICAL_SCROLL | HorizontalFieldManager.USE_ALL_WIDTH);
		mkpyEdit = new mkpyEditField(null, value, size, style, backcolor);
		super.setBackground(BackgroundFactory.createSolidBackground(backcolor));
		this.add(new BitmapField(img, BitmapField.FIELD_LEFT | BitmapField.FIELD_VCENTER));
		this.add(mkpyEdit);
	}
	
	public String getText() {
		return mkpyEdit.getText();
	}
}
