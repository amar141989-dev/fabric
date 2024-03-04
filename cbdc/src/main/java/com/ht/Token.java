package com.ht;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.Objects;

@DataType()
public class Token {

    @Property()
    private String tokenId;

    @Property()
    private String value;

    @Property()
    private String owner;

    public Token (@JsonProperty("tokenId") final String tokenId,
                  @JsonProperty("value") final String value,
                  @JsonProperty("owner") final String owner) {
        this.tokenId = tokenId;
        this.value = value;
        this.owner = owner;
    }
    public String getTokenId() {
        return tokenId;
    }

    public String getValue() {
        return value;
    }

    public String getOwner() {
        return owner;
    }

    @Override
    public boolean equals(final Object obj) {
        if(this==obj) {
            return true;
        }

        if((obj ==null) || (getClass()!=obj.getClass())) {
            return false;
        }

        Token other=(Token)obj;

        return Objects.deepEquals(new String[]{getTokenId(), getValue(), getOwner()},
                new String[]{other.getTokenId(), other.getValue(), other.getOwner()});
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTokenId(), getValue(), getOwner());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "@"
                + Integer.toHexString(hashCode()) + " [tokenId=" + tokenId
                + ", value=" + value + ", owner=" + owner + "]";
    }
}
