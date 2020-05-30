package com.project.siternak.models.grafik;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GrafikModel {
    @SerializedName("label")
    @Expose
    private List<String> label;
    @SerializedName("data")
    @Expose
    private List<String> data;
    @SerializedName("jantan")
    @Expose
    private List<String> jantan;
    @SerializedName("betina")
    @Expose
    private List<String> betina;

    public GrafikModel(List<String> label, List<String> data, List<String> jantan, List<String> betina) {
        this.label = label;
        this.data = data;
        this.jantan = jantan;
        this.betina = betina;
    }

    public List<String> getLabel() {
        return label;
    }

    public void setLabel(List<String> label) {
        this.label = label;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public List<String> getJantan() {
        return jantan;
    }

    public void setJantan(List<String> jantan) {
        this.jantan = jantan;
    }

    public List<String> getBetina() {
        return betina;
    }

    public void setBetina(List<String> betina) {
        this.betina = betina;
    }
}
