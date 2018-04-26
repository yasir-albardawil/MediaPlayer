package net.baummar.Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.FadeTransition;
import javafx.application.Platform;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Tooltip;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.baummar.Util.DateTimeUtil;
import net.baummar.Util.FileUtilities;
import net.baummar.Util.ShowWindowsUtil;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class MediaPlayerController implements Initializable {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private MediaView mediaView;

    @FXML
    private AnchorPane controllers;

    @FXML
    private MenuBar menuBar;

    @FXML
    private JFXSlider volumeSlider;

    @FXML
    private Label labelStatus;

    @FXML
    private Label labelSpeed;

    @FXML
    private JFXSlider timeSlider;

    @FXML
    private FontAwesomeIconView volumeUpDown;

    @FXML
    private Tooltip toolTipMuteUnmute;

    @FXML
    private Label playTime;

    @FXML
    private Label playStatus;


    @FXML
    private JFXButton playButton;

    @FXML
    private JFXButton pauseButton;

    @FXML
    private JFXButton stopButton;

    private MediaPlayer mediaPlayer;
    private double getHeight;
    private Duration duration;



    @FXML
    void handleMenuItemOpen(ActionEvent event) {
        try {
            openMedia();
        } catch (MediaException e) {
            reset();
            playStatus.setText("Cannot render the file");
        }

    }

    @FXML
    void handleMenuItemClose(ActionEvent event) {
        reset();
    }

    @FXML
    void handleDecreaseSpeed(ActionEvent event) {
        if (mediaPlayer != null) {
            if (mediaPlayer.getRate() == 4) {
                mediaPlayer.setRate(2.00);
                labelSpeed.setText("Speed: 2.00x");
                setFadeTransition(labelSpeed);
            } else if (mediaPlayer.getRate() == 2) {
                mediaPlayer.setRate(1.00);
                labelSpeed.setText("Speed: 1.00x");
                setFadeTransition(labelSpeed);
            } else if (mediaPlayer.getRate() == 1) {
                mediaPlayer.setRate(0.50);
                labelSpeed.setText("Speed: 0.50x");
            } else if (mediaPlayer.getRate() == 0.50) {
                mediaPlayer.setRate(0.25);
                labelSpeed.setText("Speed: 0.25x");
                setFadeTransition(labelSpeed);
            } else {
                mediaPlayer.setRate(0.13);
                labelSpeed.setText("Speed: 0.13x");
                setFadeTransition(labelSpeed);
            }
        }
    }


    @FXML
    void handleIncreaseSpeed(ActionEvent event) {

        if (mediaPlayer != null) {
            if (mediaPlayer.getRate() == 1) {
                mediaPlayer.setRate(2);
                labelSpeed.setText("Speed: 2.00x");
                setFadeTransition(labelSpeed);
            } else if (mediaPlayer.getRate() == 2) {
                mediaPlayer.setRate(4);
                labelSpeed.setText("Speed: 4.00x");
                setFadeTransition(labelSpeed);
            } else if (mediaPlayer.getRate() == 0.50) {
                mediaPlayer.setRate(1.00);
                labelSpeed.setText("Speed: 1.00x");
                setFadeTransition(labelSpeed);
            } else if (mediaPlayer.getRate() == 0.25){
                mediaPlayer.setRate(0.50);
                labelSpeed.setText("Speed: 0.50x");
                setFadeTransition(labelSpeed);
            } else if (mediaPlayer.getRate() == 0.13) {
                mediaPlayer.setRate(0.25);
                labelSpeed.setText("Speed: 0.25x");
                setFadeTransition(labelSpeed);
            }

        }
    }


    @FXML
    void handleMuteUnmute(ActionEvent event) {

        if (mediaPlayer != null) {
            if (!mediaPlayer.isMute()) {
                mediaPlayer.setMute(true);
                toolTipMuteUnmute.setText("Unmute");
                volumeUpDown.setIcon(FontAwesomeIcon.VOLUME_OFF);
            } else {
                mediaPlayer.setMute(false);
                toolTipMuteUnmute.setText("Mute");

                volumeUpDown.setIcon(FontAwesomeIcon.VOLUME_UP);
            }
        }
    }

    @FXML
    void handleAboutMe(ActionEvent event) {
        ShowWindowsUtil.showWindows("About", "../../../views/about.fxml");
    }

    @FXML
    void handlePause(ActionEvent event) {
        if ((mediaPlayer != null)
                && (MediaPlayer.Status.PLAYING == getMediaPlayerStatus())) {
            mediaPlayer.pause();
            playStatus.setText("Paused");
            labelStatus.setText("Paused");
            setFadeTransition(labelStatus);
            playButton.getStyleClass().remove("selected");
            pauseButton.getStyleClass().add("selected");
            stopButton.getStyleClass().remove("selected");

        }
    }

    @FXML
    void handlePlay(ActionEvent event) {
        if ((mediaPlayer != null)
            && (MediaPlayer.Status.PAUSED == getMediaPlayerStatus()
            || MediaPlayer.Status.READY == getMediaPlayerStatus()
            || MediaPlayer.Status.STOPPED == getMediaPlayerStatus())) {
            mediaPlayer.play();
            labelStatus.setText("Play");

            setFadeTransition(labelStatus);
            playStatus.setText("Playing");

            playButton.getStyleClass().add("selected");
            pauseButton.getStyleClass().remove("selected");
            stopButton.getStyleClass().remove("selected");

            if (mediaPlayer.getCurrentTime().equals(duration)) {
                mediaPlayer.seek(Duration.ZERO);
                mediaPlayer.play();
            }
        }

    }

    @FXML
    void handleSkipBackward(ActionEvent event) {

    }

    @FXML
    void handleSkipForward(ActionEvent event) {

    }

    @FXML
    void handleStep(ActionEvent event) {

    }

    @FXML
    void handleStop(ActionEvent event) {
        if ((mediaPlayer != null)
                && (MediaPlayer.Status.PLAYING == getMediaPlayerStatus()
                || MediaPlayer.Status.PAUSED == getMediaPlayerStatus())) {
            mediaPlayer.stop();
            mediaPlayer.seek(Duration.ZERO);
            playStatus.setText("Stopped");
            labelStatus.setText("Stop");
            setFadeTransition(labelStatus);

            playButton.getStyleClass().remove("selected");
            pauseButton.getStyleClass().remove("selected");
            stopButton.getStyleClass().add("selected");
        }
    }

    @FXML
    void onDragOver(DragEvent event) {
        Dragboard dragboard = event.getDragboard();
        if (dragboard.hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY);
        } else {
            event.consume();
        }
    }

    @FXML
    void onDragDropped(DragEvent event) {
        Dragboard dragboard = event.getDragboard();
        if (dragboard.hasFiles()) {
            for (Path filePath : FileUtilities.convertListFileToListPath(dragboard.getFiles())) {
                try {
                    if (mediaView.getMediaPlayer() != null)
                        mediaPlayer.stop();
                        maximized(true);
                        playVideo(filePath.toAbsolutePath().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    void handleExit(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void onMouseClicked(MouseEvent mouseEvent) {
        if (mediaPlayer != null) {
            if (mouseEvent.getClickCount() == 2) {
                getStage().setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
                getStage().setFullScreen(!getStage().isFullScreen());

                if (getStage().isFullScreen()) {
                    setAnchorPaneConstrains(labelStatus, 10, 10);
                    hideShowElement(menuBar, false);

                } else {
                    setAnchorPaneConstrains(labelStatus, 35, 10);
                    hideShowElement(menuBar, true);
                    hideShowElement(controllers, true);
                }
            }
        }
    }

    @FXML
    private void onMouseMoved(MouseEvent mouseEvent) {
        double height = getHeight - mouseEvent.getSceneY();
        if (height <= 80) {
            if (isFullScreen()) {
                hideShowElement(controllers, true);
            }
        } else {
            if (isFullScreen()) {
                hideShowElement(controllers, false);

            }
        }
    }

    @FXML
    void onMouseClickedTimeSlider(MouseEvent event) {
        updateValues(mediaPlayer);
    }

    private void playVideo(String url_file) {
        try {
            String url = URLEncoder.encode(url_file, "UTF-8");
            url = "file:/"
                    + (url).replace("\\", "/").replace("+", "%20");
            Media media = new Media(url);

            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);
            mediaPlayer.setAutoPlay(true);
            Stage stage = getStage();
            stage.setTitle(Paths.get(url_file).getFileName().toString());
            resizeable(mediaView);
            playStatus.setText("Playing");
            labelStatus.setText(Paths.get(url_file).getFileName().toString());
            setFadeTransition(labelStatus);
            playButton.getStyleClass().add("selected");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void pausePlayVideo(MouseEvent event) {
        if (mediaPlayer != null) {
            pausePlayVideo();
        }
    }

    @FXML
    private void onKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.SPACE)
            if (mediaPlayer != null)
                pausePlayVideo();
    }

    private void pausePlayVideo() {
        MediaPlayer.Status status = mediaPlayer.getStatus();
        if (status == MediaPlayer.Status.PAUSED) {
            mediaPlayer.play();
            playStatus.setText("Playing");
            labelStatus.setText("Play");
            setFadeTransition(labelStatus);
        } else if (status == MediaPlayer.Status.PLAYING){
            mediaPlayer.pause();
            playStatus.setText("Paused");
            labelStatus.setText("Pause");
            setFadeTransition(labelStatus);
        }
    }

    private void setAnchorPaneConstrains(Node node, double top, double right, double bottom, double left) {
        AnchorPane.setTopAnchor(node, top);
        AnchorPane.setRightAnchor(node, right);
        AnchorPane.setBottomAnchor(node, bottom);
        AnchorPane.setLeftAnchor(node, left);
    }

    private void setAnchorPaneConstrains(Node node, double top, double left) {
        AnchorPane.setTopAnchor(node, top);
        AnchorPane.setLeftAnchor(node, left);

    }
    
    private boolean isFullScreen() {
        Stage stage = getStage();
        return stage.isFullScreen();
    }

    private void openMedia() {
        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Media files (All types)", "*.mp4", "*.mp3", "*.acc", "*.pcm", "*.vp6", "*.wav"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP4", "*.mp4, *.mp3, *avi"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP3", "*.mp3"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("ACC", "*.acc"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PCM", "*.pcm"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("H.264/AVC", "*.mp3"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("VP6", "*.vp6"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("WAV", "*.wav"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All files", "*.*"));

        File file =  fileChooser.showOpenDialog(getStage());
        if (file != null) {
            if (mediaPlayer == null) {
                maximized(true);
                playVideo(file.toString());
                bindMediaPlayerControls(mediaPlayer);
            } else {
                mediaPlayer.dispose();
                maximized(true);
                playVideo(file.toString());
                bindMediaPlayerControls(mediaPlayer);
            }
        }
    }

    private void reset() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }

        playStatus.setText(null);
        playTime.setText(null);
        timeSlider.setValue(-1);
        timeSlider.setDisable(true);
        getStage().setTitle("Media player");
    }

    private void maximized(boolean value) {
        Stage stage = getStage();
        stage.setMaximized(value);
    }

    private void resizeable(MediaView mediaView) {
        mediaView.fitWidthProperty().bind(rootPane.widthProperty());
        mediaView.fitHeightProperty().bind(rootPane.heightProperty());
    }


    private Stage getStage() {
        return (Stage) rootPane.getScene().getWindow();
    }

    private void bindMediaPlayerControls(final MediaPlayer mediaPlayer) {

        mediaPlayer.currentTimeProperty().addListener((ov) ->
            updateValues(mediaPlayer));

        mediaPlayer.setOnReady(() ->  {
            duration = mediaPlayer.getMedia().getDuration();
            updateValues(mediaPlayer);
        });

        timeSlider.valueProperty().addListener((ov) -> {
            if (timeSlider.isValueChanging()) {
                if (mediaPlayer != null)
                    // multiply duration by percentage calculated by
                    // slider position
                    mediaPlayer.seek(duration.multiply(timeSlider
                            .valueProperty()
                            .getValue() / 100.0));
                else
                    timeSlider.setValue(0);
            }
        });

        volumeSlider.valueProperty().addListener((ov) -> {
            if (mediaPlayer != null) {
                mediaPlayer.setVolume(volumeSlider
                        .valueProperty()
                        .getValue() / 100.0);
            }

        });
    }

    private void updateValues(MediaPlayer mediaPlayer) {
        if (mediaPlayer != null) {
            Platform.runLater(() -> {
                Duration currentTime = mediaPlayer.getCurrentTime();
                playTime.setText(DateTimeUtil.formatTime(currentTime, duration));
                timeSlider.setDisable(duration.isUnknown());
                if (!timeSlider.isDisabled()
                        && duration.greaterThan(Duration.ZERO)
                        && !timeSlider.isValueChanging()) {
                    timeSlider
                            .valueProperty()
                            .setValue(
                                    currentTime.divide(duration.toMillis()).toMillis() * 100.0);
                }

                if (!volumeSlider.isValueChanging()) {
                    volumeSlider.valueProperty()
                            .setValue(
                                    (int) Math.round(mediaPlayer
                                            .getVolume() * 100));

                } else {
                    labelStatus.setText("Volume: " + String.valueOf((int) Math.round(mediaPlayer
                            .getVolume() * 100)) + "%");
                    setFadeTransition(labelStatus);
                    if (volumeSlider.getValue() >= 50 && volumeSlider.getValue() <= 100) {
                        volumeUpDown.setIcon(FontAwesomeIcon.VOLUME_UP);
                    }

                    if (volumeSlider.getValue() < 50 && volumeSlider.getValue() > 0) {
                        volumeUpDown.setIcon(FontAwesomeIcon.VOLUME_DOWN);
                    }

                    if (volumeSlider.getValue() == 0) {
                        volumeUpDown.setIcon(FontAwesomeIcon.VOLUME_OFF);
                    }
                }
            });
        }
    }

    private void hideShowElement(Node node, boolean hideShow) {
        node.setVisible(hideShow);
    }

    private void setFadeTransition(Node node) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(5000), node);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.play();
    }

    private MediaPlayer.Status getMediaPlayerStatus() {
        return mediaPlayer.getStatus();
    }

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rootPane.layoutBoundsProperty().addListener((observable, oldValue, newValue) ->
                getHeight = newValue.getHeight());

        if (mediaPlayer != null)
            labelStatus.setText("Vol: " + volumeSlider.valueProperty().getValue() + "%");
    }
}
