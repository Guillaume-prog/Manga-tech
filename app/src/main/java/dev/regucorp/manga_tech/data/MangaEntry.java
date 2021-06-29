package dev.regucorp.manga_tech.data;

public class MangaEntry {

    public static final int BORROW = 0;
    public static final int LEND = 1;

    private int id;
    private final int type, numVolumes;
    private final String person, name;
    private String owned;

    public MangaEntry(int type, String person, String name, int numVolumes) {
        this.type = type;
        this.person = person;
        this.name = name;
        this.numVolumes = numVolumes;

        owned = new String(new char[numVolumes]).replace('\0', '0');
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

    public int getNumVolumes() {
        return numVolumes;
    }

    public String getPerson() {
        return person;
    }

    public String getName() {
        return name;
    }


    public String getOwned() {
        return owned;
    }

    public void setOwned(String owned) {
        if(owned.length() == this.owned.length())
            this.owned = owned;
    }

    public int getNumOwned() {
        int num = 0;
        for(char c : owned.toCharArray()) {
            if(c == '1') num++;
        }
        return num;
    }
}
