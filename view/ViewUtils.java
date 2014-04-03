package view;

import javafx.scene.control.Button;
import javafx.scene.layout.Region;

public class ViewUtils {
	public static Region getVPlaceHolder(int height) {
		Region heightHolder = new Region();
		heightHolder.setPrefHeight(height);
		return heightHolder;
	}

	public static Region getHPlaceHolder(int width) {
		Region widthHolder = new Region();
		widthHolder.setPrefWidth(width);
		return widthHolder;
	}
	
	static public void validateDifficulty(Button button, String str) {
		try {
			int i = Integer.parseInt(str);
			button.setDisable(!(0 <= i && i <= 10));
		} catch (NumberFormatException ex) {
			button.setDisable(true);
		}
	}
	
	static public void validateNat(Button button, String str) {
		try {
			int i = Integer.parseInt(str);
			button.setDisable(!(0 <= i));
		} catch (NumberFormatException ex) {
			button.setDisable(true);
		}
	}
}
