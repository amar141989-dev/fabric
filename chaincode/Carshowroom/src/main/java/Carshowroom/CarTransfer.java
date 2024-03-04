package Carshowroom;

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

@Contract(
        name = "CarShowroom",
        info = @Info(
                title = "CarShowroom contract",
                description = "A sample car showroom chaincode example",
                version="0.0.1-SNAPSHOT"
        )
)
@Default
public class CarTransfer implements ContractInterface {
        private final Genson genson = new Genson();

        private enum CarShowroomErrors {
                CAR_NOT_FOUND,
                CAR_ALREADY_EXISTS
        }

        {
                System.out.println("In carTransfer class..");
        }
        public CarTransfer() {
                System.out.println("In carTransfer constructor..");
        }

        @Transaction()
        public void initLedger(final Context context) {
                System.out.println("In carTransfer.initLedger..");
                ChaincodeStub stub=context.getStub();
                if(!stub.getMspId().equalsIgnoreCase("Org1MSP")) {
                        throw new ChaincodeException("Your organization is not allowed to invoke initLedger.", "ACCESS_NOT_ALLOWED");
                }
                Car car=new Car("1", "Maruti", "Mark", "6756");
                String carState=genson.serialize(car);
                stub.putStringState("1",carState);
        }

        @Transaction()
        public Car addNewCar(final Context context, final String id, final String model,
                             final String owner, final String value) {
                System.out.println("In carTransfer.addNewCar.."+id);
                ChaincodeStub stub=context.getStub();
                if(!stub.getMspId().equalsIgnoreCase("Org1MSP")) {
                        throw new ChaincodeException("Your organization is not allowed to add car.", "ACCESS_NOT_ALLOWED");
                }
                String carState=stub.getStringState(id);
                if(!StringUtil.isNullOrEmpty(carState)) {
                        String errorMessage = String.format("Car %s already exist", id);
                        System.out.println(errorMessage);
                        throw new ChaincodeException(errorMessage, CarShowroomErrors.CAR_ALREADY_EXISTS.toString());
                }
                Car car=new Car(id, model, owner, value);
                carState=genson.serialize(car);
                stub.putStringState(id, carState);
                return car;
        }

        @Transaction()
        public Car getCarById(final Context context, final String id) {
                System.out.println("In carTransfer.getCarById.."+id);
                ChaincodeStub stub=context.getStub();
                String carState=stub.getStringState(id);
                if(StringUtil.isNullOrEmpty(carState)) {
                        String errorMessage = String.format("Car %s does not exist", id);
                        System.out.println(errorMessage);
                        throw new ChaincodeException(errorMessage, CarShowroomErrors.CAR_NOT_FOUND.toString());
                }
                Car car=genson.deserialize(carState, Car.class);
                return car;
        }

        @Transaction()
        public Car changeCarOwnership(final Context context, final String id, final String newCarOwner) {
                System.out.println("In carTransfer.changeCarOwnership..");
                ChaincodeStub stub=context.getStub();
                String carState=stub.getStringState(id);
                if(StringUtil.isNullOrEmpty(carState)) {
                        String errorMessage = String.format("Car %s does not exist", id);
                        System.out.println(errorMessage);
                        throw new ChaincodeException(errorMessage, CarShowroomErrors.CAR_NOT_FOUND.toString());
                }
                Car car=genson.deserialize(carState, Car.class);
                Car newCar=new Car(car.getId(), car.getModel(), newCarOwner, car.getValue());
                String newCarState=genson.serialize(newCar);
                stub.putStringState(id, newCarState);
                return newCar;
        }
}
