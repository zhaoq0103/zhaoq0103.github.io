package com.ai.x.scan.init;

public class SpecifyInitBean {

    String appTitle;

    String appVersion;

    public String appName;

    SpecifyInitBean(String appTitle) {
        this(appTitle, "1.0");
    }
    SpecifyInitBean(String appTitle, String appVersion) {
        this.appTitle = appTitle;
        this.appVersion = appVersion;
    }

    void init() {
        this.appName = this.appTitle + " = " + this.appVersion;
    }
}
