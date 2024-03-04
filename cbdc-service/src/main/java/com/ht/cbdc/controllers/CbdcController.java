package com.ht.cbdc.controllers;

import com.ht.cbdc.record.Asset;
import com.ht.cbdc.services.CbdcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class CbdcController {
    @Autowired
    private CbdcService service;

    @GetMapping("/test")
    public ResponseEntity test() {
        return ResponseEntity.ok("It works");
    }

    @GetMapping(value = "/assets/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity GetAssests(@PathVariable Long id, @RequestHeader("org-id") String orgId) throws Exception {
        return ResponseEntity.ok(service.getAssetById(id, orgId));
    }

    @PostMapping(value = "/assets", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity GetAssests(@RequestBody Asset asset, @RequestHeader("org-id") String orgId) throws Exception {
        return ResponseEntity.ok(service.createAsset(asset, orgId));
    }
}