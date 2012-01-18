package com.makipuray.ui;

import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;

public class mkpyLabelChoiceField extends HorizontalFieldManager {
	private mkpyLabelField mkpyLabel;
	private ObjectChoiceField opciones;
	
	public mkpyLabelChoiceField(String label, String[] value, int size, long style, int color, int backcolor) {
		super(HorizontalFieldManager.NO_HORIZONTAL_SCROLL 
                | HorizontalFieldManager.NO_VERTICAL_SCROLL | HorizontalFieldManager.USE_ALL_WIDTH);
		mkpyLabel = new mkpyLabelField(label, style | BitmapField.FIELD_VCENTER, color, backcolor);
		opciones = new ObjectChoiceField("", value, 0, ObjectChoiceField.FIELD_VCENTER | ObjectChoiceField.USE_ALL_WIDTH | ObjectChoiceField.FORCE_SINGLE_LINE | style);
		super.setBackground(BackgroundFactory.createSolidBackground(backcolor));
        mkpyLabel.setFont(this.getFont().derive(Font.BOLD));
        opciones.setFont(this.getFont().derive(this.getFont().getStyle(), 13));
		this.add(mkpyLabel);
		this.add(opciones);
	}
	
	public int getSelectedIndex() {
		return opciones.getSelectedIndex();
	}

	public void setSelectedIndex(int index) {
		opciones.setSelectedIndex(index);
	}
	
	public ObjectChoiceField getOpciones(){
		return opciones;
	}
	
	public void setOpciones(ObjectChoiceField opciones){
		this.opciones = opciones;
	}
	
}
