package com.ht.cbdc.config;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Org2Config implements OrgConfig {
    private static final String MSP_ID = "Org2MSP";
    private static final String CHANNEL_NAME = "tokenchannel";
    private static final String CHAINCODE_NAME = "Carshowroom";

    private static final File file = new File("/Users/amarjeet.p_hoo/Projects/cbdc/fabric/fabric-samples/test-network/organizations/peerOrganizations/org2.example.com");
    private static final Path CRYPTO_PATH = Paths.get(file.toURI());
    // Path to user certificate.
    private static final Path CERT_PATH = CRYPTO_PATH.resolve(Paths.get("users/User1@org2.example.com/msp/signcerts/cert.pem"));
    // Path to user private key directory.
    private static final Path KEY_DIR_PATH = CRYPTO_PATH.resolve(Paths.get("users/User1@org2.example.com/msp/keystore"));
    // Path to peer tls certificate.
    private static final Path TLS_CERT_PATH = CRYPTO_PATH.resolve(Paths.get("peers/peer0.org2.example.com/tls/ca.crt"));

    // Gateway peer end point.
    private static final String PEER_ENDPOINT = "localhost:9051";
    private static final String OVERRIDE_AUTH = "peer0.org2.example.com";

    @Override
    public String getMspId() {
        return MSP_ID;
    }

    @Override
    public String getChannelName() {
        return CHANNEL_NAME;
    }

    @Override
    public String getChainCode() {
        return CHAINCODE_NAME;
    }

    @Override
    public Path getCertificatePath() {
        return CERT_PATH;
    }

    @Override
    public Path getKeyDirPath() {
        return KEY_DIR_PATH;
    }

    @Override
    public Path getTlsCertPath() {
        return TLS_CERT_PATH;
    }

    @Override
    public String getPeerEndpoint() {
        return PEER_ENDPOINT;
    }

    @Override
    public String getOverridePath() {
        return OVERRIDE_AUTH;
    }
}