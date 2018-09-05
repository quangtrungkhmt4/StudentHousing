package com.example.quang.studenthousing.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Favorite implements Serializable{

    @SerializedName("IDFAV")
    @Expose
    private int idFav;
    @SerializedName("IDUSER")
    @Expose
    private int idUser;
    @SerializedName("IDHOUSE")
    @Expose
    private int idHouse;

    public int getIDFAV() {
        return idFav;
    }

    public void setIDFAV(int iDFAV) {
        this.idFav = iDFAV;
    }

    public int getIDUSER() {
        return idUser;
    }

    public void setIDUSER(int iDUSER) {
        this.idUser = iDUSER;
    }

    public int getIDHOUSE() {
        return idHouse;
    }

    public void setIDHOUSE(int iDHOUSE) {
        this.idHouse = iDHOUSE;
    }

}