package Example;

import java.util.Calendar;
import java.util.Objects;

public class Film {
    private final String isan; // International Standard Audiovisual Number
    private String title;
    private String director;
    private String language;
    private String genre;
    private byte duration;
    private Calendar launchDate;

    public Film(String isan, String title, String director, String language, String genre, int duration, Calendar launchDate) {
        this.isan = isan;
        this.title = title;
        this.director = director;
        this.language = language;
        this.genre = genre;
        this.duration = (byte) duration;
        this.launchDate = launchDate;
    }

    public String getIsan() {
        return isan;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public byte getDuration() {
        return duration;
    }

    public void setDuration(byte duration) {
        this.duration = duration;
    }

    public Calendar getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(Calendar launchDate) {
        this.launchDate = launchDate;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + Objects.hashCode(this.isan);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Film other = (Film) obj;
        if (!Objects.equals(this.isan, other.isan)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Film{" + "isan=" + isan + ", title=" + title + ", director=" + director + ", language=" + language + ", genre=" + genre + ", duration=" + duration + ", launchDate=" + launchDate + '}';
    }

}
