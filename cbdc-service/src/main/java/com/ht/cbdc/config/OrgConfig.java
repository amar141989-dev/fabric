package com.ht.cbdc.config;

import java.nio.file.Path;

public interface OrgConfig {
    String getMspId();

    String getChannelName();

    String getChainCode();

    Path getCertificatePath();

    Path getKeyDirPath();

    Path getTlsCertPath();

    String getPeerEndpoint();

    String getOverridePath();
}