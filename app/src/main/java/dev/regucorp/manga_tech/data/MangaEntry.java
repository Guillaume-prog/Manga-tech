package dev.regucorp.manga_tech.data;

import android.os.Parcel;
import android.os.Parcelable;

public class MangaEntry implements Parcelable {

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

    // Parcel constructor
    public MangaEntry(Parcel in) {
        this.type = in.readInt();
        this.person = in.readString();
        this.name = in.readString();
        this.numVolumes = in.readInt();
        this.owned = in.readString();
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
        return owned.length() - owned.replace("1", "").length();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeString(this.person);
        dest.writeString(this.name);
        dest.writeInt(this.numVolumes);
        dest.writeString(this.owned);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public MangaEntry createFromParcel(Parcel in) {
            return new MangaEntry(in);
        }

        public MangaEntry[] newArray(int size) {
            return new MangaEntry[size];
        }
    };
}
