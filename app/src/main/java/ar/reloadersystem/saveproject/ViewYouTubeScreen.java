package ar.reloadersystem.saveproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import pe.sacooliveros.apptablet.ServiceHistorial.Estudiante;
import pe.sacooliveros.apptablet.ServiceHistorial.MainApplication;
import pe.sacooliveros.apptablet.Utils.HiloSecundario;
import pe.sacooliveros.apptablet.Utils.ShareDataRead;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewYouTubeScreen extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, YouTubePlayer.PlaybackEventListener {

    YouTubePlayerView youTubePlayerView;
    YouTubePlayer.PlayerStyle style;
    String claveyoutube = "AIzaSyBBoMapePV_AthrQPLWevncKB-RVw6QXtw";
    String id_channel;
    String listChanel;
    TextView play_time;
    ImageButton play_video, pause_video;
    SeekBar video_seekbar;
    private Handler mHandler = null;
    private YouTubePlayer mPlayer;
    String formattedTime;
    int count = 0;

    String titleID;
    String descripcionID;
    static String capitulo_canalID = " ";
    static String asignatura_canalID = " ";
    static String tomopdf_canalID = " ";
    String emailID;
    String dniID;
    String matriculaID;
    String unidadID;

    static String unidadSelected;
    static String moduloSelected = " ";

    //capitulo,materia,tomopdf
    public static void testTomoSelected(String capitulo, String materia, String tomo) {
        capitulo_canalID = capitulo;
        asignatura_canalID = materia;
        tomopdf_canalID = tomo;
    }

    public static void testUnidadSelected(String unidad) {
        unidadSelected = unidad; //Unidad 1
    }

    public static void moduloSelected(String modulo) {
        moduloSelected = modulo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_you_tube_screen);

        play_video = findViewById(R.id.play_video);
        pause_video = findViewById(R.id.pause_video);
        video_seekbar = findViewById(R.id.video_seekbar);
        play_time = findViewById(R.id.play_time);

        youTubePlayerView = findViewById(R.id.youtube_viewfullscreen);

        style = YouTubePlayer.PlayerStyle.MINIMAL;
        video_seekbar.setOnSeekBarChangeListener(mVideoSeekBarChangeListener);

        youTubePlayerView.initialize(claveyoutube, this);

        Bundle datos = this.getIntent().getExtras();

        id_channel = datos.getString("video_itemselec");
        listChanel = datos.getString("lista_canal");

        titleID = datos.getString("titleID");
        descripcionID = datos.getString("descripcionID");
//        capitulo_canalID = datos.getString("capitulo_canal"); // primaria null
//        asignatura_canalID = datos.getString("asignatura_canal"); // primaria null
//        tomopdf_canalID = datos.getString("tomopdf_canal"); // primaria null

        matriculaID = ShareDataRead.obtenerValor(getApplicationContext(), "MatriculaID");
        dniID = ShareDataRead.obtenerValor(getApplicationContext(), "dniusuario");
        emailID = ShareDataRead.obtenerValor(getApplicationContext(), "EMail");


        if (unidadSelected != null) {
            unidadID = unidadSelected;
        }

        //TipoGradoAsiste

        String sharedata = ShareDataRead.obtenerValor(getApplicationContext(), "TipoGradoAsiste");
        String gradoNivel = ShareDataRead.obtenerValor(getApplicationContext(), "ServerGradoNivel"); //5to PRe
        String gradoNombre = ShareDataRead.obtenerValor(getApplicationContext(), "GradoNombre"); //5to PRe


        if (moduloSelected.equalsIgnoreCase("VIDEO SEMINARIO")
                || sharedata.equalsIgnoreCase("CATOLICA") ||
                sharedata.equalsIgnoreCase("SAN MARCOS") ||
                sharedata.equalsIgnoreCase("SELECCION") ||
                sharedata.equalsIgnoreCase("PRE") && gradoNivel.equalsIgnoreCase("5 Secundaria") && gradoNombre.equalsIgnoreCase("Quinto Año")
        ) {
            Log.e("VIDEO_SEMINARIO", "Visor VIDEOS Cerrado");
        } else {
            HiloSecundario accion = new HiloSecundario() {
                @Override
                public void accion() {

                    //TODO ENVIAR SERVICIO  CONTEO  CON DATA  PONER EN UN HILO DIFERENTE

                    Estudiante user = new Estudiante(emailID, dniID, matriculaID, tomopdf_canalID, capitulo_canalID, asignatura_canalID, unidadID, listChanel,
                            id_channel, titleID, descripcionID);

                    MainApplication.apiManager.createUser(user, new Callback<Estudiante>() {
                        @Override
                        public void onResponse(Call<Estudiante> call, Response<Estudiante> response) {
                            Estudiante responseUser = response.body();

                            if (response.isSuccessful() && responseUser != null) {
                                Log.i("ENVIO_HISTORIAL_EXITOSO", responseUser.getMessage() + " " + responseUser.getStatus());
                            } else {
                                Log.i("ERROR_CODE_HISTORIAL", String.valueOf(response.code()));
                            }
                        }

                        @Override
                        public void onFailure(Call<Estudiante> call, Throwable t) {
                            Log.d("ERROR_FAILURE_HISTORIAL", t.getMessage());
                        }
                    });
                }
            };
            accion.start();
        }


        mHandler = new Handler();

//        YouTubePlayer.PlayerStyle style = YouTubePlayer.PlayerStyle.MINIMAL;

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
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer
            youTubePlayer, boolean fueRestaurado) {

        mPlayer = youTubePlayer;

        youTubePlayer.setPlayerStateChangeListener(mPlayerStateChangeListener);
        youTubePlayer.setPlaybackEventListener(mPlaybackEventListener);

        if (!fueRestaurado) {

            youTubePlayer.setPlayerStyle(style);
            youTubePlayer.setShowFullscreenButton(false);


            //youTubePlayer.cueVideo("RHcUU085kZc");
            youTubePlayer.loadVideo(id_channel);

            //Usa una lista de videos de  youtube x el id
//            List<String> videoList= new ArrayList<>();
//            videoList.add("RHcUU085kZc");
//            videoList.add("AK-BL5g6ETk");
//            videoList.add("x5fAvIsN1UA");
//            youTubePlayer.loadVideos(videoList);


            //youTubePlayer.loadPlaylist("PL6dvGWNWC1Uga4podWHHnSZQDyBFOsdhM");

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

            //mPlayer.seekToMillis(30000); inicia en el segundo 30
            //mPlayer.getCurrentTimeMillis();

            displayCurrentTime();

        }

        @Override
        public void onVideoEnded() {

            //Cuando Finalizo el Video Preguntas

//            ArrayList<mPruebas> mListPruebas;
//            mListPruebas = new ArrayList<mPruebas>();
//            PopupCustomDialog popupCustomDialog = new PopupCustomDialog();
//
//            mListPruebas.add(new mPruebas("Misioneros Franciscanos", "http://red.ilce.edu.mx/sitios/el_otono_2014/entrale/entrale_2000/pdf/jimmy.pdf"));
//            mListPruebas.add(new mPruebas("Corregidores e Intendente", "https://vercomics.com/batman-ano-uno-1/"));
//            mListPruebas.add(new mPruebas("Tributos y Mitas Mineras", "https://klimtbalan.wordpress.com/solo-para-fumadores-julio-ramon-ribeyro/"));
//            mListPruebas.add(new mPruebas("Repartos Mercantiles", "https://www.dropbox.com/s/y1pweeopm90qidk/Batman-La-broma-asesina_.pdf?dl=0#Batman-La-broma-asesina_.pdf"));
//
//            popupCustomDialog.customDialog(ViewYouTubeScreen.this, "Mitad del Siglo 20", mListPruebas);


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
    public void onInitializationFailure(YouTubePlayer.Provider
                                                provider, YouTubeInitializationResult youTubeInitializationResult) {

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
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(this, YouTubeViewer.class);
        intent.putExtra("video_itemselec", id_channel);
        intent.putExtra("lista_canal", listChanel);
        startActivity(intent);

        this.finish();


    }

    private void displayCurrentTime() {
        if (null == mPlayer) return;
        formattedTime = formatTime(mPlayer.getDurationMillis() - mPlayer.getCurrentTimeMillis());


//        if (formattedTime.equalsIgnoreCase("--:00:33") && count == 0) {
//            // Toast.makeText(ViewYouTubeScreen.this, "Estas en el minuto" + formattedTime, Toast.LENGTH_SHORT).show();
//            count = count + 1;
//
//
//            ArrayList<mPruebas> mListPruebas;
//            mListPruebas = new ArrayList<mPruebas>();
//            QuestionsPopup popupCustomDialog = new QuestionsPopup();
//            mListPruebas.add(new mPruebas("A)", "del atún y del bonito. "));
//            mListPruebas.add(new mPruebas("B)", "de la anchoveta y del guanay"));
//            mListPruebas.add(new mPruebas("C)", "de la pota y de la sardina."));
//            mListPruebas.add(new mPruebas("D)", "del fitoplancton y del zooplancton"));
//
//
//            popupCustomDialog.customDialog(getApplicationContext(), "La fauna de mayor importancia económica de la ecorregión del mar frío depende", mListPruebas);
//
//        }
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
