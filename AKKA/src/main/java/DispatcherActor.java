import akka.actor.*;
import akka.japi.pf.DeciderBuilder;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Map;

public class DispatcherActor extends AbstractActor{

	private static SupervisorStrategy strategy =
			new OneForOneStrategy(
					1,
					Duration.ofMinutes(1),
					DeciderBuilder.match(Exception.class, e -> SupervisorStrategy.resume()).build() //RESUME STRATEGY
			);

	private final static int NO_PROCESSORS = 2;
	private int indexRoundRobin = 0;
	private int indexLoadBalancing = 0;

	//associate to a processor a list of sensors
	private List<ActorRef> processors;
	private Map<ActorRef, List<ActorRef>> mapping = new HashMap<>();

	public DispatcherActor() {
		for(int i = 0; i < NO_PROCESSORS; i++ ){
			mapping.put(getContext().actorOf(SensorProcessorActor.props()), new ArrayList<>());
		}
		processors = new ArrayList<>(mapping.keySet());
	}

	@Override
	public SupervisorStrategy supervisorStrategy() {
		return strategy;
	}


	@Override
	public AbstractActor.Receive createReceive() {
		return loadBalancing(); 
	}

	private Receive loadBalancing() {
		return receiveBuilder()
				.match(DispatchLogicMsg.class, this::onDispatchLogicMsg)
				.match(TemperatureMsg.class, this::dispatchDataLoadBalancer)
				.build();
	}

	private void onDispatchLogicMsg(DispatchLogicMsg dispatchLogicMsg) {
		if(dispatchLogicMsg.getLogic() == 1) {
			getContext().become(loadBalancing());
			System.out.println("DISPATCHER: switched to load balancing");
		}else {
			getContext().become(roundRobin());
			System.out.println("DISPATCHER: switched to round robin");
		}
	}

	private Receive roundRobin() {
		return receiveBuilder()
				.match(DispatchLogicMsg.class, this::onDispatchLogicMsg)
				.match(TemperatureMsg.class, this::dispatchDataRoundRobin)
				.build();
	}

	private void dispatchDataLoadBalancer(TemperatureMsg msg) {
		addSensorToProcessor(msg.getSender());
		for (var listSensors:mapping.values()) {
			if(listSensors.contains(msg.getSender())){
				for (var processor:mapping.keySet()) {
					if(mapping.get(processor).equals(listSensors)) {
						processor.tell(msg, self());
						return;
					}
				}
				return;
			}
		}
	}

	private void dispatchDataRoundRobin(TemperatureMsg msg) {
		addSensorToProcessor(msg.getSender());
		processors.get(indexRoundRobin).tell(msg, self());
		indexRoundRobin++;
		if(indexRoundRobin >= processors.size())
			indexRoundRobin = 0;
	}

	private Boolean isNewSensor(ActorRef sender){
		List<ActorRef> sensors =
				mapping.values().stream()
						.flatMap(List::stream)
						.collect(Collectors.toList());
		return (!sensors.contains(sender));
	}

	private void addSensorToProcessor(ActorRef sender) {
		if(isNewSensor(sender)){
			mapping.get(processors.get(indexLoadBalancing)).add(sender);
			System.out.println("Added sender " + sender.toString() + " to processor "  + processors.get(indexLoadBalancing).toString());
			++indexLoadBalancing;
			if(indexLoadBalancing >= processors.size())
					indexLoadBalancing = 0;
		}
	}

	static Props props() {
		return Props.create(DispatcherActor.class);
	}
}
