
#! /bin/sh

./network.sh down
wait $! 
./network.sh up -ca -s couchdb
wait $!
./network.sh createChannel -c tokenchannel
wait $!
docker exec peer0.org1.example.com peer channel list
wait $!
source ~/.zprofile #in order to make below command work

#Configure org 1 
source ../../lifecycle_setup_org1.sh

# Package chain code
peer lifecycle chaincode package Carshowroom.tar.gz --path ../../chaincode/Carshowroom/build/install/Carshowroom --lang java --label Carshowroom_1 
wait $!
#Install chain code to org 1 peer 
peer lifecycle chaincode install Carshowroom.tar.gz --peerAddresses localhost:7051 --tlsRootCertFiles $CORE_PEER_TLS_ROOTCERT_FILE
wait $!
#Check if chain code installed in org 1
peer lifecycle chaincode queryinstalled --peerAddresses localhost:7051 --tlsRootCertFiles $CORE_PEER_TLS_ROOTCERT_FILE
wait $!
#package id is "name:hashcode"
export PACKAGE_ID=Carshowroom_1:1a5590eb6abfca17bd07d6f872aea0a7fb2d96145ea4995b949975d57fdf0442

#source ~/.zprofile
source ../../lifecycle_setup_org2.sh

#Install chain code to org 2 peer 
peer lifecycle chaincode install Carshowroom.tar.gz --peerAddresses localhost:9051 --tlsRootCertFiles $CORE_PEER_TLS_ROOTCERT_FILE
wait $!
#Check if chain code installed in org 2
peer lifecycle chaincode queryinstalled --peerAddresses localhost:9051 --tlsRootCertFiles $CORE_PEER_TLS_ROOTCERT_FILE
wait $!
#Approve installed chaincode for org 1
source ../../lifecycle_setup_org1.sh
peer lifecycle chaincode approveformyorg -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --tls --cafile $ORDERER_CA -C tokenchannel --name Carshowroom --version 1.0 --init-required --package-id $PACKAGE_ID --sequence 1
wait $!
#Approve installed chaincode for org 2
source ../../lifecycle_setup_org2.sh
peer lifecycle chaincode approveformyorg -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --tls --cafile $ORDERER_CA -C tokenchannel --name Carshowroom --version 1.0 --init-required --package-id $PACKAGE_ID --sequence 1
wait $!
#Commit, installed and approved chaincode for org 1
source ../../lifecycle_setup_org1.sh
peer lifecycle chaincode checkcommitreadiness -C tokenchannel --name Carshowroom --version 1.0  --sequence 1 --output json --init-required
wait $!
#Commit, installed and approved chaincode for org 2
source ../../lifecycle_setup_org2.sh
peer lifecycle chaincode checkcommitreadiness -C tokenchannel --name Carshowroom --version 1.0  --sequence 1 --output json --init-required
wait $!
source ../../lifecycle_setup_commit_channel.sh
wait $!
peer lifecycle chaincode commit -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --tls $CORE_PEER_TLS_ENABLED --cafile $ORDERER_CA -C tokenchannel --name Carshowroom --peerAddresses localhost:7051 --tlsRootCertFiles $CORE_PEER_TLS_ROOTCERT_FILE_ORG1 --peerAddresses localhost:9051 --tlsRootCertFiles $CORE_PEER_TLS_ROOTCERT_FILE_ORG2 --version 1.0 --sequence 1 --init-required
wait $!
peer chaincode invoke -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --tls $CORE_PEER_TLS_ENABLED --cafile $ORDERER_CA -C tokenchannel -n Carshowroom --peerAddresses localhost:7051 --tlsRootCertFiles $CORE_PEER_TLS_ROOTCERT_FILE_ORG1 --peerAddresses localhost:9051 --tlsRootCertFiles $CORE_PEER_TLS_ROOTCERT_FILE_ORG2 --isInit -c '{"Args":["initLedger"]}'
wait $!
# peer chaincode invoke -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --tls $CORE_PEER_TLS_ENABLED --cafile $ORDERER_CA -C tokenchannel -n Carshowroom --peerAddresses localhost:7051 --tlsRootCertFiles $CORE_PEER_TLS_ROOTCERT_FILE_ORG1 --peerAddresses localhost:9051 --tlsRootCertFiles $CORE_PEER_TLS_ROOTCERT_FILE_ORG2 -c '{"Args":["addNewCar","2","Ford","John","1000"]}'
# wait $!
# peer chaincode invoke -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --tls $CORE_PEER_TLS_ENABLED --cafile $ORDERER_CA -C tokenchannel -n Carshowroom --peerAddresses localhost:7051 --tlsRootCertFiles $CORE_PEER_TLS_ROOTCERT_FILE_ORG1 --peerAddresses localhost:9051 --tlsRootCertFiles $CORE_PEER_TLS_ROOTCERT_FILE_ORG2 -c '{"Args":["getCarById","2"]}'
# wait $!
