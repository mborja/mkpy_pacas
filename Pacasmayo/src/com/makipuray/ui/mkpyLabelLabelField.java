package com.makipuray.ui;

import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;

public class mkpyLabelLabelField extends HorizontalFieldManager {
	private mkpyLabelField lblTitle, lblValue; 
	
	public mkpyLabelLabelField(String title, String value, long style, int color, int backcolor) {
		super(HorizontalFieldManager.NO_HORIZONTAL_SCROLL 
                | HorizontalFieldManager.NO_VERTICAL_SCROLL | HorizontalFieldManager.USE_ALL_WIDTH);

		lblTitle = new mkpyLabelField(title, style | BitmapField.FIELD_VCENTER, color, backcolor);
		lblTitle.setFont(this.getFont().derive(Font.BOLD));
        this.setBackground(BackgroundFactory.createSolidBackground(backcolor));
		this.add(lblTitle);
		lblValue = new mkpyLabelField(value, style | LabelField.FIELD_VCENTER | LabelField.ELLIPSIS, color, backcolor);
		this.add(lblValue);
	}

	public mkpyLabelLabelField(String title, String value, long style, int color, int backcolor, boolean scroll) {
		super(HorizontalFieldManager.NO_HORIZONTAL_SCROLL 
                | HorizontalFieldManager.NO_VERTICAL_SCROLL | HorizontalFieldManager.USE_ALL_WIDTH);

		lblTitle = new mkpyLabelField(title, style | BitmapField.FIELD_VCENTER, color, backcolor);
		lblTitle.setFont(this.getFont().derive(Font.BOLD));
        this.setBackground(BackgroundFactory.createSolidBackground(backcolor));
		this.add(lblTitle);
		lblValue = new mkpyLabelField(value, style | LabelField.FIELD_VCENTER | LabelField.ELLIPSIS, color, backcolor, scroll);
		this.add(lblValue);
	}

	public String getTitle() {
		return lblTitle.getText();
	}

	public void setTitle(String value) {
		lblTitle.setText(value);
	}

	public String getValue() {
		return lblValue.getText();
	}

	public void setValue(String value) {
		lblValue.setText(value);
	}
}
