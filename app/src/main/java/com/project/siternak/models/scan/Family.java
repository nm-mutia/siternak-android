package com.project.siternak.models.scan;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Family {
    @SerializedName("inst")
    @Expose
    private Instance inst;
    @SerializedName("parent")
    @Expose
    private List<Instance> parent;
    @SerializedName("sibling")
    @Expose
    private List<Instance> sibling;
    @SerializedName("child")
    @Expose
    private List<Instance> child;
    @SerializedName("gparent")
    @Expose
    private List<Instance> gparent;
    @SerializedName("gchild")
    @Expose
    private List<Instance> gchild;

    public Family(Instance inst, List<Instance> parent, List<Instance> sibling, List<Instance> child, List<Instance> gparent, List<Instance> gchild) {
        this.inst = inst;
        this.parent = parent;
        this.sibling = sibling;
        this.child = child;
        this.gparent = gparent;
        this.gchild = gchild;
    }

    public Instance getInst() {
        return inst;
    }

    public void setInst(Instance inst) {
        this.inst = inst;
    }

    public List<Instance> getParent() {
        return parent;
    }

    public void setParent(List<Instance> parent) {
        this.parent = parent;
    }

    public List<Instance> getSibling() {
        return sibling;
    }

    public void setSibling(List<Instance> sibling) {
        this.sibling = sibling;
    }

    public List<Instance> getChild() {
        return child;
    }

    public void setChild(List<Instance> child) {
        this.child = child;
    }

    public List<Instance> getGparent() {
        return gparent;
    }

    public void setGparent(List<Instance> gparent) {
        this.gparent = gparent;
    }

    public List<Instance> getGchild() {
        return gchild;
    }

    public void setGchild(List<Instance> gchild) {
        this.gchild = gchild;
    }
}
