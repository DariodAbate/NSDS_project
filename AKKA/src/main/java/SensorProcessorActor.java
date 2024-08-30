import akka.actor.AbstractActor;
import akka.actor.Props;

public class SensorProcessorActor extends AbstractActor {

	private double currentAverage;
	private int count;

	@Override
	public Receive createReceive() {

		return receiveBuilder()
				.match(TemperatureMsg.class, this::gotData)
				.build();
	}

	private void gotData(TemperatureMsg msg) throws Exception {

		if(msg.getTemperature() < 0) {
			throw new Exception("ERROR " + self() + ": Got negative data from " + msg.getSender() +", resuming...");
		}

		count++;
		currentAverage = ((count - 1) * currentAverage + msg.getTemperature()) / count;

		System.out.println("SENSOR PROCESSOR " + self() + ": Got data from " + msg.getSender() + " Current avg is " + currentAverage);
	}

	static Props props() {
		return Props.create(SensorProcessorActor.class);
	}

	public SensorProcessorActor() {
		this.count = 0;
		this.currentAverage = 0;
	}
}
