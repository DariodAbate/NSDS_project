import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

public class TemperatureSensorFaultyActor extends TemperatureSensorActor {

	private ActorRef dispatcher;
	public final static int FAULT_TEMP = -50;

	@Override
	public AbstractActor.Receive createReceive() {
		return receiveBuilder().match(GenerateMsg.class, this::onGenerate)
				.match(SensorAddressMsg.class, this::onAddressMessage)
				.build();
	}
	private void onAddressMessage(SensorAddressMsg sensorAddressMessage) {
		dispatcher = sensorAddressMessage.getDispatcher();
	}

	private void onGenerate(GenerateMsg msg) {
		System.out.println("TEMPERATURE SENSOR "+self()+": Sensing temperature!");
		dispatcher.tell(new TemperatureMsg(FAULT_TEMP,self()), self());
	}

	static Props props() {
		return Props.create(TemperatureSensorFaultyActor.class);
	}

}
