package com.example.mobileshop_mobileapp.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Mobile {
    public String id;
    public String imeMobitela;
    public String model;
    public String cijena;
    public String slika;

    public Mobile() {
    }

    public Mobile(String id, String imeMobitela, String model, String cijena, String slika) {
        this.id = id;
        this.imeMobitela = imeMobitela;
        this.model = model;
        this.cijena = cijena;
        this.slika = slika;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImeMobitela() {
        return imeMobitela;
    }

    public void setImeMobitela(String imeMobitela) {
        this.imeMobitela = imeMobitela;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCijena() {
        return cijena;
    }

    public void setCijena(String cijena) {
        this.cijena = cijena;
    }

    public String getSlika() {
        return slika;
    }

    public void setSlika(String slika) {
        this.slika = slika;
    }
}