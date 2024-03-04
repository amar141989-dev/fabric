package com.ht.cbdc.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ht.cbdc.record.Asset;
import io.grpc.Grpc;
import io.grpc.ManagedChannel;
import io.grpc.TlsChannelCredentials;
import org.hyperledger.fabric.client.Contract;
import org.hyperledger.fabric.client.Gateway;
import org.hyperledger.fabric.client.Gateway.Builder;
import org.hyperledger.fabric.client.identity.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.cert.CertificateException;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Service
public class CbdcService {

    private Contract contract = null;

    private final String assetId = "asset" + Instant.now().toEpochMilli();

    public String getAssetById(Long id, String orgId) throws Exception {

        try (var gateway = FabricService.createInstance(orgId).getBuilder().connect()) {
            var result = contract.evaluateTransaction("getCarById", id.toString());
            return new String(result);
        } catch (Exception e) {
            throw e;
        }
    }

    public String createAsset(Asset asset, String orgId) throws Exception {
        try (var gateway = FabricService.createInstance(orgId).getBuilder().connect()) {
            String[] params = new String[4];
            params[0] = asset.id();
            params[1] = asset.model();
            params[2] = asset.owner();
            params[3] = asset.value();
            var result = contract.submitTransaction("addNewCar", params);
            return new String(result);
        } catch (Exception e) {
            throw e;
        } finally {
            //channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
    }
}
