package com.ht.cbdc.config;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Org1Config implements OrgConfig {
    private final String MSP_ID = "Org1MSP";
    private final String CHANNEL_NAME = "tokenchannel";
    private final String CHAINCODE_NAME = "Carshowroom";

    private final File file = new File("/Users/amarjeet.p_hoo/Projects/cbdc/fabric/fabric-samples/test-network/organizations/peerOrganizations/org1.example.com");
    private final Path CRYPTO_PATH = Paths.get(file.toURI());
    // Path to user certificate.
    private final Path CERT_PATH = CRYPTO_PATH.resolve(Paths.get("users/User1@org1.example.com/msp/signcerts/cert.pem"));
    // Path to user private key directory.
    private final Path KEY_DIR_PATH = CRYPTO_PATH.resolve(Paths.get("users/User1@org1.example.com/msp/keystore"));
    // Path to peer tls certificate.
    private final Path TLS_CERT_PATH = CRYPTO_PATH.resolve(Paths.get("peers/peer0.org1.example.com/tls/ca.crt"));

    // Gateway peer end point.
    private final String PEER_ENDPOINT = "localhost:7051";
    private final String OVERRIDE_AUTH = "peer0.org1.example.com";

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
