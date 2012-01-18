package com.makipuray.ui;

import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.decor.BackgroundFactory;

public class HyperlinkField extends LabelField {
//    private MenuItem mGetLinkMenuItem;
            
    public HyperlinkField(String label) {
        super(label, FOCUSABLE | USE_ALL_WIDTH);
        Font font = this.getFont().derive(Font.UNDERLINED);
        setFont(font);

//        mGetLinkMenuItem = new MenuItem("seguir vinculo", 100, 8) {
//            public void run() {
//                if( label.equals(verVenta)){
//                	consultaVentas();
//                }
//                else if (label.equals(verVentaxMarca)){
//                	consultaVentasxMarca();
//                }
//                else{ //"Ver detalle puntaje "
//                	consultaPuntaje();
//                }
//  
//            }
//          };
    }
    
    public void setBackColor(int backColor) {
		super.setBackground(BackgroundFactory.createSolidBackground(backColor));
    }
//
//    public HyperlinkField(String label) {
//        this(label, 0);
//    }

//    protected void makeMenu(Menu menu, int instance) {
//        menu.add(mGetLinkMenuItem);
//        menu.add(MenuItem.separator(0));
//    }
}
