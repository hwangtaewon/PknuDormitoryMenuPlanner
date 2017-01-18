package kr.ac.pknu.dormitory;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ViewClass extends Application{

	int rowIndex = 0, columnIndex = 0;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {		
		MenuParsingClass menuParsingClass = new MenuParsingClass();
		
		primaryStage.setTitle("부경대학교 생활관 식단표");
		StackPane root = new StackPane();
		GridPane grid = new GridPane();
		
		Label[][] labelArray = new Label[4][8];
		String content = null;
		
		
		
		//Class<?> labelClass = Class.forName("javafx.scene.control.Label"); //reflection

		for(int i = 0; i < 4; i++)
		{
			for(int j = 0; j < 8; j++)
			{
				content = menuParsingClass.menuPlanner[i][j];
				//labelArray[i][j] = (Label) labelClass.newInstance(); //reflection
				labelArray[i][j] = new Label();
				labelArray[i][j].setText(content);
				GridPane.setConstraints(labelArray[i][j], j, i); //이 과정이 있어야 셀에 접근이 가능하다?
				if(i == 0) {
					GridPane.setHalignment(labelArray[i][j], HPos.CENTER );
					dateDisplay(i, j, labelArray);
				}
				if(j == 0) GridPane.setHalignment(labelArray[i][j], HPos.CENTER );
				
				grid.getChildren().add(labelArray[i][j]);
			}
		}
		//for안에서 음..아직 등록?이 안 된 셀을 이용하려고 하면 null exception 에러가 뜬다.
		//for문을 벗어나면 모든 셀의 등록이 완료된 상태이므로 그리드의 모든 셀에 대한 접근이 자유롭다.
		timeDisplay(labelArray);
		root.getChildren().add(grid);
		grid.setHgap(10);
		grid.setVgap(10);
		
		primaryStage.setScene(new Scene(root, 900, 400));
        primaryStage.show();
	}
	
	
	//If today is equal to day of week, that day label border appear red color.
	public void dateDisplay(int i, int j, Label[][] labelArray)
	{
		GetNow getDate = new GetNow();
		String date = getDate.date();
		String labelDay = labelArray[i][j].getText();
		Pattern p = Pattern.compile("[^가-힣^\\(^\\)]");
		Matcher m = p.matcher(labelDay);
		String compareString = "";
		while(m.find())
		{
			compareString += m.group();
		}
		if(compareString.equals(date))
		{
			System.out.println("i : " + i + "j : " + j);
			labelArray[i][j].setStyle("-fx-border-color: red;");
			rowIndex = i;
			columnIndex = j;
		}
	}
	
	/**
	 *		if I find date matched today, I will call timeDisplay method in dateDisplay.
	 */
	@SuppressWarnings("deprecation")
	public void timeDisplay(Label[][] labelArray)
	{
		GetNow getTime = new GetNow();
		Date nowTime = getTime.time();
		Date morning = new Date(); //am09:00
		Date dinner = new Date(); //pm13:40
		morning.setHours(9);
		morning.setMinutes(0);
		dinner.setHours(13);
		dinner.setMinutes(40);
		
		if(nowTime.before(morning)) {
			System.out.println("now is morning");
			labelArray[1][columnIndex].setStyle("-fx-border-color: red;");
		}else if(nowTime.after(dinner)) {
			System.out.println("now is dinner");
//			System.out.println(labelArray[i][j].toString());
			labelArray[3][columnIndex].setStyle("-fx-border-color: red;");
		}else {
			System.out.println("now is lunch");
			labelArray[2][columnIndex].setStyle("-fx-border-color: red;");
		}
	}
}
