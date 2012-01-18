package com.makipuray.ui;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;

public class mkpyImageLabelField extends HorizontalFieldManager {	
	public mkpyImageLabelField(Bitmap img, String value, long style, int color, int backcolor) {
		super(HorizontalFieldManager.NO_HORIZONTAL_SCROLL 
                | HorizontalFieldManager.NO_VERTICAL_SCROLL | HorizontalFieldManager.USE_ALL_WIDTH);
		super.setBackground(BackgroundFactory.createSolidBackground(backcolor));
		this.add(new BitmapField(img, BitmapField.FIELD_LEFT));
		this.add(new mkpyLabelField(value, style, color, backcolor));
	}
	
	public mkpyImageLabelField(Bitmap img, String value, long style, int color, int backcolor, boolean scroll) {
		super(HorizontalFieldManager.NO_HORIZONTAL_SCROLL 
                | HorizontalFieldManager.NO_VERTICAL_SCROLL | HorizontalFieldManager.USE_ALL_WIDTH);
		super.setBackground(BackgroundFactory.createSolidBackground(backcolor));
		this.add(new BitmapField(img, BitmapField.FIELD_LEFT));
		this.add(new mkpyLabelField(value, style, color, backcolor, scroll));
	}
}
