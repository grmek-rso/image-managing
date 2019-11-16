package com.grmek.rso.imagemanaging;

public class Info {

    private final String[] clani = {"rg6954"};
    private final String opis_projekta = "Portal za deljenje in analizo fotografij.";
    private final String[] mikrostoritve = {"http://35.224.218.28:8080/v1/users"};
    private final String[] github = {"https://github.com/grmek-rso/image-managing"};
    private final String[] travis = {"https://travis-ci.org/grmek-rso/image-managing"};
    private final String[] dockerhub = {"https://hub.docker.com/r/grmek/image-managing"};

    public String[] getClani() {
        return clani;
    }

    public String getOpis_projekta() {
        return opis_projekta;
    }

    public String[] getMikrostoritve() {
        return mikrostoritve;
    }

    public String[] getGithub() {
        return github;
    }

    public String[] getTravis() {
        return travis;
    }

    public String[] getDockerhub() {
        return dockerhub;
    }
}
