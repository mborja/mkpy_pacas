package com.makipuray.ui;

import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;

public class mkpyLabelEditField extends HorizontalFieldManager {
	private mkpyLabelField mkpyLabel;
	private mkpyEditField mkpyEdit;
	
	public mkpyLabelEditField(String label, String value, int size, long style, int color, int backcolor) {
		super(HorizontalFieldManager.NO_HORIZONTAL_SCROLL 
                | HorizontalFieldManager.NO_VERTICAL_SCROLL | HorizontalFieldManager.USE_ALL_WIDTH);
		
		mkpyLabel = new mkpyLabelField(label, style  | BitmapField.FIELD_VCENTER, color, backcolor);
		mkpyEdit = new mkpyEditField(null, value, size, style | BitmapField.FIELD_VCENTER, backcolor);
		super.setBackground(BackgroundFactory.createSolidBackground(backcolor));
        mkpyLabel.setFont(this.getFont().derive(Font.BOLD));
		this.add(mkpyLabel);
		this.add(mkpyEdit);
	}
	
	public mkpyEditField getText() {
		return mkpyEdit;
	}

	public mkpyLabelField getLabel() {
		return mkpyLabel;
	}
	
	public void setEditable(boolean isEditable){
		mkpyEdit.setEditable(isEditable());
	}
	
}
