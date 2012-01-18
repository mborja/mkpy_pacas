package com.makipuray.ui;

import java.util.Timer;
import java.util.TimerTask;

import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.decor.BackgroundFactory;

public class mkpyLabelField extends LabelField {
	private int backColor, foreColor;
	private Timer _scrollTimer;
	private String ORIGINALTEXT = "";
	private final static long ESPERA = 3000;
	private final static long DORMIR = 200;
	
	public mkpyLabelField(String text, long style, int forecolor, int backcolor) {
		super(text, style);
		this.backColor = backcolor;
		this.foreColor = forecolor;
		super.setBackground(BackgroundFactory.createSolidBackground(backColor));
	}

	public mkpyLabelField(String text, long style, int forecolor, int backcolor, boolean scroll) {
		super(text, style);
		ORIGINALTEXT = text;
		this.backColor = backcolor;
		this.foreColor = forecolor;
		super.setBackground(BackgroundFactory.createSolidBackground(backColor));
		if ( scroll ) {
			startScroll();
		}
	}

	public void paint(Graphics g) {
        g.setColor(this.foreColor);
        super.paint(g);		
	}
	
    private void startScroll() {
        if ( _scrollTimer == null ) {
            _scrollTimer = new Timer();
            
            TimerTask _scrollTimerTask = new TimerTask() {
            	private String currentText = null;
            	private int curChar = 0, len = ORIGINALTEXT.length();
            	
                public void run() {
                	for ( curChar = 0; curChar < len; curChar+=2 ) {
                        currentText = ORIGINALTEXT.substring(curChar);
                        try {
                            setText(currentText);
                        } catch(Exception e) {
                        }
    	          		try {
    						Thread.sleep(DORMIR);
    					} catch (InterruptedException e) {
    					}
                	}
                    setText(ORIGINALTEXT);
                }
            };
            _scrollTimer.scheduleAtFixedRate(_scrollTimerTask, ESPERA, ESPERA + (DORMIR * ORIGINALTEXT.length()));
   
        }
    }

}
