package com.makipuray.ui;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.GaugeField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.DialogFieldManager;
import net.rim.device.api.ui.container.PopupScreen;

public class mkpyStatusProgress {
	private PopupScreen popup;
	private GaugeField gaugeField;
	private LabelField label;
	private String title;
	private int minValue, maxValue;
	private int progress;
  
	public mkpyStatusProgress(String title, int min, int max, long style) {
		this.title = title;
		maxValue = max;
		minValue = min;
		progress = 0;
		DialogFieldManager manager = new DialogFieldManager();
		popup = new PopupScreen(manager);
		label = new LabelField(title);
		manager.addCustomField(label);
		gaugeField = new GaugeField(null, minValue, maxValue, progress, style);
		manager.addCustomField(gaugeField);
	}
	  
	public mkpyStatusProgress(String title) {
		this.title = title;
		maxValue = 0;
		minValue = 0;
		progress = 0;
		DialogFieldManager manager = new DialogFieldManager();
		popup = new PopupScreen(manager);
		label = new LabelField(title);
		manager.addCustomField(label);
	}

	public void open() {
		if (!popup.isDisplayed()) {
			UiApplication.getUiApplication().pushScreen(popup);
		}
		popup.doPaint();
	}
	  
	public void close() {
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
				if (popup.isDisplayed()) {
					UiApplication.getUiApplication().popScreen(popup);
				}
			}
		});
	}

	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
		label.setText(this.title);
		popup.doPaint();
	}
	
	public int getProgress() {
		return progress;
	}
	
	public void setProgress(int progress) {
		this.progress = progress;
		gaugeField.setValue(this.progress);
		popup.doPaint();
	}
	  
}
