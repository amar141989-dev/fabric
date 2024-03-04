package com.ht.cbdc.services;

import com.ht.cbdc.config.OrgConfig;
import com.ht.cbdc.util.FabricHelper;
import io.grpc.Grpc;
import io.grpc.ManagedChannel;
import io.grpc.TlsChannelCredentials;
import org.hyperledger.fabric.client.Contract;
import org.hyperledger.fabric.client.Gateway;
import org.hyperledger.fabric.client.identity.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class FabricService {

    private static Map<String, FabricService> orgService=new HashMap<>();
    private ManagedChannel channel = null;
    private Gateway.Builder builder = null;
    private OrgConfig orgConfig = null;

    private FabricService(String org) {
        this.orgConfig = FabricHelper.getOrgConfig(org);
    }

    private void initialize() throws Exception {
        // The gRPC client connection should be shared by all Gateway connections to
        // this endpoint.
        this.channel = newGrpcConnection();

        this.builder = Gateway.newInstance().identity(newIdentity()).signer(newSigner()).connection(channel)
                // Default timeouts for different gRPC calls
                .evaluateOptions(options -> options.withDeadlineAfter(5, TimeUnit.SECONDS))
                .endorseOptions(options -> options.withDeadlineAfter(15, TimeUnit.SECONDS))
                .submitOptions(options -> options.withDeadlineAfter(5, TimeUnit.SECONDS))
                .commitStatusOptions(options -> options.withDeadlineAfter(1, TimeUnit.MINUTES));
    }

    private ManagedChannel newGrpcConnection() throws IOException {
        var credentials = TlsChannelCredentials.newBuilder()
                .trustManager(this.orgConfig.getTlsCertPath().toFile())
                .build();
        return Grpc.newChannelBuilder(this.orgConfig.getPeerEndpoint(), credentials)
                .overrideAuthority(this.orgConfig.getOverridePath())
                .build();
    }

    private Identity newIdentity() throws IOException, CertificateException {
        var certReader = Files.newBufferedReader(this.orgConfig.getCertificatePath());
        var certificate = Identities.readX509Certificate(certReader);

        return new X509Identity(this.orgConfig.getMspId(), certificate);
    }

    private Signer newSigner() throws IOException, InvalidKeyException {
        var keyReader = Files.newBufferedReader(getPrivateKeyPath());
        var privateKey = Identities.readPrivateKey(keyReader);

        return Signers.newPrivateKeySigner(privateKey);
    }

    private Path getPrivateKeyPath() throws IOException {
        try (var keyFiles = Files.list(this.orgConfig.getKeyDirPath())) {
            return keyFiles.findFirst().orElseThrow();
        }
    }

    public Gateway.Builder getBuilder() {
        return this.builder;
    }

    public Contract getContract(Gateway gateway) {
        var network = gateway.getNetwork(this.orgConfig.getChannelName());
        Contract contract = network.getContract(this.orgConfig.getChainCode());
        return contract;
    }

    public static FabricService createInstance(String org) throws Exception {
        if(orgService.get(org)==null) {
            FabricService service = new FabricService(org);
            service.initialize();
            orgService.put(org, service);
        }
        return orgService.get(org);
    }
}
