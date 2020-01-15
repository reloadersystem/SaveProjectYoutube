package ar.reloadersystem.saveproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class YouTubeControls extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, YouTubePlayer.PlaybackEventListener{


    YouTubePlayerView youTubePlayerView;

    YouTubePlayer.PlayerStyle style;

    private YouTubePlayer mPlayer;

    String claveyoutube = "AIzaSyD9GZLfzFIdowg9dIJyb6jfgac3P6mRp1U";


    TextView play_time;

    ImageButton play_video, pause_video;

    SeekBar video_seekbar;

    String videoselect;

    private Handler mHandler = null;

    String API_KEY = "AIzaSyD9GZLfzFIdowg9dIJyb6jfgac3P6mRp1U";

    //String url="https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PLq_MGynXklvnlaed3VruS0dp1a7Qgpg4i&key=AIzaSyD9GZLfzFIdowg9dIJyb6jfgac3P6mRp1U";
    String url = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PLb7yWrygxdEEIEllgROgC05byO3QjIQV8&key=" + API_KEY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_controls);

        youTubePlayerView = findViewById(R.id.youtube_view);
        play_video = findViewById(R.id.play_video);
        pause_video = findViewById(R.id.pause_video);
        video_seekbar = findViewById(R.id.video_seekbar);
        play_time = findViewById(R.id.play_time);

        mHandler = new Handler();

        style = YouTubePlayer.PlayerStyle.MINIMAL;

        video_seekbar.setOnSeekBarChangeListener(mVideoSeekBarChangeListener);

        youTubePlayerView.initialize(claveyoutube, this);

        // listView.setOnItemClickListener(this);

        play_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (null != mPlayer && !mPlayer.isPlaying()) {
                    mPlayer.play();
                }
            }
        });

        pause_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != mPlayer && mPlayer.isPlaying()) {
                    mPlayer.pause();
                }

            }
        });
    }



    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean fueRestaurado) {


        mPlayer = youTubePlayer;

        youTubePlayer.setPlayerStateChangeListener(mPlayerStateChangeListener);
        youTubePlayer.setPlaybackEventListener(mPlaybackEventListener);

        if (!fueRestaurado) {

            youTubePlayer.setPlayerStyle(style);
            youTubePlayer.setShowFullscreenButton(false);


          //  youTubePlayer.cueVideo("CWXhL_mMZ9A");
           youTubePlayer.loadVideo("CWXhL_mMZ9A");
            //youTubePlayer.loadVideo("2OiSO1NaJoI");

            //Usa una lista de videos de  youtube x el id
//            List<String> videoList= new ArrayList<>();
//            videoList.add("RHcUU085kZc");
//            videoList.add("AK-BL5g6ETk");
//            videoList.add("x5fAvIsN1UA");
//            youTubePlayer.loadVideos(videoList);


            ///youTubePlayer.loadPlaylist("PLlEW_8OcYYBQBg5gqGlKcb4Ztp1QvUG_t");

            //  youTubePlayer.loadVideo(videoselect);

            //youTubePlayer.loadVideo("PLb7yWrygxdEEIEllgROgC05byO3QjIQV8");


            // youTubePlayer.loadPlaylist("PLq_MGynXklvnlaed3VruS0dp1a7Qgpg4i");


        }


    }

    YouTubePlayer.PlayerStateChangeListener mPlayerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {
        @Override
        public void onLoading() {

        }

        @Override
        public void onLoaded(String s) {

        }

        @Override
        public void onAdStarted() {

        }

        @Override
        public void onVideoStarted() {
            displayCurrentTime();

        }

        @Override
        public void onVideoEnded() {

        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {

        }
    };

    YouTubePlayer.PlaybackEventListener mPlaybackEventListener = new YouTubePlayer.PlaybackEventListener() {
        @Override
        public void onPlaying() {

            mHandler.postDelayed(runnable, 100);
            displayCurrentTime();

        }

        @Override
        public void onPaused() {
            mHandler.removeCallbacks(runnable);

        }

        @Override
        public void onStopped() {
            mHandler.removeCallbacks(runnable);

        }

        @Override
        public void onBuffering(boolean b) {

        }

        @Override
        public void onSeekTo(int arg0) {
            mHandler.postDelayed(runnable, 100);

        }
    };

    SeekBar.OnSeekBarChangeListener mVideoSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
            long lengthPlayed = (mPlayer.getDurationMillis() * progress) / 100;
            mPlayer.seekToMillis((int) lengthPlayed);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };


    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, 1).show();
        } else {
            String error = "Error al  iniciar Youtube" + youTubeInitializationResult.toString();

            Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
        }

    }

    // @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 1)
            getYoutubePlayerProvider().initialize(claveyoutube, this);
        {

        }

    }

    protected YouTubePlayer.Provider getYoutubePlayerProvider() {
        return youTubePlayerView;
    }


    @Override
    public void onPlaying() {

    }

    @Override
    public void onPaused() {

    }

    @Override
    public void onStopped() {

    }

    @Override
    public void onBuffering(boolean b) {

    }

    @Override
    public void onSeekTo(int i) {

    }




    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    private void displayCurrentTime() {
        if (null == mPlayer) return;
        String formattedTime = formatTime(mPlayer.getDurationMillis() - mPlayer.getCurrentTimeMillis());
        play_time.setText(formattedTime);
    }

    private String formatTime(int millis) {
        int seconds = millis / 1000;
        int minutes = seconds / 60;
        int hours = minutes / 60;


        //return  String.format("%02d:%02d", minutes % 60, seconds % 60);
        return (hours == 0 ? "--:" : hours + ":") + String.format("%02d:%02d", minutes % 60, seconds % 60);
    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            displayCurrentTime();
            mHandler.postDelayed(this, 100);
        }
    };


}
