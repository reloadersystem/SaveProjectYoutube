package ar.reloadersystem.saveproject;

public class VideoDetails
{
    public String title, description, url;
    private String videoId;

    public VideoDetails(String title, String description, String url, String videoId) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.videoId = videoId;
    }


    public VideoDetails()
    {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
}
