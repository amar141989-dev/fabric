package com.ht.cbdc.util;

import com.ht.cbdc.config.Org1Config;
import com.ht.cbdc.config.Org2Config;
import com.ht.cbdc.config.OrgConfig;

public class FabricHelper {
    public static OrgConfig getOrgConfig(String orgName) {
        switch(orgName) {
            case "org1":
                return new Org1Config();
            case "org2":
                return new Org2Config();
        }

        return null;
    }
}
