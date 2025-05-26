package phbmediaplayer;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.util.Pair;

public class FXMLDocumentController implements Initializable {

    private MediaPlayer mediaPlayer;

    @FXML
    private MediaView mediaView;

    @FXML
    private StackPane sPane;

    @FXML
    private Button playPause;

    @FXML
    private Slider volume;

    @FXML Slider seek;

    @FXML
    BorderPane bPane;

    List<String> playlist = new ArrayList<>();
    List<String> sourceName = new ArrayList<>();
    static int INDEX, PLAY = 0;

    @FXML
    private void openFiles(ActionEvent event) {
        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(
            "Media File", "*.mp4", "*.mp3");
        fc.getExtensionFilters().add(filter);
        List<File> f = fc.showOpenMultipleDialog(null);
        if (!f.isEmpty()) {
            if (!playlist.isEmpty()) {
                playlist.clear();
                sourceName.clear();
            }
            for (int i = 0; i < f.size(); i++) {
                playlist.add(f.get(i).toURI().toString());
                sourceName.add(f.get(i).getName());
            }

            INDEX= 0;
            playMedia(INDEX);
        }
    }

    private void playMedia(int index) {
        String source = playlist.get(index);
        Media media = new Media(source);

        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.stop();
        }

        mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);
        mediaPlayer.setAutoPlay(true);

        DoubleProperty width = mediaView.fitWidthProperty();
        DoubleProperty height = mediaView.fitHeightProperty();
        width.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
        height.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));
        mediaView.setPreserveRatio(true);

        volume.setValue(50);
        volume.valueProperty().addListener((Observable observable) -> {
            mediaPlayer.setVolume(volume.getValue() / 100);
        });

        mediaPlayer.currentTimeProperty().addListener((Observable observable) -> {
            Duration current = mediaPlayer.getCurrentTime();
            Duration total = mediaPlayer.getTotalDuration();
            seek.setValue(current.toSeconds() / total.toSeconds() * 100);
        });

        seek.setOnMouseClicked((MouseEvent event1) -> {
            double seekTime = mediaPlayer.getTotalDuration().toSeconds() * seek.getValue() / 100;
            mediaPlayer.seek(Duration.seconds(seekTime));
        });

        mediaPlayer.play();
        Image imagePause = new Image(getClass().getResourceAsStream("/images/0.png"));
        playPause.setGraphic(new ImageView(imagePause));
    }

    @FXML
    private void seekBackward(ActionEvent event) {
        mediaPlayer.seek(Duration.seconds(0.5));
    }

    @FXML
    private void backward(ActionEvent event) {
        if (INDEX  > 0) {
            INDEX--;
            playMedia(INDEX);
        }
    }

    @FXML
    private void stop(ActionEvent event) {
        mediaPlayer.stop();
        Image imagePlay = new Image(getClass().getResourceAsStream("/images/5.png"));
        playPause.setGraphic(new ImageView(imagePlay));
        INDEX = 0;
    }

    @FXML
    private void pausePlay(ActionEvent event) {
        if (!playlist.isEmpty()) {
            if(PLAY == 1) {
                mediaPlayer.pause();
                Image imagePLay = new Image(getclass().getResourceAsStream("/images/5.png"));
                playPause.setGraphic(new ImageView(imagePLay));
                PLAY = 0;
            }else{
                mediaPlayer.pause();
                Image imagePlay = new Image(getClass().getResourceAsStream("/image/00.png"));
                String imagePause = null;
                playPause.setGraphic(new ImageView(imagePause));
                PLAY = 1;
            }
        }else{
            Dialog<Pair<String, String>> dialog = new Dialog<>();
            dialog.setTitle("message");
            dialog.setContentText("Please Open Media!!!");
            dialog.setOnCloseRequest((DialogEvent event1) -> {
                dialog.close();
            });
            dialog.initStyle(StageStyle.DECORATED);
            dialog.initModality(Modality.NONE);
            dialog.show();
        }
    }

    @FXML
    private void forward(ActionEvent event) {
        if (INDEX >= 0&& INDEX < playlist.size()) {
            INDEX++;
            playMedia(INDEX);
        }
    }

    @FXML
    private void seekForward(ActionEvent event) {
        mediaPlayer.setRate(1.5);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialization code if needed
}

    private Object getclass() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}