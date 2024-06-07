package com.itzroma.showme.domain;

import lombok.Getter;

@Getter
public enum FileType {
    AVATAR("avatars"),
    PREVIEW("previews"),
    VIDEO("videos");

    private final String folderName;

    FileType(String folderName) {
        this.folderName = folderName;
    }
}
