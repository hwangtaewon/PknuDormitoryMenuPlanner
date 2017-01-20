package kr.ac.pknu.dormitory;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ViewClass extends Application {

	int rowIndex = 0, columnIndex = 0;
	boolean toggle = true;
	private static double xOffset = 0;
	private static double yOffset = 0;
	// public static void main(String[] args) {
	// launch(args);
	// }

	@Override
	public void start(Stage primaryStage) throws Exception {
		MenuParsingClass menuParsingClass = new MenuParsingClass();
		VBox verticalBox = new VBox();
		StackPane root = new StackPane();
		GridPane grid = new GridPane();
		Label[][] labelArray = new Label[4][8];
		String content = null;
		
		

		// Class<?> labelClass = Class.forName("javafx.scene.control.Label");
		// //reflection
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 8; j++) {
				content = menuParsingClass.menuPlanner[i][j];
				// labelArray[i][j] = (Label) labelClass.newInstance();
				// //reflection
				labelArray[i][j] = new Label();
				labelArray[i][j].setText(content);
				GridPane.setConstraints(labelArray[i][j], j, i); // 이 과정이 있어야 셀에
																	// 접근이 가능하다?
				if (i == 0) {
					GridPane.setHalignment(labelArray[i][j], HPos.CENTER);
					dateDisplay(i, j, labelArray);
				}
				if (j == 0)
					GridPane.setHalignment(labelArray[i][j], HPos.CENTER);

				grid.getChildren().add(labelArray[i][j]);
			}
		}

		/**
		 *  Make the buttons.
		 */
		
//		Button windowToggleButton = new Button("크기조절");
//		
//		windowToggleButton.setOnAction(e -> {
//			if(toggle){
//				windowToggleButton.setText("크기조절");
//				primaryStage.hide();
//				primaryStage.initStyle(StageStyle.DECORATED);
//				primaryStage.show();
//				toggle = !toggle;
//			}else {
//				windowToggleButton.setText("크기고정");
//				primaryStage.hide();
//				primaryStage.initStyle(StageStyle.TRANSPARENT);
//				primaryStage.show();
//				toggle = !toggle;
//			}
//		});
		
		// for안에서 음..아직 등록?이 안 된 셀을 이용하려고 하면 null exception 에러가 뜬다.
		// for문을 벗어나면 모든 셀의 등록이 완료된 상태이므로 그리드의 모든 셀에 대한 접근이 자유롭다.
		timeDisplay(labelArray);
		//verticalBox.getChildren().add(windowToggleButton);
		verticalBox.getChildren().add(grid);
		root.getChildren().add(verticalBox);
		
		// root.setAlignment(grid, Pos.CENTER); //매개변수로 node가 들어가는 setAlignment는
		// static메소드다. root를 써주면 경고뜸 왜?
		// StackPane.setAlignment(grid, Pos.CENTER_RIGHT);
		grid.setAlignment(Pos.CENTER); // StackPane위에 올라가있는 grid를 가운데로 정렬한다.
		grid.setHgap(10);
		grid.setVgap(10);
		// VBox의 마진값을 준다. Insets객체에 대해 알아보자.
		// VBox.setMargin(root, new Insets(10, 10, 10, 10));
		VBox.setMargin(grid, new Insets(30, 10, 15, 10));
		
		// primaryStage.setScene(new Scene(root, 900, 400));
		Scene rootScene = new Scene(root, 900, 400);
		rootScene.setFill(Color.TRANSPARENT);
		// grid.setStyle("-fx-border-color: burlywood");
		root.setStyle("-fx-background-color: rgba(252, 228, 236, 0.7);");
		// primaryStage.setTitle("부경대학교 생활관 식단표");
		// primaryStage.initStyle(StageStyle.UNDECORATED);
		
		rootScene.setOnMousePressed(e -> {
			xOffset = primaryStage.getX() - e.getScreenX();
			yOffset = primaryStage.getY() - e.getScreenY();
			System.out.println("xOffset : " + xOffset + "stageX : " + primaryStage.getX() + "   Screen X : " + e.getScreenX());
			System.out.println("yOffset : " + yOffset + "stageY : " + primaryStage.getY() + "   Screen Y : " + e.getScreenY());
		});		
		
		rootScene.setOnMouseDragged(e -> {
			primaryStage.setX(e.getScreenX() + xOffset);
			primaryStage.setY(e.getScreenY() + yOffset);
			System.out.println("ScreenX : " + e.getSceneX() + "  ScreenY : " + e.getScreenY());
		});
		
		
		
		
		
		//DragResizer dragResizer = new DragResizer((Region)rootScene);
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.setScene(rootScene);
		primaryStage.show();
		
		//프로그램의 배경을 투명하게 만드려면 Stage, Scene, Pane을 투명하게 만들어줘야 한다.
		//이 때 Stage와 Scene은 setStyle()을 지원하지 않고 Stage는 initStyle(), Scene은 setFill()을 통하여 투명하게 만들 수 있다.
	}

	// If today is equal to day of week, that day label border appear red color.
	public void dateDisplay(int i, int j, Label[][] labelArray) {
		GetNow getDate = new GetNow();
		String date = getDate.date();
		String labelDay = labelArray[i][j].getText();
		Pattern p = Pattern.compile("[^가-힣^\\(^\\)]"); //Regular Expression 월(1/17) -> 1/17
		Matcher m = p.matcher(labelDay);
		String compareString = "";
		while (m.find()) {
			compareString += m.group();
		}
		if (compareString.equals(date)) {
			System.out.println("i : " + i + "j : " + j);
			labelArray[i][j].setStyle("-fx-border-color: red;");
			rowIndex = i;
			columnIndex = j;
		}
	}

	/**
	 * if I find date matched today, I will call timeDisplay method in
	 * dateDisplay.
	 */
	@SuppressWarnings("deprecation")
	public void timeDisplay(Label[][] labelArray) {
		GetNow getTime = new GetNow();
		Date nowTime = getTime.time();
		Date morning = new Date(); // am09:00
		Date dinner = new Date(); // pm13:40
		morning.setHours(9);
		morning.setMinutes(0);
		dinner.setHours(13);
		dinner.setMinutes(40);

		if (nowTime.before(morning)) {
			System.out.println("now is morning");
			labelArray[1][columnIndex].setStyle("-fx-border-color: red;");
		} else if (nowTime.after(dinner)) {
			System.out.println("now is dinner");
			// System.out.println(labelArray[i][j].toString());
			labelArray[3][columnIndex].setStyle("-fx-border-color: red;");
		} else {
			System.out.println("now is lunch");
			labelArray[2][columnIndex].setStyle("-fx-border-color: red;");
		}
	}
}
