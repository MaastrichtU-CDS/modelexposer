package com.carrier.modelexposer.webservice.domain;

import java.util.List;
import java.util.Map;

public class Comparison {
    private Map<String, String> changed;
    private List<Attribute> attributes;

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public Map<String, String> getChanged() {
        return changed;
    }

    public void setChanged(Map<String, String> changed) {
        this.changed = changed;
    }
}
