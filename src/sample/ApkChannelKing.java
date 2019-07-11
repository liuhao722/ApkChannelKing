package sample;

import java.io.*;
import java.text.BreakIterator;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class ApkChannelKing extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(final Stage primaryStage) {
        Group root = new Group();

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        Text scenetitle = new Text("️打包神器");
        scenetitle.setFont(Font.font("", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label apkPathL = new Label("apk源文件:");
        grid.add(apkPathL, 0, 1);

        TextField apkPathT = new TextField();
        apkPathT.setPromptText("点击右侧按钮,选择apk安装包");
        apkPathT.setEditable(false);
        grid.add(apkPathT, 1, 1);
        Button upButton = new Button("浏览..");
        HBox hbBtn0 = new HBox(1);
        hbBtn0.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn0.getChildren().add(upButton);
        grid.add(upButton, 2, 1);
        upButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent arg0) {
                FileChooser fileChooser = new FileChooser();
                // 限制文件类型
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("APK files (*.apk)", "*.apk");
                fileChooser.getExtensionFilters().add(extFilter);
                File file = fileChooser.showOpenDialog(primaryStage);
                if(file!=null){
                    apkPathT.setText(file.getPath());
                }

            }
        });

        Label channelNameL = new Label("渠道名称:");
        grid.add(channelNameL, 0, 2);
        TextArea channelName = new TextArea();
        channelName.setPromptText("输入渠道名,按行分割");
        channelName.setPrefHeight(200);
        channelName.setPrefWidth(390);
        grid.add(channelName, 1, 2);
        Button btn = new Button("生成渠道包");
        HBox hbBtn = new HBox(1);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 3);
        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);
        ImageView imv = new ImageView();
        Image image = new Image(ApkChannelKing.class.getResourceAsStream("niubi.png"));
        grid.add(imv,1,7);

        btn.setOnAction(new EventHandler<ActionEvent>(){
            @Override

            public void handle(ActionEvent arg0) {
                //String apkPath = apkPathT.getText();
                String channelList = channelName.getText();
                if(channelList.isEmpty()){
                    actiontarget.setFill(Color.RED);
                    actiontarget.setText("渠道都没有,打什么包?");
                    return;
                }
                //System.out.println(channelList);
                //System.out.println(apkPath);
                Runtime rt = Runtime.getRuntime();
                try {
                    File directory = new File(".");
                    addChannel(directory.getCanonicalPath(), channelList);
                    String cmd= "python "+directory.getCanonicalPath()+"/牛逼打包神器/AutomaticPackaging/run.py";
//                    String cmd= "python "+directory.getCanonicalPath()+"/run.py";
                    System.out.println(cmd);
                    rt.exec(cmd);
                    //Process exec = rt.exec("python /Users/apple/Desktop/牛逼打包神器/AutomaticPackaging/run.py");
                    Process exec = rt.exec(cmd);

                    InputStream is = exec.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    String line;
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    exec.waitFor();
                    is.close();
                    reader.close();
                    exec.destroy();
                    actiontarget.setFill(Color.BLUE);
                    actiontarget.setText("牛逼!成功!");
                    imv.setImage(image);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        root.getChildren().add(grid);
        primaryStage.setScene(new Scene(root, 520, 400));
        primaryStage.show();
    }


    private void addChannel(String path,String channelList){
        try{

            File file =new File(path+"/channel.txt");
            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter fileWritter = new FileWriter(file.getName());
            fileWritter.write(channelList);
            fileWritter.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }


    private void getApkFromServer(){

    }

}
