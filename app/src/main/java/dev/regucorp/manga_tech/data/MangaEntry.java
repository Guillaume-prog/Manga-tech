package dev.regucorp.manga_tech.data;

public class MangaEntry {

    public static final int BORROW = 0;
    public static final int LEND = 1;

    private int id;
    private final int type, startVolume, endVolume;
    private final String person, name;

    public MangaEntry(int type, String person, String name, int startVolume, int endVolume) {
        this.type = type;
        this.person = person;
        this.name = name;
        this.startVolume = startVolume;
        this.endVolume = endVolume;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public int getStartVolume() {
        return startVolume;
    }

    public int getEndVolume() {
        return endVolume;
    }

    public String getPerson() {
        return person;
    }

    public String getName() {
        return name;
    }
}
