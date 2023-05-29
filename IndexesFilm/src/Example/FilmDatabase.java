package Example;

import Database.DatabaseException;
import Database.IndexedDatabase;
import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Calendar;

public class FilmDatabase extends IndexedDatabase<String,Film> {

    public FilmDatabase(String filename) {
        super(filename);
    }

    // Engado este método por comodidade
    public Film delete(String isan) throws DatabaseException {
        Film f=get(isan);
        if (f==null) throw new DatabaseException("FilmDatabase delete by isan: O rexistro non existe");
        delete(f);
        return f;
    }
    
    @Override
    public String getKey(Film data) {
        return data.getIsan();
    }

    @Override
    protected void remove(RandomAccessFile ras) throws DatabaseException {
        try {
            ras.writeChar('*');  // Un ISAN que comece por * indica rexistro borrado
        } catch (IOException ex) {
            throw new DatabaseException("FilmDatabase remove: "+ex.getMessage());
        }
    }

    @Override
    protected Film read(RandomAccessFile ras) throws DatabaseException {
        String isan; // International Standard Audiovisual Number
        String title;
        String director;
        String language;
        String genre;
        byte duration;
        Calendar launchDate;
        
        try {
            isan=ras.readUTF();
            title=ras.readUTF();
            director=ras.readUTF();
            language=ras.readUTF();
            genre=ras.readUTF();
            duration=ras.readByte();
            long time=ras.readLong();
            launchDate=Calendar.getInstance();
            launchDate.setTimeInMillis(time);
            return new Film(isan,title,director,language,genre,duration,launchDate);
        } catch(EOFException e) { 
            return null;
        } catch (IOException ex) {
            throw new DatabaseException("FilmDatabase read: "+ex.getMessage());
        }
        
    }

    @Override
    protected void write(RandomAccessFile ras, Film data) throws DatabaseException {
        String isan; // International Standard Audiovisual Number
        String title;
        String director;
        String language;
        String genre;
        byte duration;
        Calendar launchDate;
        
        try {
            ras.writeUTF(data.getIsan());
            ras.writeUTF(data.getTitle());
            ras.writeUTF(data.getDirector());
            ras.writeUTF(data.getLanguage());
            ras.writeUTF(data.getGenre());
            ras.writeByte(data.getDuration());
            ras.writeLong(data.getLaunchDate().getTimeInMillis());
        } catch (IOException ex) {
            throw new DatabaseException("FilmDatabase write: "+ex.getMessage());
        }
    }
    
    @Override
    public boolean isDeleted(Film f) {
        return (f.getIsan().charAt(0)=='*');
    }
}
