import akka.actor.ActorRef;

public class SensorAddressMsg {
    private ActorRef dispatcher;
    public SensorAddressMsg(ActorRef dispatcher){
        this.dispatcher = dispatcher;
    }

    public ActorRef getDispatcher() {
        return dispatcher;
    }
}
