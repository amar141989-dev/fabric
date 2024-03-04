package com.ht;

import com.owlike.genson.Genson;
import io.grpc.netty.shaded.io.netty.util.internal.StringUtil;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;

import java.util.List;

@Contract(
        name = "cbdc",
        info = @Info (
                title = "CBDC Token",
                description = "Token representing de-nomination",
                version = "1.0"
        )
)
@Default
public class TokenManager implements ContractInterface {
    private final Genson genson=new Genson();
    private enum CarShowroomErrors {
        TOKEN_NOT_FOUND,
        TOKEN_ALREADY_EXISTS,
        TRANSFER_
    }
    @Transaction()
    public void mintToken(final Context context, final String newOwner,
                          final String value, final String mintCount) {

        //If yes, then mint those token and return response
        System.out.println("In mintToken, newOwner: " + newOwner + ", value: " + value);

        ChaincodeStub stub = context.getStub();
        if(!stub.getMspId().equalsIgnoreCase("Org1MSP")) {
            throw new ChaincodeException("Your organization is not allowed to mint token.", "ACCESS_NOT_ALLOWED");
        }

        Long val = Long.parseLong(mintCount);

        for (int i = 0; i < val; i++) {
            Token token=new Token(System.currentTimeMillis()+""+i, value, newOwner);
            String tokenState=genson.serialize(token);
            stub.putStringState(token.getTokenId(), tokenState);
        }
    }

    @Transaction()
    public Token transferToken(final Context context, String tokenId, final String fromOwner, final String toOwner) {
        //TODO transfer token from one user to another
        //Check if user is owner of the token, only then transfer the token
        System.out.println("In transferToken(), tokenId: " + tokenId + ", fromOwner: " + fromOwner + ", toOwner: " + toOwner);
        ChaincodeStub stub=context.getStub();
        String tokenState=stub.getStringState(tokenId);
        if(StringUtil.isNullOrEmpty(tokenState)) {
            System.out.println("Token (" + tokenId + ") does not exist.");
            throw new ChaincodeException("Token (" + tokenId + ") does not exist.", "TOKEN_NOT_EXISTS");
        }
        Token token=genson.deserialize(tokenState, Token.class);
        return token;
    }

    @Transaction()
    public List<Token> getToken(final Context context, final String tokenId) {
        //TODO get list of tokens for a owner
        System.out.println("In getToken(), tokenId: " + tokenId);
        ChaincodeStub stub=context.getStub();
        String tokenState=stub.getStringState(tokenId);
        if(StringUtil.isNullOrEmpty(tokenState)) {
            System.out.println("Token (" + tokenId + ") does not exist.");
            throw new ChaincodeException("Token (" + tokenId + ") does not exist.", "TOKEN_NOT_EXISTS");
        }
        Token token=genson.deserialize(tokenState, Token.class);
        return token;
    }
}
